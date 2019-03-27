[TOC]

# 事务

redis中支持事务，并且使用一组命令来操作事务。事务拥有以下特点：

- 事务中的命令都被排序并且严格按照顺序来执行；
- redis在执行一个事务的时候，不会执行其他的命令请求，这样保证了事务的独立性；
- 事务中的命令要么全部执行，要么全不执行；
- `exec`命令才会真正的触发事务开启，在此之前如果连接断开那么所有命令都不会执行，否则所有命令都会执行；



## 用法

redis中使用`multi`命令来标记事务的开始，该命令总是返回`OK`状态。在此之后可以键入多个命令，但是这些命令并不会被执行，而是被添加到队列中，所以之后的命令都会返回`QUEUED`状态。当使用`exec`命令后，队列中的所有命令都会一次执行，该命令返回一个数组，每条都代表相应命令的执行结果。例如：

```
> MULTI
OK
> INCR foo
QUEUED
> INCR bar
QUEUED
> EXEC
1) (integer) 1
2) (integer) 1
```



## 错误

事务中可能出现以下两种类型的错误：

- 在`exec`命令执行之前出现的错误。例如插入队列中的命令存在语法错误，或者服务器内存不足等；
- `exec`命令后命令执行出错。例如对键执行了错误的操作(对set进行`incr`操作)；

当出现第一种错误时，redis根据将命令插入队列时的返回信息：如果成功返回QUEUED，如果错误返回error。如果出现错误，客户端会直接弃用该事务。但是从2.6.5开始，服务器会记住这个错误但并不会立即弃用事务，而是会在执行`exec`命令的时候返回一个错误信息并且弃用事务。例如：

```
MULTI
+OK
INCR a b c
-ERR wrong number of arguments for 'incr' command
```

当出现第二种错误时，redis会记录错误信息并且继续执行下面的任务，`exec`命令后的错误不会中断事务的执行，执行完毕后会返回响应的错误信息。例如：

```
MULTI
+OK
SET a 3
abc
+QUEUED
LPOP a
+QUEUED
EXEC
*2
+OK
-ERR Operation against a key holding the wrong kind of value
```

## 回滚

redis中**不支持回滚**操作，有以下几点原因：

- redis事务出现错误一般是因为语法问题，这些问题通常代表我们的程序代码有问题，也就是开发中出现的问题，只要程序代码正确生产中一般不会出现问题；
- 不使用回滚的话redis可以优化内部实现，使得执行速度更快；



## 弃用事务

可以使用`discard`命令来弃用当前的事务。例如：

```
> SET foo 1
OK
> MULTI
OK
> INCR foo
QUEUED
> DISCARD
OK
> GET foo
"1"
```



## 乐观锁

redis事务中使用`watch`命令来提供check-and-set(CAS)行为。使用`watch`标记过的key状态为watched，当其值发生变化时会被服务器检测到。如果在执行`exec`命令时检测到watched的key发生变化，则当前事务会被弃用，`exec`命令会返回一个null值。例如：

```
WATCH mykey
val = GET mykey
val = INCR mykey
MULTI
SET mykey $val
EXEC
```

假设当前有两个客户端，客户端1执行了如上操作，另客户端2在其执行`exec`命令之前执行了`INCR mykey`操作，那么客户端1的命令结果如下所示：

![https://github.com/yancongcong1/blog/tree/master/redis/static/images/transactions.png](https://github.com/yancongcong1/blog/tree/master/redis/static/images/transactions.png)

> NOTE：前面说过redis在执行一个事务时不会执行其他的命令，请于这边的watch命令做理解区分。

### Watch解析

`watch`可以用来监听key的变化，其用来作为事务执行的条件：如果所有被监听的key都没有变化，那么事务可以执行，否则将会被弃用。注意如果监听了一个拥有过期时间的key，如果执行事务之前其key过期，那么事务仍然会执行，[详情](http://code.google.com/p/redis/issues/detail?id=270)。

`watch`可以被执行多次，也可以在一次中监听多个key。但是请注意当`exec`命令被执行后，无论事务是否被执行，所有被监听的key都会变为unwatched状态。当然如果我们在执行事务之前发现需要改变监听key，可以通过`unwatch`命令来取消监听所有的key。

## Scripting and Transactions

redis的script被定义为事务的，也就是说你利用事务进行的操作都可以使用script来进行，并且通常脚本编辑起来会更加简单、执行速度更加快。
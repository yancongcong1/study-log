[TOC]

# REDIS-CLI

redis-cli是redis目录下的一个可执行程序，提供一个接口以便终端可以向redis服务端发送命令以及接受服务端的响应信息。

redis-cli提供两种模式来让用户访问redis服务：

- 交互式：redis-cli中支持REPL(Read Eval Print Loop，交互式解释器)，它可以让用户给服务端发送命令并且将响应信息显示到标准输出
- 命令行：redis-cli将命令以参数的形式发送给服务端并且将响应信息显示到标准输出

redis-cli有许多启动选项，你可以通过这些选项来进入特殊模式中，这些参数使得redis-cli可以执行更多复杂的任务，例如：

- 模拟从服务器并且打印从主服务器接收到的复制流数据
- 检查服务器的延迟信息并显示统计数据



## 可选项

下面我们介绍部分较重要的可选项，详细信息请输入`redis-cli --help`查看：

| 选项                 | 含义                                                         |
| -------------------- | ------------------------------------------------------------ |
| -h `<hostname>`      | redis服务器的主机名，默认为127.0.0.1                         |
| -p `<port>`          | redis服务端口，默认为6379                                    |
| -s `<socket>`        | redis服务套接字，这个选项会覆盖`-h`和`-p`选项                |
| -a `<password>`      | redis服务器的密码                                            |
| -u `<uri>`           | redis服务的合法uri，这个选项类似`-h`和`-p`结合使用           |
| -r `<repeat>`        | 重复执行n次命令                                              |
| -i `<interval>`      | 当使用`-r`命令时，指定命令的执行时间间隔，单位为秒，可以通过0.1这种<br/>方式来指定毫秒级单位 |
| -n `<db>`            | 数据库序号                                                   |
| -x                   | 从标准输出读取上一个参数                                     |
| -d `<delimiter>`     | 定义分隔符，默认为\n                                         |
| -c                   | 启用集群模式                                                 |
| --raw                | 使用响应的原始格式(当标准输出不是tty的时候默认为该格式)      |
| --no-raw             | 当标准输出不是tty时强制使用非原始格式                        |
| --csv                | 输出CSV格式                                                  |
| --slave              | 模拟从服务器显示主服务器接收到的命令                         |
| --rdb `<filename>`   | 从服务器下载RDB存储文件                                      |
| --pipe               | 从标准输入将原始的redis协议传给服务器                        |
| --pipe-timeout `<n>` | 在`--pipe`模式中，设置超时时间，如果n秒内没有收到回复，则报错从而中<br/>断命令，默认为30秒，0表示永不超时 |
| --scan               | 使用SCAN命令列出所有的key                                    |
| --pattern `<pat>`    | 指定SCAN的模式                                               |
| --eval `<file>`      | 将Lua脚本文件当作EVAL命令发送给服务器                        |
| --ldb                | 使用`--eval`选项的时候开启redis的Lua调试器                   |
| --ldb-sync-mode      | 使用`--eval`选项的时候开启redis的同步Lua调试器               |
| --version            | 输出redis版本信息                                            |
| --help               | 输出命令详细信息                                             |

redis-cli命令的一些使用用例：

```
  cat /etc/passwd | redis-cli -x set mypasswd
  redis-cli get mypasswd
  redis-cli -r 100 lpush mylist x
  redis-cli -r 100 -i 1 info | grep used_memory_human:
  redis-cli --eval myscript.lua key1 key2 , arg1 arg2 arg3
  redis-cli --scan --pattern '*:12345*'
```



## 命令行模式

命令行模式中我们将想要执行的命令当作参数传递给`redie-cli`，例如：

```
$ redis-cli incr mycounter
(integer) 7
```

我们可以看到命令返回结果为：(interger)7，redis中不同命令的返回值以及类型都不同，具体参考命令详细信息。在redis中返回值分为5种类型：

- string
- array
- interger
- null(nil)
- error

### 连接服务器

redis默认连接到`localhost(127.0.0.1)`的`6379`端口，你可以通过以下命令连接到任意redis服务器：

```
$ redis-cli -h redis15.localnet.org -p 6390 ping
PONG
```

如果服务器设置了密码，可以这样来连接：

```
$ redis-cli -a myUnguessablePazzzzzword123 ping
PONG
```

该选项跟`AUTH`命令功能一样。

redis默认会连接序号为0的数据库，我们可以通过下面的命令在连接时指定数据库：

```
$ redis-cli -n 1
```

当然，你也可以通过设置URI来指明之前说过的部分或者全部连接信息，例如：

```
$ redis-cli -u redis://p%40ssw0rd@redis-16379.hosted.com:16379/0 ping
PONG
```

### 更换标准输出

redis的返回信息默认返回到tty(终端)，如果我们想要将返回信息输出到指定文件，可以使用UNIX输出重定向的方式：

```
$ redis-cli incr mycounter > /tmp/output.txt
$ cat /tmp/output.txt
8
```

我们之前在`redis-cli`选项中就介绍过，如果输出不是tty，将会使用响应的raw格式(也就是不返回类型)，我们可以通过添加`--no-raw`来改变这种情况。

### 更换标准输入

有时候我们可能会需要从其他程序获取输入作为命令的参数，我们有两种方式达成目标：

1. `-x`选项以及UNIX输入重定向

   ```
   $ redis-cli -x set foo < /etc/services
   OK
   $ redis-cli getrange foo 0 50
   "#\n# Network services, Internet style\n#\n# Note that "
   ```

   通过`-x`选项以及UNIX输入重定向将/etc/services文件中的内容作为`set`命令的参数使用。

2. 通过UNIX的管道命令

   ```
   $ cat /tmp/commands.txt
   set foo 100
   incr foo
   append foo xxx
   get foo
   $ cat /tmp/commands.txt | redis-cli
   OK
   (integer) 101
   (integer) 6
   "101xxx"
   ```

   `|`命令的作用是将上一个命令的结果作为参数传递给下一个命令。**我们发现使用这个方式我们可以同时执行多条命令(这边的意思是在一次连接中执行多个命令)。**

### 连续执行同样的命令

我们在选项中介绍了`-r`选项用来将一个命令重复执行多次，具体用法如下：

```
$ redis-cli -r 5 -i 1 incr foo
(integer) 1
(integer) 2
(integer) 3
(integer) 4
(integer) 5
```

我们使用`-r`指定重复次数，使用`-i`指定执行时间间隔。如果想要将命令重复执行无数次，可以将`-r`设置成-1。

### 运行Lua脚本

我们可以通过--eval选项来执行Lua脚本，例如：

```
$ cat /tmp/script.lua
return redis.call('set',KEYS[1],ARGV[1])
$ redis-cli --eval /tmp/script.lua foo , bar
OK
```

更多详细信息请参考[文档](https://redis.io/topics/ldb)。



## 交互模式

如果我们想要进入redis-cli的交互模式，我们只需要在运行redis-cli时不添加任何参数就行了，当然你可以使用任何你需要的选项：

```
$ redis-cli
127.0.0.1:6379> ping
PONG
```

在交互模式中，用户在提示符处输入redis命令，命令被发送到服务端，然后经过处理返回响应，相应信息被解析后呈现到标准输出中供用户阅读。这个连接会长时间保存，所以用户可以跟服务器进行多次交互。

上面的提示符`127.0.0.1:6379`表示当前客户端连接到了本地的6379端口上提供的redis服务，并且连接到了序号为`0`的数据库。如果你想要改变这些信息，可以通过追加命令行选项的方式，当然你也可以通过redis提供的一些命令来达到目的。例如：

```
127.0.0.1:6379> select 2
OK
127.0.0.1:6379[2]> dbsize
(integer) 1
127.0.0.1:6379[2]> select 0
OK
127.0.0.1:6379> dbsize
(integer) 503
```

### 重复执行相同的命令

交互模式中如果我们想要重复执行相同的命令只需在想要执行的命令之前加上想要重复的次数就行了：

```
127.0.0.1:6379> 5 incr mycounter
(integer) 1
(integer) 2
(integer) 3
(integer) 4
(integer) 5
```

> 注意：交互模式中不能无限重复执行命令，重复的次数必须大于等于1。

### 使用help命令

在交互模式中有时候我们忘记了一些命令，我们需要使用`help`来查看这些命令。交互模式提供两种help方式：

1. 查询某种类型的所有相关命令

   我们可以使用`help @<category>`来查询指定功能的所有相关命令，redis提供以下功能种类：

   -  `@generic`
   -  `@string`
   -  `@list`
   -  `@set`
   -  `@sorted_set`
   -  `@hash`
   -  `@pubsub`
   -  `@transactions`
   -  `@connection`
   -  `@server`
   -  `@scripting`
   -  `@hyperloglog`
   -  `@cluster`
   -  `@geo`

   从名字我们就能很方便的分辨出这些不同种类命令的作用。

2. 直接查询指定命令的详细信息

   如果我们想要查询`PFADD`命令，可以这样做：

   ```
   127.0.0.1:6379> help PFADD
   
     PFADD key element [element ...]
     summary: Adds the specified elements to the specified HyperLogLog.
     since: 2.8.9
     group: hyperloglog
   ```

### 清屏

在交互模式中可以使用`clear`命令来清除屏幕内容



## 特殊操作

上面我们介绍了如何使用redis-cli操作命令与服务器交互，接下来我们简单介绍一下redis-cli的一些特殊模式。redis-cli支持一些特殊的模式，这些模式都是通过`可选项`来实现的，它们可以完成以下功能：

- 监听服务端状态
- 扫描redis库中的超大的key
- 根据输入规则列出当前库中存在的key
- 发布/订阅模式(会有单独的章节进行讨论)
- 显示redis实例接收到的所有命令
- 监听服务器延迟
- 检查本地计算机的调度延迟
- 进行RDB备份
- 模拟从库并显示接收到的信息
- 模拟LRU工作负载
- 作为Lua的调试客户端

[详细信息](https://redis.io/topics/rediscli#special-modes-of-operation)
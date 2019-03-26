[TOC]

# Performance Optimiza

接下来我们简单了解一下redis性能提升的一些小技巧。



## 使用pipeline

### 原理

redis使用C/S模型，客户端发起一次请求的基本流程如下：

1. 客户端向服务端发送命令，进入阻塞状态，准备从socket读取响应信息；
2. 服务端处理命令并且返回响应信息。

当客户端发起一个命令到接收到服务端响应的这个时间称为RRT(Round Trip Time)。假设一次请求的RRT为250ms，试想以下如果我们发送100万条请求需要浪费多少时间，同理如果我们在一次请求中发起了多条命令，那么我们就会省下相应的RRT。

利用pipeline提升redis性能的地方不仅仅在RRT这一方面，它同时可以提高redis服务器每秒执行的操作数。众所周知，计算机中的I/O操作是非常昂贵的，当我们每次发起请求时都会调用操作系统的read()和write()方法进行I/O操作，每次请求都会涉及到操作系统层面的上下文切换，这是非常耗时的。当使用pipeline时，可以通过一个read()方法读取多条命令，使用一个write()方法返回多个响应，这样节省了操作系统切换上下文的时间，使得每秒查询的总数随着pipeline的长度呈现线性增长：

![image](http://redis.io/images/redisdoc/pipeline_iops.png)

### 如何操作

- 使用pipeline

  您可以使用`|`来执行管道操作命令：

  ```
  $ (printf "PING\r\nPING\r\nPING\r\n"; sleep 1) | nc localhost 6379
  +PONG
  +PONG
  +PONG
  ```

- 使用script

  script可以用来提升查询速度，使用script时会将script脚本发送到redis服务端，然后直接执行redis脚本命令，它在读取和写入数据时的延迟非常小，所以可以快速的执行读取、计算和写入这些操作。

  关于script用法请阅读command的scripting部分。


## 优化数据结构

详细信息请参考[文档](https://redis.io/topics/memory-optimization)



## 使用index

[详细信息](https://redis.io/topics/indexes)
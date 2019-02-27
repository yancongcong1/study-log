# NC

### 介绍

nc命令几乎可以用于所有涉及tcp和udp的程序。它可以开启tcp连接，发送udp数据，监听tcp和udp端口，做端口扫描，同时支持IPv4和IPv6协议。

与telnet不同，nc可以很好的将错误消息分离到标准错误中，而不是像telnet那样将错误消息发送到标准输出中。

### 语法及参数

语法：

```
nc [-46bCDdhklnrStUuvZz] [-I length] [-i interval] [-O length] 
   [-P proxy_username] [-p source_port] [-q seconds] [-s source] 
   [-T toskeyword] [-V rtable] [-w timeout] [-X proxy_protocol] 
   [-x proxy_address[:port]] [destination] [port]
```

重要参数：

| 参数                 | 含义                                                         |
| -------------------- | ------------------------------------------------------------ |
| **-4**               | 只允许使用IPv4地址                                           |
| **-6**               | 只允许使用IPv6地址                                           |
| **-d**               | 禁用标准输入留stdin                                          |
| **-i** *interval*    | 指定发送和接受文本之间的实践间隔，同时制定连接多端口时的时间间隔 |
| **-l**               | 指定nc用来监听传入连接，而不是发起连接。不能与-p、-s或者-z一起使用。同时会是-w选项失效 |
| **-n**               | 不在指定的地址、主机名或者端口上就行DNS或服务查找            |
| **-p** *source_port* | Specifies the source port nc should use, subject to privilege restrictions and availability. |
| **-u**               | 使用UDP协议，默认为TCP协议                                   |
| **-v**               | 让nc给出更详细的输出                                         |
| **-w** *timeout*     | 设置超时时间                                                 |
| **-z**               | 指定nc只能扫描进程而不会向它们发送数据，不能与-l一起使用     |
| **-U**               | Specifies to use UNIX-domain sockets.                        |

destination可以使ip地址或者主机名称，对UNIX套接字来说，destination必须被指定。

port可以使单个端口或者一组端口。可以通过nn-mm的格式来指定端口范围。通常必须给出端口号除非使用-U参数。

### 使用

nc命令有多种用途：

- 监听本地或者远端服务端口
- 传输数据
- 与服务器交互
- 端口扫描

我们主要介绍一下端口扫描功能：

端口扫描主要用来检测服务端端口是否开放并且有服务在其上运行。-z可以指定nc只报告端口信息而不是打开一个连接，-v可以用来获取详细的输出信息。例如：

```
nc -zv host.example.com 20-30
```

```
Connection to host.example.com 22 port [tcp/ssh] succeeded!
Connection to host.example.com 25 port [tcp/smtp] succeeded!
```

该服务器的20-30端口会被递增的检查，你也可以指定一个端口列表，例如：

```
nc -zv host.example.com 80 20 22
```

```
nc: connect to host.example.com 80 (tcp) failed: Connection refused 
nc: connect to host.example.com 20 (tcp) failed: Connection refused 
Connection to host.example.com port [tcp/ssh] succeeded! 
```

另外，有时候可能需要获取端口运行的服务和版本。这些信息通常都包含在握手信息中，所以需要先建立一个连接，然后当受到握手信息后中断连接。这可以通过-w指定一个稍短的超时时间或者通过向服务端发送"QUIT"指令来做到：

```
echo "QUIT" | nc host.example.com 20-30
```

```
SSH-1.99-OpenSSH_3.6.1p2 Protocol mismatch. 
220 host.example.com IMS SMTP Receiver Version 0.84 Ready
```

[参考文档](https://www.computerhope.com/unix/nc.htm)

[man文档](https://linux.die.net/man/1/nc)






















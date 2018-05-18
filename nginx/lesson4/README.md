[TOC]



# nginx.conf配置详解(持续更新ing)

nginx配置系统由一个主配置文件和一些辅助配置文件组成。这些配置文件都是纯文本文件，都位于conf文件夹下面。

只要启动nginx就会加载主配置文件，辅助配置文件只是在特定的情况下才会使用。

在配置文件中有若干的配置项，每一个配置项都由配置指令和指令参数构成。指令参数也就是指令对应的配置值。



## 指令

配置指令是一个字符串，可以用单引号或者双引号括起来，可以不括起来。但是如果配置指令包含空格，一定要括起来。



## 指令参数

指令的参数使用一个或者多个空格或者TAB字符与指令分开。指令的参数有一个或者多个TOKEN串组成。TOKEN串之间由空格或者TAB键分隔。

TOKEN串分为**简单字符串**或者是**复合配置块**。复合配置块即是由大括号括起来的一堆内容。一个复合配置块中可能包含若干其他的配置指令。

### 简单字符串

如果一个配置指令的参数全部由简单字符串构成，也就是不包含复合配置块，那么我们就说这个配置指令是一个简单配置项，否则称之为复杂配置项。例如下面这个是一个简单配置项：

```
error_page   500 502 503 504  /50x.html;
```

对于简单配置，配置项的结尾使用分号结束。

### 复合配置块

对于复合配置项，包含多个TOKEN串的，一般都是简单TOKEN串放在前面，复合配置块一般位于最后，而且其结尾，并不需要再添加分号。例如下面这个复合配置项：

```
location / {
    root   /home/jizhao/nginx-book/build/html;
    index  index.html index.htm;
}
```



## 指令上下文

nginx.conf中的配置信息，根据其逻辑上的意义，对它们进行了分类，也就是分成了多个作用域，或者称之为配置指令上下文。不同的作用域含有一个或者多个配置项。

nginx比较常用几个指令上下文：

|   上下文   | 描述                                                         |
| :--------: | :----------------------------------------------------------- |
|   main:    | nginx在运行时与具体业务功能（比如http服务或者email服务代理）无关的一些参数，比如工作进程数，运行的身份等。 |
|   http:    | 与提供http服务相关的一些配置参数。例如：是否使用keepalive啊，是否使用gzip进行压缩等。 |
|  server:   | http服务上支持若干虚拟主机。每个虚拟主机一个对应的server配置项，配置项里面包含该虚拟主机相关的配置。在提供mail服务的代理时，也可以建立若干server.每个server通过监听的地址来区分。 |
| location:  | http服务中，某些特定的URL对应的一系列配置项。                |
|   mail:    | 实现email相关的SMTP/IMAP/POP3代理时，共享的一些配置项（因为可能实现多个代理，工作在多个监听地址上）。 |
| upstream： | 负载均衡相关的配置。                                         |

指令上下文，可能有包含的情况出现。例如：通常http上下文和mail上下文一定是出现在main上下文里的。在一个上下文里，可能包含另外一种类型的上下文多次。例如：如果http服务，支持了多个虚拟主机，那么在http上下文里，就会出现多个server上下文。

我们来看一个示例配置：

```
user  nobody;
worker_processes  1;
error_log  logs/error.log  info;

events {
    worker_connections  1024;
}

http {
    server {
        listen          80;
        server_name     www.linuxidc.com;
        access_log      logs/linuxidc.access.log main;
        location / {
            index index.html;
            root  /var/www/linuxidc.com/htdocs;
        }
    }

    server {
        listen          80;
        server_name     www.Androidj.com;
        access_log      logs/androidj.access.log main;
        location / {
            index index.html;
            root  /var/www/androidj.com/htdocs;
        }
    }
}

mail {
    auth_http  127.0.0.1:80/auth.php;
    pop3_capabilities  "TOP"  "USER";
    imap_capabilities  "IMAP4rev1"  "UIDPLUS";

    server {
        listen     110;
        protocol   pop3;
        proxy      on;
    }
    server {
        listen      25;
        protocol    smtp;
        proxy       on;
        smtp_auth   login plain;
        xclient     off;
    }
}
```
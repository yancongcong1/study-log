[TOC]



# nginx作为http服务器

nginx可以作为各种服务器使用，本节简单介绍一下nginx如何作为http服务器使用。
nginx作为http服务器的功能需要添加[ngx_http_core_module](http://nginx.org/en/docs/http/ngx_http_core_module.html)模块。




## nginx如何分配请求

通常情况下，配置文件中可能包含被listen和server_name区分的几个server块。一旦nginx决定了由那个server处理请求，它就会将请求头中的url和server块中的location进行匹配了，从而返回相应的资源。

当接收到一个请求时，nginx首先匹配server块中的listen配置，如果是匹配成功；那么nginx会将server_name和请求头中的Host信息进行匹配，如果匹配成功，则使用该服务处理请求，否则使用该IP和端口的默认服务处理请求。

不同的IP和端口可以有不同的default_server。

```
server {
    listen      192.168.1.1:80;
    server_name example.org www.example.org;
    ...
}

server {
    listen      192.168.1.1:80 default_server;
    server_name example.net www.example.net;
    ...
}

server {
    listen      192.168.1.2:80 default_server;
    server_name example.com www.example.com;
    ...
}
```



## 完整配置文件

```
http {
    server {
    	listen       80;
        server_name  admin.xxx.top www.admin.xxx.top;

        # charset koi8-r;

        # access_log  logs/host.access.log  main;

        root html;

        location = / {
            index  index.html;
        }

        location ~* \.(html|js|css|gif|png|ico|jpg|jpeg|svg|woff|woff2|ttf|eot|ijmap)$ {
            access_log off;
            expires 30d;
        }
    }
}
```

http服务需要我们添加http上下文，其中的server上下文提供处理请求的相关信息。



## 上下文中的指令介绍

- **http上下文**

> - *server*——配置虚拟服务器。基于IP的虚拟服务和基于NAME的虚拟服务之间没有明确的界限划分。通过listen指令配置IP虚拟服务，通过server_name指令配置NAME虚拟服务。



- **server上下文**

> - *listen*——服务器的IP和端口，可以配置基于IP的虚拟服务。可以配置为**(IP的地址和端口)**|**(端口)**|**(unix主机的套接字(支持正则表达式))**。虚拟服务只接受与配置匹配的请求。
>
>   ```
>   listen 127.0.0.1:8000;
>   listen 127.0.0.1;
>   listen 8000;
>   listen *:8000;
>   listen localhost:8000;
>   
>   // IPV6的地址在中括号中指定
>   listen [::]:8000;
>   listen [::1];
>   
>   // UNIX套接字使用(unix:)前缀指定
>   listen unix:/var/run/nginx.sock;
>   ```
>
>   如果没有指定端口号，那么默认的端口号为80。
>
>   如果没有配置listen指令，那么如果使用root超级用户权限运行nginx，该指令默认为`*:80`，否则默认为 `*:8000`。
>
>   **default_server参数设置：可以在listen中设置默认服务，如果所有的IP都匹配失败，请求会使用默认的服务处理，如果该参数没有被设置，那么nginx认为第一个server为默认的服务(**0.8.21 版本后可以简化为default**)**。
>
>   详细信息请参考：[官方文档](http://nginx.org/en/docs/http/ngx_http_core_module.html#listen)
>
> - *server_name*——匹配请求的请求头Host部分，是服务器的名称(域名)或者IP地址。可以是精确的名称、通配符或者正则表达式。
>
>   如果多个server_name被匹配，那么nginx会按照下面的规则来选择：
>
>   1. 精确名称；
>   2. 以`*`开头的最长通配符名称；
>   3. 以`*`结尾的最长通配符名称；
>   4. 第一个匹配到的正则表达式。
>
>   关于server_name各种形式的详细信息请参考[文档](http://nginx.org/en/docs/http/server_names.html)。
>
>   ```
>   server {
>       listen       80;
>       server_name  example.org  www.example.org;
>       ...
>   }
>   
>   server {
>       listen       80;
>       server_name  *.example.org;
>       ...
>   }
>   
>   server {
>       listen       80;
>       server_name  mail.*;
>       ...
>   }
>   
>   server {
>       listen       80;
>       server_name  ~^(?<user>.+)\.example\.net$;
>       ...
>   }
>   ```
>
> - *root*——配置请求的根目录，可以在`http`, `server`, `location` 中，对同级别的所有请求生效。
>
>   ```
>   location /i/ {
>       root /data/w3;
>   }
>   对于/i/a.jpg和/i/test/b.jpg来说，它们的根目录都是/data/w3。
>   ```
>
>   值可以包含变量，除了`$document_root` 和 `$realpath_root`。 
>
> - *location*——设置请求uri的匹配规则。nginx首先对uri进行解码(%XX之类)，然后会解析相对路径./和../。以及将多个相邻的/压缩。
>
>   匹配规则可以是`前缀字符串`或者`正则表达式`。正则表达式由前缀修饰符`~*`和`~`来表示。
>
>   ```
>   匹配顺序：
>   1. nginx首先匹配前缀字符串的规则，它会记住匹配到的最长的规则；
>   2. 然后，ngxin会顺序的匹配正则表达式，它会选择第一个匹配到的正则表达式；
>   3. 如果没有匹配到正则表达式，它会使用之前记住的前缀字符串。
>   
>   NOTE:
>   1. 如果带^~修饰符的前缀字符串已经被匹配，那么正则表达式就不会被匹配了；
>   2. 修饰符=可以进行精确匹配(只匹配与字符串完全相同的url)，如果匹配成功的话，匹配将会被终止。
>   这样做可以加速匹配速度。(在0.7.1 至 0.8.41之间，如果没有添加=或者^~，如果前缀字符串成功匹配的话，正则表达式也不会进行匹配了)
>   ^~和=一般用来修饰前缀字符串
>   ```
>
>   eg:
>
>   ```
>   location = / {
>       [ configuration A ]
>   }
>   
>   location / {
>       [ configuration B ]
>   }
>   
>   location /documents/ {
>       [ configuration C ]
>   }
>   
>   location ^~ /images/ {
>       [ configuration D ]
>   }
>   
>   location ~* \.(gif|jpg|jpeg)$ {
>       [ configuration E ]
>   }
>   
>   "/"	
>   "/index.html"
>   "/documents/document.html"
>   "/images/1.gif"
>   "/documents/1.jpg"
>   ```
>
>   详细信息请参考：[官方文档](http://nginx.org/en/docs/http/ngx_http_core_module.html#location)
>

还有许多其他的指令，本节只是介绍重要的指令，[官方文档](http://nginx.org/en/docs/http/ngx_http_core_module.html)。
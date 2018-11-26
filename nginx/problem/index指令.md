[TOC]



# nginx配置代理后无法访问首页

## 问题描述

当nginx配置代理后，如果没有配置静态资源的访问控制，将无法正确路由到首页。配置如下：

```
server {

    listen       80;
    server_name  127.0.0.1;

    root /root/client;

    location = / {
        index  index.html;
    }

    # location ~* \.(html|js|css|gif|png|ico|jpg|jpeg|svg|woff|woff2|ttf|eot|ijmap)$ {
    #     access_log off;
    #     expires 30d;
    # }

    location / {
        proxy_pass   http://127.0.0.1:8080/;
    }

}
```

这时候访问http://127.0.0.1会出现502错误。

## 问题分析

通过对access.log日志的分析，发现最终请求为http://127.0.0.1:8080/index.html。这说明请求其实被代理模块处理，为什么会出现这个问题呢，根据location指令的定位规则应该的确是由第一个location配置进行处理啊？

找了半天也没有找到问题出现在哪里，突然发现了第一个location指令中的index指令，感觉会是这个的问题，于是从官网文档的到以下信息：

> Defines files that will be used as an index. The file name can contain variables. Files are checked in the specified order. The last element of the list can be a file with an absolute path. Example:
>
> ```
> index index.$geo.html index.0.html /index.html;
> ```
>
> It should be noted that **using an index file causes an internal redirect**, and the request can be processed in a different location. For example, with the following configuration:
> 
> ```
> location = / {
>     index index.html;
> }
> 
> location / {
>     ...
> }
> ```
>
> a “/” request will actually be processed in the second location as “/index.html”.

注意标黑体的信息，这边index指令帮我们做了一个重定向工作，也就是我们实际访问的url为：http://127.0.0.1/index.html。这样的话根据location匹配规则来看的确是由代理location配置来处理请求的，所以在这种情况下必须配置静态资源的请求处理，也就是以上配置中被注解掉的部分。
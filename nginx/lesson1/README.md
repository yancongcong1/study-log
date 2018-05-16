[TOC]



# nginx简介



## nginx起源

Nginx是俄罗斯人编写的十分轻量级的HTTP服务器,Nginx，它的发音为“engine X”， 是一个高性能的HTTP和反向代理服务器，同时也是一个IMAP/POP3/SMTP 代理服务器．Nginx是由俄罗斯人 Igor Sysoev为俄罗斯访问量第二的 Rambler.ru站点开发的，它已经在该站点运行超过两年半了。Igor Sysoev在建立的项目时,使用基于BSD许可。



## nginx应用现状

Nginx 已经在俄罗斯最大的门户网站── Rambler Media（www.rambler.ru）上运行了3年时间，同时俄罗斯超过20%的虚拟主机平台采用Nginx作为反向代理服务器。 在国内，已经有 淘宝、新浪博客、新浪播客、网易新闻、六间房、56.com、Discuz!、水木社区、豆瓣、YUPOO、海内、迅雷在线 等多家网站使用 Nginx 作为Web服务器或反向代理服务器。



## nginx特点

- 跨平台：Nginx 可以在大多数 Unix like OS编译运行，而且也有Windows的移植版本。
- 配置异常简单，非常容易上手。配置风格跟程序开发一样，神一般的配置
- 非阻塞、高并发连接：数据复制时，磁盘I/O的第一阶段是非阻塞的。官方测试能够支撑5万并发连接，在实际生产环境中跑到2～3万并发连接数.(这得益于Nginx使用了最新的epoll模型)
- 事件驱动：通信机制采用epoll模型，支持更大的并发连接。
- master/worker结构：一个master进程，生成一个或多个worker进程
- 内存消耗小：处理大并发的请求内存消耗非常小。在3万并发连接下，开启的10个Nginx 进程才消耗150M内存（15M*10=150M） 
- 成本低廉：Nginx为开源软件，可以免费使用。而购买F5 BIG-IP、NetScaler等硬件负载均衡交换机则需要十多万至几十万人民币
- 内置的健康检查功能：如果 Nginx Proxy 后端的某台 Web 服务器宕机了，不会影响前端访问。
- 节省带宽：支持 GZIP 压缩，可以添加浏览器本地缓存的 Header 头。
- 稳定性高：用于反向代理，宕机的概率微乎其微




## nginx用途

- http服务器
- 反向代理服务器
- 负载均衡服务器



## 拓展

1. 正向代理和反向代理

   网上比较不错的文章：

   - https://www.cnblogs.com/Anker/p/6056540.html
   - https://blog.csdn.net/m13666368773/article/details/8060481
   - https://www.jianshu.com/p/208c02c9dd1d

   > **NOTE**:可能刚开始了解正向代理和反向代理的时候会比较懵，感觉这两个货色完全就是一样的东西吗！不错，正向代理和反向代理在功能上都是一样的，都是代理客户端和目标服务器之前的请求和响应。它们的区别是代理的方式和主要作用的对象：
   >
   > - 正向代理需要进行配置，客户端需明确告诉代理服务器自己想要访问的源服务器(也就是说客户端知道自己访问的是代理服务器而不是源服务器)。因此代理服务器可以对源服务器保护客户端信息或者限制客户端的一些访问。
   > - 反向代理无需客户端进行多余的配置，客户端以为自己访问的是源服务器(其实它访问的是代理服务器)。因此代理服务器可以对客户端保护源服务器的信息。

   ​


[^燕聪聪]: 唯天下之至拙，能胜天下之至巧

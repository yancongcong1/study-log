[TOC]

# redis配置

当我们安装好redis之后，可以通过`redis-server`来启动redis服务，redis服务的一些配置包含在redis.conf文件中，如果我们想要修改其中的某些配置可以通过修改该文件来达到目的。

关于redis配置字段的说明在redis.conf文件中有着详细描述。

除此之外还有一些修改配置的方式，我们会一一说明。

## 命令行选项

从2.6版本开始，redis支持在redis-server命令后面添加可选项的方式来修改配置。例如：

```
./redis-server --port 6380 --slaveof 127.0.0.1 6379
```

这个格式只是在指令前面加上了`--`，指令和redis.conf文件中没有任何区别。

## 运行时修改

redis允许在服务启动后修改配置。redis提供的`config set`和`config get`命令来修改和读取当前的配置。但是这种方式并不支持所有的配置选项，只有部分配置可以通过`config set`命令来修改。

值得注意的是使用这种方式来修改redis配置并不会直接修改redis.conf配置文件，如果我们想要将配置同步到redis.conf文件中，我们可以使用`config rewrite`命令。

## 配置缓存

如果你设计的redis中的每个key都有expire time，那么你可以通过设置最大内存的方式来配置缓存：

```
maxmemory 2mb
maxmemory-policy allkeys-lru
```

在这种方式下你无需设置过期时间，当内存使用超过2M后，所有的key都会以LRU算法来进行清除。这种配置下的reids工作方式与memcached类似。

详细信息请参考[文档](https://redis.io/topics/lru-cache)
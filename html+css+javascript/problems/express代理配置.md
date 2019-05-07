# express代理配置

首先查看了express相关配置，发现其代理实现其实使用了插件[http-proxy-middleware](https://github.com/chimurai/http-proxy-middleware)，这个插件又封装了[node-http-proxy](https://github.com/nodejitsu/node-http-proxy)。

反向代理功能很简单，只需要配置proxyTable或者router就可以实现。

正向代理通过查找文档发现如下几个配置可能和正向代理有关：

```
option.forward: url string to be parsed with the url module

option.agent: object to be passed to http(s).request (see Node's https agent and http agent objects)

option.xfwd: true/false, adds x-forward headers

option.toProxy: true/false, passes the absolute URL as the path (useful for proxying to proxies)

option.changeOrigin: true/false, Default: false - changes the origin of the host header to the target URL
```

但是http-proxy-middleware没有正向代理相关的示例，在node-http-proxy中找到了[示例代码|<https://github.com/nodejitsu/node-http-proxy/blob/master/examples/http/forward-and-target-proxy.js>]。

根据示例代码最终自己的配置如下(部分关键配置)：

```
var proxyServer = process.env.http_proxy || process.env.HTTP_PROXY)

module.exports = {
  dev: {
    proxyTable: {
      '/api/v1': {
        target: {
          host: 'host',
          port: 80
        },
        changeOrigin: true,
        forward: proxyServer ? {
          host: proxyServer,
          port: 808
        } : '',
        toProxy: proxyServer ? true : false, 
        onError: function onError(err, req, res) {
          console.log(err)
        }
      }
    }
  }
}
```

但是运行时报错，根据报错位置，修改node_module中代码添加console输出(可能调试不够规范)，最后发现这边target和forward不能这么配置。而是需要配置成字符串形式，并且需要完整的(protocal+hostname+port)形式，重新阅读文档后发现如下：

```
option.target: url string to be parsed with the url module

option.forward: url string to be parsed with the url module
```

注意是url string，但是文档表现和示例输出明显不一致是什么情况就不知道了？？？照此修改后配置如下：

```
var proxyServer = process.env.http_proxy || process.env.HTTP_PROXY)

module.exports = {
  dev: {
    proxyTable: {
      '/api/v1': {
        target: 'http://host:port',
        changeOrigin: true,
        forward: proxyServer ? proxyServer : '',
        toProxy: proxyServer ? true : false,
        onError: function onError(err, req, res) {
          console.log(err)
        }
      }
    }
  }
}
```

运行后之前的报错问题解决，但是执行登录接口代理服务器返回错误信息如下：

```
[HPM] Error occurred while trying to proxy request /api/v1/login from localhost:4000 to http://host:port (ECONNRESET) (https://nodejs.org/api/errors.html#errors_common_system_errors) 
```

此时接口可以正常代理成功。

查阅两个插件的issue，找到相关issue如下：

- <https://github.com/chimurai/http-proxy-middleware/issues/211>
- <https://github.com/nodejitsu/node-http-proxy/issues/1160>
- <https://github.com/nodejitsu/node-http-proxy/issues/832>
- <https://github.com/chimurai/http-proxy-middleware/issues/22>
- <https://github.com/nodejitsu/node-http-proxy/issues/1160>

从而发现了另外一种方式来配置正向代理，修改后配置如下：

```
var proxyServer = process.env.http_proxy || process.env.HTTP_PROXY)
var ProxyAgent = require('http-proxy-agent')

module.exports = {
  dev: {
    proxyTable: {
      '/api/v1': {
        target: 'http://host:port',
        changeOrigin: true,
        agent: new ProxyAgent(proxyServer),
        onError: function onError(err, req, res) {
          console.log(err)
        }
      }
    }
  }
}
```

重新启动服务调用登录接口，不返回任何错误信息，接口代理成功。






































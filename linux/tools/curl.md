[TOC]

# CURL

curl是一种可以对URL执行各种操作以及传输的命令行工具，可以通过`curl --help` 或 `curl --manual`命令来查看curl的一些帮助信息。

当然curl不是万能的，它可以发起请求、接收数据、发送数据以及检索信息，但是它仍然有许多无法完成的事情。所以你可能需要使用某种脚本语言或者重复的手动调用来获取完整的数据。



## curl协议相关

curl客户端一般使用HTTP协议，下面我们简单介绍一些有用的调试参数

### 查看协议信息

默认情况下curl只会显示服务器返回的内容，如果你想要查看发送请求以及收到响应的具体信息，可以使用`--verbose`参数，如果这个参数还是没有满足你的需求的话，请使用`--trace`或者`--trace-ascii`参数，这两个参数会返回请求响应的所有内容(注意会保存在文件中，一般在终端运行目录)。

### 分析时间

如果你想要分析请求响应中的时间占用问题，可以使用--trace-time参数，同样这会在终端运行目录生成一个文件。

### 改变响应输出

curl命令默认所有输出为标准输出流(stdout)，你可以通过`-o`或者`-O`参数来使其输出到指定文件中。



## 发起HTTP请求

### GET

- 简单get请求

  直接使用curl后跟请求URL就行了，例如：

  ```
  curl https://curl.haxx.se
  ```

- 带参数的get请求

  参数一般有两种形式:formdata和application/x-www-form-urlencoded(json格式)，这两种格式都是在请求URL后加？的方式来传递参数，例如：

  ```
  curl "http://www.hotmail.com/when/junk.cgi?birthyear=1905&press=OK"
  ```

### POST

post请求只需要添加`-d`或者`--data`参数就可以了，这边对于不同形式的参数有点区别：

- application/x-www-form-urlencoded

  `-d`后传入json字符串，例如：

  ```
  curl -d "{'a': '1','b': '2'}" https://curl.haxx.se
  ```

  注意：可以使用--data-urlencode指定不对参数进行解码

- formdata

  `-F`或者`--form`后跟参数，例如：

  ```
  curl -F "a=1&b=2" https://curl.haxx.se
  ```

- 上传文件

  在`-F`后面添加参数，通过`@`符号引入文件，例如：

  ```
  curl "http://ip:port/api/v1/file" -H "Content-Type: multipart/form-data" -F "file=@/root/image/u68.jpg;type=image/png"
  ```

  ```
  curl "http://ip:port/api/v1/file" -H "Content-Type: multipart/form-data" -F "file=@C:\Users\u68.jpg;type=image/png"
  ```



## 其余用法

### 添加请求头

通过`-H`或者`--header`参数来添加请求头，例如：

```
curl --header "Host:" http://www.example.com
```

### 自定义请求方法

可以通过`-X`或者`--request`参数来自定义请求方法，例如：

```
curl -X POST http://example.org/
```

### 使用代理

可以通过代理服务器来访问服务，通过`-x`或者`--proxy`参数来设置代理服务器，例如：

```
curl -x my-proxy:888 ftp://ftp.leachsite.com/README
```

注意：curl会读取环境变量`http_proxy`，`HTTPS_PROXY`，`FTP_PROXY`来设置默认代理，也可以设置`ALL_PROXY`环境变量来设置通用代理。`NO_PROXY`用来设置不代理的URL(以上环境变量大小写由官方文档而来，不是自己编造的)。当然`-x`和`--proxy`参数会覆盖这些值。

### 设置设备信息

用户设备信息一般与user-agent请求头有关，服务器可以根据这个字段来返回不同的网页格式，curl可以通过`-A`或者`--user-agent`参数来设置该请求头，例如：

```
curl -A 'Mozilla/3.0 (Win95; I)' http://www.nationsbank.com/
```


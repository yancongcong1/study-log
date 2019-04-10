[TOC]

# Http认证问题

当git上传大文件时(几百M)，出现如下错误：

```
Counting objects: 61350, done.
Delta compression using up to 2 threads.
Compressing objects: 100% (20587/20587), done.
efatal: The remote end hung up unexpecterror: RPC failed; HTTP 413 curl 22 The requested U
RL returned error: 413 Request Entity Too Large
dly
Writing objects: 100% (61350/61350), 4.32 GiB | 657.00 KiB/s, done.
Total 61350 (delta 39636), reused 59367 (delta 37653)
fatal: The remote end hung up unexpectedly
Everything up-to-date
```


## 解决方法

修改git配置中的http.postBuffer，官方介绍如下：

> Maximum size in bytes of the buffer used by smart HTTP transports when POSTing data to the remote system. For requests larger than this buffer size, HTTP/1.1 and Transfer-Encoding: chunked is used to avoid creating a massive pack file locally. Default is 1 MiB, which is sufficient for most requests.

有两种方式来修改该配置：

- 修改.git/config文件

  添加如下内容：
  
  ```
  [http]
  	postBuffer = 524288000
  ```
  
- 通过`git config`命令修改

  ```
  git config http.postBuffer 524288000
  ```
[TOC]

# Http认证问题

当git使用http进行认证的时候，有时候会出现这样的情况：终端报认证失败错误，但是认证过程中并没有弹出http认证窗口。反复尝试情况没有任何改变。



## 思路

这可能是因为该网站的http认证信息被git或者操作系统进行了缓存，所以每次不会再次询问用户认证信息，而是直接使用该信息进行认证，但是这个信息却是错误的......



## 解决方法

这边提供两种解决方式，但是目前只是测试过一种：

- **操作系统缓存**(已测试)

  我们通过windows的控制面板可以看到在凭证管理中会有我们相应的网站的http认证信息，我们只需要删除掉相应的认证信息重新认证就ok了。如下图所示：

  控制面板路径：用户账户和家庭安全--->凭据管理器

  ![https://github.com/yancongcong1/blog/tree/master/git/static/images/http-authentication.png](https://github.com/yancongcong1/blog/tree/master/git/static/images/http-authentication.png)

- **git认证配置缓存**(未测试)

  git在使用http认证时会缓存认证信息，可以通过命令删除缓存：

  ```
  git config --system --unset credential.helper
  ```
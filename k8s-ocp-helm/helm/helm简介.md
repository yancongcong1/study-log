[TOC]

# Helm



## 简介

Helm(和Tiller)用来管理Kubernetes集群中的包资源，在Helm中主要有三大概念：

- **Chart**

  一个Chart代表一个Helm资源包，它包含了Kubernetes中运行一个应用、工具或者服务所有需要的对象资源，可以把它想象成Homebrew formula、Apt dpkg或者Yum RPM文件。

- **Repository**

  Repository(仓库)顾名思义就是存放charts的地方，通过它我们可以方便的管理和分享chart。它跟[CPAN archive](https://www.cpan.org/)或者[Fedora Package Database](https://apps.fedoraproject.org/packages/s/pkgdb)类似，只不过它存放的是Kubernetes包资源。

- **Release**

  一个Release是运行在Kubernetes集群中的一个Chart实例。一个Chart可以在一个Kubernetes集群中安装多次，每次安装后都会创建一个新的release。

  拿MySQL chart举例，如果你想要在一个集群中运行两个数据库，你可以将该chart安装两次，每此安装都会创建一个新的release。

结合上面的三个概念，我们这样来形容Helm：

Helm在Kubernets集群中安装charts，每次安装后都会创建一个对应的release。



## 安装

我们可以通过`Helm init`命令来安装Helm客户端(Helm)和服务端(Tiller)。Tiller服务主要将chart资源进行整合以及将整合后的资源发送给Kubernetes进行安装，这会在以后的文档中详细说明。

想要运行Helm命令需要我们下载Helm的二进制运行包，我们可以直接下载二进制包，也可以通过一些包管理工具来下载：

- [二进制包下载地址](https://github.com/helm/helm/releases/tag/v2.13.0)
- 通过包管理工具：
  - [Homebrew](https://brew.sh/) --- `brew install kubernetes-helm`.
  - [Chocolatey](https://chocolatey.org/) --- `choco install kubernetes-helm`.
  - [Scoop](https://scoop.sh/) --- `scoop install helm`.
  - [GoFish](https://gofi.sh/) --- `gofish install helm`.

> 这边可能有点疑问：难道我们下载的这个二进制包不就是Helm客户端吗？的确，这样说也没有问题，`init`命令主要会帮我们做一些Helm本地的一些配置。运行`init`命令后可以在HELM_HOME下面看到.helm文件，这里面包含着我们的一些配置文件。
>
> 你可能会发现，我们不运行init来设置本地配置依然可以执行部分helm命令(前提是之前必须在Kubernetes集群中安装好了Tiller服务)例如`install`命令，但是执行一些命令可能报错例如`search`命令。所以为了更加完好的使用helm，使用init命令进行本地的初始化配置必不可少，当然你也可以将配置文件复制到正确的地方，只是这样的做法有些麻烦就是了。

下面我们主要介绍一下`helm init`命令以及部分主要参数的含义：

`helm init`命令会通过$KUBECONFIG环境变量中的配置读取到Kubernetes集群的信息并将使用$TILLER_NAMESPACE定义的命名空间来安装Tiller服务。

如果只需要进行本地配置而不需要安装Tiller服务，可以使用--client-only选项。

`helm init`默认安装最新稳定的Tiller，可以通过--tiller-image来指定Tiller的版本。

| 参数              | 含义                                                         |
| ----------------- | ------------------------------------------------------------ |
| -c, --client-only | 不安装Tiller服务                                             |
| --dry-run         | 你可能会在许多命令中看到这个选项，该选项的意思是进行模拟操作，会模拟命令执行以及获得模拟结果，但是不会改变客户端以及服务端状态。 |
| --local-repo-url  | 本地仓库的地址，默认为http://127.0.0.1:8879/charts           |
| --node-selectors  | 指定集群中Tiller安装的node                                   |
| --stable-repo-url | 标准仓库的地址，默认为https://kubernetes-charts.storage.googleapis.com。如果该地址无法访问的话，可以配置为国内阿里的仓库地址:https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts |
| --upgrade         | 更新Tiller服务                                               |
| --force-upgrade   | 强制将Tiller服务更新到当前的helm版本                         |



##  TILLER, NAMESPACES AND RBAC

有时你可能会有在一个集群中安装多个Tiller服务的需求，下面是一些在这种场景下的最佳实践：

1. Tiller在任意的namespace中都可以安装。默认情况下会被安装到kube-system中。可以通过指定namespace的方式来在一个集群中运行多个Tiller服务。
2. 限制Tiller只能安装到指定的namespace是由Kubernetes的[RBAC](https://kubernetes.io/docs/reference/access-authn-authz/rbac/)角色和角色绑定机制来控制的。使用helm init命令时，可以通过--service-account选项来指定Tiller的服务账户。
3. 每个Tiller服务生成的Release名称都是唯一的。
4. Charts只因该包含存在于单个namespace中的资源。
5. 不建议使用多个Tiller来共同管理同一个namespace中的资源。
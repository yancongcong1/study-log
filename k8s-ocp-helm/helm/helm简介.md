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

  > 这边简单区分一下release和revision：release类似一个应用，revision类似于应用的版本号。当使用install命令的时候会生成对应的release(指定名称)以及revision(默认为1)。以后release都不会改变，使用install也不能再指定该release名称(除非删除并释放，之后会介绍)，每次使用upgrade命令都会重新部署一个release版本，默认会在最近的基础上加1(也可以手动指定)，但是release不变，变得是revision。

结合上面的三个概念，我们这样来形容Helm：

Helm在Kubernets集群中安装charts，每次安装后都会创建一个对应的release。
Helm中的其他**专业术语**请参考[文档](https://helm.sh/docs/glossary/)。



## 安装

我们可以通过`Helm init`命令来安装Helm客户端(Helm)和服务端(Tiller)。Tiller服务主要将chart资源进行整合以及将整合后的资源发送给Kubernetes进行安装，这会在以后的文档中详细说明。

想要运行Helm命令需要我们下载Helm的二进制运行包，我们可以直接下载二进制包，也可以通过一些包管理工具来下载：

- [二进制包下载地址](https://github.com/helm/helm/releases/tag/v2.13.0)
- 通过包管理工具：
  - [Homebrew](https://brew.sh/) --- `brew install kubernetes-helm`.
  - [Chocolatey](https://chocolatey.org/) --- `choco install kubernetes-helm`.
  - [Scoop](https://scoop.sh/) --- `scoop install helm`.
  - [GoFish](https://gofi.sh/) --- `gofish install helm`.

> 这边可能有点疑问：难道我们下载的这个二进制包不就是Helm客户端吗？这样说的确也没有问题，`init`命令主要作用是帮我们做一些Helm本地的配置。运行`init`命令后可以在HELM_HOME下面看到.helm文件，这里面包含着我们的一些配置文件。
>
> 你可能会发现，我们不运行init来设置本地配置依然可以执行部分helm命令(前提是之前必须在Kubernetes集群中安装好了Tiller服务)例如`install`命令，但是执行一些命令可能报错例如`search`命令。所以为了更加完好的使用helm，使用init命令进行本地的初始化配置必不可少，当然你也可以将配置文件复制到正确的地方，只是这样的做法有些麻烦就是了。

那么`init`命令会将Tiller安装到什么地方呢？这就不得不说一下使用helm的前提条件了：

- 首先你需要一个Kubernetes集群。Helm的许多命令例如`install`和`upgrade`都需要Kubernetes才能正常使用。
- 你最好在本地有一个`kubectl`客户端来访问Kubernetes服务，当然`oc`客户端也是可以的(关于openshift会在相关文档中另行说明)

当我们使用`init`命令时，Helm会通过查找Kubernetes配置文件(通常在`$HOME/.kube/config`，此文件包含了kubectl客户端访问Kubernetes集群的一些配置信息)，从而确定将Tiller服务安装到哪个集群上面(`kubectl`客户端可以连接多个集群，我们可以通过--kube-context选项将Tiller安装到指定的集群中)。从这个过程中我们可以看到，在使用`init`命令之前，我们需要先登录Kubernetes集群，这样配置文件中才会保留登录令牌以便Helm访问集群时使用。

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

### 小结

我们总结一下安装Helm的过程：

1. 准备好Kubernetes集群，本地安装kubectl客户端或者oc客户端；
2. 准备好helm二进制包，用来执行`helm init`命令；
3. 执行登录Kubernetes的命令，保证将登录令牌写入配置文件(登录时自动写入)；
4. 执行`init`命令来安装Tiller服务或者只是配置本地的Helm，具体参照`init`命令介绍。

###  TILLER AND NAMESPACES

有时你可能会有在一个集群中安装多个Tiller服务的需求，下面是一些在这种场景下的最佳实践：

1. Tiller在任意的namespace中都可以安装。默认情况下会被安装到kube-system中。可以通过指定namespace的方式来在一个集群中运行多个Tiller服务。
2. 每个Tiller服务生成的Release名称都是唯一的。
3. Charts只因该包含存在于单个namespace中的资源。
4. 不建议使用多个Tiller来共同管理同一个namespace中的资源。

### 安全问题和RBAC

如果你是自己学习或者开发使用，通常使用helm init简单安装就行了；但是如果你想要在生产环境中安装Helm，最好启用一些安全设置，具体请参考[文档](https://helm.sh/docs/using_helm/#securing-your-helm-installation)。

如果你的集群开启了RBAC，你还得做一些关于权限的配置才能正确使用Tiller服务，详情请参考[configure a service account and rules](https://helm.sh/docs/using_helm/#role-based-access-control) 

关于更多关于安装HELM的详细信息请参考[官方文档](https://helm.sh/docs/using_helm/#installing-helm)
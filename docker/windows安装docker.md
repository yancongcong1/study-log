[TOC]

### 版本介绍

在windows上安装docker有着不同的做法，因为docker是基于linux内核的，所以在windows和mac上安装只能通过虚拟机来实现了。具体的操作步骤在阿里云文档有详细说明：[阿里云文档](https://cr.console.aliyun.com/cn-hangzhou/mirrors)，注意需要先登录阿里云才能查看该文档。


### 安装方法

可以看到在windows上安装docker主要有两种方式：

- Docker Toolbox(VirtualBox)
- Docker for Windows(Hyper-V)

这两种方式分别通过不同的虚拟工具来实现，可以从文档看到win10以上用户推荐使用Docker for Windows，win10以下用户推荐使用Docker Toolbox(我win7竟然根本无法打开Docker for Windows安装文件，不知是否个例)。关于VirtualBox和Hyper-X的区别可以自行查阅资料。接下来我们直接进行安装的相关步骤：

在此之前需要确认机器是否开启了虚拟化：[机器如何开启虚拟化](http://jingyan.baidu.com/article/335530daa55d7e19cb41c3c2.html)

本人机器：windows7 64位

Docker工具：Docker Toolbox

1. 创建虚拟机

   当安装好后，我们可以直接通过docker-machine命令来创建虚拟机。这边有许多值得注意的点，我们会一一道来。我们先看一下安装虚拟机的命令：

   ```
   docker-machine create -d virtualbox default
   ```

   当我们运行该命令的时候，有的时候就会发现这样一个问题：

   > Running pre-create checks...
   > Unable to get the local Boot2Docker ISO version: Did not find prefix "-v" in version string
   > (default) Default Boot2Docker ISO is out-of-date, downloading the latest release...
   > (default) Latest release for github.com/boot2docker/boot2docker is v18.09.0
   > (default) Downloading .................
   >
   > 这个问题该如何解决呢？
   >
   > 可以通过指定boot2docker.ios来跳过检查，[参考文档](https://www.v2ex.com/t/509795)：
   >
   > ```
   > docker-machine.exe create --driver virtualbox --virtualbox-boot2docker-url=boot2docker.iso default
   > ```
   >
   > **注意使用这个方式的话命令行当前目录需要和boot2docker.iso文件在同一目录**。

   如果你没有其他的需求，至此应该就算完成了，但是当你打开cmd终端或者git-bash输入docker ps时，会报这样的错误：

   > error during connect: Get http://%2F%2F.%2Fpipe%2Fdocker_engine/v1.37/containers/json: open //./pipe/docker_engine: The system cannot find the file specified. In the default daemon configuration on Windows, the docker client must be run elevated to connect. This error may also indicate that the docker daemon is not running.

   朋友们可能就会很疑惑了，我的docker虚拟机明明都启动了啊，为什么还是无法连接docker服务，这是因为我们的终端并没有和docker虚拟机建立起连接，所以我们无法使用docker服务。

   这个地方就会牵扯到第二个问题，那就是不同终端的区别，`cmd`、`git-bash`以及`windows powershell`这三个终端，不同终端连接虚机的命令都不同，具体可以通过docker-machine env命令来查看。

   **通过输入`docker-machine env default`，控制台输入的最后一行就是连接虚机的命令。**

   powershell连接docker虚拟机命令如下：

   ```
   docker-machine env default | Invoke-Expression
   ```

   如何判断当前终端是否与虚拟机建立连接，又是连接到哪个虚拟机其实也很方便，通过以下命令即可：

   ```
   docker-machine ls
   ```

   会看到以下图片：

   ![docker-machine-connect](https://github.com/yancongcong1/study-log/tree/master/docker/static/images/docker-machine-connect.png)

   可以看到当前有两台虚拟机，**active属性下面为*表示和当前终端建立了连接**。这时候可以试一下docker ps命令是否可以正常执行了。

   在主机终端访问docker大多数情况都是因为工作需要获取本地某个目录的上下文，如果主机终端可以直接访问docker服务，那么就可以很方便的切换工作空间。如果此时根据以上步骤进行了配置但是主机扔无法访问docker服务又该怎么做呢(可能错误如下所示，暂未找到解决方法，错误大多跟此有关)？

   ```
   error during connect: Get https://192.168.99.100:2376/v1.37/containers/json?all=1: unexpected EOF
   ```

   跟docker类似，virtualbox提供虚拟机挂载目录的功能，此时我们可以将需要的工作目录直接挂载到docker虚拟机中的指定目录，然后通过ssh进入docker虚拟机内部来进行工作，可以使用`docker-machine mount`命令来进行目录的挂载，然后使用`docker-machine ssh`命令进入虚拟机。
   虚拟机挂载目录的相关信息只能通过VirtualBox应用界面的设置进行查看，`docker-machine inspect`命令查询不到挂载信息：

   ![vituralBox-setting](https://github.com/yancongcong1/study-log/tree/master/docker/static/images/vituralBox-setting.png)

2. 配置镜像加速

   使用默认docker仓库有时访问过慢或者干脆直接失败，所以我们需要配置镜像加速，如何配置阿里的文档上说明的也很清楚了。这边我们提供两种方案来进行配置：

   - 修改配置文件

     ```
     # 通过ssh命令进入虚拟机内部
     docker-machine ssh default
     
     # 切换为root用户，只有root用户才能修改配置文件
     sudo -s
     
     # 编辑/var/lib/boot2docker/profile文件
     vi /var/lib/boot2docker/profile
     
     # 在EXTRA_ARGS中添加--registry-mirror https://xxxxxxx.mirror.aliyuncs.com(你自己的加速地址)
     ```

     最后重新启动虚拟机：`docker-machine restart default`或者重新启动docker服务`sudo /etc/init.d/docker restart`

     > **注意：在这边会出现另一个有意思的地方，就是当我使用ssh命令进入虚拟机的时候，如果使用cmd或者powershell终端修改配置文件会出现乱码和根本看不懂什么鬼的情况。解决办法就是使用git-bash来修改配置文件。**

   - 创建虚拟机时指定参数

     ```
     docker-machine create --engine-registry-mirror=https://xxxxxxx.mirror.aliyuncs.com -d virtualbox default
     ```

   > **注意：这边如果修改配置文件后无法生效建议使用创建虚机时指定参数的方式。**

3. 添加私人仓库

   一般情况下，公司都会有自己的私人仓库，如果我们想要连接我们自己的仓库就需要进行相关的配置。该配置也有两种方法，同上：

   - 修改配置文件

     ```
     # 编辑/var/lib/boot2docker/profile文件
     # 在下面EXTRA_ARGS中添加--insecure-registry=https://registry.it.test.com(你自己的仓库地址)
     ```

     最后重新启动虚拟机：`docker-machine restart default`或者重新启动docker服务`sudo /etc/init.d/docker restart`

   - 创建虚拟机时指定参数

     ```
     docker-machine create --engine-insecure-registry=https://registry.it.test.com -d virtualbox default
     ```

   一般情况下到此我们的配置就完成了，但是有一些公司设置了内网外网访问权限，所以如果在这种情况下还需要配置代理。

4. 配置代理

   配置代理也是跟上面一样有两种方式，[github官方文档](https://github.com/docker/kitematic/wiki/Common-Proxy-Issues-&-Fixes)

   - 修改配置文件

     ```
     # 编辑/var/lib/boot2docker/profile文件
     # 在最下方添加
     export HTTP_PROXY=ip:port
     export HTTPS_PROXY=ip:port
     export NO_PROXY=ip:port
     ```

     最后重新启动虚拟机：`docker-machine restart default`或者重新启动docker服务`sudo /etc/init.d/docker restart`

   - 创建虚拟机时指定参数

     ```
     docker-machine create --engine-env HTTP_PROXY=ip:port --engine-env HTTPS_PROXY=ip:port --engine-env NO_PROXY=ip:port -d virtualbox default
     ```

     NO_PROXY表示不想走代理服务的访问地址。

   在这边配置时环境相当复杂。比如你的机器可能是处于外网环境或者处于内网环境，这两种环境对于代理的配置完全相反并且在某些方面差距会相当大：

   - 外网环境

     外网环境配置代理那就是要访问内网了，这时候HTTP_PROXY和HTTPS_PROXY配置内网代理服务地址这个毋庸置疑，但是当我们配置好之后发现docker pull一直报错：XXXX EOF，就是无法与服务连接呗。所以我们需要配置NO_PROXY，让某些访问不走代理。这边比较恶心的是docker pull的时候会访问多个地址，需要一个一个配置（*也许会有通配符写法，但是我的机器暂时无法访问docker网站，所以先告一段落，有兴趣可以研究一下*)。完成后我的机器代理部分配置如下：

     ```
     export "HTTP_PROXY=ip:port"
     export "HTTPS_PROXY=ip:port"
     export "NO_PROXY=registry-1.docker.io, auth.docker.io, production.cloudflare.docker.com, xxxxxx.mirror.aliyuncs.com"
     ```

   - 内网环境

     内网环境配置代理访问外网相对来说就比较简单了，首先HTTP_PROXY和HTTPS_PROXY配置外网代理服务地址。这时一般只要配置NO_PROXY为内网仓库地址就ok了。完成后代理配置如下：

     ```
     export "HTTP_PROXY=ip:port"
     export "HTTPS_PROXY=ip:port"
     export "NO_PROXY=registry.it.test.com"
     ```



### 总结

至此，win7上的docker安装并且配置完毕，最后附上创建虚机完整命令以及一张完全配置：

创建虚机命令：

```
docker-machine create 
	-d virtualbox
	--virtualbox-boot2docker-url=boot2docker.iso
	--engine-insecure-registry=https://registry.it.test.com
	--engine-registry-mirror=https://xxxxxx.mirror.aliyuncs.com
	--engine-env HTTP_PROXY=ip:port
	--engine-env HTTPS_PROXY=ip:port
	--engine-env NO_PROXY=xxxxxx.mirror.aliyuncs.com
	default
```

完整配置：

```
EXTRA_ARGS='
--label provider=virtualbox
--insecure-registry https://registry.it.test.com
--registry-mirror https://xxxxxxx.mirror.aliyuncs.com

'
CACERT=/var/lib/boot2docker/ca.pem
DOCKER_HOST='-H tcp://0.0.0.0:2376'
DOCKER_STORAGE=aufs
DOCKER_TLS=auto
SERVERKEY=/var/lib/boot2docker/server-key.pem
SERVERCERT=/var/lib/boot2docker/server.pem

export HTTP_PROXY=ip:port #注意这边加不加""都可以
export HTTPS_PROXY=ip:port
export NO_PROXY=ip:port
```

关于docker-machine创建时的参数可以通过`docker-machine create --help`命令查询。
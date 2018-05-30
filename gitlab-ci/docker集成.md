[TOC]

# Gitlab CI/CD之docker集成



## 简介

我们可以使用docker的强大功能来执行我们的CI构建过程，本节从以下几个方面来简单介绍一下如何集成docker：

1. docker配置之image和services
2. 使用docker参与CI构建





## 1、docker配置之image和services

Gitlab CI过程可以使用Docker Engine测试和构建任何应用。

在这一步我们需要做一些准备工作，那就是在注册Gitlab Runner的时候需要选择docker执行器，当然，这不是必须的，我们还可以通过其他的方式来集成docker，当我们选择docker的时候可能需要同时选择默认的image以及services，之前.gitlab-ci.yml中也提到过这两个选项，下面我们就详细介绍一下这两个配置选项：

**image**

image---string/hash，是docker镜像的名称，docker通过启动镜像来执行CI任务。

默认情况下，executor会从Docker Hub拉取镜像，但是可以通过在gitlab-runner/config.toml中配置 [Docker pull policy](https://docs.gitlab.com/runner/executors/docker.html#how-pull-policies-work) 来从本地拉取镜像。

**services**

services---array，定义了在job期间与image定义的镜像关联起来的另外的镜像，这样的话我们就能在构建期间使用service镜像了。

service镜像可以运行任何应用，但是大多数情况下通常是一个数据库服务，例如mysql。这样的话我们可以方便快速的将mysql当作一个容器来启动运行，而不是安装一个mysql服务。

当然，你可以添加任何你需要的服务，你可以在.gitlab-ci.yml中通过配置选项或者修改config.toml配置文件来设置services。

这儿有一些services使用的[例子](https://docs.gitlab.com/ee/ci/services/README.html) 。



### service如何与job关联

在docker继承中，service的镜像和job镜像关联起来主要依靠docker提供的容器关联技术，[详细信息](https://docs.docker.com/network/links/)。

总而言之如果你给你的应用添加了一个mysql服务，那么将会使用这个镜像创建一个容器来与job的容器相关联。

### 在.gitlab-ci.yml中定义image和services

在.gitlab-ci.yml中定义image和services有两种情况：

1、定义全局的image和services

```yaml
image: ruby:2.2

services:
  - postgres:9.3

before_script:
  - bundle install

test:
  script:
  - bundle exec rake spec
```

2、给每个job单独定义image和services

```yaml
before_script:
  - bundle install

test:2.1:
  image: ruby:2.1
  services:
  - postgres:9.3
  script:
  - bundle exec rake spec

test:2.2:
  image: ruby:2.2
  services:
  - postgres:9.4
  script:
  - bundle exec rake spec
```



### docker配置选项拓展

image和services有两种配置方式，string或者map：

- 当使用string配置时，必须是镜像的全名
- 当使用map时，name选项为必选项，它用来设置镜像的名称

下面这两种配置等效：

```yaml
image: "registry.example.com/my/image:latest"

services:
- postgresql:9.4
- redis:latest
```

```yaml
image:
  name: "registry.example.com/my/image:latest"

services:
- name: postgresql:9.4
- name: redis:latest
```

简单介绍一下使用map配置时的一些可选配置项：

#### image可选项

| Setting      | Required                             | GitLab version | Description                              |
| ------------ | ------------------------------------ | -------------- | ---------------------------------------- |
| `name`       | yes, when used with any other option | 9.4            | Full name of the image that should be used. It should contain the Registry part if needed. |
| `entrypoint` | no                                   | 9.4            | Command or script that should be executed as the container's entrypoint. It will be translated to Docker's `--entrypoint` option while creating the container. The syntax is similar to [`Dockerfile`'s `ENTRYPOINT`](https://docs.docker.com/engine/reference/builder/#entrypoint) directive, where each shell token is a separate string in the array. |

#### services可选项

| Setting      | Required                             | GitLab version | Description                              |
| ------------ | ------------------------------------ | -------------- | ---------------------------------------- |
| `name`       | yes, when used with any other option | 9.4            | Full name of the image that should be used. It should contain the Registry part if needed. |
| `entrypoint` | no                                   | 9.4            | Command or script that should be executed as the container's entrypoint. It will be translated to Docker's `--entrypoint` option while creating the container. The syntax is similar to [`Dockerfile`'s `ENTRYPOINT`](https://docs.docker.com/engine/reference/builder/#entrypoint) directive, where each shell token is a separate string in the array. |
| `command`    | no                                   | 9.4            | Command or script that should be used as the container's command. It will be translated to arguments passed to Docker after the image's name. The syntax is similar to [`Dockerfile`'s `CMD`](https://docs.docker.com/engine/reference/builder/#cmd) directive, where each shell token is a separate string in the array. |
| `alias`      | no                                   | 9.4            | Additional alias that can be used to access the service from the job's container. Read [Accessing the services](https://docs.gitlab.com/ee/ci/docker/using_docker_images.html#accessing-the-services) for more information. |

更多[详细信息](https://docs.gitlab.com/ee/ci/docker/using_docker_images.html) 。



## 2、使用docker参与CI构建

使用docker构建可以分为下面几个步骤：

1. 创建一个应用镜像
2. 运行创建好的应用镜像的测试用例
3. 将镜像推送到仓库
4. 将镜像部署到服务器

例如：

```shell
$ docker build -t my-image dockerfiles/
$ docker run my-docker-image /script/to/run/tests
$ docker tag my-image my-registry:5000/my-image
$ docker push my-registry:5000/my-image
```

从上面的例子中我们可以看出使用到了docker命令，那么我们该如何做能让Gitlab Runner来执行这些命令呢，这儿有`三种`方式可以帮助我们完成这些构建：

**NOTE：这儿有一个通用操作，那就是先安装自己的runner，然后进行注册，唯一不同的操作就是executor的选择，还需在同一个服务器上安装docker**

### 使用shell执行构建

在准备完成之后，我们可以根据以下步骤来实现我们的目的：

**NOTE：除了shell，其他方式只能使用docker命令来执行构建(有待深入探讨)**

1. 首先需要在注册runner时选择shell作为executor

2. 可能从之前的实战问题中大家的到了一些提示，当使用shell作为executor时，linux系统默认使用gitlab-runner用户执行命令，此时无法使用docker命令，我们可以将gitlab-runner添加到docker组(安装docker的时候自动创建)中：

   ```shell
   sudo usermod -aG docker gitlab-runner
   ```

   确认gitlab-runner是否拥有docker权限：

   ```
   sudo -u gitlab-runner -H docker info
   ```

   在.gitlab-ci.yml中确认权限是否可行：

   ```yaml
   before_script:
     - docker info

   build_image:
     script:
       - docker build -t my-docker-image .
       - docker run my-docker-image /script/to/run/tests
   ```

3. 现在你就可以使用docker来执行构建了

**NOTE：这种方式可能需要赋予gitlab-runner更高的权限，比如root，所以有时候这种方式不太安全，具体参考[文档](https://www.andreas-jung.com/contents/on-docker-security-docker-group-considered-harmful)。**

个人认为这是最简单的一种方式，也很容易理解，主要因为我对docker还不熟，熟悉的朋友可能觉得下面的才更加简单，的确，它也是官方推荐的docker集成方法。

### 使用docker-in-docker执行构建

这种方式需要我们选择docker作为我们的executor并且需要docker作为image，同时在特权模式中运行我们的构建：

1. 选择docker作为executor，配置docker作为image，设置docker特权模式(可以修改config.toml文件)，执行以下命令注册runner：

   ```shell
   sudo gitlab-runner register -n \
     --url https://gitlab.com/ \
     --registration-token REGISTRATION_TOKEN \
     --executor docker \
     --description "My Docker Runner" \
     --docker-image "docker:latest" \
     --docker-privileged
   ```

   完成后config.toml结构如下：

   ```tex
   [[runners]]
     url = "https://gitlab.com/"
     token = TOKEN
     executor = "docker"
     [runners.docker]
       tls_verify = false
       image = "docker:latest"
       privileged = true
       disable_cache = false
       volumes = ["/cache"]
     [runners.cache]
       Insecure = false
   ```

   NOTE：如果使用这种方式，那么必须开启特权模式，[详细信息](https://blog.docker.com/2013/09/docker-can-now-run-within-docker/)。

2. 现在你可以在你的构建script中使用docker了(注意下面例子中的docker:dind服务)

   ```yaml
   image: docker:latest

   # When using dind, it's wise to use the overlayfs driver for
   # improved performance.
   variables:
     DOCKER_DRIVER: overlay2

   services:
   - docker:dind

   before_script:
   - docker info

   build:
     stage: build
     script:
     - docker build -t my-docker-image .
     - docker run my-docker-image /script/to/run/tests
   ```

TODO：此处对于为什么使用docker:dind服务抱有疑问，上面解释说使用该服务docker就会自动使用overlayfs驱动来提升性能，但是花式懵逼，暂时先不讨论这个问题，等以后学习docker的时候在来补充。

github网址：[docker-in-docker](https://github.com/jpetazzo/dind)



### 通过绑定docker socket的方式执行构建

这种方式需要我们选择docker作为我们的executor并且需要docker作为image，此时并不需要开启特权模式，作为替换将/var/run/docker.sock挂载到容器(可以修改config.toml文件)，这样的话在镜像中就可以使用docker命令了：

1. 执行以下命令注册runner：

   ```shell
   sudo gitlab-runner register -n \
     --url https://gitlab.com/ \
     --registration-token REGISTRATION_TOKEN \
     --executor docker \
     --description "My Docker Runner" \
     --docker-image "docker:latest" \
     --docker-volumes /var/run/docker.sock:/var/run/docker.sock
   ```

   NOTE：简单解释一下上面命令的意思，就是将外部docker与我们的docker镜像挂载起来，使用的还是我们runner的docker守护进程，但是通过docker命令生成的所有容器并不是runner的docker的子容器，而是和它同级的容器。

   完成后config.toml内容如下：

   ```tex
   [[runners]]
     url = "https://gitlab.com/"
     token = TOKEN
     executor = "docker"
     [runners.docker]
       tls_verify = false
       image = "docker:latest"
       privileged = true
       disable_cache = false
       volumes = ["/cache"]
     [runners.cache]
       Insecure = false
   ```

2. 现在我们可以使用docker命令了(注意我们不需要使用docker:dind服务了)

   ```yaml
   image: docker:latest

   before_script:
   - docker info

   build:
     stage: build
     script:
     - docker build -t my-docker-image .
     - docker run my-docker-image /script/to/run/tests
   ```

当使用这种方式的时候，你需要知道：

- 通过共享docker守护进程，你可以有效禁用容器的安全机制，将你的主机权限提升，从而导致容器“翻墙”。例如，如果你的项目有这样一段构建`docker rm -f $(docker ps -a -q)`，这将移除Gitlab Runner容器

- 并发的工作可能不会起作用，如果您的测试想要创建特定名称的容器，它们可能相互冲突

- 从源关联到容器中的共享文件和目录可能不会正常工作，因为volume挂载在了host上下文中而不是构建容器中，例如：

  ```shell
  docker run --rm -t -i -v $(pwd)/src:/home/app/src test-image:latest run_app_tests
  ```

## 实战以及遇到的问题

实战参考第四节

Q：使用docker打包镜像时报错：COPY failed: Forbidden path outside the build context: ../dist

问题描述：

1、具体执行命令：docker build -t sakura-client-nginx ./docker

2、文件结构如下：![](https://gitlab.com/yancongcong1/CICD-Test/uploads/35ba86562c5b33bf2cbd098acf269e7e/%E6%96%87%E4%BB%B6%E7%BB%93%E6%9E%84.png)

A：[解决办法](https://howtovault.com/posts/how-to-fix-copy-failed-forbidden-path-outside-the-build-context-in-docker/)
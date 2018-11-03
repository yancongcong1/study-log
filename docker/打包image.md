[TOC]

# docker 打包 image

  docker有多种方式可以创建image：

  - 基于已有镜像的容器创建
  
  - 基于本地模板导入
  
  - 基于Dockerfile创建

### 基于已有镜像

  本方法基于`docker commit`命令来创建镜像：

  ```
  docker commit [OPTIONS] CONTAINER [REPOSITORY:[:TAG]]
  ```

  > 提示：具体参考命令介绍
  >
  >
  > 示例：`docker commit -m "Added a new file" -a "Docker Newbee" a925cb40b3f0 test:0.1`

### 基于本地模板

  本方法基于`docker import`命令来创建镜像：

  ```
  docker import [OPTIONS] file|URL|- [REPOSITORY[:TAG]]
  ```

  > 提示：具体参考命令介绍，同时可以使用`docker export`将一个容器导出为模板
  >
  >
  > 示例：`docker import --input redis.tar`

### 基于Dockerfile

  本方法基于`docker build`命令和Dockerfile文件来创建镜像：

  ```
  docker build [OPTIONS] PATH | URL | -
  ```

  > 提示：具体参考命令介绍，关于Dockerfile的介绍会在下篇文章中进行
  >
  >
  > 示例：`docker build -t build_repo/first_image -f /tmp/docker_builder/`

### image拓展

  使用`docker save`命令可以将image存储为tar压缩文件，拷贝到另外的机器后使用`docker load`命令来载入镜像。一般在不同机器上拷贝镜像时使用，关于命令详情参考命令介绍。
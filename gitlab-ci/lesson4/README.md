[TOC]

# Gitlab CI/CD之构建文件.gitlab-ci.yml



## 简介

从7.2版本开始，Gitlab CI开始使用YAML(.gitlab-ci.yml)作为项目的配置文件，它被放置在项目的根目录。

.gitlab-runner.yml告诉runner该做什么，如果不定义stages的话，默认执行三个阶段：build、test、deploy。你不需要使用所有的阶段，因为那些没有定义job的阶段会被忽略。

> NOTE：这边有一个小技巧，如果写完.gitlab-ci.yml后并不确定自己的语法是否准确，可以在项目中进行语法的检查，具体位置如下：
>
> - 首先进入项目
> - 进入CI/CD--->Pipelines或者CI/CD--->Jobs，右上角点击CI Lint，进去后将内容全部粘贴就可以进行校验了



## 配置选项

.gitlab-ci.yml通过一些配置选项定义了CI/CD的构建过程，在此简单介绍部分常用的配置，如果你想要了解全面的配置参数，请阅读[官方文档](https://docs.gitlab.com/ee/ci/yaml/README.html)。

本节介绍的配置选项包括：

- image 和 services
- before_script 和 after_script
- stages
- cache
- job





### image & sevices

这两个选项允许我们配置一个Docker镜像和一个服务列表，以供我们在job期间使用它们，详细的信息请参照[第六节](https://github.com/yancongcong1/study-log/tree/master/gitlab-ci/lesson6)。



### before_script & after_script

- before_script：

  用来定义一些命令，它们在每个job之前运行，但是在artifacts被恢复之后执行，关于artifacts的相关信息请参照job选项，它是job的一个子选项。before_script可以是array或者多行的string。

- after_script：

  > ***NOTE：在Gitlab8.7中被引入并且至少需要Gitlab Runner v1.2***
  >

  用来定义一些命令，它们在每个job之后运行，即使job失败了也会执行这些命令。同样的，可以是array或者多行的string。

  > ***NOTE：before_script和 job中的script是被串联起来运行在同一个context/container中，但是after_script却是单独运行的，所以工作空间中的一些变化对于它来说可能是不可见的，例如我们在before_script中安装了一个node，在after_script中就不可以使用。***


示例如下：

```
before_script:
  - test
  - echo "before at all jobs!"
  
after_script:
  - test
  - echo "after at all jobs!"
```



### stages

stage被用来定义job的阶段，它允许我们灵活的定义多种阶段来执行我们的构建工作，它同时定义了作业的执行顺序(即job的顺序和stage的顺序相同)：

- 同一个阶段中的job同时执行
- 下一个阶段中的job只在上个阶段中所有的job成功后执行

在定义stages时有一些小的技巧需要注意：

- 如果.gitlab-ci.yml中没有定义任何stage，那么默认存在三个stage，build-->test-->deploy
- 如果job没有指定一个stage，那么默认为test阶段

示例如下：

```
stages:
  - build
  - test
  - deploy
  - mydefine
```



### cache

> NOTE：
>
> - 在GitLab Runner v0.7.0中被引入
> - 在GitLab 9.2之前的版本中，caches在artifacts之后才会被恢复
> - 在GitLab 9.2之后的版本中，caches在artifacts之前被恢复
>
> artifacts为job选项中的一个子选项，功能与cache十分相似，具体请参考**job中的artifacts**
>
> 从Gitlab 9.0开始，pipeline和job之间的缓存被默认启用并共享

cache被用来指定可以在各个job之间被缓存的文件或者文件夹的列表，你只需要使用项目中缓存的文件路径就可以访问它们。

如果缓存配置选项被定义在job的外面，那么它就是全局缓存并且所有的job都可以使用指定的缓存。

一定要注意job中定义的缓存会覆盖掉全局缓存或者前面的缓存，所以如果你想要给不同的job定义不同的缓存，那么你需要给每一个缓存定义一个key，chche的几个属性属性分别如下：

- untracked---是否缓存所有git没有跟踪的文件，值为true或者false
- paths---定义缓存的文件或者文件夹
- key---[详细信息](https://docs.gitlab.com/ee/ci/yaml/README.html#cache-key)
- policy---[详细信息](https://docs.gitlab.com/ee/ci/yaml/README.html#cache-policy)

示例如下：

```

rspec:
  script: test
  cache:
    key: rspec
    paths:
      - binaries/
      - test/
```



### job

.gitlab-ci.yml允许你定义多个job，每个job必须拥有特殊的名字，但是不能是关键字，不能用来做job的关键字列表如下：

| Keyword       | Required | Description                              |
| ------------- | -------- | ---------------------------------------- |
| image         | no       | Use docker image, covered in [Use Docker](https://docs.gitlab.com/ee/ci/docker/README.html) |
| services      | no       | Use docker services, covered in [Use Docker](https://docs.gitlab.com/ee/ci/docker/README.html) |
| stages        | no       | Define build stages                      |
| types         | no       | Alias for `stages` (deprecated)          |
| before_script | no       | Define commands that run before each job's script |
| after_script  | no       | Define commands that run after each job's script |
| variables     | no       | Define build variables                   |
| cache         | no       | Define list of files that should be cached between subsequent runs |

job中有很多子配置，这些配置有些是必须的，有些是可选的，具体情况如下：

| Keyword       | Required | Description                              |
| ------------- | -------- | ---------------------------------------- |
| script        | yes      | Defines a shell script which is executed by Runner |
| image         | no       | Use docker image, covered in [Using Docker Images](https://docs.gitlab.com/ee/ci/docker/using_docker_images.html#define-image-and-services-from-gitlab-ciyml) |
| services      | no       | Use docker services, covered in [Using Docker Images](https://docs.gitlab.com/ee/ci/docker/using_docker_images.html#define-image-and-services-from-gitlab-ciyml) |
| stage         | no       | Defines a job stage (default: `test`)    |
| type          | no       | Alias for `stage`                        |
| variables     | no       | Define job variables on a job level      |
| only          | no       | Defines a list of git refs for which job is created |
| except        | no       | Defines a list of git refs for which job is not created |
| tags          | no       | Defines a list of tags which are used to select Runner |
| allow_failure | no       | Allow job to fail. Failed job doesn't contribute to commit status |
| when          | no       | Define when to run job. Can be `on_success`, `on_failure`, `always` or `manual` |
| dependencies  | no       | Define other jobs that a job depends on so that you can pass artifacts between them |
| artifacts     | no       | Define list of [job artifacts](https://docs.gitlab.com/ee/user/project/pipelines/job_artifacts.html) |
| cache         | no       | Define list of files that should be cached between subsequent runs |
| before_script | no       | Override a set of commands that are executed before job |
| after_script  | no       | Override a set of commands that are executed after job |
| environment   | no       | Defines a name of environment to which deployment is done by this job |
| coverage      | no       | Define code coverage settings for a given job |
| retry         | no       | Define how many times a job can be auto-retried in case of a failure |

job的配置子选项比较多，所以这边这介绍上面提到过的artifacts选项：

artifacts---指定文件或者文件夹列表，如果job构建成功，其他job可以获取到这些内容，或者可以直接在gitlab中进行下载。使用方法和cache类似。在不同job中使用artifacts可以配合dependencies选项来使用。可以参考实战演示中的例子。

> artifacts和cache的区别：
>
> - artifacts可以指定过期时间，cache为永久缓存
> - artifacts只在job成功后进行存储，并且只在被定义的job中进行存储，cache如果为全局的话每个job都会缓存一遍或者检查缓存内容是否变化

想要了解job以及其子选项的具体情况，请阅读[详细信息](https://docs.gitlab.com/ee/ci/yaml/README.html#jobs)

示例如下：

```
job_name:
  script:
    - rake spec
    - coverage
  stage: test
  only:
    - master
  except:
    - develop
  tags:
    - ruby
    - postgres
  allow_failure: true
```



## 实战演示

此次以一个前端vue构建的工程的CI/CD为示例，Gitlab Runner的tags为：node、docker，执行器选择的是shell，和docker集成的部分请参考第六节，.gitlab-ci.yml如下：

```
before_script:
  - npm install

build_job:
  stage: build
  script:
    - npm run build
  artifacts:
    paths:
      - dist/
    expire_in: 1 week
  tags:
    - node

test_job:
  stage: test
  script:
    - npm run e2e
  tags:
    - node

deploy_job:
  stage: deploy
  before_script:
    - docker login -u=xxx -p=xxx registry
  script:
    - mv -f dist $TARGET_DIR
    - docker build -t $TARGET_IMAGE.`date +%Y%m%d_%H%M%S` $TARGET_DIR
    - rm -rf $TARGET_DIR/dist
  after_script:
    - docker logout registry
  dependencies:
    - build_job
  when: manual
  tags:
    - docker
```

简单介绍：

从上面的.gitlab-ci.yml文件中可以看出，没有定义stages，使用默认的三个stage，执行构建的流程如下：

1. 执行build任务，如果构建成功，进入下一步，否则停止构建
2. 执行test任务，如果测试成功，进入下一步，否则停止构建
3. 进入deploy任务，如果部署成功，项目构建成功，否则停止构建

可以看出，因为我们定义了before_script所以每个job之前都会进行该操作，而在deploy中我们覆盖了该配置，那么在这个任务中之后执行覆盖的操作。



### 实战中遇到的问题

- Q：在shell中npm命令后面的其他命令无法正常执行，而之前的命令却没有影响

  A：在网上找了很久，最后发现这个由于npm的bug导致的，虽然知道问题出现在什么地方了，但是最终没有关注该bug是否已经修复，有兴趣可以自行了解一下，相关网址：

  https://stackoverflow.com/questions/41790917/gitlab-ci-npm-install-command-terminates-current-step

  https://github.com/npm/npm/issues/2938

- Q：centos上安装的gitlab-runner老是提示命令找不到，例如使用node或者npm命令

  问题描述：如上，但是可以确定系统中已经安装了node并且正确设置环境变量，可以使用--version查看node版本信息

  A：找了半天，在shell执行器的文档中找到了蛛丝马迹；当我们使用shell作为执行器的时候，不管linux系统上的用户是谁，即使是管理员拥有root权限，执行job时默认使用gitlab-runner这个用户来执行操做，而如果该用户没有相应的权限，是无法使用一些软件例如node的，解决办法：

  - 将gitlab-runner添加到能执行npm命令的用户组中，例如root用户组
  - 将gitlab-runner提权为root用户

  这两者选一个即可，相关[文档](https://docs.gitlab.com/runner/executors/shell.html#running-as-unprivileged-user)。
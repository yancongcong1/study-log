[TOC]

# Gitlab Runner



## 简介

Gitlab Runner被用来执行定义在.gitlab-ci.yml中的job任务，以及给用户返回执行结果。

Gitlab Runner分为两种：

- specific runner：这种runner只为特定的项目提供服务。
- shared runner：这种runner为所有的项目提供服务。





## Shared vs Specific Runners

> Shared Runners在多个项目中对那些有相同需求的job来说比较有用，这样的话你就可以只配置一小部分runner来处理多个项目，而不是在多个项目中让大多数的runner闲置下来。Shared Runners使用[fair queue](https://docs.gitlab.com/ee/ci/runners/README.html#how-shared-runners-pick-jobs)来保证可用的runner资源不被浪费掉。

> Specific Runners对于那些有着特殊需求的job或者有特定需求的项目比较有用。例如你想要部署一个特定的项目，那么你就可以设置一个特定的runner来执行特定的job。这种情况下你可以使用[tags](https://docs.gitlab.com/ee/ci/runners/README.html#using-tags)来辅助工作。Specific Runners使用[FIFO queue](https://en.wikipedia.org/wiki/FIFO_(computing_and_electronics))。

NOTE：你可以设置一个Specific Runner给多个项目使用，但是你必须在项目中明确启用该Runner来保证job的正确执行。



## 注册Runner

1、shared runner

​       只有gitlab的管理员才可以注册公共的Runner，这个的意思就是说除非你自己搭建gitlab服务器，否则如果你使用的是官方的gitlab，你就只能使用官方提供的那些公共Runner。

​       具体的步骤如下：

- 在`admin/runners`页面获取Shared-Runner的token

![哈哈](https://docs.gitlab.com/ee/ci/runners/img/shared_runners_admin.png)

- [注册Runner](http://docs.gitlab.com/runner/register/)

NOTE：在GitLab的8.2版本之后，Shared-Runner在项目中默认开启，但是你可以在项目中的**Settings ➔ CI/CD**页面中禁用它。在之前的版本中默认是关闭的。

2、Specific Runners

- [安装Runner](https://docs.gitlab.com/runner/install/index.html)，不同的操作系统有不同的安装方法


- 在**Settings ➔ CI/CD**页面获取项目的token

![](https://github.com/yancongcong1/study-log/blob/master/gitlab-ci/static/images/lesson3-1.png)

- [注册Runner](http://docs.gitlab.com/runner/register/)



## 更多信息

[更多关于Shared-Runners和Specific-Runners的信息](https://docs.gitlab.com/ee/ci/runners/README.html)



## Runner Command

[Runner的命令文档](https://docs.gitlab.com/runner/commands/README.html)




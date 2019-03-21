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



## 安装Runner

1、shared runner

​      只有gitlab的管理员才可以注册公共的Runner，这个的意思就是说除非你自己搭建gitlab服务器，否则如果你使用的是官方的gitlab，你就只能使用官方提供的那些公共Runner。

​      以下简单介绍一下如何安装shared runner(假设自己拥有管理员权限)，否则你可以直接去项目的**setting→CI/CD→Runners settings**中查看可用的公共runner。

​      如何安装自己的gitlab请参考[文档](https://docs.gitlab.com/ee/administration/index.html) 。

​      具体的步骤如下：

- 在`admin/runners`页面获取Shared-Runner的token

![](https://docs.gitlab.com/ee/ci/runners/img/shared_runners_admin.png)

- 注册Runner

NOTE：在GitLab的8.2版本之后，Shared-Runner在项目中默认开启，但是你可以在项目中的**Settings ➔ CI/CD**页面中禁用它。在之前的版本中默认是关闭的。

2、Specific Runners

***特别提示：如果你安装的版本是gitlab-runner 10或者之上，你需要将可执行文件重命名为gitlab-runner，如果是之前的版本，请参考[文档](https://docs.gitlab.com/runner/install/old.html) 。***

- [安装Runner](https://docs.gitlab.com/runner/install/index.html)，不同的操作系统有不同的安装方法


- 在**Settings ➔ CI/CD**页面获取项目的token

![](https://github.com/yancongcong1/blog/blob/master/gitlab-ci/static/images/lesson3-1.png)

- 注册Runner




## Runner Command

当你安装好自己的Runner后，Gitlab Runner会给你提供一些服务，你可以通过命令来使用Runner：

[Runner的命令文档](https://docs.gitlab.com/runner/commands/README.html)



## 注册Runner

当私有Runner安装完成后，需要将Runner注册到具体的项目中，注册的步骤官方提供的很详细了，[注册Runner](http://docs.gitlab.com/runner/register/)，不同的操作系统可能步骤不同。

注册成功后你可以在自己的项目中看到如下信息：

![](https://github.com/yancongcong1/blog/blob/master/gitlab-ci/static/images/lesson3-2.png)

上图中所有圈出的内容匹配则表示注册成功。



## 选择Executor

当你注册Runner时，最后一步通常需要选择一种executor，那么，executor又是什么东西呢？

从字面上理解，它是一种执行器，那么它又是执行什么的呢？它是用来在不同的场景中执行我们构建的任务的，包括各种脚本。

executor包含多种，一般我们使用shell，ssh或者docker，但是如果你不确定什么适合自己的话，你可以阅读下面的文档：

[I am not sure](https://docs.gitlab.com/runner/executors/README.html#i-am-not-sure)

[executor兼容性表](https://docs.gitlab.com/runner/executors/README.html#compatibility-chart)



如果你想要了解各个executor的详细信息，请阅读：

[各种executor的详细信息](https://docs.gitlab.com/runner/#selecting-the-executor)



## Runner配置文件

当Runner安装好之后，会有一个`config.toml`文件，不同操作系统有所不同，例如windows中和安装文件在统一目录，linux中在/etc/gitlab-runner下。

可以通过修改该文件直接修改注册时的一些信息。




## 更多信息

[更多关于Shared-Runners和Specific-Runners的信息](https://docs.gitlab.com/ee/ci/runners/README.html)



## 实战过程中遇到的问题

`由于项目代码在gitlab，所以没有贴上代码，简单介绍一下实战过程中遇到的问题：`

- Q：GitLab-Runner在windows上的搭建问题

> 问题描述：由于自己的操作问题，在安装runner时出现了问题，所以决定删除重安，但是一直出现如下错误：
>
> ![](https://github.com/yancongcong1/blog/blob/master/gitlab-ci/static/images/lesson3-3.png)

​	A：这个问题是因为删除的时候注册表没有删除干净，这时候我们只需要找到相应的注册表删除重新安装就可以了，[参考文章](https://www.cnblogs.com/jiaoyiping/p/5638506.html)，注册表位置：HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services

- Q：windows上启动Runner问题

> 问题描述：注册成功后运行gitlab-runner start启动服务，出现如下错误：
>
> ![](https://github.com/yancongcong1/blog/blob/master/gitlab-ci/static/images/lesson3-4.png)

​	A：在网上扒了扒，发现有这么[一篇文章](https://support.threattracksecurity.com/support/solutions/articles/1000071019-error-1053-the-service-did-not-respond-in-a-timely-fashion-when-attempting-when-attempting-to-star)，感觉与我的问题有些相似，于是尝试了一下，结果果然成功了，千万记得重启电脑。出现问题的原因是机器设置的服务响应时间太短了，加长一些就行了。为什么会出现这样的问题该文章中说的比较清楚，这儿就不多说了。

> **linux上Runner的安装和注册并没有出现什么问题，根据命令直接就搞定了。**
>



------

本节介绍了gitlab-runner，现在CI/CD的构建环境已经有了，下面我们就需要介绍一下构建的具体过程了，我们所有的构建过程都在.gitlab-ci.yml这个文件中定义，所以下一节我们将会集中讨论一下.gitlab-ci.yml文件作用以及如何进行配置。
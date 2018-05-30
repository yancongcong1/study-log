[TOC]

# Gitlab CI/CD概念之pipelines



## 简介

在Gitlab CI/CD中有几个比较重要的概念，理解了它们可能对于学习CI/CD提供一部分帮助，下面对它们简单介绍一下。



## Pipelines

一个pipeline是一组可以分阶段执行的jobs，也就是说，一个pipeline就是一个.gitlab-ci.yml定义的构建，它包括所有阶段，每一个stage中可以包含多个job。

如下图所示：

![](https://docs.gitlab.com/ee/ci/img/pipelines.png)

[详细信息](https://docs.gitlab.com/ee/ci/pipelines.html)



### Pipelines的类型

有三种类型的pipelines经常使用pipeline这个缩写，人们经常认为其中的每一个都是一个pipeline，但是它们仅仅是一个完整pipeline的一部分。

这三种pipeline的图示：

![](https://docs.gitlab.com/ee/ci/img/types-of-pipelines.svg)

1. Project Pipeline：可以理解为准备阶段，比如跨项目的CI，例如微服务，这就需要依赖 [triggered via API](https://docs.gitlab.com/ee/ci/triggers/README.html#ci-job-token)来完成了
2. CI Pipeline：一般包括build以及test阶段
3. Deploy Pipeline：部署阶段，即从开发到生产的转变


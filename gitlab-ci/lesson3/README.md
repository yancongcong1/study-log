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






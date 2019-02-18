[TOC]

# Kubernetes



## Kubernetes简介

Kubernetes是一个跨主机的容器调度平台，它可以自动化应用容器的部署、拓展和操作。最基础的，Kubernetes 可以在物理或虚拟机集群上调度和运行应用程序容器。然而，Kubernetes 还允许开发人员从物理和虚拟机’脱离’，从以**主机为中心**的基础架构转移到以**容器为中心**的基础架构，这样可以提供容器固有的全部优点和益处。Kubernetes 提供了基础设施来构建一个真正以**容器为中心**的开发环境。

kubernets可以做什么：

- [Pod](https://kubernetes.io/docs/user-guide/pods/) 提供复合应用并保留一个应用一个容器的容器模型,
- [挂载外部存储](https://kubernetes.io/docs/user-guide/volumes/),
- [Secret管理](https://kubernetes.io/docs/user-guide/secrets/),
- [应用健康检查](https://kubernetes.io/docs/tasks/configure-pod-container/configure-liveness-readiness-probes/),
- [副本应用实例](https://kubernetes.io/docs/user-guide/replication-controller/),
- [横向自动扩缩容](https://kubernetes.io/docs/user-guide/horizontal-pod-autoscaling/),
- [服务发现](https://kubernetes.io/docs/user-guide/connecting-applications/),
- [负载均衡](https://kubernetes.io/docs/user-guide/services/),
- [滚动更新](https://kubernetes.io/docs/user-guide/update-demo/),
- [资源监测](https://kubernetes.io/docs/user-guide/monitoring/),
- [日志采集和存储](https://kubernetes.io/docs/user-guide/logging/overview/),
- [支持自检和调试](https://kubernetes.io/docs/user-guide/introspection-and-debugging/),
- [认证和鉴权](https://kubernetes.io/docs/admin/authorization/).



## 组件介绍

1. ### master组件

   master提供集群的控制节点，它可以检测到集群中的事件以及做出相应的响应。master中的组件可以分别运行在集群中的任何一台机器上，但是一般将所有的组件运行在同一台机器上，并且该机器不会作为node节点来运行Pod。

   - kube-apiserver

     提供了HTTP Rest接口，是Kubernetes中所有资源增删改查操作的唯一入口，也是集群控制的入口进程。[更多信息](https://kubernetes.io/docs/setup/independent/high-availability/)

   - etcd

     作为存储组件来存储集群所有的资源对象。[更多信息](https://github.com/coreos/etcd/blob/master/Documentation/docs.md)

   - kube-scheduler

     负责Pod的调度，给Pod指定合适的node

   - kube-controller-manager

     Kubernetes中所有资源的控制中心，运行了所有资源的控制器进程。逻辑上Kubernets不同控制器的进程相互分离，但是为了减少复杂程度将所有的控制器进程放到同一个二进制包中一起运行。[更多信息](https://kubernetes.io/docs/concepts/overview/components/#kube-controller-manager)

2. ### node组件

   除了master的集群中的每一台机器被称为node节点。node组件需要运行在每一台node机上，主要用来运行Pod和提供一些运行环境。

   - kubelet

     kubelet负责Pod的创建、启停等任务，会定时向master节点汇报自身情况，从而实现集群管理的基本功能。

   - kube-proxy

     实现Kubernetes Service的通信和负载均衡。

   - Container Runtime

     负责运行容器的软件。Kubernetes支持以下这些环境：Docker、rkt、runc等。

除了上面的一些必须组件，Kubernetes还提供了一些可选的组件来加强服务能力：

- DNS

  DNS可以让我们访问Service的时候更加简洁，无需再配置Ip，只需要通过Service的名称的服务的端口号即可以正确访问服务。需要搭建DNS服务器并且在每台node机指定相应的DNS服务器。

- Web UI (Dashboard)

  提供界面化的管理。



## 名词解释

- Name

  Kubernetes中的对象通过Name和Uid进行唯一标识。对于**同一类型**的对象来说，Name只能有一个，当其被删除后才可以创建下一个对象。

- Namespace

  Namespace类似于Java中的package，给Name提供一个隔离区，同一Namespace下的Name不能相同，但是不同Namespace的Name可以相同。

- Label & Selector

  Label可以对Kubernetes中的对象进行标记，它由键值对组成，可以对一个对象进行多个标记，但是键的值必须唯一，可以对不同的对象设置相同的Label。

  [Label的语法和规则](https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/#syntax-and-character-set)

  Selector用来选择指定Label的对象，有两种类型的选择器：基于相等Label匹配和基于集合条件匹配。[更多信息](https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/#syntax-and-character-set)

- Annotation

  Annotation一般用来给对象添加非标记类的元数据，例如构建发布的镜像信息、日志信息、工具信息等。跟Label类似，由键值对组成，[语法规则](https://kubernetes.io/docs/concepts/overview/working-with-objects/annotations/#syntax-and-character-set)

  > 注意：Annotation不会被Kubernetes直接引用，其主要目的是方便用户阅读查找。


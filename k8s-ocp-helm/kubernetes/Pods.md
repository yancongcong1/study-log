[TOC]

# Pods

Pod是Kubernetes中可以创建和部署的最小最简单的对象单元，在集群中作为一个独立的进程运行。

一个Pod中包含了应用程序的容器(一个或者多个)、存储资源、唯一的网络IP和控制容器如何运行的配置。



## 类型

Kubernetes集群中的Pod可以分为两类：

- 只运行一个容器

  在这个模式中，一个Pod中只运行一个容器，你可以将Pod看成Container的包装器，Kubernetes只会直接操控Pod而不会操控Container。

- 运行需要共同合作的多个容器

  Pod可以封装有多个耦合且需要共享资源的容器组成的应用程序。Pod可以将这些容器和存储资源打包成可以管理的实体。

下面是一些Pod应用实践：

- [The Distributed System Toolkit: Patterns for Composite Containers](https://kubernetes.io/blog/2015/06/the-distributed-system-toolkit-patterns)
- [Container Design Patterns](https://kubernetes.io/blog/2016/06/container-design-patterns)



## 资源共享方式

Pod为其组成容器提供两种共享资源，Networking和Storage。



### Networking

每个Pod都会被分配一个唯一的IP地址。Pod中的每个容器都共享网络命名空间，包括IP地址和端口。Pod中的容器可以使用localhost来彼此通信。当Pod中的容器与Pod外的实体通信时必须协调好如何使用共享网络资源(例如端口号)。



### Storage

Pod可以指定一组共享存储卷，Pod中的所有容器都可以访问这个共享卷，从而允许容器共享数据。



## Pod Template

Pod模板是包含在其他对象中的Pod规范，Controllers通过Pod模板来生成实际的Pod实例。

[Pod模板语法](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#pod-v1-core)
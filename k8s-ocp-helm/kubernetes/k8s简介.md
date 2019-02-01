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



## 名词解释


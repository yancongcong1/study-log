[TOC]

# Service

Service是微服务架构中的一个“微服务”。其中有几个重要的概念：Service、Endpoint、Ingress。



## Service

**Kubernetes** Pod 是有生命周期的，它们可以被创建，也可以被销毁，这个过程中每个 Pod 都会获取它自己的 IP 地址，即这些 IP 地址不总是稳定可依赖的。 这会导致一个问题：在 Kubernetes 集群中，如果一组 Pod（称为 backend）为其它 Pod （称为 frontend）提供服务，那么那些 frontend 该如何发现，并连接到这组 Pod 中的哪些 backend 呢？

Service就是这样定义了一组Pod逻辑上的抽象。这一组Pod可以被Service访问，frontend可以通过访问Service获取服务而无需关心backend细节，因此实现了frontend和backend的解耦。

根据有没有定义selector，Service代理服务可以分为两类：

- 未定义selector，这类型的服务一般不属于集群内部，例如：
  1. 希望在生产环境中使用外部的数据库集群，但测试环境使用自己的数据库；
  2. 正在将工作负载转移到 Kubernetes 集群，和运行在 Kubernetes 集群之外的 backend；
- 定义了selector，这类型服务一般为集群服务，通过selector选取合适的Pod作为backend。

[Service字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#service-v1-core)

[Service其他用法](https://kubernetes.io/docs/concepts/services-networking/service/)



### 服务发布

Service可以通过设置ServiceTypes字段的值来选择服务发布的方式，可以分为以下几种：

- `ClusterIP`：通过集群的内部 IP 暴露服务，选择该值，服务只能够在集群内部可以访问，这也是默认的 `ServiceType`。
- `NodePort`：通过每个 Node 上的 IP 和静态端口（`NodePort`）暴露服务。`NodePort` 服务会路由到 `ClusterIP` 服务，这个 `ClusterIP` 服务会自动创建。通过请求 `<NodeIP>:<NodePort>`，可以从集群的外部访问一个 `NodePort` 服务。
- `LoadBalancer`：使用云提供商的负载局衡器，可以向外部暴露服务。外部的负载均衡器可以路由到 `NodePort` 服务和 `ClusterIP` 服务。
- `ExternalName`：通过返回 `CNAME` 和它的值，可以将服务映射到 `externalName` 字段的内容（例如， `foo.bar.example.com`）。 没有任何类型代理被创建，这只有 Kubernetes 1.7 或更高版本的 `kube-dns` 才支持。

[服务发布详细信息](https://kubernetes.io/docs/concepts/services-networking/service/#publishing-services-service-types)



### 服务发现

kuberbetes支持两种服务发现，环境变量和DNS：

- **环境变量**

  当启动Pod时，k8s会将每一个活跃Service的相关信息以一定规范的命名方式注入Pod的环境变量中，例如host和port为 `{SVCNAME}_SERVICE_HOST` 和 `{SVCNAME}_SERVICE_PORT` 。这时候就可以通过环境变量方便的访问服务了。

- **DNS**

  如果安装的DNS服务器并且DNS被启用的话，可以通过Service的名称和namespace来访问服务。如何找到Service的Cluster IP是DNS服务器帮我们做的事情。

  [DNS相关介绍](https://kubernetes.io/docs/concepts/services-networking/dns-pod-service/)

[服务发现相关文档](https://kubernetes.io/docs/concepts/services-networking/service/#discovering-services)



## Endpoint

Endpoint表示了一个Service对应的所有Pod副本的访问地址。对于定义了selector的Service，Endpoint Controller会自动监听Pod副本的变化并修改或者创建对应的Endpoint；没有定义selector的Service需要手动创建Endpoint。

[Endpoint字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#endpoints-v1-core)



## Ingress

Service和Pod的IP仅可在集群内部访问。集群外部的请求需要通过负载均衡转发到Service在Node上暴露的NodePort上，然后再由kube-proxy将其转发给相关的Pod。

Ingress为进入集群的请求提供路由规则。Ingress可以给service提供集群外部访问的URL、负载均衡、SSL终止、HTTP路由等。为了配置这些Ingress规则，集群管理员需要部署一个[Ingress controller](https://kubernetes.io/docs/concepts/services-networking/ingress/#ingress-controllers)，它监听Ingress和service的变化，并根据规则配置负载均衡并提供访问入口。

[Ingress字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#ingress-v1beta1-extensions)

[Ingress详细信息](https://kubernetes.io/docs/concepts/services-networking/ingress/)
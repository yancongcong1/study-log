[TOC]

# Configuration

本文主要介绍一下Kubernetes中常用的一些辅助配置对象，包括Secret和ConfigMap。

[Kubernetes配置最佳实践](https://kubernetes.io/docs/concepts/configuration/overview/)



## Secrets

Secret是Kubernetes中包含敏感数据的对象，例如密码、token或者密钥。将这些敏感数据放入Secret比直接放入Pod定义或者image中要更加安全。关于Secret的运作机制请查阅[文档](https://github.com/kubernetes/community/blob/master/contributors/design-proposals/auth/secrets.md)。



### Secret语法

Secret的内容由key/value对组成，字段类型有两种：data和stringData。

data必须存放使用base64编码过的内容，stringData则可以存放没有进行过编码的内容。

> 注意：stringData只是方便编写对象模板时使用，当我们使用命令查看Secret模板内容的时候，其输出结果是经过base64编码后的结果。

> 注意：当data和stringData中同时定义了一个相同的key时，将会使用stringData中定义的内容。

[Secret接口规范](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#secret-v1-core)



### Secret使用

Pod需要添加相关的引用才可以使Secret发挥作用。有两种方式：

- 作为挂在卷(Volume)上的一个文件被使用：

  1. 将Secret内容直接挂在到卷(Volume)中，这种情况下会根据Secret中的键给指定卷中生成相应的文件，每个键生成单一的文件，文件名为键的名字，文件内容为键的值。[详细信息](https://kubernetes.io/docs/concepts/configuration/secret/#using-secrets-as-files-from-a-pod)

  2. 可以指定根据键生成的挂在文件的名称，这种情况下文件的名字为指定path名字而不再是键的名字了，当然文件内容还是键的值。[详细信息](https://kubernetes.io/docs/concepts/configuration/secret/#using-secrets-as-files-from-a-pod)

     > 注意：此时如果想要为每一个键都生成一个文件，需要对每个键都显示声明。

- 作为容器的环境变量被使用，[详细信息](https://kubernetes.io/docs/concepts/configuration/secret/#using-secrets-as-environment-variables)



## ConfigMap

ConfigMap和Secret类似，只不过它不存储敏感信息，只是用来存储配置信息。



### ConfigMap语法

ConfigMap的内容由key/value对组成，字段类型有两种：data和binaryData。

data存储使用UTF-8编码的内容，binaryData用来存储二进制数据。

> 注意：在这边需要注意的是ConfigMap中data和binaryData中的key不允许重复。



### ConfigMap使用

ConfigMap一般有以下几种用法：

- 用作Pod中容器的环境变量
  1. 从单个ConfigMap对象中获取配置信息，[详细信息](https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/#define-a-container-environment-variable-with-data-from-a-single-configmap)
  2. 从多个ConfigMap对象中获取配置信息，[详细信息](https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/#define-container-environment-variables-with-data-from-multiple-configmaps)
  3. 将一个ConfigMap对象中的键值对自动映射为环境变量的名称和值，[详细信息](https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/#configure-all-key-value-pairs-in-a-configmap-as-container-environment-variables)
  4. 容器启动命令中使用从ConfigMap对象中获取的环境变量，[详细信息](https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/#use-configmap-defined-environment-variables-in-pod-commands)
- 将ConfigMap中字段挂载到卷(Volume)中
  1. 将ConfigMap内容直接挂在到卷(Volume)中，这种情况下会根据ConfigMap中的键给指定卷中生成相应的文件，每个键生成单一的文件，文件名为键的名字，文件内容为键的值。[详细信息](https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/#populate-a-volume-with-data-stored-in-a-configmap)
  2. 可以指定根据键生成的挂在文件的名称，这种情况下文件的名字为指定path名字而不再是键的名字了，当然文件内容还是键的值。[详细信息](https://kubernetes.io/docs/tasks/configure-pod-container/configure-pod-configmap/#add-configmap-data-to-a-specific-path-in-the-volume)
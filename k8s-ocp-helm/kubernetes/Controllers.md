[TOC]

# Controllers

Controllers在kubernetes中扮演者一个管理者的角色，主要用来管理pod的启动与销毁。接下来简单介绍一下所有Controller的作用以及之间的区别。

kubernetes对象通用必需字段：`apiVersion`， `kind`， `metadata`



## ReplicationController

ReplicationController确保同一时间运行指定数目的pod副本。

如果启动的pod数过多，ReplicationController会自动终止多余的pod；如果pod数不足，ReplicationController会自动启动pod直到达到要求。当pod失败、被删除或者终止的时候，ReplicationController会自动创建新的pod，例如当你的node机器由于kernel升级导致pod终止，这些pod会被自动重建。基于这个原因无论你的应用是否只有一个pod都建议使用ReplicationController。

ReplicationController功能类似于process supervisor，但是与之不同的是ReplicationController可以在多个node之间进行管理，而process supervisor只能在单独的node进行管理。

[ReplicationController字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#replicationcontroller-v1-core)

[ReplicationController其他用法](https://kubernetes.io/docs/concepts/workloads/controllers/replicationcontroller/)



## ReplicaSet

ReplicaSet是第二代的ReplicationController，ReplicaSet和ReplicationController之间的唯一区别是ReplicaSet支持基于集合的选择器，而ReplicationController只支持基于相等匹配的选择器。

虽然ReplicaSet可以单独使用，但是接下来要介绍的Deployment是一个更加高级的概念，它同样可以管理pod副本并同时向pod提供声明性的更新以及许多其他有用的功能。

[选择器详解](https://kubernetes.io/docs/concepts/overview/working-with-objects/labels/)

[ReplicaSet字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#replicaset-v1-apps)

[ReplicaSet其他用法](https://kubernetes.io/docs/concepts/workloads/controllers/replicaset/)



## Deployment

Deployment给Pod和ReplicaSet提供声明式的更新。无需进行ReplicaSet的声明，Deployment会自动创建ReplicaSet并对其进行命名。

Deployment最大的便利就是我们可以随时知道当前Pod的部署进度。由于一个Pod的创建、调度、绑定节点以及在目标node上启动对应的容器都需要一定的时间，所以我们期待的启动n个Pod副本的目标状态其实是一个连续变化的“部署过程”导致的最终状态。

如果你想要更新Pod，只需要更新Deployment中的templete就可以了。

[Deployment字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#deployment-v1-apps)

[Deployment其他用法](https://kubernetes.io/docs/concepts/workloads/controllers/deployment/#creating-a-deployment)



## Job

Job负责处理短暂的一次性任务，这个任务只被执行一次，它保证批处理任务的一个或者多个Pod成功结束。当任务执行成功后，Job和Pod不会被删除而是保存下来方便查看日志以及状态。

Job Controller负责根据Job Spec创建Pod，并持续监控Pod的状态，直至其成功结束。如果失败，则根据restartPolicy（只支持OnFailure和Never，不支持Always）决定是否创建新的Pod再次重试任务。

根据.spec.completions和.spec.Parallelism的设置，可以将Job划分为以下几种pattern：

| Job类型               | 使用示例                | 行为                                         | completions | Parallelism |
| :-------------------- | ----------------------- | -------------------------------------------- | ----------- | ----------- |
| 一次性Job             | 数据库迁移              | 创建一个Pod直至其成功结束                    | 1           | 1           |
| 固定结束次数的Job     | 处理工作队列的Pod       | 依次创建一个Pod运行直至completions个成功结束 | 2+          | 1           |
| 固定结束次数的并行Job | 多个Pod同时处理工作队列 | 依次创建多个Pod运行直至completions个成功结束 | 2+          | 2+          |
| 并行Job               | 多个Pod同时处理工作队列 | 创建一个或多个Pod直至有一个成功结束          | 1           | 2+          |

[Job字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#job-v1-batch)

[Job其他用法](https://kubernetes.io/docs/concepts/workloads/controllers/jobs-run-to-completion/)



## CronJob

CronJob即定时任务，在指定的周期内执行指定的任务。

### 限制

在CronJob中有一些使用限制，具体情况轻阅读以下：

- [Cron Job Limitations](https://kubernetes.io/docs/concepts/workloads/controllers/cron-jobs/#cron-job-limitations)
- [what startingDeadlineSeconds  mean?](https://stackoverflow.com/questions/51065538/what-does-kubernetes-cronjobs-startingdeadlineseconds-exactly-mean)

[CronJob字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#cronjobspec-v1beta1-batch)

[CronJob其他用法](https://kubernetes.io/docs/concepts/workloads/controllers/cron-jobs/)



## DaemonSet

DaemonSet保证每个Node上都运行一个容器副本，通常用来部署集群的日志，监控或其他系统应用。

典型的应用包括：

- 系统程序
- 日志收集
- 系统监控

DaemonSet默认在所有的Node上都运行容器，可以通过设置nodeSelector或者affinity来指定运行容器的Node。

[DaemonSet字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#daemonset-v1-apps)

[DaemonSet其他用法](https://kubernetes.io/docs/concepts/workloads/controllers/daemonset/)



## StatefulSet

StatefulSet用来管理有状态的应用(相对于Deployment和RC的无状态)。应用场景有：

- 稳定的持久化存储
- 稳定的网络标识
- 有序部署、有序拓展，即Pod是有序的
- 有序收缩、删除

StatefulSet的主要组成部分：

- 一个Headless Service用于定义网络标识(DNS domain)
- 用于创建PersistentVolumes的volumeClaimTemplates
- StatefulSet定义文件

[StatefulSet字段详解](https://kubernetes.io/docs/reference/generated/kubernetes-api/v1.13/#statefulset-v1-apps)

[StatefulSet其他用法](https://kubernetes.io/docs/concepts/workloads/controllers/statefulset/)

[StatefulSet的zookerper用例](https://kubernetes.io/docs/tutorials/stateful-application/zookeeper/)
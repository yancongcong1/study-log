[TOC]

# Storage

在Kubernetes中也有一个Volume的概念，它据有和Pod相同的生命周期，用来解决下面的两个问题：

1. docker中Vloume当容器删除时数据随之清除的问题
2. 多个docker容器共享数据的问题

通过设置Pod的`.spec.volumes`和Container的`.spec.containers.volumeMounts`可以进行Volume的挂载。



## Volume类型

- [awsElasticBlockStore](https://kubernetes.io/docs/concepts/storage/volumes/#awselasticblockstore)
- [azureDisk](https://kubernetes.io/docs/concepts/storage/volumes/#azuredisk)
- [azureFile](https://kubernetes.io/docs/concepts/storage/volumes/#azurefile)
- [cephfs](https://kubernetes.io/docs/concepts/storage/volumes/#cephfs)
- [configMap](https://kubernetes.io/docs/concepts/storage/volumes/#configmap)---详细信息参照Configuration中的ConfigMap
- [csi](https://kubernetes.io/docs/concepts/storage/volumes/#csi)
- [downwardAPI](https://kubernetes.io/docs/concepts/storage/volumes/#downwardapi)
- [emptyDir](https://kubernetes.io/docs/concepts/storage/volumes/#emptydir)---Pod启动时会在当前Node节点挂载一个空目录，该目录与Pod的声明周期相同。
- [fc (fibre channel)](https://kubernetes.io/docs/concepts/storage/volumes/#fc)
- [flexVolume](https://kubernetes.io/docs/concepts/storage/volumes/#flexVolume)
- [flocker](https://kubernetes.io/docs/concepts/storage/volumes/#flocker)
- [gcePersistentDisk](https://kubernetes.io/docs/concepts/storage/volumes/#gcepersistentdisk)
- [gitRepo (deprecated)](https://kubernetes.io/docs/concepts/storage/volumes/#gitrepo)
- [glusterfs](https://kubernetes.io/docs/concepts/storage/volumes/#glusterfs)
- [hostPath](https://kubernetes.io/docs/concepts/storage/volumes/#hostpath)---会将当前Pod所在的主机节点中的一个文件系统(目录或者文件)挂载到Pod中，分为多种类型。一般用来开发和测试。
- [iscsi](https://kubernetes.io/docs/concepts/storage/volumes/#iscsi)
- [local](https://kubernetes.io/docs/concepts/storage/volumes/#local)---跟hostPath功能类似，测试功能。
- [nfs](https://kubernetes.io/docs/concepts/storage/volumes/#nfs)
- [persistentVolumeClaim](https://kubernetes.io/docs/concepts/storage/volumes/#persistentvolumeclaim)---允许将一个PersistentVolume挂载到Pod中，PersistentVolume是一种用户在不了解特定与环境细节的情况下声明持久存储的方法，具体请参考下节内容。
- [projected](https://kubernetes.io/docs/concepts/storage/volumes/#projected)---可以将多个卷资源映射到同一个Pod目录中。卷必需是`secret`，`configMap`，`downwardAPI`中的一种。
- [portworxVolume](https://kubernetes.io/docs/concepts/storage/volumes/#portworxvolume)
- [quobyte](https://kubernetes.io/docs/concepts/storage/volumes/#quobyte)
- [rbd](https://kubernetes.io/docs/concepts/storage/volumes/#rbd)
- [scaleIO](https://kubernetes.io/docs/concepts/storage/volumes/#scaleio)
- [secret](https://kubernetes.io/docs/concepts/storage/volumes/#secret)---详细信息请参照Configuration中的Secret。
- [storageos](https://kubernetes.io/docs/concepts/storage/volumes/#storageos)
- [vsphereVolume](https://kubernetes.io/docs/concepts/storage/volumes/#vspherevolume)

> 注意：直接在Pod中挂载Volume的方式成为静态绑定，在这种情况下Volume生命周期与Pod相同。



## PersistentVolume系统

不同于直接挂载到Pod的Volume，PersistentVolume子系统为用户提供了一个API，用来抽象出存储方式的实现细节，并且可以使Volume的声明周期独立于任何使用它们的Pod。接下来我们将介绍与之相关的Kubernetes中的另外三个资源：`PersistentVolumeClaim`、`PersistentVolume`和`StorageClasses`。



### PersistentVolumeClaim

PersistentVolumeClaim(PVC)是用户对存储请求细节的定义，也就是用户Pod使用PersistentVolume的入口资源。类似于Pod消耗node资源一样，PVCs会消耗PV资源。Pod可以请求特定级别的资源(CPU和内存)。Claims也可以请求特定的存储空间大小和访问模式(例如只挂载一次读/写请求或者挂载多次只读请求)。

对于不同的场景，用户通常需要具有不同属性的Volume。管理员需要提供不同存储空间和访问模式的各种Volume并且无需让用户了解其实现细节，这时候可以使用Kubernetes中的StorageClasses资源来解决该问题。

关于PVC的详细语法请参考[文档](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#persistentvolumeclaims)。



### PersistentVolume

PersistentVolume(PV)是集群中由管理员提供的一个存储实现。PV是一种卷插件，用来定义存储的实现细节。其定义的存储可以是NFS、ISCSI，也可以是特定于指定云提供的存储系统。

PersistentVolume(PV)用来静态创建PV资源，创建后的PV资源不可修改。

关于PV的详细语法请参考[文档](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#persistent-volumes)。



### StorageClass

StorageClass用来创建动态PV资源，也就是说如果使用StorageClass定义存储资源时，PVC在绑定PV时会根据需要自动创建所需的PV资源，当然该PV资源类型是基于指定的底层存储的(例如AWS EBS，Azure磁盘或者Cinder卷)。

关于StorageClass的详细语法请参考[文档](https://kubernetes.io/docs/concepts/storage/storage-classes/#the-storageclass-resource)。



### PVCs和PVs的生命周期

#### Provisioning(供应)

PV可以通过两种方式来进行提供：statically 和 dynamically.

- statically

  集群管理员定义一组PVs，它们定义了集群中用户可以使用存储的细节，可以通过Kubernetes API进行使用。

- dynamically

  当PVC在集群中匹配不到static PV时，集群会尝试动态的为PVC提供一个volume。动态供应PV的方式基于Kubernetes的StorageClasses资源：管理员必须创建并配置好StorageClasses以便PVC可以请求到该资源。PVC请求类为`""`时表示该PVC禁用动态供应方式。

  使用动态供应必须开启admission controller，更多信息请参考 [kube-apiserver文档](https://kubernetes.io/docs/admin/kube-apiserver/)

#### Binding(绑定)

假设用户已经创建了一个动态供应的PV和一个指定容量和请求的PVC，那么master节点的控制器会不断的检测是否有符合PVC请求的PV被提供，如果有就会将他们绑定在一起；如果没有匹配到符合要求的卷，PVC会一直保持未绑定状态。

如果PV是动态提供的，控制器会直接将PVC和它绑定，否则会将与PVC最小请求匹配的PV进行绑定。一旦绑定成功后，PV将会被PVC独占使用，它们之间是一对一的关系。

#### Using(使用)

集群通过检查Claim来找到绑定卷并将之挂载到Pod，对于支持多种访问模式的卷，用户在Pod中指明所用的模式。详细语法请参照[文档](https://kubernetes.io/docs/concepts/storage/persistent-volumes/#claims-as-volumes)。

#### Reclaiming(回收)

当用户处理完Volume后，他们可以从允许回收资源的API中删除PVC对象。PV对象的回收政策告诉集群当Volume被PVC释放后应该被如何处理。目前Volume支持三种回收策略：`Retained`，`Recycled`和`Deleted`。

##### Retain

Retain回收策略允许手动回收资源。当PVC被删除后，PV处于`released`状态，此时PV仍然不能被灵位的PVC绑定因为之前的PVC数据仍然保留在volume上。管理员可以通过以下步骤手动回收volume：

1. 删除PV资源。在删除PV后，外部设备中的相关存储资源仍然存在(例如AWS EBS，Azure磁盘或者Cinder卷)。
2. 手动清除相关储存资源上的数据。
3. 手动删除存储资源，如果你想要再次使用该存储资源，可以为该存储资源重新定义一个PV。

##### Delete

Delete回收策略会同时删除PV资源以及外部设施中的储存资源(例如AWS EBS，Azure磁盘或者Cinder卷)。动态提供的Volume会继承StorageClass资源的默认策略Delete，管理员应该根据实际情况配置StorageClass的回收策略。

##### Recycle

Recycle回收策略会先对卷执行一个基本的删除工作(rm -rf /thevolume/*)，然后将其用于新的PVC。

管理员可以通过修改容器启动的命令参数来自定义回收Volume的方式，例如：

```
apiVersion: v1
kind: Pod
metadata:
  name: pv-recycler
  namespace: default
spec:
  restartPolicy: Never
  volumes:
  - name: vol
    hostPath:
      path: /any/path/it/will/be/replaced
  containers:
  - name: pv-recycler
    image: "k8s.gcr.io/busybox"
    command: ["/bin/sh", "-c", "test -e /scrub && rm -rf /scrub/..?* /scrub/.[!.]* /scrub/*  && test -z \"$(ls -A /scrub)\" || exit 1"]
    volumeMounts:
    - name: vol
      mountPath: /scrub
```



### PV的状态

基于PV的声明周期，PV拥有以下几种状态：

- Available：PV被创建成功并且未被绑定或者被回收
- Bound：PV被绑定
- Released：PV资源被释放(解除绑定状态)
- Failed：PV资源创建失败
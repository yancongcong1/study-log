[TOC]

# Storage

在Kubernetes中也有一个Volume的概念，它据有和Pod相同的声明周期，用来解决下面的两个问题：

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
- [hostPath](https://kubernetes.io/docs/concepts/storage/volumes/#hostpath)---
- [iscsi](https://kubernetes.io/docs/concepts/storage/volumes/#iscsi)
- [local](https://kubernetes.io/docs/concepts/storage/volumes/#local)---
- [nfs](https://kubernetes.io/docs/concepts/storage/volumes/#nfs)
- [persistentVolumeClaim](https://kubernetes.io/docs/concepts/storage/volumes/#persistentvolumeclaim)
- [projected](https://kubernetes.io/docs/concepts/storage/volumes/#projected)
- [portworxVolume](https://kubernetes.io/docs/concepts/storage/volumes/#portworxvolume)
- [quobyte](https://kubernetes.io/docs/concepts/storage/volumes/#quobyte)
- [rbd](https://kubernetes.io/docs/concepts/storage/volumes/#rbd)
- [scaleIO](https://kubernetes.io/docs/concepts/storage/volumes/#scaleio)
- [secret](https://kubernetes.io/docs/concepts/storage/volumes/#secret)---详细信息请参照Configuration中的Secret
- [storageos](https://kubernetes.io/docs/concepts/storage/volumes/#storageos)
- [vsphereVolume](https://kubernetes.io/docs/concepts/storage/volumes/#vspherevolume)
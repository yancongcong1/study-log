[TOC]

# DOM_OBSERVER

本节介绍的接口用来监听DOM节点的变化并且可以做出反应。



## MutationObserver

提供对DOM所做更改进行监听的能力，可以检测到更改类型

### 构造函数

- MutationObserver()---传入一个callback，用来指定监听到DOM更改时执行的回调。

### 方法

- observe()---为观察者对象指定一个观察对象以及指定相应的观察配置。观察者只会监听配置中指定的更改。

  > NOTE：可以为同一个观察者对象指定不同的DOM节点来监听，如果重复指定相同的DOM节点，之前的观察者会被移除。

  > NOTE：除非调用disconnect函数，否则无论被监听的节点被移动或者移除，该监听器一直存在。

- disconnect()---停止观察者对象对所有目标的观察。

- takeRecords()---返回一个还未来得及被回调函数处理的MutationRecord对象列表。最长见的做法是在调用disconnect方法之前获取所有被挂起的记录，以便在停止观察之前能正确处理掉这些记录。



## MutationRecord

该接口包含了一个监听对象的相关变动。

### 属性

- type---表明DOM节点变更的类型。
- target---与type相关，表明节点改变后的信息(发生改变的触发节点，而不一定是被监听的节点)。
- oldValue---与type相关，表明节点改变之前的信息(发生改变的触发节点，而不一定是被监听的节点)。
- addedNodes---返回添加的节点列表，如果没有则返回null。
- removedNodes---返回删除的节点列表，如果没有则返回null。
- previousSibling---返回添加或者删除节点的前一个节点，如果没有返回null。
- nextSibling---返回添加或者删除节点的下一个节点，如果没有返回null。
- attributeName---返回改变属性的名称或者null。
- attributeNamespace---返回改变属性的namespace或者null。

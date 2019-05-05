[TOC]



# beforeDestory钩子函数获取元素问题



## 问题描述

组件的销毁会触发beforeDestory钩子函数，但是有很多种组件销毁触发条件。我们这边的问题主要是通过v-if指令来触发组件的销毁的时候，这时候通过 beforeDestory函数获取元素时会出现问题，无法获取到元素或者获取到的元素状态错误(元素属性出现错误)


## 问题分析

查看官方文档：

- [ref特性](https://cn.vuejs.org/v2/api/#ref)

其实就是在使用组件的时候添加ref，例如：

```
<child-component ref="child"></child-component>
```

这时候可以通过ref属性在beforeDestory函数中获取到该组件的正确状态：

```
this.$refs.child(DOM NODE ELEMENT)
```
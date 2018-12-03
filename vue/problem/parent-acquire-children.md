[TOC]



# 父组件访问子组件实例的问题



## 问题描述

在vue中如何在父组件中访问子组件实例(元素、变量、方法)？


## 问题分析

查看官方文档：

- [父组件中访问子组件实例](https://cn.vuejs.org/v2/guide/components-edge-cases.html#%E8%AE%BF%E9%97%AE%E5%AD%90%E7%BB%84%E4%BB%B6%E5%AE%9E%E4%BE%8B%E6%88%96%E5%AD%90%E5%85%83%E7%B4%A0)
- [ref特性](https://cn.vuejs.org/v2/api/#ref)

其实就是在使用子组件的时候添加ref，例如：

```
<child-component ref="child"></child-component>
```

然后就可以在父组件js中直接调用子组件实例了：

```
this.$refs.child.focus()
```
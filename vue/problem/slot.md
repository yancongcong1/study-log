[TOC]



# 父组件访问子组件实例的问题



## 问题描述

对于公共组件，有时候需要在不同的引用处定制个性化内容。由于组件是公共使用的，直接修改组件是行不通的。当然我们可以通过给组件传进一个为type的`props`，然后根据v-if来进行分别判断，但是这样做的话会使组件很臃肿；这时候我们就需要用到Vue的另一个内置组件了------slot(插槽)。


## 问题分析

查看官方文档：

- [api文档](https://cn.vuejs.org/v2/api/#slot-1)
- [实例属性api文档](https://cn.vuejs.org/v2/api/#vm-slots)
- [组件通过插槽分发内容](https://cn.vuejs.org/v2/guide/components.html#%E9%80%9A%E8%BF%87%E6%8F%92%E6%A7%BD%E5%88%86%E5%8F%91%E5%86%85%E5%AE%B9)
- [插槽](https://cn.vuejs.org/v2/guide/components-slots.html)
- [渲染函数中的插槽](https://cn.vuejs.org/v2/guide/render-function.html#%E6%8F%92%E6%A7%BD)

插槽(slot)的使用其实很简单，官方文档中也讲述的相当清楚。在公共组件中定义插槽，在引用时通过给组件传入相应的插槽内容来替换掉公共组件中的内容就ok了。例如下面的示例：

```
<div class="container">
  <header>
    <slot name="header"></slot>
  </header>
  <main>
    <slot></slot>
  </main>
  <footer>
    <slot name="footer"></slot>
  </footer>
</div>
```

使用时只要这样写就行了：

```
<base-layout>
  <template slot="header">
    <h1>Here might be a page title</h1>
  </template>

  <p>A paragraph for the main content.</p>
  <p>And another one.</p>

  <template slot="footer">
    <p>Here's some contact info</p>
  </template>
</base-layout>
```

可以根据插槽属性通过vue实例来访问插槽内容，例如：

```
vm.$slots.foo 可以访问到 slot="foo" 的插槽内容
```

详细信息请参考官方文档。
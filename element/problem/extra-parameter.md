[TOC]



# 组件事件如何添加自定义参数



## 问题描述

element对元素的事件回调进行了重写，回调参数也进行了修改，如果在触发回调时添加了自定义的参数，那么默认的回调参数会被覆盖。具体如下：

```
<el-select @change="changeUserRole(operator.id)>
</el-select>
```



## 解决方法

可以使用以下两种方式来解决该问题：

- 使用$event

  可以在自定义参数前添加$event，然后再传其它值，这样就可以同时使用默认参数和自定义参数了。例如：

  ```
  <el-select @change="changeUserRole($event, operator.id)>
  </el-select>
  ```

  这是vue提供的在内联处理语句中访问原始DOM时间的方法。

- 多次封装

  可以在回调处多添加一层回调来进行封装，如下：

  ```
  <el-select @change="val => changeUserRole(val, operator.id)>
  </el-select>
  ```
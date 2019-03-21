# cursor和pointer-events冲突

今天写组件的时候突然发现同一个元素如果同时设置`cusor: not-allowed`和`pointer-events: none`会出现冲突，结果是`cursor`失效，`pointer-events`生效。



## 描述

对于ul标签下的li标签，想要在满足某种条件的时候cursor为not-allowed，并且点击事件无法触发。代码如下：

```html
<ul>
    <li click="recall">撤回</li>
</ul>
```

现在当条件满足的时候设置li的样式如下：

```css
li {
    background-color: #f5f5f5;
    color: #BBBBBB;
    cursor: not-allowed;
    pointer-events: none;
}
```

结果发现上述问题，但是cursor和pointer-evnets单独设置时都可以正常生效。



## 解决

1. 将两个功能分别实现

   `cursor`功能还是在css中设置，但是函数不生效可以在recall函数中设置，代码如下：

   ```
   recall (event) {
       event.preventDefault();
   }
   ```

   或者移除li标签的onclick事件：

   ```
   $('.disableCss').removeAttr('onclick');
   ```

   当然这边移除事件的写法可以根据你使用的lib决定。

2. `cursor`和`pointer-events`分别写在不同的元素中

   修改html如下：

   ```html
   <ul>
       <li><span click="recall">撤回</span></li>
   </ul>
   ```

   css如下：

   ```
   li {
       background-color: #f5f5f5;
       color: #BBBBBB;
       cursor: not-allowed;
   }
   span {
       pointer-events: none;
   }
   ```

   这样`cursor`和`pointer-events`分别作用于不同的元素，就不会产生冲突了。当然你可以修改span的其他样式来使之满足长宽的要求。

总之，这两种方法的原理都是将cursor和pointer-events这两个样式分开设置，不让它们作用在同一个元素上。
















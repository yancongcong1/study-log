[TOC]

# DOM_EVENT

对于浏览器事件来说，它只能在浏览器窗口中被触发，并且与触发元素绑定---该元素可以是浏览器window、当前tab加载的document或者一个元素，在浏览器中存在许多可以被触发的事件，你可以查阅该[文档](https://developer.mozilla.org/en-US/docs/Web/Events)来了解浏览器支持哪些事件。

事件在被触发的时候通常会执行一个用户自定义的js函数，该函数通常被称为事件处理器(也叫事件监听器)。

> NOTE：这边需要注意的是事件监听机制并不是js的内容，它只是浏览器提供的JS_API的一部分(其实是在DOM规范中进行了定义，浏览器实现了该规范)，所以DOM事件的触发时机以及默认操作我们都无法修改，它们是浏览器自带的一种默认行为。
>

> NOTE：除了浏览器提供的事件机制，其它的编程语言甚至其它的js环境也有自己的事件处理机制，例如node和jQuery。



## 事件监听器添加方式

在普通的**浏览器环境**中我们有3中方式来为元素添加事件监听器，当元素事件被触发时会执行这些事件监听器。接下来我们简单介绍一下这几种不同的方式。

1. DOM元素属性

   DOM元素拥有默认的事件属性来帮我们设置其事件监听器，我们可以这样使用：

   ```
   var btn = document.querySelector('button');
   
   btn.onclick = function() {
     var rndCol = 'rgb(' + random(255) + ',' + random(255) + ',' + random(255) + ')';
     document.body.style.backgroundColor = rndCol;
   }
   ```

2. HTML属性

   事件属性还可以直接定义在元素标签里面，例如：

   ```
   <button onclick="bgChange()">Press me</button>
   
   function bgChange() {
     var rndCol = 'rgb(' + random(255) + ',' + random(255) + ',' + random(255) + ')';
     document.body.style.backgroundColor = rndCol;
   }
   ```

   甚至可以直接在元素标签中编写js代码：

   ```
   <button onclick="alert('Hello, this is my old-fashioned event handler!');">Press me</button>
   ```

   > NOTE：不建议使用这种方式，因为这会使你的代码变得混乱以及不好管理。

3. addEventListener()和removeEventListener()

   浏览器的DOM_API中的EventTarget对象提供了两个方法addEventListener()和removeEventListener()用来为元素添加和删除事件监听器，例如：

   ```
   var btn = document.querySelector('button');
   
   function bgChange() {
     var rndCol = 'rgb(' + random(255) + ',' + random(255) + ',' + random(255) + ')';
     document.body.style.backgroundColor = rndCol;
   }   
   
   btn.addEventListener('click', bgChange);
   ```

   当我们不需要该事件处理器的时候可以将之移除：

   ```
   btn.removeEventListener('click', bgChange);
   ```



### 对比

因为我们不建议使用HTML属性的方式来为元素添加事件处理器，所以这边我们只是简单对比一下DOM元素属性以及DOM_API提供的事件处理方式：

- DOM元素属性方式对于旧版本的浏览器很友好
- DOM_API方式可以为一个事件添加多个事件处理器而DOM元素属性则不能

**NOTE：大部分旧浏览器与现代浏览器的事件机制实现方式不同，所以兼容时可能有点麻烦，不过也无需担心这个问题，因为许多第三方库(例如jQuery)已经帮我们做了跨浏览器的处理。**



## Event-Object

当元素事件被触发时，浏览器会默认给事件处理器传递一个event object用于提供一些额外的特性以及信息，例如事件触发的源元素。

```
function bgChange(e) {
  var rndCol = 'rgb(' + random(255) + ',' + random(255) + ',' + random(255) + ')';
  e.target.style.backgroundColor = rndCol;
  console.log(e);
}  

btn.addEventListener('click', bgChange);
```

这个event object是一个DOM_API中的[Event](https://developer.mozilla.org/en-US/docs/Web/API/Event)对象，它拥有一系列默认的属性以及方法，然而一些高级事件对象还会提供一些包含运行所需的额外数据的属性。



## 阻止默认行为

有时候我们需要阻止浏览器事件的默认行为，Event对象的preventDefault()方法即可以用来达到该目的。其实该方法本质上是修改了Event对象的defaultPrevented属性，可以猜测浏览器触发事件时会检测该属性的值从而做出不同的判断。

```
form.onsubmit = function(e) {
  if (fname.value === '' || lname.value === '') {
    e.preventDefault();
    para.textContent = 'You need to fill in both names!';
  }
}
```



## 事件冒泡以及事件捕获

当一个拥有父元素的的元素事件被触发后，现代浏览器会执行两个不同的流程---**事件捕获**和**事件冒泡**

- 事件捕获
  1. 浏览器遍历出触发元素到最外层元素(html)的一个元素包含链
  2. 浏览器从最外层元素逐步检查，如果该元素包含一个该事件的事件处理器，那么执行该事件处理器
- 事件冒泡
  1. 浏览器遍历出触发元素到最外层元素(html)的一个元素包含链
  2. 浏览器从触发元素逐步检查，如果该元素包含一个该事件的事件处理器，那么执行该事件处理器

这边有一张图片可以清楚的指明冒泡以及捕获阶段：

![Graphical representation of an event dispatched in a DOM tree using the DOM event flow](https://www.w3.org/TR/DOM-Level-3-Events/images/eventflow.svg)

这两种事件处理机制完全相反，现代浏览器**默认使用事件冒泡机制**，Event对象提供一个stopPropagation()方法来阻止事件冒泡的发生。

```
video.onclick = function(e) {
  e.stopPropagation();
  video.play();
};
```

> NOTE：同时拥有事件捕获机制和事件冒泡机制是一个历史遗留问题，网景浏览器和ie浏览器分别使用这两种机制，所以w3c统一标准之后将这两种机制同时保留了下来，从结果来看冒泡机制更胜一筹。但是如果你有特殊的需求需要使用捕获机制也是可以的，可以通过将EventTarget对象的addEventListener()方法中第三个参数设置成true来开启事件捕获机制。



## 事件委托

事件冒泡还有一个优势那就是可以允许我们使用事件委托机制，例如如果我们有一个列表，想要在每一个元素上添加一个点击事件，点击时弹出元素的class名称，那么正常我们需要为每一个元素都添加事件处理器。

现在我们通过事件冒泡机制来为其父元素添加一个点击事件，那么其所有的子元素在点击时都会触发这个事件了，这种机制我们称之为事件委托。



## Event

Event接口定义了在DOM中发生的事件，一个Event对象包含了该事件的一些具体信息。

一个浏览器事件可以通过用户行为来触发，也可以通过编程来触发(HTMLElement.click()或者EventTarget.dispatchEvent())。

有多种多样的Event接口，它们全部直接或者间接继承了Event接口，Event接口中包含着所有事件公共的属性和方法。完整的Event接口列表请查看[文档](https://developer.mozilla.org/en-US/docs/Web/API/Event#Introduction)。



### 构造函数

```
new Event(typeArg, eventInit);
```

typeArg是一个DOMString，表示事件名称

eventInit是一个数据字典，包含以下可选字段

- bubbles：一个布尔值，表明是否进行事件冒泡，默认为false
- cancelable：一个布尔值，表明事件是否可以被取消，默认为false
- composed：一个布尔值，表明事件是否会触发shadow root以外的事件监听器，默认为false

例子：

```
new Event("look", {"bubbles":true, "cancelable":false});
```



### 属性

- bubbles---是否允许事件在DOM中冒泡
- cancelable---事件是否可以被取消
- composed---是否允许事件在常规DOM和阴影DOM的边界进行冒泡
- currentTarget---事件当前注册对象的引用
- defaultPrevented---是否调用了event.preventDefault()方法
- eventPhase---表明正在处理事件流的哪一个阶段
- target---事件源对象的引用
- timeStamp---事件被创建的时间
- type---事件名称，不区分大小写
- isTrusted---表明事件由浏览器发起(例如用户点击)还是由脚本发起



### 方法

- composedPath()---返回事件将被调用的元素链，如果阴影元素被创建是ShadowRoot.mode关闭将不会包含阴影树。
- preventDefault()---取消事件(浏览器默认行为)。
- stopImmediatePropagation()---如果同一个对象对于同一事件拥有多个事件处理器，默认会根据它们被添加的顺序依次执行，执行该方法后监听链上的剩余处理器将不会被执行。该方法同时会阻止事件冒泡。
- stopPropagation()---阻止事件的冒泡行为。



## EventTarget

EventTarget是一个由对象实现的DOM接口，对象可以接收事件并且可以拥有自己的事件监听器。

许多对象可以继承该接口例如Element、Document、Window，但是除了这些常用的对象外所有其它的对象也可以继承EventTarget接口，例如XMLHttpRequest、AudioNode、AudioContext等。



### 构造函数

```
new EventTarget();
```



### 方法

- addEventListener()---为目标对象添加一个指定的事件监听器。
- removeEventListener()---移除目标对象的事件监听器。
- dispatchEvent()---触发目标对象的一个指定事件。





## 自定义事件

除了浏览器自带的默认事件，我们也可以实现自定义事件，例如：

```
var event = new Event('build');

// Listen for the event.
elem.addEventListener('build', function (e) { /* ... */ }, false);

// Dispatch the event.
elem.dispatchEvent(event);
```

我们还可以通过CustomEvent接口来添加自定义的数据，CustomEvent接口继承了Event接口，它允许用户将自定义的数据添加到Event-Object中。



### 构造函数

```
new CustomEvent(typeArg, customEventInit);
```

typeArg是一个DOMString，表示事件名称

customEventInit是一个数据字典，包含以下可选字段

- detail：一个可以关联到事件对象的任何类型的值，默认为null

- 还有从Event构造函数的EventInit数据字典中继承的所有字段

我们可以如下创建一个包含自定义数据的事件，然后直接通过e.detail来得到这个数据：

```
var event = new CustomEvent('build', { detail: elem.dataset.time });
function eventHandler(e) {
  console.log('The time is: ' + e.detail);
}
```
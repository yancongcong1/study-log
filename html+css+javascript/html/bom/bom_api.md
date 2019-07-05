[TOC]

# Window

Window对象代表浏览器窗口，在包含多个tab的浏览器中，每一个tab都有自己的window对象。浏览器环境中的所有其它全局对象、函数和变量会自动成为window对象的成员。

事实上不存在BOM的官方标准，这部分内容由[HTML协议](https://html.spec.whatwg.org/multipage/window-object.html#window)来定义。

Window接口继承了EventTarget接口并且实现了混入类WindowOrWorkerGlobalScope和WindowEventHandlers中的属性和方法。



## 构造函数

window包含所有[DOM_API](https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model)的构造函数。



## 属性

- console---返回浏览器调试控制台对象，该对象提供多个方法来进行调试工作。

- customElements---返回一个CustomElementRegistry对象，该对象可以用来注册自定义元素并且获取元素的相关信息。

- crypto---返回Crypto对象，用来获取一些加密服务。

- document---返回当前window包含的document对象。

- event---返回当前正在处理的Event对象，如果没有返回undefined。

  > NOTE：应该避免使用这个属性，而是使用事件处理器函数中的event对象，因为这个属性没有被普遍支持，即使受到了支持，也会使代码变得脆弱。

- frameElement---返回当前窗口的嵌入元素(例如`iframe`、`object`、`embed`)，如果没有嵌入其它元素或者嵌入了非同源元素，返回null。

- frames---返回一个包含当前窗口所有的子frame的类数组对象。

- fullScreen---如果当前窗口是全屏模式返回true否则返回false。

- history---返回一个History对象，可以用来操作当前tab的历史记录。

- innerHeight---浏览器可视窗口的高度(包括滚动条，单位为px)。

- innerWidth---浏览器可视窗口的宽度(包括滚动条，单位为px)。

- isSecureContext---表明当前上下文是否一个安全的上下文，具体信息请参考[文档](https://developer.mozilla.org/en-US/docs/Web/Security/Secure_Contexts)。

- length---返回`frame`和`iframe`元素的数量。

- location---返回一个Location对象表示当前文档的url信息，其属性和方法可以用来操作当前的url。

  > NOTE：作用和History接口部分重合，History侧重于浏览器记录而Location侧重于当前URL信息。

- locationbar---有一个visible属性表示浏览器的URL显示区域是否可见。

- localStorage---返回一个localStorage对象用于存储数据，详情请参考[文档](https://developer.mozilla.org/en-US/docs/Web/API/Storage)。

- menubar---有一个visible属性表示浏览器的菜单区域是否可见。

- messageManager---

- name---获取或者设置window的name属性。

- navigator---返回一个Navigator对象用来表示浏览器的信息，该对象实例唯一，可以通过window.navigator来引用。

  > NOTE：其规范在[HTML规范](https://html.spec.whatwg.org/multipage/system-state.html#the-navigator-object)中进行了定义。

- opener---返回通过open()方法打开当前窗口的窗口的引用。例如A通过open()开启了新窗口B，则B.opener返回A。

- outerHeight---浏览器整体高度(单位为px)。

- outerWidth---浏览器整体宽度(单位为px)。

- pageXOffset/scrollX---浏览器窗口水平滚动距离(单位为px)。

- pageYOffset/scrollY---浏览器窗口垂直滚动距离(单位为px)。

- parent---返回当前窗口的父窗口对象，如果没有返回当前窗口对象。`iframe`、`object`和`frame`对象的父窗口就是元素的嵌入窗口对象。

- performance---返回一个Performance对象用来获取当前页面的性能信息，该对象实例唯一，可以通过window.performance来引用。

- personalbar---有一个visible属性表示用户喜欢的页面链接的区域是否可见(书签栏)。

- screen---返回一个Screen对象，表示当前窗口的screen信息

- screenX/screenLeft---

- screenY/screenTop---

- scrollbars---

- self---

- sessionStorage---

- speechSyncThesis---

- status---

- statusbar---

- toolbar---

- top---

- visualViewport---

- window---

- window[0]/window[1]/etc---

------

- WindowOrWorkerGlobalScope.caches---
- WindowOrWorkerGlobalScope.indexedDB---
- WindowOrWorkerGlobalScope.isSecureContext---
- WindowOrWorkerGlobalScope.origin---



## 方法

- alert()---
- blur()---
- cancelAnimationFrame()---
- cancelIdleCallback()---
- clearImmediate()---
- close()---
- confirm()---
- dispatchEvent()---
- find()---
- focus()---
- getComputedStyle()---
- getSelection()---
- matchMedia()---
- maximize()---
- minimize()---
- moveBy()---
- moveTo()---
- open()---
- postMessage()---
- print()---
- prompt()---
- requestAnimationFrame()---
- requestIdleCallback()---
- resizeBy()---
- resizeTo()---
- scroll()---
- scrollBy()---
- scrollTo()---
- setImmediate()---
- stop()---

------

- EventTarget.addEventListener()---
- EventTarget.removeEventListener()---

------

- WindowOrWorkerGlobalScope.atob()---
- WindowOrWorkerGlobalScope.btoa()---
- WindowOrWorkerGlobalScope.clearInterval()---
- WindowOrWorkerGlobalScope.clearTimeout()---
- WindowOrWorkerGlobalScope.createImageBitmap()---
- WindowOrWorkerGlobalScope.fetch()---
- WindowOrWorkerGlobalScope.setInterval()---
- WindowOrWorkerGlobalScope.setTimeout()---



## 事件属性

详细信息请参考[Event handlers](<https://developer.mozilla.org/en-US/docs/Web/API/Window#Event_handlers>)。
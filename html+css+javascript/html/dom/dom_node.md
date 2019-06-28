[TOC]

# DOM_NODE

本节简单介绍一下DOM中和Node(节点)有关的接口



## Node

Node接口表示DOM文档中的一个节点，许多其它的接口直接或间接的继承了该接口来使用其提供的公共属性以及方法。这些接口包括Document、Element、Attr、CharacterData、ProcessingInstruction、DocumentFragment、DocumentType、Notation。

**Node接口继承了EventTarget接口，拥有其所有的属性以及方法。**

### 属性

- baseURI---返回节点的基本URL，大多数情况下是文档的URL，但是可以通过`<base>`标签改变其值。
- isConnected---表明当前节点是否直接或间接关联到上下文对象中(正常文档流中的document对象和阴影文档流中的shadow root对象)。
- nodeName---返回包含节点名称的字符串，具体返回信息请参考[文档](<https://developer.mozilla.org/en-US/docs/Web/API/Node/nodeName#Value>)。
- nodeType---返回一个代表节点类型的无符号short型整数，具体信息请参考[文档](<https://developer.mozilla.org/en-US/docs/Web/API/Node/nodeType#Constants>)。
- nodeValue---**获取或者设置**节点的nodeValue，默认值请参考[文档](https://developer.mozilla.org/en-US/docs/Web/API/Node/nodeValue#Value)。
- textContent---**获取或者设置**节点的文本内容，获取时会取节点以及其子节点的文本内容，具体返回信息请参考[文档](https://developer.mozilla.org/en-US/docs/Web/API/Node/textContent#Description)。设置时会先移除节点的所有子节点然后再插入一个Text节点。
- childNodes---返回一个NodeList，包含了节点的所有子节点，子节点下标从0开始。
- firstChild---如果存在返回节点的第一个子节点，否则返回null。
- lastChild---如果存在返回节点的最后一个子节点，否则返回null。
- parentNode---返回指定节点的父节点，如果不存在返回null。
- parentElement---如果指定节点的父节点存在并且是一个Element对象则返回，否则返回null。
- previousSibling---返回文档树中该节点的上一个节点，如果不存在返回null。
- nextSibling---返回文档树中该节点的下一个节点，如果不存在返回null。

### 方法

- appendChild()---将指定节点作为最后一个子节点插入到当前节点中，如果该节点为当前DOM中的节点，那么会将其从当前位置移到新的位置。

- replaceChild()---会用一个新节点来替换掉当前节点的指定子节点，如果该新的节点为当前DOM中的节点，那么会先将其从原位置删除。

- removeChild()---移除当前节点的指定子节点。

- insertBefore()---将指定节点插入到当前节点的指定子节点之前，如果该节点为当前DOM中的节点，那么会先将其从原位置删除。

- cloneNode()---克隆指定的节点，有一个可选的参数deep，如果设置为true将会完整的克隆该节点的子节点树，否则只是会克隆该节点的所有DOM内容(所有属性和值、内联监听器)但是不会克隆使用脚本添加的内容(事件属性监听器等)

  > NOTE：deep属性有其默认值，但是在新的标准和旧标准对其默认行为不同，所以为了兼容建议传入该值而不是使用缺省值。

- contains()---指定节点是否为该节点的后代节点，返回布尔值。

- hasChildNodes()---如果节点包含任意子节点(文本在DOM中体现为Text节点)，返回true，否则返回false。

- isDefaultNamespace()---传入的URI是否为当前节点的默认URI。

- isEqualNode()---判断两个节点是否拥有相同的特征(属性，子节点数)。

- isSameNode()---判断两个节点是否拥有相同的引用。

- compareDocumentPosition()---比较节点与指定节点(任意文档)的相对位置信息。

- getRootNode()---返回当前节点的根节点，如果在正常文档流中返回HTMLDocuement对象，如果在阴影文档流中返回ShadowRoot。

- normalize()---规范化指定节点的子节点数，规范化后的子节点中不会存在相邻的Text节点。



## NodeIterator

该接口没有父接口。NodeIterator可以用来深度遍历DOM文档中指定节点的所有子节点，该接口没有构造器，直接通过Document.createNodeIterator()方法创建，例如：

```
document.createNodeIterator(root, whatToShow, filter);
```

### 属性

- root---创建iterator时指定的根节点。
- whatToShow---一个无符号的整数用来表示什么类型的node节点会被返回，详细信息请参考[文档](https://developer.mozilla.org/zh-CN/docs/Web/API/NodeIterator/whatToShow)。
- filter---用来选择相关节点的NodeFilter。

### 方法

- previousNode()---返回前一个节点，如果已经是第一个节点则返回null。
- nextNode()---返回后一个节点，如果已经是最后一个则返回null。



## TreeWalker

该接口没有父接口。TreeWalker接口同NodeIterator接口功能一样，也是被用来深度遍历DOM文档中指定节点的所有符合条件的子节点，但其新增了许多操作。它同样没有构造器，可以通过Document.createTreeWalker()方法创建，例如：

```
document.createTreeWalker(root, whatToShow, filter);
```

### 属性

- root---创建iterator时指定的根节点。
- whatToShow---一个无符号的整数用来表示什么类型的node节点会被返回，详细信息请参考[文档](https://developer.mozilla.org/zh-CN/docs/Web/API/NodeIterator/whatToShow)。
- filter---用来选择相关节点的NodeFilter。
- currentNode---TreeWalker指向的当前节点

### 方法

- parentNode()---查找currentNode节点的第一个父节点，并将currentNode设置成该节点。如果没有这个节点或者节点在TreeWalker根节点之外，返回null并且currentNode不做任何改变。
- firstChild()---查找currentNode节点的第一个子节点，并将currentNode设置成该节点。如果没有子节点则返回null并且currentNode不做任何改变。
- lastChild()---查找currentNode节点的最后一个子节点，并将currentNode设置成该节点。如果没有子节点则返回null并且currentNode不做任何改变。
- previousSibling()---查找currentNode节点的前一个节点，并将currentNode设置成该节点。如果没有找到则返回null并且currentNode不做任何改变。
- nextSibling()---查找currentNode节点的后一个节点，并将currentNode设置成该节点。如果没有找到则返回null并且currentNode不做任何改变。
- previousNode()---查找currentNode节点的前一个**可见**节点，并将currentNode设置成该节点。如果没有找到或者节点在TreeWalker根节点之外，返回null并且currentNode不做任何改变。
- nextNode()---查找currentNode节点的后一个**可见**节点，并将currentNode设置成该节点。如果没有找到或者节点在TreeWalker根节点之外，返回null并且currentNode不做任何改变。



## NodeFilter

它不继承任何接口，NodeFilter对象用来对NodeIterator或TreeWalker中的子节点进行过滤。

> NOTE：浏览器没有提供任何对该接口的实现，用户需要在使用Document接口生成NodeIterator或者TreeWalker时自己实现该接口。

### 方法

- acceptNode()---提供一个过滤NodeIterator或TreeWalker子节点的算法，返回一个无符号短整型，具体信息请参考[文档](https://developer.mozilla.org/en-US/docs/Web/API/NodeFilter/acceptNode)。



## NodeList

NodeList是Node对象的集合，通常由其他接口的属性或方法返回，例如Node.childNodes和document.querySelectorAll()。

> NOTE：NodeList不是Array，但是仍然可以通过NodeList.forEach()来遍历它，或者通过Array.from()将之转换成真正的Array。但是一些老版本的浏览器可能不支持这两中写法，我们可以通过Array.prototype.forEach()来回避这个问题，详情请参考[文档](https://developer.mozilla.org/en-US/docs/Web/API/NodeList#Example)。

> NOTE：Node.childNodes返回的是一个**动态**的NodeList，当子节点发生变化时该NodeList同样改变；document.querySelectorAll()返回的是一个**静态**的NodeList，子节点的变化不会影响该NodeList。

### 属性

- length---Node对象的数目

### 方法

- item()---根据下标返回对应的Node对象，如果下标越界返回null。
- entries()---返回一个允许遍历列表中所有键值对的一个迭代器。
- forEach()---为每个节点执行相同的方法。
- keys()---返回一个可以遍历所有键的迭代器。
- values()---返回一个可以遍历所有Node对象的迭代器。
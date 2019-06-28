[TOC]

# DOM_API

DOM(文档对象模型)是HTML和XML文档的编程接口，定义了使用编程语言访问和控制文档的规范。它提供了对文档的结构化表述，并定义了一种方式可以通过程序对该结构进行访问和修改。

DOM将文档解析为一个由节点和对象组成的结构集合。[W3C DOM](http://www.w3.org/DOM/) 和[WHATWG DOM](https://dom.spec.whatwg.org/)定义了浏览器中对DOM的基本实现。



## Javascript和DOM

DOM是一种规范，定义了使用编程语言访问和控制HTML和XML文档的方法。

Javascript是一种编程语言，可以实现DOM规范(这也表明了DOM可以使用其它的语言来实现，例如Python)。

所以我们可以得到下面的近似等式：

```
HTML_API = DOM + JS
```



## DOM学习

HTML是一种特殊的XML文档，DOM接口主要是用以结构化XML文档，所以针对HTML的操作其实就是细化后的DOM_API，我们简单称之为HTML_API。HTML_API以DOM_API为基础拓展了自己的元素接口，其根接口为HTMLDocument接口，而HTMLDocument接口又继承了DOM_API的Document接口，所以想要学习HTML_API，我们需要先熟悉DOM_API。

DOM_API中包含许多接口定义，为了方便学习，我们可以从最顶级接口入手，EventTarget和Node接口在DOM_API中为众多其余接口直接或间接继承，所以我们以这两个接口为突破口来学习DOM_API。



## 分类

根据接口的类型以及功能，我们简单将DOM core提供的接口分为下面几类：

### event

详细信息请参考dom_event文档。

- Event
- CustomEvent
- EventTarget



### node

详细信息请参考dom_node文档。

- Node
- NodeFilter
- NodeIterator
- NodeList
- TreeWalker



### document

- Document
- DocumentFragment
- DocumentType



### character

- CharacterData
- Text
- Comment
- ProcessingInstruction



### element

- Element
- HTMLCollection
- Attr



### util

- DOMException
- DOMImplementation
- DOMString
- DOMTimeStamp
- DOMSettableTokenList
- DOMStringList
- DOMTokenList



### system

- Window
- URL
- Worker
- Tange
- MutationObserver
- MutationRecord
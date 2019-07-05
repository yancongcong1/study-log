[TOC]

# DOM_CHARACTER

本节简单介绍一下DOM中一些包含字符的Node(节点)相关的接口



## CharacterData

改接口继承自Node接口，表示一些只包含字符的Node节点。这是一个抽象接口，意味着该接口没有任何实现，但是一些其它的接口实现了CharacterData接口，例如Text、Comment和ProcessingInstruction。

### 属性

- data---返回一个包含对象文本数据的字符串。
- length---返回data的字符数目。

### 方法

- appendData()---在data最后插入指定的字符串。
- deleteData()---删除指定位置和长度的字符床。
- insertData()---在指定位置插入字符串。
- replaceData()---将指定位置和长度的字符串替换成另外的字符串。
- substringData()---截取指定位置和长度的字符串并返回该字符串。



## Text

Text接口表示Element对象或Attr对象的文本内容。如果一个元素中没有其它任何标记，那么它就会只包含一个Text文本节点；如果包含其它任何元素，那么会将其解析为包含一个文本节点的元素节点。

> NOTE：浏览器不会解析出任何相邻的文本节点，所有相邻的文本节点都只会在编程操作DOM的过程中产生。

### 属性

- wholeText---返回该文本节点逻辑上相邻的所有文本节点的所有文本，文本按照文档顺序连接。也可以直接设置该属性来改变相邻文本节点的内容。

  > NOTE：这边需要区分一下wholeText，textContent以及innerText的区别。

### 方法

- splitText()---从指定位置将文本节点拆分成两个兄弟文本节点。



## Comment

表示一个注释，通常不可见，但是在源码中可读。HTML中的注释通常在`<!--`和`-->`标记之间。没有属性和方法，可以继承部分来自CharacterData和Node接口的属性和方法。



## ProcessingInstruction

这个不常用的接口表示 XML 文档中的一个处理指令（或 PI）。使用 HTML 文档的程序设计者不会遇到 ProcessingInstruction 节点。
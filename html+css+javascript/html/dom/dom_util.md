[TOC]

# DOM_UTIL

本节简单介绍一下DOM的一些工具类接口，包括exception，类型定义。



## DOMString

是一个UTF-16编码的字符串，会被直接映射为js的string。



## DOMTimeStamp

表示一个相对或者绝对的毫秒数。



## DOMStringList

该接口表示一个DOMString的集合，没有构造函数，通常由其它接口的方法返回。

### 属性

- length---返回集合的长度。

### 方法

- item()--根据下标返回一个字符串。
- contains()---如果集合中包含字符串则返回true，否则返回false。



## DOMTokenList

该接口表示一个由空格分隔的token字符串，返回一个从下标0开始的集合。它没有构造函数，通常由其他接口的属性或方法返回，例如Element.classList、HTMLLinkElement.relList、HTMLAnchorElement.relList、HTMLAreaElement.relList等。

### 属性

- length---返回数组中的对象数。
- value---将集合作为一个字符串返回。

### 方法

- item()---根据下标返回一个字符串。
- contains()---如果集合包含给出的token字符串返回true，否则返回false。
- add()---将指定token添加到集合末尾。
- remove()---从集合中移除指定的token字符串，如果token不存在，不执行任何操作。
- replace()---替换指定的token，如果替换成功返回true，否则返回false。
- toggle()---如果集合中存在指定token则删除它并且返回false，如果不存在那么在集合中添加它并且返回true。
- entries()---返回一个允许遍历列表中所有键值对的一个迭代器。
- forEach()---为每个节点执行相同的方法。
- keys()---返回一个可以遍历所有键的迭代器。
- values()---返回一个可以遍历所有Node对象的迭代器。



## DOMException

表示当获取API属性或调用其方法时发生的异常事件。常见的错误[列表](https://developer.mozilla.org/en-US/docs/Web/API/DOMException#Error_names)。

### 构造函数

- DOMException()

### 属性

- message---描述异常信息。
- name---异常的名称。



## DOMImplementation

该接口是一个占位符，可以用来存放不属于任何文档对象的Document对象，可以执行与当前文档对象实例无关的任何操作，通过Document.implementation来获取。

### 方法

- createDocument()---创建一个XML文档对象。
- createDocumentType()---创建一个DocumentType对象。
- createHTMLDocument()---创建一个HTML文档对象。

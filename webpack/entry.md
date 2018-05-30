[TOC]

# webpack核心概念之entry

想要使用好webpack来满足项目的各种各样的功能就不可避免的需要仔细研究webpack的各种功能，是不是有时候会因为不知道如何学习而感到
烦躁和困惑，不要着急，如果不是需要特别复杂的功能，你只需要理解webpack的四个核心概念就可以开始你的项目了。

## 核心概念
* Entry
* Output
* Loaders
* Plugins  

本节简单介绍一下webpack核心概念之Entry

---

## 配置选项
> context——从官网的字面意思理解，这个配置就是来确定配置文件中所有path的默认路径  
> 本例中想要设置lesson3目录为默认路径，只需要添加：  
```
context: path.resolve(__dirname, '../')  
```
> 之后直接使用相对路径就可以了

> Entry——[官网介绍](https://webpack.js.org/configuration/entry-context/)
>
> 从官网的介绍我们可以看出entry的配置有四种方式：  
>
> - string,字符串——表示入口文件的路径，这时候output的输出文件名字随意
>
> - []，数组——多个入口文件的路径，这时候如果output输出文件名字没有区分的话，块的名称为main
>
> - {},对象——每个键值都是块的名称
>
> - function,函数——返回以上三个类型

本节中所有的方式都有例子，可以自行尝试。

## 实例演示
- 1、上一节我们使用了默认的配置文件的方式，这一次我们使用--config命令来使用配置文件。
> 首先，我们新建一个conf文件夹，将配置文件改名为webpack.conf.js并放到conf下面，完成后目录如下：  
>
> **本节中为了查看多文件入口的效果，新加了print.js，关于output中的配置请查阅下一节。**  
![image](https://github.com/yancongcong1/study-log/blob/master/webpack/static/images/lesson3-1.png)

- 2、修改我们的package.json文件，因为我们读取配置文件的方式已经改变了
```
{
  "name": "lesson3",
  "version": "1.0.0",
  "description": "webpack lesson3",
  "main": "index.js",
  "scripts": {
    "build": "webpack --config ./conf/webpack.conf.js"
  },
  "author": "ycc",
  "license": "ISC",
  "devDependencies": {
    "lodash": "^4.17.4",
    "webpack": "^3.10.0"
  }
}
```

---
运行npm run build  
输出：'Hello webpack'或者'Hello webpack' 'Hello webpack3'(看有几个入口)。打包成功。
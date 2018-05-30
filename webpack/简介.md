[TOC]

# Webpack Getting Started

## 前言
之前学习过一些webpack相关的东西，但是感觉学习的有点乱，所以决定系统的重新学习一边这个前端非常流行的打包工具，同时记录一下学习的路线以及学习过程中遇到的问题，方便自己复习。

## webpack简介
既然要学习一个新的技能，总是要知道为什么学习它，它又能带给我们什么便利。  

> &emsp;有时候我们可能需要以下功能：  
> - 压缩我们的js文件合并成一个文件
> - 在我们的前端代码项目中使用NPM包管理工具
> - 使用ES6/ES7规范书写代码（借助babel）
> - 缩小/优化代码
> - 编译LESS/SCSS成CSS
> - 使用HMR（Hot Module Replacement/实时的模块监听改变）
> - 把任何类型的文件放进我们的javascript中
> - 我们需要了解、掌握更多先进和潮流的技术

> &emsp;为什么需要这些功能呢？
> - 压缩js文件——让我们可以编写模块化的javascript代码，却不需要为把它放进单独的js文件来被单个的<script>标签所引用。（如果我们需要配置多个js文件时）
> - 使用NPM包管理在我们的前端代码项目——NPM是互联网上最大的开源代码生态系统。我们可以试试把写好的代码保存上传到NPM看看，把想要的库放进你的前端项目。
> - ES6/ES7——为javascript增加了很多新特性，使它更具有潜力又容易编写。看看这里的介绍.
> - 缩小/优化代码——减少我们发布文件的大小，就有助于让我们页面更快地加载的。
> - 编译LESS/SCSS成CSS——更好的方式去编写CSS代码。如果你不熟悉可以看这里的介绍.
> - 使用HMR-生产力的提高，每次保存代码的时候，只要他是被引入到该页面那么不需要完整的页面刷新。这在我们编辑代码时候是非常方便的。
> - 把任何类型的文件包含进我们的javascript中——减少其他构建工具的需要，并允许我们用代码的方式修改和使用这些文件。

> 那么webpack到底是什么呢？  
> Webpack 是一个前端资源加载/打包工具。它将根据模块的依赖关系进行静态分析，然后将这些模块按照指定的规则生成对应的静态资源。  
> ![image](https://github.com/yancongcong1/study-log/blob/master/webpack/static/images/lesson1-1.png)  
> 从图中我们可以看出，Webpack 可以将多种静态资源 js、css、less 转换成一个静态文件，减少了页面的请求。

## 拓展
既然webpack是一个模块打包工具，那么模块又是什么呢？关于模块的概念这边有两篇文章写的很好([第一篇](https://medium.freecodecamp.com/javascript-modules-a-beginner-s-guide-783f7d7a5fcc#.jw1txw6uh)、[第二篇](https://medium.freecodecamp.org/javascript-modules-part-2-module-bundling-5020383cf306))。

## 实例演示
终于要开始了，心里还有点小激动呢！  
按照官网的步骤，我们就开始吧：  

- 1、创建一个目录，初始化package.json，安装本地webpack  
```
> npm init -y  
> npm install --save-dev webpack  
```

- 2、项目结构  
 project
> ![image](https://github.com/yancongcong1/study-log/blob/master/webpack/static/images/lesson1-2.png)

 src/index.js
```
import _ from 'lodash'

function component() {
    var element = document.createElement('div');

    // Lodash, currently included via a script, is required for this line to work
    element.innerHTML = _.join(['Hello', 'webpack'], ' ');

    return element;
}

document.body.appendChild(component()); 
```

 dist/index.html
```
<html>
<head>
    <title>Getting Started</title>
</head>
<body>
<script src="./bundle.js"></script>
</body>
</html>
```

- 3、运行以下命令，添加需要的依赖：
```
npm install lodash --save  
```
- 4、package.json添加script如下：  
> ![image](https://github.com/yancongcong1/study-log/blob/master/webpack/static/images/lesson1-3.png)

---
运行npm run build  
打开index.html，出现'Hello webpack'。表明打包成功。  
初步的webpack打包已经成功，但是这么简单就完成明显不是webpack的全部功能，这时候更加强大的功能需要配置文件来配合了。下一节我们就简单介绍一下webpack的配置文件。
[TOC]

# 配置文件

## 前言
> 大多数项目需要的不仅仅是简单的打包功能，可能需要更为复杂的配置，这时候就需要使用webpack的配置文件了。
> 虽然这些配置也可以直接在命令行中进行添加，但是使用配置文件要直观简单并且高效得多。所以我们使用配置文件来
> 取代前面的命令行参数的写法。  
> 
> 如果当前文件夹下面存在webpack.config.js文件，webpack会默认使用这个文件作为它的配置文件，否则需要使用
> **--config**来指定配置文件，至于为什么会出现这种形式的配置文件，看看官网如何回答的吧：

> This will be useful for more complex configurations that need to be split into multiple files.

> 简单来说就是这种方式更加灵活，比如多环境下的配置文件的使用。

本节使用默认配置文件的方式对webpack进行配置，之后的章节会使用其他的方式对webpack进行配置。

## 实例演示
- 1、创建webpack.config.js文件，添加如下内容：
```
const path = require('path');

module.exports = {
    entry: './src/index.js',
    output: {
        filename: 'bundle.js',
        path: path.resolve(__dirname, 'dist')
        // __dirname是node中的全局变量，表示当前模块的为文件夹的绝对路径，webpack.conf.js的__dirname为D://....../lesson2
    }
};
```

> 简单介绍：  
> entry表示入口文件  
> output表示打包文件的一些信息

- 2、修改package.json文件如下：
```
{
  "name": "lesson2",
  "version": "1.0.0",
  "description": "webpack lesson2",
  "main": "index.js",
  "scripts": {
    "build": "webpack"
  },
  "author": "ycc",
  "license": "ISC",
  "devDependencies": {
    "lodash": "^4.17.4",
    "webpack": "^3.10.0"
  }
}
```
这边可以看到我们只是删除掉了webpack后面的入口和输出文件信息

---
运行npm run build  
打开index.html，出现'Hello webpack'。表明打包成功。
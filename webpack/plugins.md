[TOC]

# 核心概念之Plugins



## 简介

> ​	Plugins是webpack的骨架，webpack本身就是建立在类似你使用的配置文件中同样的插件体系之上的。这些插件体统了Loader不能提供的那些服务。
>
> ​	插件用于以多种方式定制webpack的构建过程。webpack在[webpack.[plugin.name]](https://github.com/webpack/webpack/tree/master/lib)下面提供了各种各样的内置插件。文档列表在[这里](https://webpack.js.org/plugins/)可以找到。同时在社区中还有更多其他的插件和文档。
>
> ​	可以说Loader和Plugins是相辅相成的。



## 深入剖析

> ​	一个webpack组件实质上就是一个拥有apply属性的一个js对象。这个apply属性被webpack编译器调用，允许组件访问整个webpack的编译期。

​	下面是一个插件的部分代码：

​	**ConsoleLogOnBuildWebpackPlugin.js**

```
function ConsoleLogOnBuildWebpackPlugin() {

};

ConsoleLogOnBuildWebpackPlugin.prototype.apply = function(compiler) {
  compiler.plugin('run', function(compiler, callback) {
    console.log("The webpack build process is starting!!!");

    callback();
  });
};
```



## 用法

> ​	由于plugins可以携带参数，所以你**只能**在配置文件中的plugins选项中**新建一个插件实例**来使用它。根据你使用webpack方式的不同，插件的使用方式也是多种多样的。

​	下面以配置文件的方式为例：

- 1、首先安装需要的插件

```
npm install --save-dev html-webpack-plugin
```

- 2、在配置文件中进行配置

```
var HtmlWebpackPlugin = require('html-webpack-plugin');
var path = require('path');

var webpackConfig = {
  entry: 'index.js',
  output: {
    path: path.resolve(__dirname, './dist'),
    filename: 'index_bundle.js'
  },
  plugins: [
  	new HtmlWebpackPlugin()
  ]
};
```



## 配置选项

> plugins——arrays，数组，表示webpack插件的列表



## 实例演示

- 1、本章简单实用两个插件来进行演示

  HtmlWebpackPlugin主要用来自己生成index.html文件并且自动引入输出包

  UglifyJsPlugin使我们的输出包变得更小，也就是压缩

- 2、修改项目结构，project结构如下：

  ![image](https://github.com/yancongcong1/blog/blob/master/webpack/static/images/lesson6-1.png)


- 3、安装插件

```
npm install --save-dev html-webpack-plugin
npm i -D uglifyjs-webpack-plugin
```

- 4、修改webpack.conf.js

```
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const UglifyJsPlugin = require('uglifyjs-webpack-plugin')

module.exports = {
    context: path.resolve(__dirname, '../'),
    entry: ['./src/index.css', './src/print.js'],
    // entry: () => ['./src/index.js', './src/print.js'],
    output: {
        filename: 'main.bundle.js',
        path: path.resolve('dist'),
        // library: 'Test',
        // libraryExport: "default",
        libraryTarget: 'umd'
        // __dirname是node中的全局变量，表示当前模块的为文件夹的绝对路径，webpack.conf.js的__dirname为D://....../lesson2
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: [
                    { loader: 'style-loader' },
                    {
                        loader: 'css-loader',
                        options: {
                            modules: true
                        }
                    }
                ]
            }
        ]
    },
    plugins: [
        new HtmlWebpackPlugin(),
        new UglifyJsPlugin()
    ]
};
```

------

运行npm run build，在dist目录下发现index.html，以及被压缩后的输出包，查看html内容，发现已经自动引入了输出包。

运行html文件，观察效果。
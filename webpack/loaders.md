[TOC]

# 核心概念之Loaders



## 介绍

> ​	webpack支持通过加载器来编写各种语言和预处理器的模块。加载器向webpack描述如何处理非javascript模块，并将这些依赖项包含到你的输出包中。webpack社区已经为各种流行的语言和语言处理器构建了加载器，包括:
>
> - CoffeeScript
> - TypeScript
> - ESNext (Babel)
> - Sass
> - Less
> - Stylus  
>
>   官网提供的各种[Loaders](https://webpack.js.org/loaders/)



## 特性

[官网](https://webpack.js.org/concepts/loaders/)上面介绍的已经十分详细了。



## 配置选项

> module——这个选项决定了项目中不同类型模块将如何被处理
>
> [详细信息](https://webpack.js.org/configuration/module/)

> module.rules——当模块被创建后匹配请求的一些rule组成的数组。这些规则可以修改模块的创建方式。可以将在模块中应用加载器，或者修改模块的解析方式。
>
> [详细信息](https://webpack.js.org/configuration/module/#module-rules)

> Rule.resource——匹配资源的条件，在这个选项中允许你提供一条规则给加载器匹配需要加载的资源时使用。使用`Rule.test`, `Rule.exclude`, 和`Rule.include`拥有同样的功能。
>
> [详细信息](https://webpack.js.org/configuration/module/#rule-resource)

> Rule.test——Rule.resource.test的快捷方式。这两个选项只能设置一个
>
> [详细信息](https://webpack.js.org/configuration/module/#rule-test)

> Rule.use——一个应用于模块的UseEntries(用法)列表，每一条都需要指定要使用的loader。查看UseEntry获取详细信息

> UseEntry——必须用一个名为loader的字符串选项
>
> [详细信息](https://webpack.js.org/configuration/module/#useentry)



## 使用Loaders

在应用中有三种方式使用加载器：

- [Configuration](https://webpack.js.org/concepts/loaders/#configuration) (recommended): Specify them in your **webpack.config.js** file.
- [Inline](https://webpack.js.org/concepts/loaders/#inline): Specify them explicitly in each `import` statement.
- [CLI](https://webpack.js.org/concepts/loaders/#cli): Specify them within a shell command.

### Configuration

> 使用配置文件，配置文件中的module.rules选项允许你在webpack中指定多个加载器，这是使用加载器的一种比较简洁的方式，有助于维护干净的代码。它还提供了对每个各自的加载器的完整概述。如下：

```
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
  }
```

### Inline

> 直接在导入语句中使用加载器。可以在import语句中或者任何等价的“导入”方法中指定加载器。使用!区分资源和加载器。表达式中的每一个资源的路径都是相对于当前目录的。

```
import Styles from 'style-loader!css-loader?modules!./styles.css';
```

**通过将！设置为整个规则的前缀可以覆盖配置中的任何加载器。**

这时候加载器的一些配置选项可以通过查询参数或者json字符串来设置，如下：

```
?key=value&foo=bar
?{"key":"value","foo":"bar"}
```

尽可能的使用配置文件的方式，因为这样做可以减少源码中的样板文件，并且允许你更快的调试或者定位加载器的使用位置。

### CLI

> 在CLI中使用加载器，例如：

```
webpack --module-bind jade-loader --module-bind 'css=style-loader!css-loader'
```

这将为.jade文件使用jade-loader并且为.css文件使用style-loader和css-loader。



## 实例演示

- 1、新增index.css文件，设置网页的背景颜色

```
body {
    background-color: green;
}
```

- 2、修改配置文件

```
const path = require('path');

module.exports = {
    context: path.resolve(__dirname, '../'),
    entry: ['./src/index.js', './src/index.css', './src/print.js'],
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
    }
};
```

注意本例中entry使用方式为数组，这时候如果使用output.library属性，只有最后的模块会被输出，所以一定要注意次序，其他方式可以自行尝试。

------

运行npm run build，输出“Hello, Webpack”和“Hello, Webpack4”并且背景为绿色，打包成功。
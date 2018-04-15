[TOC]

# webpack之externals



## 简介

externals选项提供了一种可以将依赖从输出包中排除的方法。与此同时，创建的输出包依赖于**使用者**当前环境的依赖(也就是使用输出包的环境中需要安装输出包未包含的依赖)。这个特性对于**库开发人员**来说比较有用，但是市面上已经有了好多用于这方面的应用了。

> NOTE：这里的**使用者** 指任何终端用户应用程序包括使用webpack打包的库。



## 配置选项

> externals——组织打包时某些由import导入的包的捆绑打包，并且在运行时检索这些外部依赖项。

阅读下面这个简单的官方例子：

index.html

```
<script
  src="https://code.jquery.com/jquery-3.1.0.js"
  integrity="sha256-slogkvB1K3VOkzAI8QITxV3VzpOnkeNVsKvtkYLMjfk="
  crossorigin="anonymous">
</script>
```

webpack.config.js

```
externals: {
  jquery: 'jQuery'
}
```

在其他模块中的使用：

```
import $ from 'jquery';

$('.my-element').animate(...);
```

可以看出使用和以前没有什么区别，但是打包的时候并不会吧jQuery捆绑进去，而是从cdn引用。

externals有5种配置形式：

> - string——上面的例子表明，jquery应该被排除在输出包之内。为了替换这个模块，我们使用值jQuery来检索全局jQuery变量。也就是说如果我们提供了一个字符串，那么它就会被视为全局的root。
>
>



>
> - array——数组形式，subtract: ['./math', 'subtract']提供了一种父子形式的的引用，如果./math是父级模块并且你的输出包只需要子集subtract时用到这种形式。
>
> ```
> externals: {
>   subtract: ['./math', 'subtract']
> }
> ```
>



> - object——这种形式用来描述外部可用库的所有可能的导入方法。对于下面的lodash字符串来说，它是commonjs和amd中的lodash，以及全局变量中的_。对于object和array混用的情况，下面的第三种情况中subtract是math中的一个属性(window['math']['subtract'])
>
> ```
> externals : {
>   lodash : {
>     commonjs: "lodash",
>     amd: "lodash",
>     root: "_" // indicates global variable
>   }
> }
> ```
>
> or
>
> ```
> externals : {
>   react: 'react'
> }
> ```
>
> or
>
> ```
> externals : {
>   subtract : {
>     root: ["math", "subtract"]
>   }
> }
> ```
>



> - function——我们还可以定义自己的函数来控制webpack打包的包含行为。例如，[webpack-node-externals](https://www.npmjs.com/package/webpack-node-externals) 库，可以排除项目中的node_module目录中的所有的模块，并且提供一些其它的选项。
>
> 下面的例子中“commonjs”+request定义了需要模块化的模块类型。
>
> ```
> externals: [
>   function(context, request, callback) {
>     if (/^yourregex$/.test(request)){
>       return callback(null, 'commonjs ' + request);
>     }
>     callback();
>   }
> ],
> ```



> - regex——可以使用正则表达式来排除依赖
>
> ```
> externals: /^(jquery|\$)$/i
> ```
>
> 在上面的例子中，任何命名为jQuery的依赖或者$都会被排除在输出包外。



## 实例演示

- 1、修改index.js

```
// import _ from 'lodash'
import _ from '_lodash'

function component() {
    var element = document.createElement('div');

    // Lodash, currently included via a script, is required for this line to work
    element.innerHTML = _.join(['Hello', 'webpack4'], ' ');

    return element;
}
document.body.appendChild(component());
```

- 2、修改webpack.conf.js，添加一下内容

```
externals: {
	_lodash: 'lodash'
},
```

------

运行npm run build，比较生成的包与之前的输出包的大小，发现现在的输出包明显变小，证明配置成功。
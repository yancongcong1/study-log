[TOC]

# 核心概念之output



## 介绍
webpack的output告诉webpack如何将已经编译好的文件写进磁盘。注意，虽然entry的入口点可以配置多个，但是output只能配置一处



## 配置选项
简单介绍output中的一些选项：

> - filename——这个选项决定了输出包的名称，这个包被写入path指定的目录  
>
>   [详细信息](https://webpack.js.org/configuration/output/#output-filename)
```
  单文件的输出目录如下：
  filename: 'bundle.js'
  但是，如果使用多入口、代码分割或者多个插件创建多个输出包时，你可以通过下面的替换方法来给每一个包一个唯一的名称：
  1、使用入口名称：
  filename: '[name].bundle.js'
  2、使用内部块的id：
  filename: '[id].bundle.js'
  3、使用为每一个构建生成的hash值：
  filename: '[hash].bundle.js'
  4、根据每一个块的内容的散列：
  filename: '[chunkhash].bundle.js'
  
```
![image](https://github.com/yancongcong1/blog/blob/master/webpack/static/images/lesson4-1.png)  



> - path——输出目录，是一个绝对路径

> - pathinfo——默认为false，可以让webpack在输出包中添加一些关于包含的模块的注释信息。不建议在生产中使用，但是在读取生成的代码时比较有用  

> - publicPath——该选项指定在浏览器中引用的输出目录的公共URL，即网页中引用资源的相对路径  
>
>   [详细信息](https://webpack.js.org/configuration/output/#output-filename)  
>
>   [这篇文章介绍的不错](https://www.cnblogs.com/gaomingchao/p/6911762.html)  
>
>   [详解webpack中的路径](https://www.cnblogs.com/libin-1/p/6592114.html)  



> - library——library如何使用取决于libraryTarget选项，这个选项一般在需要打包称库的时候使用，也就是给输出的模块起一个名称  
>
>   [详细信息](https://webpack.js.org/configuration/output/#output-library)  
>
> **Note that if an array is provided as an entry point, only the last module in the array will be exposed.
> If an object is provided, it can exposed using an array syntax (see this **[example](https://github.com/webpack/webpack/tree/master/examples/multi-part-library)** for details).**

**注意：如果入口文件是一个数组(Arrays)，那么仅仅只有最后的一个模块将会被输出。如果入口文件提供了一个对象，可以使用数组语法来输出这些模块(例子中简单测试了一下)**
```
output: {
  library: "MyLibrary"
}
如果输出文件被HTML页面中的script标签引用时，变量MyLibrary将被绑定到文件的返回值
```

>- libraryExport——配置哪些模块将会通过libraryTarget公开，默认是命名空间或者你的输入文件的默认模块
>
>  [详细信息](https://webpack.js.org/configuration/output/#output-libraryexport)



> - libraryTarget——配置library如何被公开，该选项与output.library一起工作。  也就是模块的规范，CMD、AMD、UMD，window全局变量等。
>
>    [详细信息](https://webpack.js.org/configuration/output/#output-libraryTarget)  
>
>     ##### 拓展内容
>
>     1、[amd、commonJs、umd](http://web.jobbole.com/82238/)
>
>     2、[webpack externals详解](http://www.tangshuang.net/3343.html)
>
>     3、[webpack打包umd的问题](https://segmentfault.com/q/1010000005028964)

## Output简单使用
在webpack配置中，output至少需要配置两项内容：

- **filename**
- **path**  

例如：  

webpack.conf.js
```
const config = {
  output: {
    filename: 'bundle.js',
    path: '/home/proj/public/assets'
  }
};

module.exports = config;
```
这个配置将会在/home/proj/public/assets输出单个文件bundle.js  

## 实例演示
- 1、本节的例子和上节比较几乎没有改动，只是新增了library和libraryTarget两个选项，这两个选项的注释查看本节的Output部分。  
> 修改print.js如下：
```
import _ from 'lodash'

class Test {

    constructor (config) {
        if (!Test.instance) {
            Test.instance = this
        }
        return Test.instance
    }

    hello () {
        function component() {
            var element = document.createElement('div');

            // Lodash, currently included via a script, is required for this line to work
            element.innerHTML = _.join(['Hello', 'webpack4'], ' ');

            return element;
        }
        document.body.appendChild(component());
    }

}

export default Test
```

---
运行npm run build
输出：'Hello webpack'或者'Hello webpack' 'Hello webpack4'(具体查看配置，查看console控制台是否报错，报错打包失败)。打包成功。
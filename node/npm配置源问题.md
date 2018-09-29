# npm源配置

根据网上的方法，将npm的源配置为淘宝源，各种输出信息都没错，但是在install chromedriver这个模块的时候还是报错，按理说配置好源后所有的模块都会从淘宝源下载，但是在错误信息里看到了如下的内容：

![](https://github.com/yancongcong1/study-log/blob/master/node/static/images/1-1.png)

实在是顽固不堪！

根据错误信息在网上发现了如下的解决办法：

```
npm install chromedriver --chromedriver_cdnurl=http://cdn.npm.taobao.org/dist/chromedriver 
```

可是自己根据package.json文件进行全局安装，不会一个一个安装，怎么办？

考虑到更换npm源的方法，尝试在.npmrc中最后一行添加如下内容：

```
chromedriver_cdnurl=http://cdn.npm.taobao.org/dist/chromedriver 
```

发现问题完美解决！
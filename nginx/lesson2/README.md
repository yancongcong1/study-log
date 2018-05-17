[TOC]



# nginx下载安装



## 下载

进入nginx的官方源码下载页面，可以看到如下的信息：

![官方下载页面](https://github.com/yancongcong1/study-log/blob/master/nginx/static/images/lesson2-1.png)

- > Mainline version--nginx主分支的版本


- > Stable version--当前最新的稳定版本


- > Legacy versions--历史版本

在所有的版本中又分为两个版本：linux版和windows版。这两个版本很好区分，windows版带有windows后缀，可以根据需要进行下载。



## 安装

本章只介绍nginx在linux上的安装，linux安装nginx也分好多中不同的方法，这边介绍的是通过源码编译的方式来安装nginx：

linux安装nginx也分好多中不同的方法，这边介绍的是通过源码编译的方式来安装nginx：

1. 下载linux版的源包，下载文件的格式为.tar.gz格式；

2. 将下载好的nginx压缩包上传至linux服务器中(上传方式随意)；

3. 解压上传好的nginx压缩包：

   ```
   tar -zxvf XXXtar.gz -C XXX
   ```

4. 编译源码：

   ```
   ./configure --prefix=/root/nginx/ --with-http_ssl_module
   ```

   运行`make && make install`

5. 进入`root/nginx/sbin`，运行`./nginx -g start`。在浏览器中输入linux服务器地址查看是否得到正确的nginx首页信息(nginx默认端口为80)。如果得到下图内容，则安装正确：

![nginx首页](https://github.com/yancongcong1/study-log/blob/master/nginx/static/images/lesson2-2.png)

> 编译源码配置详解：
>
> - --prefix=path
>
>   prefix定义了nginx的安装目录。可以使用相对路径或者绝对路径，相对路径相对于configure路径默认为/usr/local/nginx目录。
>
>
> - --sbin-path=path
>
>   设置启动nginx的可执行文件的名字。默认为nginx，目录为**prefix**/sbin/nginx。prefix为上面的配置。
>
> - --modules-path=path
>
>   设置动态模块的安装目录，默认为**prefix**/modules
>
> - --conf-path=path
>
>   设置nginx.conf配置文件的路径以及名称。在启动nginx时可以通过命令行添加-c file来加载指定的配置文件，默认为**prefix**/conf/nginx.conf。
>
> - --error-log-path=path
>
>   设置基础的错误、警告、诊断信息的日志文件的路径以及名字，该文件可以在安装nginx后通过nginx.conf配置文件来改变，默认为**prefix**/logs/error.log。
>
> - --pid-path=path
>
>   设置nginx主进程进程ID存放文件nginx.pid的路径以及名称。可以在安装nginx后通过nginx.conf配置文件进行修改，默认为**prefix**/logs/nginx.pid。
>
> - --user=name
>
>   设置工作流用户的名称，该用户名会在外部访问nginx资源时使用，可以在nginx.conf配置文件中进行修改，默认为nobody。
>
> - --group=name
>
>   设置工作流用户组的名称，会在外部访问nginx资源时使用，可以在nginx.conf配置文件中进行修改，默认为nobody。
>
> - --with-select_module
>   --without-select_module
>
>   启用或者禁用构建动态模块。如果平台不支持该模块，将会自动禁用构建。



## 遇到的问题

1. 在安装的过程中可能会遇到一些依赖找不到的情况而出错，这时候只需要安装相应的依赖即可，nginx需要的一些依赖包：

   - zlib
   - openssl
   - pcre

2. 出现403 forbidden的两种原因：

   - 缺少索引文件（index.html/inde.php）

   比如下面的配置：

   ```
   server {
       listen 80;
       server_name z.com;

       location / {
           root /home/www/zgw/;
           index index.html;
       }
   }
   ```

   当在目录/home/www/zgw下没有index.html时就会出现403 forbidden错误！解决办法添加索引文件。

   - 权限问题

   **<u>为了保证文件能正确执行，nginx既需要文件的读权限,又需要文件所有父目录的可执行权限。</u>**

   比如如下配置：

   ```
   server {
       listen 80;
       server_name z.com;

       location / {
           root /home/www/zgw/;
           index index.html;
       }
   }
   ```

   nginx的启动默认用户是nginx，所以对web目录没有一个读的权限，此时会报403  forbidden。此时有两种解决办法：

   1、第一种直接使用root用户启动nginx，让其拥有最大权限，修改配置如下：

   ```
   user  root root;
   ```

   2、第二种修改文件权限：

   设置所有父目录为755权限，设置文件为644权限。
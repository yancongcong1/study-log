[TOC]

# Dockerfile详解

​	[官方文档](https://docs.docker.com/engine/reference/builder/)，docker可以通过`docker build`命令读取Dockerfile文件来创建docker镜像，这种方法中Dockerfile文件至关重要，下面我们就来详细了解一下Dockerfile文件。



### 格式

   ```
# Comment
INSTRUCTION arguments
   ```

​	指令不区分大小写，但是习惯将指令写为大写以便跟参数进行区分。Dockerfile文件中的指令必须以`FROM`开头，`FROM`指令指定了构建镜像的[基础镜像](https://docs.docker.com/glossary/)，`FROM`指令之前可以有一个或者多个`ARG`指令，它们声明了`FROM`行中使用的参数。

​	Docker认为以#开头的行为注释行，除非这个行是一个合法的**Parser Directive**。如果#不是出现在开头，Docker将认为这是一个参数，例如：

   ```
# Comment
RUN echo 'we are running some # of cool things'
   ```



### Parser Directive

​	在Dockerfile中，Parser Directive是可选的。但是如果被定义，**必须**定义在Dockerfile中的最上方，每个Parser Directive只能定义一次。格式如下：

   ```
# directive=value1
   ```

   > 规则：
   >
   > - 必须定义在最上方
   >
   > - 大小写不敏感
   >
   > - 不能重复
   >
   > - 行内空格忽略，不允许跨行

​	目前Dockerfile只支持一个Parser Directive，`escape` --- 用来设置Dockerfile中的转义字符，格式如下：

   ```
# escape=\ (backslash)
   ```

   Or

   ```
# escape=` (backtick)
   ```

​	这种转义字符在windows中非常有用，因为默认转义符`\`是windows的目录分隔符，会与转义字符产生冲突。



### 环境变量

​	Dockerfile中的环境变量使用`ENV`指令来声明，通过`$variable_name`和`${variable_name}`方式来引用。

   > 大括号语法特点 --- `${variable_name}` :
   >
   > 1. 可以用来处理组合变量名问题，例如`${foo}_bar`;
   >
   > 2. 支持下面两种标准的bash修饰符：(word可以为任何字符串，也可以为其他环境变量)
   >    - `${variable:-word}`---如果定义了变量，那么值为变量值，否则为word；
   >    - `${variable:+word}`---如果定义了变量，那么值为word，否则为空字符串。

​	可以通过转义字符`\`将变量引用转义为字面值，例如：`\$foo`的值为字符串`$foo`，而不是变量foo的值。

​	Dockerfile中以下指令都支持使用环境变量：

- ADD
- COPY
- ENV
- EXPOSE
- FROM
- LABEL
- STOPSIGNAL
- USER
- VOLUME
- WORKDIR
- ONBUILD(注意该指令只在与上面任意一个指令结合使用时才支持环境变量)

> 注意：在1.4版本之前，ONBUILD指令无论是否与其他指令结合使用，都不支持环境变量。

​	定义的环境变量只从下一个指令开始生效，在本指令中不生效。例如：

```
ENV abc=hello
ENV abc=bye def=$abc
ENV ghi=$abc
```

def的值为hello，ghi的值为bye。因为定义def时将abc设置为bye还没生效。



### .dockerignore

​	在docker客户端将构建的上下文发送给docker守护进程之前，它会检查构建根目录下的.dockerignore文件。如果该文件存在，客户端将修改上下文以排除与其模式匹配额文件和目录。这有助于避免将不必要的过大的或者敏感的文件和目录发送给守护进程，从而通过`ADD`或者`COPY`命令将它们添加到镜像中。

​	`#`在.dockerignore文件中用来表示注释，文件路径匹配规则采用go的[文件路径匹配规则](http://golang.org/pkg/path/filepath#Match)(与git的.gitignore类似)，使用`!`来表示不被排除的文件或者目录。



### FROM

​	格式为：

```
FROM <image> [AS <name>]
```

Or

```
FROM <image>[:<tag>] [AS <name>]
```

Or

```
FROM <image>[@<digest>] [AS <name>]
```

​	`FROM`指令用来开始一个构建并且给后面的指令指定一个基础镜像。所以，一个合法的Dockerfile文件必须以`FROM`指令开始，基础镜像可以是任何一个合法的镜像。

​	使用`FROM`指令需要注意一下几点：

- `ARG`指令是唯一可以出现在`FROM`指令之前的一个指令。
- `FROM`指令可以在Dockerfile文件中出现多次，用来创建多个镜像或者将一个构建阶段作为另一个的依赖(只需要记录上次构建时的commit输出的最后一个镜像ID)。每一个`FROM`执行前都会清楚之前指令创建的任何状态。
- (可选)可以通过给`FROM`指令添加`As name`来给构建的镜像提供一个名称，该名称可以用于后续的`FROM`和`COPY --from=<name|index>`指令。
- tag和digest是可选的，如果没有设置，docker会默认使用latest作为tag。



### ARG

​	`FROM`指令支持定义在**第一个**`FROM`指令之前的`ARG`指令定义的变量。例如：

```
ARG  CODE_VERSION=latest
FROM base:${CODE_VERSION}
CMD  /code/run-app

FROM extras:${CODE_VERSION}
CMD  /code/run-extras
```

​	定义在`FROM`指令之前的`ARG`指令其实不属于构建环节，所以除了`FROM`指令其他任何指令都无法使用`ARG`指令定义的变量，如果想要使用该变量，需要在构建环节使用`ARG`定义一个重名的空值变量。例如：

```
ARG VERSION=latest
FROM busybox:$VERSION
ARG VERSION
RUN echo $VERSION > image_version
```



### ENV

​	`ENV`指令有两种格式：

```
ENV <key> <value> (第一个空格后面的所有内容都会被当成value，包括空格；因为可能被其他变量引用，所以引号需要转义，否则会被删除)
```

OR

```
ENV <key>=<value> ... (允许一次定义多个变量，如果想要添加空格，可以使用引号和转义符)
```

​	`ENV`指令用来定义环境变量，该变量只会对**本指令之后的指令**生效，并且使用`ENV`设置的环境变量会在镜像中**一直存在**。

​	可以使用`docker inspect`查看设置的环境变量，使用`docker run --env <key>=<value>`替换镜像中的环境变量值。

​	下面两种形式的写法是一样的意思：

```
ENV myName="John Doe" myDog=Rex\ The\ Dog \
    myCat=fluffy
```

```
ENV myName John Doe
ENV myDog Rex The Dog
ENV myCat fluffy
```

> 注意：使用环境变量持久化可能会产生非预期的后果，可以使用`RUN <key>=<value> <command>`给单一的命令设置变量。

> `ARG`&`ENV`：`ARG`指令一般给`FROM`设置变量，`ENV`则是给镜像和其后的指令设置变量；`ARG`在`FROM`之前，`ENV`在`FROM`之后；



### RUN

​	`RUN`指令有两种格式：

```
RUN <command> (shell格式，命令在shell环境中执行，linux默认为/bin/sh -c，windows默认为cmd /S /C)
```

OR

```
RUN ["executable", "param1", "param2"] (exec格式)
```

​	`RUN`命令将会在基于当前镜像的新镜像层中执行所有命令并提交结果，该结果会被Dockerfile文件中的下一个命令使用。

- EXEC格式

  可以避免shell字符串篡改，并且可以在没有shell可执行环境的image中执行命令，同时可以指定想要的可执行环境。例如：

  ```
  RUN ["/bin/bash", "-c", "echo hello"]
  ```

  > 注意：exec格式中必须使用双引号，因为被解析为JSON数组。**exec格式不启动shell环境，所以正常的shell环境变量就无法获取。此时如果执行`RUN ["echo", "$HOME"]`，环境变量HOME将无法获取，如果想要获取shell环境，只需要执行`RUN ["sh", "-c", "echo $HOME"]`就可以了。

- SHELL格式

  shell格式的默认可执行环境可以被`SHELL`指令替换。

  shell格式中可以使用`\`来换行，下面两种写法一样：

  ```
  RUN /bin/bash -c 'source $HOME/.bashrc; \
  echo $HOME'
  ```

  AND

  ```
  RUN /bin/bash -c 'source $HOME/.bashrc; echo $HOME'
  ```

  RUN`指令的缓存会在下一次构建中被重用。例如`RUN apt-get dist-upgrade -y`，可以通过在`build`命令添加`--no-cache`标记来禁用缓存。



### CMD

​	`CMD`指令有三种格式：

```
CMD ["executable","param1","param2"] (exec格式，也是推荐格式)
```

OR

```
CMD ["param1","param2"] (作为ENTRYPOINT指令的默认参数)
```

OR

```
CMD command param1 param2 (shell格式，命令在shell环境中执行，linux默认为/bin/sh -c)
```

​	当以**exec**或者**shell**格式来指定`CMD`指令时，它用来设置启动容器时要执行的默认命令。当省略可执行文件(参数中的*executable*和*command*)时，必须与`ENTRYPOINT`指令一起使用来指定启动容器时执行的命令。

​	在Dockerfile文件中，`CMD`指令只能出现一次，当出现多次时只有**最后一个**会生效。

> **注意：exec格式的注意事项同`RUN`指令。**

​	可以通过	`docker run`命令来覆盖`CMD`指令设置的容器启动时的默认命令。如果想要让容器每次启动都执行相同的命令，请使用`ENTRYPOINT`指令。



### ENTRYPOINT

​	`ENTRYPOINT`指令有两种格式：

```
ENTRYPOINT ["executable", "param1", "param2"] (exec格式，推荐格式)
```

OR

```
ENTRYPOINT command param1 param2 (shell格式)
```

​	`ENTRYPOINT`指令用来设置容器启动后执行的命令。同CMD指令一样，ENTRYPOINT指令可以被定义多个，但是只有最后一个才会生效。

> 高能预警：很好，现在我们已经彻底的被`RUN`、`CMD`、`ENTRYPOINT`这三个指令完全弄懵逼了！那么这三个家伙之间到底有什么区别呢？？？`RUN`指令在镜像层中运行命令并且将结果提交给下一个指令使用，一般用来给镜像中安装一些软件，它在镜像的构建阶段生效；`CMD`和`ENTRYPOINT`指令在构建阶段不起作用，只有在容器启动时才会生效。`CMD`指令可以设置容器启动时的**默认**执行命令，但是可以被`docker run`命令中的命令或者参数取代；`ENTRYPOINT`指令用来设置容器启动时的执行命令，参数不能被`docker run`命令后的参数替换，但是`ENTRYPOINT`指令可以通过`docker run --entrypoint`来被替换。

* EXEC格式

  exec格式会将docker run命令传递过来的所有参数追加在自己参数的后面，同时这些参数会替换CMD指令中定义的所有参数。例如：

  ```
  # Dockerfile
  FROM ubuntu
  ENTRYPOINT ["echo", "hello"]
  CMD ["cmd"]
  ```

  ```
  docker run -it --rm --name test param
  ```

  输出结果为：hello param。

  > **注意：exec格式的注意事项同`RUN`指令。关于exec格式的更多用法，请参考[官方文档](https://docs.docker.com/engine/reference/builder/#exec-form-entrypoint-example)。**

* SHELL格式

  shell格式不会使用`CMD`指令或者`docker run`命令传递的参数，同时它有一个缺陷，当使用shell格式时，sh将会作为启动容器的`PID 1`，并且它默认不能处理传递进来的`UNIX`信号，这时如果`docker stop <container>`命令传递一个`SIGTERM`进来就会出现问题。例如启动一个redis容器，当调用`docker stop`命令时会忽略`SIGTERM`信号，那么容器会在等待10秒后强制杀死容器redis进程，这时如果redis正在进行同步操作，就会出现不同步的问题。

  > 参考：[理解Docker容器的进程管理(转载)](https://www.cnblogs.com/ilinuxer/p/6188303.html)

  启动下面的容器，会发现`PID 1`进程为`/bin/sh`：

  ```
  FROM ubuntu
  ENTRYPOINT exec top -b
  ```

  ```
  $ docker run -it --name test top --ignored-param2
  Mem: 1704184K used, 352484K free, 0K shrd, 0K buff, 140621524238337K cached
  CPU:   9% usr   2% sys   0% nic  88% idle   0% io   0% irq   0% sirq
  Load average: 0.01 0.02 0.05 2/101 7
    PID  PPID USER     STAT   VSZ %VSZ %CPU COMMAND
      1     0 root     S     3168   0%   0% /bin/sh -c top -b cmd cmd2
      7     1 root     R     3164   0%   0% top -b
  ```

  如果想要shell格式可以正确处理`UNIX`信号，需要从`exec`启动该命令：

  ```
  FROM ubuntu
  ENTRYPOINT exec top -b
  ```

  ```
  $ docker run -it --rm --name test top
  Mem: 1704520K used, 352148K free, 0K shrd, 0K buff, 140368121167873K cached
  CPU:   5% usr   0% sys   0% nic  94% idle   0% io   0% irq   0% sirq
  Load average: 0.08 0.03 0.05 2/98 6
    PID  PPID USER     STAT   VSZ %VSZ %CPU COMMAND
      1     0 root     R     3164   0%   0% top -b
  ```

  这时候使用`docker stop`命令才能干净优雅的退出容器。

  关于更多shell格式的用法，请参考[官方文档](https://docs.docker.com/engine/reference/builder/#shell-form-entrypoint-example)。

* 和CMD命令结合

  下面的表展示了不同格式的`CMD`和`ENTRYPOINT`指令的组合结果：

  |                                | No ENTRYPOINT              | ENTRYPOINT exec_entry p1_entry | ENTRYPOINT [“exec_entry”, “p1_entry”]          |
  | ------------------------------ | -------------------------- | ------------------------------ | ---------------------------------------------- |
  | **No CMD**                     | *error, not allowed*       | /bin/sh -c exec_entry p1_entry | exec_entry p1_entry                            |
  | **CMD [“exec_cmd”, “p1_cmd”]** | exec_cmd p1_cmd            | /bin/sh -c exec_entry p1_entry | exec_entry p1_entry exec_cmd p1_cmd            |
  | **CMD [“p1_cmd”, “p2_cmd”]**   | p1_cmd p2_cmd              | /bin/sh -c exec_entry p1_entry | exec_entry p1_entry p1_cmd p2_cmd              |
  | **CMD exec_cmd p1_cmd**        | /bin/sh -c exec_cmd p1_cmd | /bin/sh -c exec_entry p1_entry | exec_entry p1_entry /bin/sh -c exec_cmd p1_cmd |



### WORKDIR

​	`WORKDIR`指令格式如下：

```
WORKDIR /path/to/workdir
```

​	`WORKDIR`指令用来给位于其后的指令(`RUN CMD ENTRYPOINT COPY ADD`)设置工作目录，如果没有设置该指令，则默认工作目录为根目录`/`。

​	`WORKDIR`指令可以在Dockerfile文件中出现多次，如果设置的值是相对路径，那么它相对于之前的`WORKDIR`来设置的。例如：

```
WORKDIR /a
WORKDIR b
WORKDIR c
RUN pwd
```

​	输出结果为：`/a/b/c`。

​	`WORKDIR`变量可以引用`ENV`设置的环境变量并且只能使用Dockerfile文件中显示定义的变量。例如：

```
ENV DIRPATH /path
WORKDIR $DIRPATH/$DIRNAME
RUN pwd
```

​	输出结果为：`/path/$DIRNAME`。
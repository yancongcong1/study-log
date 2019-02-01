[TOC]

# Dockerfile详解

​	[官方文档](https://docs.docker.com/engine/reference/builder/)，docker可以通过`docker build`命令读取Dockerfile文件来创建docker镜像，这种方法中Dockerfile文件至关重要，下面我们就来详细了解一下Dockerfile文件。

​	[docker构建上下文(转载)](http://www.cnblogs.com/sparkdev/p/9573248.html)



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

- `FROM`指令可以在Dockerfile文件中出现多次，用来创建多个镜像或者将一个构建阶段作为另一个的依赖(只需要记录上次构建时的commit输出的最后一个镜像ID)。每一个`FROM`执行前都会清除之前指令创建的任何状态。

- (可选)可以通过给`FROM`指令添加`As name`来给构建的镜像提供一个名称，该名称可以用于后续的`FROM`和`COPY --from=<name|index>`指令。

  参考文档：[docker多阶段构建(转载)](https://www.cnblogs.com/sparkdev/p/8508435.html)

- tag和digest是可选的，如果没有设置，docker会默认使用latest作为tag。



### ARG

​	`ARG`指令格式如下：

```
ARG <name>[=<default value>]
```

​	`ARG`指令用来定义构建时期的变量。Dockerfile中可以使用多个`ARG`指令。可以使用d`ocker build --build-arg <varname>=<value>`来覆盖变量的默认值，但是如果Dockerfile文件中没有定义该变量，docker会抛出一个警告：

```
[Warning] One or more build-args [foo] were not consumed.
```

​	使用`docker history`命令可以查看构建时的变量值，所以不建议使用ARG指令来设置密钥信息。

​	`FROM`指令可以使用**首个**`FROM`指令之前的`ARG`指令定义的变量。例如：

```
ARG  CODE_VERSION=latest
FROM base:${CODE_VERSION}
CMD  /code/run-app

FROM extras:${CODE_VERSION}
CMD  /code/run-extras
```

- **默认值**

  `ARG`指令可以设置默认值，如果在构建时没有通过`docker build`命令传递构建变量值将会使用默认值：

  FROM busybox
  ARG user1=someuser
  ARG buildno=1
  ...

- **scope**

  1. 定义在`FROM`指令之前的`ARG`指令其实不属于构建环节，所以除了`FROM`指令其他任何指令都无法使用`ARG`指令定义的变量，如果想要使用该变量，需要在构建环节使用`ARG`定义一个重名的空值变量。例如：

     ```
     ARG VERSION=latest
     FROM busybox:$VERSION
     ARG VERSION
     RUN echo $VERSION > image_version
     ```

  2. `ARG`指令定义的变量只在对指令之后的其他指令有效。例如：

     ```
     1 FROM busybox
     2 USER ${user:-some_user}
     3 ARG user
     4 USER $user
     ...
     ```

     使用`docker build --build-arg user=what_user .`构建，结果为：第2行值为`some_user`，第4行值为`what_user`。

  3. `ARG`指令定义的变量在构建阶段结束时失效，下个构建阶段的参数必须重新定义。例如：

     ```
     FROM busybox
     ARG SETTINGS
     RUN ./run/setup $SETTINGS
     
     FROM busybox
     ARG SETTINGS
     RUN ./run/other $SETTINGS
     ```

- **与`ENV`指令结合**

  `ENV`指令定义的变量会覆盖`ARG`指令定义的变量。例如：

  ```
  1 FROM ubuntu
  2 ARG CONT_IMG_VER
  3 ENV CONT_IMG_VER ${CONT_IMG_VER:-v1.0.0}
  4 RUN echo $CONT_IMG_VER
  ```

  当使用`docker build .`构建镜像时，结果为：第4行的值为v1.0.0，因为`docker build`命令没有给变量设置值。

- **预定义变量**

  docker有一组预定义的`ARG`变量，可以不在Dockerfile中定义而直接通过`docker build`设置值：

  ```
  HTTP_PROXY
  http_proxy
  HTTPS_PROXY
  https_proxy
  FTP_PROXY
  ftp_proxy
  NO_PROXY
  no_proxy
  ```

  例如：`docker build -build-arg HTTP_PROXY=http://user:pass@proxy.lon.example.com`

  默认情况下这些预定义变量无法被`docker history`命令查到，也不会被缓存。可以通过在Dockerfile中重新定义来改变这个状况：

  ```
  FROM ubuntu
  ARG HTTP_PROXY
  RUN echo "Hello World"
  ```

- **对构建缓存的影响**

  参考[官方文档](https://docs.docker.com/engine/reference/builder/#impact-on-build-caching)



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



### WORKDIR

​	`WORKDIR`指令格式如下：

```
WORKDIR /path/to/workdir
```

​	`WORKDIR`指令用来给位于其后的指令(`RUN CMD ENTRYPOINT COPY ADD`)设置工作目录(镜像层中的目录)，如果没有设置该指令，则默认工作目录为根目录`/`。

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

  > **注意：exec格式中必须使用双引号，因为被解析为JSON数组。**exec格式不启动shell环境，所以正常的shell环境变量就无法获取。此时如果执行`RUN ["echo", "$HOME"]`，环境变量HOME将无法获取，如果想要获取shell环境，只需要执行`RUN ["sh", "-c", "echo $HOME"]`就可以了。

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

  `RUN`指令的缓存会在下一次构建中被重用。例如`RUN apt-get dist-upgrade -y`，可以通过在`build`命令添加`--no-cache`标记来禁用缓存。



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

> FBI高能预警：很好，现在我们已经彻底的被`RUN`、`CMD`、`ENTRYPOINT`这三个指令完全弄懵逼了！那么这三个家伙之间到底有什么区别呢？？？`RUN`指令在镜像层中运行命令并且将结果提交给下一个指令使用，一般用来给镜像中安装一些软件，它在镜像的构建阶段生效；`CMD`和`ENTRYPOINT`指令在构建阶段不起作用，只有在容器启动时才会生效。`CMD`指令可以设置容器启动时的**默认**执行命令，但是可以被`docker run`命令中的命令或者参数取代；`ENTRYPOINT`指令用来设置容器启动时的执行命令，参数不能被`docker run`命令后的参数替换，但是`ENTRYPOINT`指令可以通过`docker run --entrypoint`来被替换。
>
> **注意：CMD指令为ENTRYPOINT指令提供默认参数是基于镜像层次结构生效的，而不是基于是否在同个Dockerfile文件中。意思就是说，如果Dockerfile指定基础镜像中是ENTRYPOINT指定的启动命令，则该Dockerfile中的CMD依然是为基础镜像中的ENTRYPOINT设置默认参数。**



### COPY

​	`COPY`指令有两种格式：

```
COPY [--chown=<user>:<group>] ["<src>",... "<dest>"] (路径中包含空格可以使用该格式)
```

OR

```
COPY [--chown=<user>:<group>] <src>... <dest>
```

​	`COPY`指令将构建上下文中源目录的文件或文件夹拷贝到镜像文件系统的目标目录中。路径中可以使用通配符(GO的文件规则)，例如：

```
COPY hom* /mydir/        # adds all files starting with "hom"
COPY hom?.txt /mydir/    # ? is replaced with any single character, e.g., "home.txt"
```

​	源目录相对于构建上下文，目标目录相对于`WORKDIR`目录。

​	在复制包含特殊字符的文件或者目录时，需要转义那些遵循`Golang`规则的路径。例如复制一个名为arr[0].txt的文件：

```
COPY arr[[]0].txt /mydir/    # copy a file named "arr[0].txt" to /mydir/
```

​	所有复制后的文件和目录的所有权的`UID`和`GID`都为0，可以使用可选选项`--chown`来指定文件和目录的所有权为特定用户、组或者指定的`UID`和`GID`组合。如果只提供了一个用户名(组名)或者只提供了一个`UID(GID)`，那么该文件或者目录的用户和组名称相同。如果给文件或目录设置了组权限，那么容器会在根目录的`/etc/passwd`和`/etc/group`文件中来记录用户名、组名和`UID`以及`GID`的对应关系。如果容器根目录没有`/etc/passwd`和`/etc/group`文件，并且使用了`--chown`选项来指定文件所有权，那么构建将会失败并报错。下面是`--chown`的用法：

```
COPY --chown=55:mygroup files* /somedir/
COPY --chown=bin files* /somedir/
COPY --chown=1 files* /somedir/
COPY --chown=10:11 files* /somedir/
```

​	`COPY`指令也可以通过`--from=<name|index>`选项来指定前一阶段构建的产物(参照`FROM`指令的多阶段构建)，name为`FROM`指令指定的别名，index为构建标记，从0开始。如果没有找到指定名称的构建阶段，则会寻找具有相同名称的镜像。

> `COPY`指令需要遵循以下规则：
>
> - 源目录必须在构建上下文路径中，`COPY ../something /something`这种写法是非法的，因为docker构建开始时会将上下文目录(和子目录)发送到docker守护进程，而`../`父目录没有被发送。
> - 如果源是一个目录，那么将会复制目录的全部内容包括系统元数据。
> - 如果源是一个文件，目标则是一个目录，那么源文件会被复制到目标文件的根目录下。
> - 如果指定了多个源(可以使用通配符)，那么目标必须是一个目录，并且必须以`/`结尾。
> - 如果目标不以`/`结尾，那么它被认为是一个文件，源文件的内容将会被复制到其中。
> - 如果目标不存在，那么它会被自动创建。



### ADD

​	ADD指令有两种格式：

```
ADD [--chown=<user>:<group>] ["<src>",... "<dest>"]
```

OR

```
ADD [--chown=<user>:<group>] <src>... <dest>
```

​	`ADD`指令和`COPY`指令有点类似，但是除了`COPY`指令的功能外，`ADD`还可以将远程源下载到本地镜像文件系统的指定目标处。

​	***`ADD`指令拥有`COPY`指令所有的特征，功能和规则***。除此之外，它还具有一些特殊的规则：

​	当源是一个远程文件或目录的时候，目标文件或文件夹的权限将会被设置为600。还有如果使用http来获取远程文件，如果具有`Last Modified`响应头，那么该时间戳将会被设置为目标文件的`mtime`。

​	因为`ADD`指令不支持身份验证，所以如果远程文件需要进行身份验证，需要使用`RUN wget`、`RUN curl`或者`容器中的其他工具`来获取文件(**也是推荐的方式**)。

​	如果源的内容发生改变，那么第一个`ADD`指令将会使Dockerfile文件中的所有以下指令的缓存失效，包括`RUN`指令的缓存。

> 除了拥有`COPY`指令的规则，`ADD`还具有：
>
> - 如果源是一个文件，那么会直接将文件下载到目标；如果源是一个目录，那么会在目标处先创建该目录，然后将内容下载到其中。
> - 如果源是**本地文件**并且被识别为一个**压缩文件**，那么它将会被解压(注意是否为压缩文件不是由文件名决定，而是由文件内容决定的)，但是**远端**的压缩文件不会被解压。解压方式为`tar -x`，解压结果为：
>   1. 所有文件和目录解压到目标目录；
>   2. 如果有文件冲突，默认使用"2"的方式解决。(应该是vi命令冲突后提示的第2种方式？？？)

> `ADD`&`COPY`：这两个指令的目的都是为了给镜像中复制文件；`COPY`只能拷贝本地文件和目录，`ADD`还可以拷贝远程文件和目录(建议使用`RUN wget`来获取)；`COPY`不能解压缩文件，`ADD`可以自动解压本地压缩文件；



### USER

​	`USER`指令有两种格式：

```
USER <user>[:<group>]
```

OR

```
USER <UID>[:<GID>]
```

​	`USER`指令给Dockerfile中位于其后的`RUN`、`CMD`、`ENTRYPOINT`指令设置运行的用户和组权限。

> 注意：
>
> - 组权限可以省略，默认为root组权限。
>
> - 在windows上，如果用户不是内置账户，需要先创建该账户。例如：
>
>   ```
>   FROM microsoft/windowsservercore
>   # Create Windows user in the container
>   RUN net user /add patrick
>   # Set it for subsequent commands
>   USER patrick
>   ```



### LABEL

​	`LABEL`格式：

```
LABEL <key>=<value> <key>=<value> <key>=<value> ...
```

​	`LABEL`用来给镜像设置元数据。`LABEL`以键值对的形式出现在Dockerfile文件中，Dockerfile文件中可以有多个`LABEL`指令。可以在一个`LABEL`指令中指定多个键值对。例如：

```
LABEL "com.example.vendor"="ACME Incorporated"
LABEL com.example.label-with-value="foo"
LABEL version="1.0"
LABEL description="This text illustrates \
that label-values can span multiple lines."
```

​	`LABEL`指令可以从父镜像中继承，如果定义了同名的label，那么将会覆盖父镜像中的值。可以使用`docker inspect`来查看镜像的元数据信息。

> 注意：如果想要在`LABEL`中使用空格，需要使用**双引号**或者**转义字符**。



### EXPOSE

​	`EXPOSE`格式：

```
EXPOSE <port> [<port>/<protocol>...]
```

​	`EXPOSE`指令用来指定容器启动后暴露给宿主机的端口，可以指定协议，默认为`TCP`协议。例如：

```
EXPOSE 80/tcp
EXPOSE 80/udp
```

​	这时候可以同时将tcp和udp的80端口发布给宿主机：`docker run -p 80:80/tcp -p 80:80/udp ...`

​	`EXPOSE`指令实际上并不会发布端口，假如使用`EXPOSE`指令暴露了镜像的80端口，这时启动容器后却发现宿主机无法访问到该端口，这是因为还没有将80端口映射到宿主机，也就是说`EXPOSE`只是构建镜像的人提供的可以监听容器内部的端口，最后还需要启动容器的人来决定是否发布到宿主机。可以通过`-p`或者`-P`参数来建立映射。

> `docker network`命令支持在容器之间直接通信，而不需要公开和发布特定的端口。



### SHELL

​	`SHELL`格式：

```
SHELL ["executable", "parameters"]
```

​	`SHELL`指令可以重写其他指令(`RUN`、`CMD`、`ENTRYPOINT`)shell格式的默认SHELL环境，必须使用双引号。linux默认SHELL环境为`["/bin/sh", "-c"]`，windows默认SHELL环境为`["cmd", "/S", "/C"]`。

​	`SHELL`指令在windows中非常有用，因为windows中有两种完全不同且常用的SHELL：`cmd`和`powershell`，还有备用的`sh`。

​	`SHELL`指令可以出现多次，每个`SHELL`指令都会覆盖之前的SHELL指令，对后续所有的shell格式的指令生效。

​	下面举例说明`SHELL`指令的用法：

```
...
RUN powershell -command Execute-MyCmdlet -param1 "c:\foo.txt"
...
```

​	docker调用的命令为：

```
cmd /S /C powershell -command Execute-MyCmdlet -param1 "c:\foo.txt"
```

​	上面这种写法十分低效：第一，需要运行一个完全无用的`cmd`进程；第二，`RUN`命令需要加上`powershell --command`前缀来强制改变默认shell环境。

​	可以通过两种方法来改进这个问题：第一可以通过`RUN``指令的exec格式来指定shell环境；第二可以通过SHELL`指令来替换默认SHELL环境。使用SHELL指令的方式则会显得更加自然：

```
...
RUN ["powershell", "-command", "Execute-MyCmdlet", "-param1 \"c:\\foo.txt\""]
...
```

```
# escape=`

FROM microsoft/nanoserver
SHELL ["powershell","-command"]
RUN New-Item -ItemType Directory C:\Example
ADD Execute-MyCmdlet.ps1 c:\example\
RUN c:\example\Execute-MyCmdlet -sample 'hello world'
```



### VOLUME

​	`VOLUME`指令有两种格式：

```
VOLUME ["/data1", "/data2"]
```

OR

```
VOLUME /data1 /data2
```

​	`VOLUME`指令会将容器的某些指定目录挂载为匿名卷。这边暂时不对卷的内容多做阐述。

> 参考链接：
>
> [Dockerfile指令详解 && VOLUME 指令(转载)](https://www.cnblogs.com/reachos/p/8621748.html)
>
> [docker学习笔记18：Dockerfile 指令 VOLUME 介绍(转载)](https://www.cnblogs.com/51kata/p/5266626.html)



### ONBUILD

​	`ONBUILD`指令格式如下：

```
ONBUILD [INSTRUCTION]
```

​	`ONBUILD`指令会将一个触发器指令添加到镜像中。当某个构建以该镜像为基础时会触发触发器指令，相当于在该构建的`FROM`指令之后立即插入了触发器指令。

​	除了`FROM`和`MAINTAINER` 之外的任何构建指令都可以被作为触发器指令。例如：

```
[...]
ONBUILD ADD . /app/src
ONBUILD RUN /usr/local/bin/python-build --dir /app/src
[...]
```

> 注意：
>
> - ONBUILD指令不支持链式调用，例如： `ONBUILD ONBUILD` 
> - 更多内容请参考：[官方文档](https://docs.docker.com/engine/reference/builder/#onbuild)



### STOPSIGNAL

​	`STOPSIGNAL`格式如下：

```
STOPSIGNAL signal
```

​	`STOPSIGNAL`指令设置停止容器是默认使用的signal。这个信号可以是一个有效的数字(例如9)，也可以是`SIGNAME`格式的名称(例如SIGKILL)。



### HEALTHCHECK

​	`HEALTHCHECK`指令格式有两种：

```
HEALTHCHECK [OPTIONS] CMD command (通过运行一个命令来检查容器是否处于健康状态)
```

OR

```
HEALTHCHECK NONE (禁用从基础镜像集成的所有HEALTHCHECK)
```

​	`HEALTHCHECK`指令可以告诉docker如何检测一个容器是否正常运行。可以检测到一些异常情况，例如web服务器正常运行，但是服务端陷入死循环，无法处理新链接的问题。

​	可以通过`docker inspect`命令查看容器健康信息。更多使用请参考：[官方文档](https://docs.docker.com/engine/reference/builder/#healthcheck)
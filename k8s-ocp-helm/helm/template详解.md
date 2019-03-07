[TOC]

# Template详解

本节主要介绍一下内容：

- template/目录
- 模板语法
- 内置对象以及values.yaml
- 模板方法



## templates目录

templates目录用来存放模板文件。当Tiller服务计算Chart结果的时候，会将templates目录下的所有文件发送给模版引擎来处理，然后收集结果并将结果发送给Kubernetes。

使用`helm create`命令创建一个新的Chart，templates目录如下：

```
mychart/
  templates/
    NOTES.txt		# The “help text” for your chart. This will be displayed to your users when they run helm install.
    deployment.yaml # A basic manifest for creating a Kubernetes deployment
    service.yaml	# A basic manifest for creating a service endpoint for your deployment
    _helpers.tpl	# A place to put template helpers that you can re-use throughout the chart
```

我们简单介绍一下上面几类文件：

- NOTES.txt：存放着Chart使用的一些相关信息，在使用`install`或者其他命令后其内容会被输出到终端。[详细信息](https://helm.sh/docs/chart_template_guide/#creating-a-notes-txt-file)
- \_\*.\*：以`_`打头的文件不作为Kubernetes对象定义，而是被用来定义局部模板，其内容可以在Chart模板中使用。
- *.yaml：除以上两种的文件都为Helm的模板文件，也就是被用来定义Kubernetes对象。

> NOTES：Template没有严格的命名规则，建议使用.yaml作为YAML文件的后缀，使用.tpl作为辅助文件的后缀。
>



## 模版语法

template相当于Kubernetes中的资源，每一个template对应着一个Kubernetes资源。可以看到，template定义资源语法与Kubernets定义资源语法很相似。

让我们从一个例子来说明模板语法，假如我们创建了一个文件叫mychart/templates/configmap.yaml：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mychart-configmap
data:
  myvalue: "Hello World"
```

我们使用`helm install`命令安装后返回如下信息：

```console
NAME: full-coral
LAST DEPLOYED: Tue Nov  1 17:36:01 2016
NAMESPACE: default
STATUS: DEPLOYED

RESOURCES:
==> v1/ConfigMap
NAME                DATA      AGE
mychart-configmap   1         1m
```

我们成功创建了一个ConfigMap资源，通过`helm get manifest`命令我们可以获取release的信息以及Kubernetes资源：

```console
---
# Source: mychart/templates/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: mychart-configmap
data:
  myvalue: "Hello World"
```

上面模板的name是直接硬编码到模板中的，这种方式显而易见不是最佳方式。那么我们如何动态的生成一个模板名称呢？

> name字段：因为DNS系统的限制长度最大为53。

让我们用重新编写configmap.yaml文件：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
data:
  myvalue: "Hello World"
```

可以发现我们将name字段写成了{{ .Release.Name }}-configmap，让我们使用`helm install --debug --dry-run ./mychart`命令来模拟安装该Chart：

```console
$ helm install --debug --dry-run ./mychart
SERVER: "localhost:44134"
CHART PATH: /Users/mattbutcher/Code/Go/src/k8s.io/helm/_scratch/mychart
NAME:   goodly-guppy
TARGET NAMESPACE:   default
CHART:  mychart 0.1.0
MANIFEST:
---
# Source: mychart/templates/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: goodly-guppy-configmap
data:
  myvalue: "Hello World"
```

可以看到Kubernetes的ConfigMap资源的name被赋予了正确的值。

**Helm模板语法就是Kubernetes模板语法以及模板指令的结合(在`{{`和`}}`中定义模板指令)。**在指令`{{ .Release.Name }}`中我们看到Helm模板指令使用`.`来定位作用域(命名空间)。指令中的第一个`.`表示最顶层的作用域，关于这部分我们会在之后的小节中介绍。上面的指令意思为：在最顶层的域中寻找Release(Helm的内置对象之一)对象，然后再Release对象域中寻找Name对象。

### 命名空间

指令中的第一个.表示Chart的根目录上下文，也就是顶层作用域或命名空间，它可以获取到一些内置对象以及文件。



## Helm内置对象

对象通过模板引擎来传递给模板，对象可以是一个简单的值或者包含其他对象或者方法。

helm中的内种对象主要有以下几个：

- Release---描述release的信息
- Values---从values.yaml中或者自定义文件中传入模板中的值，默认是empty
- Chart---Chart.yaml中的内容，其中的任何字段都可以获取，例如{{.Chart.Name}}-{{.Chart.Version}}
- Files---提供了对Chart中非特殊文件的访问方法
- Capabilities---提供了关于Kubernetes集群支持哪些功能的信息
- Template---包含当前正在执行的模板的信息

接下来我们详细介绍一下各个对象的内容：

- Release

  | 对象      | 含义                                               |
  | --------- | -------------------------------------------------- |
  | Name      | release的名称                                      |
  | Time      | release创建的时间                                  |
  | Namespace | release所在的命名空间                              |
  | Service   | Tiller服务的名称                                   |
  | Revision  | release的版本号，从1开始并且每使用一次upgrade递增1 |
  | IsUpgrade | 当使用upgrade或者rollback命令后为true              |
  | IsInstall | 使用install后为true                                |

- Values

  自定义values.yaml如下：

  ```yaml
  favorite:
    drink: coffee
    food: pizza
  ```

  我们可以这样使用：

  ```yaml
  apiVersion: v1
  kind: ConfigMap
  metadata:
    name: {{ .Release.Name }}-configmap
  data:
    myvalue: "Hello World"
    drink: {{ .Values.favorite.drink }}
    food: {{ .Values.favorite.food }}
  ```

  当然values.yaml中的值可以被命令行参数或者其他自定义文件中的值替代。

- Chart

  所有的对象可以在[Charts Guide](https://github.com/helm/helm/blob/master/docs/charts.md#the-chartyaml-file)中找到，对象使用大写例如：{{.Chart.Name}}

- Files

  | 方法     | 含义                                   |
  | -------- | -------------------------------------- |
  | Get      | 通过文件名称获取文件内容，返回字符串   |
  | GetBytes | 通过文件名称获取文件内容，返回字节数组 |

  [详细信息](https://helm.sh/docs/chart_template_guide/#accessing-files-inside-templates)

- Capabilities

  详细信息请参考[内置对象](https://helm.sh/docs/chart_template_guide/#built-in-objects)中相关部分

- Template

  | 对象     | 含义                                                         |
  | -------- | ------------------------------------------------------------ |
  | Name     | 当前template的文件路径，例如mychart/templates/mytemplate.yaml |
  | BaesPath | 当前template所在文件的路径，例如mychart/templates            |

> 注意：这些对象可以在任何顶级模板中使用，但是并不表示可以在任何地方使用。



### values.yaml

在Chart中可以使用values.yaml文件来自定义模板中使用的值，Helm会自动将values.yaml文件中的值传递给模板引擎用来计算，例如：

```yaml
imageRegistry: "quay.io/deis"
dockerTag: "latest"
pullPolicy: "Always"
storage: "s3"
```

在模板中这样使用：

```yaml
containers:
  - name: deis-database
    image: {{.Values.imageRegistry}}/postgres:{{.Values.dockerTag}}
    imagePullPolicy: {{.Values.pullPolicy}}
```

values.yaml中的值可以通过以下两种方式来进行覆盖：

- 自定义其他yaml文件

  例如定义myvals.yaml文件如下：

  ```yaml
  storage: "gcs"
  ```

  使用`helm install --values=myvals.yaml wordpress`指令来覆盖values.yaml中定义的默认值，返回结果为：

  ```yaml
  imageRegistry: "quay.io/deis"
  dockerTag: "latest"
  pullPolicy: "Always"
  storage: "gcs"
  ```

  > 注意：Chart中提供默认值的文件必须命名为values.yaml，但是自定义的文件可以随便命名。

- 命令行中使用--set

  ```
  helm install --set tags.front-end=true --set subchart2.enabled=false
  ```



## 模板方法

上次说过Helm的模板语法中支持[Go template language](https://godoc.org/text/template)中的一些函数以及[sprig库](http://masterminds.github.io/sprig/)的许多函数和Helm特定的函数，接下来我们就介绍一下Helm中函数的使用，语法：

```
functionName arg1 arg2...
```

例如：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
data:
  myvalue: "Hello World"
  drink: {{ quote .Values.favorite.drink }}
  food: {{ quote .Values.favorite.food }}
```

### PIPELINES

在模板指令中管道是一个很重要的操作，而在管道操作中`|`是很重要的关键字符，它可以将前一个命令的结果作为参数发送给接下来的命令，这允许我们连接多个命令。

管道是模板方法中的一个重要概念，它利用UNIX概念，将一系列模板命令连接在一起工作。例如：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
data:
  myvalue: "Hello World"
  drink: {{ .Values.favorite.drink | quote }}
  food: {{ .Values.favorite.food | quote }}
```

> NOTES：倒序是模板中常用的做法。你可能会经常看到这样的写法`.val | quote`而不是这样`quote .val`，当然这两种写法都没问题。

### Go templates

模板指令中可以使用[Go templates](https://godoc.org/text/template)内置的一些方法

### Sprig library

模板指令中可以使用[Sprig library](https://godoc.org/github.com/Masterminds/sprig)库中提供的方法，处于安全考虑`env` 和 `expandenv`方法无法使用。

### 特定函数

Helm中还提供了一些特定的方法：

- include：详细信息参考本文档局部模板中的INCLUDE部分

- required：

  用来表明哪些值是必须的，并且如果值不存在时指定输出的错误信息。例如：

  ```yaml
  value: {{ required "A valid .Values.who entry required!" .Values.who }}
  ```

下面我们列出一些常用的方法：

- quote：用来给字符串添加双引号

- include：引入局部模板

- required：表明必须值同时指定错误信息

- tpl：tpl函数允许将字符串内容作为模板内容来进行解析

  ```yaml
  # values
  template: "{{ .Values.name }}"
  name: "Tom"
  
  # template
  {{ tpl .Values.template . }}
  
  # output
  Tom
  ```

## 流程控制

Helm模板语法中的流程控制语句可以控制模板的生成，主要有下面几类：

- if/else条件控制
- with可以重定位作用域
- range提供类似for each的循环语句

除此之外，Helm还提供了一些声明和使用模板段的操作：

- define在当前的模板中声明一个新的模板
- template用来导入声明的模板
- block声明一个特殊的可以填充的模板区域

### 流程控制语句

#### IF/ELSE

```yaml
{{ if PIPELINE }}
  # Do something
{{ else if OTHER PIPELINE }}
  # Do something else
{{ else }}
  # Default case
{{ end }}
```

当为下面的值的时候条件的值为false，出此之外的值都为true：

- boolean值false
- 数字0
- 空字符串
- nil(empty或者null)
- 一个空集合(map，slice，tuple，dict，array)

#### WITH

with用来控制变量的作用域，模板指令中的`.`表示当前作用域。

```yaml
{{ with PIPELINE }}
  # restricted scope
{{ end }}
```

我们可以通过with语句将语句块中的作用域从`.`该为指定对象。例如：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
data:
  myvalue: "Hello World"
  {{- with .Values.favorite }}
  drink: {{ .drink | default "tea" | quote }}
  food: {{ .food | upper | quote }}
  {{- end }}
```

我们可以注意到with语句块中的当前作用域已经变为.Values.favorite了，其中的所有变量都可以直接引用了。

> NOTES：with作用域中无法获取父域中的其他对象。例如：
>
> ```yaml
>   {{- with .Values.favorite }}
>   drink: {{ .drink | default "tea" | quote }}
>   food: {{ .food | upper | quote }}
>   release: {{ .Release.Name }}
>   {{- end }}
> ```
>
> 我们可以将这些对象的引用置于with语句块外面：
>
> ```yaml
> {{- with .Values.favorite }}
>   drink: {{ .drink | default "tea" | quote }}
>   food: {{ .food | upper | quote }}
>   {{- end }}
>   release: {{ .Release.Name }}
> ```
>
> 除此之外，我们还可以通过模板变量来解决这个问题，详细信息阅读模版变量部分。

#### RANGE

range提供遍历数组的功能，如果我们有以下的values.yaml：

```yaml
pizzaToppings:
  - mushrooms
  - cheese
  - peppers
  - onions
```

我们在ConfigMap中这样写：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
data:
  myvalue: "Hello World"
  toppings: |-
    {{- range .Values.pizzaToppings }}
    - {{ . | title | quote }}
    {{- end }}
```

每次遍历后，第一次遍历的时候，`.`为当前遍历对象的顶部，之后每次遍历都会下移。

执行后模板为：

```yaml
# Source: mychart/templates/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: edgy-dragonfly-configmap
data:
  myvalue: "Hello World"
  toppings: |-
    - "Mushrooms"
    - "Cheese"
    - "Peppers"
    - "Onions"
```

> `|-`为YAML语法，表示多行字符串

#### 处理空格

让我们我们看一下以下例子：

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
data:
  myvalue: "Hello World"
  drink: {{ .Values.favorite.drink | default "tea" | quote }}
  food: {{ .Values.favorite.food | upper | quote }}
  {{if eq .Values.favorite.drink "coffee"}}
    mug: true
  {{end}}
```

当我们使用命令`helm install --dry-run --debug ./mychart`后，得到如下结果：

```console
SERVER: "localhost:44134"
CHART PATH: /Users/mattbutcher/Code/Go/src/k8s.io/helm/_scratch/mychart
Error: YAML parse error on mychart/templates/configmap.yaml: error converting YAML to JSON: yaml: line 9: did not find expected key
```

生成的模板如下：

```yaml
# Source: mychart/templates/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: eyewitness-elk-configmap
data:
  myvalue: "Hello World"
  drink: "coffee"
  food: "PIZZA"
    mug: true
```

我们发现mug出现了错误的缩进，让我们将错误的部分修改为如下：

```yaml
{{if eq .Values.favorite.drink "coffee"}}
mug: true
{{end}}
```

运行后的到正确结果，但是结果看起来却有点奇怪：

```yaml
# Source: mychart/templates/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: telling-chimp-configmap
data:
  myvalue: "Hello World"
  drink: "coffee"
  food: "PIZZA"

  mug: true
```

之所以出现上面的情况，是因为模版引擎在运行的时候，会删除在`{{`和`}}`之间的内容，但是会保留其产生的空格。在YAML文件中空格是有意义的，所以处理空格就变得格外重要了。空格生成示意图：

```yaml
  food: {{ .Values.favorite.food | upper | quote }}*
**{{ if eq .Values.favorite.drink "coffee"}}*
**mug: true*
**{{ end}}
```

Helm中提供携带特殊字符的大括号作为标记以便模版引擎可以处理空格：

`{{-` ：-后面必须跟一个空格，删除左侧产生的所有空格(本行)

 `-}}`：-前面必须加一个空格，删除右侧产生的所有空格(本行)

> NOTES：新行也是空格！

接下来我们修改模板如下：

```yaml
food: {{ .Values.favorite.food | upper | quote }}
{{- if eq .Values.favorite.drink "coffee"}}
mug: true
{{- end}}
```

执行后的到正确的结果。我们可以使用indent方法来更好的格式化我们的模板：

```yaml
food: {{ .Values.favorite.food | upper | quote }}
{{- if eq .Values.favorite.drink "coffee"}}
  {{indent 2 "mug:true"}}
{{- end}}
```

更多信息请参考[Official Go template documentation](https://godoc.org/text/template)

#### 模板变量

在模板语法中，支持模板变量的定义和使用。其语法如下：

```
定义：
$name := value
使用：
$name
```

所以在with语句块中出现的问题我们可以这样解决：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
data:
  myvalue: "Hello World"
  {{- $relname := .Release.Name -}}
  {{- with .Values.favorite }}
  drink: {{ .drink | default "tea" | quote }}
  food: {{ .food | upper | quote }}
  release: {{ $relname }}
  {{- end }}
```

在range语句块中，可以用来捕获数组项的index和value：

```yaml
toppings: |-
  {{- range $index, $topping := .Values.pizzaToppings }}
    {{ $index }}: {{ $topping }}
  {{- end }}
```

```yaml
toppings: |-
      0: mushrooms
      1: cheese
      2: peppers
      3: onions
```

对于Object来说，可以用来捕获key和value：

```yaml
data:
  myvalue: "Hello World"
  {{- range $key, $val := .Values.favorite }}
  {{ $key }}: {{ $val | quote }}
  {{- end}}
```

```yaml
data:
  myvalue: "Hello World"
  drink: "coffee"
  food: "pizza"
```

模板变量一般不是全局起作用的，它们只在定义的作用域中生效。Helm模板变量提供一个指向根上下文的全局变量`$`，某些情况下我们会需要它例如：

```yaml
{{- range .Values.tlsSecrets }}
apiVersion: v1
kind: Secret
metadata:
  name: {{ .name }}
  labels:
    # Many helm templates would use `.` below, but that will not work,
    # however `$` will work here
    app.kubernetes.io/name: {{ template "fullname" $ }}
    # I cannot reference .Chart.Name, but I can do $.Chart.Name
    helm.sh/chart: "{{ $.Chart.Name }}-{{ $.Chart.Version }}"
    app.kubernetes.io/instance: "{{ $.Release.Name }}"
    app.kubernetes.io/managed-by: "{{ $.Release.Service }}"
type: kubernetes.io/tls
data:
  tls.crt: {{ .certificate }}
  tls.key: {{ .key }}
---
{{- end }}
```

### 局部模板

我们可以在templates目录下的以`_`开头的文件中定义局部模板，当我们使用`create`命令创建一个新的Chart目录后，可以在templates目录下看到一个`_helper.tpl`文件，该文件被默认为局部模板的创建地方。局部模板也可以直接在模板文件中定义，但是为了方便观察我们将其统一定义在以`_`开头的文件中，也就是说除了`_helper.tpl`文件我们也可以自定义其他文件(必须以`_`开头)。

**值得注意的是，在Helm中局部模板的名称是全局的，如果定义了两个相同名称的局部模板，那么后被加载的会生效；同时无论局部模板被定义在什么地方，所有的模板中都可以引用已经定义的局部模板(包括子Chart中的模板)**。因为在Helm中子Chart和父Chart中的模板被同时编译，所以我们在命名是一般会加上父Chart的名称作为前缀以防止冲突。例如：

```
{{ define "mychart.labels" }}
```

#### DEFINE

该指令用来定义模板

```yaml
{{ define "MY.NAME" }}
  # body of template here
{{ end }}
```

例如：

```yaml
{{/* Generate basic labels */}}
{{- define "mychart.labels" }}
  labels:
    generator: helm
    date: {{ now | htmlDate }}
{{- end }}
```

> 可以使用{{/* ......  */}]}来添加注释

#### TEMPLATE

该指令用来引用模板

```yaml
{{- template "mychart.labels" }}
```

将mychart.labels替换成需要的局部模板的名称就行了。

#### INCLUDE

让我们看下面的例子，定义局部模板：

```yaml
{{- define "mychart.app" -}}
app_name: {{ .Chart.Name }}
app_version: "{{ .Chart.Version }}+{{ .Release.Time.Seconds }}"
{{- end -}}
```

我们这样引用：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
  labels:
    {{ template "mychart.app" .}}
data:
  myvalue: "Hello World"
  {{- range $key, $val := .Values.favorite }}
  {{ $key }}: {{ $val | quote }}
  {{- end }}
{{ template "mychart.app" . }}
```

执行命令后得到输出：

```yaml
# Source: mychart/templates/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: measly-whippet-configmap
  labels:
    app_name: mychart
app_version: "0.1.0+1478129847"
data:
  myvalue: "Hello World"
  drink: "coffee"
  food: "pizza"
  app_name: mychart
app_version: "0.1.0+1478129847"
```

我们发现两处的app_version字段缩进都有问题，为什么会出现这样的问题？因为模板是一个动作指令，而不是一个函数，被替换的模板会将文本向右对齐，数据只是被简单的内联插入，通知也无法将输出传递给函数进行处理。

为了解决这个问题，Helm提供了一个替代template指令的方法，可以将模板导入的内容传递到管道中的函数以便进行处理，该方法就是include函数。例子如下：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
  labels:
    {{- include "mychart.app" . | nindent 4 }}
data:
  myvalue: "Hello World"
  {{- range $key, $val := .Values.favorite }}
  {{ $key }}: {{ $val | quote }}
  {{- end }}
  {{- include "mychart.app" . | nindent 2 }}
```

现在我们就可以得到正确的结果了。

> 建议在Helm模板中使用include函数而不是template指令，这样的话可以更好的处理yaml文档格式。如果我们仅仅需要导入文件内容而不是作为模板，那么我们可以使用File内置对象来实现，具体参考内置对象。

#### BLOCK(deprecated )

建议使用include方法，[详情](https://helm.sh/docs/chart_template_guide/#avoid-using-blocks)

#### 局部模板中的作用域

```yaml
{{/* Generate basic labels */}}
{{- define "mychart.labels" }}
  labels:
    generator: helm
    date: {{ now | htmlDate }}
    chart: {{ .Chart.Name }}
    version: {{ .Chart.Version }}
{{- end }}
```

接写来在适当的地方引用该模板：

```gotpl
{{- template "mychart.labels" }}
```

如果我们这样定义一个模板，注意在上面的模板中我们使用了作用域来定位对象。如果此时我们使用template指令来引用模板，会的到如下结果：

```yaml
labels:
  generator: helm
  date: 2016-11-02
  chart:
  version:
```

为什么会出现上面的问题？这是因为`.`并不在我们的模板范围内，当将定义的模板使用template指令呈现的时候，模板会接受指令传入的作用域。在这个例子中我们template并没有传递一个作用域给模板，所以会出现上述问题。此时我们修改引用方法为：

```
{{- template "mychart.labels" . }}
```

重新使用`helm install --dry-run --debug ./mychart`安装会发现的到了正确结果。

## YAML语法解析

[详细信息](https://helm.sh/docs/chart_template_guide/#yaml-techniques)
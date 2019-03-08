[TOC]

# Chart

Helm使用Chart作为打包格式，Chart是用来描述Kubernetes资源的文件集合。Chart可以被用来部署简单的服务(一个缓存服务)或者复杂完整的服务(包含http服务、缓存、数据库等的完整服务)。



## 目录结构

Chart用那个有一个特定的目录结构，根目录名称即为Chart的名称，内部的目录结构如下：

```
wordpress/
  Chart.yaml          # A YAML file containing information about the chart
  LICENSE             # OPTIONAL: A plain text file containing the license for the chart
  README.md           # OPTIONAL: A human-readable README file
  requirements.yaml   # OPTIONAL: A YAML file listing dependencies for the chart
  values.yaml         # The default configuration values for this chart
  charts/             # A directory containing any charts upon which this chart depends.
  templates/          # A directory of templates that, when combined with values,
                      # will generate valid Kubernetes manifest files.
  templates/NOTES.txt # OPTIONAL: A plain text file containing short usage notes
```

下面详细介绍一下各个模块：

### Chart.yaml

Chart中必须包含该文件，该文件的具体内容如下：

```yaml
apiVersion: The chart API version, always "v1" (required)
name: The name of the chart (required)
version: A SemVer 2 version (required)
kubeVersion: A SemVer range of compatible Kubernetes versions (optional)
description: A single-sentence description of this project (optional)
keywords:
  - A list of keywords about this project (optional)
home: The URL of this project's home page (optional)
sources:
  - A list of URLs to source code for this project (optional)
maintainers: # (optional)
  - name: The maintainer's name (required for each maintainer)
    email: The maintainer's email (optional for each maintainer)
    url: A URL for the maintainer (optional for each maintainer)
engine: gotpl # The name of the template engine (optional, defaults to gotpl)
icon: A URL to an SVG or PNG image to be used as an icon (optional).
appVersion: The version of the app that this contains (optional). This needn't be SemVer.
deprecated: Whether this chart is deprecated (optional, boolean)
tillerVersion: The version of Tiller that this chart requires. This should be expressed as a SemVer range: ">2.0.0" (optional)
```

- version

  Chart拥有一个遵循 [SemVer 2](https://semver.org/) 标准的版本号(必须遵循该标准)，该版本号被用作发布标记，仓库中的包由Chart名称和版本来标识。例如一个版本为version: 1.2.3的nginx Chart在仓库中的名字为：nginx-1.2.3.tgz。

- appVersion

  该字段和version无关，一般用来简单说明app的版本，对Chart版本没有影响。

- deprecated

  如果要启用Chart，可以设置该字段。当仓库中最新版本的Chart被置为启用，Helm认为Chart被启用，可以在新的版本中通过修改该字段重新启用Chart。

### LICENSE, README.md AND NOTES.txt

LICENSE是包含Chart许可证的文本文件。

README.md应该包含以下内容：

- 应用或者Chart提供服务的描述
- 运行Chart的先决条件和要求
- values.yaml中选项的描述
- 跟Chart安装或配置相关的其他信息

NOTES.txt文件在templates/目录下面，一般记录使用说明、后续步骤或者与Chart发布相关的信息(例如是否可以连接数据库，是否提供UI)，最好有一个指向README.md的连接以便查看更详细的信息。该文件的内容一般会在使用`install`和`status`命令后被输出。

### requirements.yaml AND charts/

在Helm中，一个Chart可以依赖多个其他的Chart，它们必须在requirements.yaml文件中指定。但是可以动态的定URL或者手动在charts目录中导入其他的Chart目录。尽管手动管理的方式对于某些团队来说有一些有点，但是在requirements.yaml文件中声明依赖仍然是首选方式。

> NOTES：chart.yaml中的dependencies字段已经被移除。

- requirements.yaml

  该文件内容可能如下：

  ```yaml
  dependencies:
    - name: apache # chart名称 (required)
      version: 1.2.3 # chart版本 (required)
      repository: http://example.com/charts # chart仓库的完整URL，你必须在本地使用helm reop add指令进行添加 (required)
      alias: new-subchart-1 # chart别名 (optional)
      condition: subchart1.enabled,global.subchart1.enabled # 可以在顶级Chart(自己创建的Chart)中设置指定路径的值(boolean)，根据该值来判断子Chart是否启用，只会使用匹配的第一个值，如果没有条件失效 (optional)
      tags: # Chart的标签，同condition一样，可以在顶级Chart中设置值(boolean)，用来判定是否启用该Chart (optional)
        - front-end
        - subchart1
    - name: mysql
      version: 3.2.1
      repository: http://another.example.com/charts
  ```

  当编写好requirements.yaml文件后，可以使用`helm dependency update`命令来将依赖Chart的压缩文件下载到chart目录下面。对于上面的文件执行后chart/目录如下：

  ```
  charts/
    apache-1.2.3.tgz
    mysql-3.2.1.tgz
  ```

  - alias

    如果需要将Chart重新命名或者需要使用多个同样内容的Chart我们可以设置`alias`参数，例如：

    ```yaml
    # parentchart/requirements.yaml
    dependencies:
      - name: subchart
        repository: http://localhost:10191
        version: 0.1.0
        alias: new-subchart-1
      - name: subchart
        repository: http://localhost:10191
        version: 0.1.0
        alias: new-subchart-2
      - name: subchart
        repository: http://localhost:10191
        version: 0.1.0
    ```

    使用`helm dependency update`后目录如下：

    ```
    subchart
    new-subchart-1
    new-subchart-2
    ```

    当然我们也可以手动复制chart压缩文件然后将其重命名。

  - condition and tags

    可以通过condition和tags字段来判断是否会启用子Chart，例如：

    ```yaml
    # parentchart/requirements.yaml
    dependencies:
      - name: subchart1
        repository: http://localhost:10191
        version: 0.1.0
        condition: subchart1.enabled,global.subchart1.enabled
        tags:
          - front-end
          - subchart1
    
      - name: subchart2
        repository: http://localhost:10191
        version: 0.1.0
        condition: subchart2.enabled,global.subchart2.enabled
        tags:
          - back-end
          - subchart2
    ```

    ```yaml
    # parentchart/values.yaml
    
    subchart1:
      enabled: true
    tags:
      front-end: false
      back-end: true
    ```

    上面的例子结果如下：subchart1和subchart2都会被启用。

    我们可能会有疑问，如果tags和condition冲突时该怎么办。下面我们介绍一下两者的判断规则：

    - 如果condition被赋值，其总是优先于tags判定
    - 如果condition被赋值，只有第一个匹配到的会生效
    - 如果有多个tag，那么只要其中一个为true，Chart将被启用
    - condition和tags的值只能在顶级Chart中的Values.yaml中设置才生效
    - The `tags:` key in values must be a top level key. Globals and nested `tags:` tables are not currently supported.

  - 顶级Chart中引用子Chart中的values

    [详细信息](https://helm.sh/docs/developing_charts/#importing-child-values-via-requirements-yaml)

- **charts/**

  我们可以手动的将所需依赖Chart复制到charts目录下面，但是文件或者目录不能以`_`或者`.`开头，这些文件会被忽略

  [详细信息](https://helm.sh/docs/developing_charts/#managing-dependencies-manually-via-the-charts-directory)

### templates/ AND values.yaml

- **templates**

  所有的模板文件存放在Chart的templates目录下面，当Helm安装或者更新的时候，会通过模版引擎将模板文件传递到Tiller服务器。

  模板文件使用[go语法](https://golang.org/pkg/text/template/)编写，可以支持[sprig库](http://masterminds.github.io/sprig/)的50多种函数以及一些[helm特定的函数](https://helm.sh/docs/developing_charts/#know-your-template-functions)。关于这边之后的文档中会详细介绍。

- **values**

  Helm中包含了一些对象，模板可以引用这些对象的值，其中Values对象允许自定义values来提供给模板使用，在Helm中有两种方式：

  - 在values.yaml文件中定义，Helm会自动读取该文件中的默认值。可以通过自定义yaml文件然后再命令行中引用该文件的方式来覆盖values.yaml中的默认值。
  - 在使用命令式通过命令行的方式指定

  关于values的使用会在之后的文档中详细介绍。

## SubChart

Chart依赖的其他Chart即为它的子Chart。在介绍子Chart之前我们要注意以下几点：

- 子Chart不能依赖父Chart，因此子Chart也不能获取到父Chart中的vlaues
- 父Chart可以覆盖子Chart中定义的values
- Helm在父Chart中提供一个全局vlaues以便子Chart可以获取

假如我们有一个Chart名为mychart，其有一个子Chart名为mysubchart，子Chart定义如下：

```yaml
# mychart/charts/mysubchart/values.yaml
dessert: cake
```

```yaml
# mychart/charts/mysubchart/templates/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-cfgmap2
data:
  dessert: {{ .Values.dessert }}
```

因为执行安装结果如下：

```console
# Source: mysubchart/templates/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: newbie-elk-cfgmap2
data:
  dessert: cake
```

### 覆盖子Chart中的Values

我们可以在mychart/values.yaml中这样定义：

```yaml
mysubchart:
  dessert: ice cream
```

重新安装后得到如下结果：

```yaml
# Source: mychart/charts/mysubchart/templates/configmap.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: unhinged-bee-cfgmap2
data:
  dessert: ice cream
```

我们发现，自模板中引用并没有发生变化，但是值的确被父values中的值覆盖了。这是因为当模板引擎传递values时，它同时会传递作用域，因此对于mysubchart下的模板来说，只有在mysubchart作用域下的values才能被使用。

### 全局values

Helm中提供了全局values来使自Chart通过完全相同的名称来引用values，全局values需要显式声明Values数据中有一个名为`Values.global`的保留值用来定义全局values。例如

```yaml
global:
  salad: caesar
```

在子Chart中的模板中这样使用：

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name }}-configmap
data:
  salad: {{ .Values.global.salad }}
```

## Hooks

Helm提供了一个钩子机制，允许Chart开发人员在release的某个生命周期来进行一些操作。一般用作以下几点：

- 安装时可以在加载任何Chart之前加载ConfigMap或者Secret资源
- 在安装新Chart之前来启动一个作业来备份数据，升级成功后启动第二个作业来回复数据
- 在删除release之前执行作业确保服务优雅的终端

[详细信息](https://helm.sh/docs/developing_charts/#hooks)



## 知识点

### 当配置改变时自动部署

如果我们更改了ConfigMap或Secret，此时使用`upgrade命令`的话，因为Deployment资源内容没有改变，所以仍然使用旧配置来部署，从而导致部署不一致。

`sha256sum`函数可以用于确保在另一个文件更改时其值发生变化，所以我们可以通过如下方式来更改Deployment资源从而解决这个问题：

```yaml
kind: Deployment
spec:
  template:
    metadata:
      annotations:
        checksum/config: {{ include (print $.Template.BasePath "/configmap.yaml") . | sha256sum }}
[...]
```

### 删除release时保留资源

有时候需要在使用`delete`命令时保留资源，可以通过如下方式做到这一点：

```yaml
kind: Secret
metadata:
  annotations:
    "helm.sh/resource-policy": keep
[...]
```

这时候使用`delete`命令将会保留资源，但这些资源会变成孤立状态并且不受Helm管理。这时候如果使用`helm install --replace`就可能会出现问题。

### 使用随机值时要特别小心

Helm中有一些函数允许生成随机数据、加密密钥等。这些函数给我们提供了很好的便利，但是要注意在使用upgrade时，模板会重新生成，会触发对资源的更新，这时候可能就会出现一些问题。

## Chart Repository

[详细信息](https://helm.sh/docs/developing_charts/#the-chart-repository-guide)












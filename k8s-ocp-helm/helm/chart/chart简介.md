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

在Helm中，一个Chart可以依赖多个其他的Chart，它们可以在requirements.yaml文件中动态的指定或者手动在charts/目录中导入其他的Chart目录。尽管手动管理的方式对于某些团队来说有一些有点，但是在requirements.yaml文件中声明依赖仍然是首选方式。

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

  当编写好requirements.yaml文件后，可以使用`helm dependency update`命令来将依赖Chart的压缩文件下载到chart/目录下面。对于上面的文件执行后chart/目录如下：

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

    上面的例子结果如下：subchart1和subchart2都会被启用。**从这个例子中我们可以得出结论，condition条件判断是优先于tags判断条件的。**


























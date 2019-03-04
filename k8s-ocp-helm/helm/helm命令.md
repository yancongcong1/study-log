[TOC]

# Helm命令



## 命令简介

Helm一共有如下命令：

- [helm completion](https://helm.sh/docs/helm/#helm-completion) - 为指定的shell生成自动补全脚本
- [helm create](https://helm.sh/docs/helm/#helm-create) - 创建一个Chart
- [helm delete](https://helm.sh/docs/helm/#helm-delete) - 删除Kubernetes集群中指定的release
- [helm dependency](https://helm.sh/docs/helm/#helm-dependency) - 管理charts的依赖
- [helm fetch](https://helm.sh/docs/helm/#helm-fetch) - 从仓库下载chart并解压
- [helm get](https://helm.sh/docs/helm/#helm-get) - 下载release
- [helm history](https://helm.sh/docs/helm/#helm-history) - 获取release历史
- [helm home](https://helm.sh/docs/helm/#helm-home) - 打印HELM-HOME位置
- [helm init](https://helm.sh/docs/helm/#helm-init) - 安装helm
- [helm inspect](https://helm.sh/docs/helm/#helm-inspect) - 列出Chart的详细信息
- [helm install](https://helm.sh/docs/helm/#helm-install) - 安装Chart并创建对应的release
- [helm lint](https://helm.sh/docs/helm/#helm-lint) - 校验Chart
- [helm list](https://helm.sh/docs/helm/#helm-list) - 打印release列表
- [helm package](https://helm.sh/docs/helm/#helm-package) - 将Chart目录进行打包
- [helm plugin](https://helm.sh/docs/helm/#helm-plugin) - 针对Helm插件的一些操作
- [helm repo](https://helm.sh/docs/helm/#helm-repo) - 针对Helm仓库的一些操作
- [helm reset](https://helm.sh/docs/helm/#helm-reset) - 卸载Tiller服务
- [helm rollback](https://helm.sh/docs/helm/#helm-rollback) - 将release回滚到之前的版本
- [helm search](https://helm.sh/docs/helm/#helm-search) - 从仓库中查找Charts
- [helm serve](https://helm.sh/docs/helm/#helm-serve) - 开启本地的http仓库服务
- [helm status](https://helm.sh/docs/helm/#helm-status) - 显示指定release的状态
- [helm template](https://helm.sh/docs/helm/#helm-template) - locally render templates
- [helm test](https://helm.sh/docs/helm/#helm-test) - 测试一个release
- [helm upgrade](https://helm.sh/docs/helm/#helm-upgrade) - 更新一个release
- [helm verify](https://helm.sh/docs/helm/#helm-verify) - 验证Chart签名是否有效
- [helm version](https://helm.sh/docs/helm/#helm-version) - 显示Helm的客户端和服务端信息



## 详细介绍

接下来我们针对一些常用的helm命令来进行详细的说明

### helm init

详细信息请参考helm简介的安装部分

### helm create

```
helm create NAME [flags]
```

该命令会根据指定名称创建一个chart目录以及chart使用的一些公共文件和目录。该目录结构可能如下：

```
foo/
  |
  |- .helmignore        # Contains patterns to ignore when packaging Helm charts.
  |
  |- Chart.yaml         # Information about your chart
  |
  |- values.yaml        # The default values for your templates
  |
  |- charts/            # Charts that this chart depends on
  |
  |- templates/         # The template files
  |
  |- templates/tests/   # The test files
```

可选参数：

| 选项                 | 含义                                                         |
| -------------------- | ------------------------------------------------------------ |
| -p, --starter string | 指定chart默认目录。如果不存在，在执行时自动创建；如果该目录中已经有文件，会覆盖冲突的文件，其余文件会保留。 |

[详细信息](https://helm.sh/docs/helm/#helm-create)

### helm delete

```
helm delete [flags] RELEASE-NAME [...]
```

根据release名称删除Kubernetes上和release关联的所有资源。

可选参数：

| 选项      | 含义                                                         |
| --------- | ------------------------------------------------------------ |
| --dry-run | 查看会删除哪些资源但是不会实际执行操作                       |
| --purge   | 将release从存储中删除，并释放其名称(name)，以便在以后重新使用 |

Helm会保存删除release的记录，如果名称未被释放，则不能使用该名称。如果必须使用该名称，可以使用--replace参数，但是这仅仅是重新使用该replace并且替换关联的资源。正是因为release的这种保存方式，我们可以回滚或者激活已经被删除的release。

> 这边我们碰到一个问题：删除release但并没有释放名称，之后删除其所在的命名空间，该Kubernetes弃用了RBAC，那么该如何彻底删除该release呢？
>
> 答案就是重新创建该namespace，配置Tiller的权限，然后释放release。当然你可以选择上面重新使用的方式。

[详细信息](https://helm.sh/docs/helm/#helm-delete)

### helm get

```
helm get [flags] RELEASE-NAME
```

该指令用来显示指定release的详细信息，可以通过额外指令获取到以下额外信息:

- 生成release的values
- 生成release的chart
- 生成的清单文件

[详细信息](https://helm.sh/docs/helm/#helm-get)

### helm history

```
helm history [flags] RELEASE-NAME
```

输出指定release的历史版本，默认最多输出256条版本信息，可以通过--max来设置。[详细信息](https://helm.sh/docs/helm/#helm-history)

### helm inspect

```
helm inspect [CHART] [flags]
```

该指令会会检查chart并输出相关信息。[详细信息](https://helm.sh/docs/helm/#helm-inspect)

### helm install

```
helm install [CHART] [flags]
```

安装chart。参数必须为chart引用、chart文件路径、chart压缩文件路径或者一个URL。具体如下：

- chart引用：helm install stable/mariadb
- chart包路径：helm install ./nginx-1.2.3.tgz
- chart文件路径：helm install ./nginx
- URL：https://example.com/charts/nginx-1.2.3.tgz
- chart引用和仓库url：helm install –repo https://example.com/charts/ nginx

可以通过附加--values参数并传入文件或者附加--set并从命令行传入配置的方式覆盖values.yaml的配置。

[详细信息](https://helm.sh/docs/helm/#helm-install)

### helm list

```
helm list [flags] [FILTER]
```

列举release，默认只列举成功部署的release。

可选参数：

| 选项      | 含义                                |
| --------- | ----------------------------------- |
| -a, --all | 列举所有的release而不只是部署成功的 |
| --deleted | 列举被删除但却未被释放的release     |
| --failed  | 列举部署失败的release               |

[详细信息](https://helm.sh/docs/helm/#helm-list)

### helm repo

这个指令包含多个子指令去操作chart仓库。可以执行add、remove、update、list和index子指令。

[详细信息](https://helm.sh/docs/helm/#helm-repo)

### helm rollback

```
helm rollback [flags] [RELEASE] [REVISION]
```

将release回滚到指定版本。第一个参数是release名称，第二个参数为版本号。可以通过history命令来查询release版本信息。

[详细信息](https://helm.sh/docs/helm/#helm-rollback)

### helm search

```
helm search [keyword] [flags]
```

根据关键字读取配置中的所有chart仓库并找到匹配项。

[详细信息](https://helm.sh/docs/helm/#helm-search)

### helm status

```
helm status [flags] RELEASE-NAME
```

显示指定release的状态(UNKNOWN、DEPLOYED、DELETED、SUPERSEDED、FAILED、DELETING)

[详细信息](https://helm.sh/docs/helm/#helm-status)

### helm upgrade

```
helm upgrade [RELEASE] [CHART] [flags]
```

该命令将chart升级到指定版本或者更新chart的values。

必须的参数为release名称和chart引用(许多引用中的一种，参照install命令)。

可选参数：

| 选项         | 含义                         |
| ------------ | ---------------------------- |
| --version    | 指定版本号，默认为最新的     |
| -f, --values | 指定yaml文件来覆盖values配置 |

[详细信息](https://helm.sh/docs/helm/#helm-upgrade)
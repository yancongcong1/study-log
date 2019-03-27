[TOC]

# Pub/Sub

redis中的`subscribe`、`unsubscribe`和`publish`实现了发送者发消息时不指定具体接收者的[发布/订阅消息规范](https://en.wikipedia.org/wiki/Publish/subscribe)。

该模式中发布者不知道目标是谁，只需要将消息发不到通道中。而接受者也不知道发送者的信息，只是订阅自己感兴趣的通道(可以订阅多个)，并且只会收到来自这些通道的消息。这个模式将发布者以及接收者解耦从而实现了更大的拓展性以及更动态的网络拓扑。

当客户端进入发布/订阅模式时，虽然仍然允许使用一些命令来进行订阅或者解除订阅([SUBSCRIBE](https://redis.io/commands/subscribe), [PSUBSCRIBE](https://redis.io/commands/psubscribe), [UNSUBSCRIBE](https://redis.io/commands/unsubscribe), [PUNSUBSCRIBE](https://redis.io/commands/punsubscribe), [PING](https://redis.io/commands/ping) and [QUIT](https://redis.io/commands/quit).)，但是原则上不应该在此期间使用该客户端发送命令。虽然官方文档表明在客户端进入订阅模式时我们仍然可以使用以上的命令，但是根据实际操作发现当进入订阅模式后客户端将无法键入任何命令(可能与客户端有关，我这边使用的是安装包中自带的客户端)。官方文档还指出可以使用`ctrl+c`来退出订阅模式，但是在自带客户端下使用该命令会直接退出客户端。

发布订阅相关的命令时返回信息以消息的形式返回，这是什么意思呢？就是返回信息是一个包含三个字段的数组，其中第一个字段表示本条消息的消息类型。

## 使用

那么Pub/Sub模式到底如何使用呢？发布者如何将消息发送到指定的channel中，接收者又如何从channel中接收消息呢？

首先我们可以看到无论是发送消息还是接收消息都需要通过channel，那么这个channel是个什么东西呢？这个暂时我也不太清楚，因为这边涉及到网络编程的一些内容，所以这边等深入研究网络编程之后再来解答。我们可以暂时理解为channel表示redis服务的一个socket，当我们使用`subscribe`订阅这个channel之后，服务端会保存一份socket和客户端地址的一份映射表(类似于路由表)，当使用`publish`向channel中发送消息时，会将消息推送给映射表中的所有客户端。[无论这个假设正不正确，这边只是用来辅助理解的，这边的内容会在学习过网络编程之后更新]。

同时我们并不需要定义channel，客户端只需要直接使用`subscribe`就可以直接订阅相关channel了。例如：

![https://github.com/yancongcong1/blog/tree/master/redis/static/images/subscribe.png](https://github.com/yancongcong1/blog/tree/master/redis/static/images/subscribe.png)

同样的如果我们需要向channel中发送消息，只需要使用`publish`命令就行了。例如：

![https://github.com/yancongcong1/blog/tree/master/redis/static/images/publish.png](https://github.com/yancongcong1/blog/tree/master/redis/static/images/publish.png)

## 数据库限制

我们知道redis默认提供0-15一共16个数据库，但redis在Pub/Sub模式下，没有key空间的限制。这句话的意思是无论我们在那个数据库下，即使客户端使用`subscribe`命令的数据库和另一个客户端使用`publish`命令的数据库不同，仍然可以无限制的收发消息。

如果你想要设置一个限制，官方的建议是在channel命名时添加前缀，例如test, staging, production。

## 模式匹配

redis的Pub/Sub模式提供两个命令`psubscribe`和`punsubscribe`用来进行模式匹配，在这两个命令中我们可以使用通配符来进行channel的订阅和解除订阅。

使用模式匹配需要注意的是，我们之前说过发布订阅相关的命令时返回信息以消息的形式返回，第一个字段代表消息类型，关于这个消息类型我们接下来会进行详细的解释。我们这边需要知道的是使用模式匹配和直接匹配在订阅模式下收到消息的类型不同：message和pmessage。

### 同时满足模式匹配和直接匹配

细心的朋友通过上面的介绍可能会发现，如果我使用直接匹配和模式匹配匹配到了同一个channel怎么办？

没错，的确会出现这种情况。并且如果出现了这种情况，我们会将该通道的消息接收两次，这是因为这两次消息的类型不同。例如：

```
SUBSCRIBE foo
PSUBSCRIBE f*
```

## 消息格式

我们简单描述一下不同命令收到消息的类型以及各个字段表示的含义：

- subscribe

  使用`subscribe`命令会返回3个字段。第一个字段为subscribe，表示消息类型为subscribe；第二个字段为成功订阅的channel名称；第三个字段代表客户端当前订阅的channel数目

- unsubscribe

  使用`unsubscribe`命令会返回3个字段。第一个字段为unsubscribe，表示消息类型为unsubscribe；第二个字段为成功解除订阅的channel名称；第三个字段表示客户端当前订阅的channel数目

- Sub模式下

  客户端在Sub模式下会根据匹配模式的不同返回不同的消息类型：

  - 直接匹配

    会返回3个字段。第一个字段为message，表示消息类型为message；第二个字段为收到消息的channel名称；第三个字段为消息实体

  - 模式匹配

    会返回4个字段。第一个字段为pmessage，表示消息类型为pmessage；第二个字段为匹配模式，也就是psubscribe命令后的参数；第三个参数为收到消息的channel名称；第四个参数为消息实体

## 实际用例

使用redis的Pub/Sub模式来涉及一个简单的[聊天室](https://gist.github.com/pietern/348262)
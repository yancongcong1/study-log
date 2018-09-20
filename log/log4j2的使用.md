[TOC]

# log4j 2使用

官网地址：https://logging.apache.org/log4j/2.x



## 引入jar包

```$xml
<!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.11.1</version>
</dependency>

<!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-api -->
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-api</artifactId>
    <version>2.11.1</version>
</dependency>
```



## 配置加载顺序

Log4j has the ability to automatically configure itself during initialization. When Log4j starts it will locate all the ConfigurationFactory plugins and arrange them in weighted order from highest to lowest. As delivered, Log4j contains four ConfigurationFactory implementations: one for JSON, one for YAML, one for properties, and one for XML.
* Log4j will inspect the **log4j.configurationFile** system property and, if set, will attempt to load the configuration using the ConfigurationFactory that matches the file extension.
* If no system property is set the properties ConfigurationFactory will look for `log4j2-test.properties` in the classpath.
* If no such file is found the YAML ConfigurationFactory will look for `log4j2-test.yaml` or `log4j2-test.yml` in the classpath.
* If no such file is found the JSON ConfigurationFactory will look for `log4j2-test.json` or `log4j2-test.jsn` in the classpath.
* If no such file is found the XML ConfigurationFactory will look for `log4j2-test.xml` in the classpath.
* If a test file cannot be located the properties ConfigurationFactory will look for `log4j2.properties` on the classpath.
* If a properties file cannot be located the YAML ConfigurationFactory will look for `log4j2.yaml` or `log4j2.yml` on the classpath.
* If a YAML file cannot be located the JSON ConfigurationFactory will look for `log4j2.json` or `log4j2.jsn` on the classpath.
* If a JSON file cannot be located the XML ConfigurationFactory will try to locate `log4j2.xml` on the classpath.
* If no configuration file could be located the DefaultConfiguration will be used. This will cause logging output to go to the console.

示例：

```
import com.foo.Bar;
 
// Import log4j classes.
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
 
public class MyApp {
 
    // Define a static logger variable so that it references the
    // Logger instance named "MyApp".
    private static final Logger logger = LogManager.getLogger(MyApp.class);
 
    public static void main(final String... args) {
 
        // Set up a simple configuration that logs on the console.
 
        logger.trace("Entering application.");
        Bar bar = new Bar();
        if (!bar.doIt()) {
            logger.error("Didn't do it.");
        }
        logger.trace("Exiting application.");
    }
}
```

如果log4j无法找到配置文件，它会提供一个默认的配置如下：

The default configuration, provided in the DefaultConfiguration class, will set up:

- A [ConsoleAppender](https://logging.apache.org/log4j/2.x/log4j-core/apidocs/org/apache/logging/log4j/core/appender/ConsoleAppender.html) attached to the root logger.
- A [PatternLayout](https://logging.apache.org/log4j/2.x/log4j-core/apidocs/org/apache/logging/log4j/core/layout/PatternLayout.html) set to the pattern "%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n" attached to the ConsoleAppender

Note that by default Log4j assigns the root logger to `Level.ERROR`.

## 配置方式

log4j2的配置形式多种多样，上面已经有所提及，具体可以参照官方文档：https://logging.apache.org/log4j/2.x/manual/configuration.html#ConfigurationSyntax

下面以property的形式提供一份参考配置：

```
status = error
dest = err
name = PropertiesConfig
appenders = console, file

property.filename = logs/bookstore.log

filter.threshold.type = ThresholdFilter
filter.threshold.level = debug

appender.console.name = console
appender.console.type = Console
appender.console.layout.type = PatternLayout
appender.console.layout.pattern = %d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n

appender.file.name = file_log
appender.file.type = File
appender.file.filename = ${filename}
appender.file.layout.type = PatternLayout
appender.file.layout.pattern = %d{yyyy-MM-dd HH:mm:ss} [%t] %-5level %logger{36} - %msg%n
appender.file.layout.footer = ------------ End Of Request ------------%n
appender.file.filter.threshold.type = ThresholdFilter
appender.file.filter.threshold.level = info

appender.rolling.type = RollingFile
appender.rolling.name = RollingFile
appender.rolling.fileName = ${filename}
appender.rolling.filePattern = target/rolling2/test1-%d{MM-dd-yy-HH-mm-ss}-%i.log.gz
appender.rolling.layout.type = PatternLayout
appender.rolling.layout.pattern = %d %p %C{1.} [%t] %m%n
appender.rolling.policies.type = Policies
appender.rolling.policies.time.type = TimeBasedTriggeringPolicy
appender.rolling.policies.time.interval = 2
appender.rolling.policies.time.modulate = true
appender.rolling.policies.size.type = SizeBasedTriggeringPolicy
appender.rolling.policies.size.size=100MB
appender.rolling.strategy.type = DefaultRolloverStrategy
appender.rolling.strategy.max = 5

logger.rolling.name = com.wizard.bookstore
logger.rolling.level = debug
logger.rolling.additivity = false
logger.rolling.appenderRef.rolling.ref = RollingFile

rootLogger.level = info
rootLogger.appenderRefs = stdout, file
rootLogger.appenderRef.stdout.ref = console
rootLogger.appenderRef.file.ref = file_log
```

不同的配置方式有不同的表现形式。下面简单介绍一下log4j2的一些重要的模块。

## Properties

用来定义常量，方便在其他配置中使用

## Appender

Appender是log4j2的输出源，定义日志输出的位置。一般都会给appender定义一个名称，方便logger日志引用输出配置。log4j2支持的输出源有很多种，包括Console(控制台)、File(文件)、RollingRandomAccessFile(文件升级版本)、MongoDB(数据库)、Flume 等 。

- Console：将日志输出到控制台，一般开发的时候使用
- File：将日志输出到文件，需要进行相关的配置
- RollingRandomAccessFile：将日志输出到文件，不同的是功能更加强大，可以决定在文件达到一定的大小之后重新开始一个文件，以及定义文件的命名规则。
- NoSql：输出到数据库中，例如MongoDB
- Flume：输出到Apache Flume(Flume是Cloudera提供的一个高可用的，高可靠的，分布式的海量日志采集、聚合和传输的系统，Flume支持在日志系统中定制各类数据发送方，用于收集数据；同时，Flume提供对数据进行简单处理，并写到各种数据接受方（可定制）的能力。) 

关于Appender的具体配置可以参考文档：https://logging.apache.org/log4j/2.x/manual/appenders.html

## Layout 

配置appender时，需要配置一个Layout节点，这个节点用来定义日志的输出格式。log4j2包含很多种类的Layout节点，例如ParrentLayout、HTMLLayout等。当然，log4j2包含一个默认的PatternLayout节点。

关于Layout的更多内容请参考：https://logging.apache.org/log4j/2.x/manual/layouts.html

## Logger

日志器分root日志器和自定义日志器，当根据日志名字获取不到指定的日志器时就使用Root作为默认的日志器，自定义时需要指定每个Logger的名称name（对于命名可以以包名作为日志的名字，不同的包配置不同的级别等），日志级别level，相加性additivity（是否继承下面配置的日志器）， 对于一般的日志器（如Console、File、RollingRandomAccessFile）一般需要配置一个或多个输出源AppenderRef；

每个logger可以指定一个level（TRACE, DEBUG, INFO, WARN, ERROR, ALL or OFF），不指定时level默认为ERROR。

additivity指定是否同时输出log到父类的appender，缺省为true。一般设置为false。




















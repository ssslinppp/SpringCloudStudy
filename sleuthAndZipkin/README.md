![图标](https://zipkin.io/public/img/zipkin-logo-200x119.jpg)        
# 概述
分布式追踪系统示例项目，使用`Zipkin` + `Spring cloud sleuth`作为分布式追踪系统的实现；

---

# zipKin介绍
## zipKin架构图     
[架构图链接](https://zipkin.io/pages/architecture.html)    
![zipKin架构图](https://zipkin.io/public/img/architecture-1.png)       

## 基本组件
zipkin主要由4个组件组成：
1. 收集数据(collector)： 对收集到的数据进行 验证、存储和索引；
2. 数据存储(storage): 支持`Cassandra`、`ElasticSearch `和`Mysql`等多种存储后端；
3. 提供查询API(search)： 提供 JSON API 查询接口；
4. UI界面展示等功能(web UI)： 使用Json API查询数据，并在界面上进行展示； 

## 数据传输(Transport)：
系统追踪数据(Spans)需要由服务端（如：ServerA/ServerB）传输到Zipkin的collectors，目前有3中基本的Transport：
1. HTTP; 
2. Kafka（消息队列）;
3. Scribe.

## Zipkin 工作流示例   
```
┌─────────────┐ ┌───────────────────────┐  ┌─────────────┐  ┌──────────────────┐
│ User Code   │ │ Trace Instrumentation │  │ Http Client │  │ Zipkin Collector │
└─────────────┘ └───────────────────────┘  └─────────────┘  └──────────────────┘
       │                 │                         │                 │
           ┌─────────┐
       │ ──┤GET /foo ├─▶ │ ────┐                   │                 │
           └─────────┘         │ record tags
       │                 │ ◀───┘                   │                 │
                           ────┐
       │                 │     │ add trace headers │                 │
                           ◀───┘
       │                 │ ────┐                   │                 │
                               │ record timestamp
       │                 │ ◀───┘                   │                 │
                             ┌─────────────────┐
       │                 │ ──┤GET /foo         ├─▶ │                 │
                             │X-B3-TraceId: aa │     ────┐
       │                 │   │X-B3-SpanId: 6b  │   │     │           │
                             └─────────────────┘         │ invoke
       │                 │                         │     │ request   │
                                                         │
       │                 │                         │     │           │
                                 ┌────────┐          ◀───┘
       │                 │ ◀─────┤200 OK  ├─────── │                 │
                           ────┐ └────────┘
       │                 │     │ record duration   │                 │
            ┌────────┐     ◀───┘
       │ ◀──┤200 OK  ├── │                         │                 │
            └────────┘       ┌────────────────────────────────┐
       │                 │ ──┤ asynchronously report span     ├────▶ │
                             │                                │
                             │{                               │
                             │  "traceId": "aa",              │
                             │  "id": "6b",                   │
                             │  "name": "get",                │
                             │  "timestamp": 1483945573944000,│
                             │  "duration": 386000,           │
                             │  "annotations": [              │
                             │--snip--                        │
                             └────────────────────────────────┘
```
ServiceA通过Get调用请求ServiceB时，Trace组件会自动添加相关追踪信息，然后通过`异步`的方式发送到Zipkin的Collector端；

## ZipKin-Server的搭建
[官方地址链接](https://zipkin.io/pages/quickstart.html)     
官方提供了3种不同的方式：
1. Docker方式：`docker run -d -p 9411:9411 openzipkin/zipkin`
2. Java方式（Java8）：直接运行zipkin.jar，`java -jar zipkin.jar`；
3. Running from Source： 直接从源代码中编译运行；

在我们的示例程序中，除了使用上述3种方式外，还可以通过`Spring boot`+`引入zipkin相关Jar包`来启动Zipkin-Server，该方式会在`basic`中进行介绍；     
`ZipKin-Server`服务启动后，在浏览器输入：`http://your_host:9411`就可以看到Zipkin的UI界面；   

---

# Spring-cloud-sleuth介绍
## 架构图   
![spring-cloud-sleuth](https://raw.githubusercontent.com/spring-cloud/spring-cloud-sleuth/master/docs/src/main/asciidoc/images/trace-id.png)    
[Github地址](https://github.com/spring-cloud/spring-cloud-sleuth)   

## 主要功能：`延时分析`    
通过记录相关时间信息，来帮助`延时分析`，通过Sleuth，可以查明系统延时的原因。   
1. 可以控制采样策略：通俗的讲就是，哪些请求需要追踪，哪些不需要，跟踪的频率可以控制（如10次请求调用，只追踪1次）；
2. 可以报告给Zipkin进行查询和可视化分析，默认时使用Http传输，默认端口为：9411；


## 跟踪的入口点和出口点
主要是Spring应用的入口和出口点（如）：
- servlet filter
- async endpoints
- rest template （服务间的调用）
- scheduled actions 
- message channels 
- zuul filters 
- feign client 

## 报告追踪数据（To Zipkin）
Sleuth收集跟踪信息（Trace），并通过Http或RabbitMq等方式传递给Zipkin-Server的Collector；   
包含`spring-cloud-sleuth-zipkin`依赖时，系统就会`收集`并`传输`与Zipkin兼容的跟踪信息到Zipkin-server；  
传输到Zipkin-Server的方式：
1. Http方式（默认）： 通过本地的`9411`端口进行传输，可以通过配置`spring.zipkin.baseUrl`来改变Zipkin-Server的地址； 
2. spring-rabbit 或 spring-kafka： 当包含相应依赖时，`trace`信息将会发送到broker中，而不是通过Http进行传输；

## 采样频率设置
主要有两种方式：
1. 配置文件中设置属性：`spring.sleuth.sampler.percentage=10%`；
2. 编程的方式，自定义Bean: `Sampler`，此种方式更加灵活，可以进行更精准的控制；

示例：
```
// 收集50%的请求
@Bean
Sampler customSampler() {
	return new Sampler() {
		@Override
		public boolean isSampled(Span span) {
			return Math.random() > .5 ;
		}
	};
}
```

## 日志记录
默认时，SLF4J的日志都会记录`appname, traceId, spanId, exportable`信息，默认值：
```
logging.pattern.level=%5p [${spring.zipkin.service.name:${spring.application.name:-}},%X{X-B3-TraceId:-},%X{X-B3-SpanId:-},%X{X-Span-Export:-}]
```
其他的日志方式，必须手动进行配置；

## 注意点
1. `spring-cloud-sleuth-stream`已废弃，请不要再使用；
2. 采样频率设置：`spring.sleuth.sampler.percentage=10%`来设置采样频率；

## 其他
允许在进程将传递信息（baggage）： 可以通过Http或者Messaging进行传递；   

---

# 相关链接：
- [ZipKin官网](https://zipkin.io/)   
- [spring-cloud-sleuth--Github](https://github.com/spring-cloud/spring-cloud-sleuth) 

---

# 项目分类
## basic目录
基本的示例程序，该程序仅仅演示如何使用`Zipkin` + `Spring cloud sleuth`完成基本的分布式追踪系统，不适合在生产环境上部署；


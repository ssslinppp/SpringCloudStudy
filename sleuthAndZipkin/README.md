![图标](https://zipkin.io/public/img/zipkin-logo-200x119.jpg)        
# 概述
分布式追踪系统示例项目，使用`Zipkin` + `Spring cloud sleuth`作为分布式追踪系统的实现；

## 架构图
- zipKin架构图     
[架构图链接](https://zipkin.io/pages/architecture.html)    
![zipKin架构图](https://zipkin.io/public/img/architecture-1.png)       

 
zipkin主要由4个组件组成：
1. 收集数据(collector)： 对收集到的数据进行 验证、存储和索引；
2. 数据存储(storage): 支持`Cassandra`、`ElasticSearch `和`Mysql`等多种存储后端；
3. 提供查询API(search)： 提供 JSON API 查询接口；
4. UI界面展示等功能(web UI)： 使用Json API查询数据，并在界面上进行展示； 

数据传输(Transport)：
系统追踪数据(Spans)需要由服务端传输到Zipkin的collectors，目前有3中基本的Transport：
1. HTTP; 
2. Kafka;
3. Scribe.


- 工作流示例：   
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


- Spring-cloud-sleuth架构图    
![spring-cloud-sleuth](https://raw.githubusercontent.com/spring-cloud/spring-cloud-sleuth/master/docs/src/main/asciidoc/images/trace-id.png)    
[Github地址](https://github.com/spring-cloud/spring-cloud-sleuth)   


---

## 相关链接：
- [ZipKin官网](https://zipkin.io/)   
- [spring-cloud-sleuth--Github](https://github.com/spring-cloud/spring-cloud-sleuth) 

---

# 分类
## basic目录
基本的示例程序，该程序仅仅演示如何使用`Zipkin` + `Spring cloud sleuth`完成基本的分布式追踪系统，不适合在生产环境上部署；


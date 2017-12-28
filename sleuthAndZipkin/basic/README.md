# 概述
[前章回顾](https://github.com/ssslinppp/SpringCloudStudy/tree/master/sleuthAndZipkin)   

包括3个子模块，分别为：
1. Zipkin-Server: Zipkin服务器，[其他方式](https://zipkin.io/pages/quickstart.html)；   
2. sleuth-serverA: 与sleuth-serverB通过http互相调用；
3. sleuth-serverB: 与sleuth-serverA通过http互相调用；

---

# Zipkin-Server
依赖：
```
spring-cloud.version>Edgware.RELEASE</spring-cloud.version>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
    
<dependency>
  <groupId>io.zipkin.java</groupId>
  <artifactId>zipkin-autoconfigure-ui</artifactId>
</dependency>

<dependency>
    <groupId>io.zipkin.java</groupId>
    <artifactId>zipkin-server</artifactId>
</dependency>
```
添加注解：
```
@EnableZipkinServer
```

设置项目名称和端口号：application.properties
```
spring.application.name=zipkin-server
server.port=9411
```

验证：
```
http://localhost:9411
```

---

# sleuth-serverA
[参考链接](https://github.com/spring-cloud/spring-cloud-sleuth)     

依赖：
```
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- Sleuth with Zipkin via HTTP -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

说明：    
如果仅仅需要Sleuth，而不需要Zipkin，则只需要使用如下依赖，不必引入`spring-cloud-starter-zipkin`; 
```
<!-- Only Sleuth (log correlation) -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

配置服务名、端口号、Zipkin-server地址：application.properties
```properties
spring.application.name=sleuth-server-a
server.port=18001
spring.zipkin.base-url=http://localhost:9411
```

## 相关注解
- @NewSpan: 创建新的Span
- @SpanTag：添加自定义的tag，方便在搜索时，快速定位；

示例：
```
@NewSpan(name = "customNameOnTestMethod5")
@RequestMapping("/test5")
public String testMethod5(@SpanTag("test5Tag") String param) {
    logger.info("testMethod5() is called...");
    return "retValue-testMethod5()";
}
```

使用`http://localhost:18001/test5?param=test`调用时，对应的部分Json如下，tag被包装在`binaryAnnotations` 中：
```
// 如下是该方法对应的Span
{
  "traceId": "dcd01ed20e1e2060",
  "id": "a78408a707d88934",
  "name": "custom-name-on-test-method5",  // @NewSpan(name = "customNameOnTestMethod5")
  "parentId": "dcd01ed20e1e2060",
  "timestamp": 1514445627851000,
  "duration": 3355,
  "binaryAnnotations": [
    {
      "key": "class",
      "value": "ServerAController",
      "endpoint": {
        "serviceName": "sleuth-server-a",
        "ipv4": "172.20.21.176",
        "port": 18001
      }
    },
    {
      "key": "method",
      "value": "testMethod5",
      "endpoint": {
        "serviceName": "sleuth-server-a",
        "ipv4": "172.20.21.176",
        "port": 18001
      }
    },
    {
      "key": "test5Tag",   // 这是使用 @SpanTag 标注的tag
      "value": "test",     // http://localhost:18001/test5?param=test 时显示的参数值
      "endpoint": {
        "serviceName": "sleuth-server-a",
        "ipv4": "172.20.21.176",
        "port": 18001
      }
  }
```

---

# sleuth-serverB
与sleuth-serverA基本相同；
[参考链接](https://github.com/spring-cloud/spring-cloud-sleuth)     

依赖：
```
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- Sleuth with Zipkin via HTTP -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

说明：    
如果仅仅需要Sleuth，而不需要Zipkin，则只需要使用如下依赖，不必引入`spring-cloud-starter-zipkin`; 
```
<!-- Only Sleuth (log correlation) -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
```

配置服务名、端口号、Zipkin-server地址：application.properties
```properties
spring.application.name=sleuth-server-b
server.port=18002
spring.zipkin.base-url=http://localhost:9411
```

# 测试
通过`sleuth-serverA`与`sleuth-serverB`之间的Http调用，可以看到日志输出包含 traceId、spanId等信息；
```
                                                traceId                 spanId  
2017-12-27 14:17:04.046  INFO [sleuth-server-b,2c3e2a40249dd5ee,9a706b868168abfc,false] 8308 --- [io-18002-exec-1] c.s.controller.ServerBController         : serviceB is being called
2017-12-27 14:17:11.773  INFO [sleuth-server-b,1031a65d0ca680ae,67bee7dc65168c54,false] 8308 --- [io-18002-exec-3] c.s.controller.ServerBController         : serviceB is being called
2017-12-27 14:17:12.366  INFO [sleuth-server-b,51ca80860db437c3,0223064eda93867c,false] 8308 --- [io-18002-exec-4] c.s.controller.ServerBController         : serviceB is being called
2017-12-27 14:17:13.087  INFO [sleuth-server-b,3ce361261bda1320,9da183b7840762d1,false] 8308 --- [io-18002-exec-5] c.s.controller.ServerBController         : serviceB is being called
```

登录到ZipKin-server服务端，可以看到调用关系图： `http://localhost:9411/zipkin/dependency/`   
## 遇到的问题     
### Q1: 系统调用的跟踪信息丢失（其实并没有丢失）
不知是什么缘故，发现Zipkin的经常会看不到调用信息（即服务间调用的跟踪可能会丢失），    
如果没有看到调用依赖关系，则多尝试调用几次，会看到有调用关系出现（实际测试，发现服务间调用了多次，才会在Zipkin UI上看到1次或多次调用，即：有丢失）；
官方解释：
```
// 默认的采用频率为 10%， 即10次请求才跟踪一次
If using Zipkin, configure the percentage of spans exported using spring.sleuth.sampler.percentage (default 0.1, i.e. 10%).    
Otherwise you might think that Sleuth is not working cause it’s omitting some spans.
```
解决方式1： 配置策略，每一个请求都采样 
```
// 添加如下 @Bean声明，表示每次的服务调用都会跟踪，不会丢失信息（生产环境中，不建议这么做，可能会导致数据量巨大：个人理解）
@Bean
public AlwaysSampler defaultSampler() {
    return new AlwaysSampler();
}
```
解决方式2： 设置采样频率
通过配置：`spring.sleuth.sampler.percentage=50%`来配置采样频率；




[基础回顾](https://github.com/ssslinppp/SpringCloudStudy/tree/master/sleuthAndZipkin)   

# 概述
Sleuth with Zipkin via RabbitMQ or Kafka
Sleuth传输数据到Zipkin-Server，使用rabbitmq而不是默认的Http；     

## 注意
`spring-cloud-sleuth-stream`: 已经废弃；

---

# 依赖
### 微服务需要的依赖
当包含`spring-rabbit` or `spring-kafka`依赖时，应用程序将不再使用http传输traces，而是直接发送traces到消息队列的broker.

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
    
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit</artifactId>
</dependency>
```

### Zipkin-Server需要的依赖



 
 
 


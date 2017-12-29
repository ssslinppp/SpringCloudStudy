package com.ssslinppp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.ContinueSpan;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Desc:
 * <p>
 * User: liulin ,Date: 2017/12/27 , Time: 11:32 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@RestController
public class ServerAController {
    private static final Logger logger = LoggerFactory.getLogger(ServerAController.class);

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 使用{@code @NewSpan} 声明，可以自定义Span
     */
    @NewSpan("customNameOnTestMethod4")
    @RequestMapping("/test4")
    public String testMethod4() {
        logger.info("This is testMethod4() with span name: customNameOnTestMethod4 ");
        return "test4";
    }

    /**
     * 普通方法调用声明 @NewSpan、@SpanTag 的方法
     * 结果：被调用的test5方法，其Span和tag没有被记录；
     *
     * @return
     */
    @RequestMapping("/test6")
    public String testMethod6() {
        logger.info("testMethod6(without annotation) will call testMethod5(with @spantag)");
        return testMethod5("test6()");
    }

    /**
     * 测试相关声明的使用
     *
     * @param param
     * @return
     */
    @NewSpan(name = "customNameOnTestMethod5")
    @RequestMapping("/test5")
    public String testMethod5(@SpanTag("test5Tag") String param) {
        logger.info("testMethod5() is called...");
        return "retValue-testMethod5()";
    }

    /**
     * 使用ContinueSpan来添加tag
     *
     * @param param
     */
    @ContinueSpan(log = "testMethod11")
    @RequestMapping("/test11")
    public String testMethod11(@SpanTag("testTag11") String param) {
        logger.info("testMethod11() is called...");
        return "retValue-testMethod11()";
    }

    @RequestMapping("/serviceA")
    public String home() {
        logger.info("serviceA is being called");
        return "This is serviceA";
    }

    @RequestMapping("/serviceACall")
    public String service() {
        logger.info("serviceACall is being called");
        return restTemplate.getForObject("http://localhost:18002/serviceB", String.class);
    }

    /**
     * 测试服务调用不通时的情况
     *
     * @return
     */
    @RequestMapping("/callError")
    public String serviceError() {
        logger.info("callError is being called");

        //没有此服务，或此服务不通时的测试
        return restTemplate.getForObject("http://localhost:18002/serviceBError", String.class);
    }

    /**
     * 测试超时
     *
     * @return
     */
    @RequestMapping("/timeout")
    public String timeout() {
        logger.info("timeout is being called");

        //没有此服务，或此服务不通时的测试
        return restTemplate.getForObject("http://localhost:18002/timeoutB", String.class);
    }
}

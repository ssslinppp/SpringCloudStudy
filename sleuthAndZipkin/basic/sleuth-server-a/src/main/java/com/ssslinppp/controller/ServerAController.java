package com.ssslinppp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
}

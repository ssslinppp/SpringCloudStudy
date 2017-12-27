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
public class ServerBController {
    private static final Logger logger = LoggerFactory.getLogger(ServerBController.class);

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("serviceBCall")
    public String home() {
        logger.info("serviceBCall is being called");
        return restTemplate.getForObject("http://localhost:18001/serviceA", String.class);
    }

    @RequestMapping("/serviceB")
    public String service() {
        logger.info("serviceB is being called");
        return "This is serviceB";
    }
}

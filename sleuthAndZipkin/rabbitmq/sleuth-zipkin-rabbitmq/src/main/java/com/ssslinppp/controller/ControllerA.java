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
 * User: liulin ,Date: 2017/12/28 , Time: 18:02 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@RestController
public class ControllerA {

    private static final Logger logger = LoggerFactory.getLogger(ControllerA.class);

    @Autowired
    private RestTemplate restTemplate;

    private String url = "http://localhost:18102";

    @RequestMapping("/serviceA1")
    public String serviceA1() {
        logger.info("serviceA1 run... ");
        String result = this.restTemplate.getForObject(url + "/serviceB1", String.class);
        logger.info("result of serviceB1: " + result);
        return result;
    }

}

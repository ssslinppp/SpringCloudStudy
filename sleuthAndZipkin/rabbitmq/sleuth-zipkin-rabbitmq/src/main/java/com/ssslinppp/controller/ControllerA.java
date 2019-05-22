package com.ssslinppp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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

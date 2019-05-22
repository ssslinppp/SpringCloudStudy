package com.ssslinppp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

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

    @RequestMapping("/timeoutB")
    public String timeoutB() throws InterruptedException {
        logger.info("timeoutB is being called");
        TimeUnit.SECONDS.sleep(20);
        logger.info("timeoutB is call finished");
        return "This is serviceB";
    }
}

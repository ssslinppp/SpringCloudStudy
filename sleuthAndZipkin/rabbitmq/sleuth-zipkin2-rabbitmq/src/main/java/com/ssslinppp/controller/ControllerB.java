package com.ssslinppp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControllerB {

    private static final Logger logger = LoggerFactory.getLogger(ControllerB.class);

    @RequestMapping("/serviceB1")
    public String serviceB1() {
        logger.info("serviceB1 run... ");
        return "ret-serviceB1";
    }
}

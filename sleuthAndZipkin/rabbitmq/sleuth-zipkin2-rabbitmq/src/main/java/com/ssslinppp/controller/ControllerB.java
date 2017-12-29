package com.ssslinppp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Desc:
 * <p>
 * User: liulin ,Date: 2017/12/28 , Time: 18:02 <br/>
 * Email: liulin@cmss.chinamobile.com <br/>
 * To change this template use File | Settings | File Templates.
 */
@RestController
public class ControllerB {

    private static final Logger logger = LoggerFactory.getLogger(ControllerB.class);

    @RequestMapping("/serviceB1")
    public String serviceB1() {
        logger.info("serviceB1 run... ");
        return "ret-serviceB1";
    }
}

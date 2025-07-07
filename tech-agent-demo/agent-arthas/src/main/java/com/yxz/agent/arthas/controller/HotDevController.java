package com.yxz.agent.arthas.controller;

import com.yxz.agent.arthas.service.HelloService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Date 2025-04-01
 * @Created by Yolo
 */
@RestController
public class HotDevController {

    @RequestMapping("/hotdev")
    public void hotdev() {
        new HelloService().hello();
    }

}

package com.yxz.agent.arthas.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description TODO
 * @Date 2025-04-01
 * @Created by Yolo
 */
@RestController
public class ArthasController {

    /**
     * # 1、启动arthas
     * java -jar arthas-boot.jar
     *
     * # 2、sc: search class 查找文件
     * sc *ArthasController
     *
     * # 3、jad 反编译class 并输出到文件
     * jad --source-only com.yxz.agent.arthas.controller.ArthasController > /tmp/agent/ArthasController.java
     *
     * # 4、修改源代码
     * vi /tmp/agent/ArthasController.java
     *
     * # 5、sc查找加载 ArthasController 的 ClassLoader，-d参数可以打印出类加载的具体信息
     * sc -d *ArthasController | grep classLoaderHash
     *
     * # 6、编译源代码，使用mc（Memory Compiler）命令编译，并且通过-c参数指定 ClassLoader
     * mc -c 512ddf17 /tmp/agent/ArthasController.java -d /tmp/agent
     *
     * # 7、使用redefine命令重新加载新编译好的class
     * redefine /tmp/agent/com/mk/agent/arthas/controller/ArthasController.class
     *
     * # redefine成功之后，访问controller进行调用，观察代码是否生效
     */
    @RequestMapping("/arthas")
    public void demo() {
        System.out.println("hello arthas");
    }

}

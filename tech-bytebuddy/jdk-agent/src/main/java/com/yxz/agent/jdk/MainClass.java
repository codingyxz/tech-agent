package com.yxz.agent.jdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description TODO
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class MainClass {

    /**
     * -javaagent:/Users/linkk/codes/freestyle/tech-agent/tech-bytebuddy/jdk-agent/target/jdk-agent-1.0-SNAPSHOT-jar-with-dependencies.jar=k1=v1,k2=v2
     */
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int count = 0; count < 4; ++count) {
            executorService.submit(() -> {
                for (int i = 100; i > 0; --i) {
                    System.out.println(Thread.currentThread().getName() + ", HelloService.hello():" + HelloService.hello("Yolo"));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }
}

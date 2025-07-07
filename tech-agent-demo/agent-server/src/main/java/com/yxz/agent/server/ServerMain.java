package com.yxz.agent.server;

/**
 * @Description TODO
 * @Date 2025-03-31
 * @Created by Yolo
 */
public class ServerMain {


    /**
     * 一个线程每个500毫秒就调用一次TransClass的test方法
     * vm参数：（-javaagent:/Users/linkk/codes/freestyle/tech-road/tech-agent-demo/agent-base/target/agent-base-1.0-SNAPSHOT.jar）
     *        （-javaagent:/Users/linkk/codes/freestyle/tech-road/tech-agent-demo/agent-base/target/agent-base-1.0-SNAPSHOT-jar-with-dependencies.jar)
     * @param args
     */
    public static void main(String[] args) {

        new Thread(() -> {
            while (true) {
                new TransClass().test();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}

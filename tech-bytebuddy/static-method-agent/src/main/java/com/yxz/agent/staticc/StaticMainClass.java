package com.yxz.agent.staticc;

/**
 * @Desc TODO
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class StaticMainClass {


    /**
     * -javaagent:/Users/linkk/codes/freestyle/tech-agent/tech-bytebuddy/static-method-agent/target/static-method-agent-1.0-SNAPSHOT-jar-with-dependencies.jar=k1=v1,k2=v2
     */
    public static void main(String[] args) {
        System.out.println("执行 main");
        System.out.println(StaticUtils.hi("Ashiamd"));
        System.out.println(StaticUtils.hi("IABTD"));
    }

}

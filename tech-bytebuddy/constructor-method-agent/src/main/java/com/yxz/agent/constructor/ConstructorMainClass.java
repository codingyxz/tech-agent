package com.yxz.agent.constructor;

/**
 * @Desc 主程序入口
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class ConstructorMainClass {

    /**
     * -javaagent:/Users/linkk/codes/freestyle/tech-agent/tech-bytebuddy/constructor-method-agent/target/constructor-method-agent-1.0-SNAPSHOT-jar-with-dependencies.jar=k1=v1,k2=v2
     */
    public static void main(String[] args) {
        ConstructorTest test1 = new ConstructorTest("hello world111");
        ConstructorTest test2 = new ConstructorTest("hello world222");
    }

}

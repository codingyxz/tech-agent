package com.yxz.agent.instance;

/**
 * @Desc 主程序类
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class InstanceMainClass {

    /**
     * -javaagent:/Users/linkk/codes/freestyle/tech-agent/tech-bytebuddy/instance-method-agent/target/instance-method-agent-1.0-SNAPSHOT-jar-with-dependencies.jar=k1=v1,k2=v2
     */

    public static void main(String[] args) {
        System.out.println("执行 main");
        TestService01 testService01 = new TestService01();
        System.out.println("main, testService01.returnSeven(1, 2) = " + testService01.returnSeven(1, 2));
        System.out.println("main, testService01.returnZero(1, 2) = " + testService01.returnZero(1, 2));

        TestService02 testService02 = new TestService02();
        System.out.println("main, testService02.returnHello(\"hi\", \"hello\") = " + testService02.returnHello("hi", "hello"));
        System.out.println("main, testService02.returnHi(\"hahaha\") = " + testService02.returnHi("hahaha"));
        System.out.println("main, testService02.returnArgs(\"arg1\", \"arg2\") = " + testService02.returnArgs("arg1", "arg2"));
    }

}

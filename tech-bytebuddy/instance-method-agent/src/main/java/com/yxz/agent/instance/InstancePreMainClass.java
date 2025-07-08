package com.yxz.agent.instance;

import net.bytebuddy.agent.builder.AgentBuilder;

import java.lang.instrument.Instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * @Desc agent入口
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class InstancePreMainClass {


    /**
     * Manifest-Version: 1.0
     * Created-By: Maven Archiver 3.6.0
     * Build-Jdk-Spec: 11
     * Class-Path: ../lib/byte-buddy-1.14.10.jar
     * Main-Class: com.yxz.agent.instance.InstanceMainClass
     * Can-Redefine-Classes: true
     * Can-Retransform-Classes: true
     * Can-Set-Native-Method-Prefix: true
     * Premain-Class: com.yxz.agent.instance.InstancePreMainClass
     */

    /**
     * java agent 入口, premain在main方法之前执行
     */
    public static void premain(String arg, Instrumentation instrumentation) {
        System.out.println("执行 premain");
        // 使用 Byte Buddy 包装的 agent常见处理逻辑(指定要拦截的对象, 以及拦截后的处理逻辑, 任何字节码操作工具都基本这个流程)
        AgentBuilder agentBuilder = new AgentBuilder.Default()
                // 忽略(不拦截)的类, 这里忽略 java自带类和byte buddy的类
                .ignore(nameStartsWith("java.")
                        .or(nameStartsWith("javax."))
                        .or(nameStartsWith("jdk."))
                        .or(nameStartsWith("sun."))
                        // 忽略byte buddy的类
                        .or(nameStartsWith("net.bytebuddy.")))
                // 拦截的类
                .type(isAnnotatedWith(nameStartsWith("com.yxz.agent.instance.Yo").and(nameEndsWith("Log"))))
                // 拦截的方法, 以及指定修改/增强的逻辑
                .transform(new InstanceTransformer())
                // 注册 回调方法监听器
                .with(new InstanceListener());
        // 注册到 Instrumentation
        agentBuilder.installOn(instrumentation);
    }

}

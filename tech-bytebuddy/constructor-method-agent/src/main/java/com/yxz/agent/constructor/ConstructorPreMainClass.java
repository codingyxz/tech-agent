package com.yxz.agent.constructor;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * @Desc agent入口
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class ConstructorPreMainClass {

    public static void premain(String arg, java.lang.instrument.Instrumentation instrumentation) {
        System.out.println("=============================premain方法执行=======================");
        System.out.println("premain, arg = " + arg);
        new AgentBuilder.Default()
                .type(ElementMatchers.named("com.yxz.agent.constructor.ConstructorTest"))
                .transform(new ConstructorTransformer())
                .installOn(instrumentation);
    }

}

package com.yxz.agent.jdk;

import java.lang.instrument.Instrumentation;

/**
 * 一般premain都是在另一个单独工程/module内编写然后打包, 这里图方便直接和需要插桩增强的{@link MainClass}放在同一个module内打包
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class PreMainClass {


    /**
     * java agent启动的方式之一(另一个是agentmain), premain方法在main方法之前先执行, 是插桩入口
     *
     * @param arg             javaagent指定的参数
     * @param instrumentation jdk自带的工具类
     */
    public static void premain(String arg, Instrumentation instrumentation) {
        System.out.println("=============================premain方法执行=======================");

        System.out.println("premain, arg = " + arg);

        // 注册我们编写的 字节码转化器
        instrumentation.addTransformer(new ByteBuddyTransformer());
    }

}

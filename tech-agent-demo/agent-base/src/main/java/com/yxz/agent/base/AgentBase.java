package com.yxz.agent.base;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * 文件MANIFEST.MF
 * Manifest-Version: 1.0
 * Archiver-Version: Plexus Archiver
 * Created-By: Apache Maven
 * Built-By: linkk
 * Build-Jdk: 11.0.19
 * Agent-Class: com.yxz.agent.base.AgentBase
 * Can-Redefine-classes: true
 * Can-Retransform-classes: true
 * Premain-Class: com.yxz.agent.base.AgentBase
 * Class-Path: asm-commons-8.0.1.jar asm-8.0.1.jar asm-tree-8.0.1.jar asm-analysis-8.0.1.jar asm-util-8.0.1.jar javassist-3.25.0-GA.jar byte-buddy-1.17.5.jar
 *
 * @Date 2025-03-31
 * @Created by Yolo
 */
public class AgentBase {

    /**
     * 启动时执行入口
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("=============================premain方法执行=======================");
        inst.addTransformer(new MyClassFileTransformer());
    }


    /**
     * 运行时执行入口
     *
     * @param agentArgs
     * @param inst
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("=============================agentmain方法执行=======================");
        for (Class loadedClass : inst.getAllLoadedClasses()) {
            System.out.println("classLoader：" + loadedClass.getClassLoader() + " --- className：" + loadedClass.getName());
        }
        inst.addTransformer(new MyClassFileTransformer(), true);

        /**
         * transformer是会对尚未加载的类进行增加代理层，这里是已经运行中的jvm，所以类已经被加载了，
         * 必须主动调用 retransformClasses 让jvm再对运行中的类进行加上代理层
         */
        for (Class loadedClass : inst.getAllLoadedClasses()) {
            if (loadedClass.getName().contains("com.yxz.agent.server.TransClass")) {
                System.out.println(loadedClass.getName());
                try {
                    inst.retransformClasses(loadedClass);
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}

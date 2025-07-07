package com.yxz.agent.base;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * @Description TODO
 * @Date 2025-03-31
 * @Created by Yolo
 */
public class MyClassFileTransformer implements ClassFileTransformer {

    /**
     * 待增强的类
     */
    private static final String TRANS_CLASS = "com/yxz/agent/server/TransClass";

    /**
     * @param loader              - 定义要转换的类加载器；如果是引导类加载器，则为null
     * @param className           - 完全限定类内部形式的类名称和 The Java Virtual Machine Specification中定义的接口名称。例如"java/util/List"
     * @param classBeingRedefined - 如果是被重定义或重转换触发，则为重定义或重转换的类；如果是类加载，则为null
     * @param protectionDomain    - 要定义或重定义的类的保护域
     * @param classfileBuffer     - 类文件格式的输入字节缓冲区（不得修改）
     * @return - 一个格式良好的类文件缓冲区（转换的结果），如果未执行转换，则返回null
     * @throws IllegalClassFormatException
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        System.out.println("loader：" + loader + " --- className：" + className);

        // 判断指定的class
        if (TRANS_CLASS.equals(className)) {
            try {
                /**
                 * 获取更改后的类class字节数组
                 */
                // 方式一： 读取已经准备好的 class 文件
//                classfileBuffer = Files.readAllBytes(Paths.get("/Users/heronsbill/code/agent-share/agent-demo/class/TransClass.class"));

                // 方式二： 使用 asm 技术动态修改 class 文件，实现增强
                classfileBuffer = byASM(classfileBuffer);
                // 方式三： 使用 javassist 技术动态修改 class 文件，实现增强
//                classfileBuffer = byJavassist(className.replaceAll("/", "."));
                // 方式四： 使用 byteBuddy 技术动态修改 class 文件，实现增强

            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return classfileBuffer;
    }


    /**
     * 使用 ASM 修改字节码文件
     *
     * @param classfileBuffer
     * @return
     */
    private byte[] byASM(byte[] classfileBuffer) {
        /**
         * ClassReader: 用于读取已经编译好的 .class 文件。
         * ClassWriter: 用于重新构建编译后的类，如修改类名、属性以及方法，也可以生成新的类的字节码文件。
         */
        ClassReader classReader = new ClassReader(classfileBuffer);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        /**
         * 各种Visitor类：CoreAPI根据字节码从上到下依次处理，对于字节码文件中不同的区域有不同的Visitor，
         * 比如用于访问方法的MethodVisitor、用于访问类变量的FieldVisitor、用于访问注解的AnnotationVisitor等。
         * 为了实现AOP，重点要使用的是MethodVisitor。
         */
        ClassVisitor classVisitor = new MyClassVisitor(Opcodes.ASM7, classWriter);
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
        return classWriter.toByteArray();
    }

    /**
     * 使用 Javassist 修改字节码文件
     *
     * @param fullClassName
     * @return
     */
    private byte[] byJavassist(String fullClassName) throws Exception {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(fullClassName);
        CtMethod m = cc.getDeclaredMethod("test");
        m.insertBefore("{ System.out.println(\"start javassist\"); }");
        m.insertAfter("{ System.out.println(\"end javassist\"); }");
        return cc.toBytecode();
    }

    /**
     * 使用 ByteBuddy 修改字节码文件
     *
     * @param classfileBuffer
     * @return
     */
    private byte[] byByteBuddy(byte[] classfileBuffer) {

        return null;
    }

}

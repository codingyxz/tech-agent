package com.yxz.agent.hotdev;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.ProtectionDomain;

import static com.yxz.agent.hotdev.VarContainer.CLASS_NAME_PREFIX;
import static com.yxz.agent.hotdev.VarContainer.PATH_PREFIX;


/**
 * @Desc TODO
 * @Author zhxy
 * @Version V1.0
 **/
public class HotClassFileTransformer implements ClassFileTransformer {

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
//        System.out.println(loader + "--" + className);

        // 存储目标jvm中的类文件路径与类名的映射
        keepMap(className);
        try {
            if (className.startsWith(CLASS_NAME_PREFIX)) {
                System.out.format("\33[32;1m hotdev : Reloading class %s%n", className);
                // 获取更改后的类class字节数组
                classfileBuffer = Files.readAllBytes(Paths.get(VarContainer.nameToPath.get(className)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classfileBuffer;
    }

    private void keepMap(String className) {
        if (VarContainer.nameToPath.get(className) != null || !className.startsWith(CLASS_NAME_PREFIX)) {
            return;
        }
        try {
            String path = String.format("%s/%s.%s", PATH_PREFIX, className, "class");
            VarContainer.pathToName.put(path, className);
            VarContainer.nameToPath.put(className, path);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }
}

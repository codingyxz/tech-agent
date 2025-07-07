package com.yxz.agent.hotdev;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import java.lang.instrument.Instrumentation;

import static com.yxz.agent.hotdev.VarContainer.CLASS_NAME_PREFIX;
import static com.yxz.agent.hotdev.VarContainer.PATH_PREFIX;

/**
 * @Desc TODO
 * @Author zhxy
 * @Version V1.0
 **/
public class HotAgent {
    // JVMTI 所创建的插桩对象
    private static Instrumentation instrumentation;

    /**
     * 启动时执行入口
     *
     * @param agentArgs
     * @param inst
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("=============================premain方法执行=======================");
        instrumentation = inst;
        inst.addTransformer(new HotClassFileTransformer(), true);

        // 注册文件监听器
        try {
            String path = String.format("%s/%s", PATH_PREFIX, CLASS_NAME_PREFIX);
            System.out.println("监控路径：" + path);

            FileAlterationMonitor monitor = new FileAlterationMonitor(2000);
            FileAlterationObserver observer = new FileAlterationObserver(path);
            observer.addListener(new FileListener());

            monitor.addObserver(observer);
            monitor.start();
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

    }

    public static void reloadByFullName(String fullName) {
        for (Class loadedClass : instrumentation.getAllLoadedClasses()) {
            if (loadedClass.getName().contains(fullName.replaceAll("/", "."))) {
                try {
                    instrumentation.retransformClasses(loadedClass);
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

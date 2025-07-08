package com.yxz.byteb.interceptor;


import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @Description 对静态方法增强
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class DelegateInterceptor19 {

    @RuntimeType
    public void staticMethodEnhance(
            // 静态方法对应的类class对象
            @Origin Class<?> clazz,
            // 静态方法不可访问 @This Object targetObj,
            @Origin Method targetMethod,
            @AllArguments Object[] targetMethodArgs,
            // 静态方法不可访问 @Super Object targetSuperObj,
            @SuperCall Callable<?> zuper) {
        // 原方法逻辑 System.out.println("what to Say, say: " + whatToSay);
        System.out.println("clazz = " + clazz);
        System.out.println("targetMethod.getName() = " + targetMethod.getName());
        System.out.println("Arrays.toString(targetMethodArgs) = " + Arrays.toString(targetMethodArgs));
        try {
            System.out.println("before sayWhat");
            // 调用目标方法
            zuper.call();
            System.out.println("after sayWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

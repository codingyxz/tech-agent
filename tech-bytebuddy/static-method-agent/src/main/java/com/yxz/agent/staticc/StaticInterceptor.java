package com.yxz.agent.staticc;


import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @Desc 静态方法拦截器
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class StaticInterceptor {

    @RuntimeType
    public Object staticMethodInterceptor(
            @Origin Class<?> clazz,
            @Origin Method targetMethod,
            @AllArguments Object[] targetMethodArgs,
            @SuperCall Callable<?> zuper
    ) {

        System.out.println("[增强逻辑]targetMethod.getName() = " + targetMethod.getName());
        long start = System.currentTimeMillis();
        Object returnValue = null;

        try {
            returnValue = zuper.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("[增强逻辑]callTime: " + (System.currentTimeMillis() - start));
        }
        return returnValue;
    }

}

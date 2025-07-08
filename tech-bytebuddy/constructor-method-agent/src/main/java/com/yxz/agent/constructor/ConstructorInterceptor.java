package com.yxz.agent.constructor;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.util.Arrays;

/**
 * @Desc 对拦截的构造方法进行增强
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class ConstructorInterceptor {

    @RuntimeType
    public void constructorInterceptor(
            @This Object targetObj,
            @AllArguments Object[] targetConstructorArgs) {
        System.out.println("targetObj:" + targetObj + ",args:" + Arrays.toString(targetConstructorArgs));
    }

}

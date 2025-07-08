package com.yxz.byteb.interceptor;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * @Description 构造器增强
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class DelegateInterceptor18 {

    @RuntimeType
    public void constructEnhance(
            //  表示被拦截的目标对象, 在构造方法中同样是可用的(也是实例方法)
            @This Object targetObj) {
        // constructEnhance() , com.yxz.byteb.PassionTest18@1d730606
        System.out.println("constructEnhance() , " + targetObj);
    }

}

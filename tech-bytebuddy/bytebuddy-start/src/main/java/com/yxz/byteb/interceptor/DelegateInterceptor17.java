package com.yxz.byteb.interceptor;


import com.yxz.byteb.MyCallable;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 * @Description 动态修改参数
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class DelegateInterceptor17 {

    /**
     * 修改/增强 方法
     * <a href="http://bytebuddy.net/#/tutorial-cn" target="_blank">Byte Buddy官方教程文档</a>
     *  {@code @Morph}: 这个注解的工作方式与{@code @SuperCall}注解非常相似。然而，使用这个注解允许指定用于调用超类方法参数。
     *  注意， 仅当你需要调用具有与原始调用不同参数的超类方法时，才应该使用此注解，因为使用@Morph注解需要对所有参数装箱和拆箱。
     *  如果过你想调用一个特定的超类方法， 请考虑使用@Super注解来创建类型安全的代理。在这个注解被使用之前，需要显式地安装和注册，类似于@Pipe注解。
     * </p>
     */
    @RuntimeType
    public Object customerMethodSignature(

            @AllArguments Object[] targetMethodArgs,  // 目标方法的参数
            // @SuperCall Callable<?> zuper
            @Morph MyCallable zuper  // 用于调用目标方法 (这里使用@Morph, 而不是@SuperCall, 才能修改入参)
    ) {
        // 原方法逻辑 return String.valueOf(userId);
        Object result = null;
        try {
            // 修改参数
            if (null != targetMethodArgs && targetMethodArgs.length > 0) {
                targetMethodArgs[0] = (long) targetMethodArgs[0] + 1;
            }
            // @SuperCall 不接受参数 result = zuper.call();
            // 调用目标方法
            result = zuper.apply(targetMethodArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

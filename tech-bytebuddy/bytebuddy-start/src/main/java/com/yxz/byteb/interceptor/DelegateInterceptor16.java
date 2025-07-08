package com.yxz.byteb.interceptor;

import com.yxz.byteb.SomethingClass;
import net.bytebuddy.implementation.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Callable;

/**
 * @Description 自定义方法增强
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class DelegateInterceptor16 {


    /**
     * 修改/增强 方法
     * 和 DelegateInterceptor14 以及 DelegateInterceptor15 大不相同，
     * 不需要和原目标方法保持相同的方法签名
     * 为了克服需要一致方法签名的限制，Byte Buddy 允许给方法和方法参数添加@RuntimeType注解，
     * 它指示 Byte Buddy 终止严格类型检查以支持运行时类型转换
     */
    @RuntimeType
    public Object customerMethodSignature(
            // 表示被拦截的目标对象, 只有拦截实例方法时可用
            @This Object targetObj,
            // 表示被拦截的目标方法, 只有拦截实例方法或静态方法时可用
            @Origin Method targetMethod,
            // 目标方法的参数
            @AllArguments Object[] targetMethodArgs,
            // 表示被拦截的目标对象, 只有拦截实例方法时可用 (可用来调用目标类的super方法)
            @Super Object targetSuperObj,
            // 若确定超类(父类), 也可以用具体超类(父类)接收
            @Super SomethingClass targetSuperObj2,
            // 用于调用目标方法
            @SuperCall Callable<?> zuper
    ) {
        // 原方法逻辑 return String.valueOf(userId);

        /**
         * // targetObj = com.yxz.byteb.PassionTest16@75b25825
         * // targetMethod.getName() = selectUserName
         * // Arrays.toString(targetMethodArgs) = [3]
         * // targetSuperObj = com.yxz.byteb.PassionTest16@75b25825
         * // targetSuperObj2 = com.yxz.byteb.PassionTest16@75b25825
         * // zuper = com.yxz.byteb.PassionTest16$auxiliary$EOntjcyC@18025ced
         */

        System.out.println("targetObj = " + targetObj);
        System.out.println("targetMethod.getName() = " + targetMethod.getName());
        System.out.println("Arrays.toString(targetMethodArgs) = " + Arrays.toString(targetMethodArgs));
        System.out.println("targetSuperObj = " + targetSuperObj);
        System.out.println("targetSuperObj2 = " + targetSuperObj2);
        System.out.println("zuper = " + zuper);
        Object result = null;
        try {
            // 调用目标方法
            result = zuper.call();
            // 直接通过反射的方式调用原方法, 会导致无限递归进入当前增强的逻辑
            // result = targetMethod.invoke(targetObj,targetMethodArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}

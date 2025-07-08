package com.yxz.byteb;

import com.yxz.byteb.interceptor.DelegateInterceptor17;
import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description 动态修改入参
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class ByteBuddy06_DynamicModifyArgs {

    /**
     * (17) 通过@Morph动态修改方法入参
     */
    @Test
    public void test17() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        DynamicType.Unloaded<SomethingClass> subClassUnloaded = new ByteBuddy()
                .subclass(SomethingClass.class)
                .method(ElementMatchers.named("selectUserName"))
                .intercept(
                        MethodDelegation.withDefaultConfiguration()
                                // 向Byte Buddy 注册 用于中转目标方法入参和返回值的 函数式接口
                                .withBinders(Morph.Binder.install(MyCallable.class))
                                .to(new DelegateInterceptor17()))
                .name("com.yxz.byteb.PassionTest17")
                .make();
        String returnStr = subClassUnloaded.load(getClass().getClassLoader())
                .getLoaded()
                // 实例化并调用 selectUserName 方法验证是否被修改/增强
                .getConstructor()
                .newInstance()
                .selectUserName(3L);
        // 符合预期，第一个参数被修改+1
        Assertions.assertEquals("4", returnStr);
        subClassUnloaded.saveIn(CusPathUtils.currentPathFile());
    }

}

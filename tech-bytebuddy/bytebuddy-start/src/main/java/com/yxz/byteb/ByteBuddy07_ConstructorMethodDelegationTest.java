package com.yxz.byteb;

import com.yxz.byteb.interceptor.DelegateInterceptor18;
import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description 构造器插桩
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class ByteBuddy07_ConstructorMethodDelegationTest {


    /**
     * .constructor(ElementMatchers.any()): 表示拦截目标类的任意构造方法
     * .intercept(SuperMethodCall.INSTANCE.andThen(Composable implementation): 表示在实例构造方法逻辑执行结束后再执行拦截器中定义的增强逻辑
     * @This: 被拦截的目标对象this引用，构造方法也是实例方法，同样有this引用可以使用
     */


    /**
     * (18) 对构造方法插桩
     */
    @Test
    public void test18() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        DynamicType.Unloaded<SomethingClass> subClassUnloaded = new ByteBuddy()
                .subclass(SomethingClass.class)
                // 对任何构造方法都进行插桩
                .constructor(ElementMatchers.any())
                // 表示在被拦截的构造方法原方法逻辑执行完后，再委托给拦截器
                .intercept(
                        SuperMethodCall.INSTANCE.andThen(
                                MethodDelegation.to(new DelegateInterceptor18())
                        ))
                .name("com.yxz.byteb.PassionTest18")
                .make();
        subClassUnloaded.load(getClass().getClassLoader())
                .getLoaded()
                // 实例化并调用 selectUserName 方法验证是否被修改/增强
                .getConstructor()
                .newInstance();
        subClassUnloaded.saveIn(CusPathUtils.currentPathFile());
    }


}

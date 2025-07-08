package com.yxz.byteb;

import com.yxz.byteb.interceptor.DelegateInterceptor14;
import com.yxz.byteb.interceptor.DelegateInterceptor15;
import com.yxz.byteb.interceptor.DelegateInterceptor16;
import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @Description 实例方法插桩
 * @Date 2025-07-07
 * @Created by Yolo
 */
public class ByteBuddy05_InstanceMethodDelegationTest {

    /**
     * 方法委托，可简单理解将目标方法的方法体逻辑修改为调用指定的某个辅助类方法。
     *
     * .intercept(MethodDelegation.to(Class<?> type))：将被拦截的方法委托给指定的增强类，增强类中需要定义和目标方法一致的方法签名，然后多一个static访问标识
     * .intercept(MethodDelegation.to(Object target))：将被拦截的方法委托给指定的增强类实例，增强类可以指定和目标类一致的方法签名，或通过@RuntimeType指示 Byte Buddy 终止严格类型检查以支持运行时类型转换。
     * 其中委托给相同签名的静态方法/实例方法相对容易理解，委托给自定义方法时，该视频主要介绍几个使用到的方法参数注解：
     *
     * @This Object targetObj：表示被拦截的目标对象, 只有拦截实例方法时可用
     * @Origin Method targetMethod：表示被拦截的目标方法, 只有拦截实例方法或静态方法时可用
     * @AllArguments Object[] targetMethodArgs：目标方法的参数
     * @Super Object targetSuperObj：表示被拦截的目标对象, 只有拦截实例方法时可用 (可用来调用目标类的super方法)。若明确知道具体的超类(父类类型)，这里Object可以替代为具体超类(父类)
     * @SuperCall Callable<?> zuper：用于调用目标方法
     * 其中调用目标方法时，通过Object result = zuper.call()。不能直接通过反射的Object result = targetMethod.invoke(targetObj,targetMethodArgs)进行原方法调用。因为后者会导致无限递归进入当前增强方法逻辑。
     *
     * 其他具体细节和相关介绍，可参考[官方教程](Byte Buddy - runtime code generation for the Java virtual machine)的"委托方法调用"章节。尤其是各种注解的介绍，官方教程更加完善一些，但是相对比较晦涩难懂一点。
     */


    /**
     * (14) 将拦截的方法委托给相同方法签名的静态方法进行修改/增强
     */
    @Test
    public void test14() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        DynamicType.Unloaded<SomethingClass> subClassUnloaded = new ByteBuddy()
                .subclass(SomethingClass.class)
                .method(ElementMatchers.named("selectUserName"))
                // 将 selectUserName 方法委托给 DelegateInterceptor14 中的 相同方法签名(方法描述符)的静态方法 进行修改/增强
                .intercept(MethodDelegation.to(DelegateInterceptor14.class))
                .name("com.yxz.byteb.PassionTest14")
                .make();
        // 前置 saveIn则在 subClassUnloaded.load(getClass().getClassLoader()) 报错 java.lang.IllegalStateException: Class already loaded: class com.yxz.byteb.PassionTest14
//        subClassUnloaded.saveIn(CusPathUtils.currentPathFile());
        // 加载类
        String returnStr = subClassUnloaded.load(getClass().getClassLoader())
                .getLoaded()
                // 实例化并调用 selectUserName 方法验证是否被修改/增强
                .getConstructor()
                .newInstance()
                .selectUserName(1L);
        Assertions.assertEquals("DelegateInterceptor14.selectUserName, userId: 1", returnStr);
        subClassUnloaded.saveIn(CusPathUtils.currentPathFile());
    }

    /**
     * (15) 将拦截的方法委托给相同方法签名的实例方法进行修改/增强
     */
    @Test
    public void test15() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        DynamicType.Unloaded<SomethingClass> subClassUnloaded = new ByteBuddy()
                .subclass(SomethingClass.class)
                .method(ElementMatchers.named("selectUserName"))
                // 将 selectUserName 方法委托给 DelegateInterceptor15 中的 相同方法签名(方法描述符)的实例方法 进行修改/增强
                .intercept(MethodDelegation.to(new DelegateInterceptor15()))
                .name("com.yxz.byteb.PassionTest15")
                .make();
        // 前置 saveIn则在 subClassUnloaded.load(getClass().getClassLoader()) 报错 java.lang.IllegalStateException: Class already loaded: class com.yxz.byteb.PassionTest15
        // subClassUnloaded.saveIn(DemoTools.currentClassPathFile());
        // 加载类
        String returnStr = subClassUnloaded.load(getClass().getClassLoader())
                .getLoaded()
                // 实例化并调用 selectUserName 方法验证是否被修改/增强
                .getConstructor()
                .newInstance()
                .selectUserName(2L);
        Assertions.assertEquals("DelegateInterceptor15.selectUserName, userId: 2", returnStr);
        subClassUnloaded.saveIn(CusPathUtils.currentPathFile());
    }

    /**
     * (16) 将拦截的方法委托给自定义方法
     */
    @Test
    public void test16() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        DynamicType.Unloaded<SomethingClass> subClassUnloaded = new ByteBuddy()
                .subclass(SomethingClass.class)
                .method(ElementMatchers.named("selectUserName"))
                // 将 selectUserName 方法委托给 DelegateInterceptor16 进行修改/增强
                .intercept(MethodDelegation.to(new DelegateInterceptor16()))
                .name("com.yxz.byteb.PassionTest16")
                .make();
        // 前置 saveIn则在 subClassUnloaded.load(getClass().getClassLoader()) 报错 java.lang.IllegalStateException: Class already loaded: class com.yxz.byteb.PassionTest16
        // subClassUnloaded.saveIn(DemoTools.currentClassPathFile());
        // 加载类
        String returnStr = subClassUnloaded.load(getClass().getClassLoader())
                .getLoaded()
                // 实例化并调用 selectUserName 方法验证是否被修改/增强
                .getConstructor()
                .newInstance()
                .selectUserName(3L);
        // returnStr = 3
        System.out.println("returnStr = " + returnStr);
        subClassUnloaded.saveIn(CusPathUtils.currentPathFile());
    }


}

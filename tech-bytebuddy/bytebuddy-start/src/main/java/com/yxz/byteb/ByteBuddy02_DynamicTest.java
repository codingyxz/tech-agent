package com.yxz.byteb;

import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * 修改/增强现有类主要有3种方法，subclass(创建子类)，rebase(变基)，redefine（重定义）。
 *
 * 1、.subclass(目标类.class)：继承目标类，以子类的形式重写超类方法，达到增强效果
 * 2、.rebase(目标类.class)：  变基，原方法变为private，并且方法名增加&origanl&{随机字符串}后缀，目标方法体替换为指定逻辑
 * 3、.redefine(目标类.class)：重定义，原方法体逻辑直接替换为指定逻辑
 *
 * @Date 2025-07-07
 * @Created by Yolo
 */
public class ByteBuddy02_DynamicTest {

    /**
     * (8) 对实例方法插桩(stub), 修改原本的toString方法逻辑
     *
     * java开发中说的插桩(stub)通常指对字节码进行修改(增强)。埋点可通过插桩或其他形式实现，比如常见的代码逻辑调用次数、耗时监控打点，Android安卓应用用户操作行为打点上报等。
     *
     * .method(XXX)               指定后续需要修改/增强的方法
     * .intercept(XXX)            对方法进行修改/增强
     * DynamicType.Unloaded       表示未加载到JVM中的字节码实例
     * DynamicType.Loaded         表示已经加载到JVM中的字节码实例
     * 无特别配置参数的情况下，通过Byte Buddy动态生成的类，实际由net.bytebuddy.dynamic.loading.ByteArrayClassLoader加载
     * 其他注意点，见官方教程文档的"类加载"章节，这里暂不展开
     *
     */
    @Test
    public void test08() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        // 1. 声明一个未加载到ClassLoader中的 Byte Buddy 对象
        DynamicType.Unloaded<NothingClass> nothingClassUnloaded = new ByteBuddy()
                // 指定 超类 NothingClass
                .subclass(NothingClass.class)
                // 指定要拦截(插桩)的方法
                .method(ElementMatchers.named("toString"))
                // 指定拦截(插桩)后的逻辑, 这里设置直接返回指定值
                .intercept(FixedValue.value("just nothing."))
                .name("com.yxz.byteb.PassionTest08")
                .make();
        // 2. 将类通过 AppClassLoader 加载到 JVM 中
        ClassLoader currentClassLoader = getClass().getClassLoader();
        // jdk.internal.loader.ClassLoaders$AppClassLoader
        System.out.println(currentClassLoader.getClass().getName());

        DynamicType.Loaded<NothingClass> loadedType = nothingClassUnloaded.load(currentClassLoader);
        // 3. 反射调用 toString方法, 验证方法内逻辑被我们修改
        Class<? extends NothingClass> loadedClazz = loadedType.getLoaded();
        // net.bytebuddy.dynamic.loading.ByteArrayClassLoader
        System.out.println(loadedClazz.getClassLoader().getClass().getName());

        NothingClass subNothingObj = loadedClazz.getDeclaredConstructor().newInstance();
        System.out.println(subNothingObj.toString());
        // 4. 将字节码写入本地
        loadedType.saveIn(new File(CusPathUtils.currentPath()));
    }

    /**
     * (9) 通过subclass继承类, 重写父类方法
     */
    @Test
    public void test09() throws IOException {
        DynamicType.Unloaded<SomethingClass> subClass = new ByteBuddy()
                .subclass(SomethingClass.class)
                .method(
                        ElementMatchers
                                .named("selectUserName")
                                // 注意实际字节码Local Variable 0 位置为this引用, 但是这里说的参数位置index只需要关注方法声明时的参数顺序, 无需关注隐性参数this引用
                                .and(ElementMatchers.takesArgument(0, Long.class))
                                // .and(ElementMatchers.returns(Objects.class)) 匹配不到
                                .and(ElementMatchers.returns(String.class))
                )
                .intercept(FixedValue.value("Passion"))
                .name("com.yxz.byteb.PassionTest09")
                .make();
        subClass.saveIn(new File(CusPathUtils.currentPath()));
    }

    /**
     * (10)
     * rebase变基, 原方法保留变为private且被改名(增加$original${随机字符串}后缀),
     * 原方法名内逻辑替换成我们指定的逻辑
     */
    @Test
    public void test10() throws IOException {
        DynamicType.Unloaded<SomethingClass> rebase = new ByteBuddy()
                .rebase(SomethingClass.class)
                .method(
                        ElementMatchers
                                .named("selectUserName")
                                // 注意实际字节码Local Variable 0 位置为this引用, 但是这里说的参数位置index只需要关注方法声明时的参数顺序, 无需关注隐性参数this引用
                                .and(ElementMatchers.takesArgument(0, Long.class))
                                // .and(ElementMatchers.returns(Objects.class)) 匹配不到
                                .and(ElementMatchers.returns(String.class))
                )
                .intercept(FixedValue.value("Passion"))
                .method(ElementMatchers.named("getAge"))
                .intercept(FixedValue.value(0))
                .name("com.yxz.byteb.PassionTest10")
                .make();
        rebase.saveIn(new File(CusPathUtils.currentPath()));
    }

    /**
     * (11) redefine重定义, 重写指定的方法, 原方法逻辑不保留(被我们指定的逻辑覆盖掉)
     */
    @Test
    public void test11() throws IOException {
        DynamicType.Unloaded<SomethingClass> redefine = new ByteBuddy()
                .redefine(SomethingClass.class)
                .method(
                        ElementMatchers.named("print")
                        // 不匹配 .and(ElementMatchers.returns(NullType.class))
                        // 不匹配 .and(ElementMatchers.returnsGeneric(Void.class))
                        // 不匹配 .and(ElementMatchers.returns(TypeDescription.ForLoadedType.of(Void.class)))
                        // 不匹配 .and(ElementMatchers.returns(Void.class))
                        // 匹配 .and(ElementMatchers.returns(TypeDescription.VOID))
                        // 匹配 .and(ElementMatchers.returns(void.class))
                )
                .intercept(FixedValue.value(TypeDescription.ForLoadedType.of(Void.class)))
                .name("com.yxz.byteb.PassionTest11")
                .make();
        redefine.saveIn(new File(CusPathUtils.currentPath()));
    }

}

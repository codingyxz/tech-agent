package com.yxz.byteb;

import com.yxz.byteb.interceptor.DelegateInterceptor19;
import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.ModifierReviewable;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

/**
 * @Description bytebuddy的类加载器
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class ByteBuddy09_ClassLoaderTest {


    /**
     * Byte Buddy 提供了几种开箱即用的类加载策略， 这些策略都在ClassLoadingStrategy.Default中定义.
     * 其中:
     * WRAPPER策略会创建一个新的，经过包装的ClassLoader，
     * CHILD_FIRST策略会创建一个类似的具有孩子优先语义的类加载器，
     * INJECTION策略会用反射注入一个动态类型。
     */


    /**
     * (20) 默认类加载策略`WRAPPER`, 不保存`.class`文件到本地, 重复加载类
     */
    @Test
    public void test20() {
        DynamicType.Unloaded<SomethingClass> sayWhatUnload = new ByteBuddy()
                .rebase(SomethingClass.class)
                .method(ElementMatchers.named("sayWhat").and(ModifierReviewable.OfByteCodeElement::isStatic))
                .intercept(MethodDelegation.to(new DelegateInterceptor19()))
                .name("com.yxz.byteb.PassionTest20")
                .make();
        Class<? extends SomethingClass> loaded01 = sayWhatUnload.load(getClass().getClassLoader()).getLoaded();
        Class<? extends SomethingClass> loaded02 = sayWhatUnload.load(getClass().getClassLoader()).getLoaded();
        Assertions.assertNotEquals(loaded01, loaded02);
        // loaded01 = class com.yxz.byteb.PassionTest20
        System.out.println("loaded01 = " + loaded01);
        // loaded02 = class com.yxz.byteb.PassionTest20
        System.out.println("loaded02 = " + loaded02);
        // loaded01.hashCode() = 1621254922
        System.out.println("loaded01.hashCode() = " + loaded01.hashCode());
        // loaded02.hashCode() = 864221358
        System.out.println("loaded02.hashCode() = " + loaded02.hashCode());
    }

    /**
     * (21) 默认类加载策略`WRAPPER`,保存`.class`文件到本地, 之后加载类
     */
    @Test
    public void test21() throws IOException {
        DynamicType.Unloaded<SomethingClass> sayWhatUnload = new ByteBuddy()
                .rebase(SomethingClass.class)
                .method(ElementMatchers.named("sayWhat").and(ModifierReviewable.OfByteCodeElement::isStatic))
                .intercept(MethodDelegation.to(new DelegateInterceptor19()))
                .name("com.yxz.byteb.PassionTest21")
                .make();
        sayWhatUnload.saveIn(CusPathUtils.currentPathFile());
        // 会抛出 java.lang.IllegalStateException: Class already loaded: class com.yxz.byteb.PassionTest21
        sayWhatUnload.load(getClass().getClassLoader()).getLoaded();
    }

    /**
     * (22) 类加载策略`CHILD_FIRST`，保存`.class`文件到本地，之后重复加载类
     */
    @Test
    public void test22() throws IOException {
        DynamicType.Unloaded<SomethingClass> sayWhatUnload = new ByteBuddy()
                .rebase(SomethingClass.class)
                .method(ElementMatchers.named("sayWhat").and(ModifierReviewable.OfByteCodeElement::isStatic))
                .intercept(MethodDelegation.to(new DelegateInterceptor19()))
                .name("com.yxz.byteb.PassionTest22")
                .make();
        sayWhatUnload.saveIn(CusPathUtils.currentPathFile());
        Class<? extends SomethingClass> loaded01 = sayWhatUnload
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST)
                .getLoaded();
        Class<? extends SomethingClass> loaded02 = sayWhatUnload
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST)
                .getLoaded();
        Assertions.assertNotEquals(loaded01, loaded02);
        // loaded01 = class com.yxz.byteb.PassionTest22
        System.out.println("loaded01 = " + loaded01);
        // loaded02 = class com.yxz.byteb.PassionTest22
        System.out.println("loaded02 = " + loaded02);
        // loaded01.hashCode() = 1552870927
        System.out.println("loaded01.hashCode() = " + loaded01.hashCode());
        // loaded02.hashCode() = 1489193907
        System.out.println("loaded02.hashCode() = " + loaded02.hashCode());
    }

    /**
     * (23) redefine后，配合`CHILD_FIRST`加载类
     */
    @Test
    public void test23() throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        DynamicType.Unloaded<NothingClass> redefine = new ByteBuddy()
                .redefine(NothingClass.class)
                .defineMethod("returnBlankString", String.class, Modifier.PUBLIC | Modifier.STATIC)
                .withParameters(String.class, Integer.class)
                .intercept(FixedValue.value(""))
                .make();

        redefine.saveIn(CusPathUtils.currentPathFile());

        Class<? extends NothingClass> loaded01 = redefine
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST)
                .getLoaded();
        // loaded01 = class com.yxz.byteb.NothingClass
        System.out.println("loaded01 = " + loaded01);
        // loaded01.equals(NothingClass.class) = false
        System.out.println("loaded01.equals(NothingClass.class) = " + loaded01.equals(NothingClass.class));
        // loaded01.getClassLoader() = net.bytebuddy.dynamic.loading.ByteArrayClassLoader$ChildFirst@531f4093
        System.out.println("loaded01.getClassLoader() = " + loaded01.getClassLoader());
        // NothingClass.class.getClassLoader() = jdk.internal.loader.ClassLoaders$AppClassLoader@2c13da15
        System.out.println("NothingClass.class.getClassLoader() = " + NothingClass.class.getClassLoader());
        // loaded01.getDeclaredConstructor().newInstance() instanceof NothingClass = false
        System.out.println("loaded01.getDeclaredConstructor().newInstance() instanceof NothingClass = " +
                (loaded01.getDeclaredConstructor().newInstance() instanceof NothingClass));

        Class<? extends NothingClass> loaded02 = redefine
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.CHILD_FIRST)
                .getLoaded();
        // loaded02 = class com.yxz.byteb.NothingClass
        System.out.println("loaded02 = " + loaded02);
        // loaded01.equals(loaded02) = false
        System.out.println("loaded01.equals(loaded02) = " + loaded01.equals(loaded02));
        // loaded01.hashCode() = 1034094674
        System.out.println("loaded01.hashCode() = " + loaded01.hashCode());
        // loaded02.hashCode() = 1280429864
        System.out.println("loaded02.hashCode() = " + loaded02.hashCode());
    }

    /**
     * (24) 从指定 “jar包”, “文件目录”, “系统类加载器” 加载指定类
     */
    @Test
    public void test24() throws IOException {
        // 1. 指定需要扫描的jar包路径
        ClassFileLocator jarPathLocator = ClassFileLocator.ForJarFile.of(new File("/Users/linkk/storage/repo/commons-io/commons-io/2.15.0/commons-io-2.15.0.jar"));
        // 2. 指定需要扫描的.class文件所在路径
        ClassFileLocator.ForFolder classPathLocator = new ClassFileLocator.ForFolder(new File("/Users/linkk/codes/freestyle/tech-agent/tech-bytebuddy/bytebuddy-start/target/classes"));
        // 3. 从系统类加载器中扫描类 (不加则找不到jdk自身的类)
        ClassFileLocator classLoaderLocator = ClassFileLocator.ForClassLoader.ofSystemLoader();
        // 整合 多个 自定义的类扫描路径
        ClassFileLocator.Compound locatorCompound = new ClassFileLocator.Compound(jarPathLocator, classPathLocator, classLoaderLocator);
        // locatorCompound 去掉 classLoaderLocator 后, 后续net.bytebuddy.ByteBuddy.redefine(ByteBuddy.java:886)往下调用时,
        // 报错 net.bytebuddy.pool.TypePool$Resolution$NoSuchTypeException: Cannot resolve type description for java.lang.Object
        // ClassFileLocator.Compound locatorCompound = new ClassFileLocator.Compound(jarPathLocator, classPathLocator);
        // 类型池, 提供根据 全限制类名 从指定 类路径扫描范围内 获取 类描述对象 的方法
        TypePool typePool = TypePool.Default.of(locatorCompound);
        // 4. 从前面指定的扫描类范围中, 获取 “commons-io-2.15.0.jar” 内 FileUtils 类描述对象, resolve()不会触发类加载
        TypeDescription jarPathTypeDescription = typePool.describe("org.apache.commons.io.FileUtils").resolve();
        // 5. 获取 target下测试类路径的NothingClass类
        TypeDescription classPathTypeDescription = typePool.describe("com.yxz.byteb.NothingClass").resolve();

        // 6-1 redefine 指定 jar包内的 FileUtils 类, 并将生成的.class文件保存到本地
        new ByteBuddy()
                .redefine(jarPathTypeDescription, locatorCompound)
                .method(ElementMatchers.named("current"))
                .intercept(FixedValue.nullValue())
                .make()
                .saveIn(CusPathUtils.currentPathFile());

        // 6-2 redefine 指定.class文件路径内的 NothingClass类, 并将生成的.class文件保存到本地
        new ByteBuddy()
                .redefine(classPathTypeDescription, locatorCompound)
                .defineMethod("justVoid", void.class, Modifier.PUBLIC)
                .intercept(FixedValue.value(void.class))
                .make()
                .saveIn(CusPathUtils.currentPathFile());
    }


}

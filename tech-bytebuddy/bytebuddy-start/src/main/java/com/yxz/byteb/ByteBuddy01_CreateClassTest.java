package com.yxz.byteb;

import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * 使用 Byte Buddy 生成类字节码
 *
 * @Date 2025-07-07
 * @Created by Yolo
 */
public class ByteBuddy01_CreateClassTest {


    /**
     * <p>(1) 不指定任何特别的参数, 只声明为Object的子类 </p>
     * <p>
     * <a href="http://bytebuddy.net/#/tutorial-cn">官方教程</a>已经说明了没有显示命名会发生什么: <br/>
     * <pre>
     * 如果没有显式的命名会发生什么？ Byte Buddy 遵循约定优于配置原则， 并且提供我们发现的便利的默认值。
     * 至于类的名称，Byte Buddy 的默认配置提供了一个NamingStrategy（命名策略）， 它可以根据动态类的超类名称随机生成一个名称。
     * 此外，定义的类名中的包和超类相同的话，直接父类的包私有方法对动态类就是可见的。 例如，如果你子类化一个名为example.Foo的类，
     * 生成的类的名称就像example.Foo$$ByteBuddy$$1376491271，其中数字序列是随机的。
     * 这条规则的一个例外情况是：子类化的类型来自Object所在的包java.lang。 Java 的安全模型不允许自定义的类在这个命名空间。
     * 因此，在默认的命名策略中，这种类型以net.bytebuddy.renamed前缀命名。
     * </pre>
     * </p>
     * <p>
     * 根据官方教程可以看出来, 生成的新类默认命名策略即:
     *  <ol>
     *    <li>父类是jdk自带类: {超类名}$ByteBuddy${随机字符串}</li>
     *    <li>父类非jdk自带类: net.bytebuddy.renamed{超类名}$ByteBuddy${随机字符串}</li>
     *  </ol>
     * </p>
     */
    @Test
    public void test01() throws IOException {
        // 1. 创建Object的子类(Object是所有java类的父类)
        DynamicType.Unloaded<Object> objectSubClass = new ByteBuddy()
                // 表示当前新生成的类为 Object 的子类
                .subclass(Object.class).make();
        // 2. 将生成的字节码保存到 本地 (由于没有直接指定类名, 每次运行时生成不同的类, 类名不同)
        // 我本地第一次运行: net.bytebuddy.renamed.java.lang.Object$ByteBuddy$YbDNW0Kx
        // 我本地第二次运行: net.bytebuddy.renamed.java.lang.Object$ByteBuddy$FrN82cJg
        objectSubClass.saveIn(new File(CusPathUtils.currentPath()));
    }

    /**
     * (2) 指定父类为非jdk自带类, 不指定命名策略和其他参数
     */
    @Test
    public void test02() throws IOException {
        // 1. 创建 非jdk自带类 的子类
        DynamicType.Unloaded<NothingClass> noJdkSubClass = new ByteBuddy()
                // 表示当前新生成的类为 NothingClass 的子类
                .subclass(NothingClass.class)
                .make();
        // 2. 将生成的字节码保存到 本地 (由于没有直接指定类名, 每次运行时生成不同的类, 类名不同)
        // 我本地第一次运行: com.yxz.byteb.NothingClass$ByteBuddy$f7zBKYwS
        // 我本地第二次运行: com.yxz.byteb.NothingClass$ByteBuddy$FHZdoEVm
        noJdkSubClass.saveIn(new File(CusPathUtils.currentPath()));
    }

    /**
     * (3) 指定父类为ArrayList(jdk自带类), 使用官方教程建议的Byte Buddy自带的命名策略 (NamingStrategy.SuffixingRandom)
     */
    @Test
    public void test03() throws IOException {
        // 1. 创建 ArrayList(jdk自带类) 的子类
        DynamicType.Unloaded<ArrayList> arrayListSubClass = new ByteBuddy()
                // 使用官方教程建议的Byte Buddy自带的命名策略 (NamingStrategy.SuffixingRandom)
                .with(new NamingStrategy.SuffixingRandom("Passion"))
                // 表示当前新生成的类为 ArrayList 的子类
                .subclass(ArrayList.class)
                .make();
        // 2. 将生成的字节码保存到 本地 (由于没有直接指定类名, 每次运行时生成不同的类, 类名不同)
        // 我本地第一次运行: net.bytebuddy.renamed.java.util.ArrayList$Passion$f3Y3FP77
        // 我本地第二次运行: net.bytebuddy.renamed.java.util.ArrayList$Passion$6m66lLu2
        arrayListSubClass.saveIn(new File(CusPathUtils.currentPath()));
    }

    /**
     * (4) 父类非jdk自带类, 指定命名策略和具体类名
     */
    @Test
    public void test04() throws IOException {
        // 1. 创建 NothingClass 的子类
        DynamicType.Unloaded<NothingClass> nothingClassSubClass = new ByteBuddy()
                // 使用官方教程建议的Byte Buddy自带的命名策略 (NamingStrategy.SuffixingRandom)
                .with(new NamingStrategy.SuffixingRandom("Passion"))
                // 表示当前新生成的类为 NothingClass 的子类
                .subclass(NothingClass.class)
                // 指定类名
                .name("com.yxz.byteb.PassionTest04")
                .make();
        // 2. 将生成的字节码保存到 本地, 每次运行结果一致
        // 第N次运行: com.yxz.byte.PassionTest
        nothingClassSubClass.saveIn(new File(CusPathUtils.currentPath()));
    }

    /**
     * (5) 尝试指定不合法的类名, 由于Byte Buddy本身带有字节码校验逻辑, 会提前报错
     */
    @Test
    public void test05() {
        try {
            // 1. 创建 NothingClass 的子类
            DynamicType.Unloaded<NothingClass> nothingClassSubClass = new ByteBuddy()
                    // 使用官方教程建议的Byte Buddy自带的命名策略 (NamingStrategy.SuffixingRandom)
                    .with(new NamingStrategy.SuffixingRandom("Passion"))
                    // 表示当前新生成的类为 NothingClass 的子类
                    .subclass(NothingClass.class)
                    // 指定类名 (不合法, 不能以数字开头)
                    .name("com.yxz.byteb.1111PassionTest05")
                    .make();
        } catch (Exception e) {
            // java.lang.IllegalStateException: Illegal type name: com.example.1111AshiamdTest05 for class com.example.1111AshiamdTest04
            Assertions.assertTrue(e instanceof IllegalStateException);
        }
    }

    /**
     * (6) 指定不合法类名, 关闭Byte Buddy自带的字节码校验逻辑(该校验虽耗费性能, 但一般对项目影响不大, 也不建议关闭)
     */
    @Test
    public void test06() throws IOException {
        // 1. 创建 NothingClass 的子类
        DynamicType.Unloaded<NothingClass> nothingClassSubClass = new ByteBuddy()
                // 关闭Byte Buddy的默认字节码校验逻辑
                .with(TypeValidation.of(false))
                // 使用官方教程建议的Byte Buddy自带的命名策略 (NamingStrategy.SuffixingRandom)
                .with(new NamingStrategy.SuffixingRandom("Passion"))
                // 表示当前新生成的类为 NothingClass 的子类
                .subclass(NothingClass.class)
                // 指定类名 (不合法, 不能以数字开头)
                .name("com.yxz.byteb.321PassionTest06")
                .make();
        // 2. 将生成的字节码保存到 本地, 生成的字节码实际非法
        // 第N次运行: com.yxz.byte.321PassionTest06
        nothingClassSubClass.saveIn(new File(CusPathUtils.currentPath()));
    }

    /**
     * (7) 将生成的字节码, 注入一个jar包中 <br/>
     * 这里本地将 simple_jar 模块打包成 simple_jar-1.0-SNAPSHOT-jar-with-dependencies.jar
     */
    @Test
    public void test07() throws IOException {
        // 1. 创建 NothingClass 的子类
        DynamicType.Unloaded<NothingClass> nothingClassSubClass = new ByteBuddy()
                // 关闭Byte Buddy的默认字节码校验逻辑
                .with(TypeValidation.of(false))
                // 使用官方教程建议的Byte Buddy自带的命名策略 (NamingStrategy.SuffixingRandom)
                .with(new NamingStrategy.SuffixingRandom("Passion"))
                // 表示当前新生成的类为 NothingClass 的子类
                .subclass(NothingClass.class)
                // 指定类名
                .name("com.yxz.byteb.PassionTest07")
                .make();
        // 2. 将生成的字节码 注入到 simple-jar-1.0-SNAPSHOT-jar-with-dependencies.jar 中
        // 获取当前工作目录路径 (也就是当前 bytebuddy-start 目录路径)
        String currentModulePath = System.getProperty("user.dir");
        System.out.println(currentModulePath);
        // 获取 simple_jar 模块目录路径
        String simpleJarModulePath = currentModulePath.replace("bytebuddy-start", "simple-jar");
        // 需本地提前将simple_jar 通过 mvn package 打包
        File jarFile = new File(simpleJarModulePath + "/target/simple-jar-1.0-SNAPSHOT.jar");
        // 本地打开jar可以看到新生成的class文件也在其中
        nothingClassSubClass.inject(jarFile);
    }

}

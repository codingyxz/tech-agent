package com.yxz.byteb;

import com.yxz.byteb.interceptor.DelegateInterceptor19;
import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.ModifierReviewable;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Description 静态方法插桩
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class ByteBuddy08_StaticMethodDelegationTest {


    /**
     * 增强静态方法时，通过@This和@Super获取不到目标对象
     * 增强静态方法时，通过@Origin Class<?> clazz可获取静态方法所处的Class对象
     */


    /**
     * (19) 对静态方法插桩
     */
    @Test
    public void test19() throws InvocationTargetException, IllegalAccessException, IOException, NoSuchMethodException {
        DynamicType.Unloaded<SomethingClass> sayWhatUnload = new ByteBuddy()
                .rebase(SomethingClass.class)
                // 拦截 名为 "sayWhat" 的静态方法
                .method(ElementMatchers.named("sayWhat").and(ModifierReviewable.OfByteCodeElement::isStatic))
                // 拦截后的修改/增强逻辑
                .intercept(MethodDelegation.to(new DelegateInterceptor19()))
                .name("com.yxz.byteb.PassionTest19")
                .make();
        // 调用类静态方法, 验证是否执行了增强逻辑
        Class<? extends SomethingClass> loadedClazz = sayWhatUnload.load(getClass().getClassLoader()).getLoaded();
        Method sayWhatMethod = loadedClazz.getMethod("sayWhat", String.class);
        sayWhatMethod.invoke(null, "hello world");
        sayWhatUnload.saveIn(CusPathUtils.currentPathFile());
    }


    /**
     * @SuperCall仅在原方法仍存在的场合能够正常使用，比如subclass超类方法仍为目标方法，
     * rebase则是会重命名目标方法并保留原方法体逻辑；但redefine直接替换掉目标方法，所以@SuperCall不可用
     * rebase和redefine都可以修改目标类静态方法，但是若想在原静态方法逻辑基础上增加其他增强逻辑，
     * 那么只有rebase能通过@SuperCall或@Morph调用到原方法逻辑；redefine不保留原目标方法逻辑
     */

    /**
     * 这里使用的示例代码和"test19"一致，主要是用于说明前面"test19 对静态方法进行插桩"时为什么只能用rebase，
     * 而不能用subclass；以及使用rebase后，整个增强的大致调用流程。
     *
     * subclass：以目标类子类的形式，重写父类方法完成修改/增强。子类不能重写静态方法，所以增强目标类的静态方法时，不能用subclass
     *
     * redefine：因为redefine不保留目标类原方法，所以SomethingInterceptor06中的sayWhatEnhance方法获取不到@SuperCall Callable<?> zuper参数，
     * 若注解掉zuper相关的代码，发现能正常运行，但是目标方法相当于直接被替换成我们的逻辑，达不到保留原方法逻辑并增强的目的。
     *
     * rebase：原方法会被重命名并保留原逻辑，所以能够在通过@SuperCall Callable<?> zuper保留执行原方法逻辑执行的情况下，继续执行我们自定义的修改/增强逻辑
     */


    /**
     * Byte Buddy通过我们指定的代码增强SomethingClass.sayWhat方法后，执行逻辑大致可描述为：
     *
     * 1、生成拦截器类PassionTest19和PassionTest19$auxiliary$KDNm4G6W
     *
     * 2、调用PassionTest19的sayWhat方法时，如下流程
     *
     *      2.1、PassionTest19 的sayWhat对应拦截器类的@Origin Method targetMethod
     *
     *      2.2、目标方法SomethingClass.sayWhat逻辑则被重命名为PassionTest19.sayWhat$original$t8WPjqQX$accessor$MucANj8Q，对应拦截器类的@SuperCall Callable<?> zuper
     *
     *      2，3.执行PassionTest19.sayWhat即执行拦截器类的 DelegateInterceptor19.staticMethodEnhance 实例方法。
     */


    /**
     * subclass, rebase, redefine各自的默认命名策略如下：
     *
     * .subclass(目标类.class)：
     *      超类为jdk自带类: net.bytebuddy.renamed.{超类名}$ByteBuddy${随机字符串}
     *      超类非jdk自带类 {超类名}$ByteBuddy${随机字符串}
     * .rebase(目标类.class)：和目标类的类名一致（效果上即覆盖原本的目标类class文件）
     * .redefine(目标类.class)：和目标类的类名一致（效果上即覆盖原本的目标类class文件）
     */

}

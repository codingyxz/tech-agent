package com.yxz.byteb;

import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.StubMethod;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * @Description 清空方法体
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class ByteBuddy10_EmptyMethodContextTest {


    /**
     * (25) 清空指定类的所有方法的方法体(包含超类方法)
     */
    @Test
    public void test25() throws IOException {
        DynamicType.Unloaded<SomethingClass> allMethodIncludeSuper = new ByteBuddy()
                .redefine(SomethingClass.class)
                // 拦截所有方法(包括超类方法)
                .method(ElementMatchers.any())
                // 根据方法返回值类型, 返回对应类型的默认值
                .intercept(StubMethod.INSTANCE)
                .name("com.yxz.byteb.PassionTest25")
                .make();
        allMethodIncludeSuper.saveIn(CusPathUtils.currentPathFile());
    }

    /**
     * (26) 清空指定类的当前类声明的所有方法的方法体(不包含超类方法)
     */
    @Test
    public void test26() throws IOException, ClassNotFoundException {
        DynamicType.Unloaded<SomethingClass> allMethod = new ByteBuddy()
                .subclass(SomethingClass.class)
                // 拦截所有目标类声明的方法(不包括超类方法)
                .method(ElementMatchers.any().and(ElementMatchers.isDeclaredBy(SomethingClass.class)))
                // 根据方法返回值类型, 返回对应类型的默认值
                .intercept(StubMethod.INSTANCE)
                // 若这里使用rebase或redefine, 则需要去掉.name(“全限制类名”), 覆盖原类后才能使清空方法体的逻辑生效
                .name("com.yxz.byteb.PassionTest26")
                .make();
        allMethod.saveIn(CusPathUtils.currentPathFile());
    }

}

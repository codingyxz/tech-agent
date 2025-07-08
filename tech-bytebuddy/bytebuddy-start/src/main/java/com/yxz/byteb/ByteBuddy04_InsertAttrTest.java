package com.yxz.byteb;

import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FieldAccessor;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * @Description 插入新属性 以及 getter、setter方法
 * @Date 2025-07-07
 * @Created by Yolo
 */
public class ByteBuddy04_InsertAttrTest {


    /**
     * .defineField(String name, Type type, int modifier): 定义成员变量
     * .implement(Type interfaceType): 指定实现的接口类
     * .intercept(FieldAccessor.ofField("成员变量名") 或
     * .intercept(FieldAccessor.ofBeanProperty())
     * 在实现的接口为Bean规范接口时，都能生成成员变量对应的getter和setter方法
     */


    /**
     * (13) 增加新成员变量, 以及生成对应的getter, setter方法
     */
    @Test
    public void test13() throws IOException {
        DynamicType.Unloaded<NothingClass> ageBean = new ByteBuddy()
                .subclass(NothingClass.class)
                // 定义新增的字段 name, type, 访问描述符
                .defineField("age", int.class, Modifier.PRIVATE)
                // 指定类实现指定接口(接口内定义我们需要的getter和setter方法)
                .implement(IAgeBean.class)
                // 指定实现接口的逻辑
                // ok .intercept(FieldAccessor.ofField("age"))
                .intercept(FieldAccessor.ofBeanProperty())
                .name("com.yxz.byteb.PassionTest13")
                .make();
        ageBean.saveIn(CusPathUtils.currentPathFile());
    }

}

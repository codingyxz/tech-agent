package com.yxz.byteb;

import com.yxz.byteb.util.CusPathUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;

/**
 * @Description 插入新方法
 * @Date 2025-07-07
 * @Created by Yolo
 */
public class ByteBuddy03_InsertMethodTest {


    /**
     * .defineMethod(方法名, 方法返回值类型, 方法访问描述符): 定义新增的方法
     * .withParameters(Type...): 定义新增的方法对应的形参类型列表
     * .intercept(XXX): 和修改/增强现有方法一样，对前面的方法对象的方法体进行修改
     */

    /**
     * (12) redefine基础上, 增加新方法
     */
    @Test
    public void test12() throws IOException {
        DynamicType.Unloaded<NothingClass> redefine = new ByteBuddy()
                .redefine(NothingClass.class)
                // 定义方法的 方法名, 方法返回值类型, 方法访问修饰符
                .defineMethod("returnBlankString", String.class, Modifier.PUBLIC | Modifier.STATIC)
                // 定义方法的形参
                .withParameters(String.class, Integer.class)
                // 定义方法体内逻辑
                .intercept(FixedValue.value(""))
                .name("com.yxz.byteb.PassionTest12")
                .make();
        redefine.saveIn(new File(CusPathUtils.currentPath()));
    }

}

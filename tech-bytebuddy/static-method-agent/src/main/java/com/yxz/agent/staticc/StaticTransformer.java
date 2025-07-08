package com.yxz.agent.staticc;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

/**
 * @Desc 针对静态方法进行增强
 * 某个类被 AgentBuilder.type匹配，将要被加载时，进入 transform 方法
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class StaticTransformer implements AgentBuilder.Transformer {

    @Override
    public DynamicType.Builder<?> transform(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain) {

        return builder
                .method(ElementMatchers.isStatic())
                .intercept(MethodDelegation.to(new StaticInterceptor()));
    }
}

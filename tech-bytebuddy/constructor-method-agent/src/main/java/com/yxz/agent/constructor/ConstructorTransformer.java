package com.yxz.agent.constructor;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

/**
 * @Desc 构造方法增强
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class ConstructorTransformer implements AgentBuilder.Transformer {

    @Override
    public DynamicType.Builder<?> transform(
            DynamicType.Builder<?> builder,
            TypeDescription typeDescription,
            ClassLoader classLoader,
            JavaModule module,
            ProtectionDomain protectionDomain) {

        return builder
                .constructor(ElementMatchers.any())
                .intercept(SuperMethodCall.INSTANCE.andThen(MethodDelegation.to(new ConstructorInterceptor())));
    }
}

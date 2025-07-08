package com.yxz.agent.instance;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.*;

/**
 * 针对 实例 方法进行 修改/增强的 类转换器 <br/>
 * 某个类被 type 匹配, 将要被类加载时, 进入transform方法
 *
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class InstanceTransformer implements AgentBuilder.Transformer {

    /**
     * transform 转化逻辑
     *
     * @param builder          之前Byte Buddy 增强修改类时的中间产物Builder(比如 {@link ByteBuddy#subclass(Class)}的返回值就是) The dynamic builder to transform.
     * @param typeDescription  将被加载的类对应的类信息
     * @param classLoader      加载当前类的类加载器，如果是引导类加载器，则返回 null
     * @param module           加载当前类锁对应的模块，如果不支持则为null
     * @param protectionDomain The protection domain of the transformed type.
     * @return : A transformed version of the supplied builder.
     */
    @Override
    public DynamicType.Builder<?> transform(DynamicType.Builder<?> builder,
                                            TypeDescription typeDescription,
                                            ClassLoader classLoader,
                                            JavaModule module,
                                            ProtectionDomain protectionDomain) {
        return builder
                .method(not(isStatic()).and(isAnnotatedWith(nameStartsWith("com.yxz.agent.instance.Yo").and(nameEndsWith("Log")))))
                .intercept(MethodDelegation.to(new InstanceInterceptor()));
    }
}

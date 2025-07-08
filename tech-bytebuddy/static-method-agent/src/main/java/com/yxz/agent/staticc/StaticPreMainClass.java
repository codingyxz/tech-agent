package com.yxz.agent.staticc;

import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;

import java.lang.instrument.Instrumentation;

/**
 * @Desc agent入口，进行 java agent 插桩
 * @Date 2025-07-08
 * @Created by Yolo
 */
public class StaticPreMainClass {


    public static void premain(String arg, Instrumentation instrumentation) {

        System.out.println(" 执行 premain " + arg);
        // 相同效果，类名匹配 return ElementMatchers.named("com.yxz.agent.staticc.StaticUtils");
        new AgentBuilder.Default()
                // 使用我们自定义的匹配器指定拦截的类
                .type(getMatcher())
                .transform(new StaticTransformer())
                .installOn(instrumentation);


    }

    private static ElementMatcher<? super TypeDescription> getMatcher() {
        return new ElementMatcher.Junction.AbstractBase<TypeDescription>() {
            @Override
            public boolean matches(TypeDescription target) {
                // 当类名匹配时，进行拦截
                return target.getActualName().equals("com.yxz.agent.staticc.StaticUtils");
            }
        };
    }


}

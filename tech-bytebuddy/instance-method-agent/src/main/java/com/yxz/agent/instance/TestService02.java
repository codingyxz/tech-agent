package com.yxz.agent.instance;

import java.util.Arrays;

/**
 * @Desc 后续 进行 插桩增强的目标类 02
 * @Date 2025-07-08
 * @Created by Yolo
 */

@YoCallLog
@YoTestAnno
public class TestService02 {

    @YoCallLog
    public static String returnHello(String... noUseArgs) {
        return "Hello";
    }

    @YoCallLog
    public String returnArgs(String... args) {
        return Arrays.toString(args);
    }

    @YoTestAnno
    public String returnHi(String noUseArg) {
        return "Hi";
    }

}

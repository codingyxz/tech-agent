package com.yxz.agent.instance;

/**
 * @Desc 后续 进行 插桩增强的目标类 01
 * @Date 2025-07-08
 * @Created by Yolo
 */

@YoTestAnno
@YoCallLog
public class TestService01 {

    public int returnZero(int number01, int number02) {
        return 0;
    }

    @YoCallLog
    public int returnSeven(int... numbers) {
        return 7;
    }

}

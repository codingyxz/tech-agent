package com.yxz.agent.instance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 模拟其他 注解, 无实际作用
 * @Date 2025-07-08
 * @Created by Yolo
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
public @interface YoTestAnno {
}

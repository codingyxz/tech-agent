package com.yxz.byteb;

/**
 * @Description TODO
 * @Date 2025-07-07
 * @Created by Yolo
 */
public class SomethingClass {
    public SomethingClass() {
        System.out.println("SomethingClass()");
    }

    public String selectUserName(Long userId) {
        return String.valueOf(userId);
    }

    public void print() {
        System.out.println("print something");
    }

    public int getAge() {
        return Integer.MAX_VALUE;
    }

    public static void sayWhat(String whatToSay) {
        System.out.println("what to Say, say: " + whatToSay);
    }
}

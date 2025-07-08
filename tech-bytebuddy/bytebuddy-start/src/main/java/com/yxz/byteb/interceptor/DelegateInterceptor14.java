package com.yxz.byteb.interceptor;

/**
 * @Description 静态方法增强
 * @Date 2025-07-07
 * @Created by Yolo
 */
public class DelegateInterceptor14 {


    /**
     * 修改/增强 方法
     * 注意这里除了增加static访问修饰符，其他方法描述符信息和原方法(被修改/增强的目标方法)一致
     */
    public static String selectUserName(Long userId) {
        // 原方法逻辑 return String.valueOf(userId);
        return "DelegateInterceptor14.selectUserName, userId: " + userId;
    }


}

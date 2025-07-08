package com.yxz.byteb.interceptor;


/**
 * @Description 实例方法增强
 * @Date 2025-07-07
 * @Created by Yolo
 */
public class DelegateInterceptor15 {

    /**
     * 修改/增强  方法
     * 这里和 DelegateInterceptor14 主要不同点在于没有 static修饰, 是实例方法
     */
    public String selectUserName(Long userId) {
        // 原方法逻辑 return String.valueOf(userId);
        return "DelegateInterceptor15.selectUserName, userId: " + userId;
    }

}

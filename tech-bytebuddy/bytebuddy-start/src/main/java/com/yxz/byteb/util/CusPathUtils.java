package com.yxz.byteb.util;

import com.yxz.byteb.ByteBuddy01_CreateClassTest;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @Description TODO
 * @Date 2025-07-07
 * @Created by Yolo
 */
public class CusPathUtils {

    public static void main(String[] args) {
        // /Users/linkk/codes/freestyle/tech-agent/tech-bytebuddy/bytebuddy-start/target/classes
        System.out.println(CusPathUtils.currentPath());
    }

    public static String currentPath() {
        try {
            String path = new File(CusPathUtils.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI())
                    .getPath();
            return path;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to get current classpath", e);
        }
    }

    public static File currentPathFile() {
        return new File(currentPath());
    }

}

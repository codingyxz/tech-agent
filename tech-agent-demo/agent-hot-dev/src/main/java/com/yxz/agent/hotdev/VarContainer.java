package com.yxz.agent.hotdev;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description TODO
 * @Date 2025-04-01
 * @Created by Yolo
 */
public class VarContainer {

    // 目标程序编译路径
    public static final String PATH_PREFIX = "/Users/linkk/codes/freestyle/tech-agent/tech-agent-demo/agent-arthas/target/classes";
    // 待监听的包
    public static final String CLASS_NAME_PREFIX = "com/yxz/agent/arthas/service";

    public static Map<String, String> pathToName = new HashMap<>();
    public static Map<String, String> nameToPath = new HashMap<>();

}

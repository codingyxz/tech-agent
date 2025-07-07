package com.yxz.agent.arthas.service;

/**
 * @Desc TODO
 * @Author zhxy
 * @Version V1.0
 **/
public class HelloService {
    public void hello() {

        String msg = System.currentTimeMillis() + " : "
                + new SleepService().sleep() + " -- " + new WorkService().work();

        System.out.format("\33[36;1m %s%n", msg);

    }
}

package com.yxz.agent.attach;

import com.sun.tools.attach.*;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * @Description TODO
 * @Date 2025-04-01
 * @Created by Yolo
 */
public class AgentAttach {

    public static void main(String[] args) {

        // 查找所有jvm进程，排除attach测试工程
        List<VirtualMachineDescriptor> attach = VirtualMachine.list()
                .stream()
                .filter(jvm -> !jvm.displayName().equals("com.yxz.agent.attach.AgentAttach"))
                .collect(Collectors.toList());

        for (int i = 0; i < attach.size(); i++) {
            System.out.println("[" + i + "]" + attach.get(i).displayName() + ":" + attach.get(i).id());
        }
        System.out.println("请输入需要attach的pid编号：");
        Scanner scanner = new Scanner(System.in);
        String num = scanner.nextLine();
        VirtualMachineDescriptor virtualMachineDescriptor = attach.get(Integer.valueOf(num));

        try {
            VirtualMachine virtualMachine = VirtualMachine.attach(virtualMachineDescriptor.id());
            virtualMachine.loadAgent("/Users/linkk/codes/freestyle/tech-road/tech-agent-demo/agent-base/target/agent-base-1.0-SNAPSHOT-jar-with-dependencies.jar");
            virtualMachine.detach();
        } catch (AttachNotSupportedException e) {
            System.out.println("AttachNotSupportedException :" + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException :" + e.getMessage());
        } catch (AgentLoadException e) {
            System.out.println("AgentLoadException :" + e.getMessage());
        } catch (AgentInitializationException e) {
            System.out.println("AgentInitializationException :" + e.getMessage());
        }
    }

}

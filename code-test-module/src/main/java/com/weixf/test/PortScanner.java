package com.weixf.test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PortScanner {

    public static void main(String[] args) {

        // 目标IP地址
        String targetIP = "10.2.0.88";
        int startPort = 1;
        int endPort = 3000;

        // 使用线程池加速检测（8线程）
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);

        for (int port = startPort; port <= endPort; port++) {
            final int finalPort = port;
            System.out.println("正在处理: " + finalPort);
            completionService.submit(() -> checkPort(targetIP, finalPort));
        }

        executor.shutdown();

        while (!executor.isTerminated()) {
            try {
                Integer port = completionService.take().get();
                if (port != null) {
                    System.out.println("开放端口：" + port);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    private static Integer checkPort(String ip, int port) {
        try (Socket socket = new Socket()) {
            // 1秒超时
            socket.connect(new InetSocketAddress(ip, port), 1000);
            return port;
        } catch (IOException e) {
            return null;
        }
    }

}

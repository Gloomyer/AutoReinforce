package com.gloomyer.auto.protection.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CompileTask {


    private enum Instance {
        INS;
        private CompileTask instance;

        Instance() {
            instance = new CompileTask();
        }
    }

    public static CompileTask get() {
        return Instance.INS.instance;
    }

    private int taskCount;
    private FileOutputStream fos;


    private CompileTask() {

    }

    public void init(File saveDir) {
        try {
            fos = new FileOutputStream(new File(saveDir, "result.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public synchronized void add() {
        this.taskCount++;
    }

    /**
     * 结果回调
     *
     * @param normalUrl     normalUrl
     * @param protectionUrl protectionUrl
     * @param file          file
     */
    public synchronized void compile(String normalUrl, String protectionUrl, File file) {
        //下载地址写出保存
        taskCount--;
        if (normalUrl != null && protectionUrl != null) {
            try {
                fos.write(String.format("加固包:%s \n\n普通包:%s\n\n",
                        protectionUrl,
                        normalUrl
                ).getBytes());
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (taskCount == 0) {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }
}

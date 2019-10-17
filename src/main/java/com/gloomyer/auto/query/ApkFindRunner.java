package com.gloomyer.auto.query;

import com.gloomyer.auto.runner.ReinForceRunner;
import com.gloomyer.auto.runner.Scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ApkFindRunner implements Runnable {
    private static boolean isRunner = true;
    private final File appDir;
    private List<String> keys;

    public static void start(File appDir) {
        isRunner = true;
        new Thread(new ApkFindRunner(appDir)).start();
    }

    public static void end() {
        isRunner = false;
    }

    private ApkFindRunner(File appDir) {
        this.appDir = appDir;
        keys = new ArrayList<>();
    }

    @Override
    public void run() {
        while (isRunner) {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            traversing(appDir);

        }
    }

    /**
     * 递归获取apk
     *
     * @param file
     */
    @SuppressWarnings("ConstantConditions")
    private void traversing(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    traversing(f);
                }
            }
        } else {
            if (file.getName().endsWith("apk")) {
                File parentFile = file.getParentFile();
                if (parentFile != null) {
                    if (parentFile.listFiles() != null) {
                        if (parentFile.listFiles().length > 1) {
                            //apk 生成完毕
                            if (!keys.contains(file.getAbsolutePath())) {
                                keys.add(file.getAbsolutePath());
                                Scheduler.get().addRunner(new ReinForceRunner(file.getAbsolutePath()));
                            }
                        }

                    }
                }

            }
        }
    }
}

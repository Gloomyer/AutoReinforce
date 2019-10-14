package com.gloomyer.auto.utils;

import com.gloomyer.auto.runner.ReinForceRunner;
import com.gloomyer.auto.runner.Scheduler;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

public class Utils {
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        try {
            return DigestUtils.md5Hex(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 找到app dir
     */
    @SuppressWarnings("DuplicatedCode")
    public static File findAppDir(String workFile) {
        File work = new File(workFile);
        File[] files = work.listFiles();
        if (files == null) {
            throw new RuntimeException("工作目录不正确!");
        }
        for (File file : files) {
            if (file.isDirectory()) {
                File[] childs = file.listFiles();
                if (childs != null) {
                    for (File child : childs) {
                        if ("build.gradle".equals(child.getName())) {
                            BufferedInputStream bis = null;
                            try {
                                bis = new BufferedInputStream(new FileInputStream(child));
                                byte[] buffer = new byte[2048];
                                int len;
                                StringBuilder sb = new StringBuilder();
                                while ((len = bis.read(buffer)) > 0) {
                                    sb.append(new String(buffer, 0, len));
                                }
                                String gradle = sb.toString();
                                if (gradle.contains("applicationId")
                                        && !gradle.contains("runAsApp")) {
                                    Log.e("app 目录为:{0}", file.getAbsolutePath());
                                    return file;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                if (bis != null) {
                                    try {
                                        bis.close();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 生成apk包
     *
     * @param appDir
     */
    public static void createApkFile(File appDir) {
        long startTime = System.currentTimeMillis();

        String gradlew = ShellExecute.isUnix() ? "./gradlew " : "gradlew.bat ";

        ShellExecute.exec(gradlew + "clean");
        Log.e("项目清理完成.");
        //切换
        String cmd = gradlew + appDir.getName() + ":assembleRelease";
        Log.e("生成包命令:" + cmd);
        ShellExecute.exec(cmd);
        Log.e("生产环境包生产 耗时:{0}毫秒", System.currentTimeMillis() - startTime);
    }

}

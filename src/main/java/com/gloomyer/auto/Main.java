package com.gloomyer.auto;

import com.gloomyer.auto.config.Config;
import com.gloomyer.auto.utils.ExecLinux;
import com.gloomyer.auto.utils.Log;
import com.gloomyer.auto.utils.Utils;
import com.google.gson.Gson;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import jnr.posix.util.DefaultPOSIXHandler;

import java.io.*;

public class Main {
    public static long START_TIME;

    public static void main(String[] args) throws IOException {
        START_TIME = System.currentTimeMillis();
        readConfig();
        File file = new File(Config.get().getProjectPath());
        if (file.exists()) {
            //修改工作目录
            POSIX posix = POSIXFactory.getPOSIX(new DefaultPOSIXHandler(), true);
            posix.chdir(Config.get().getProjectPath());
            ExecLinux.exec("pwd");

            //找到app目录
            File appDir = Utils.findAppDir(Config.get().getProjectPath());
            //生成apk文件
            //Utils.createApkFile(appDir);
            //开始遍历app目录 找apk
            Utils.traversing(appDir);
        } else {
            Log.e("启动路径不存在!");
        }

    }

    @SuppressWarnings("all")
    private static void readConfig() {
        File config = new File("config.json");
        if (!config.exists()) {
            throw new RuntimeException("文件不存在！");
        }
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(config));
            byte[] buffer = new byte[2048];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = bis.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, len));
            }
            String cfg = sb.toString();
            Config.set(new Gson().fromJson(cfg, Config.class));
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

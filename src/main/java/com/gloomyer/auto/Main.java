package com.gloomyer.auto;

import com.gloomyer.auto.config.Config;
import com.gloomyer.auto.utils.Log;
import com.gloomyer.auto.utils.ShellExecute;
import com.gloomyer.auto.utils.Utils;
import com.google.gson.Gson;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import jnr.posix.util.DefaultPOSIXHandler;

import java.io.*;
import java.text.MessageFormat;

public class Main {
    public static long START_TIME;

    public static void main(String[] args) throws IOException {
        START_TIME = System.currentTimeMillis();
        readConfig();
        Config config = Config.getDefault();
        File saveDir = new File(config.getDir().getProjectSavePath());
        if (!saveDir.exists() || saveDir.isFile()) {
            saveDir.mkdirs();
        }

        POSIX posix = POSIXFactory.getPOSIX(new DefaultPOSIXHandler(), true);
        posix.chdir(saveDir.getAbsolutePath());
        Log.e("切换目录至:{0}", new File("").getAbsolutePath());

        //切换分支
        ShellExecute.exec(MessageFormat.format("git checkout -b {0}",
                config.getDir().getBuildBranch()));
        //绑定远程分支
        ShellExecute.exec(MessageFormat.format("git branch --set-upstream-to=origin/{0} {0}",
                config.getDir().getBuildBranch()));
        ShellExecute.exec("git pull");//拉取最新的代码

        //找到主app目录
        File appDir = Utils.findAppDir(config.getDir().getProjectSavePath());

        if (appDir != null) {
            //生成apk文件
            //Utils.createApkFile(appDir);

            //开始遍历app目录 找apk
            Utils.traversing(appDir);
        } else {
            throw new RuntimeException("主app model 没有找到！");
        }


    }

    /**
     * 读取配置文件
     */
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
            Config.setDefault(new Gson().fromJson(cfg, Config.class));
        } catch (Exception e) {
            throw new RuntimeException("配置文件错误！" + e.toString());
        }
    }
}

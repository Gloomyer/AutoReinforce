package com.gloomyer.auto.init;

import com.gloomyer.auto.config.Config;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class ReadConfigJson implements Runnable {

    public ReadConfigJson() {
        new Thread(this).start();
    }

    @Override
    public void run() {
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
            System.out.println("default config:" + Config.getDefault());
        } catch (Exception e) {
            throw new RuntimeException("配置文件错误！" + e.toString());
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bis = null;
            }
        }
    }
}

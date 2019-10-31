package com.gloomyer.auto.task;

import com.gloomyer.auto.utils.ShellExecute;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class OpenWeb implements Runnable {

    @Value(value = "${server.port}")
    private int port;

    public OpenWeb() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            String url = "http://127.0.0.1:" + port;
            if (ShellExecute.isUnix()) {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[]{String.class});
                openURL.invoke(null, new Object[]{url});
            } else {
                Runtime.getRuntime().exec("explorer  " + url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

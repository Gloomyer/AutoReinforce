package com.gloomyer.auto.utils;

import com.gloomyer.auto.config.Config;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;

public class HttpUtils {
    public static boolean download(final String url, File saveFile) {
        final long startTime = System.currentTimeMillis();
        Request request = new Request.Builder().url(url).build();
        OkHttpClient client = new OkHttpClient();
        Response response = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(new FileOutputStream(saveFile));
            byte[] buffer = new byte[2048];
            int len;
            while ((len = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
                bos.flush();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }
}

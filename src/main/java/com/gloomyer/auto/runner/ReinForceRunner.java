package com.gloomyer.auto.runner;

import com.gloomyer.auto.Main;
import com.gloomyer.auto.config.ApkUrlConfig;
import com.gloomyer.auto.config.Config;
import com.gloomyer.auto.domain.ApkInfo;
import com.gloomyer.auto.query.IQuery;
import com.gloomyer.auto.query.LGQuery;
import com.gloomyer.auto.reinforce.IReinforce;
import com.gloomyer.auto.reinforce.Legu;
import com.gloomyer.auto.upload.IUpload;
import com.gloomyer.auto.upload.QiniuUpload;
import com.gloomyer.auto.utils.*;
import com.qiniu.util.Json;
import com.qiniu.util.StringMap;

import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 加固任务
 */
public class ReinForceRunner implements Runnable {
    private final String apkPath;
    private final File apkFile;
    private String mapKey;

    public ReinForceRunner(String apkPath) {
        this.apkPath = apkPath;
        this.apkFile = new File(apkPath);
        this.mapKey = apkFile.getName().split("@")[0];
    }


    public void run() {
        ApkInfo apkInfo = ApkUtils.read2Legu(apkPath);
        if (apkInfo != null) {
            //循环上传apk
            do {
                if (uploadinng(apkFile) != null)
                    apkInfo.setAppUrl(Config.getDefault().getUpload().getQNStartUrl() + apkFile.getName());
            } while (apkInfo.getAppUrl() == null);
            //加入url map
            ApkUrlConfig.get().put(mapKey, false, apkInfo.getAppUrl());
            Log.i("apkInfo:{0}", apkInfo.toString());

            //加固
            String info = reinforce(apkInfo);

            //查询加固结果
            String apkUrl = query(info);

            //下载加固好的apk
            File file = saveApk(apkInfo, apkUrl);

            //签名
            if (file != null) {
                file = sign(file);
            }

            if (file != null) {
                if (uploadinng(file) != null) {
                    ApkUrlConfig.get().put(mapKey, true, Config.getDefault().getUpload().getQNStartUrl() + file.getName());
                }

            }
        }
        compile();
    }

    /**
     * 完成
     */
    private void compile() {
        Scheduler.get().del();

        if (Scheduler.get().count() == 0) {
            //结束了
            BufferedOutputStream bos = null;
            File optFile = new File(Config.getDefault().getDir().getOutputDir());
            File file = new File(optFile, "result.txt");

            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                HashMap<String, String> map = ApkUrlConfig.get().getMap();
                Set<Map.Entry<String, String>> entries = map.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    bos.write(MessageFormat.format("{0}:{1}\n\n", entry.getKey(), entry.getValue()).getBytes());
                    QRUtils.createQrImg(
                            500, 500,
                            entry.getValue(),
                            new File(optFile, entry.getKey() + ".jpg")
                    );
                    bos.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.e("总耗时:{0}秒", (System.currentTimeMillis() - Main.START_TIME) / 1000);
        }
    }

    //签名
    private File sign(File file) {
        String retFile = file.getAbsolutePath().replace(".apk", ".signed.apk");
        String cmd = MessageFormat.format(
                "{0} sign -ks {1} --ks-key-alias {2} --ks-pass pass:{3} --key-pass pass:{4} --out {5} {6}",
                Config.getDefault().getSigner().getApksignerPath(),
                Config.getDefault().getSigner().getStoreFile(),
                Config.getDefault().getSigner().getKeyAlias(),
                Config.getDefault().getSigner().getKeyPassword(),
                Config.getDefault().getSigner().getStorePassword(),
                retFile,
                file.getAbsolutePath()
        );
        Log.e("sign cmd:{0}", cmd);
        ShellExecute.exec(cmd);
        file.delete();
        return new File(retFile);
    }

    //下载加固好的apk
    private File saveApk(ApkInfo apkInfo, String apkUrl) {
        File saveFile = new File(Config.getDefault().getDir().getOutputDir());
        if (!saveFile.exists() || !saveFile.isDirectory()) {
            saveFile.mkdirs();
        }

        saveFile = new File(saveFile, apkInfo.getFileName().replace(".apk", ".legu.apk"));
        if (apkUrl != null) {
            if (HttpUtils.download(apkUrl, saveFile)) {
                Log.e("下载成功:{0}", saveFile.getAbsolutePath());
                return saveFile;
            }
        }

        return null;
    }

    //查询加固结果
    private String query(String info) {
        StringMap map = Json.decode(info);
        IQuery query;
        if ("legu".equals(Config.getDefault().getReinforce().getReinforceMethod())) {
            query = new LGQuery();
        } else {
            throw new RuntimeException("加固方式未配置！");
        }

        return query.query(map);
    }

    private String reinforce(ApkInfo apkInfo) {
        IReinforce reinforce;
        if ("legu".equals(Config.getDefault().getReinforce().getReinforceMethod())) {
            reinforce = Legu.get();
        } else {
            throw new RuntimeException("加固方式未配置！");
        }

        String info;
        try {
            info = reinforce.reinforce(apkInfo);
            Log.e(info);
        } catch (Exception e) {
            throw new RuntimeException(e.toString());
        }

        return info;
    }

    /**
     * 上传apk
     *
     * @param apkFile
     * @return
     */
    private String uploadinng(File apkFile) {
        IUpload upload;
        if ("qiniu".equals(Config.getDefault().getUpload().getUploadMethod())) {
            upload = QiniuUpload.get();
        } else {
            throw new RuntimeException("上传方式未配置！");
        }

        return upload.upload(apkFile);
    }
}

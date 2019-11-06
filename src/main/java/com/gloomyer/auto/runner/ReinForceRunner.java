package com.gloomyer.auto.runner;

import com.gloomyer.auto.domain.ApkInfo;
import com.gloomyer.auto.query.IQuery;

import java.io.*;

/**
 * 加固任务
 */
public class ReinForceRunner implements Runnable {
    private final String apkPath;
    private final File apkFile;
    private String mapKey;
    private IQuery query;

    public ReinForceRunner(String apkPath) {
        this.apkPath = apkPath;
        this.apkFile = new File(apkPath);
        this.mapKey = apkFile.getName().split("@")[0];
    }


    public void run() {
//        ApkInfo apkInfo = ApkUtils.read2Legu(apkPath);
//        if (apkInfo != null) {
//            //循环上传apk
//            do {
//                if (uploadinng(apkFile) != null)
//                    apkInfo.setAppUrl(Config.getDefault().getUpload().getStartUrl() + apkFile.getName());
//            } while (apkInfo.getAppUrl() == null);
//            //加入url map
//            ApkUrlConfig.get().put(mapKey, false, apkInfo.getAppUrl());
//            LG.i("apkInfo:{0}", apkInfo.toString());
//
//
//            //查询加固结果
//            String info;
//            String apkUrl;
//            do {
//                //加固
//                info = reinforce(apkInfo);
//                apkUrl = query(info);
//            } while ("-1".equals(apkUrl));
//
//
//            //下载加固好的apk
//            File file = saveApk(apkInfo, apkUrl);
//
//            //删除任务
//            if (query != null) {
//                StringMap map = Json.decode(info);
//                query.delete(map);
//            }
//
//            //签名
//            if (file != null) {
//                file = sign(file);
//            }
//
//            if (file != null) {
//                if (uploadinng(file) != null) {
//                    ApkUrlConfig.get().put(mapKey, true, Config.getDefault().getUpload().getStartUrl() + file.getName());
//                }
//
//            }
//        }
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
            //File optFile = new File(Config.getDefault().getDir().getOutputDir());
            //File file = new File(optFile, "result.txt");

            try {
                //bos = new BufferedOutputStream(new FileOutputStream(file));
                //HashMap<String, String> map = ApkUrlConfig.get().getMap();
                //Set<Map.Entry<String, String>> entries = map.entrySet();
//                for (Map.Entry<String, String> entry : entries) {
//                    bos.write(MessageFormat.format("{0}:{1}\n\n", entry.getKey(), entry.getValue()).getBytes());
//                    QRUtils.createQrImg(
//                            500, 500,
//                            entry.getValue(),
//                            new File(optFile, entry.getKey() + ".jpg")
//                    );
//                    bos.flush();
//                }

            } catch (Exception e) {
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
            doZip();
            //LG.e("总耗时:{0}秒", (System.currentTimeMillis() - Main.START_TIME) / 1000);
            System.exit(0);
        }
    }

    /**
     * 打压缩包
     */
    private void doZip() {

    }

    //签名
    private File sign(File file) {
//        String retFile = file.getAbsolutePath().replace(".apk", ".signed.apk");
//        String cmd = MessageFormat.format(
//                "{0} sign -ks {1} --ks-key-alias {2} --ks-pass pass:{3} --key-pass pass:{4} --out {5} {6}",
//                Config.getDefault().getSigner().getApksignerPath(),
//                Config.getDefault().getSigner().getStoreFile(),
//                Config.getDefault().getSigner().getKeyAlias(),
//                Config.getDefault().getSigner().getKeyPassword(),
//                Config.getDefault().getSigner().getStorePassword(),
//                retFile,
//                file.getAbsolutePath()
//        );
//        LG.e("sign cmd:{0}", cmd);
//        ShellExecute.exec(cmd);
//        file.delete();
//        return new File(retFile);
        return null;
    }

    //下载加固好的apk
    private File saveApk(ApkInfo apkInfo, String apkUrl) {
//        File saveFile = new File(Config.getDefault().getDir().getOutputDir());
//        if (!saveFile.exists() || !saveFile.isDirectory()) {
//            saveFile.mkdirs();
//        }
//
//        saveFile = new File(saveFile, apkInfo.getFileName().replace(".apk", ".legu.apk"));
//        if (apkUrl != null) {
//            if (HttpUtils.download(apkUrl, saveFile)) {
//                LG.e("下载成功:{0}", saveFile.getAbsolutePath());
//                return saveFile;
//            }
//        }

        return null;
    }

    //查询加固结果
    private String query(String info) {
//        StringMap map = Json.decode(info);
//        if ("legu".equals(Config.getDefault().getReinforce().getReinforceMethod())) {
//            query = new LGQuery();
//        } else {
//            throw new RuntimeException("加固方式未配置！");
//        }
//
//        try {
//            return query.query(map);
//        } catch (JGError error) {
//            LG.e(getClass().getSimpleName(), "加固失败！");
//            //query.delete(map);
//            //重新走上传
//            return "-1";
//        }
        return "";
    }

    private String reinforce(ApkInfo apkInfo) {
//        IReinforce reinforce;
//        if ("legu".equals(Config.getDefault().getReinforce().getReinforceMethod())) {
//            reinforce = Legu.get();
//        } else {
//            throw new RuntimeException("加固方式未配置！");
//        }
//
//        String info;
//        try {
//            info = reinforce.reinforce(apkInfo);
//            LG.e(info);
//        } catch (Exception e) {
//            throw new RuntimeException(e.toString());
//        }
//
//        return info;
        return null;
    }

    /**
     * 上传apk
     *
     * @param apkFile
     * @return
     */
    private String uploadinng(File apkFile) {
//        IUpload upload;
//        if ("qiniu".equals(Config.getDefault().getUpload().getUploadMethod())) {
//            upload = QiniuUpload.get();
//        } else if ("ali".equals(Config.getDefault().getUpload().getUploadMethod())) {
//            upload = AliUpload.get();
//        } else {
//            throw new RuntimeException("上传方式未配置！");
//        }
//
//        return upload.upload(apkFile);
        return "";
    }
}

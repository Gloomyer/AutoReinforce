package com.gloomyer.auto.protection.impl.qh;

import com.aliyun.oss.common.utils.LogUtils;
import com.gloomyer.auto.protection.Protection;
import com.gloomyer.auto.protection.ProtectionCache;
import com.gloomyer.auto.protection.impl.CompileTask;
import com.gloomyer.auto.upload.Upload;
import com.gloomyer.auto.upload.UploadCache;
import com.gloomyer.auto.upload.UploadFactory;
import com.gloomyer.auto.utils.FileUtils;
import com.gloomyer.auto.utils.LG;
import com.gloomyer.auto.utils.ShellExecute;
import com.gloomyer.auto.utils.SignUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

public class QihuProtectionImpl implements Protection {

    private File jar;
    private Upload upload;

    @Override
    public void protection(String uploadMethod, String uploadAccessKey,
                           String uploadSecretKey, String uploadBucketName,
                           String uploadUrlPrefix, String protectionSecretId,
                           String protectionSecretKey, int protectionMaxTaskSize,
                           String saveDir) {
        jar = new File("qihoo");
        jar = new File(jar, "jiagu.jar");
        LG.e("{0}", jar.getAbsolutePath());
        UploadCache.uploadAccessKey = uploadAccessKey;
        UploadCache.uploadSecretKey = uploadSecretKey;
        UploadCache.uploadBucketName = uploadBucketName;
        UploadCache.uploadUrlPrefix = uploadUrlPrefix;
        upload = UploadFactory.create(uploadMethod);

        login();
        File dir = new File(saveDir);
        File[] files = dir.listFiles();
        assert files != null;
        CompileTask.get().init(dir);
        CompileTask.get().add();
        for (File file : files) {
            if (file.getName().endsWith(".apk")) {
                String normalApkUrl = upload(file);
                protection(normalApkUrl, file, dir);
            }
        }
        CompileTask.get().compile(null, null, null);
    }

    /**
     * 保存文件 上传一下
     *
     * @param file file
     * @return return
     */
    private String upload(File file) {
        return upload.upload(file);
    }

    /**
     * 加固
     *
     * @param normalApkUrl normalApkUrl
     * @param file         file
     * @param dir          dir
     */
    private void protection(String normalApkUrl, File file, File dir) {
        ShellExecute.exec(MessageFormat.format(
                "java -jar {0} -jiagu {1} {2}",
                jar.getAbsolutePath(),
                file.getAbsolutePath(),
                dir
        ));

        //遍历获取加固后的
        File[] files = dir.listFiles();
        assert files != null;
        for (File f : files) {
            if (f.getName().endsWith("jiagu.apk")
                    && f.getName().contains(file.getName().replace(".apk", ""))) {
                //签名
                File ret = sign(f, file.getName());
                String protectionUrl = upload(ret);
                CompileTask.get().compile(normalApkUrl, protectionUrl, ret);
                break;
            }
        }
    }

    /**
     * 对apk文件进行签名
     *
     * @param file    file
     * @param oldName oldName
     * @return return
     */
    private File sign(File file, String oldName) {
        try {
            File unsignApk = new File(
                    file.getParent(),
                    oldName.replace(".apk", ".360.unsign.apk"));
            FileUtils.copy(file, unsignApk);
            file.delete();

            SignUtils.sign(unsignApk);

            File signedFile = new File(file.getParent(),
                    unsignApk.getName().replace(".360.unsign.apk", ".360.signed.apk"));
            FileUtils.copy(unsignApk, signedFile);
            unsignApk.delete();

            return signedFile;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    private void login() {
        String cmd = MessageFormat.format(
                "java -jar {0} -login {1} {2}",
                jar.getAbsolutePath(),
                ProtectionCache.qihooUserName,
                ProtectionCache.qihooPassword
        );
        ShellExecute.exec(cmd);
        ShellExecute.exec(MessageFormat.format(
                "java -jar {0} -config -",
                jar.getAbsolutePath()
        ));
    }
}

package com.gloomyer.auto.protection.impl.lg;

import com.gloomyer.auto.protection.Protection;
import com.gloomyer.auto.protection.impl.CompileTask;
import com.gloomyer.auto.upload.Upload;
import com.gloomyer.auto.upload.UploadCache;
import com.gloomyer.auto.upload.UploadFactory;
import com.gloomyer.auto.utils.LG;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LGProtectionImpl implements Protection {
    private String uploadMethod;
    private String uploadAccessKey;
    private String uploadSecretKey;
    private String uploadBucketName;
    private String uploadUrlPrefix;
    private String protectionSecretId;
    private String protectionSecretKey;
    private File saveDir;
    private List<LGProtectionTask> LGProtectionTasks;
    private ThreadPoolExecutor mPoolExecutor;

    @Override
    public void protection(String uploadMethod,
                           String uploadAccessKey, String uploadSecretKey,
                           String uploadBucketName, String uploadUrlPrefix,
                           String protectionSecretId, String protectionSecretKey,
                           int protectionMaxTaskSize, String saveDir) {
        this.uploadMethod = uploadMethod;
        this.uploadAccessKey = uploadAccessKey;
        this.uploadSecretKey = uploadSecretKey;
        this.uploadBucketName = uploadBucketName;
        this.uploadUrlPrefix = uploadUrlPrefix;
        this.protectionSecretId = protectionSecretId;
        this.protectionSecretKey = protectionSecretKey;
        this.saveDir = new File(saveDir);
        this.LGProtectionTasks = new ArrayList<>();
        mPoolExecutor = new ThreadPoolExecutor(
                protectionMaxTaskSize,
                protectionMaxTaskSize,
                15, TimeUnit.MINUTES,
                new LinkedBlockingDeque<>());
        //先上传
        upload();
        //开始加固
        protection();
    }

    /**
     * 加固逻辑
     */
    private void protection() {
        CompileTask.get().init(saveDir);
        for (LGProtectionTask task : LGProtectionTasks) {
            CompileTask.get().add();
            mPoolExecutor.execute(task);
        }
    }

    private void upload() {
        Upload upload = UploadFactory.create(uploadMethod);
        if (upload == null) throw new RuntimeException("不支持的上传方式");
        File[] files = saveDir.listFiles();
        //开始走文件上传逻辑
        if (files == null) throw new RuntimeException("输出目录不包含apk文件");
        LG.e("开始文件上传至云存储!");

        //设置上传参数
        UploadCache.uploadAccessKey = uploadAccessKey;
        UploadCache.uploadSecretKey = uploadSecretKey;
        UploadCache.uploadBucketName = uploadBucketName;
        UploadCache.uploadUrlPrefix = uploadUrlPrefix;

        long start = System.currentTimeMillis();
        int count = 0;
        for (File file : files) {
            if (file.isFile() || file.getName().endsWith(".apk")) {
                count++;
                String fileUrl = upload.upload(file);
                LGProtectionTasks.add(new LGProtectionTask(
                        file,
                        fileUrl,
                        protectionSecretId,
                        protectionSecretKey
                ));
            }
        }
        long end = System.currentTimeMillis();
        LG.e("{0}个文件上传完成，耗时:{1}秒", count, (end - start) / 1000);
        int i = 0;
        for (LGProtectionTask task : LGProtectionTasks) {
            LG.e("已上传文件URL,{0}:{1}", i++, task.url);
        }
    }

    private class Co {

    }
}

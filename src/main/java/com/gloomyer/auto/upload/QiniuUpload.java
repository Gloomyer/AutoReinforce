package com.gloomyer.auto.upload;

import com.gloomyer.auto.utils.LG;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.File;

public class QiniuUpload implements IUpload {

    private static QiniuUpload ins = new QiniuUpload();

    public static QiniuUpload get() {
        return ins;
    }

    private static Configuration cfg;
    private static UploadManager uploadManager;

    @Override
    public String upload(File file) {
//              if (Config.getDefault().QNIsEmpty()) {
//            throw new RuntimeException("七牛参数未配置!");
//        }
//
//        if (cfg == null) {
//            cfg = new Configuration(Region.autoRegion());
//            uploadManager = new UploadManager(cfg);
//        }
//
//        String accessKey = Config.getDefault().getUpload().getQNAccessKey();
//        String secretKey = Config.getDefault().getUpload().getQNSecretKey();
//        String bucket = Config.getDefault().getUpload().getQNBucket();
//        Auth auth = Auth.create(accessKey, secretKey);
//
//        String upToken = auth.uploadToken(bucket);
//        LG.i("七牛上传token:{0}", upToken);
//
//        try {
//            Response response = uploadManager.put(file.getAbsolutePath(), file.getName(), upToken);
//            return response.bodyString();
//        } catch (QiniuException e) {
//            e.printStackTrace();
//        }

        return null;
    }

}

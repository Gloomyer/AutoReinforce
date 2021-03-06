package com.gloomyer.auto.upload;

import com.aliyun.oss.OSS;

import java.io.File;

public class AliUpload {

    private static AliUpload ins = new AliUpload();

    public static AliUpload get() {
        return ins;
    }

    public String upload(File file) {
        OSS ossClient = null;
        try {
//            String accessKey = Config.getDefault().getUpload().getALAccessKey();
//            String secretKey = Config.getDefault().getUpload().getALSecretKey();
//            String endpoint = Config.getDefault().getUpload().getALEndpoint();
//            String bucket = Config.getDefault().getUpload().getQNBucket();

//            ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
//            PutObjectResult result = ossClient.putObject(bucket, file.getName(), file);
//            return MessageFormat.format(
//                    "ClientCRC:{0},getServerCRC:{1},ETag:{2},VersionId:{3}",
//                    result.getClientCRC(),
//                    result.getServerCRC(),
//                    result.getETag(),
//                    result.getVersionId()
//            );
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭OSSClient。
            if (ossClient != null)
                ossClient.shutdown();
        }
        return null;
    }
}

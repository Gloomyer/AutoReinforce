package com.gloomyer.auto.upload;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.gloomyer.auto.config.Config;

import java.io.File;
import java.text.MessageFormat;

public class AliUpload implements IUpload {

    private static AliUpload ins = new AliUpload();

    public static AliUpload get() {
        return ins;
    }

    @Override
    public String upload(File file) {
        OSS ossClient = null;
        try {
            String accessKey = Config.getDefault().getUpload().getALAccessKey();
            String secretKey = Config.getDefault().getUpload().getALSecretKey();
            String endpoint = Config.getDefault().getUpload().getALEndpoint();
            String bucket = Config.getDefault().getUpload().getQNBucket();

            ossClient = new OSSClientBuilder().build(endpoint, accessKey, secretKey);
            PutObjectResult result = ossClient.putObject(bucket, file.getName(), file);
            return MessageFormat.format(
                    "ClientCRC:{0},getServerCRC:{1},ETag:{2},VersionId:{3}",
                    result.getClientCRC(),
                    result.getServerCRC(),
                    result.getETag(),
                    result.getVersionId()
            );
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

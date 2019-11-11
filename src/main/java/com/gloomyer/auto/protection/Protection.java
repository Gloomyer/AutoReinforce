package com.gloomyer.auto.protection;

public interface Protection {
    /**
     * 执行加固逻辑
     *
     * @param uploadMethod          上传方式
     * @param uploadAccessKey       上传key
     * @param uploadSecretKey       上传secrekey
     * @param uploadBucketName      上传bucket
     * @param uploadUrlPrefix       上传url 前缀
     * @param protectionSecretId    加固secretID
     * @param protectionSecretKey   加固 secretKey
     * @param protectionMaxTaskSize 同时加固的最大任务数量
     * @param saveDir               apk保存目录
     */
    void protection(String uploadMethod, String uploadAccessKey, String uploadSecretKey, String uploadBucketName, String uploadUrlPrefix, String protectionSecretId, String protectionSecretKey, int protectionMaxTaskSize, String saveDir);
}

package com.gloomyer.auto.interfaces;

public interface StarterParams {
    String KEY_ACTION = "-A"; //方式
    String KEY_MODEL = "-M"; //模式 0:debug,1:release 默认0
    String KEY_CHANNEL = "-C"; //渠道配置
    String KEY_SAVE_PATH = "-S"; //保存目录
    String KEY_PROJECT_PATH = "-PP"; //项目目录
    String KEY_PROJECT_BRANCH = "-PB"; //项目打包分支
    String KEY_REPLACE_TEXT_VALUE = "-RTV"; //准备替换的字符串值
    String KEY_SIGN_CMD = "-SCMD"; //签名工具全路径
    String KEY_SIGN_STORE_FILE = "-SSF"; //签名全路径
    String KEY_SIGN_STORE_PASSWORD = "-SSP"; //签名文件密码
    String KEY_SIGN_KEY_ALIAS = "-SKA"; //签名文件别名
    String KEY_SIGN_KEY_PASSWORD = "-SKP"; //签名别名密码
    String KEY_PGY_API_KEY = "-PAK"; //蒲公英ApiKey
    String KEY_UPLOAD_METHOD = "-UM"; //上传 AccessKey 加固使用
    String KEY_UPLOAD_ACCESS_KEY = "-UAK"; //上传 AccessKey 加固使用
    String KEY_UPLOAD_SECRET_KEY = "-USK"; //上传 SecretKey 加固使用
    String KEY_UPLOAD_BUCKET_NAME = "-UBN"; //上传 BucketName 加固使用
    String KEY_UPLOAD_URL_PREFIX = "-UUP"; //上传 BucketName 加固使用
    String KEY_PROTECTION_SECRETID = "-PSI"; //上传 UrlPrefix 加固使用
    String KEY_PROTECTION_SECRETKEY = "-PSK"; //上传 UrlPrefix 加固使用
}

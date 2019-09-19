package com.gloomyer.auto.config;

import com.qiniu.util.StringUtils;

public class Config {
    private static Config ins;

    public static Config set(Config config) {
        return ins = config;
    }

    public static Config get() {
        return ins;
    }

    private String projectPath; //项目路径
    private String uploadMethod; //上传方式
    private String QNAccessKey; //七牛
    private String QNSecretKey; //七牛
    private String QNBucket; //七牛
    private String QNStartUrl; //七牛
    private String reinforceMethod; //加固方式
    private String LGSecretId; //乐固
    private String LGSecretKey; //乐固
    private String saveDir; //保存目录
    private String apksignerPath; //apksignerPath
    private String storeFile; //storeFile
    private String keyAlias; //keyAlias
    private String keyPassword; //keyPassword
    private String storePassword; //storePassword

    public static Config getIns() {
        return ins;
    }

    public static void setIns(Config ins) {
        Config.ins = ins;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getUploadMethod() {
        return uploadMethod;
    }

    public void setUploadMethod(String uploadMethod) {
        this.uploadMethod = uploadMethod;
    }

    public String getQNAccessKey() {
        return QNAccessKey;
    }

    public void setQNAccessKey(String QNAccessKey) {
        this.QNAccessKey = QNAccessKey;
    }

    public String getQNSecretKey() {
        return QNSecretKey;
    }

    public void setQNSecretKey(String QNSecretKey) {
        this.QNSecretKey = QNSecretKey;
    }

    public String getQNBucket() {
        return QNBucket;
    }

    public void setQNBucket(String QNBucket) {
        this.QNBucket = QNBucket;
    }

    public String getQNStartUrl() {
        return QNStartUrl;
    }

    public void setQNStartUrl(String QNStartUrl) {
        this.QNStartUrl = QNStartUrl;
    }

    public String getReinforceMethod() {
        return reinforceMethod;
    }

    public void setReinforceMethod(String reinforceMethod) {
        this.reinforceMethod = reinforceMethod;
    }

    public String getLGSecretId() {
        return LGSecretId;
    }

    public void setLGSecretId(String LGSecretId) {
        this.LGSecretId = LGSecretId;
    }

    public String getLGSecretKey() {
        return LGSecretKey;
    }

    public void setLGSecretKey(String LGSecretKey) {
        this.LGSecretKey = LGSecretKey;
    }

    public String getSaveDir() {
        return saveDir;
    }

    public void setSaveDir(String saveDir) {
        this.saveDir = saveDir;
    }

    public String getApksignerPath() {
        return apksignerPath;
    }

    public void setApksignerPath(String apksignerPath) {
        this.apksignerPath = apksignerPath;
    }

    public String getStoreFile() {
        return storeFile;
    }

    public void setStoreFile(String storeFile) {
        this.storeFile = storeFile;
    }

    public String getKeyAlias() {
        return keyAlias;
    }

    public void setKeyAlias(String keyAlias) {
        this.keyAlias = keyAlias;
    }

    public String getKeyPassword() {
        return keyPassword;
    }

    public void setKeyPassword(String keyPassword) {
        this.keyPassword = keyPassword;
    }

    public String getStorePassword() {
        return storePassword;
    }

    public void setStorePassword(String storePassword) {
        this.storePassword = storePassword;
    }

    /**
     * 七牛参数未配置
     *
     * @return 是否配置
     */
    public boolean QNIsEmpty() {
        return StringUtils.isNullOrEmpty(QNAccessKey)
                || StringUtils.isNullOrEmpty(QNSecretKey)
                || StringUtils.isNullOrEmpty(QNBucket)
                || StringUtils.isNullOrEmpty(QNStartUrl);
    }
}

package com.gloomyer.auto.config;

import com.qiniu.util.StringUtils;

public class Config {
    private static Config ins;

    private DirBean dir;
    private UploadBean upload;
    private ReinforceBean reinforce;
    private SignerBean signer;

    public static void setDefault(Config config) {
        if (ins == null)
            ins = config;
    }

    public static Config getDefault() {
        return ins;
    }


    /**
     * 七牛参数未配置
     *
     * @return 是否配置
     */
    public boolean QNIsEmpty() {
        return upload == null
                || StringUtils.isNullOrEmpty(upload.QNAccessKey)
                || StringUtils.isNullOrEmpty(upload.QNSecretKey)
                || StringUtils.isNullOrEmpty(upload.QNBucket)
                || StringUtils.isNullOrEmpty(upload.QNStartUrl);
    }

    public DirBean getDir() {
        return dir;
    }

    public void setDir(DirBean dir) {
        this.dir = dir;
    }

    public UploadBean getUpload() {
        return upload;
    }

    public void setUpload(UploadBean upload) {
        this.upload = upload;
    }

    public ReinforceBean getReinforce() {
        return reinforce;
    }

    public void setReinforce(ReinforceBean reinforce) {
        this.reinforce = reinforce;
    }

    public SignerBean getSigner() {
        return signer;
    }

    public void setSigner(SignerBean signer) {
        this.signer = signer;
    }


    public static class DirBean {

        private String projectSavePath;
        private String buildBranch;
        private String outputDir;

        public String getProjectSavePath() {
            return projectSavePath;
        }

        public void setProjectSavePath(String projectSavePath) {
            this.projectSavePath = projectSavePath;
        }

        public String getBuildBranch() {
            return buildBranch;
        }

        public void setBuildBranch(String buildBranch) {
            this.buildBranch = buildBranch;
        }

        public String getOutputDir() {
            return outputDir;
        }

        public void setOutputDir(String outputDir) {
            this.outputDir = outputDir;
        }
    }

    public static class UploadBean {

        private String uploadMethod;
        private String QNAccessKey;
        private String QNSecretKey;
        private String QNBucket;
        private String QNStartUrl;

        private String ALAccessKey;
        private String ALSecretKey;
        private String ALBucket;
        private String ALStartUrl;
        private String ALEndpoint;

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

        public String getALAccessKey() {
            return ALAccessKey;
        }

        public void setALAccessKey(String ALAccessKey) {
            this.ALAccessKey = ALAccessKey;
        }

        public String getALSecretKey() {
            return ALSecretKey;
        }

        public void setALSecretKey(String ALSecretKey) {
            this.ALSecretKey = ALSecretKey;
        }

        public String getALBucket() {
            return ALBucket;
        }

        public void setALBucket(String ALBucket) {
            this.ALBucket = ALBucket;
        }

        public String getALStartUrl() {
            return ALStartUrl;
        }

        public void setALStartUrl(String ALStartUrl) {
            this.ALStartUrl = ALStartUrl;
        }

        public String getALEndpoint() {
            return ALEndpoint;
        }

        public void setALEndpoint(String ALEndpoint) {
            this.ALEndpoint = ALEndpoint;
        }
    }

    public static class ReinforceBean {

        private String reinforceMethod;
        private String LGSecretId;
        private String LGSecretKey;

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
    }

    public static class SignerBean {

        private String apksignerPath;
        private String storeFile;
        private String keyAlias;
        private String keyPassword;
        private String storePassword;

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
    }
}

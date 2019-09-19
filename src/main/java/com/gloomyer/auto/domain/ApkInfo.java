package com.gloomyer.auto.domain;

public class ApkInfo {
    private String AppUrl;
    private String AppMd5;
    private long AppSize;
    private String FileName;
    private String AppPkgName;
    private String AppVersion;
    private String AppName;

    public String getAppUrl() {
        return AppUrl;
    }

    public void setAppUrl(String appUrl) {
        AppUrl = appUrl;
    }

    public String getAppMd5() {
        return AppMd5;
    }

    public void setAppMd5(String appMd5) {
        AppMd5 = appMd5;
    }

    public long getAppSize() {
        return AppSize;
    }

    public void setAppSize(long appSize) {
        AppSize = appSize;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getAppPkgName() {
        return AppPkgName;
    }

    public void setAppPkgName(String appPkgName) {
        AppPkgName = appPkgName;
    }

    public String getAppVersion() {
        return AppVersion;
    }

    public void setAppVersion(String appVersion) {
        AppVersion = appVersion;
    }


    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    @Override
    public String toString() {
        return "ApkInfo{" +
                "AppUrl='" + AppUrl + '\'' +
                ", AppMd5='" + AppMd5 + '\'' +
                ", AppSize=" + AppSize +
                ", FileName='" + FileName + '\'' +
                ", AppPkgName='" + AppPkgName + '\'' +
                ", AppVersion='" + AppVersion + '\'' +
                ", AppName='" + AppName + '\'' +
                '}';
    }
}

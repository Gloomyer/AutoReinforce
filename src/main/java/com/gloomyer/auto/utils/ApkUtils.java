package com.gloomyer.auto.utils;

import com.gloomyer.auto.domain.ApkInfo;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.io.IOException;

public class ApkUtils {

    public static ApkInfo read2Legu(File file) {
        ApkFile apkParser = null;
        try {
            apkParser = new ApkFile(file);
            ApkMeta meta = apkParser.getApkMeta();
            ApkInfo info = new ApkInfo();
            info.setFileName(file.getName());
            info.setAppName(meta.getLabel());
            info.setAppPkgName(meta.getPackageName());
            info.setAppSize(file.length());
            info.setAppVersion(meta.getVersionName());
            info.setAppMd5(Utils.getFileMD5(file));
            return info;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (apkParser != null)
                    apkParser.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }


}

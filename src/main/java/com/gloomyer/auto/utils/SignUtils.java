package com.gloomyer.auto.utils;


import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * 签名工具类
 */
public class SignUtils {
    private static String signCmd = "";
    private static String storeFile = "";
    private static String keyAlias = "";
    private static String keyPassword = "";
    private static String storePassword = "";

    public static void setSignCmd(String signCmd) {
        SignUtils.signCmd = signCmd;
    }

    public static void setStoreFile(String storeFile) {
        SignUtils.storeFile = storeFile;
    }

    public static void setKeyAlias(String keyAlias) {
        SignUtils.keyAlias = keyAlias;
    }

    public static void setKeyPassword(String keyPassword) {
        SignUtils.keyPassword = keyPassword;
    }

    public static void setStorePassword(String storePassword) {
        SignUtils.storePassword = storePassword;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void sign(File apkFile) {
        File newFile = new File(apkFile.getParent(), "temp" + System.currentTimeMillis() + ".apk");
        String cmd = MessageFormat.format(
                "{0} sign -ks {1} --ks-key-alias {2} --ks-pass pass:{3} --key-pass pass:{4} --out {5} {6}",
                signCmd,
                storeFile,
                keyAlias,
                keyPassword,
                storePassword,
                newFile.getAbsolutePath(),
                apkFile.getAbsolutePath()
        );
        LG.e("sign cmd:{0}", cmd);
        ShellExecute.exec(cmd);
        apkFile.delete();
        try {
            FileUtils.copy(newFile, apkFile);
            newFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    public static boolean isEmpty() {
        return StringUtils.isEmpty(signCmd)
                || StringUtils.isEmpty(storeFile)
                || StringUtils.isEmpty(keyAlias)
                || StringUtils.isEmpty(keyPassword)
                || StringUtils.isEmpty(storePassword);
    }
}

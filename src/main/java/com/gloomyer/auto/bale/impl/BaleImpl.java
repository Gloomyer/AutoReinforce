package com.gloomyer.auto.bale.impl;

import com.gloomyer.auto.bale.Bale;
import com.gloomyer.auto.generate.ApkNameGenerateRule;
import com.gloomyer.auto.utils.*;
import jnr.posix.POSIX;
import jnr.posix.POSIXFactory;
import jnr.posix.util.DefaultPOSIXHandler;

import java.io.*;
import java.text.MessageFormat;
import java.util.List;

public class BaleImpl implements Bale {

    @Override
    public void bale(String projectDir, String branch, List<String> channels,
                     String saveDir, String replaceTextValue) {
        File appModelDir = changeDir(projectDir);
        operateGit(branch);
        Utils.createApkFile(appModelDir);
        File apkFile = traversing(appModelDir, saveDir);
        System.out.println(apkFile);
        createChannelApks(apkFile, replaceTextValue, channels);
    }

    /**
     * 创建渠道包
     *
     * @param apkFile          apkFile
     * @param replaceTextValue replaceTextValue
     * @param channels         channels
     */
    private void createChannelApks(File apkFile, String replaceTextValue, List<String> channels) {
        unpacking(apkFile);
        File outputDir = new File(apkFile.getParent(), "output");
        File manifestFile = new File(outputDir, "AndroidManifest.xml");

        String manifest = readManifestContent(manifestFile);
        int idx = manifest.indexOf(replaceTextValue);
        if (idx == -1) throw new RuntimeException("解包失败！");
        LG.e("idx:{0}", idx);
        ApkNameGenerateRule nameGenerate = Utils.getDefaultImpl(ApkNameGenerateRule.class);
        for (String channel : channels) {
            replaceManifestFile(manifestFile, manifest, replaceTextValue, channel);
            assert nameGenerate != null;
            File channelApkFile = packing(channel, apkFile,
                    outputDir, nameGenerate,
                    replaceTextValue);
            //签名
            SignUtils.sign(channelApkFile);
        }
    }

    /**
     * 重新打包
     *
     * @param channel      channel
     * @param oldApkFile   oldApkFile
     * @param outDir       outDir
     * @param nameGenerate nameGenerate
     */
    private File packing(String channel, File oldApkFile, File outDir,
                         ApkNameGenerateRule nameGenerate, String replaceTextValue) {
        String newApkName = nameGenerate.generate(oldApkFile.getName(), replaceTextValue, channel);
        File apkTool = new File("apktool_2.4.0.jar");
        File newFile = new File(oldApkFile.getParent(), newApkName);
        String cmd = MessageFormat.format("java -jar {0} b {1} -o {2}",
                apkTool.getAbsolutePath(),
                outDir.getAbsolutePath(),
                newFile.getAbsolutePath()
        );
        ShellExecute.exec(cmd);
        return newFile;
    }

    /**
     * 读取manifest 文件内容
     *
     * @param manifestFile 文件
     * @return 内容
     */
    private String readManifestContent(File manifestFile) {
        StringBuilder sb = new StringBuilder();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(manifestFile);
            int len;
            byte[] buffer = new byte[2048];
            while ((len = fis.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, len));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                assert fis != null;
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
                //noinspection ThrowFromFinallyBlock
                throw new RuntimeException(e.getMessage());
            }
        }
        return sb.toString();
    }

    /**
     * 替换manifest文件
     *
     * @param manifestFile 文件路径
     * @param manifest     文件内容
     * @param replaceText  要替换的渠道字符串
     * @param channel      新的渠道字符串
     */
    private void replaceManifestFile(File manifestFile, String manifest,
                                     String replaceText, String channel) {
        String newManifest = manifest.replace(replaceText, channel);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(manifestFile);
            fos.write(newManifest.getBytes());
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * 递归获取apk
     *
     * @param file    file
     * @param saveDir 保存目录
     */
    private File traversing(File file, String saveDir) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                File ret = null;
                for (File f : files) {
                    File r = traversing(f, saveDir);
                    if (r != null) ret = r;
                }
                return ret;
            }
        } else {
            if (file.getName().endsWith("apk")) {
                return copyFile(file, saveDir);
            }
        }
        return null;
    }

    /**
     * 拷贝生成好的文件到输出目录去
     *
     * @param file    生成好的apk
     * @param saveDir 保存目录
     */
    private File copyFile(File file, String saveDir) {
        try {
            File apkFile = new File(saveDir, file.getName());
            FileUtils.copy(file, apkFile);
            return apkFile;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 拆包
     *
     * @param apkFile 拆包的apk
     */
    private void unpacking(File apkFile) {
        File apkTool = new File("apktool_2.4.0.jar");
        String gradlew = MessageFormat.format(
                "java -jar {0} decode {1} -f -s -o {2}",
                apkTool.getAbsolutePath(),
                apkFile.getAbsolutePath(),
                new File(apkFile.getParent(), "output")
        );
        ShellExecute.exec(gradlew);
    }


    /**
     * Git的一些操作，切换分支，拉去最新的代码..
     *
     * @param branch 分支
     */
    private void operateGit(String branch) {
        //切换分支
        if (!StringUtils.isEmpty(branch)) {
            ShellExecute.exec(MessageFormat.format("git checkout {0}",
                    branch));
            LG.e("切换分支至:{0}", branch);
        }

        //拉取最新的代码
        ShellExecute.exec("git pull");
    }

    /**
     * 修改工作目录
     *
     * @param projectDir 项目目录
     */
    private File changeDir(String projectDir) {
        POSIX posix = POSIXFactory.getPOSIX(new DefaultPOSIXHandler(), true);
        posix.chdir(projectDir);
        LG.e("切换目录至:{0}", projectDir);
        return Utils.findAppDir(projectDir);
    }
}

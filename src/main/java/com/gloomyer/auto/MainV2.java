package com.gloomyer.auto;

import com.gloomyer.auto.bale.Bale;
import com.gloomyer.auto.interfaces.StarterParams;
import com.gloomyer.auto.protection.Protection;
import com.gloomyer.auto.upload.Upload;
import com.gloomyer.auto.upload.UploadCache;
import com.gloomyer.auto.upload.UploadFactory;
import com.gloomyer.auto.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainV2 implements StarterParams {

    //0:只打包,1:只加固,2:打包+上传至蒲公英, 3:打包加加固
    private static int action;
    private static List<String> channels;
    private static String saveDir;
    private static String projectPath;
    private static String projectBranch = ""; //空表示不进行切换分支行为
    private static String replaceTextValue;
    private static String model = "0"; //模式 0:debug,1:release 默认0
    private static String pgyApiKey; //蒲公英 apiKey
    private static String uploadMethod;
    private static String uploadAccessKey;
    private static String uploadSecretKey;
    private static String uploadBucketName;
    private static String uploadUrlPrefix;
    private static String protectionSecretId;
    private static String protectionSecretKey;
    private static int protectionMaxTaskSize = 5; //同时加固的任务最大数量

    public static void main(String[] args) {
        createParams(args);
        checkParams();
        if (action == 2 || action == 0 || action == 3) {
            long startTime = System.currentTimeMillis();
            //走打包流程,
            createApks();
            long endTime = System.currentTimeMillis();
            LG.e("打包耗时:{0}秒", (endTime - startTime) / 1000);
        }

        if (action == 2) {
            upload2Pgy();
        }

        if (action == 1 || action == 3) {
            protection();
        }
    }

    /**
     * 走加固流程
     */
    private static void protection() {
        Protection impl = Utils.getDefaultImpl(Protection.class);
        assert impl != null;
        impl.protection(uploadMethod,
                uploadAccessKey,
                uploadSecretKey,
                uploadBucketName,
                uploadUrlPrefix,
                protectionSecretId,
                protectionSecretKey,
                protectionMaxTaskSize,
                saveDir
        );

    }

    /**
     * 上传文件到蒲公英
     */
    private static void upload2Pgy() {
        //蒲公英
        Upload upload = UploadFactory.create(UploadFactory.UploadMethod.PGY);
        UploadCache.pgyApiKey = pgyApiKey;
        File dir = new File(saveDir);
        File[] files = dir.listFiles();
        assert upload != null;
        assert files != null;
        for (File file : files) {
            LG.e("{0}", upload.upload(file));
        }
    }

    /**
     * 检查必要启动参数，并且创建对于环境
     */
    private static void checkParams() {
        if (StringUtils.isEmpty(saveDir)) {
            throw new RuntimeException("保存目录不能为空!");
        }

        if (StringUtils.isEmpty(replaceTextValue)) {
            throw new RuntimeException("准备替换的字符串不能为空!");
        }

        if (StringUtils.isEmpty(projectPath)) {
            throw new RuntimeException("项目目录不能为空!");
        }

        if (channels == null || channels.isEmpty()) {
            throw new RuntimeException("渠道必须配置!");
        }

        if (SignUtils.isEmpty()) {
            throw new RuntimeException("签名信息必须配置!");
        }

        if (action == 2 && channels.size() > 1) {
            String channel = channels.get(0);
            LG.e("蒲公英上传模式只支持一个渠道包，只打:{0}渠道", channel);
            channels.clear();
            channels.add(channel);
        }

        if (action == 2 && StringUtils.isEmpty(pgyApiKey)) {
            throw new RuntimeException("action==2，蒲公英Apikey必须配置!");
        }

        if (action == 1 || action == 3) {
            if (StringUtils.isEmpty(uploadAccessKey)
                    || StringUtils.isEmpty(uploadMethod)
                    || StringUtils.isEmpty(uploadSecretKey)
                    || StringUtils.isEmpty(uploadBucketName)
                    || StringUtils.isEmpty(uploadUrlPrefix)
                    || StringUtils.isEmpty(protectionSecretId)
                    || StringUtils.isEmpty(protectionSecretKey)) {
                throw new RuntimeException("加固必须提供" +
                        "uploadMethod/accessKey/secretKey/bucketName/urlPrefix/protectionSecretId/protectionSecretKey");
            }
        }

        //如果是只加固，将利用之前生成的包，所以不走删除逻辑
        if (action != 1) {
            File saveDir = new File(MainV2.saveDir);
            FileUtils.deleteFile(saveDir);
            //noinspection ResultOfMethodCallIgnored
            saveDir.mkdirs();
        }
    }

    /**
     * 读取启动参数
     *
     * @param args 参数读取
     */
    private static void createParams(String[] args) {
        int size = args.length / 2;

        for (int i = 0; i < size; i++) {
            String key = args[i * 2];
            String value = args[i * 2 + 1];
            System.out.println(String.format(
                    "启动参数[%s]:%s",
                    key, value
            ));

            if (KEY_ACTION.equalsIgnoreCase(key)) {
                action = Integer.parseInt(value);
            } else if (KEY_MODEL.equalsIgnoreCase(key)) {
                model = value;
            } else if (KEY_CHANNEL.equalsIgnoreCase(key)) {
                if (channels == null) channels = new ArrayList<>();
                channels.add(value);
            } else if (KEY_SAVE_PATH.equalsIgnoreCase(key)) {
                saveDir = value;
            } else if (KEY_PROJECT_PATH.equalsIgnoreCase(key)) {
                projectPath = value;
            } else if (KEY_PROJECT_BRANCH.equalsIgnoreCase(key)) {
                projectBranch = value;
            } else if (KEY_REPLACE_TEXT_VALUE.equalsIgnoreCase(key)) {
                replaceTextValue = value;
            } else if (KEY_SIGN_CMD.equalsIgnoreCase(key)) {
                SignUtils.setSignCmd(value);
            } else if (KEY_SIGN_STORE_FILE.equalsIgnoreCase(key)) {
                SignUtils.setStoreFile(value);
            } else if (KEY_SIGN_STORE_PASSWORD.equalsIgnoreCase(key)) {
                SignUtils.setStorePassword(value);
            } else if (KEY_SIGN_KEY_ALIAS.equalsIgnoreCase(key)) {
                SignUtils.setKeyAlias(value);
            } else if (KEY_SIGN_KEY_PASSWORD.equalsIgnoreCase(key)) {
                SignUtils.setKeyPassword(value);
            } else if (KEY_PGY_API_KEY.equalsIgnoreCase(key)) {
                pgyApiKey = value;
            } else if (KEY_UPLOAD_METHOD.equalsIgnoreCase(key)) {
                uploadMethod = value;
            } else if (KEY_UPLOAD_ACCESS_KEY.equalsIgnoreCase(key)) {
                uploadAccessKey = value;
            } else if (KEY_UPLOAD_SECRET_KEY.equalsIgnoreCase(key)) {
                uploadSecretKey = value;
            } else if (KEY_UPLOAD_BUCKET_NAME.equalsIgnoreCase(key)) {
                uploadBucketName = value;
            } else if (KEY_UPLOAD_URL_PREFIX.equalsIgnoreCase(key)) {
                uploadUrlPrefix = value;
            } else if (KEY_PROTECTION_SECRETID.equalsIgnoreCase(key)) {
                protectionSecretId = value;
            } else if (KEY_PROTECTION_SECRETKEY.equalsIgnoreCase(key)) {
                protectionSecretKey = value;
            }
        }
    }

    /**
     * 打包
     */
    private static void createApks() {
        Bale impl = Utils.getDefaultImpl(Bale.class);
        assert impl != null;
        impl.bale(projectPath,
                projectBranch,
                model,
                channels,
                saveDir,
                replaceTextValue);
    }
}

package com.gloomyer.auto;

import com.gloomyer.auto.bale.Bale;
import com.gloomyer.auto.interfaces.StarterParams;
import com.gloomyer.auto.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainV2 implements StarterParams {

    //0:只打包 1:只加固,2:打包+加固
    private static int action;
    private static List<String> channels;
    private static String saveDir;
    private static String projectPath;
    private static String projectBranch = ""; //空表示不进行切换分支行为
    private static String replaceTextValue;

    public static void main(String[] args) {
        createParams(args);
        checkParams();
        if (action == 2 || action == 0) {
            long startTime = System.currentTimeMillis();
            //走打包流程,
            createApks();
            long endTime = System.currentTimeMillis();
            LG.e("打包耗时:{0}秒", (endTime - startTime) / 1000);
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

        File saveDir = new File(MainV2.saveDir);
        FileUtils.deleteFile(saveDir);
        //noinspection ResultOfMethodCallIgnored
        saveDir.mkdirs();
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
            }
        }
    }

    /**
     * 打包
     */
    private static void createApks() {
        Bale impl = Utils.getDefaultImpl(Bale.class);
        assert impl != null;
        impl.bale(projectPath, projectBranch, channels, saveDir, replaceTextValue);
    }
}

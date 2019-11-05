package com.gloomyer.auto.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Shell执行器
 */
public class ShellExecute {
    private static String OS = System.getProperty("os.name").toLowerCase();

    /**
     * 是否非linux系统
     *
     * @return 是否非linux系统
     */
    public static boolean isUnix() {
        return !OS.contains("windows");
    }

    public static String exec(String cmd) {
        if (OS.contains("windows")) {
            return execWin(cmd);
        } else {
            return execUnix(cmd);
        }

    }

    /**
     * windows 执行命令
     *
     * @param cmd 命令
     */
    @SuppressWarnings("DuplicatedCode")
    private static String execWin(String cmd) {
        try {
            if (!(cmd.startsWith("git")
                    || cmd.startsWith("ssh")
                    || cmd.startsWith("gradlew")
            )) {
                cmd = "cmd.exe " + cmd;
            }
            Process process = Runtime.getRuntime().exec(cmd);

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    process.getInputStream(), "gbk"));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                LG.i(line);
                sb.append(line).append("\n");
            }

            br = new BufferedReader(new InputStreamReader(process.getErrorStream(), "gbk"));
            sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                LG.i(line);
                sb.append(line).append("\n");
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * unix 系列系统执行命令
     *
     * @param cmd 命令
     */
    private static String execUnix(String cmd) {
        try {
            //String[] cmdA = {"/bin/sh", "-c", cmd};
            Process process = Runtime.getRuntime().exec(cmd);
            InputStream in = process.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                LG.i(line);
                sb.append(line).append("\n");
            }

            br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                LG.i(line);
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package com.gloomyer.auto.generate;


import com.gloomyer.auto.annotation.DefaultImpl;
import com.gloomyer.auto.generate.impl.ApkNameGenerateRuleImpl;

@DefaultImpl(clazz = ApkNameGenerateRuleImpl.class)
public interface ApkNameGenerateRule {

    /**
     * 生成新的APK文件名称的规则
     *
     * @param oldName     apk文件名称
     * @param replaceTextValue 预留的字符串
     * @param channelName 渠道的名称
     * @return 新的apk文件名称
     */
    String generate(String oldName, String replaceTextValue, String channelName);
}

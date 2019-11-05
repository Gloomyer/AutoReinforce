package com.gloomyer.auto.bale;


import com.gloomyer.auto.annotation.DefaultImpl;
import com.gloomyer.auto.bale.impl.BaleImpl;

import java.util.List;

@DefaultImpl(clazz = BaleImpl.class)
public interface Bale {

    /**
     * 打包
     *
     * @param projectDir       项目目录
     * @param branch           分支
     * @param channels         渠道
     * @param saveDir          保存目录
     * @param replaceTextValue 渠道预留字符串
     */
    void bale(String projectDir, String branch, List<String> channels, String saveDir, String replaceTextValue);
}

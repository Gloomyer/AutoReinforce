package com.gloomyer.auto.generate.impl;

import com.gloomyer.auto.generate.ApkNameGenerateRule;

public class ApkNameGenerateRuleImpl implements ApkNameGenerateRule {
    @Override
    public String generate(String oldName, String replaceTextValue, String channelName) {
        return oldName.replace(replaceTextValue, channelName);
    }
}

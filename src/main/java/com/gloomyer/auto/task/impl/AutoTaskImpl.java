package com.gloomyer.auto.task.impl;

import com.gloomyer.auto.task.AutoTask;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class AutoTaskImpl implements AutoTask {

    private int status;

    @Override
    public String status() {
        return status == 0 ? "未运行" : "运行中";
    }
}

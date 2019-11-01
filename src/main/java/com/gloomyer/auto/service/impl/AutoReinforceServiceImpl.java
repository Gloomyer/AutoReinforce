package com.gloomyer.auto.service.impl;

import com.gloomyer.auto.service.AutoReinforceService;
import com.gloomyer.auto.service.StopService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class AutoReinforceServiceImpl implements
        AutoReinforceService, Runnable {
    private int status;
    private final StopService stopService;

    public AutoReinforceServiceImpl(StopService stopService) {
        this.stopService = stopService;
    }

    @Override
    public String status() {
        return status == 0 ? "未运行" :
                (status == 1 ? "运行中" : "已停止");
    }

    @Override
    public void start(int action) {
        synchronized (this) {
            if (status == 1 && action == 1)
                throw new RuntimeException("服务已经在运行了");
        }
        status = action;
        new Thread(this).start();
    }

    @Override
    public void stop(int action) {
        status = action;
    }

    @Override
    public void run() {
        while (status == 1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("run....");
        }

        //如果是手动停止，执行删除任务，删除之前的工作环境
        if (status == 2) {
            execStop();
        }
    }


    /**
     * 执行停止任务
     */
    private void execStop() {
        stopService.exec();
    }
}

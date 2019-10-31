package com.gloomyer.auto.api;

import com.gloomyer.auto.pojo.resp.BaseResp;
import com.gloomyer.auto.task.AutoTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("task")
public class TaskController {

    final AutoTask autoTask;

    public TaskController(AutoTask autoTask) {
        this.autoTask = autoTask;
    }

    @GetMapping("status")
    public BaseResp<String> status() {
        return BaseResp.success(autoTask.status());
    }

    @GetMapping("action/{action}")
    public BaseResp<String> action(@PathVariable("action") int action) {
        return BaseResp.success("success:" + action);
    }
}

package com.gloomyer.auto.api;

import com.gloomyer.auto.pojo.resp.BaseResp;
import com.gloomyer.auto.service.AutoReinforceService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("task")
public class TaskController {

    private final AutoReinforceService service;

    public TaskController(AutoReinforceService service) {
        this.service = service;
    }

    @GetMapping
    public BaseResp<String> status() {
        return BaseResp.success(service.status());
    }

    @PostMapping
    public BaseResp<String> action(@RequestBody HashMap<String, String> body) {
        int action = Integer.parseInt(body.get("action"));
        if (action == 1) {
            //开始任务,
            service.start(action);
        }else{
            service.stop(action);
        }
        return BaseResp.success("success:" + action);
    }
}

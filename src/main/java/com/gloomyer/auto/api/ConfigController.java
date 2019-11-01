package com.gloomyer.auto.api;

import com.gloomyer.auto.config.Config;
import com.gloomyer.auto.pojo.resp.BaseResp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("config")
public class ConfigController {

    @GetMapping
    public BaseResp<Config> get() {
        return BaseResp.success(Config.getDefault());
    }
}

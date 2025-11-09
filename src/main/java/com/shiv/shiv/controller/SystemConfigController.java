package com.shiv.shiv.controller;

import com.shiv.shiv.entity.SystemConfigEntity;
import com.shiv.shiv.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/config")
public class SystemConfigController {

    @Autowired
    private SystemConfigService configService;

    @GetMapping
    public SystemConfigEntity getConfig() {
        return configService.getConfig();
    }

    @PutMapping
    public SystemConfigEntity updateConfig(@RequestBody SystemConfigEntity updated) {
        return configService.updateConfig(updated);
    }
}

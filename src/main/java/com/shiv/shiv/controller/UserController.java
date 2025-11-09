package com.shiv.shiv.controller;

import com.shiv.shiv.Utility.SystemConfiguration;
import com.shiv.shiv.entity.SystemConfigEntity;
import com.shiv.shiv.entity.UserEntity;
import com.shiv.shiv.service.SystemConfigService;
import com.shiv.shiv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private SystemConfigService systemConfigService;
    @PostMapping("/create")
    public boolean createUser(@RequestBody UserEntity user){
        if(systemConfigService.getConfig().getBlockedCountries().stream().anyMatch(user.getMobNo() :: startsWith)) return false;
        userService.saveUser(user);
        return true;
    }

    @GetMapping("/see/{mobile}")
    public UserEntity getUser(@PathVariable String mobile){
       return userService.getUser(mobile);
    }

}

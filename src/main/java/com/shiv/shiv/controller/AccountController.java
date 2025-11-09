package com.shiv.shiv.controller;

import com.shiv.shiv.entity.UserEntity;
import com.shiv.shiv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private UserService userService;

    @PostMapping("/unfreeze/{mobile}")
    public String unfreezeAccount(@PathVariable String mobile){
        UserEntity user = userService.getUser(mobile);
        if(user == null) return "User not Found";
        if(!user.isFrozen()) return "Account is already active";

        user.setFrozen(false);
        user.setFailedTpinAttempts(0);
        user.getNotifications().add(Instant.now() + "-- Account unfrozen by admin --");
        userService.saveUser(user);
        return "Account successfully unfrozen";
    }
}

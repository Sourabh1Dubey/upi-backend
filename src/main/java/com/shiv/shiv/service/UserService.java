package com.shiv.shiv.service;

import com.shiv.shiv.entity.UserEntity;
import com.shiv.shiv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public void saveUser(UserEntity user){
        userRepository.save(user);
    }

    public UserEntity getUser(String mobile){
        return userRepository.findUserByMobNo(mobile);
    }
}

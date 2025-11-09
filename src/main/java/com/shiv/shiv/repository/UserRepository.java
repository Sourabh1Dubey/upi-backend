package com.shiv.shiv.repository;

import com.shiv.shiv.entity.UserEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, ObjectId> {
    public UserEntity findUserByMobNo(String MobNo);
}

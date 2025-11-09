package com.shiv.shiv.repository;

import com.shiv.shiv.entity.SystemConfigEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigRepository extends MongoRepository<SystemConfigEntity, String> {
}

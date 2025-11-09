package com.shiv.shiv.repository;

import com.shiv.shiv.entity.HistoryEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HistoryRepository extends MongoRepository<HistoryEntity, ObjectId> {
    public List<HistoryEntity> findBySenderMobile(String mobile);
    public List<HistoryEntity> findByReceiverMobile(String mobile);
}

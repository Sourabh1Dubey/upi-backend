package com.shiv.shiv.entity;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "history")
public class HistoryEntity {
    @Id
    private ObjectId id;
    private String senderMobile;
    private String receiverMobile;
    private BigDecimal amount;
    private String result;
    private String timeStamp;
}

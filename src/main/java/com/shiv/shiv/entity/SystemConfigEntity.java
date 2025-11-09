package com.shiv.shiv.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;
@Data
@Document(collection = "System-Configuration")
public class SystemConfigEntity {
    @Id
    private String id;

    private BigDecimal dailyLimit;
    private BigDecimal weeklyLimit;
    private BigDecimal monthlyLimit;

    private int dailyTxnLimit;
    private int weeklyTxnLimit;
    private int monthlyTxnLimit;

    private int maxWrongPin;
    private int ipChangeTime;

    private List<String> blockedCountries;

//    private int rapidTxnLimit = 2;
//    private int rapidTxnTimeWindow = 600;
}

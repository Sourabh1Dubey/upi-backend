package com.shiv.shiv.entity;

import lombok.Data;
import lombok.NonNull;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Document(collection = "shiv_users")
@Data
public class UserEntity {
    @Id
    private ObjectId id;
    @NonNull
    private String name;
    private String bankName;
    private String tpin;
    private String mobNo;
    private BigDecimal bal = BigDecimal.ZERO;
    private List<String> notifications;

    private boolean frozen = false;
    private int failedTpinAttempts = 0;

    private BigDecimal dailyAmount = BigDecimal.ZERO;
    private BigDecimal weeklyAmount = BigDecimal.ZERO;
    private BigDecimal monthlyAmount = BigDecimal.ZERO;

    private int dailyTransactions = 0;
    private int weeklyTransactions = 0;
    private int monthlyTransactions = 0;

    private Instant lastTransaction = null;

    private String lastIpAddress;
    private String lastIpCheckTime;
}


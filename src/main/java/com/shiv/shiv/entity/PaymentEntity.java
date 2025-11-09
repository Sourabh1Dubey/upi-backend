package com.shiv.shiv.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentEntity {
    private String senderMobile;
    private String receiverMobile;
    private BigDecimal amount;
    private String tpin;
}

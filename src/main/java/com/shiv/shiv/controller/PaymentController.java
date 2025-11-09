package com.shiv.shiv.controller;

import com.shiv.shiv.entity.PaymentEntity;
import com.shiv.shiv.service.HistoryService;
import com.shiv.shiv.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private HistoryService historyService;

    @PostMapping
    public String payProcess(@RequestBody PaymentEntity paymentEntity, HttpServletRequest request) {
        //String clientIp = request.getRemoteAddr();
        String clientIp = request.getHeader("X-Forwarded-For");
        if (clientIp == null) clientIp = request.getRemoteAddr();
        String outcome = paymentService.processPayment(paymentEntity, clientIp);
        historyService.recordTxn(paymentEntity.getSenderMobile(),paymentEntity.getReceiverMobile(),outcome,paymentEntity.getAmount());
        return outcome;
    }
}

package com.shiv.shiv.service;

import com.shiv.shiv.Utility.SystemConfiguration;
import com.shiv.shiv.entity.PaymentEntity;
import com.shiv.shiv.entity.SystemConfigEntity;
import com.shiv.shiv.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
@Component
public class PaymentService {
    @Autowired
    private UserService userService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private SystemConfiguration systemConfiguration;
    @Autowired
    private HistoryService historyService;

    public String processPayment(PaymentEntity paymentEntity, String currentIp){
        UserEntity sender = userService.getUser(paymentEntity.getSenderMobile());
        UserEntity receiver = userService.getUser(paymentEntity.getReceiverMobile());

        if (sender == null){
          return "Sender not found";
        }
        if (receiver == null){
            return "Receiver not found";
        }
        if(sender.getMobNo().equals(receiver.getMobNo())) return "sender and receiver can't be same";
        if (sender.isFrozen()){
           return "Account is frozen. Cannot process Payment";
        }

        if(receiver.isFrozen()) return "This account is Frozen , cannot send money to this account";
        SystemConfigEntity config = systemConfigService.getConfig();

        if (sender.getLastIpAddress() != null && !sender.getLastIpAddress().equals(currentIp)) {
            Instant lastCheck = Instant.parse(sender.getLastIpCheckTime());
            Instant now = Instant.now();

            long minutesSinceLastIp = java.time.Duration.between(lastCheck, now).toMinutes();
            System.out.println("Current IP: " + currentIp);
            System.out.println("Last IP: " + sender.getLastIpAddress());
            System.out.println("Last IP check time: " + sender.getLastIpCheckTime());


            if (minutesSinceLastIp < config.getIpChangeTime()) {
                sender.setFrozen(true);
                sender.getNotifications().add(Instant.now() + " -- Account frozen due to suspicious IP change ");
                System.out.println("Difference in minutes: " + minutesSinceLastIp);
                System.out.println("User frozen before check: " + sender.isFrozen());
                userService.saveUser(sender);
                return "🚫 Account frozen due to suspicious IP change.";
            }
        }

        // 🕓 Update last IP and time after every transaction
        sender.setLastIpAddress(currentIp);
        sender.setLastIpCheckTime(Instant.now().toString());
        userService.saveUser(sender);

        if (sender.getTpin() == null || !sender.getTpin().equals(paymentEntity.getTpin())) {
            sender.setFailedTpinAttempts(sender.getFailedTpinAttempts()+1);
            if (sender.getFailedTpinAttempts() >= config.getMaxWrongPin()){
                sender.setFrozen(true);
                sender.getNotifications().add(Instant.now()+"-- Account frozen due to wrong TPIN Attempts");
            }
            userService.saveUser(sender);
           return "Invalid TPIN";
        }
        sender.setFailedTpinAttempts(0);

        if (paymentEntity.getAmount() == null || paymentEntity.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return "Invalid Amount";
        }

        if (sender.getBal().compareTo(paymentEntity.getAmount()) < 0) {
            return "Insufficient balance";
        }

        systemConfiguration.resetIfNeeded(sender);

        if(systemConfiguration.exceedsLimits(sender,paymentEntity.getAmount())){
            sender.setFrozen(true);
            sender.getNotifications().add(Instant.now()+"-- Account frozen due to exceeding transaction limits --");
            userService.saveUser(sender);
            return "Transaction limit exceeded . Account Frozen";
        }

        sender.setBal(sender.getBal().subtract(paymentEntity.getAmount()));
        receiver.setBal(receiver.getBal().add(paymentEntity.getAmount()));

        systemConfiguration.updateCounters(sender,paymentEntity.getAmount());

        if (sender.getNotifications() == null) {
            sender.setNotifications(new ArrayList<>());
        }
        if (receiver.getNotifications() == null) {
            receiver.setNotifications(new ArrayList<>());
        }

        String time = Instant.now().toString();
        sender.getNotifications().add(time + " - Debited " + paymentEntity.getAmount() + " to " + receiver.getMobNo());
        receiver.getNotifications().add(time + " - Credited " + paymentEntity.getAmount() + " from " + sender.getMobNo());

        userService.saveUser(sender);
        userService.saveUser(receiver);
      //  historyService.recordTxn(sender.getMobNo(), receiver.getMobNo(),"Success",paymentEntity.getAmount());
     //   systemConfiguration.checkRapidIncoming(receiver);
       return "Success";
    }
}

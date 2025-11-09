package com.shiv.shiv.Utility;

import com.shiv.shiv.entity.HistoryEntity;
import com.shiv.shiv.entity.SystemConfigEntity;
import com.shiv.shiv.entity.UserEntity;
import com.shiv.shiv.repository.HistoryRepository;
import com.shiv.shiv.service.SystemConfigService;
import com.shiv.shiv.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@Component
public class SystemConfiguration {
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private UserService userService;
    @Autowired
    private HistoryRepository historyRepository;
    public boolean exceedsLimits(UserEntity user, BigDecimal amount) {
        SystemConfigEntity config = systemConfigService.getConfig();

        if (user.getDailyAmount().add(amount).compareTo(config.getDailyLimit()) > 0) return true;
        if (user.getWeeklyAmount().add(amount).compareTo(config.getWeeklyLimit()) > 0) return true;
        if (user.getMonthlyAmount().add(amount).compareTo(config.getMonthlyLimit()) > 0) return true;

        if (user.getDailyTransactions() + 1 > config.getDailyTxnLimit()) return true;
        if (user.getWeeklyTransactions() + 1 > config.getWeeklyTxnLimit()) return true;
        if (user.getMonthlyTransactions() + 1 > config.getMonthlyTxnLimit()) return true;

        return false;
    }

    public void updateCounters(UserEntity user, BigDecimal amount) {
        user.setDailyAmount(user.getDailyAmount().add(amount));
        user.setWeeklyAmount(user.getWeeklyAmount().add(amount));
        user.setMonthlyAmount(user.getMonthlyAmount().add(amount));

        user.setDailyTransactions(user.getDailyTransactions() + 1);
        user.setWeeklyTransactions(user.getWeeklyTransactions() + 1);
        user.setMonthlyTransactions(user.getMonthlyTransactions() + 1);

        user.setLastTransaction(Instant.now());
    }

    public void resetIfNeeded(UserEntity user) {
        if (user.getLastTransaction() == null) return;

        Duration diff = Duration.between(user.getLastTransaction(), Instant.now());
        long days = diff.toDays();

        if (days >= 1) {
            user.setDailyAmount(BigDecimal.ZERO);
            user.setDailyTransactions(0);
        }
        if (days >= 7) {
            user.setWeeklyAmount(BigDecimal.ZERO);
            user.setWeeklyTransactions(0);
        }
        if (days >= 30) {
            user.setMonthlyAmount(BigDecimal.ZERO);
            user.setMonthlyTransactions(0);
        }
    }

//    public void checkRapidIncoming(UserEntity receiver){
//        SystemConfigEntity config = systemConfigService.getConfig();
//        Instant now = Instant.now();
//        Instant windowStart = now.minusSeconds(config.getRapidTxnTimeWindow());
//        List<HistoryEntity> recentTxns = historyRepository.findByReceiverMobile((receiver.getMobNo()));
//
//        long uniqueSenders = recentTxns.stream()
//                .filter(txn -> OffsetDateTime.parse(txn.getTimeStamp()).toInstant().isAfter(windowStart))
//                .map(HistoryEntity :: getSenderMobile)
//                .distinct()
//                .count();
//        if(uniqueSenders >= config.getRapidTxnLimit()){
//            receiver.setFrozen(true);
//            receiver.getNotifications().add(
//                    Instant.now() +" -- Account frozen due to rapid incoming transactions from multiple users."
//            );
//            userService.saveUser(receiver);
//        }
//    }
}
package com.shiv.shiv.service;

import com.shiv.shiv.entity.HistoryEntity;
import com.shiv.shiv.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    public void recordTxn(String senderMobile, String receiverMobile, String outcome, BigDecimal amount){
        HistoryEntity historyEntity = new HistoryEntity();
        historyEntity.setSenderMobile(senderMobile);
        historyEntity.setReceiverMobile(receiverMobile);
        historyEntity.setAmount(amount);
        historyEntity.setResult(outcome);
        historyEntity.setTimeStamp(Instant.now().toString());
        historyRepository.save(historyEntity);
    }

    public List<HistoryEntity> findMyHistory(String mobile){
        return historyRepository.findAll(Sort.by(Sort.Direction.DESC, "timeStamp"))
                .stream()
                .filter(x -> x.getSenderMobile().equals(mobile) || x.getReceiverMobile().equals(mobile))
                .collect(Collectors.toList());
    }
}

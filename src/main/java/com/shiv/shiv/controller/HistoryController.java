package com.shiv.shiv.controller;

import com.shiv.shiv.entity.HistoryEntity;
import com.shiv.shiv.repository.HistoryRepository;
import com.shiv.shiv.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.shiv.shiv.entity.HistoryEntity.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private HistoryService historyService;
    @GetMapping("/admin")
    public List<HistoryEntity> getAllTxn(){
        return historyRepository.findAll(Sort.by(Sort.Direction.DESC, "timeStamp"));
    }
    @GetMapping("/user/{mobile}")
    public List<HistoryEntity> getMyTxn(@PathVariable String mobile){
        return  historyService.findMyHistory(mobile);
    }
}

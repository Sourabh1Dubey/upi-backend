package com.shiv.shiv.service;

import com.shiv.shiv.entity.SystemConfigEntity;
import com.shiv.shiv.repository.SystemConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;

@Service
public class SystemConfigService {

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    private SystemConfigEntity currentConfig;

    @PostConstruct
    public void loadConfig() {
        List<SystemConfigEntity> configs = systemConfigRepository.findAll();
        if (configs.isEmpty()) {
            // Default values
            SystemConfigEntity config = new SystemConfigEntity();
            config.setDailyLimit(new java.math.BigDecimal("5000"));
            config.setWeeklyLimit(new java.math.BigDecimal("20000"));
            config.setMonthlyLimit(new java.math.BigDecimal("50000"));
            config.setDailyTxnLimit(5);
            config.setWeeklyTxnLimit(20);
            config.setMonthlyTxnLimit(50);
            config.setMaxWrongPin(3);
            config.setIpChangeTime(30);
            config.setBlockedCountries(java.util.Arrays.asList("+92", "+83", "+93"));
            currentConfig = systemConfigRepository.save(config);
        } else {
            currentConfig = configs.get(0);
        }
    }

    public SystemConfigEntity getConfig() {
        return currentConfig;
    }

    public SystemConfigEntity updateConfig(SystemConfigEntity updated) {
        updated.setId(currentConfig.getId());
        currentConfig = systemConfigRepository.save(updated);
        return currentConfig;
    }
}

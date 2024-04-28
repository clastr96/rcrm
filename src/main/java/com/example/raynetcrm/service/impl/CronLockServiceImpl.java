package com.example.raynetcrm.service.impl;

import com.example.raynetcrm.entity.CronLock;
import com.example.raynetcrm.repository.CronLockRepository;
import com.example.raynetcrm.service.CronLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CronLockServiceImpl implements CronLockService {
    private final CronLockRepository cronLockRepository;
    @Override
    public boolean acquireLock() {
        Optional<CronLock> cronLockOptional = cronLockRepository.findByLockedAtNotNull();
        return cronLockOptional.isPresent();
    }

    @Override
    public CronLock createLock() {
        CronLock cronLock = new CronLock();
        cronLock.setLockedAt(Instant.now());
        return cronLockRepository.save(cronLock);
    }

    @Override
    public void releaseLock(CronLock cronLock) {
        cronLockRepository.delete(cronLock);
    }
}

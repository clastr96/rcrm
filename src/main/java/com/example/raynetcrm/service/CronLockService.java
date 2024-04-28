package com.example.raynetcrm.service;

import com.example.raynetcrm.entity.CronLock;

public interface CronLockService {
    boolean acquireLock();
    CronLock createLock();
    void releaseLock(CronLock cronLock);
}

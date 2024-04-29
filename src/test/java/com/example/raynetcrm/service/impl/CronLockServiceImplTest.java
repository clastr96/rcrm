package com.example.raynetcrm.service.impl;

import com.example.raynetcrm.entity.CronLock;
import com.example.raynetcrm.repository.CronLockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CronLockServiceImplTest {
    @Mock
    private CronLockRepository cronLockRepository;

    @InjectMocks
    private CronLockServiceImpl cronLockService;

    @Test
    void testAcquireLock() {
        // Given
        when(cronLockRepository.findByLockedAtNotNull()).thenReturn(Optional.of(new CronLock()));

        // When
        boolean lockAcquired = cronLockService.acquireLock();

        // Then
        assertTrue(lockAcquired);
    }

    @Test
    void testAcquireLockWhenNotLocked() {
        // Given
        when(cronLockRepository.findByLockedAtNotNull()).thenReturn(Optional.empty());

        // When
        boolean lockAcquired = cronLockService.acquireLock();

        // Then
        assertFalse(lockAcquired);
    }

    @Test
    void testCreateLock() {
        // Given
        CronLock expectedLock = new CronLock();
        when(cronLockRepository.save(any(CronLock.class))).thenReturn(expectedLock);

        // When
        CronLock createdLock = cronLockService.createLock();

        // Then
        assertNotNull(createdLock);
        assertEquals(expectedLock, createdLock);
        verify(cronLockRepository).save(any(CronLock.class));
    }

    @Test
    void testReleaseLock() {
        // Given
        CronLock cronLock = new CronLock();

        // When
        cronLockService.releaseLock(cronLock);

        // Then
        verify(cronLockRepository).delete(cronLock);
    }
}

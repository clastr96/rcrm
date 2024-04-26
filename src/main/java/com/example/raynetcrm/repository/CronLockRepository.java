package com.example.raynetcrm.repository;

import com.example.raynetcrm.entity.CronLock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CronLockRepository extends JpaRepository<CronLock, Long> {
    Optional<CronLock> findByLockedAtNotNull();
}

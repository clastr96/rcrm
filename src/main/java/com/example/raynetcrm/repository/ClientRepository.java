package com.example.raynetcrm.repository;

import com.example.raynetcrm.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByRegNumber(String regNumber);

    List<Client> findAllByRegNumber(String regNumber);
}

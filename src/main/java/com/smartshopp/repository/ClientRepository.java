package com.smartshopp.repository;

import com.smartshopp.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
    boolean existsByEmail(String email);
    Client findByUser_Id(Long userId);
}
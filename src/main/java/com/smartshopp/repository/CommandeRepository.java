package com.smartshopp.repository;

import com.smartshopp.model.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findAllByClient_Id(Long clientId);
}

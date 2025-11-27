package com.smartshopp.repository;

import com.smartshopp.model.Commande;
import com.smartshopp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeRepository extends JpaRepository<Commande, Integer> {
}

package com.smartshopp.model;

import com.smartshopp.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;

    @Enumerated(EnumType.STRING)
    private CustomerTier niveauFidelite = CustomerTier.BASIC;

    private Integer totalOrders = 0;
    private BigDecimal totalSpent = BigDecimal.ZERO;

    private LocalDate firstOrderDate;
    private LocalDate lastOrderDate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Commande> commandes;
}

package com.smartshopp.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "promo_codes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String code;

    @Column(name = "percentage_remise")
    private double percentageRemise;

    private boolean active;
    private LocalDate createdAt = LocalDate.now();

    @OneToMany(mappedBy = "promoCode")
    private List<Commande> orderList;
}

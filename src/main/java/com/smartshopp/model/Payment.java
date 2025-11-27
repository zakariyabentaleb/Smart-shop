package com.smartshopp.model;

import com.smartshopp.enums.StatutPaiement;
import com.smartshopp.enums.TypePaiement;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec la commande
    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    // Numéro de paiement (1er, 2ème, 3ème…)
    private int numeroPaiement;

    private double montant;

    @Enumerated(EnumType.STRING)
    private TypePaiement typePaiement;

    @Enumerated(EnumType.STRING)
    private StatutPaiement statut;

    private LocalDateTime datePaiement;      // date du paiement
    private LocalDateTime dateEncaissement;  // pour chèques/virements

    // Informations bancaires
    private String reference; // obligatoire pour chèque/virement
    private String banque;

    private LocalDate echeance; // si le chèque est postdaté


}

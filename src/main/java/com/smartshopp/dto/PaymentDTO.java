package com.smartshopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private Long id;
    private Long commandeId;
    private Integer numeroPaiement;
    private Double montant;
    private String typePaiement;
    private String statut;
    private LocalDateTime datePaiement;
    private LocalDateTime dateEncaissement;
    private String reference;
    private String banque;
    private LocalDate echeance;
}

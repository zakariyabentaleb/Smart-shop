package com.smartshopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDTO {
    private Long id;
    private Long clientId; // id du client
    private LocalDateTime dateCreation;

    private double sousTotal;
    private double remise;
    private double montantHTApresRemise;
    private double tva;
    private double total;

    private String codePromo;
    private String statut; // PENDING, CONFIRMED, CANCELED, REJECTED

    private double montantRestant;

    private List<CommandeLigneDTO> lignes;
}

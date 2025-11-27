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

    private Double sousTotal;
    private Double remise;
    private Double montantHTApresRemise;
    private Double tva;
    private Double total;

    private String codePromo;
    private String statut; // PENDING, CONFIRMED, CANCELED, REJECTED

    private Double montantRestant;

    private List<CommandeLigneDTO> lignes;
}

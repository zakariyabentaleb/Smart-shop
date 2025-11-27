package com.smartshopp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeLigneDTO {
    private Long id;
    private Long produitId;
    private String produitNom;
    private int quantite;
    private double prixUnitaire;
    private double totalLigne;
}

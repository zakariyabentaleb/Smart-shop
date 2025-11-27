package com.smartshopp.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private Long id;
    private String nom;
    private String email;
    private String tier; // CustomerTier : BASIC, SILVER, GOLD, PLATINUM

    private Integer totalOrders;
    private Double totalSpent;
    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;

    private UserDTO user;

    private List<CommandeDTO> commandes;
}

package com.smartshopp.model;


import com.smartshopp.enums.StatutCommande;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "commandes")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<CommandeLigne> lignes;

    private LocalDate date;

    private double sousTotal;
    private double remise;
    private double tva;
    private double total;

    private String codePromo;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;
}

package com.smartshopp.mapper;

import com.smartshopp.dto.CommandeDTO;
import com.smartshopp.dto.CommandeLigneDTO;
import com.smartshopp.model.Commande;
import com.smartshopp.model.CommandeLigne;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface CommandeMapper {

    @Mappings({
            @Mapping(source = "client.id", target = "clientId"),
            @Mapping(target = "dateCreation", expression = "java(commande.getDate() == null ? null : commande.getDate().atStartOfDay())"),
            @Mapping(source = "sousTotal", target = "sousTotal"),
            @Mapping(source = "remise", target = "remise"),
            @Mapping(source = "tva", target = "tva"),
            @Mapping(source = "total", target = "total"),
            @Mapping(source = "codePromo", target = "codePromo"),
            @Mapping(source = "statut", target = "statut"),
            @Mapping(source = "montantRestant", target = "montantRestant"),
            @Mapping(source = "lignes", target = "lignes")
    })
    CommandeDTO toDTO(Commande commande);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "date", ignore = true),
            @Mapping(target = "statut", constant = "PENDING"),
            @Mapping(target = "promoCode", ignore = true)
    })
    Commande toEntity(CommandeDTO dto);

    @Mappings({
            @Mapping(source = "produit.id", target = "produitId"),
            @Mapping(source = "produit.nom", target = "produitNom"),
            @Mapping(source = "produit.prixUnitaire", target = "prixUnitaire"),
            @Mapping(source = "prixTotal", target = "totalLigne")
    })
    CommandeLigneDTO toLigneDTO(CommandeLigne commandeLigne);
}
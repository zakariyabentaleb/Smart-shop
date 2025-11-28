package com.smartshopp.mapper;

import com.smartshopp.dto.CommandeDTO;
import com.smartshopp.dto.ProductDTO;
import com.smartshopp.model.Commande;
import com.smartshopp.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommandeMapper {
    CommandeDTO toDTO(Commande commande);
    Commande toEntity(CommandeDTO commandeDTO);
}
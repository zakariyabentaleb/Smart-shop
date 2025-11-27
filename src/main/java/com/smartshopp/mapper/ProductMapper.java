package com.smartshopp.mapper;

import com.smartshopp.dto.ProductDTO;

import com.smartshopp.model.Product;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDTO(Product product);
    Product toEntity(ProductDTO productDTO);
}






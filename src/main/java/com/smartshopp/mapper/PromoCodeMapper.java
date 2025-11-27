package com.smartshopp.mapper;

import com.smartshopp.dto.PromoCodeDTO;
import com.smartshopp.model.PromoCode;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {
    PromoCodeDTO toDTO (PromoCode promoCode);
    PromoCode toEntity(PromoCodeDTO promoCodeDTO);
}

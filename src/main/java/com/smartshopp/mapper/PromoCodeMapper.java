package com.smartshopp.mapper;

import com.smartshopp.dto.PromoCodeDTO;
import com.smartshopp.model.PromoCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {
    @Mappings({
            @Mapping(source = "percentageRemise", target = "percentageRemise"),
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "active", target = "active"),
            @Mapping(source = "id", target = "id")
    })
    PromoCodeDTO toDTO (PromoCode promoCode);

    @Mappings({
            @Mapping(source = "percentageRemise", target = "percentageRemise"),
            @Mapping(source = "code", target = "code"),
            @Mapping(source = "active", target = "active"),
            @Mapping(source = "id", target = "id")
    })
    PromoCode toEntity(PromoCodeDTO promoCodeDTO);
}

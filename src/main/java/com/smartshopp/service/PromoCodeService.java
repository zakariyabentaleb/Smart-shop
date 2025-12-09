package com.smartshopp.service;

import com.smartshopp.dto.PromoCodeDTO;
import com.smartshopp.exception.ResourceNotFoundException;
import com.smartshopp.mapper.PromoCodeMapper;
import com.smartshopp.model.PromoCode;
import com.smartshopp.repository.PromoCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PromoCodeService {
    private final PromoCodeRepository promoCodeRepository;
    private final PromoCodeMapper promoCodeMapper;

    public PromoCodeDTO savePromoCode(PromoCodeDTO dto){
        // convert using mapper then explicitly set critical fields to avoid mapping gaps
        PromoCode entity = promoCodeMapper.toEntity(dto);
        if (dto.getCode() != null) entity.setCode(dto.getCode());
        // explicitly set percentageRemise to ensure the value from DTO is persisted
        entity.setPercentageRemise(dto.getPercentageRemise());
        entity.setActive(dto.isActive());

        PromoCode savedPromoCode = promoCodeRepository.save(entity);
        return promoCodeMapper.toDTO(savedPromoCode);
    }

    public PromoCodeDTO MakePromoCodeInActive(String promoCodeId){
        PromoCode promoCode = promoCodeRepository.findById(promoCodeId).orElseThrow(
                () -> new ResourceNotFoundException("Aucun Promo Code avec id: "+promoCodeId)
        );
        promoCode.setActive(false);
        PromoCode saved = promoCodeRepository.save(promoCode);
        return promoCodeMapper.toDTO(saved);
    }
}
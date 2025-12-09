package com.smartshopp.controller;

import com.smartshopp.dto.PromoCodeDTO;
import com.smartshopp.service.PromoCodeService;
import com.smartshopp.utils.AdminChecker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/promoCode")
@RequiredArgsConstructor
public class PromoCodeController {
    private final PromoCodeService promoCodeService;

    @PostMapping
    public ResponseEntity<PromoCodeDTO> savePromoCode(
            @RequestBody PromoCodeDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.checkAdminAccess(request);

        PromoCodeDTO responseDTO = promoCodeService.savePromoCode(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromoCodeDTO> setPromoCodeInactive(
            @PathVariable("id") String codePromoId,
            HttpServletRequest request
    ){
        AdminChecker.checkAdminAccess(request);

        PromoCodeDTO responseDTO = promoCodeService.MakePromoCodeInActive(codePromoId);

        return ResponseEntity.ok(responseDTO);
    }
}

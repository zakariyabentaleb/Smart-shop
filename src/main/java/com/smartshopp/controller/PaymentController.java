package com.smartshopp.controller;

import com.smartshopp.dto.PaymentDTO;
import com.smartshopp.service.PaymentService;
import com.smartshopp.utils.AdminChecker;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentDTO> payOrder(
            @Valid @RequestBody PaymentDTO dto,
            HttpServletRequest request
    ){
        AdminChecker.checkAdminAccess(request);

        PaymentDTO responseDTO = paymentService.payOrder(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}

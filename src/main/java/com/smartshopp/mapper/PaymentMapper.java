package com.smartshopp.mapper;

import com.smartshopp.dto.PaymentDTO;
import com.smartshopp.model.Payment;

public interface PaymentMapper {
    PaymentDTO toDTO(Payment payment);
    Payment toEntity(PaymentDTO paymentDTO);
}

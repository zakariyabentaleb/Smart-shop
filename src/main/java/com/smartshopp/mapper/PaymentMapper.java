package com.smartshopp.mapper;

import com.smartshopp.dto.PaymentDTO;
import com.smartshopp.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDTO toDTO(Payment payment);
    Payment toEntity(PaymentDTO paymentDTO);
}

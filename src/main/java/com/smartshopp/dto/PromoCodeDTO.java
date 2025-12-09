package com.smartshopp.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromoCodeDTO {
    private String id;
    private String code;
    private double percentageRemise;
    private boolean active;
}

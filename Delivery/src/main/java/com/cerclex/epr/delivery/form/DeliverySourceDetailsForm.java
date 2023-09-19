package com.cerclex.epr.delivery.form;

import com.cerclex.epr.enums.BrandTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class DeliverySourceDetailsForm {
    @NotBlank
    private Long brandId;
    @NotNull
    private BrandTypeEnum brandTypeEnum;
    @NotBlank
    private String brandName;
    private String brandLocation;
    @NotBlank
    private String brandAddress;
    private String recyclerCapacity;
    private String availableCapacity;
}

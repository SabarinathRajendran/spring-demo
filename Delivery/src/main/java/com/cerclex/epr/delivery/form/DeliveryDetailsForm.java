package com.cerclex.epr.delivery.form;

import com.cerclex.epr.enums.DeliveryStatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DeliveryDetailsForm {
    private Long id;
    private Double totalQuantity;
    private Date deliveryDate;
    @NotBlank
    private String stateName;
    @NotBlank
    private String wasteType;
    @NotBlank
    private String recyclingType;
    @NotBlank
    private String type;
    private String category;
    @NotNull
    private Double pricePerKg;
    private Double gstRate;
    private String truckNo;
    private Long serviceProviderId;
    private Long driverId;
    private Long recyclerId;
    private Long sourceId;
    private Long transportId;
    private DeliveryStatusEnum deliveryStatus = DeliveryStatusEnum.IN_PROGRESS;
    private DeliveryPickupForm pickupDetails;
    private DeliveryTransportForm transportDetails;
    private DeliveryRecyclerForm recycleDetails;
    // Block Chain Link
    private String deliveryHash;
}

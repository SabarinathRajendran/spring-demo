package com.cerclex.epr.delivery.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class InvoiceDeliveryDetailsForm {
    private Long id;
    private Double totalQuantity;
    private Date deliveryDate;
    @NotBlank
    private String stateName;
    @NotBlank
    private String type; // waste type
    private String category;
    private Double gstRate;
    private String truckNo;
    private String serviceProviderId;
    private String driverId;
    private DeliveryPickupForm pickupDetails;
    private DeliveryTransportForm transportDetails;
    private DeliveryRecyclerForm recycleDetails;
}

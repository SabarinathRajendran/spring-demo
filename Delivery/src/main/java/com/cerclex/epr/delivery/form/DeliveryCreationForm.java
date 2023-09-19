package com.cerclex.epr.delivery.form;

import com.cerclex.epr.delivery.entity.DeliveryDetails;
import com.cerclex.epr.enums.DeliveryStatusEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class DeliveryCreationForm {
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
    private String deliveryHash;

    public DeliveryDetails getDeliveryDetails(DeliveryDetails deliveryDetails){
        if (totalQuantity != null) deliveryDetails.setTotalQuantity(totalQuantity);
        deliveryDetails.setDeliveryDate(deliveryDate);
        deliveryDetails.setStateName(stateName);
        deliveryDetails.setType(type);
        deliveryDetails.setCategory(category);
        deliveryDetails.setPricePerKg(pricePerKg);
        deliveryDetails.setGstRate(gstRate);
        deliveryDetails.setTruckNo(truckNo);
        deliveryDetails.setDriverId(driverId);
        deliveryDetails.setDeliveryStatus(deliveryStatus);
        deliveryDetails.setDeliveryHash(deliveryHash);

        //Mapping the recycler, transporter and the source to the delivery
        deliveryDetails.setSourceId(sourceId);
        deliveryDetails.setTransportId(transportId);
        deliveryDetails.setRecyclerId(recyclerId);

        //Added Newly for the Support of E-Waste and Recycling type for the reports calculations
        deliveryDetails.setWasteType(wasteType);
        deliveryDetails.setRecyclingType(recyclingType);
        return deliveryDetails;
    }

    public DeliveryDetails getDeliveryDetails(){
        DeliveryDetails deliveryDetails = new DeliveryDetails();
        if (totalQuantity != null) deliveryDetails.setTotalQuantity(totalQuantity);
        deliveryDetails.setDeliveryDate(deliveryDate);
        deliveryDetails.setStateName(stateName);
        deliveryDetails.setType(type);
        deliveryDetails.setCategory(category);
        deliveryDetails.setPricePerKg(pricePerKg);
        deliveryDetails.setGstRate(gstRate);
        deliveryDetails.setTruckNo(truckNo);
        deliveryDetails.setDriverId(driverId);
        deliveryDetails.setDeliveryStatus(deliveryStatus);
        deliveryDetails.setDeliveryHash(deliveryHash);

        //Mapping the recycler, transporter and the source to the delivery
        deliveryDetails.setSourceId(sourceId);
        deliveryDetails.setTransportId(transportId);
        deliveryDetails.setRecyclerId(recyclerId);

        //Added Newly for the Support of E-Waste and Recycling type for the reports calculations
        deliveryDetails.setWasteType(wasteType);
        deliveryDetails.setRecyclingType(recyclingType);
        return deliveryDetails;
    }
}

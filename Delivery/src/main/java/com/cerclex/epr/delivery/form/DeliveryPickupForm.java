package com.cerclex.epr.delivery.form;

import lombok.Data;

import java.util.Date;

@Data
public class DeliveryPickupForm {
    private boolean updated;
    private Date reportedDate;
    private Double totalWeight;
    private Double tareWeight;
    private DeliverySourceDetailsForm sourceId;
    private String providerName;
    private String providerAddress;
    private String providerLocation;
    private AttachmentDetailsForm weighBridgeDetails;
    private AttachmentDetailsForm sourceInvoice;
    private AttachmentDetailsForm lorryReceipt;
    private AttachmentDetailsForm others1;
    private AttachmentDetailsForm others2;
    private AttachmentDetailsForm others3;
}

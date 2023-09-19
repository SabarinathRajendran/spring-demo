package com.cerclex.epr.delivery.form;

import lombok.Data;

import java.util.Date;

@Data
public class DeliveryTransportForm {
    private boolean updated;
    private Date startDate;
    private Date endDate;
    private DeliverySourceDetailsForm sourceId;
    private String providerName;
    private String providerAddress;
    private String providerLocation;
    private AttachmentDetailsForm deliveryChallan;
    private AttachmentDetailsForm ewayBill;
    private AttachmentDetailsForm vehiclePhoto1;
    private AttachmentDetailsForm vehiclePhoto2;
    private AttachmentDetailsForm vehiclePhoto3;
    private AttachmentDetailsForm vehiclePhoto4;
    private AttachmentDetailsForm others1;
    private AttachmentDetailsForm others2;
}

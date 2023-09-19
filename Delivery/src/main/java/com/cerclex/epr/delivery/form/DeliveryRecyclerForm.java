package com.cerclex.epr.delivery.form;

import lombok.Data;

import java.util.Date;

@Data
public class DeliveryRecyclerForm {
    private boolean updated;
    private Date reportDate;
    private Double totalWeight;
    private Double tareWeight;
    private DeliverySourceDetailsForm sourceId;
    private String providerName;
    private String providerAddress;
    private String providerLocation;
    private AttachmentDetailsForm dropProof;
    private AttachmentDetailsForm weighBridgeDetails;
    private AttachmentDetailsForm recyclerCertificate;
    private AttachmentDetailsForm coProcessingCertificate;
    private AttachmentDetailsForm recyclerAcknowledgement;
    private AttachmentDetailsForm others1;
    private AttachmentDetailsForm others2;
}

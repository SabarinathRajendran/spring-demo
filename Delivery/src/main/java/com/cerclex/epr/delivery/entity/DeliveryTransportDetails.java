package com.cerclex.epr.delivery.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class DeliveryTransportDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryDetails deliveryDetails;
    private Date startDate;
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private DeliverySourceDetails sourceId;
    private String providerName;
    private String providerAddress;
    private String providerLocation;
    @ManyToOne
    @JoinColumn(name = "delivery_challan_id")
    private AttachmentDetails deliveryChallan;
    @ManyToOne
    @JoinColumn(name = "eway_bill_id")
    private AttachmentDetails ewayBill;
    @ManyToOne
    @JoinColumn(name = "vehicle_photo_1_id")
    private AttachmentDetails vehiclePhoto1;
    @ManyToOne
    @JoinColumn(name = "vehicle_photo_2_id")
    private AttachmentDetails vehiclePhoto2;
    @ManyToOne
    @JoinColumn(name = "vehicle_photo_3_id")
    private AttachmentDetails vehiclePhoto3;
    @ManyToOne
    @JoinColumn(name = "vehicle_photo_4_id")
    private AttachmentDetails vehiclePhoto4;
    @ManyToOne
    @JoinColumn(name = "others_1_id")
    private AttachmentDetails others1;
    @ManyToOne
    @JoinColumn(name = "others_2_id")
    private AttachmentDetails others2;
    private Date createdDate;
    private Date createdBy;
    private Date modifiedDate;
    private Date modifiedBy;
}

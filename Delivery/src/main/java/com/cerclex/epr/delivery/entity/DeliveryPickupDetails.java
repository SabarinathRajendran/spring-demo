package com.cerclex.epr.delivery.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DeliveryPickupDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date reportedDate;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private DeliverySourceDetails sourceId;
    private String providerName;
    private String providerAddress;
    private String providerLocation;
    private Double tareWeight;
    private Double totalWeight;
    @ManyToOne
    @JoinColumn(name = "weigh_bridge_details_id")
    private AttachmentDetails weighBridgeDetails;
    @ManyToOne
    @JoinColumn(name = "source_invoice_id")
    private AttachmentDetails sourceInvoice;
    @ManyToOne
    @JoinColumn(name = "lorry_receipt_id")
    private AttachmentDetails lorryReceipt;
    @ManyToOne
    @JoinColumn(name = "others_1_id")
    private AttachmentDetails others1;
    @ManyToOne
    @JoinColumn(name = "others_2_id")
    private AttachmentDetails others2;
    @ManyToOne
    @JoinColumn(name = "others_3_id")
    private AttachmentDetails others3;
    private Date createdDate;
    private Date createdBy;
    private Date modifiedDate;
    private Date modifiedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeliveryPickupDetails that = (DeliveryPickupDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

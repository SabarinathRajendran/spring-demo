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
public class DeliveryRecyclerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryDetails deliveryDetails;
    private Date reportDate;
    private Double totalWeight;
    private Double tareWeight;
    @ManyToOne
    @JoinColumn(name = "source_id")
    private DeliverySourceDetails sourceId;
    private String providerName;
    private String providerAddress;
    private String providerLocation;
    @ManyToOne
    @JoinColumn(name = "drop_proof_id")
    private AttachmentDetails dropProof;
    @ManyToOne
    @JoinColumn(name = "weigh_bridge_details_id")
    private AttachmentDetails weighBridgeDetails;
    @ManyToOne
    @JoinColumn(name = "recycler_certificate_id")
    private AttachmentDetails recyclerCertificate;
    @ManyToOne
    @JoinColumn(name = "co_processing_certificate_id")
    private AttachmentDetails coProcessingCertificate;
    @ManyToOne
    @JoinColumn(name = "recycler_acknowledgement_id")
    private AttachmentDetails recyclerAcknowledgement;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeliveryRecyclerDetails that = (DeliveryRecyclerDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

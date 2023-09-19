package com.cerclex.epr.delivery.entity;


import com.cerclex.epr.delivery.utils.DatePrefixedSequenceIdGenerator;
import com.cerclex.epr.delivery.utils.StringPrefixedSequenceIdGenerator;
import com.cerclex.epr.enums.DeliveryStatusEnum;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DeliveryDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="deliveryId_seq")
    @GenericGenerator(name = "deliveryId_seq",
            strategy = "com.cerclex.epr.delivery.utils.DatePrefixedSequenceIdGenerator",
            parameters = {
            @Parameter(name = DatePrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1")
    })
    private Long id;
    private Double totalQuantity = (double) 0;
    private Double utilizedQuantity = (double) 0;
    private Date deliveryDate;
    private String stateName;
    private String wasteType;
    private String recyclingType;
    private String type;
    private String category;
    private Double pricePerKg;
    private Double gstRate;
    private String truckNo;
    private Long serviceProviderId;
    private Long driverId;
    private Long recyclerId;
    private Long sourceId;
    private Long transportId;
    private DeliveryStatusEnum deliveryStatus = DeliveryStatusEnum.IN_PROGRESS;

    //PTD = Pickup Transport Drop
    @OneToOne
    @JoinColumn(name = "pickup_details_id")
    @ToString.Exclude
    private DeliveryPickupDetails pickupDetails;
    @ManyToOne
    @JoinColumn(name = "transport_details_id")
    private DeliveryTransportDetails transportDetails;
    @ManyToOne
    @JoinColumn(name = "recycle_details_id")
    private DeliveryRecyclerDetails recycleDetails;

    // Block Chain Link
    @Column(length = 2048)
    private String deliveryHash;

    @OneToMany(mappedBy = "deliveryDetails")
    @ToString.Exclude
    private List<DeliveryAllocationDetails> deliveryAllocationDetails = new ArrayList<>();
    private boolean isActive = true;
    private Date createdDate;
    private Date createdBy;
    private Date modifiedDate;
    private Date modifiedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeliveryDetails that = (DeliveryDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

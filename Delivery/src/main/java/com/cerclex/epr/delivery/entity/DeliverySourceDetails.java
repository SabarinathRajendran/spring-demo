package com.cerclex.epr.delivery.entity;

import com.cerclex.epr.enums.BrandTypeEnum;
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
public class DeliverySourceDetails {

    private Long brandId;
    private Long serviceProviderId;
    @Enumerated(value = EnumType.STRING)
    private BrandTypeEnum brandTypeEnum;
    private String brandName;
    private String brandLocation;
    private String brandAddress;
    private String recyclerCapacity;
    private String availableCapacity;
    private Date createdDate;
    private Date createdBy;
    private Date modifiedDate;
    private Date modifiedBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeliverySourceDetails that = (DeliverySourceDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

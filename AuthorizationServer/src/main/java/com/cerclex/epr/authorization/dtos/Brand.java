package com.cerclex.epr.authorization.dtos;

import com.cerclex.epr.authorization.utils.StringPrefixedSequenceIdGenerator;
import com.cerclex.epr.enums.BrandTypeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="brand")
public class Brand {
    @Id
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private String email;
    private String mobile;
    private String billingAddress;
    private String shippingAddress;
    private Boolean defaultApproveDelivery = false ;
    private String gstId;
    private String panNumber;
    private BrandTypeEnum brandType;
    private boolean isActive;
    private boolean isEnabled;
    private Boolean isBlockChainEnabled = false;
    private Date createdDate;
    private String createdBy;
    private Date modifiedDate;
    private String modifiedBy;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

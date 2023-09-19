package com.cerclex.epr.delivery.entity;

import com.cerclex.epr.delivery.enums.PaymentStatusEnum;
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
public class FinanceInvoiceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "financeInvoiceDetails")
    private DeliveryAllocationDetails deliveryAllocationDetails;
    private Long approvedById;
    private Long paymentById;
    private Long verifiedById;
    private String poNumber;
    private String type;
    private Double quantity;
    private Double pricePerKg;
    private Double serviceAmount;
    private Double gstAmount;
    private Double totalAmount;
    private Date dueDate;
    private Date billDate;
    @Enumerated(value = EnumType.STRING)
    private PaymentStatusEnum status;
    @ManyToOne
    @JoinColumn(name = "payment_attachment_id")
    private AttachmentDetails paymentProof;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FinanceInvoiceDetails that = (FinanceInvoiceDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

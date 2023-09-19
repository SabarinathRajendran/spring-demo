package com.cerclex.epr.delivery.entity;

import com.cerclex.epr.delivery.enums.PaymentStatusEnum;
import com.cerclex.epr.enums.DeliveryStatusEnum;
import com.cerclex.epr.events.model.invoice.InvoiceEvent;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class DeliveryAllocationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long invoiceId;
    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private DeliveryDetails deliveryDetails;
    private Long brandId;
    private String poNumber;
    private DeliveryStatusEnum status;
    private Double allocatedQuantity;
    private Double price;
    private String stateName;
    private String category;
    @OneToOne
    @JoinColumn(name = "finance_invoice_id")
    private FinanceInvoiceDetails financeInvoiceDetails;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeliveryAllocationDetails that = (DeliveryAllocationDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public DeliveryAllocationDetails getAllocationFromInvoiceEvent(InvoiceEvent invoiceEvent){
        DeliveryAllocationDetails deliveryAllocationDetails = new DeliveryAllocationDetails();
        deliveryAllocationDetails.setInvoiceId(invoiceEvent.getId());
        deliveryAllocationDetails.setBrandId(invoiceEvent.getBrandId());
        deliveryAllocationDetails.setPoNumber(invoiceEvent.getPoNumber());
        deliveryAllocationDetails.setStatus(DeliveryStatusEnum.PENDING);
        deliveryAllocationDetails.setStateName(invoiceEvent.getStateName());
        deliveryAllocationDetails.setPrice(invoiceEvent.getPrice());
        deliveryAllocationDetails.setCategory(invoiceEvent.getCategoryName());
        return deliveryAllocationDetails;
    }
}

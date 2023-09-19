package com.cerclex.epr.events.model.delivery;

import com.cerclex.epr.enums.DeliveryStatusEnum;
import com.cerclex.epr.enums.InvoiceStatusType;
import com.cerclex.epr.events.model.AllocationStatusEnum;
import lombok.Data;

@Data
public class DeliveryAllocationEvent {
    private Long id;
    private Long invoiceId;
    private Long deliveryId;
    private Double totalQuantity;
    private Double allocatedQuantity;
    private InvoiceStatusType previousState;
    private AllocationStatusEnum status;
    private InvoiceStatusType nextState;
    private String message;
}

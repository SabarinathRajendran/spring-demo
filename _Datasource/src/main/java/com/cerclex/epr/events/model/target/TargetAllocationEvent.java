package com.cerclex.epr.events.model.target;

import com.cerclex.epr.enums.DeliveryStatusEnum;
import com.cerclex.epr.enums.InvoiceStatusType;
import com.cerclex.epr.events.model.AllocationStatusEnum;
import lombok.Data;

@Data
public class TargetAllocationEvent {
    private Long id;
    private String invoiceId;
    private Long deliveryId;
    private Double totalQuantity;
    private Double allocatedQuantity;
    private InvoiceStatusType previousState;
    private AllocationStatusEnum status;
    private InvoiceStatusType nextState;
    private String message;
}

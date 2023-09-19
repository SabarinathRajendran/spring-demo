package com.cerclex.epr.events.model.invoice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceApprovalRequestEvent {
    private Long invoiceId;
    private Long brandId;
    private Double totalQuantity;
    private String stateName;
    private String categoryName;
    private Boolean isDefaultApprove;
}

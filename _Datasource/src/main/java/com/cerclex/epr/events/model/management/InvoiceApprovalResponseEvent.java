package com.cerclex.epr.events.model.management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceApprovalResponseEvent {
    private Long invoiceId;
    private Long brandId;
    private Boolean isDefaultApprove;
}

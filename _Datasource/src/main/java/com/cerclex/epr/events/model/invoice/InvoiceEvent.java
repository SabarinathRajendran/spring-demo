package com.cerclex.epr.events.model.invoice;

import com.cerclex.epr.enums.InvoiceRequestType;
import com.cerclex.epr.enums.InvoiceStatusType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InvoiceEvent {
    private Long id;
    private String invoiceId;
    private Long deliveryId;
    private Long brandId;
    private String poNumber;
    private Long serviceProviderId;
    private Long recyclerId;
    private Double totalQuantity;
    private Double allocatedQuantity;
    private InvoiceStatusType previousState;
    private InvoiceRequestType status;
    private Double price;
    private String stateName;
    private String categoryName;
    private String materialName;
}

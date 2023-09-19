package com.cerclex.epr.delivery.event.service;

import com.cerclex.epr.events.model.delivery.DeliveryAllocationEvent;
import com.cerclex.epr.events.model.invoice.InvoiceEvent;

public interface DeliveryAllocationService {

    public DeliveryAllocationEvent createAllocation(InvoiceEvent invoiceEvent);

    public DeliveryAllocationEvent editAllocation(InvoiceEvent invoiceEvent);

    public DeliveryAllocationEvent cancelAllocation(InvoiceEvent invoiceEvent);

    public void rollbackAllocation(InvoiceEvent invoiceEvent);

}

package com.cerclex.epr.delivery.event.controller;

import com.cerclex.epr.delivery.event.service.DeliveryAllocationService;
import com.cerclex.epr.events.model.delivery.DeliveryAllocationEvent;
import com.cerclex.epr.events.model.invoice.InvoiceEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery-internal")
public class AllocationController {

    @Autowired
    DeliveryAllocationService deliveryAllocationService;

    @PostMapping("/create-allocation")
    @PreAuthorize("hasAuthority('TARGET_ALLOCATE')")
    public DeliveryAllocationEvent createAllocation(@RequestBody InvoiceEvent invoiceEvent){
        return deliveryAllocationService.createAllocation(invoiceEvent);
    }

    @PostMapping("/edit-allocation")
    public DeliveryAllocationEvent editAllocation(@RequestBody InvoiceEvent invoiceEvent){
        return deliveryAllocationService.editAllocation(invoiceEvent);
    }

    @PostMapping("/cancel-allocation")
    public DeliveryAllocationEvent cancelAllocation(@RequestBody InvoiceEvent invoiceEvent){
        return deliveryAllocationService.cancelAllocation(invoiceEvent);
    }

    @PostMapping("/rollback-allocation")
    public void rollbackAllocation(@RequestBody InvoiceEvent invoiceEvent){
        deliveryAllocationService.rollbackAllocation(invoiceEvent);
    }
}

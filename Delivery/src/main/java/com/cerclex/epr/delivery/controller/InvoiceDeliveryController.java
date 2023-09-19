package com.cerclex.epr.delivery.controller;

import com.cerclex.epr.delivery.form.InvoiceDeliveryDetailsForm;
import com.cerclex.epr.delivery.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/delivery/invoice")
public class InvoiceDeliveryController {

    private final DeliveryService deliveryService;

    public InvoiceDeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PreAuthorize("hasAuthority('INVOICE_READ')")
    @GetMapping("/{deliveryId}")
    public ResponseEntity<InvoiceDeliveryDetailsForm> getDeliveryForInvoice(@PathVariable String deliveryId){
        try{
            return new ResponseEntity<>(deliveryService.getDeliveryForInvoice(deliveryId), HttpStatus.OK);
        }catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to get the delivery");
        }
    }
}

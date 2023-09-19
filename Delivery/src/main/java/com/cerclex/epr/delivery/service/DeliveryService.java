package com.cerclex.epr.delivery.service;

import com.cerclex.epr.delivery.entity.DeliverySourceDetails;
import com.cerclex.epr.delivery.exception.BrandAlreadyExistsException;
import com.cerclex.epr.delivery.form.*;

import java.util.List;

public interface DeliveryService {
    public String createDelivery(DeliveryCreationForm deliveryCreationForm);

    DeliverySourceDetailsForm createDeliverySource(DeliverySourceDetailsForm deliverySourceDetailsForm) throws BrandAlreadyExistsException;

    List<DeliverySourceDetailsForm> getDeliverySource();

    DeliveryDetailsForm updatePickupDropDetails(String deliveryId, DeliveryDetailsForm deliveryDetailsForm);

    public DeliveryDetailsForm getDeliveryById(String deliveryId);

    public List<DeliveryDetailsForm> getDeliveryByUser(DeliveryDetailsForm deliveryDetailsForm);

    public List<DeliveryDetailsForm> getAllDeliveries(String year);

    public DeliveryDetailsForm updateDelivery(String deliveryId, DeliveryCreationForm deliveryCreationForm);

    public DeliveryDetailsForm updateDeliveryHash(String deliveryId, String hash);

    public String deleteDelivery(String deliveryId);

    String allocateDelivery(String deliveryId, String poNumber, String brandid, Double quantity);

    List<BrandInvoiceForm> getDeliveryAllocation(String deliveryId);

    InvoiceDeliveryDetailsForm getDeliveryForInvoice(String deliveryId);
}

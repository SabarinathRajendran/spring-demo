package com.cerclex.epr.delivery.event.service;

import com.cerclex.epr.delivery.entity.DeliveryAllocationDetails;
import com.cerclex.epr.delivery.entity.DeliveryDetails;
import com.cerclex.epr.delivery.repo.DeliveryAllocationRepository;
import com.cerclex.epr.delivery.repo.DeliveryRepository;
import com.cerclex.epr.enums.DeliveryAllocationTypeEnum;
import com.cerclex.epr.enums.DeliveryStatusEnum;
import com.cerclex.epr.enums.InvoiceStatusType;
import com.cerclex.epr.events.model.delivery.DeliveryAllocationEvent;
import com.cerclex.epr.events.model.AllocationStatusEnum;
import com.cerclex.epr.events.model.invoice.InvoiceEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Component
public class DeliveryAllocationServiceImpl implements DeliveryAllocationService {

    private final DeliveryAllocationRepository deliveryAllocationRepository;
    private final DeliveryRepository deliveryRepository;

    public DeliveryAllocationServiceImpl(DeliveryAllocationRepository deliveryAllocationRepository, DeliveryRepository deliveryRepository) {
        this.deliveryAllocationRepository = deliveryAllocationRepository;
        this.deliveryRepository = deliveryRepository;
    }

    public DeliveryAllocationEvent createAllocation(InvoiceEvent invoiceEvent) {

        DeliveryAllocationDetails deliveryAllocationDetail = new DeliveryAllocationDetails();
        DeliveryAllocationEvent deliveryAllocationEvent = new DeliveryAllocationEvent();

        deliveryAllocationEvent.setDeliveryId(invoiceEvent.getDeliveryId());
        deliveryAllocationEvent.setInvoiceId(invoiceEvent.getId());
        deliveryAllocationEvent.setId(invoiceEvent.getId());
        deliveryAllocationEvent.setAllocatedQuantity(0.0);
        deliveryAllocationEvent.setTotalQuantity(invoiceEvent.getTotalQuantity());
        deliveryAllocationEvent.setPreviousState(invoiceEvent.getPreviousState());
        deliveryAllocationEvent.setNextState(InvoiceStatusType.PENDING);

        try {

            Optional<DeliveryDetails> deliveryDetailsOptional = deliveryRepository.findById(invoiceEvent.getDeliveryId());
            if (deliveryDetailsOptional.isEmpty()) {
                deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
                deliveryAllocationEvent.setMessage("Given delivery Id does not exist");
                return deliveryAllocationEvent;
            }
            DeliveryDetails deliveryDetails = deliveryDetailsOptional.get();
            //TODO: Get the list and mandatory documents and check if all are present and change it to pending, then uncomment the below line
            if (deliveryDetails.getDeliveryStatus().equals(DeliveryStatusEnum.IN_PROGRESS)) {
                deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
                deliveryAllocationEvent.setMessage("Incomplete Delivery cannot be allocated, please complete all documents before allocation");
                return deliveryAllocationEvent;
            }

            Optional<DeliveryAllocationDetails> deliveryAllocationDetailsOptional = deliveryAllocationRepository.findByInvoiceId(invoiceEvent.getId());

            if (deliveryAllocationDetailsOptional.isEmpty()) {

                deliveryAllocationEvent.setTotalQuantity(deliveryDetails.getTotalQuantity());
                if ((deliveryDetails.getUtilizedQuantity() + invoiceEvent.getAllocatedQuantity()) > deliveryDetails.getTotalQuantity()) {
                    deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
                    deliveryAllocationEvent.setMessage("Requested quantity is not available in the delivery");
                } else {
                    deliveryAllocationDetail.setAllocatedQuantity(invoiceEvent.getAllocatedQuantity());
                    deliveryDetails.setUtilizedQuantity(deliveryDetails.getUtilizedQuantity() + invoiceEvent.getAllocatedQuantity());
                    deliveryAllocationDetail.setBrandId(invoiceEvent.getBrandId());
                    deliveryAllocationDetail.setPoNumber(invoiceEvent.getPoNumber());
                    deliveryAllocationDetail.setInvoiceId(invoiceEvent.getId());
                    deliveryAllocationDetail.setStatus(DeliveryStatusEnum.ACCEPTED);
                    deliveryAllocationDetail.setStateName(deliveryDetails.getStateName());
                    deliveryAllocationDetail.setPrice(deliveryDetails.getPricePerKg());
                    deliveryAllocationDetail.setCategory(deliveryDetails.getCategory());
                    deliveryAllocationDetail.setDeliveryDetails(deliveryDetails);
                    deliveryAllocationEvent.setAllocatedQuantity(invoiceEvent.getAllocatedQuantity());
                    deliveryAllocationRepository.save(deliveryAllocationDetail);
                    deliveryRepository.save(deliveryDetails);
                    deliveryAllocationEvent.setStatus(AllocationStatusEnum.RESERVED);
                    deliveryAllocationEvent.setMessage("Successfully Allocated");
                }
            } else if (deliveryAllocationDetailsOptional.get().getStatus().equals(DeliveryStatusEnum.CANCELLED)) {
                DeliveryAllocationDetails deliveryAllocationDetails = deliveryAllocationDetailsOptional.get();
                deliveryDetails.setUtilizedQuantity(deliveryDetails.getUtilizedQuantity() + deliveryAllocationDetails.getAllocatedQuantity());
                deliveryAllocationDetails.setStatus(DeliveryStatusEnum.ACCEPTED);
            } else {
                deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
                deliveryAllocationEvent.setMessage("Allocation with the same invoice id Already exists");
            }

            return deliveryAllocationEvent;
        } catch (Exception e) {
            deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
            deliveryAllocationEvent.setMessage(e.getMessage());
            return deliveryAllocationEvent;
        }
    }

    @Override
    public DeliveryAllocationEvent editAllocation(InvoiceEvent invoiceEvent) {
        DeliveryAllocationDetails deliveryAllocationDetail = new DeliveryAllocationDetails();
        DeliveryAllocationEvent deliveryAllocationEvent = new DeliveryAllocationEvent();

        deliveryAllocationEvent.setDeliveryId(invoiceEvent.getDeliveryId());
        deliveryAllocationEvent.setInvoiceId(invoiceEvent.getId());
        deliveryAllocationEvent.setAllocatedQuantity(0.0);
        deliveryAllocationEvent.setTotalQuantity(invoiceEvent.getTotalQuantity());

        try {
            Optional<DeliveryDetails> deliveryDetailsOptional = deliveryRepository.findById(invoiceEvent.getDeliveryId());
            if (deliveryDetailsOptional.isEmpty()) {
                deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
                deliveryAllocationEvent.setMessage("Given delivery Id does not exist");
                return deliveryAllocationEvent;
            }
            DeliveryDetails deliveryDetails = deliveryDetailsOptional.get();
            //TODO: Get the list and mandatory documents and check if all are present and change it to pending, then uncomment the below line
            //if (deliveryDetails.getDeliveryStatus().equals(DeliveryStatusEnum.IN_PROGRESS))throw new IllegalStateException("Incomplete Delivery cannot be allocated, please complete all documents before allocation");

            Optional<DeliveryAllocationDetails> deliveryAllocationDetailsOptional = deliveryAllocationRepository.findByInvoiceId(invoiceEvent.getId());

            if (deliveryAllocationDetailsOptional.isPresent()) {

                deliveryAllocationDetail = deliveryAllocationDetailsOptional.get();

                deliveryAllocationEvent.setTotalQuantity(deliveryDetails.getTotalQuantity());
                if (((deliveryDetails.getUtilizedQuantity() - deliveryAllocationDetail.getAllocatedQuantity()) + invoiceEvent.getAllocatedQuantity()) > deliveryDetails.getTotalQuantity()) {
                    deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
                    deliveryAllocationEvent.setMessage("Requested quantity is not available in the delivery");
                } else {
                    deliveryAllocationDetail.setAllocatedQuantity(invoiceEvent.getAllocatedQuantity());
                    deliveryDetails.setUtilizedQuantity((deliveryDetails.getUtilizedQuantity() - deliveryAllocationDetail.getAllocatedQuantity()) + invoiceEvent.getAllocatedQuantity());
                    //TODO : Add Setter to set invoice to allocation
                    deliveryAllocationDetail.setStatus(DeliveryStatusEnum.ACCEPTED);
                    deliveryAllocationDetail.setPrice(deliveryDetails.getPricePerKg());
                    deliveryAllocationRepository.save(deliveryAllocationDetail);
                    deliveryRepository.save(deliveryDetails);
                    deliveryAllocationEvent.setAllocatedQuantity(invoiceEvent.getAllocatedQuantity());
                    deliveryAllocationEvent.setStatus(AllocationStatusEnum.RESERVED);
                    deliveryAllocationEvent.setMessage("Successfully Allocated");
                }
            } else {
                deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
                deliveryAllocationEvent.setMessage("Allocation with the invoice id does not exist");
            }

            return deliveryAllocationEvent;
        } catch (Exception e) {
            deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
            deliveryAllocationEvent.setMessage(e.getMessage());
            return deliveryAllocationEvent;
        }
    }

    public DeliveryAllocationEvent cancelAllocation(InvoiceEvent invoiceEvent) {
        DeliveryAllocationDetails deliveryAllocationDetail = new DeliveryAllocationDetails();
        DeliveryAllocationEvent deliveryAllocationEvent = new DeliveryAllocationEvent();

        deliveryAllocationEvent.setDeliveryId(invoiceEvent.getDeliveryId());
        deliveryAllocationEvent.setInvoiceId(invoiceEvent.getId());
        deliveryAllocationEvent.setAllocatedQuantity(0.0);
        deliveryAllocationEvent.setTotalQuantity(invoiceEvent.getTotalQuantity());

        try {

            Optional<DeliveryDetails> deliveryDetailsOptional = deliveryRepository.findById(invoiceEvent.getDeliveryId());
            if (deliveryDetailsOptional.isEmpty()) {
                deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
                deliveryAllocationEvent.setMessage("Given delivery Id does not exist");
                return deliveryAllocationEvent;
            }
            DeliveryDetails deliveryDetails = deliveryDetailsOptional.get();

            Optional<DeliveryAllocationDetails> deliveryAllocationDetailsOptional = deliveryAllocationRepository.findByInvoiceId(invoiceEvent.getId());

            if (deliveryAllocationDetailsOptional.isPresent()) {

                deliveryAllocationDetail = deliveryAllocationDetailsOptional.get();

                deliveryAllocationEvent.setTotalQuantity(deliveryDetails.getTotalQuantity());
                if (deliveryAllocationDetail.getStatus().equals(DeliveryStatusEnum.CANCELLED)) {
                    deliveryAllocationEvent.setStatus(AllocationStatusEnum.CANCELLED);
                    deliveryAllocationEvent.setMessage("Allocation Cancelled Already");
                } else {
                    deliveryDetails.setUtilizedQuantity((deliveryDetails.getUtilizedQuantity() - deliveryAllocationDetail.getAllocatedQuantity()));
                    //TODO : Add Setter to set invoice to allocation
                    deliveryAllocationDetail.setStatus(DeliveryStatusEnum.CANCELLED);
                    //deliveryAllocationRepository.save(deliveryAllocationDetail);
                    deliveryAllocationRepository.delete(deliveryAllocationDetail);
                    deliveryRepository.save(deliveryDetails);
                    deliveryAllocationEvent.setAllocatedQuantity(invoiceEvent.getAllocatedQuantity());
                    deliveryAllocationEvent.setStatus(AllocationStatusEnum.CANCELLED);
                    deliveryAllocationEvent.setMessage("Allocation Cancelled");
                }
            } else {
                deliveryAllocationEvent.setStatus(AllocationStatusEnum.CANCELLED);
                deliveryAllocationEvent.setMessage("Allocation with the invoice id does not exist");
            }

            return deliveryAllocationEvent;
        } catch (Exception e) {
            deliveryAllocationEvent.setStatus(AllocationStatusEnum.FAILED);
            deliveryAllocationEvent.setMessage(e.getMessage());
            return deliveryAllocationEvent;
        }
    }

    @Override
    public void rollbackAllocation(InvoiceEvent invoiceEvent) {
        switch (invoiceEvent.getPreviousState()) {
            case IN_PROGRESS -> {
                cancelAllocation(invoiceEvent);
                Optional<DeliveryAllocationDetails> deliveryAllocationDetails = deliveryAllocationRepository.findByInvoiceId(invoiceEvent.getId());
                deliveryAllocationDetails.ifPresent(deliveryAllocationRepository::delete);
            }
            case CANCELLED -> createAllocation(invoiceEvent);

            default -> log.error("Rollback request invoice status does not match");

        }
    }

}

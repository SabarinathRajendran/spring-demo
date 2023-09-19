package com.cerclex.epr.delivery.service.impl;

import com.cerclex.epr.delivery.entity.DeliveryAllocationDetails;
import com.cerclex.epr.delivery.form.BrandInvoiceDetailedForm;
import com.cerclex.epr.delivery.form.BrandInvoiceForm;
import com.cerclex.epr.delivery.repo.DeliveryAllocationRepository;
import com.cerclex.epr.delivery.service.BrandInvoiceService;
import com.cerclex.epr.enums.DeliveryStatusEnum;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BrandInvoiceImpl implements BrandInvoiceService {

    private final DeliveryAllocationRepository deliveryAllocationRepository;


    public BrandInvoiceImpl(DeliveryAllocationRepository deliveryAllocationRepository) {
        this.deliveryAllocationRepository = deliveryAllocationRepository;
    }

    @Override
    public BrandInvoiceDetailedForm getBrandInvoiceById(String invoiceId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(DeliveryAllocationDetails.class, BrandInvoiceForm.class)
                .addMappings(m-> m.map(src-> src.getDeliveryDetails().getId(),BrandInvoiceForm::setDeliveryId));

        Optional<DeliveryAllocationDetails> deliveryAllocationDetail = deliveryAllocationRepository.findById(Long.valueOf(invoiceId));
        if (deliveryAllocationDetail.isEmpty()) throw new EntityNotFoundException("Unable to find the Invoice withthe given Id");
        return new ModelMapper().map(deliveryAllocationDetail,BrandInvoiceDetailedForm.class);
    }

    @Override
    public List<BrandInvoiceForm> getInvoiceForBrand(String brandId) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(DeliveryAllocationDetails.class, BrandInvoiceForm.class)
                .addMappings(m-> m.map(src-> src.getDeliveryDetails().getId(),BrandInvoiceForm::setDeliveryId));

        List<DeliveryAllocationDetails> deliveryAllocationDetailsList = deliveryAllocationRepository.findByBrandId(Long.parseLong(brandId));
        List<BrandInvoiceForm> brandInvoiceFormList = new ArrayList<>();
        for (DeliveryAllocationDetails deliveryAllocationDetails : deliveryAllocationDetailsList){
            brandInvoiceFormList.add(modelMapper.map(deliveryAllocationDetails,BrandInvoiceForm.class));
        }
        return brandInvoiceFormList;
    }

    @Override
    public List<BrandInvoiceForm> getAllInvoice() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(DeliveryAllocationDetails.class, BrandInvoiceForm.class)
                .addMappings(m-> m.map(src-> src.getDeliveryDetails().getId(),BrandInvoiceForm::setDeliveryId));

        List<DeliveryAllocationDetails> deliveryAllocationDetailsList = deliveryAllocationRepository.findAll();
        List<BrandInvoiceForm> brandInvoiceFormList = new ArrayList<>();
        for (DeliveryAllocationDetails deliveryAllocationDetails : deliveryAllocationDetailsList){
            brandInvoiceFormList.add(modelMapper.map(deliveryAllocationDetails,BrandInvoiceForm.class));
        }
        return brandInvoiceFormList;
    }

    @Override
    public String acceptInvoice(String invoiceId) {
        Optional<DeliveryAllocationDetails> deliveryAllocationDetails = deliveryAllocationRepository.findById(Long.valueOf(invoiceId));
        if (deliveryAllocationDetails.isEmpty()) throw new EntityNotFoundException("Unable to find the Invoice with the given ID");
        DeliveryAllocationDetails deliveryAllocationDetail = deliveryAllocationDetails.get();
        deliveryAllocationDetail.setStatus(DeliveryStatusEnum.ACCEPTED);
        //TODO: Update the target of the brand, create the finance
        deliveryAllocationRepository.save(deliveryAllocationDetail);
        return "Invoice Accepted Successfully";
    }

    @Override
    public String rejectInvoice(String invoiceId, String rejectMessage) {
        Optional<DeliveryAllocationDetails> deliveryAllocationDetails = deliveryAllocationRepository.findById(Long.valueOf(invoiceId));
        if (deliveryAllocationDetails.isEmpty()) throw new EntityNotFoundException("Unable to find the Invoice with the given ID");
        DeliveryAllocationDetails deliveryAllocationDetail = deliveryAllocationDetails.get();
        deliveryAllocationDetail.setStatus(DeliveryStatusEnum.REJECTED);
        //TODO: Update the target of the brand, create the finance
        deliveryAllocationRepository.save(deliveryAllocationDetail);
        return "Invoice Rejected Successfully";
    }
}

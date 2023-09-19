package com.cerclex.epr.delivery.controller;

import com.cerclex.epr.delivery.enums.PaymentStatusEnum;
import com.cerclex.epr.delivery.form.AttachmentDetailsForm;
import com.cerclex.epr.delivery.form.BrandInvoiceForm;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class FinanceController {
    public ResponseEntity<BrandInvoiceForm> getFinanceInvoiceById(){
        return null;
    }

    public ResponseEntity<List<BrandInvoiceForm>> getInvoiceForBrand(){
        return null;
    }

    public ResponseEntity<List<BrandInvoiceForm>> getAllInvoice(){
        return null;
    }

    public ResponseEntity<String> approveInvoice(String invoiceId){
        return null;
    }

    public ResponseEntity<String> respondToInvoice(String invoiceId, PaymentStatusEnum paymentStatusEnum){
        return null;
    }

    public ResponseEntity<String> uploadPaymentProof(String invoiceId, AttachmentDetailsForm attachmentDetailsForm){
        return null;
    }
}

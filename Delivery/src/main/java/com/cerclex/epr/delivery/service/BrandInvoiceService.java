package com.cerclex.epr.delivery.service;

import com.cerclex.epr.delivery.form.BrandInvoiceDetailedForm;
import com.cerclex.epr.delivery.form.BrandInvoiceForm;

import java.util.List;

public interface BrandInvoiceService {
    BrandInvoiceDetailedForm getBrandInvoiceById(String invoiceId);

    List<BrandInvoiceForm> getInvoiceForBrand(String brandId);

    List<BrandInvoiceForm> getAllInvoice();

    String acceptInvoice(String invoiceId);

    String rejectInvoice(String invoiceId, String rejectMessage);
}

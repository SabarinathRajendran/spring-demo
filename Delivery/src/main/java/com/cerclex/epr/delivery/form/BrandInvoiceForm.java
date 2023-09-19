package com.cerclex.epr.delivery.form;

import com.cerclex.epr.enums.DeliveryStatusEnum;
import lombok.Data;

@Data
public class BrandInvoiceForm {
    private Long id;
    private String brandId;
    private String poNumber;
    private DeliveryStatusEnum status;
    private Double allocatedQuantity;
    private Double price;
    private String stateName;
    private String category;
    private String deliveryId;
}

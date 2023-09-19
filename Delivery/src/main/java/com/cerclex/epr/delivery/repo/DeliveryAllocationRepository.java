package com.cerclex.epr.delivery.repo;

import com.cerclex.epr.delivery.entity.DeliveryAllocationDetails;
import com.cerclex.epr.delivery.entity.DeliveryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAllocationRepository extends JpaRepository<DeliveryAllocationDetails,Long> {
    List<DeliveryAllocationDetails> findByBrandId(Long brandId);

    List<DeliveryAllocationDetails> findByDeliveryDetails(DeliveryDetails deliveryDetails);

    Optional<DeliveryAllocationDetails> findByInvoiceId(Long invoiceId);
}

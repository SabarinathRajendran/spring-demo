package com.cerclex.epr.delivery.repo;

import com.cerclex.epr.delivery.entity.DeliveryTransportDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryTransportRepository extends JpaRepository<DeliveryTransportDetails, Long> {
}

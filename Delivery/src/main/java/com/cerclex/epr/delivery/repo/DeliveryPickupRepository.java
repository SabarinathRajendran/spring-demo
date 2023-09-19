package com.cerclex.epr.delivery.repo;

import com.cerclex.epr.delivery.entity.DeliveryPickupDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryPickupRepository extends JpaRepository<DeliveryPickupDetails, Long> {
}

package com.cerclex.epr.delivery.repo;

import com.cerclex.epr.delivery.entity.DeliveryRecyclerDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRecyclerRepository extends JpaRepository<DeliveryRecyclerDetails, Long> {
}

package com.cerclex.epr.delivery.repo;

import com.cerclex.epr.delivery.entity.DeliveryDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryDetails,Long> {
    List<DeliveryDetails> findByServiceProviderId(Long serviceProviderId);

    List<DeliveryDetails> findByRecyclerId(Long recyclerId);

    List<DeliveryDetails> findByTransportId(Long transportId);

    List<DeliveryDetails> findBySourceId(Long sourceId);

}

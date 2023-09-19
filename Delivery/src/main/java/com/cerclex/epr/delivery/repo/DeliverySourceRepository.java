package com.cerclex.epr.delivery.repo;

import com.cerclex.epr.delivery.entity.DeliverySourceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliverySourceRepository extends JpaRepository<DeliverySourceDetails, Long> {
    DeliverySourceDetails findByBrandId(Long brandId);

    List<DeliverySourceDetails> findByServiceProviderId(Long serviceProviderId);

}

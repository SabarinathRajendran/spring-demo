package com.cerclex.epr.delivery.repo;

import com.cerclex.epr.delivery.entity.AttachmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryAttachmentRepository extends JpaRepository<AttachmentDetails, Long> {
}

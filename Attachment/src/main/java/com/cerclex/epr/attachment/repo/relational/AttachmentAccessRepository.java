package com.cerclex.epr.attachment.repo.relational;

import com.cerclex.epr.attachment.entity.relational.AttachmentAccessDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentAccessRepository extends JpaRepository<AttachmentAccessDetails, Long> {
}

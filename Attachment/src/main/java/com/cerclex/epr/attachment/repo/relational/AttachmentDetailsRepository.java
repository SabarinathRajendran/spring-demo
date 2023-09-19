package com.cerclex.epr.attachment.repo.relational;

import com.cerclex.epr.attachment.entity.relational.AttachmentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttachmentDetailsRepository extends JpaRepository<AttachmentDetails, Long> {

    AttachmentDetails findByAttachmentID(UUID attachmentID);
}

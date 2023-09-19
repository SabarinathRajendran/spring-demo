package com.cerclex.epr.attachment.repo.relational;

import com.cerclex.epr.attachment.entity.relational.AttachmentDetails;
import com.cerclex.epr.attachment.entity.relational.AttachmentThumbnailDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttachmentThumbnailRepository extends JpaRepository<AttachmentThumbnailDetails, Long> {
    AttachmentThumbnailDetails findByAttachmentID(UUID attachmentID);
}

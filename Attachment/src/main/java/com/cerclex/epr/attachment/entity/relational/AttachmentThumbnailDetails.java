package com.cerclex.epr.attachment.entity.relational;

import com.cerclex.epr.attachment.enums.AttachementTypeEnum;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(indexes = {@Index(name = "thumbnailUUIDIndex", columnList = "attachmentID", unique = true)})
public class AttachmentThumbnailDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String attachmentName;
    private AttachementTypeEnum attachmentType;
    private UUID attachmentID;
    private byte[] attachmentData;
    private String contentType;
    private Date createdDate;
    private String createdBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AttachmentThumbnailDetails that = (AttachmentThumbnailDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
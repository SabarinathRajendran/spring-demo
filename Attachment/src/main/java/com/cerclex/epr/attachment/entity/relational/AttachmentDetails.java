package com.cerclex.epr.attachment.entity.relational;

import com.cerclex.epr.attachment.enums.AttachementTypeEnum;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(indexes = {@Index(name = "attachmentUUIDIndex", columnList = "attachmentID", unique = true)})
public class AttachmentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String attachmentName;
    private AttachementTypeEnum attachmentType;
    private UUID attachmentID;
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] attachmentData;
    private String originalFileName;
    private String contentType;
    private Date createdDate;
    private String createdBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AttachmentDetails that = (AttachmentDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

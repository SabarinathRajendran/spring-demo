package com.cerclex.epr.delivery.entity;

import com.cerclex.epr.delivery.enums.AttachmentTypeEnum;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AttachmentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String attachmentName;
    private AttachmentTypeEnum attachmentType;
    private String attachmentCategory;
    private String attachmentNo;
    private String contentType;
    private String attachmentDataId;
    private Date uploadDate;
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

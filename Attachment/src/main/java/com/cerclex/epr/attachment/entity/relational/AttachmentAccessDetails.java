package com.cerclex.epr.attachment.entity.relational;

import com.cerclex.epr.attachment.utils.ListStringConvertor;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class AttachmentAccessDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String attachmentId;
    private Long brandId;
    private Long serviceProviderId;
    private Long recyclerId;
    private Long transporterId;
    private Long sourceId;
    @Convert(converter = ListStringConvertor.class)
    private List<Long> otherId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AttachmentAccessDetails that = (AttachmentAccessDetails) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

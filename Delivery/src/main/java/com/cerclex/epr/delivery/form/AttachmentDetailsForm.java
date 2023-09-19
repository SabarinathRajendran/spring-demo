package com.cerclex.epr.delivery.form;

import com.cerclex.epr.delivery.enums.AttachmentTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class AttachmentDetailsForm {
    @NotNull
    private boolean modified;
    @NotBlank
    private String attachmentName;
    @NotBlank
    private AttachmentTypeEnum attachmentType;
    private String attachmentCategory;
    private String attachmentNo;
    @NotBlank
    private String contentType;
    @NotBlank
    private String attachmentDataId;
    private Date uploadDate;
}

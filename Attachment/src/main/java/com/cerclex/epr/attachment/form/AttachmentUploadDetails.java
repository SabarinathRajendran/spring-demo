package com.cerclex.epr.attachment.form;

import com.cerclex.epr.attachment.enums.AttachementTypeEnum;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AttachmentUploadDetails {
    @NotBlank
    private String attachmentName;
    @NotNull
    private AttachementTypeEnum attachmentType;
    @NotNull
    private MultipartFile attachmentData;
}

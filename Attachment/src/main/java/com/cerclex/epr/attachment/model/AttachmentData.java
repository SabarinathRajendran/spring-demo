package com.cerclex.epr.attachment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AttachmentData {
    private byte[] data;
    private String contentType;
}

package com.cerclex.epr.attachment.service;

import com.cerclex.epr.attachment.form.AttachmentUploadDetails;

public interface AttachmentUploadService {
    String uploadProfilePicture(AttachmentUploadDetails uploadDetails, String username) throws Exception;

    String uploadFile(AttachmentUploadDetails uploadDetails, String username) throws Exception;
}

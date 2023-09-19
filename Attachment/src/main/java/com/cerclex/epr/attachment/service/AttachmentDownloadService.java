package com.cerclex.epr.attachment.service;

import com.cerclex.epr.attachment.model.AttachmentData;

import java.util.Base64;

public interface AttachmentDownloadService {

    AttachmentData getProfileThumbnail(String attachmentID);

    AttachmentData getFileThumbnail(String attachmentID);

    AttachmentData getProfileImage(String attachmentID);

    AttachmentData getFile(String attachmentID);

}

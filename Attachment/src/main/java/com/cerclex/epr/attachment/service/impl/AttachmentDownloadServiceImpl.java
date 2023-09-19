package com.cerclex.epr.attachment.service.impl;

import com.cerclex.epr.attachment.entity.relational.AttachmentDetails;
import com.cerclex.epr.attachment.entity.relational.AttachmentThumbnailDetails;
import com.cerclex.epr.attachment.model.AttachmentData;
import com.cerclex.epr.attachment.repo.relational.AttachmentDetailsRepository;
import com.cerclex.epr.attachment.repo.relational.AttachmentThumbnailRepository;
import com.cerclex.epr.attachment.service.AttachmentDownloadService;
import com.cerclex.epr.attachment.utils.ImageUtils;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.persistence.EntityNotFoundException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class AttachmentDownloadServiceImpl implements AttachmentDownloadService {

    private AttachmentDetailsRepository attachmentDetailsRepository;
    private AttachmentThumbnailRepository attachmentThumbnailRepository;

    public AttachmentDownloadServiceImpl(AttachmentDetailsRepository attachmentDetailsRepository, AttachmentThumbnailRepository attachmentThumbnailRepository) {
        this.attachmentDetailsRepository = attachmentDetailsRepository;
        this.attachmentThumbnailRepository = attachmentThumbnailRepository;
    }

    @Override
    public AttachmentData getProfileThumbnail(String attachmentID) {
        AttachmentData attachmentData = new AttachmentData();
        try {
            UUID attachmentUUID = UUID.fromString(attachmentID);
            AttachmentThumbnailDetails attachmentThumbnailDetails = attachmentThumbnailRepository.findByAttachmentID(attachmentUUID);
            if(attachmentThumbnailDetails!=null){
                attachmentData.setContentType(attachmentThumbnailDetails.getContentType());
                attachmentData.setData(attachmentThumbnailDetails.getAttachmentData());
            }else{
                throw new EntityNotFoundException("Attachment Does not exist");
            }
            return attachmentData;
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Attachment ID not valid");
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public AttachmentData getFileThumbnail(String attachmentID) {
        AttachmentData attachmentData = new AttachmentData();
        try {
            UUID attachmentUUID = UUID.fromString(attachmentID);
            AttachmentThumbnailDetails attachmentThumbnailDetails = attachmentThumbnailRepository.findByAttachmentID(attachmentUUID);
            if(attachmentThumbnailDetails!=null){
                attachmentData.setContentType(attachmentThumbnailDetails.getContentType());
                attachmentData.setData(attachmentThumbnailDetails.getAttachmentData());
            }else{
                throw new EntityNotFoundException("Attachment Does not exist");
            }
            return attachmentData;
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Attachment ID not valid");
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public AttachmentData getProfileImage(String attachmentID) {
        AttachmentData attachmentData = new AttachmentData();
        try {
            UUID attachmentUUID = UUID.fromString(attachmentID);
            AttachmentDetails attachmentDetails = attachmentDetailsRepository.findByAttachmentID(attachmentUUID);
            if(attachmentDetails!=null){
                attachmentData.setContentType(attachmentDetails.getContentType());
                attachmentData.setData(ImageUtils.decompressBytes(attachmentDetails.getAttachmentData()));
            }else{
                throw new EntityNotFoundException("Attachment Does not exist");
            }
            return attachmentData;
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Attachment ID not valid");
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }

    @Override
    public AttachmentData getFile(String attachmentID) {
        AttachmentData attachmentData = new AttachmentData();
        try {
            UUID attachmentUUID = UUID.fromString(attachmentID);
            AttachmentDetails attachmentDetails = attachmentDetailsRepository.findByAttachmentID(attachmentUUID);
            if(attachmentDetails!=null){
                attachmentData.setContentType(attachmentDetails.getContentType());
                attachmentData.setData(ImageUtils.decompressBytes(attachmentDetails.getAttachmentData()));
            }else{
                throw new EntityNotFoundException("Attachment Does not exist");
            }
            return attachmentData;
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Attachment ID not valid");
        }catch (EntityNotFoundException e){
            throw new EntityNotFoundException(e.getMessage());
        }
    }
}

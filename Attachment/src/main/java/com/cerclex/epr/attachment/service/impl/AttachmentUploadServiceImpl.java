package com.cerclex.epr.attachment.service.impl;

import com.cerclex.epr.attachment.entity.relational.AttachmentDetails;
import com.cerclex.epr.attachment.entity.relational.AttachmentThumbnailDetails;
import com.cerclex.epr.attachment.form.AttachmentUploadDetails;
import com.cerclex.epr.attachment.repo.relational.AttachmentDetailsRepository;
import com.cerclex.epr.attachment.repo.relational.AttachmentThumbnailRepository;
import com.cerclex.epr.attachment.service.AttachmentUploadService;
import com.cerclex.epr.attachment.utils.ImageUtils;
import com.cerclex.epr.attachment.utils.ThumbnailGenerator;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.UUID;

@Service
public class AttachmentUploadServiceImpl implements AttachmentUploadService {

    private AttachmentDetailsRepository attachmentDetailsRepository;
    private AttachmentThumbnailRepository attachmentThumbnailRepository;

    public AttachmentUploadServiceImpl(AttachmentDetailsRepository attachmentDetailsRepository, AttachmentThumbnailRepository attachmentThumbnailRepository){
        this.attachmentDetailsRepository = attachmentDetailsRepository;
        this.attachmentThumbnailRepository = attachmentThumbnailRepository;
    }

    @Override
    public String uploadProfilePicture(AttachmentUploadDetails uploadDetails, String username) throws Exception {
        String attachmentID = "";
        try{
            AttachmentDetails attachmentDetails = new AttachmentDetails();
            AttachmentThumbnailDetails attachmentThumbnailDetails = new AttachmentThumbnailDetails();

            String attachmentContentType = uploadDetails.getAttachmentData().getContentType();

            if (attachmentContentType == null ||
                    !attachmentContentType.equals("image/jpeg") &&
                    !attachmentContentType.equals("image/png")){
                throw new Exception("Invalid File Type, Only JPEG,JPG and PNG are accepted");
            }

            BufferedImage thumbnail = new ThumbnailGenerator().getProfilePictureThumbnail(ImageIO.read(uploadDetails.getAttachmentData().getInputStream()));
            byte[] compressedImage = ImageUtils.compressBytes(uploadDetails.getAttachmentData().getBytes());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail,
                    attachmentContentType.replace("image/",""), baos);

            UUID attachmentUUID = UUID.randomUUID();

            attachmentDetails.setAttachmentID(attachmentUUID);
            attachmentDetails.setAttachmentName(uploadDetails.getAttachmentName());
            attachmentDetails.setAttachmentType(uploadDetails.getAttachmentType());
            attachmentDetails.setAttachmentData(compressedImage);
            attachmentDetails.setOriginalFileName(uploadDetails.getAttachmentData().getOriginalFilename());
            attachmentDetails.setContentType(attachmentContentType);
            attachmentDetails.setCreatedDate(new Date());
            attachmentDetails.setCreatedBy(username);

            attachmentThumbnailDetails.setAttachmentID(attachmentUUID);
            attachmentThumbnailDetails.setAttachmentName(uploadDetails.getAttachmentName());
            attachmentThumbnailDetails.setAttachmentType(uploadDetails.getAttachmentType());
            attachmentThumbnailDetails.setAttachmentData(baos.toByteArray());
            attachmentThumbnailDetails.setContentType(attachmentContentType);
            attachmentDetails.setCreatedDate(new Date());
            attachmentDetails.setCreatedBy(username);

            attachmentDetailsRepository.save(attachmentDetails);
            attachmentThumbnailRepository.save(attachmentThumbnailDetails);

            attachmentID = attachmentUUID.toString();

        }catch (Exception e){
            throw new Exception(e);
        }
        return attachmentID;
    }

    @Override
    public String uploadFile(AttachmentUploadDetails uploadDetails, String username) throws Exception {
        String attachmentID = "";
        try{
            AttachmentDetails attachmentDetails = new AttachmentDetails();
            AttachmentThumbnailDetails attachmentThumbnailDetails = new AttachmentThumbnailDetails();

            String attachmentContentType = uploadDetails.getAttachmentData().getContentType();

            if (attachmentContentType == null
                    || !attachmentContentType.equals("image/jpeg")
                    && !attachmentContentType.equals("image/png")
                    && !attachmentContentType.equals("application/pdf")){
                throw new Exception("Invalid File Type, Only JPEG,JPG,PNG and PDF are accepted");
            }

            byte[] compressedFile;
            BufferedImage thumbnail;
            String thumbnailContentType = attachmentContentType;
            if(attachmentContentType.equals("image/jpeg") || attachmentContentType.equals("image/png")) {
                 thumbnail = new ThumbnailGenerator().getProfilePictureThumbnail(ImageIO.read(uploadDetails.getAttachmentData().getInputStream()));
            }else {
                thumbnail = new ThumbnailGenerator().getPdfThumbnail(uploadDetails.getAttachmentData().getBytes());
                thumbnailContentType = "image/jpeg";
            }

            compressedFile = ImageUtils.compressBytes(uploadDetails.getAttachmentData().getBytes());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail,
                    thumbnailContentType.replace("image/", ""), baos);

            UUID attachmentUUID = UUID.randomUUID();

            attachmentDetails.setAttachmentID(attachmentUUID);
            attachmentDetails.setAttachmentName(uploadDetails.getAttachmentName());
            attachmentDetails.setAttachmentType(uploadDetails.getAttachmentType());
            attachmentDetails.setAttachmentData(compressedFile);
            attachmentDetails.setOriginalFileName(uploadDetails.getAttachmentData().getOriginalFilename());
            attachmentDetails.setContentType(attachmentContentType);
            attachmentDetails.setCreatedDate(new Date());
            attachmentDetails.setCreatedBy(username);

            attachmentThumbnailDetails.setAttachmentID(attachmentUUID);
            attachmentThumbnailDetails.setAttachmentName(uploadDetails.getAttachmentName());
            attachmentThumbnailDetails.setAttachmentType(uploadDetails.getAttachmentType());
            attachmentThumbnailDetails.setAttachmentData(baos.toByteArray());
            attachmentThumbnailDetails.setContentType(thumbnailContentType);
            attachmentDetails.setCreatedDate(new Date());
            attachmentDetails.setCreatedBy(username);

            attachmentDetailsRepository.save(attachmentDetails);
            attachmentThumbnailRepository.save(attachmentThumbnailDetails);

            attachmentID = attachmentUUID.toString();

        }catch (Exception e){
            throw new Exception(e);
        }
        return attachmentID;
    }
}

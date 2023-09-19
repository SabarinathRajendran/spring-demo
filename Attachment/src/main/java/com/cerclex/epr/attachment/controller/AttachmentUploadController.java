package com.cerclex.epr.attachment.controller;

import com.cerclex.epr.attachment.form.AttachmentUploadDetails;
import com.cerclex.epr.attachment.service.AttachmentUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/attachment/file/upload")
public class AttachmentUploadController {

    private AttachmentUploadService attachmentUploadService;

    public AttachmentUploadController(AttachmentUploadService attachmentUploadService){
        this.attachmentUploadService = attachmentUploadService;
    }

    @PreAuthorize("hasAuthority('ATTACHMENT_CREATE')")
    @RequestMapping(path = "/profilePicture",method = RequestMethod.POST ,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadProfilePicture(@Valid AttachmentUploadDetails uploadDetails){

        log.info("Attachment Upload Controller -> Upload Profile Image");
        log.debug("Attachment Details {}",uploadDetails);

        ResponseEntity<String> response = new ResponseEntity<>("Unable to Upload", HttpStatus.INTERNAL_SERVER_ERROR);
        String attachmentID = "";

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userid = authentication.getName();
            attachmentID = attachmentUploadService.uploadProfilePicture(uploadDetails,userid);
            log.debug("Generated Attachment ID {}", attachmentID);
            response = new ResponseEntity<>(attachmentID, HttpStatus.OK);
        }catch (Exception e){
            log.error("Unable to upload profile Image: ", e);
        }
        return response;
    }

    @PreAuthorize("hasAuthority('ATTACHMENT_CREATE')")
    @RequestMapping(method = RequestMethod.POST,consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@Valid AttachmentUploadDetails uploadDetails){
        log.info("Attachment Upload Controller -> Upload Documents");
        log.debug("Attachment Details {}",uploadDetails);

        ResponseEntity<String> response = new ResponseEntity<>("Unable to Upload File", HttpStatus.INTERNAL_SERVER_ERROR);
        String attachmentID = "";

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userid = authentication.getName();
            attachmentID = attachmentUploadService.uploadFile(uploadDetails, userid);
            log.debug("Generated Attachment ID {}", attachmentID);
            response = new ResponseEntity<>(attachmentID, HttpStatus.OK);
        }catch (Exception e){
            log.error("Unable to upload File: ", e);
        }
        return response;
    }
}

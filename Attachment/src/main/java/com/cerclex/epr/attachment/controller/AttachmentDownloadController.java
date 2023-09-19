package com.cerclex.epr.attachment.controller;

import com.cerclex.epr.attachment.model.AttachmentData;
import com.cerclex.epr.attachment.service.AttachmentDownloadService;
import io.swagger.annotations.ResponseHeader;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/attachment/file")
public class AttachmentDownloadController {

    private AttachmentDownloadService attachmentDownloadService;

    public AttachmentDownloadController(AttachmentDownloadService attachmentDownloadService) {
        this.attachmentDownloadService = attachmentDownloadService;
    }

    @PreAuthorize("hasAuthority('ATTACHMENT_READ')")
    @GetMapping("/profile/thumbnail")
    public ResponseEntity<byte[]> getProfileImageThumbnail(@RequestParam @Valid @Length(min = 30, max = 40) @NotBlank String attachmentID ){
        log.info("inside getProfileImageThumbnail -> for id " + attachmentID);
        ResponseEntity response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        AttachmentData attachmentData;
        try{
            attachmentData = attachmentDownloadService.getProfileThumbnail(attachmentID);
            log.debug("fetched attachment data entity : {}", attachmentData);

            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.valueOf(attachmentData.getContentType()));
            response = new ResponseEntity<>(attachmentData.getData(),httpHeaders,HttpStatus.OK);
        }catch (EntityNotFoundException e){
            log.error("Unable to find image: ", e);
            response = new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            log.error("invalid attachment ID: " + attachmentID, e);
            response = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.error("Unable to fetch image: ", e);
        }
        return response;

    }

    @PreAuthorize("hasAuthority('ATTACHMENT_READ')")
    @GetMapping("/thumbnail")
    public ResponseEntity<Base64> getFileThumbnail(@RequestParam @Valid @Length(min = 30, max = 40) @NotBlank String attachmentID ){
        log.info("inside getFileThumbnail -> for id " + attachmentID);
        ResponseEntity response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        AttachmentData attachmentData;
        try{
            attachmentData = attachmentDownloadService.getFileThumbnail(attachmentID);
            log.debug("fetched attachment data entity : {}", attachmentData);

            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.valueOf(attachmentData.getContentType()));
            response = new ResponseEntity<>(attachmentData.getData(),httpHeaders,HttpStatus.OK);
        }catch (EntityNotFoundException e){
            log.error("Unable to find file: ", e);
            response = new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            log.error("invalid attachment ID: " + attachmentID, e);
            response = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.error("Unable to fetch file: ", e);
        }
        return response;
    }

    @PreAuthorize("hasAuthority('ATTACHMENT_READ')")
    @GetMapping("/profile")
    public ResponseEntity<byte[]> getProfileImage(@RequestParam @Valid @Length(min = 30, max = 40) @NotBlank String attachmentID ){
        log.info("inside getProfileImage -> for id " + attachmentID);
        ResponseEntity response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        AttachmentData attachmentData;
        try{
            attachmentData = attachmentDownloadService.getProfileImage(attachmentID);
            log.debug("fetched attachment data entity : {}", attachmentData);

            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.valueOf(attachmentData.getContentType()));
            response = new ResponseEntity<>(attachmentData.getData(),httpHeaders,HttpStatus.OK);
        }catch (EntityNotFoundException e){
            log.error("Unable to find image: ", e);
            response = new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            log.error("invalid attachment ID: " + attachmentID, e);
            response = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.error("Unable to fetch image: ", e);
        }
        return response;

    }

    @PreAuthorize("hasAuthority('ATTACHMENT_READ')")
    @GetMapping
    public ResponseEntity<Base64> getFile(@RequestParam @Valid @Length(min = 30, max = 40) @NotBlank String attachmentID ){
        log.info("inside getProfileImage -> for id " + attachmentID);
        ResponseEntity response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        AttachmentData attachmentData;
        try{
            attachmentData = attachmentDownloadService.getFile(attachmentID);
            log.debug("fetched attachment data entity : {}", attachmentData);

            final HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.valueOf(attachmentData.getContentType()));
            response = new ResponseEntity<>(attachmentData.getData(),httpHeaders,HttpStatus.OK);
        }catch (EntityNotFoundException e){
            log.error("Unable to find image: ", e);
            response = new ResponseEntity(e.getMessage(),HttpStatus.NOT_FOUND);
        }catch (IllegalArgumentException e){
            log.error("invalid attachment ID: " + attachmentID, e);
            response = new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.error("Unable to fetch image: ", e);
        }
        return response;


    }
}

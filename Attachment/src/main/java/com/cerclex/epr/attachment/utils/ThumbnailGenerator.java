package com.cerclex.epr.attachment.utils;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ThumbnailGenerator {
    public BufferedImage getProfilePictureThumbnail(BufferedImage image){
        BufferedImage thumbnail = new BufferedImage(144,144,1);
        try {
            thumbnail = Thumbnails.of(image).size(144, 144).asBufferedImage();
        }catch (IOException e){
            log.error("Unable to generate Thumbnail", e);
        }
        return thumbnail;
    }

    public BufferedImage getPdfThumbnail(byte[] pdf) {
        BufferedImage thumbnail = new BufferedImage(200,200,1);
        try {
            PDDocument pdfDoc = PDDocument.load(pdf);
            PDFRenderer pdfRender = new PDFRenderer(pdfDoc);
            BufferedImage pdfImage = pdfRender.renderImage(0);
            thumbnail = Thumbnails.of(pdfImage).size(200, 200).outputFormat("jpg").asBufferedImage();
        }catch (IOException e){
            log.error("Unable to generate Thumbnail", e);
        }
        return thumbnail;
    }
}

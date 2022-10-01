package com.ulas.springcoretemplate.interfaces.service.other;

import org.springframework.web.multipart.MultipartFile;

public interface S3Services {
    void downloadFile(String keyName);

    void uploadFile(String fileName, MultipartFile file, String folder);

    String deleteFile(String fileUrl);

    String markImagesAsDeleted(String sourceKey);

    void copyFileInS3Bucket(String sourceKey, String destinationKey);

}

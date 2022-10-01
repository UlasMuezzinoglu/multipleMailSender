package com.ulas.springcoretemplate.service.other;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.ulas.springcoretemplate.interfaces.service.other.S3Services;
import com.ulas.springcoretemplate.util.FileUtil;
import com.ulas.springcoretemplate.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3ServicesImpl implements S3Services {
    private final Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);
    private final AmazonS3 s3Client;

    @Value("${s3.bucketname}")
    private String bucketname;

    @Override
    public void downloadFile(String keyName) {
        try {
            System.out.println("Downloading an object");
            S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketname, keyName));
            System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
            FileUtil.displayText(s3object.getObjectContent());
            logger.info("===================== Import File - Done! =====================");
        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        } catch (IOException ioe) {
            logger.info("IOE Error Message: " + ioe.getMessage());
        }
    }

    @Override
    public void uploadFile(String fileName, MultipartFile file, String folder) {
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getInputStream().available());
            objectMetadata.setContentType(file.getContentType());
            logger.info("===================== BEFORE UPLOADING TO " + S3Util.getS3FolderWithFolder(bucketname, folder) + " =====================");
            s3Client.putObject(new PutObjectRequest(
                    S3Util.getS3FolderWithFolder(bucketname, folder), fileName, file.getInputStream(), objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            logger.info("===================== Upload File - Done! =====================");

        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String deleteFile(String key) {
        System.out.println("FILE WILL BE DELETED : " + key);
        s3Client.deleteObject(new DeleteObjectRequest(bucketname, key));
        logger.info("@@@@@@@@@@@@@@@@@@@@@@@ FILE IS SUCCESSFULLY DELETED FROM S3 BUCKET @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return "Successfully deleted";
    }

    @Override
    public String markImagesAsDeleted(String sourceKey) {
        String destKey = sourceKey.substring(0, sourceKey.lastIndexOf("/") + 1)
                + "deleted/"
                + sourceKey.substring(sourceKey.lastIndexOf("/") + 1);

        copyFileInS3Bucket(sourceKey, destKey);
        deleteFile(sourceKey);

        return destKey;
    }


    @Override
    @Async("threadPoolTaskExecutor")
    public void copyFileInS3Bucket(String sourceKey, String destinationKey) {

        try {

            s3Client.copyObject(
                    bucketname,
                    sourceKey,
                    bucketname,
                    destinationKey);

        } catch (Exception e) {
            //
        }
    }
}


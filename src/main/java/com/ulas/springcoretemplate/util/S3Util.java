package com.ulas.springcoretemplate.util;


public class S3Util {
    public static String getS3FolderWithFolder(String s3BucketName, String folder) {
        return s3BucketName + "/" + folder;
    }

    public static String getFileNameWithObject(int height, int width, String extension) {
        String time = System.currentTimeMillis() + "";
        return "product-resized-"+height + "x" + width +"-" + time + "." + extension;
    }
}

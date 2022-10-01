package com.ulas.springcoretemplate.constant;


import java.util.Arrays;
import java.util.HashSet;

public class FileExtensionConstants {
    // Image file extensions
    private static final String jpg = "jpg";
    private static final String jpeg = "jpeg";
    private static final String png = "png";
    private static final String JPEG = "JPEG";
    private static final String PNG = "PNG";
    private static final String JPG = "JPG";
    private static final String JPE = "JPE";
    private static final String jpe = "jpe";
    private static final String TIFF = "TIFF";
    private static final String TIF = "TIF";
    private static final String tiff = "tiff";
    private static final String tif = "tif";
    private static final String HDR = "HDR";
    private static final String hdr = "hdr";
    private static final String PIC = "PIC";
    private static final String pic = "pic";
    private static final String WEBP = "WEBP";
    private static final String webp = "webp";
    private static final String BMP = "BMP";
    private static final String bmp = "bmp";
    private static final String heic = "heic";
    private static final String HEIC = "HEIC";
    private static final String heif = "heif";
    private static final String HEIF = "HEIF";

    public static final HashSet<String> PERMITTED_IMAGE_EXTENSIONS =
            new HashSet<>(Arrays.asList(
                    jpg,
                    jpeg,
                    png,
                    JPEG,
                    PNG,
                    JPG,
                    JPE,
                    jpe,
                    TIFF,
                    TIF,
                    tiff,
                    tif,
                    HDR,
                    hdr,
                    PIC,
                    pic,
                    WEBP,
                    webp,
                    BMP,
                    heic,
                    HEIC,
                    heif,
                    HEIF,
                    bmp
            ));
}


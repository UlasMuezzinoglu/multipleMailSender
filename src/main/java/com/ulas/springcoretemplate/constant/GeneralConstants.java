package com.ulas.springcoretemplate.constant;


import java.time.format.DateTimeFormatter;

public class GeneralConstants {
    public static final int MIN = 100000;
    public static final int MAX = 999999;
    public static final String ACTIVE_USER = "activeUser";
    public static final String DATE_PATTERN = "MM/dd/yyyy - HH:mm:ss Z";
    public static final String MONGO_ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String MONGO_ISO_DATE_FORMAT_WITH_MILLIS = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final DateTimeFormatter DATE_TME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final String SMS_CODE = "smsCode";
    public static final double ISTANBUL_LATITUDE = 41.015137;
    public static final double ISTANBUL_LONGITUDE = 28.979530;
    public static final int EXTREME_IMAGE_SIZE_LIMIT_MB = 15;
    public static final int EXTREME_FILE_SIZE_LIMIT_MB = 2;
    public static final double SERVICE_FEE = 2.99;
    public static final double TRANSACTION_FEE = 2.99d;
    public static final int ONE_HOUR_IN_MINUTE = 60;
    public static int DEFAULT_CODE = 111111;
    public static int PAGE_SIZE = 20;
    public static int LISTING_PAGE_SIZE = 20;
    public static boolean UP = true;
    public static boolean DOWN = false;
    public static int LENGTH_REF_CODE = 6;
    public static int SPHERE_RADIUS = 1000000;
    public static String DEFAULT_SORT_BY = "id";
    public static double IMAGE_SIZE_LIMIT = 1.5D;
    public static double FILE_SIZE_LIMIT = 2.0D;
    public static double KDV = 0.18D;
    public static double HUNDRED_PERCENT = 1D;
    public static int IMAGE_RESOLUTION_LIMIT = 1200;
    public static double IMAGE_MAX_RESOLUTION_RATIO = 10d;
    public static String BAN_MESSAGE = "Hesabınız Kullanım Koşullarımızı ihlal ettiği gerekçesi ile kapatılmıştır. Bunun bir yanlışlık olduğunu düşünüyorsanız lütfen bize bildirin";
    public static String REVERSE_BAN_MESSAGE = "Hesabınız incelemelerimiz sonucunda kullanımınıza geri açılmıştır.";
    public static String DEFAULT_PROFILE_IMAGE = "/icon/general/user.png";
    public static Integer PACKAGE_EXPIRE_DAYS = 365;
}


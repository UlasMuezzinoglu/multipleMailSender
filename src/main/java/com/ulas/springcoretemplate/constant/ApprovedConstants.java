package com.ulas.springcoretemplate.constant;


public class ApprovedConstants {

    public static final ApprovedConstants APPROVED_CONSTANTS = new ApprovedConstants();
    public static final String[] PERMIT_URLS = URLS._URLS;

    public static final class URLS {

        private static final String API_DOCS = "/v2/api-docs";
        private static final String SWAGGER_RESOURCES = "/swagger-resources/**";
        private static final String CONFIGURATION_UI = "/configuration/ui";
        private static final String CONFIGURATION_SECURITY = "/configuration/security";
        private static final String SWAGGER_UI_HTML = "/swagger-ui.html";
        private static final String WEBJARS = "/webjars/**";
        private static final String API_DOCS_V3 = "/v3/api-docs/**";
        private static final String SWAGGER_UI = "/swagger-ui/**";
        private static final String HOME = "/home/**";
        private static final String DOCUMENT = "/document/**";
        private static final String PURCHASE_GOOGLE = "/user/premium/notification/google";
        private static final String PURCHASE_APPLE = "/user/premium/notification/apple";
        private static final String SOCKET = "/ws/**";
        private static final String APP = "/app/**";
        private static final String CLIENT = "/client/**";

        public static final String[] _URLS = {
                URLS.API_DOCS,
                URLS.CONFIGURATION_UI,
                URLS.API_DOCS_V3,
                URLS.SWAGGER_RESOURCES,
                URLS.WEBJARS,
                URLS.CONFIGURATION_SECURITY,
                URLS.SWAGGER_UI_HTML,
                URLS.SWAGGER_UI,
                URLS.HOME,
                URLS.PURCHASE_GOOGLE,
                URLS.PURCHASE_APPLE,
                URLS.DOCUMENT,
                URLS.SOCKET,
                URLS.APP,
                URLS.CLIENT
        };
    }

    public static final class FileExtensions {

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

        public static final String[] FILE_EXTENSIONS = {
                FileExtensions.jpg,
                FileExtensions.jpeg,
                FileExtensions.png,
                FileExtensions.JPEG,
                FileExtensions.PNG,
                FileExtensions.JPG,
                FileExtensions.JPE,
                FileExtensions.jpe,
                FileExtensions.TIFF,
                FileExtensions.TIF,
                FileExtensions.tiff,
                FileExtensions.tif,
                FileExtensions.HDR,
                FileExtensions.hdr,
                FileExtensions.PIC,
                FileExtensions.pic,
                FileExtensions.WEBP,
                FileExtensions.webp,
                FileExtensions.BMP,
                FileExtensions.bmp
        };
    }
}

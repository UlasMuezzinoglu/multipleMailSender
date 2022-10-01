package com.ulas.springcoretemplate.constant;

import java.util.Calendar;

public class InfoConstants {
    public static final String BLACK_LIST_WORDS = "(email)|(gmail)|(\\.net)|(\\.com)|(\\.org)|(https:)|(http:)|(gmail)|(hotmail)|(ftp:)|(email)|(mailto:)|(:\\/\\/)";
    public static final String NETGSM_XML_ENDPOINT = "https://api.netgsm.com.tr/sms/send/xml";
    public static final String LOGO_URL_SUPPORT = "icon/general/support.svg";
    public static final String SUPPORT_EMAIL = "support@proje.com";
    public static final String ULAS = "905445575231";
    public static String LANGUAGE_EN = "en";
    public static String LOGIN_MESSAGE_TR = "PROJE icin tek kullanimlik giris kodunuzdur. Bu kodu siz istemediyseniz, lutfen bu mesaji dikkate almayin.";
    public static String CHANGE_NUMBER_MESSAGE_TR = "Telefon numara değişikliği için onay kodunuzdur.";
    public static String DELETE_ACCOUNT_MESSAGE_TR = "Hesabınızın Silinmesi için onay kodunuzdur.";
    public static Integer CURRENT_YEAR = Calendar.getInstance().get(Calendar.YEAR);
    public static String LIVE_GOSSIP_ENDPOINT = "/admin/livegossip";

}

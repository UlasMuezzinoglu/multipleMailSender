package com.ulas.springcoretemplate.constant;


public class RegexConstants {

    public static final String MONGO_ID = "^[a-f0-9]{24}$";

    public static final String YEAR = "(19|20)[0123456789]{2}";

    public static final String NOTIFICATION_ID = "^[a-z0-9-]{36}$";

    public static final String ALL_LETTERS_AND_NUMBERS = "^(\\p{L}|\\p{N})";
    public static final String ALL_LETTERS = "^(\\p{L})";
    public static final String ALL_NUMBERS_UNLIMITED = "^\\p{N}*$";

    public static final String DENY_URLS_OLD = "^((\\r\\n|\\r|\\n)?|((?=(?!(https|http|ftp|mailto:)))(?=(?!.([-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&=]*))))).)*$";
    public static final String BLACK_LIST = "^(?i)((\\r|\\n|\\r\\n)|(?!(" + InfoConstants.BLACK_LIST_WORDS + ")).)*$";
    public static final String MONGO_ID_CONSTRAINT = "^[a-f0-9]{8}-[a-f0-9]{4}-4[a-f0-9]{3}-[89aAbB][a-f0-9]{3}-[a-f0-9]{12}$|^[a-f0-9]{24,32}+$";
    public static final String GENERAL_REGEX_ALL_CHARS = "^[\\p{L}|\\p{N}]+([\\p{L}|\\p{N}]|[()_\\/%-.,:;& ])";
    public static final String GENERAL_REGEX_NAME = "^[a-zA-Z0-9ğüşöçıİĞÜŞÖÇ]+[a-zA-Z0-9ğüşöçıİĞÜŞÖÇ ]";

    public static final String URL = "(https?:\\/\\/)?(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&\\/\\/=]*)";
    public static final String URL_OR_ID_COMPLETE = "^[a-f\\d]{24}|[a-z0-9-\\/]{4,100}$";


    //public static final String CITY = "(?ix)^(\\p{L}|\\p{M}|['`-]){1,10}(?:\\s{1}(\\p{L}|\\p{M}|['`-]){1,10}){0,2}$";
    public static final String PHONE_NUMBER = "(^$|[0-9]{10,13})";

    public static final String REF_CODE = "(^[a-zA-Z]{6}$)";

    public static final String NEGATE_PREFIX = "^((?!";
    public static final String NEGATE_POSTFIX = ").)*$";
}

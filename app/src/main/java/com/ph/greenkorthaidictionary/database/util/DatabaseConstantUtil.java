package com.ph.greenkorthaidictionary.database.util;

/**
 * Created by preparkha on 15. 6. 20..
 */
public class DatabaseConstantUtil {

    public static final String DATABASE_SPLITE_NAME = "ThaiKorDictionary_SEARCH_T_20160316.sqlite";
//    public static final String DATABASE_SPLITE_NAME = "ThaiKorDictionary_SEARCH_P_20151007.sqlite";
    public static final String DATABASE_DB_NAME = "ThaiKorDic.db";
    public static final String PAKAGE_NAME = "com.ph.greenkorthaidictionary";
    public static final String DATABASE_PATH = "/data/data/"+PAKAGE_NAME+"/databases/"+DATABASE_DB_NAME;

    public static final String TABLE_KOR_THAI_NAME = "THAI_KOR_DICTIONARY";
    public static final String COLUMN_NAME_IDX = "IDX";
    public static final String COLUMN_NAME_PRONUNCIATION = "PRONUNCIATION";
    public static final String COLUMN_NAME_THAI = "THAI";
    public static final String COLUMN_NAME_KOREAN = "KOREAN";
    public static final String COLUMN_NAME_SEARCH_PRONUNCIATION = "SEARCH_PRONUNCIATION";

    public static int DATABASE_VERSION = 16;

}

package com.ph.greenkorthaidictionary.data.network;

import java.io.Serializable;

/**
 * Created by preparkha on 15. 6. 10..
 */
public class NetworkData implements Serializable {

    private Integer NO; // service id (PK)
    private Integer CURVERCD_K; // current vesrion of blue_kor_thai_dictionary
    private Integer CURVERCD_P; // current vesrion of green_thai_kor_dictionary
    private Integer CURVERCD_T; // current vesrion of read_thai_kor_dictionary
    private String CURVERNM_K; // current version name of blue_kor_thai_dictionary
    private String CURVERNM_P; // current version name of green_thai_kor_dictionary
    private String CURVERNM_T; // current version name of read_thai_kor_dictionary
    private String OS; // operate system in mobile
    private Integer DBCURVERCD_K; // database version of blue_kor_thai_dictionary
    private Integer DBCURVERCD_P; // database version of green_thai_kor_dictionary
    private Integer DBCURVERCD_T; // database version of read_thai_kor_dictionary
    private String DBCURDTPATH_K; // database path of blue_kor_thai_dictionary
    private String DBCURDTPATH_P; // database path of green_thai_kor_dictionary
    private String DBCURDTPATH_T; // database path of read_thai_kor_dictionary
    private String STORE_URL_K; // download path of blue_kor_thai_dictionary
    private String STORE_URL_P; // download path of green_thai_kor_dictionary
    private String STORE_URL_T; // download path of read_thai_kor_dictionary

    public Integer getNO() {
        return NO;
    }

    public void setNO(Integer NO) {
        this.NO = NO;
    }

    public Integer getCURVERCD_K() {
        return CURVERCD_K;
    }

    public void setCURVERCD_K(Integer CURVERCD_K) {
        this.CURVERCD_K = CURVERCD_K;
    }

    public Integer getCURVERCD_P() {
        return CURVERCD_P;
    }

    public void setCURVERCD_P(Integer CURVERCD_P) {
        this.CURVERCD_P = CURVERCD_P;
    }

    public Integer getCURVERCD_T() {
        return CURVERCD_T;
    }

    public void setCURVERCD_T(Integer CURVERCD_T) {
        this.CURVERCD_T = CURVERCD_T;
    }

    public String getCURVERNM_K() {
        return CURVERNM_K;
    }

    public void setCURVERNM_K(String CURVERNM_K) {
        this.CURVERNM_K = CURVERNM_K;
    }

    public String getCURVERNM_P() {
        return CURVERNM_P;
    }

    public void setCURVERNM_P(String CURVERNM_P) {
        this.CURVERNM_P = CURVERNM_P;
    }

    public String getCURVERNM_T() {
        return CURVERNM_T;
    }

    public void setCURVERNM_T(String CURVERNM_T) {
        this.CURVERNM_T = CURVERNM_T;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public Integer getDBCURVERCD_K() {
        return DBCURVERCD_K;
    }

    public void setDBCURVERCD_K(Integer DBCURVERCD_K) {
        this.DBCURVERCD_K = DBCURVERCD_K;
    }

    public Integer getDBCURVERCD_P() {
        return DBCURVERCD_P;
    }

    public void setDBCURVERCD_P(Integer DBCURVERCD_P) {
        this.DBCURVERCD_P = DBCURVERCD_P;
    }

    public Integer getDBCURVERCD_T() {
        return DBCURVERCD_T;
    }

    public void setDBCURVERCD_T(Integer DBCURVERCD_T) {
        this.DBCURVERCD_T = DBCURVERCD_T;
    }

    public String getDBCURDTPATH_K() {
        return DBCURDTPATH_K;
    }

    public void setDBCURDTPATH_K(String DBCURDTPATH_K) {
        this.DBCURDTPATH_K = DBCURDTPATH_K;
    }

    public String getDBCURDTPATH_P() {
        return DBCURDTPATH_P;
    }

    public void setDBCURDTPATH_P(String DBCURDTPATH_P) {
        this.DBCURDTPATH_P = DBCURDTPATH_P;
    }

    public String getDBCURDTPATH_T() {
        return DBCURDTPATH_T;
    }

    public void setDBCURDTPATH_T(String DBCURDTPATH_T) {
        this.DBCURDTPATH_T = DBCURDTPATH_T;
    }

    public String getSTORE_URL_K() {
        return STORE_URL_K;
    }

    public void setSTORE_URL_K(String STORE_URL_K) {
        this.STORE_URL_K = STORE_URL_K;
    }

    public String getSTORE_URL_P() {
        return STORE_URL_P;
    }

    public void setSTORE_URL_P(String STORE_URL_P) {
        this.STORE_URL_P = STORE_URL_P;
    }

    public String getSTORE_URL_T() {
        return STORE_URL_T;
    }

    public void setSTORE_URL_T(String STORE_URL_T) {
        this.STORE_URL_T = STORE_URL_T;
    }

}

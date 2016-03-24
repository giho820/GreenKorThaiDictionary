package com.ph.greenkorthaidictionary.data.dto;

import java.io.Serializable;

/**
 * Created by preparkha on 15. 6. 15..
 */
public class KorThaiDicDto implements Serializable {

    private int idx;
    private String kor;
    private String thai;
    private String pronu;

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getKor() {
        return kor;
    }

    public void setKor(String kor) {
        this.kor = kor;
    }

    public String getThai() {
        return thai;
    }

    public void setThai(String thai) {
        this.thai = thai;
    }

    public String getPronu() {
        return pronu;
    }

    public void setPronu(String pronu) {
        this.pronu = pronu;
    }

    @Override
    public String toString() {
        return "KorThaiDicDto{" +
                "idx=" + idx +
                ", kor='" + kor + '\'' +
                ", thai='" + thai + '\'' +
                ", pronu='" + pronu + '\'' +
                '}';
    }

}

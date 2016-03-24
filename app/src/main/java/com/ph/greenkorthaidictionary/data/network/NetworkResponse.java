package com.ph.greenkorthaidictionary.data.network;

import java.io.Serializable;

/**
 * Created by preparkha on 15. 6. 10..
 */
public class NetworkResponse implements Serializable {

    private boolean RESULT;
    private Integer RESULTCODE;
    private NetworkData SERVICEINFO;

    public boolean isRESULT() {
        return RESULT;
    }

    public void setRESULT(boolean RESULT) {
        this.RESULT = RESULT;
    }

    public Integer getRESULTCODE() {
        return RESULTCODE;
    }

    public void setRESULTCODE(Integer RESULTCODE) {
        this.RESULTCODE = RESULTCODE;
    }

    public NetworkData getSERVICEINFO() {
        return SERVICEINFO;
    }

    public void setSERVICEINFO(NetworkData SERVICEINFO) {
        this.SERVICEINFO = SERVICEINFO;
    }

    @Override
    public String toString() {
        return "NetworkResponse{" +
                "RESULT=" + RESULT +
                ", RESULTCODE=" + RESULTCODE +
                '}';
    }

}

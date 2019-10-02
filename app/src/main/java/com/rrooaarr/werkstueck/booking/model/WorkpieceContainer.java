package com.rrooaarr.werkstueck.booking.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class WorkpieceContainer {

    @SerializedName("_PK")
    private String pk;

    @SerializedName("wst_liste")
    private Map<String, String> wst_infos;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public Map<String, String> getWst_infos() {
        return wst_infos;
    }

    public WorkpieceContainer setWst_infos(Map<String, String> wst_infos) {
        this.wst_infos = wst_infos;
        return this;
    }
}
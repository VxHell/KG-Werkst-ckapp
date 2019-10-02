package com.rrooaarr.werkstueck.booking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Workpiece2 {

    @SerializedName("_PK")
    private String pk;

    @SerializedName("wst_liste")
    private List<String> wst_infos;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public List<String> getWst_infos() {
        return wst_infos;
    }

    public Workpiece2 setWst_infos(List<String> wst_infos) {
        this.wst_infos = wst_infos;
        return this;
    }
}
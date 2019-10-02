package com.rrooaarr.werkstueck.booking.model;

import com.google.gson.annotations.SerializedName;

public class Workpiece {

    @SerializedName("_PK")
    private String pk;

    @SerializedName("Werkstuecknummer")
    private String werkstuecknummer;

    @SerializedName("ProjektID")
    private String projektId;

    @SerializedName("FANummerText")
    private String faNummer;

    @SerializedName("LeitungMitarbeiter_Name")
    private String leitungMitarbeiterName;

    public String getWerkstuecknummer() {
        return werkstuecknummer;
    }

    public String getProjektId() {
        return projektId;
    }

    public String getFaNummer() {
        return faNummer;
    }

    public String getLeitungMitarbeiterName() {
        return leitungMitarbeiterName;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
}
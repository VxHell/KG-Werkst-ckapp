package com.rrooaarr.werkstueck.wsinfo;

public class WorkpieceListElement {

    private String key;

    private String value;

    public WorkpieceListElement(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}

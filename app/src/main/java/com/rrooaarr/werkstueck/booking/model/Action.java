package com.rrooaarr.werkstueck.booking.model;

public enum Action {
    FINISHING("Veredelung"), PACKAGING("Verpackung"), SHIPPING("Versand");

    Action(String name) {
        this.locale = name;
    }

    private String locale;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
package com.fevly.kasuariprogroom.datum;

import java.util.Objects;

/*==========================================
Author : Fevly Pallar
contact : fevly.pallar@gmail.com
=========================================*/
public class Datum {
    /*============040424=========
       Nanti kalau ada data baru (selain id, text)
       bikin vars disini. Jgn langsung concat  ke
       string yg mau disend di User.
         =========================*/
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Datum datum = (Datum) o;
        return Objects.equals(userID, datum.userID) && Objects.equals(text, datum.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, text);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Datum(String userID, String text) {
        this.userID = userID;
        this.text = text;
    }

    private String userID;
    private String text;
}

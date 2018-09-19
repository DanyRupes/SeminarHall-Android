package com.techie_dany.srecshb.admin.helpers;

import org.json.JSONArray;

import java.lang.reflect.Array;

public class BookingHome_Helper {

    private String book_desc,by;
    int status;
    private JSONArray session_id;

    public BookingHome_Helper(){}

    public BookingHome_Helper(JSONArray session_id, int status, String book_desc, String by) {
        this.session_id = session_id;
        this.status = status;
        this.book_desc = book_desc;
        this.by = by;
    }

    public JSONArray getSession_id() {

        return session_id;
    }

    public void setSession_id(JSONArray session_id) {
        this.session_id = session_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBook_desc() {
        return book_desc;
    }

    public void setBook_desc(String book_desc) {
        this.book_desc = book_desc;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }
}

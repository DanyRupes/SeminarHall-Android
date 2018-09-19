package com.techie_dany.srecshb.Superadmin.helpers;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;


public class Inbox_Helper implements Parcelable {

    private final String TAG ="inbox_helper_help";
    private String date,email,book_desc,by;
    int container_id;
    private String sessionid;

    public Inbox_Helper(String date, String email, String session_id, String book_desc, String by, int container_id) {

        this.date = date;
        this.email = email;
        sessionid = session_id;
        this.book_desc = book_desc;
        this.by = by;
        this.container_id = container_id;
    }

    protected Inbox_Helper(Parcel in) {
        date = in.readString();
        email = in.readString();
        sessionid = in.readString();
        book_desc = in.readString();
        by = in.readString();
        container_id = in.readInt();
    }

    public static final Creator<Inbox_Helper> CREATOR = new Creator<Inbox_Helper>() {
        @Override
        public Inbox_Helper createFromParcel(Parcel in) {

            return new Inbox_Helper(in);
        }

        @Override
        public Inbox_Helper[] newArray(int size) {
            return new Inbox_Helper[size];
        }
    };

    public String getEmail() {
        return email;
    }


    public String getDate() {
        return date;
    }
    public String getBook_desc() {
        return book_desc;
    }

    public String getBy() {
        return by;
    }

    public String getSession_id() {
        return sessionid;
    }
    public Integer getcontainer_id() {
        return container_id;
    }


    public int describeContents() {
        return 0;
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(email);
        dest.writeString(sessionid);
        dest.writeString(book_desc);
        dest.writeString(by);
        dest.writeInt(container_id);
    }
}

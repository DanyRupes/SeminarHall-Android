package com.techie_dany.srecshb.admin.helpers;

import android.os.Parcel;
import android.os.Parcelable;

public class PendingsHelper implements Parcelable {



    public PendingsHelper(){}
    public PendingsHelper(String date,String email,String ses_id,String book_desc,String by,int hall_id){

        this.date = date;
        this.email = email;
        this.ses_id = ses_id;
        this.book_desc = book_desc;
        this.by = by;
        this.hall_id = hall_id;
    }


    protected PendingsHelper(Parcel in) {
        date = in.readString();
        email = in.readString();
        ses_id = in.readString();
        book_desc = in.readString();
        by = in.readString();
        hall_id = in.readInt();
    }

    public static final Creator<PendingsHelper> CREATOR = new Creator<PendingsHelper>() {
        @Override
        public PendingsHelper createFromParcel(Parcel in) {
            return new PendingsHelper(in);
        }

        @Override
        public PendingsHelper[] newArray(int size) {
            return new PendingsHelper[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSes_id() {
        return ses_id;
    }

    public void setSes_id(String ses_id) {
        this.ses_id = ses_id;
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

    public int getHall_id() {
        return hall_id;
    }

    public void setHall_id(int hall_id) {
        this.hall_id = hall_id;
    }

    String date,email,ses_id,book_desc,by;
    int hall_id;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(email);
        dest.writeString(ses_id);
        dest.writeString(book_desc);
        dest.writeString(by);
        dest.writeInt(hall_id);
    }
}


package com.techie_dany.srecshb.admin.helpers;

public class Halls_Home_Helper {


    private String title;
    private String head;
    private int hall_id;



    public Halls_Home_Helper( int hall_id, String title, String head) {

        this.title = title;
        this.head = head;
        this.hall_id = hall_id;
    }

    public int getHall_id() {
        return hall_id;
    }

    public void setHall_id(int hall_id) {
        this.hall_id = hall_id;
    }



    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }




    public String getHead() {
        return head;
    }
    public void setHead(String head) {
        this.head = head;
    }



}

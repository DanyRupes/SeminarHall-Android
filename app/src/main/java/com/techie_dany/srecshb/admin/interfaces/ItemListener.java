package com.techie_dany.srecshb.admin.interfaces;

import com.techie_dany.srecshb.admin.helpers.BookingHome_Helper;

import java.util.ArrayList;

public interface ItemListener {
    void onSessionClick(BookingHome_Helper item);
    void onCheckedClick(BookingHome_Helper item1,int session);
    void onDeChecked(int positionSes);

}

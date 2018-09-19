package com.techie_dany.srecshb.admin.interfaces;

import android.view.View;

import com.techie_dany.srecshb.admin.helpers.PendingsHelper;

public interface Admin_Listener {
    void onPendingsCardClick(PendingsHelper temp_in_help);
    void onbookedCardClick(PendingsHelper temp_app_help);
}

package com.techie_dany.srecshb.Superadmin.listeners;

import com.techie_dany.srecshb.Superadmin.helpers.Inbox_Helper;

public interface Super_Listener {


    void onInboxCardClick(Inbox_Helper temp_in_help);
    void onApprovedCardClick(Inbox_Helper temp_app_help);

}

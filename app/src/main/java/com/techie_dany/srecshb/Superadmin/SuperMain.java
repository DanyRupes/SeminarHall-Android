package com.techie_dany.srecshb.Superadmin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.Superadmin.fragments.SuperAdmin_Inbox;
import com.techie_dany.srecshb.Superadmin.fragments.SuperAdmin_Approved;
import com.techie_dany.srecshb.admin_superadmin.SHB_Account;
import com.techie_dany.srecshb.admin_superadmin.Halls_Home;

import java.util.Iterator;
import java.util.Set;

public class SuperMain extends AppCompatActivity {


    AHBottomNavigationViewPager abottom_superman_viewpager;
    AHBottomNavigation abottom_superman_nav;
    private boolean notificationVisible = false;
    BottomNavAdapter adapter;
    public final String TAG= "superadminmain";
    private final int[] colors = {R.color.bottomtab_home, R.color.bottomtab_pending, R.color.bottomtab_booked,R.color.bottomtab_inbox,R.color.bottomtab_account};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_main);
        Intent in = getIntent();

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO);

        abottom_superman_viewpager = (AHBottomNavigationViewPager) findViewById(R.id.abottom_superman_viewpager);
        abottom_superman_nav =  (AHBottomNavigation) findViewById(R.id.abottom_superman_nav);

        setupViewpager();
        setBottomNavStyle();
        NotificationForMe();
        BottomNavItems();
        abottom_superman_nav.setCurrentItem(0);
        abottom_superman_nav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(!wasSelected){
                    abottom_superman_viewpager.setCurrentItem(position,true);
                }
                int lastItem = abottom_superman_nav.getItemsCount()-1;

                if(notificationVisible && position == lastItem){
                    abottom_superman_nav.setNotification(new AHNotification(),lastItem);
                }
                return true;
            }
        });

        try {
            if(in.getData() !=null) {
                Log.i(TAG, "onCreate: " + in.getData());
                Log.i(TAG, "onCreate: " + in.getExtras());
                Log.i(TAG, "onCreate: " + in.getStringExtra("route"));

                if (in.getStringExtra("route").equals("inbox")) {
                    abottom_superman_nav.setCurrentItem(2);
                }

//                in.putExtra("local","vc1");
                if(in.getStringExtra("local").equals("vc1")){
                    abottom_superman_nav.setCurrentItem(2);
                }
            }

        }
        catch (Exception e){
            Log.i(TAG, "onCreate: "+e);
        }



    }


    public void setupViewpager(){
        abottom_superman_viewpager.setPagingEnabled(false); //swipe navigation
         adapter = new BottomNavAdapter(getSupportFragmentManager());
        abottom_superman_viewpager.setAdapter(adapter);
    }

    public class BottomNavAdapter extends FragmentPagerAdapter {

        public BottomNavAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment= null;
            switch (position){
                case 0:
                    fragment = new Halls_Home().newInstance();
                    break;
                case 1:
                    Log.i(TAG, "approved into");
                    fragment = new SuperAdmin_Approved().newInstance();
                    break;
                case 2:
                    Log.i(TAG, "inbox into");
                    fragment = new SuperAdmin_Inbox().newInstance();
                    break;
                case 3:
                    Log.i(TAG, "account into");
                    fragment = new SHB_Account().newInstance();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 4;
        }
    }


    public void setBottomNavStyle(){
        abottom_superman_nav.setDefaultBackgroundColor(Color.parseColor("#19cce8"));
        abottom_superman_nav.setColoredModeColors(Color.WHITE,
                fetchColor(R.color.not_Selected));

        //  Enables Reveal effect
        abottom_superman_nav.setColored(true);

        //  Displays item Title always (for selected and non-selected items)
        abottom_superman_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

    }
    public void setupBottomNavBehaviors() {
//        bottomNavigation.setBehaviorTranslationEnabled(false);

        /*
        Before enabling this. Change MainActivity theme to MyTheme.TranslucentNavigation in
        AndroidManifest.
        Warning: Toolbar Clipping might occur. Solve this by wrapping it in a LinearLayout with a top
        View of 24dp (status bar size) height.
         */
        abottom_superman_nav.setTranslucentNavigationEnabled(false);
    }
    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }

    public void NotificationForMe(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText("Requests")
                        .setBackgroundColor(Color.YELLOW)
                        .setTextColor(Color.BLACK)
                        .build();
                // Adding notification to last item.

                abottom_superman_nav.setNotification(notification, 2);

                notificationVisible = true;
            }
        }, 1000);
    }

    public void BottomNavItems(){
        AHBottomNavigationItem item_home = new AHBottomNavigationItem(R.string.admin_title_home, R.drawable.ic_home_black_24dp, colors[0]);
        AHBottomNavigationItem item_approved = new AHBottomNavigationItem(R.string.admin_title_approved, R.drawable.ic_home_black_24dp, colors[1]);
        AHBottomNavigationItem item_inbox = new AHBottomNavigationItem(R.string.admin_title_inbox, R.drawable.ic_dashboard_black_24dp, colors[3]);
        AHBottomNavigationItem item_account = new AHBottomNavigationItem(R.string.admin_title_account, R.drawable.ic_notifications_black_24dp, colors[4]);

        abottom_superman_nav.addItem(item_home);
        abottom_superman_nav.addItem(item_approved);
        abottom_superman_nav.addItem(item_inbox);
        abottom_superman_nav.addItem(item_account);
    }

}

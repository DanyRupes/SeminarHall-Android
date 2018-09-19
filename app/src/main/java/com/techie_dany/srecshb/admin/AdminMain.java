package com.techie_dany.srecshb.admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.techie_dany.srecshb.admin_superadmin.SHB_Account;
import com.techie_dany.srecshb.admin.fragments.Booked;
import com.techie_dany.srecshb.admin.fragments.Pending;
import com.techie_dany.srecshb.admin_superadmin.Halls_Home;
import com.techie_dany.srecshb.R;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;


//import com.aurelhubert.ahbottomnavigation.notification.AHNotification;


public class AdminMain extends AppCompatActivity {

    private TextView mTextMessage;
    public final static String TAG = "adminmain";


    private AHBottomNavigation bottomNavigation;
    private BottomBarAdapter pagerAdapter;
    private AHBottomNavigationViewPager viewPager;
    private boolean notificationVisible = false;
    private final int[] colors = {R.color.bottomtab_home, R.color.bottomtab_pending, R.color.bottomtab_booked,R.color.bottomtab_inbox,R.color.bottomtab_account};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        viewPager = (AHBottomNavigationViewPager) findViewById(R.id.viewpager_home);
        setupViewPager();
        setupBottomNavStyle();
        createFakeNotification();
        addBottomNavigationItems();
//        setupBottomNavBehaviors();

        bottomNavigation.setCurrentItem(0);

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
//                fragment.updateColor(ContextCompat.getColor(MainActivity.this, colors[position]));

                if (!wasSelected)
                    viewPager.setCurrentItem(position);
                // remove notification badge
                int lastItemPos = bottomNavigation.getItemsCount() - 1;
                if (notificationVisible && position == lastItemPos)
                    bottomNavigation.setNotification(new AHNotification(), lastItemPos);
                    return true;
            }
        });


        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                            if(!task.isSuccessful()){
                                Log.i(TAG, "failed ");
                                return ;
                            }
                            String token = task.getResult().getToken();

                            Log.i(TAG, "onComplete: TokenID is "+token);
                    }
                });


        Intent in  = getIntent();
        try {
            Log.i(TAG, "onCreate: "+in.getData());
            Log.i(TAG, "onCreate: "+in.getExtras());
            Log.i(TAG, "onCreate: "+in.getStringExtra("route"));

            if(in.getStringExtra("route").equals("booked")){
                bottomNavigation.setCurrentItem(2);
            }
            if(in.getStringExtra("route").equals("cancel")){
                Log.i(TAG, "goto admin cancelled part");
            }

        }
        catch (Exception e){
            Log.i(TAG, "onCreate: "+e);
        }


    }
    private void setupViewPager() {

        viewPager.setPagingEnabled(false);
        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }


    public class BottomBarAdapter extends FragmentPagerAdapter {

        public BottomBarAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment fragment = null;
            Log.i(TAG, "position: " + position);
            switch (position) {
                case 0:
                    fragment = new Halls_Home().newInstance();
                    break;

                case 1:
                    fragment = new Pending().newInstance();
                    break;

                case 2:
                    fragment = new Booked().newInstance();
                    break;
                case 3:
                    fragment = new SHB_Account().newInstance();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
    private void createFakeNotification() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText("1")
                        .setBackgroundColor(Color.YELLOW)
                        .setTextColor(Color.BLACK)
                        .build();
                // Adding notification to last item.

                bottomNavigation.setNotification(notification, 2);

                notificationVisible = true;
            }
        }, 1000);
    }
    public void setupBottomNavBehaviors() {
//        bottomNavigation.setBehaviorTranslationEnabled(false);

        /*
        Before enabling this. Change MainActivity theme to MyTheme.TranslucentNavigation in
        AndroidManifest.
        Warning: Toolbar Clipping might occur. Solve this by wrapping it in a LinearLayout with a top
        View of 24dp (status bar size) height.
         */
        bottomNavigation.setTranslucentNavigationEnabled(false);
    }

    public void addBottomNavigationItems() {
        AHBottomNavigationItem item_home = new AHBottomNavigationItem(R.string.admin_title_home, R.drawable.ic_home_black_24dp, colors[0]);
        AHBottomNavigationItem item_pending = new AHBottomNavigationItem(R.string.admin_title_pending, R.drawable.ic_home_black_24dp, colors[1]);
        AHBottomNavigationItem item_booked = new AHBottomNavigationItem(R.string.admin_title_booked, R.drawable.ic_dashboard_black_24dp, colors[2]);
        AHBottomNavigationItem item_account = new AHBottomNavigationItem(R.string.admin_title_account, R.drawable.ic_notifications_black_24dp, colors[3]);

        bottomNavigation.addItem(item_home);
        bottomNavigation.addItem(item_pending);
        bottomNavigation.addItem(item_booked);
        bottomNavigation.addItem(item_account);
    }
    private int fetchColor(@ColorRes int color) {
        return ContextCompat.getColor(this, color);
    }
    private void setupBottomNavStyle() {
        /*
        Set Bottom Navigation colors. Accent color for active item,
        Inactive color when its view is disabled.
        Will not be visible if setColored(true) and default current item is set.
         */
        bottomNavigation.setDefaultBackgroundColor(Color.BLACK);
//        bottomNavigation.setAccentColor(fetchColor(R.color.bottomtab_0));
//        bottomNavigation.setInactiveColor(fetchColor(R.color.bottomtab_item_resting));

        // Colors for selected (active) and non-selected items.
        bottomNavigation.setColoredModeColors(Color.WHITE,
                fetchColor(R.color.not_Selected));

        //  Enables Reveal effect
        bottomNavigation.setColored(true);

        //  Displays item Title always (for selected and non-selected items)
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
    }
}
//    loadFragment(new Halls_Home());
//    BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//
//        AdminMain.stopBottomMovingNav(navigation);
//
//        navigation.setOnNavigationItemSelectedListener(this);

    //
//    @Override

//    public boolean bottomNavigation.setOnTabSelectedListener(new )
//
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Fragment fragment = null;
//        switch (item.getItemId()){
//            case R.id.admin_navigation_home:
//                    fragment = new Halls_Home().newInstance();
//                    break;
//
//                case R.id.admin_navigation_pending:
//                    fragment = new Pending().newInstance();
//                    break;
//
//                case R.id.admin_navigation_booked:
//                    fragment = new Booked().newInstance();
//                    break;
//                case R.id.admin_navigation_account:
//                    fragment = new SHB_Account().newInstance();
//                    break;
//        }
//        return loadFragment(fragment);
//    }
//
//    public boolean loadFragment(android.app.Fragment fragment){
//        if (fragment != null) {
//            getFragmentManager()
//                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//                    .replace(R.id.admin_frame_container, fragment)
//                    .commit();
//            return true;
//        }
//        return false;
//    }


//    @SuppressLint("RestrictedApi")
//    static void stopBottomMovingNav(BottomNavigationView navigation) {
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
//
//
//        try {
//            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
//            shiftingMode.setAccessible(true);
//            shiftingMode.setBoolean(menuView, false);
//            for (int i = 0; i < menuView.getChildCount(); i++) {
//                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
//                item.setShiftingMode(false);
//                item.setChecked(item.getItemData().isChecked());
//            }
//
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (NoSuchFieldException e) {
//            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
//        }
//    }
//}



//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//
//            Admin_Halls_Home admin_halls_home = new Admin_Halls_Home();
//            Booked admin_booked = new Booked();
//            SHB_Account admin_account = new SHB_Account();
//            Fragment active = admin_halls_home.newInstance();
//
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            android.support.v4.app.Fragment fragment = null;
//
//
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    Log.i(TAG, "navigation_home" +item.getItemId());
//                    fragment = admin_halls_home.newInstance();
//                    break;
//
//                case R.id.navigation_dashboard:
//                    Log.i(TAG, "navigation_notifications");
//
//                    fragment = admin_booked.newInstance();
//                    break;
//
//                case R.id.navigation_notifications:
//                    Log.i(TAG, "navigation_notifications");
//                    fragment = admin_account.newInstance();
//                    break;
//            }
//            transaction.replace(R.id.fragment_ahls,fragment).commit();
//
//            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            return true;
//        }
//
//
//    };

package com.techie_dany.srecshb;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.techie_dany.srecshb.admin.activities.BookingZet;
import com.techie_dany.srecshb.admin.calander.CaldroidCustomFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.roomorama.caldroid.CaldroidFragment.DIALOG_TITLE;

public class TestActivity extends AppCompatActivity {




//    private boolean undo = false;
//    private CaldroidFragment caldroidFragment;
//    private CaldroidFragment dialogCaldroidFragment;
//
//
//    private void setCustomResourceForDates() {
//        Calendar cal = Calendar.getInstance();
//
//        // Min date is last 7 days
//        cal.add(Calendar.DATE, -7);
//        Date blueDate = cal.getTime();
//
//        // Max date is next 7 days
//        cal = Calendar.getInstance();
//        cal.add(Calendar.DATE, 7);
//        Date greenDate = cal.getTime();
//
//        if (caldroidFragment != null) {
//            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.bg_dark));
//            ColorDrawable green = new ColorDrawable(Color.GREEN);
//            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
//            caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
//            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
//            caldroidFragment.setTextColorForDate(R.color.white, greenDate);
//        }
//    }
//
//    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);






//        NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
//        dialogBuilder
//                .withTitle("Confirmation")                                  //.withTitle(null)  no title
//                .withTitleColor("#9980FA")                                  //def
//                .withDividerColor("#ffffff")                              //def
//                .withMessage("About booking")                     //.withMessage(null)  no Msg
//                .withMessageColor("#5758BB")                              //def  | withMessageColor(int resid)
//                .withDialogColor("#f5f6fa")                               //def  | withDialogColor(int resid)
//                .withIcon(getResources().getDrawable(R.drawable.confirm_schedule))
//                .withDuration(700)                                          //def
//                .withEffect(Effectstype.Slit)                                         //def Effectstype.Slidetop
//                .withButton1Text("Yes")                                      //def gone
//                .withButton2Text("Not Now")                                  //def gone
//                .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
//                .setCustomView(R.layout.zetview,this)         //.setCustomView(View or ResId,context)
//                .setButton1Click(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(v.getContext(), "Yes", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setButton2Click(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(v.getContext(),"No",Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .show();

        Configuration cfg=new Configuration.Builder()
                .setAnimDuration(700)
                .setDispalyDuration(1000)
                .setBackgroundColor("#ffffff")
                .setTextColor("#2f3542")
                .setIconBackgroundColor("#f1f2f6")
                .setTextPadding(5)                      //dp
                .setViewHeight(48)                      //dp
                .setTextLines(2)                        //You had better use setViewHeight and setTextLines together
                .setTextGravity(Gravity.CENTER)         //only text def  Gravity.CENTER,contain icon Gravity.CENTER_VERTICAL
                .build();

        NiftyNotificationView.build(TestActivity.this,"Give Some Time for Your Confusion", Effects.thumbSlider,R.id.mLyout,cfg)
                .setIcon(R.drawable.customer_service)               //remove this line ,only text
                .show();

    }
//
//
//        // Setup caldroid fragment
//        // **** If you want normal CaldroidFragment, use below line ****
////        caldroidFragment = new CaldroidFragment();
//
//        // //////////////////////////////////////////////////////////////////////
//        // **** This is to show customized fragment. If you want customized
//        // version, uncomment below line ****
//        caldroidFragment = new CaldroidCustomFragment();
//
//        // Setup arguments
//
//        // If Activity is created after rotation
//        if (savedInstanceState != null) {
//            caldroidFragment.restoreStatesFromKey(savedInstanceState,
//                    "CALDROID_SAVED_STATE");
//        }
//        // If activity is created from fresh
//        else {
//            Bundle args = new Bundle();
//            Calendar cal = Calendar.getInstance();
//            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
//            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
//            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
//            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
//
//            // Uncomment this to customize startDayOfWeek
//            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
//            // CaldroidFragment.TUESDAY); // Tuesday
//
//            // Uncomment this line to use Caldroid in compact mode
//            // args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
//
//            // Uncomment this line to use dark theme
//
//            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
//
//            caldroidFragment.setArguments(args);
//        }
//
//        setCustomResourceForDates();
//
//        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
//        t.replace(R.id.calendar1, caldroidFragment);
//        t.commit();
//
//
//
//
//    final CaldroidListener listener = new CaldroidListener() {
//
//        @Override
//        public void onSelectDate(Date date, View view) {
//            Toast.makeText(getApplicationContext(), formatter.format(date),
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onChangeMonth(int month, int year) {
//            String text = "month: " + month + " year: " + year;
//            Toast.makeText(getApplicationContext(), text,
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onLongClickDate(Date date, View view) {
//            Toast.makeText(getApplicationContext(),
//                    "Long click " + formatter.format(date),
//                    Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onCaldroidViewCreated() {
//            if (caldroidFragment.getLeftArrowButton() != null) {
//                Toast.makeText(getApplicationContext(),
//                        "Caldroid view is created", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        }
//
//    };
//
//
//
//        Button showDialogButton = (Button) findViewById(R.id.show_dialog_button);
//
//        final Bundle state = savedInstanceState;
//        showDialogButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // Setup caldroid to use as dialog
//                dialogCaldroidFragment = new CaldroidFragment();
//                dialogCaldroidFragment.setCaldroidListener(listener);
//                dialogCaldroidFragment.setMinDate(new Date());
//                // If activity is recovered from rotation
//                final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
//                if (state != null) {
//                    dialogCaldroidFragment.restoreDialogStatesFromKey(
//                            getSupportFragmentManager(), state,
//                            "DIALOG_CALDROID_SAVED_STATE", dialogTag);
//                    Bundle args = dialogCaldroidFragment.getArguments();
//                    if (args == null) {
//                        args = new Bundle();
//                        dialogCaldroidFragment.setArguments(args);
//                    }
//                } else {
//                    Bundle args = new Bundle();
//                    Calendar cal = Calendar.getInstance();
//                    args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
//                    args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
//                    args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
//                    args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
//
//                    // Uncomment this to customize startDayOfWeek
//                    // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
//                    // CaldroidFragment.TUESDAY); // Tuesday
//
//                    // Uncomment this line to use Caldroid in compact mode
//                     args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
//
//                    // Uncomment this line to use dark theme
//                    args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
//
//                    dialogCaldroidFragment.setArguments(args);
//                }
//
//                dialogCaldroidFragment.show(getSupportFragmentManager(),
//                        dialogTag);
//            }
//        });
//    }
//
//    /**
//     * Save current states of the Caldroid here
//     */
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        // TODO Auto-generated method stub
//        super.onSaveInstanceState(outState);
//
//        if (caldroidFragment != null) {
//            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
//        }
//
//        if (dialogCaldroidFragment != null) {
//            dialogCaldroidFragment.saveStatesToKey(outState,
//                    "DIALOG_CALDROID_SAVED_STATE");
//        }
//    }
}



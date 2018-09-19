package com.techie_dany.srecshb.admin.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;


import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.admin.adapters.Booking_home_SAdapter;
import com.techie_dany.srecshb.admin.helpers.BookingHome_Helper;
import com.techie_dany.srecshb.admin.interfaces.ItemListener;
import com.techie_dany.srecshb.certificate.CertificateMain;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class BookingHome extends AppCompatActivity implements ItemListener {
        private final String TAG = "BookingHome";
        public  DatePickerDialog Datedialog;
        public RecyclerView admin_halls_book_recycle;
        public  GridLayoutManager gridLayoutManager;
        public Booking_home_SAdapter bookingHomeSAdapter;
        public ActionBar actionBar;
        int checkCake=0;
        String dateAA;
        int hall_id;
        CaldroidFragment caldroidFragment;
        public String finalDate;

        public FloatingActionButton fab_show_caldroid;
    //    Toolbar toolbar_book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_home);
        Intent in = getIntent();
        hall_id = in.getExtras().getInt("hall_id");

        Calendar calendar = Calendar.getInstance();
        int Year = calendar.get(Calendar.YEAR);
        int Month = calendar.get(Calendar.MONTH);
        final int Day = calendar.get(Calendar.DAY_OF_MONTH);

        Log.i(TAG, "onCreate: "+hall_id);
        final Context context = this;

        fab_show_caldroid = (FloatingActionButton) findViewById(R.id.fab_show_caldroid);

        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        caldroidFragment = new CaldroidFragment();
        final Bundle state = savedInstanceState;
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Log.i(TAG, "onSelectDate: "+formatter.format(date));
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
                caldroidFragment.setSelectedDate(date);
                finalDate = formatter.format(date);
                try {
                    new dated(context,hall_id).execute(formatter.format(date));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
//                Toast.makeText(getApplicationContext(), text,
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Log.i(TAG, "Long click: "+formatter.format(date));
                caldroidFragment.setSelectedDate(date);
                finalDate = formatter.format(date);
                try {
                    new dated(context,hall_id).execute(formatter.format(date));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
//                    Toast.makeText(getApplicationContext(),
//                            "Caldroid view is created", Toast.LENGTH_SHORT)
//                            .show();
                }
            }

        };
        caldroidFragment.setCaldroidListener(listener);
        caldroidFragment.setMinDate(new Date());

        // If activity is recovered from rotation
        final String dialogTag = "CALDROID_DIALOG_FRAGMENT";
        if (state != null) {
            caldroidFragment.restoreDialogStatesFromKey(
                    getSupportFragmentManager(), state,
                    "DIALOG_CALDROID_SAVED_STATE", dialogTag);
            Bundle args = caldroidFragment.getArguments();
            if (args == null) {
                args = new Bundle();
                caldroidFragment.setArguments(args);
            }
        } else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            // Uncomment this to customize startDayOfWeek
            // args.putInt(CaldroidFragment.START_DAY_OF_WEEK,
            // CaldroidFragment.TUESDAY); // Tuesday

            // Uncomment this line to use Caldroid in compact mode
//            args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);

            // Uncomment this line to use dark theme
            args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);

            caldroidFragment.setArguments(args);
        }

        caldroidFragment.show(getSupportFragmentManager(),
                dialogTag);

        fab_show_caldroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                caldroidFragment.show(getSupportFragmentManager(),
                        dialogTag);

            }
        });









//        Datedialog.show();


        admin_halls_book_recycle = (RecyclerView) findViewById(R.id.admin_halls_book_recycle);

        gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);

//        toolbar_book = (Toolbar) findViewById(R.id.toolbar_book_check);

//        toolbar_book.setVisibility(View.INVISIBLE);

         actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.hide();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState,
                    "DIALOG_CALDROID_SAVED_STATE");
        }
    }


    @Override
    public void onSessionClick(BookingHome_Helper bhk){
        Log.i(TAG, "onSessionClick: "+bhk.getStatus());
        if(bhk.getStatus()==-1){
            Toast.makeText(BookingHome.this,"Requested by " +bhk.getBy(),Toast.LENGTH_SHORT).show();
        }
        else if(bhk.getStatus()==1){
            Toast.makeText(BookingHome.this,"Booked by " +bhk.getBy(),Toast.LENGTH_SHORT).show();
        }
        else {

        }
    }

    int cake = 0;
    ArrayList<Integer> mySession = new ArrayList<>(Arrays.asList(new Integer[8]));

    @Override
    public void onCheckedClick(BookingHome_Helper book, final int session){
        int status = book.getStatus();
        if(status==1){
            Toast.makeText(this, "Booked ",Toast.LENGTH_SHORT).show();
        }
        else {
            Log.i(TAG, "onCheckedClick: "+session);

            mySession.set(session,session+1);
            actionBar.show();
            LayoutInflater inflater = LayoutInflater.from(BookingHome.this);
            @SuppressLint("ResourceType") View view = inflater.inflate(R.layout.layout_menu_bookhome, null);

            actionBar.setCustomView(view);
            actionBar.setDisplayShowCustomEnabled(true);

            Button addtext = (Button) view.findViewById(R.id.book_home_addText);
            Button cancelText = (Button) view.findViewById(R.id.book_home_cancelText);


            cake++;
            addtext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "add : "+mySession.size());
                   Intent intent = new Intent(BookingHome.this, BookingZet.class);

                   intent.putIntegerArrayListExtra("session_id", mySession);
                   intent.putExtra("hall_id",hall_id);
                   intent.putExtra("date",finalDate);
                    Log.i(TAG, "intent "+hall_id+" "+finalDate+" "+mySession);
                    startActivity(intent);
                    finish();
                }
            });
            cancelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "cancel : "+mySession.size());
                    mySession.clear();
                    mySession = new ArrayList<>(Arrays.asList(new Integer[8]));
                    admin_halls_book_recycle.setAdapter(bookingHomeSAdapter);
                    actionBar.hide();
                }
            });
//            toolbar_book.setVisibility(View.VISIBLE);
//            toolbar_book.setBackgroundColor(Color.parseColor("#009688"));

        }
    }



    @Override
    public void onDeChecked(int session){
        mySession.clear();

        Log.i(TAG, "onDeChecked: "+mySession.size());
        cake--;
        if(cake<1){
            actionBar.hide();
        }
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_booking_home,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.book_home_add:
//                Log.i(TAG, "onOptionsItemSelected: "+item.getItemId());
//        }
//        return super.onOptionsItemSelected(item);
//    }


    public class dated extends AsyncTask<String, ArrayList<BookingHome_Helper>, ArrayList<BookingHome_Helper>>{

        int hall_id_;
        String date_;
        Context context;
        HttpsURLConnection connection;


        public dated(Context context, int hall_id_) throws MalformedURLException {
            this.context = context;
            this.hall_id_  = hall_id_;
        }


        @Override
        protected void onPreExecute(){
            caldroidFragment.onDestroyView();

//            Log.i(TAG, "onPreExecute: ");
        }

        URL url = new URL("https://srec-shb.cf/shb/hall/id/bookings/date");
//        URL url = new URL("http://192.168.1.103/shb/hall/id/bookings/date");

        @Override
        protected ArrayList<BookingHome_Helper> doInBackground(String... strings) {
            ArrayList<BookingHome_Helper> bkh_helpList = new ArrayList<>(Arrays.asList(new BookingHome_Helper[8]));
            date_  = strings[0];
            Log.i(TAG, "onPreExecute: "+hall_id_+" "+date_);
            try {
                connection = (HttpsURLConnection) url.openConnection();
                CertificateMain Cm = new CertificateMain();
                connection.setDefaultSSLSocketFactory(Cm.getMyCertificate());
                connection.setDefaultHostnameVerifier(Cm.all_hosted);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");//x-www-form-urlencoded
                connection.setRequestProperty("Accept","application/json");
                connection.setRequestProperty("Accept-Language","en-US");
                connection.setDoOutput(true);

                    DataOutputStream dout = new DataOutputStream(connection.getOutputStream());
//                    Log.i(TAG, "doInBackground: " + hall_id_ + " " + date_);

                    JSONObject jb_details = new JSONObject();
                    jb_details.put("hall_id", hall_id_);
                    jb_details.put("date", date_);
                    dout.writeBytes(jb_details.toString());
                    dout.flush();
                    dout.close();
                Log.i(TAG, "doInBackground: "+jb_details);
//                    Log.i(TAG, "StatusCode" + connection.getResponseCode());
                if(connection.getResponseCode()==302) {

                    BufferedReader dt_bfread = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String readLine;
                    StringBuffer date_buf = new StringBuffer();
                    while ((readLine = dt_bfread.readLine()) != null) {
                        date_buf.append(readLine);
                    }


                    JSONArray detailed_date = new JSONArray(date_buf.toString());

                    for (int i = 0; i < detailed_date.length(); i++) {
//                        Log.i(TAG, "doIn" + detailed_date.get(i));
                        JSONObject jsonObject = new JSONObject(detailed_date.get(i).toString());

                        JSONArray sessions_id = jsonObject.getJSONArray("session_id");
                        int status = jsonObject.getInt("status");
                        String book_desc = jsonObject.getString("book_desc");
                        String by = jsonObject.getString("by");

                        BookingHome_Helper book = new BookingHome_Helper(sessions_id, status, book_desc, by); //db values

                        for (int j=0;j<sessions_id.length();j++){
                            Log.i(TAG, "checkCake "+checkCake);
                            checkCake++;
//                            Log.i(TAG, "doInBackground: "+(sessions_id.getInt(j)-1));
                            bkh_helpList.add(sessions_id.getInt(j)-1,book);
                        }
                    }
                    for(int n=0;n<8;n++){
                        if(bkh_helpList.get(n) == null){
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(n);
                            BookingHome_Helper book1 = new BookingHome_Helper(jsonArray, 0, " ", " "); //db values
                            bkh_helpList.set(n,book1); //local values
//                            Log.i(TAG, "  "+n);
                        }
                    }
                }
                else {
                    checkCake = 8;
                    Log.i(TAG, "++++__________ Booking Home_____No Data "+connection.getResponseCode());
                    for(int n=0;n<8;n++){
                        if(bkh_helpList.get(n) == null){
//                            Log.i(TAG, " adding "+n);
                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(n+1);
                            BookingHome_Helper book1 = new BookingHome_Helper(jsonArray, 0, " ", " "); //db values
                            bkh_helpList.set(n,book1); //local values
//                            Log.i(TAG, "  "+n);
                        }
                    }
                }
            }
            catch (Exception e){
                Log.i(TAG, "doInB Exception" +e);
            }
            return bkh_helpList;
        }


        @Override
        protected void onPostExecute(ArrayList<BookingHome_Helper> s) {
//            Log.i(TAG, "onPostExecute: \n" +s);
            admin_halls_book_recycle.setLayoutManager(gridLayoutManager);
            bookingHomeSAdapter = new Booking_home_SAdapter(context,s,BookingHome.this);
            admin_halls_book_recycle.setAdapter(bookingHomeSAdapter);
        }
    }
}


//        Datedialog = new DatePickerDialog(this,
//                new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker view, int year, int month, int day) {
//
//                month +=1;
//
//                String final_day,final_month;
//
//                if(String.valueOf(day).length() == 1){
//
//                    final_day = "0"+String.valueOf(day);
//                }else {final_day = String.valueOf(day);}
//
//                if(String.valueOf(month).length() == 1){
//                    final_month= "0"+String.valueOf(month);
//                }else {final_month = String.valueOf(month);}
//
//
//                dateAA = final_day+"-"+final_month+"-"+year;


//                try {
////                    new dated(context,hall_id).execute(dateAA);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, Year, Month, Day);

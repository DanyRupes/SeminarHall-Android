package com.techie_dany.srecshb.admin.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.gitonway.lee.niftynotification.lib.Configuration;
import com.gitonway.lee.niftynotification.lib.Effects;
import com.gitonway.lee.niftynotification.lib.NiftyNotificationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Result;
import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.certificate.CertificateMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class BookingZet extends AppCompatActivity {


    String date;
    int hall_id;
    public static final String TAG="bookzoon";
    public TextView book_zet_nameText,book_zet_descText;
    JSONArray session_id;
    Button btn_bookzet_book;
    ProgressBar progress_book_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_zet);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        hall_id = intent.getIntExtra("hall_id",000);
        date = intent.getStringExtra("date");
        ArrayList<Integer> list = intent.getIntegerArrayListExtra("session_id");
        list.removeAll(Collections.singleton(null));
        Log.i(TAG, "trimmed" +list);
        int i=0,j=0;
        String k;
        session_id = new JSONArray(list);
//        while (i<8){
//            if(list.get(i) !=null){
//                Log.i(TAG, "not null");
//                session_id[j] = list.get(i);
//                j++;
//            }
//            i++;
//        }
        Log.i(TAG, "onCreate: "+session_id+" "+hall_id+" "+date);
        Log.i(TAG, "onCreate: "+list);


        book_zet_nameText = (TextView) findViewById(R.id.book_zet_nameText);
        book_zet_descText = (TextView) findViewById(R.id.book_zet_descText);
        btn_bookzet_book = (Button) findViewById(R.id.btn_bookzet_book);
        progress_book_btn = (ProgressBar) findViewById(R.id.progress_book_btn);
        progress_book_btn.setVisibility(View.INVISIBLE);

        btn_bookzet_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((book_zet_descText.getText().toString().trim().length()!=0) && !((book_zet_nameText.getText().toString().trim().equals("")))){
                    book_zet_descText.setError(null);
                    book_zet_nameText.setError(null);
                    final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(BookingZet.this);
                    dialogBuilder
                            .withTitle("Confirmation")                                  //.withTitle(null)  no title
                            .withTitleColor("#f5f6fa")                                  //def
                            .withDividerColor("#ffffff")                              //def
                            .withMessage("About booking")                     //.withMessage(null)  no Msg
                            .withMessageColor("#5758BB")                              //def  | withMessageColor(int resid)
                            .withDialogColor("#ffffff")                               //def  | withDialogColor(int resid)
                            .withIcon(getResources().getDrawable(R.drawable.confirm_schedule))
                            .withDuration(700)                                          //def
                            .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                            .withButton1Text("Yes")                                      //def gone
                            .withButton2Text("Not Now")                                  //def gone
                            .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                            .setCustomView(R.layout.zetview,BookingZet.this)         //.setCustomView(View or ResId,context)
                            .setButton1Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AsyncBook().execute();
                                }
                            })
                            .setButton2Click(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialogBuilder.hide();
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

                                    NiftyNotificationView.build(BookingZet.this,"Give Some Time for Your Confusion", Effects.thumbSlider,R.id.zetNotify,cfg)
                                            .setIcon(R.drawable.customer_service)               //remove this line ,only text
                                            .show();
                                }
                            })
                            .show();



                }
                else {
                    if (book_zet_nameText.getText().toString().trim().equals("")){
                        book_zet_nameText.setError("Please Give Name");
                    }
                    else {
                        book_zet_descText.setError("Please Fill Description of the Event");
                    }
                }




            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        super.onBackPressed();
        return  true;
    }

    public class AsyncBook extends AsyncTask<String,Void,String>{

        HttpsURLConnection connection;
//        SharedPreferences shbBook = getSharedPreferences("srec-shb",MODE_PRIVATE);
//        String email = shbBook.getString("user_email"," ");
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(BookingZet.this);
        String email = googleSignInAccount.getEmail();

        @Override
        public void onPreExecute(){
            progress_book_btn.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL("https://srec-shb.cf/admin/book_hall");
                connection = (HttpsURLConnection) url.openConnection();
                CertificateMain Cm = new CertificateMain();
                connection.setDefaultSSLSocketFactory(Cm.getMyCertificate());
                connection.setDefaultHostnameVerifier(Cm.all_hosted);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                connection.setRequestProperty("Accept-Language","en-US");
                connection.setRequestProperty("Accept","application/json");

                Log.i(TAG, "Booking Details : "+email+ " "+hall_id+" "+date+" "+session_id +" "+book_zet_nameText.getText().toString().trim()+" "+book_zet_descText.getText().toString().trim());
                JSONObject jsonObBook = new JSONObject();
                jsonObBook.put("hall_id",hall_id);
                jsonObBook.put("email",email);
                jsonObBook.put("date",date);
                jsonObBook.put("session_id",session_id);
                jsonObBook.put("status",1);
                jsonObBook.put("by",book_zet_nameText.getText().toString().trim());
                jsonObBook.put("book_desc",book_zet_descText.getText().toString().trim());

                DataOutputStream bookout = new DataOutputStream(connection.getOutputStream());
                bookout.writeBytes(jsonObBook.toString());
                bookout.flush();
                bookout.close();


                Log.i(TAG, "connection "+connection);
                Log.i(TAG, "Response "+connection.getResponseCode());


                System.out.println(connection.getResponseCode());
                int responseCode = connection.getResponseCode();

                if(responseCode == 404){
                    System.out.println(responseCode);
                }
                else if (responseCode==401){
                    System.out.println(responseCode);
                }
                else if (responseCode==406){
                    System.out.println(responseCode);
                }
                else if (responseCode==201 || responseCode==202 || responseCode==200){
                    System.out.println(responseCode);

                    System.out.println("yeah "+connection.getResponseMessage());

                    BufferedReader postBuffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer id_post_buffer = new StringBuffer();
                    String id_res_line;
                    while ((id_res_line = postBuffer.readLine()) != null) {
                        id_post_buffer.append(id_res_line);
                    }
                    JSONObject id_post_res = new JSONObject(id_post_buffer.toString());
                    System.out.println(id_post_res);
                }

                else {
                    System.out.println("Something went Wrong");
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            progress_book_btn.setVisibility(View.INVISIBLE);
            Toast.makeText(BookingZet.this, "",Toast.LENGTH_SHORT).show();
            Toast.makeText(BookingZet.this, "Please Wait for Approval",Toast.LENGTH_SHORT).show();
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

            NiftyNotificationView.build(BookingZet.this,"Request Sent Succcessfully..! Please Wait for Approval", Effects.thumbSlider,R.id.mLyout,cfg)
                    .setIcon(R.drawable.ic_audiotrack_light)               //remove this line ,only text
                    .show();
            finish();
        }
    }

}

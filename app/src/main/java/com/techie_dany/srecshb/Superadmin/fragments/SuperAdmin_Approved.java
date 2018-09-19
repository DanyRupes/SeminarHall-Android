package com.techie_dany.srecshb.Superadmin.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.Superadmin.adapters.InBoxandApproved_Adapter;
import com.techie_dany.srecshb.Superadmin.helpers.Inbox_Helper;
import com.techie_dany.srecshb.Superadmin.listeners.Super_Listener;
import com.techie_dany.srecshb.admin_superadmin.ViewChannel;
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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;


import static android.content.Context.MODE_PRIVATE;
import static org.apache.http.HttpHeaders.USER_AGENT;


public class SuperAdmin_Approved extends Fragment implements Super_Listener {


    public TextView inbox_empty_text;
    public ProgressBar progress_inbox;
    Context approvedContext;

    public final String TAG= "superadminapproved";

    public SuperAdmin_Approved() {
    }



    public static SuperAdmin_Approved newInstance() {
        SuperAdmin_Approved fragment = new SuperAdmin_Approved();


        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public RecyclerView inbox_recycler;
    public RecyclerView.LayoutManager layoutManager_approved;
    public InBoxandApproved_Adapter adapter_approved;
    public SharedPreferences shb_approved;
    int hall_id;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_super_admin_inbox, container, false);
        inbox_empty_text = (TextView) view.findViewById(R.id.inbox_empty_text);
        progress_inbox = (ProgressBar) view.findViewById(R.id.progress_inbox);
        shb_approved = view.getContext().getSharedPreferences("srec-shb",MODE_PRIVATE);
        Log.i(TAG, "inbox");
        hall_id = shb_approved.getInt("hall_id",0);

        inbox_recycler = (RecyclerView) view.findViewById(R.id.inbox_recycler);
        layoutManager_approved = new LinearLayoutManager(view.getContext());
        inbox_recycler.setLayoutManager(layoutManager_approved);
        inbox_recycler.setHasFixedSize(true);
        new getApproved(view.getContext()).execute(hall_id);
        progress_inbox.setVisibility(View.VISIBLE);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        approvedContext = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStart(){
        super.onStart();

    }


    public void onInboxCardClick(Inbox_Helper temp_in_help) {
        Log.i(TAG, "onInboxCardClick: Not  Overriding");
    }
    @Override
    public void onApprovedCardClick(Inbox_Helper temp_app_help) {
        Log.i(TAG, "onApprovedCardClick: Overriding");
        Toast.makeText(approvedContext,"Start Approved",Toast.LENGTH_SHORT).show();

        Intent approved_intent = new Intent(approvedContext,ViewChannel.class);
        ArrayList<Inbox_Helper> app_help_list = new ArrayList<>();
        app_help_list.add(temp_app_help);
        approved_intent.putParcelableArrayListExtra("approved_help",app_help_list);
        approved_intent.putExtra("channel_id",1);
        startActivity(approved_intent);
    }


    public class getApproved extends AsyncTask<Integer, ArrayList<Inbox_Helper>,  ArrayList<Inbox_Helper>>{

        Context contextApp;
        public getApproved(Context context){
            contextApp = context;
        }

        HttpsURLConnection connection;
        ArrayList<Inbox_Helper> approvedHelpersM = new ArrayList<>();
        @Override
        protected void onPreExecute(){

        }

        @Override
        protected   ArrayList<Inbox_Helper> doInBackground(Integer... data) {

            SharedPreferences shbpref = contextApp.getSharedPreferences("srec-shb",MODE_PRIVATE);
            int hall_id = shbpref.getInt("hall_id",101);

            try {
                String tt = "https://srec-shb.cf/super_admin/approved?hall_id="+hall_id;
//                String tt = "http://192.168.1.103/super_admin/approved?hall_id="+hall_id;
                URL url = new URL(tt);
                connection = (HttpsURLConnection) url.openConnection();
                CertificateMain cm = new CertificateMain();
                connection.setDefaultSSLSocketFactory(cm.getMyCertificate());
                connection.setDefaultHostnameVerifier(cm.all_hosted);
                connection.setRequestProperty("Accept-Language", "en-US");
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestMethod("POST");

                JSONObject tempJS = new JSONObject();
                tempJS.put("hall_id",data[0]);

                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(tempJS.toString());



                int responseCode = connection.getResponseCode();

                if (responseCode==200){
                    Log.i(TAG, "Okay approved");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String readline;

                    while ((readline =reader.readLine())!=null){
                        sb.append(readline);
                    }

                    Log.i(TAG, "SB Approved : "+sb);

                    JSONArray inboxJson;
                    try {
                        inboxJson = new JSONArray(sb.toString());
                        JSONObject inboxObject = (JSONObject) inboxJson.get(0);
                    }
                    catch (Exception e){
                        return approvedHelpersM;
                    }

                    int len = inboxJson.length();
                    for(int i=0;i<len;i++){
                        JSONObject inboxObject = (JSONObject) inboxJson.get(i);
                        Log.i(TAG, "approvedObj " + inboxObject);
                        String date = inboxObject.getString("date");
                        String email = inboxObject.getString("email");


                        JSONObject sessions = inboxObject.getJSONObject("sessions");

                        JSONArray session_id = sessions.getJSONArray("session_id");
                        String book_desc = sessions.getString("book_desc");
                        String by = sessions.getString("by");


                        StringBuffer ses_id = new StringBuffer();
                        for(int j=0;j<session_id.length();j++){
                            ses_id.append(session_id.getInt(j)+"  ");
                        }
                        Log.i(TAG, "do sessions inbox-----------" +hall_id+" "+ date + " "+email+" "+ ses_id.toString() + " " + book_desc + " " + by);
                        Inbox_Helper in = new Inbox_Helper(date,email,ses_id.toString(),book_desc,by,hall_id);
//                            Inbox_Helper in = new Inbox_Helper(container_id,date, session_id, by, book_desc);
                        approvedHelpersM.add(in);
                    }
                }
                else {
                    Log.i(TAG, "not 200 okay approved");
                }

            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch (Exception e){
                Log.i(TAG, "approved exception "+e);
            }
            return approvedHelpersM;
        }

        @Override
        protected void onPostExecute( ArrayList<Inbox_Helper> IhelperI) {

            Log.i(TAG, "onPost approved ");
            progress_inbox.setVisibility(View.INVISIBLE);
            if(IhelperI==null || IhelperI.isEmpty()){
                inbox_empty_text.setVisibility(View.VISIBLE);
            }
            else {
                Log.i(TAG, "onPost inbox else" +IhelperI);
                inbox_empty_text.setVisibility(View.INVISIBLE);
                adapter_approved = new InBoxandApproved_Adapter(approvedHelpersM,1,SuperAdmin_Approved.this);
                inbox_recycler.setAdapter(adapter_approved);
            }

        }
    }

}

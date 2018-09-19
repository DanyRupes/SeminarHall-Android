package com.techie_dany.srecshb.admin.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.Superadmin.adapters.InBoxandApproved_Adapter;
import com.techie_dany.srecshb.Superadmin.fragments.SuperAdmin_Inbox;
import com.techie_dany.srecshb.Superadmin.helpers.Inbox_Helper;
import com.techie_dany.srecshb.admin.adapters.PendingsAdapter;
import com.techie_dany.srecshb.admin.helpers.PendingsHelper;
import com.techie_dany.srecshb.admin.interfaces.Admin_Listener;
import com.techie_dany.srecshb.admin_superadmin.ViewChannel;
import com.techie_dany.srecshb.certificate.CertificateMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;


public class Pending extends Fragment implements Admin_Listener {


    public RecyclerView pendig_recycle;
    public TextView pending_empty_text;
    public ProgressBar pending_progress;
    public PendingsAdapter pendingsAdapter;

    public RecyclerView.LayoutManager layoutManager;

    public final String TAG= "pendings";
    public Context context;

    public Pending() {
        // Required empty public constructor
    }

    public  Fragment newInstance() {
        Pending fragment = new Pending();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending, container, false);

        pendig_recycle = (RecyclerView) view.findViewById(R.id.pendings_recycler);
        pending_empty_text = (TextView) view.findViewById(R.id.pending_empty_text);
        pending_progress = (ProgressBar) view.findViewById(R.id.progress_pendings);

        layoutManager = new LinearLayoutManager(view.getContext());
        pendig_recycle.setHasFixedSize(true);
        pendig_recycle.setLayoutManager(layoutManager);

        new getPendings(view.getContext()).execute();
        context = view.getContext();
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onPendingsCardClick(PendingsHelper temp_in_help) {
        Log.i(TAG, "onbookedCardClick: ");

        Intent intent = new Intent(context, ViewChannel.class);


        ArrayList<PendingsHelper> listPen = new ArrayList<>();
        listPen.add(temp_in_help);
        intent.putParcelableArrayListExtra("pendings_help",listPen);
        intent.putExtra("channel_id",3);
        startActivity(intent);
    }

    public void onbookedCardClick(PendingsHelper temp_app_help) {

    }


    public class getPendings extends AsyncTask<Integer, ArrayList<PendingsHelper>,  ArrayList<PendingsHelper>> {

        Context contextBox;
        public getPendings(Context context){
            contextBox = context;
        }


        HttpsURLConnection connection ;
        ArrayList<PendingsHelper> pendingHelpersM = new ArrayList<>();
        @Override
        protected void onPreExecute(){
            Log.i(TAG, "onPreExecute: ");
            pending_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<PendingsHelper> doInBackground(Integer... data) {
            SharedPreferences shbpref = contextBox.getSharedPreferences("srec-shb",MODE_PRIVATE);
            String email = shbpref.getString("email","");
            Log.i(TAG, "doInBackground: "+email);
            try {
                String tt = "https://srec-shb.cf/admin/pendings";
//                String tt = "http://192.168.1.103/super_admin/inbox?hall_id="+hall_id;
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
                tempJS.put("email",email);

                Log.i(TAG, "Pendings Req Json "+tempJS);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.writeBytes(tempJS.toString());



                int responseCode = connection.getResponseCode();

                if (responseCode==200){
                    Log.i(TAG, "Okay inbox ");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuffer sb = new StringBuffer();
                    String readline;

                    while ((readline =reader.readLine())!=null){
                        sb.append(readline);
                    }

                    Log.i(TAG, "SB inbox: "+sb);

                    JSONArray inboxJson;
                    try {
                        inboxJson = new JSONArray(sb.toString());

                    }
                    catch (Exception e){

                        return pendingHelpersM;
                    }

                    int len = inboxJson.length();
                    for(int i=0;i<len;i++){
                        JSONObject inboxObject = (JSONObject) inboxJson.get(i);
//                            Log.i(TAG, "inboxObject " + inboxObject);
                        String date = inboxObject.getString("date");

                        JSONObject sessions = inboxObject.getJSONObject("sessions");

                        int hall_id = sessions.getInt("_id");
                        JSONArray session_id = sessions.getJSONArray("session_id");
                        String book_desc = sessions.getString("book_desc");
                        String by = sessions.getString("by");


                        StringBuffer ses_id = new StringBuffer();
                        for(int j=0;j<session_id.length();j++){
                            ses_id.append(session_id.getInt(j)+"  ");
                        }
                        Log.i(TAG, "do sessions inbox-----------" +hall_id+" "+ date + " "+email+" "+ ses_id.toString() + " " + book_desc + " " + by);
                        PendingsHelper pen = new PendingsHelper(date,email,ses_id.toString(),book_desc,by,hall_id);
                        pendingHelpersM.add(pen);
                    }
                }
                else {
                    Log.i(TAG, "not 200 okay inbox");
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
                Log.i(TAG, "inbox exception "+e);
            }
            return pendingHelpersM;
        }

        @Override
        protected void onPostExecute( ArrayList<PendingsHelper> Phelper) {
            Log.i(TAG, "onPost pendings  ");
            pending_progress.setVisibility(View.INVISIBLE);
            if(Phelper==null || Phelper.isEmpty()){
                pending_empty_text.setVisibility(View.VISIBLE);
            }
            else {
                Log.i(TAG, "onPost inbox else" +Phelper);
                pending_empty_text.setVisibility(View.INVISIBLE);
                pendingsAdapter = new PendingsAdapter(pendingHelpersM,1,Pending.this);
                pendig_recycle.setAdapter(pendingsAdapter);
            }

        }
    }
}

package com.techie_dany.srecshb.admin_superadmin;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.Superadmin.SuperMain;
import com.techie_dany.srecshb.Superadmin.helpers.Inbox_Helper;
import com.techie_dany.srecshb.admin.helpers.PendingsHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.HttpsURLConnection;

public class ViewChannel extends AppCompatActivity {


    private static final String TAG = "view_channel";
    public TextView view_channel_by,view_channel_date,view_channel_desc,view_channel_sessions;
    public Button view_channel_inbox_accept,view_channel_inbox_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_channel);
        int myChannel;
        view_channel_inbox_accept = findViewById(R.id.view_channel_inbox_accept);
        view_channel_inbox_cancel = findViewById(R.id.view_channel_inbox_cancel);
        view_channel_inbox_accept.setVisibility(View.INVISIBLE);
        view_channel_inbox_cancel.setVisibility(View.INVISIBLE);

        view_channel_sessions = findViewById(R.id.view_channel_sessions);
        view_channel_by = findViewById(R.id.view_channel_by);
        view_channel_date = findViewById(R.id.view_channel_date);
        view_channel_desc = findViewById(R.id.view_channel_desc);


        Intent in = getIntent();
        int channelId = in.getIntExtra("channel_id",0);
        ArrayList<Inbox_Helper> inbox_help = null;

        try {
            switch (channelId){
//            super admin
                case 1: // approved
                    ArrayList<Inbox_Helper>approved_help = in.getParcelableArrayListExtra("approved_help");
                    view_channel_inbox_accept.setVisibility(View.GONE);
                    view_channel_inbox_cancel.setVisibility(View.GONE);

                    Log.i(TAG, "approved_help Session_id "+approved_help.get(0).getSession_id());
                    view_channel_by.setText(approved_help.get(0).getBy());
                    view_channel_sessions.setText(approved_help.get(0).getSession_id());
                    view_channel_date.setText(approved_help.get(0).getDate());
                    view_channel_desc.setText(approved_help.get(0).getBook_desc());
                    break;

                case 2: // inbox
                    inbox_help = in.getParcelableArrayListExtra("inbox_help");

                    view_channel_inbox_accept.setVisibility(View.VISIBLE);
                    view_channel_inbox_cancel.setVisibility(View.VISIBLE);

                    Log.i(TAG, "inbox "+inbox_help.get(0).getBy());
                    Log.i(TAG, "inbox_help Session_id "+inbox_help.get(0).getSession_id());

                    view_channel_by.setText(inbox_help.get(0).getBy());
                    view_channel_sessions.setText(inbox_help.get(0).getSession_id());
                    view_channel_date.setText(inbox_help.get(0).getDate());
                    view_channel_desc.setText(inbox_help.get(0).getBook_desc());
                    break;
//        admin
                case 3: // pendings
                    ArrayList<PendingsHelper> pending_help = in.getParcelableArrayListExtra("pendings_help");
                    Log.i(TAG, "pendings "+pending_help.get(0).getBy());
                    Log.i(TAG, "pendings_help pendings_sess_id "+pending_help.get(0).getSes_id());

                    view_channel_by.setText(pending_help.get(0).getBy());
                    view_channel_sessions.setText(pending_help.get(0).getSes_id());
                    view_channel_date.setText(pending_help.get(0).getDate());
                    view_channel_desc.setText(pending_help.get(0).getBook_desc());
                    break;
                case 4: // booked
                    ArrayList<PendingsHelper> booked_help = in.getParcelableArrayListExtra("booked_help");
                    Log.i(TAG, "booked "+booked_help.get(0).getBy());
                    Log.i(TAG, "booked_help booked_help "+booked_help.get(0).getSes_id());

                    view_channel_by.setText(booked_help.get(0).getBy());
                    view_channel_sessions.setText(booked_help.get(0).getSes_id());
                    view_channel_date.setText(booked_help.get(0).getDate());
                    view_channel_desc.setText(booked_help.get(0).getBook_desc());
                    break;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }



        final Context context = this;
        final ArrayList<Inbox_Helper> finalInbox_help = inbox_help;
        view_channel_inbox_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("https://srec-shb.cf/super_admin/accept/session");
                    new SuperAknowledge(context,finalInbox_help.get(0),1).execute(url); // 1 for accepted
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
        view_channel_inbox_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("https://srec-shb.cf/super_admin/cancel/session");
                    new SuperAknowledge(context,finalInbox_help.get(0),-1).execute(url); //  for accepted
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public class SuperAknowledge extends AsyncTask<URL,Void,Void>{

        Inbox_Helper inbox_helper;
        Context cont;
        URL myUrl ;
        int aknowledge;

        SuperAknowledge(Context context,Inbox_Helper help,int ak){
            inbox_helper = help;
            cont = context;
            aknowledge = ak;
        }

        @Override
        public void onPreExecute(){

        }
//    req.body.hall_id, req.body.date, req.body.session_id
        @Override
        protected Void doInBackground(URL... strings) {
//            Log.i(TAG, "doIn  "+inbox_helper.getEmail());

            try {
                myUrl = strings[0];
                HttpsURLConnection connection = (HttpsURLConnection) myUrl.openConnection();
                connection.setRequestProperty("Content-Type","application/json");
                connection.setRequestProperty("Accept-Language","en-us");
                connection.setRequestMethod("POST");

//                converting sessio_id to JSON
                char[] sess = inbox_helper.getSession_id().toCharArray();
                Log.i(TAG, "onCreate: "+sess.length);
                JSONArray session_id = new JSONArray();
                for(int i=0;i<sess.length;i++){
                    try {
                        session_id.put(Integer.parseInt(String.valueOf(sess[i])));

                    }
                    catch(Exception e){
                    }
                    Log.i(TAG, session_id+" ");
                }


                JSONObject obj = new JSONObject();
                obj.put("hall_id",inbox_helper.getcontainer_id());
                obj.put("date",inbox_helper.getDate());
                obj.put("session_id",session_id);

                Log.i(TAG, "doInBackground: "+obj);


                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(obj.toString());


                Log.i(TAG, "Response Code"+connection.getResponseCode());
                if (connection.getResponseCode()==200){
                    Log.i(TAG, "Response Code"+connection.getResponseMessage());
                    Log.i(TAG, "doInBackground: ");

                }
                else {
                    Log.i(TAG, "Errrr 2");
                }
            }
            catch (Exception e){
                Log.i(TAG, "View Channel Exception "+e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void ak){

            if(aknowledge==1) {
                Toast.makeText(cont, "Accepted " + inbox_helper.getBy() + "'s Request", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(cont, "Cancelled  " + inbox_helper.getBy() + " 's Request", Toast.LENGTH_SHORT).show();
            }
            Intent in = new Intent(ViewChannel.this,SuperMain.class);
            in.putExtra("local","vc1");
            startActivity(in);
            finish();
        }
    }


}

package com.techie_dany.srecshb.admin_superadmin;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.techie_dany.srecshb.admin_superadmin.adapters.Hall_Home_Adapter;
import com.techie_dany.srecshb.admin.helpers.Halls_Home_Helper;
import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.certificate.CertificateMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


public class Halls_Home extends Fragment {


    private String mParam1;
    private String mParam2;
    public ProgressBar progress_halls_home;
    public RecyclerView halls_home_recycler;
    public RecyclerView.Adapter halls_adapter;
    public RecyclerView.LayoutManager layoutManager;
    public final static String TAG = "admin_halls_home";
    public GridLayoutManager homeGridLayoutManager;


//    private OnFragmentInteractionListener mListener;

    public ConstraintLayout halls_home_constraint;

    public Halls_Home() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_halls_home, container, false);

        halls_home_recycler = (RecyclerView) view.findViewById(R.id.halls_home_recycler);


//        layoutManager = new LinearLayoutManager(getContext());
        halls_home_recycler.setHasFixedSize(true);
//        halls_home_recycler.setItemViewCacheSize(20);
//        halls_home_recycler.setDrawingCacheEnabled(true);
//        halls_home_recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        homeGridLayoutManager = new GridLayoutManager(view.getContext(),2,GridLayoutManager.VERTICAL,false);

        halls_home_recycler.setLayoutManager(homeGridLayoutManager);
        halls_home_constraint = (ConstraintLayout)view.findViewById(R.id.halls_home_constraint);

//        halls_adapter = new Hall_Home_Adapter(getListHallClassess());




        progress_halls_home = view.findViewById(R.id.progress_halls_home);

        progress_halls_home.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new Halls_Home_Async(getContext()).execute();
    }

    //    getting list of Halls List
    public ArrayList<Halls_Home_Helper> getListHallClassess(){
        Log.i(TAG, "getListHallClassess: ");
        ArrayList<Halls_Home_Helper> temphelp = new ArrayList<>();
        return temphelp;
    }

    public Fragment newInstance() {  //String param1, String param2
        Halls_Home fragment = new Halls_Home();
        return fragment;
    }
    class Halls_Home_Async extends AsyncTask<Void, Void,ArrayList<Halls_Home_Helper>> {


        HttpsURLConnection connection;

        public Halls_Home_Async(Context context) {
        }

        @Override
        public void onPreExecute(){
            CertificateMain cm = new CertificateMain();
            connection.setDefaultSSLSocketFactory(cm.getMyCertificate());
            connection.setDefaultHostnameVerifier(cm.all_hosted);
        }

        @Override
        protected ArrayList<Halls_Home_Helper> doInBackground(Void... voids) {
            ArrayList<Halls_Home_Helper> hhh = new ArrayList<>();

            Log.i(TAG, "doInBackground: 1");
            try {
                URL url = new URL("https://srec-shb.cf/shb/homepage/feed/common");
                connection = (HttpsURLConnection) url.openConnection();

                connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                connection.setRequestProperty("Accept","application/json");
                connection.setRequestProperty("Accept-Language","en-US");
                connection.setRequestMethod("GET");





                 BufferedReader bufread = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 String readLine;
                 StringBuffer sb = new StringBuffer();

                 while ((readLine = bufread.readLine()) != null) {
                     sb.append(readLine);
                 }

                 JSONArray jsonArray = new JSONArray(sb.toString());
                Log.i(TAG, "res: "+connection.getResponseCode());
                Log.i(TAG, "obj : "+jsonArray);

                if(connection.getResponseCode()==200) {

                 for (int i = 0; i < jsonArray.length(); i++) {
                     Log.i(TAG, "doInBack " + jsonArray.get(i));
                     JSONObject jsonb = (JSONObject) jsonArray.get(i);
                     int hall_id = jsonb.getInt("_id");
                     String title = jsonb.getString("title");
                     String head = jsonb.getString("head");

                     Log.i(TAG, "dooooo " + hall_id + " " + title + " " + head);
                     Halls_Home_Helper H_helper = new Halls_Home_Helper(hall_id, title, head);
                     hhh.add(H_helper);
                 }
             }
             else {
                    Toast.makeText(getContext(),"Error Loading page",Toast.LENGTH_LONG).show();
                 Log.i(TAG, "//////==== SHB_Home_MainPage ====/=" +connection.getResponseCode());
             }
            }
            catch (ProtocolException e) {
                e.printStackTrace();
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return hhh;
        }


        @Override
        protected void onPostExecute(ArrayList<Halls_Home_Helper> s) {
            Log.i(TAG, "onPostExecute: "+s);
            try {

                if(s.size()<1){
                    halls_home_constraint.setBackgroundResource(R.drawable.connection_down);
                }
                else {
                    halls_adapter = new Hall_Home_Adapter(s);
                    halls_home_recycler.setAdapter(halls_adapter);
                }
                progress_halls_home.setVisibility(View.INVISIBLE);
            }
            catch (Exception e){
                Log.i(TAG, "onPostExecute: "+e);
            }
        }
    }



}


//        temphelp.add(new Halls_Home_Helper("103","IT Seminar Hall","Senthamil","Available"));
//                temphelp.add(new Halls_Home_Helper("101","Library Seminar Hall","librarian","Available"));
//                temphelp.add(new Halls_Home_Helper("102","CSE Seminar Hall","Hareesh","Available"));
//                temphelp.add(new Halls_Home_Helper("106","ECE Seminar Hall","Rebeka","Not Available"));
//                temphelp.add(new Halls_Home_Helper("104","Mechanical Seminar Hall","Peterson","Available"));
//                temphelp.add(new Halls_Home_Helper("105","AERO Seminar Hall","Harry Potter","Available"));

//    public Fragment newInstance() {  //String param1, String param2
//        Halls_Home fragment = new Halls_Home();
////        Bundle args = new Bundle();
////        args.putString(ARG_PARAM1, param1);
////        args.putString(ARG_PARAM2, param2);
////        fragment.setArguments(args);
//        return fragment;
//    }
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
////    }
//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }

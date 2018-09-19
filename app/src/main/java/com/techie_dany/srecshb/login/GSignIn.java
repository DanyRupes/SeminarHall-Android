package com.techie_dany.srecshb.login;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

//import com.fasterxml.jackson.core.JsonParser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import com.shobhitpuri.custombuttons.GoogleSignInButton;
import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.Superadmin.SuperMain;
import com.techie_dany.srecshb.admin.AdminMain;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class GSignIn extends AppCompatActivity {

    private static final String TAG = "gsign_activity";

    private static int RC_SIGN_IN = 1;
    private Socket socket = null;
    GoogleSignInButton sign_in_button;
    public ProgressBar progressBar_gsignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);
        sign_in_button = findViewById(R.id.sign_in_button);
//        sign_in_button.setSize(SignInButton.SIZE_STANDARD);

        progressBar_gsignin = (ProgressBar) findViewById(R.id.progressBar_gsignin);
        progressBar_gsignin.setVisibility(View.INVISIBLE);



        Intent gsignIntent = getIntent();
        Log.i(TAG, "getExtras "+gsignIntent.getExtras());
        Log.i(TAG, " getData "+gsignIntent.getData());


    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount google_logged = GoogleSignIn.getLastSignedInAccount(this);
        Log.i(TAG, "chk_logged" + google_logged);

        if (google_logged == null){
            String Google_onAuth_Web_client2 = getString(R.string.Google_onAuth_Web_client2);
            //        with backend server id configured
            GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(Google_onAuth_Web_client2)
//                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
//                .requestServerAuthCode(Google_onAuth_ClientID)
                    .requestEmail().build();
            final GoogleSignInClient mGSiginClient = GoogleSignIn.getClient(this, GSO);

            sign_in_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signInIntent = mGSiginClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
        }
        else {
            SharedPreferences shb = getSharedPreferences("srec-shb",MODE_PRIVATE);
            int role = shb.getInt("user_role",-1);
            switch (role){
                case 1:
                    Toast.makeText(GSignIn.this,"Super Admin",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(GSignIn.this,SuperMain.class)); ///////////////Temporary route for super-admin
                    finish();
                    break;
                case 2:
                    Toast.makeText(GSignIn.this,"Admin",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(GSignIn.this,AdminMain.class)); ///////////////Temporary route for super-admin
                    finish();
                    break;
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Task<GoogleSignInAccount> user_task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {

            AsyncTask task = signedComplete(user_task);
            Log.i(TAG, "onActivityResult: "+task);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private AsyncTask signedComplete(Task<GoogleSignInAccount> user_task) throws IOException, ApiException {
        GoogleSignInAccount my_Account = user_task.getResult(ApiException.class);

        String id_token = my_Account.getIdToken();
        return new AsyncPost(this).execute(id_token);
        }

    class AsyncPost extends AsyncTask<String, JSONObject, JSONObject> {

        private static final String TAG = "postasync";
        SSLContext sc = null;
        HttpsURLConnection https_id_post;
        private Activity activity;

        public AsyncPost(Activity activity) throws MalformedURLException {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            progressBar_gsignin.setVisibility(View.VISIBLE);
            TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;}
                public void checkClientTrusted(X509Certificate[] certs, String authTypr) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }};
            try {
                sc = SSLContext.getInstance("SSL");
                sc.init(null, trustManagers, new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
        }
        final URL posturl = new URL("https://srec-shb.cf/verify/shb_user/");
//        final URL posturl = new URL("https://192.168.1.103/verify/shb_user/");

        HostnameVerifier all_hosted = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        @Override
        protected JSONObject doInBackground(String... id_token) {

            SSLContext finalSc = sc;
            JSONObject post_res_json = null;


            Log.i(TAG, "Google_Auth_Token "+ id_token[0]);

            https_id_post.setDefaultSSLSocketFactory(finalSc.getSocketFactory());
            https_id_post.setDefaultHostnameVerifier(all_hosted);
            try {
                SharedPreferences shb = getSharedPreferences("srec-shb", MODE_PRIVATE);
                String device_token =shb.getString("device_token"," ");
                Log.i(TAG, "gsign :  "+device_token);

                https_id_post = (HttpsURLConnection) posturl.openConnection();
                https_id_post.setRequestProperty("Accept-Language", "en-US");
                https_id_post.setRequestProperty("Content-Type", "application/json");
                https_id_post.setRequestMethod("POST");
                https_id_post.setDoOutput(true);
//                send

                Log.i(TAG, "Deivice_Token "+ device_token);

                JSONObject gjson = new JSONObject();
                gjson.put("device_token",device_token);
                gjson.put("auth_token",id_token[0]);


                DataOutputStream dout = new DataOutputStream(https_id_post.getOutputStream());
                dout.writeBytes(gjson.toString());
                dout.flush();
                dout.close();

                Log.i(TAG, "doInBackground: ResponsePost " + https_id_post.getResponseCode());
                BufferedReader id_post_buff = new BufferedReader(new InputStreamReader(https_id_post.getInputStream()));
                StringBuffer id_sb = new StringBuffer();
                String res_line;

                while ((res_line = id_post_buff.readLine()) != null) {
                    id_sb.append(res_line);
                }
//                git branch --set-upstream-to=origin/<branch> master
                int responseCode = https_id_post.getResponseCode();
                Log.i(TAG, "GSIGNIN responseCode "+responseCode);
                post_res_json = new JSONObject(id_sb.toString());
                Log.i(TAG, "siginData" +post_res_json);
                if(responseCode ==200 || responseCode==201){
                    try {
                        String email = post_res_json.getString("email");
                        post_res_json.put("allow",1);
                    }
                    catch (Exception e){
                        post_res_json.put("allow",0);
                    }

                }
                else {
                    Log.i(TAG, "internal server error ");
                    post_res_json.put("allow",0);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return post_res_json;
        }
        @Override
        protected void onPostExecute(JSONObject jsonOb) {
            progressBar_gsignin.setVisibility(View.INVISIBLE);
            try {
                if(jsonOb.getInt("allow")==0){
                    Toast.makeText(GSignIn.this,"Please Login With SREC Mail ID",Toast.LENGTH_LONG).show();
                    String Google_onAuth_Web_client2 = getString(R.string.Google_onAuth_Web_client2);
                    //        with backend server id configured
                    GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(Google_onAuth_Web_client2)
//                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
//                .requestServerAuthCode(Google_onAuth_ClientID)
                            .requestEmail().build();
                    final GoogleSignInClient mGSiginClient = GoogleSignIn.getClient(GSignIn.this, GSO);
                    mGSiginClient.signOut();
                }else {
                    String email,name;
                    int role,hall_id;
                    email = jsonOb.getString("email");
                    name = jsonOb.getString("name");
                    role = jsonOb.getInt("role");

                    Log.i(TAG, ": Name " +name);
                    Log.i(TAG, ": Email " +email);
                    Log.i(TAG, ": Role " +role);
                    try{
                        hall_id = jsonOb.getInt("hall_id");
                        Log.i(TAG, ": hall_id " +hall_id);
                    }
                    catch (Exception e){
                        hall_id = 0;
                    }
                    SharedPreferences shrdPregEdit = getSharedPreferences("srec-shb",MODE_PRIVATE);
                    SharedPreferences.Editor editor = shrdPregEdit.edit();
//                    editor.putString("user_name",name);
//                    editor.putString("user_email",email);
                    editor.putInt("user_role",role);
                    editor.putInt("hall_id",hall_id);
                    editor.putString("email",email);
                    editor.apply();
//
//                    Log.i(TAG, "shared Preference result "+shrdPregEdit.getString("user_name"," "));
//                    Log.i(TAG, "shared Preference result "+shrdPregEdit.getString("user_email"," "));
                    Log.i(TAG, "shared Preference result "+shrdPregEdit.getInt("user_role",-1));
                    switch (role){
                        case 1:   //admin
                            startActivity(new Intent(activity, SuperMain.class));
                            Toast.makeText(GSignIn.this,"Super Admin",Toast.LENGTH_LONG).show(); //////////////////temporary
                            finish();
                            break;
                        case 2:  //super-admin
                            startActivity(new Intent(activity, AdminMain.class));
                            Toast.makeText(GSignIn.this,"Admin",Toast.LENGTH_LONG).show();
                            finish();
                            break;
                        default:  //staffs normal
//                            startActivity(new Intent(activity, AdminMain.class));
                            Toast.makeText(GSignIn.this,"Normal Acc",Toast.LENGTH_LONG).show();
                            finish();
                            break;
                }
            }
                } catch (JSONException e) {
                e.printStackTrace();
                }
                catch (Exception ee){
                    String Google_onAuth_Web_client2 = getString(R.string.Google_onAuth_Web_client2);
                    //        with backend server id configured
                    GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(Google_onAuth_Web_client2)
//                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
//                .requestServerAuthCode(Google_onAuth_ClientID)
                            .requestEmail().build();
                    final GoogleSignInClient Gclient = GoogleSignIn.getClient(GSignIn.this, GSO);
                    Gclient.signOut();
                    ee.printStackTrace();
                }
        }
        }
    }


////    Aysnc Classs
//    public class myRequest extends AsyncTask <String, Void, Void>{
//
//    @Override
//    protected Void doInBackground(String... strings) {
//
//        GSignIn client = new GSignIn();
//        String ip = "13.126.43.40";
//        int port = 8080;
//        client.socketConnect(ip, port);
//
//        String tokenID = "hello Dany";
//        String socOutput = client.echo(tokenID);
//        Log.i(TAG, "socOutput: " + socOutput);
//        return null;
//    }
//
//    @Override
//    protected void onPreExecute(){
//
//    }
//
//
//}
//
//
//
//
//
//
//}
////
//////        try {
////            GoogleSignInAccount my_Account = user_task.getResult(ApiException.class);
////
//////            Log.i(TAG, "getDisplayName "+my_Account.getDisplayName());
//////            Log.i(TAG, "getEmail "+my_Account.getEmail()); // email id
//////            Log.i(TAG, "getIdToken "+my_Account.getIdToken()); //to backend
////
////            String token_id = my_Account.getIdToken();
//
//
//
////
////    private String echo(String message) {
////
////        try {
////            PrintWriter out = new PrintWriter(getSocket().getOutputStream(), true);
////            BufferedReader bf = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
////
////            out.print(message);
////            String returnMessage = bf.readLine();
////            Log.i(TAG, "returnMessage: " + returnMessage);
////            return returnMessage;
////        } catch (Exception e) {
////            Log.i(TAG, "echoExe: " + e);
////        }
////
////        return null;
////    }
////
////    private Socket getSocket() {
////        return socket;
////    }
////
////    private void socketConnect(String ip, int port) {
////        Log.i(TAG, "socketConnect: " + ip + " " + port);
////        try {
////            GSignIn gSignIn = new GSignIn();
////            gSignIn.socket = new Socket(ip, port);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }




//            URL shb_url = new URL("https://srec-shb.cf/verify/shb_user/");
//            HttpsURLConnection connection = (HttpsURLConnection) shb_url.openConnection();
////
////
//            connection.setDoInput(true);
//            connection.setDoOutput(true);
//
//
//            connection.setRequestMethod("POST");
//            connection.setInstanceFollowRedirects(true);
//            connection.setRequestProperty("Cookie", "");
//            Log.i(TAG, "Start Music4 ");
//
//            String query = "token_id=" + URLEncoder.encode(token_id);
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//// open up the output stream of the connection
//           DataOutputStream output = new DataOutputStream(connection.getOutputStream());
//            int queryLength = query.length();
//            output.writeBytes(query);
//            output.close();
//
//            Log.i(TAG, "Start Music 5");
//
//            Log.i(TAG, "status Code " + connection.getResponseCode());
//            Log.i(TAG, "status Message " + connection.getResponseMessage());
//
//            DataInputStream inputStream = new DataInputStream(connection.getInputStream());
//            for (int c = inputStream.read(); c != -1; c = inputStream.read()) {
//                Log.i(TAG, "read " + (char) c);
//                inputStream.close();
//            }
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
//    }
//}

//for Normal SignIn in client side
//    GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail().build();



//    URL shb_url = new URL("https://srec-shb.cf/verify/shb_user/");
//    HttpsURLConnection connection = (HttpsURLConnection) shb_url.openConnection();
//
//
//                connection.setDoInput(true);
//                        connection.setDoOutput(true);
//
//
//                        connection.setRequestMethod("POST");
//                        connection.setInstanceFollowRedirects(true);
//                        connection.setRequestProperty("Cookie", "");
//                        Log.i(TAG, "Start Music4 ");
//
//                        String query = "token_id="+URLEncoder.encode(token_id);
//                        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
//// open up the output stream of the connection
//                        DataOutputStream output = new DataOutputStream(connection.getOutputStream());
//                        int queryLength = query.length();
//                        output.writeBytes(query);
//                        output.close();
//
//                        Log.i(TAG, "Start Music 5");
//
//                        Log.i(TAG, "status Code "+connection.getResponseCode());
//                        Log.i(TAG, "status Message "+connection.getResponseMessage());
//
//                        DataInputStream inputStream = new DataInputStream(connection.getInputStream());
//
//                        for (int c=inputStream.read(); c!=-1; c=inputStream.read()){
//                        Log.i(TAG, "read "+(char)c);
//                        inputStream.close();
//                        }

//    List nameValue = new ArrayList(1);
//
//                nameValue.add(new BasicNameValuePair("idToken", token_id));
//                        Log.i(TAG, "nameValue"+nameValue);
//
//                        httpPost.setEntity(new UrlEncodedFormEntity(nameValue));
//
//                        Log.i(TAG, "httpPost"+httpPost);
//
//                        HttpResponse response = httpClient.execute(httpPost);
//                        int statusCode = response.getStatusLine().getStatusCode();
//                        Log.i(TAG, "status Code "+statusCode);
//
//final String responseBody = EntityUtils.toString(response.getEntity());
//        Log.i(TAG, "response Body"+responseBody);



//                Log.i(TAG, "Start Music ");
//
//    //                URL shb_url = new URL("https://srec-shb.cf/verify/shb_user/");
//    URL url  = new URL("https://srec-shb.cf/verify/shb_user/");
//
//    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//    String postData = "token_id="+token_id;
//                Log.i(TAG, "token_id: "+token_id);
//
//                    if(conn != null){
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("User-Agent", USER_AGENT);
//        conn.setRequestProperty("Accept-Language", "en-GB,en;q=0.5");
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//        conn.setRequestProperty("Content-length", Integer.toString(postData.length()));
//        conn.setRequestProperty("Content-Language", "en-GB");
//        conn.setRequestProperty("charset", "utf-8");
//        conn.setUseCaches(false);
//        conn.setDoOutput(true);
//
//
//        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//        dos.writeBytes(postData);
//        dos.flush();
//        dos.close();
//
//        int ResponseCode = conn.getResponseCode();
//        Log.i(TAG, "Sending 'POST' to " + url.toString() +
//                "data: " + postData + " ResponseCode: " + ResponseCode);;
//    }
//
//}
//            catch (ClientProtocolException ie){
//                    Log.i(TAG, "Error sending ID token to backend: "+ie);
//                    }
//                    catch (IOException e1){
//                    Log.i(TAG, "Error sending ID token to backend1: "+e1);
//                    }
//                    catch (Exception e2){
//                    Log.i(TAG, "Error sending ID token to backend2: "+e2);
//                    }

//            Log.i(TAG, "authCode: "+authCode);


//            Verifying for domain

//            HttpPost httpPost = new HttpPost("https://srec-shb.cf/verify/shb_user/");
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpPost httpPost = new HttpPost("https://srec-shb.cf/verify/shb_user/");

//            Feature<HttpResponse<JsonNode>> jsonResponse;

//            {
//                    jsonResponse = Unirest.post("https://srec-shb.cf/verify/shb_user/")
//                            .header("Content-Type", "application/x-www-form-urlencoded")
//                            .queryString("token_id", token_id)
//                            .field("token_id", token_id)
//                            .asJson();
//            Log.i(TAG, "step1: ");
//                    Future<HttpResponse<JsonNode>> future = Unirest.post("https://srec-shb.cf/verify/shb_user/")
//        .header("Content-Type", "application/x-www-form-urlencoded")
//        .field("token_id", token_id)
//        .field("name", "dann")
//        .asJson();
//        Log.i(TAG, "future: "+future);



//
//    Runnable stuffToDo = new Thread() {
//        @Override
//        public void run() {
//            try {
//                signedComplete(user_task);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    };
//
//    final ExecutorService executor = Executors.newSingleThreadExecutor();
//    final Future future = executor.submit(stuffToDo);
//        executor.shutdown(); // This does not cancel the already-scheduled task.
//
//                try {
//                future.get(8, TimeUnit.MINUTES);
//                } catch (InterruptedException ie) {
//                /* Handle the interruption. Or ignore it. */
//                Log.i(TAG, "onActivityResult1: " + ie);
//                } catch (ExecutionException ee) {
//                Log.i(TAG, "onActivityResult2: " + ee);
//
//                } catch (TimeoutException te) {
//                Log.i(TAG, "onActivityResult3: " + te);
//
//                }
//                if (!executor.isTerminated())
//                Log.i(TAG, "terminated: ");
//
//                executor.shutdownNow();
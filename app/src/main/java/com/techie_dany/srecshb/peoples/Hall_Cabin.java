package com.techie_dany.srecshb.peoples;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.admin.activities.BookingHome;
import com.techie_dany.srecshb.certificate.CertificateMain;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;



public class Hall_Cabin extends AppCompatActivity {

    public static final String TAG = "admin_dept_home";
    public LinearLayout btn_admin_dept_main_book;
    public ImageView hc_head_image;
    public TextView hc_head_name,hc_head_desc,hc_head_dept,hc_prime_name,hc_prime_desc;
    public ProgressBar progress_hallcabin,progress_hallcabin1;
    public CoordinatorLayout hall_cabin_constraint_main,hall_cabin_constraint;


    public Context context;
    public LinearLayout hall_cabin_prime_linear;
    public LayoutInflater inflater_cabin;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_cabin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_admin_dept_main);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        hc_head_image = (ImageView) findViewById(R.id.hc_head_image);
        hc_head_name = (TextView) findViewById(R.id.hc_head_name);
        hc_head_desc = (TextView) findViewById(R.id.hc_head_desc);
        hc_head_dept = (TextView) findViewById(R.id.hc_head_dept);
        progress_hallcabin = (ProgressBar) findViewById(R.id.progress_hallcabin);
        progress_hallcabin1 = (ProgressBar) findViewById(R.id.progress_hallcabin1);
        hall_cabin_constraint_main = (CoordinatorLayout) findViewById(R.id.hall_cabin_constraint_main);
        hall_cabin_constraint = (CoordinatorLayout) findViewById(R.id.hall_cabin_constraint);
        hall_cabin_constraint.setVisibility(View.VISIBLE);

        progress_hallcabin.setVisibility(View.VISIBLE);


        Drawable titleImage = getResources().getDrawable(R.drawable.iwritecode);
//       getSupportActionBar().setBackgroundDrawable(titleImage);
        CollapsingToolbarLayout colabcabin = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_hallcabin);
        colabcabin.setBackground(titleImage);


        Bundle extras = getIntent().getExtras();
        int hall_id = 0;
        if (extras != null) {
            hall_id = extras.getInt("hall_id");
            String hall = null;
            switch (hall_id){
                case 100:
                    hall = "Library Seminar Hall";
                    break;
                case 101:
                    hall = "IT Seminar Hall";
                break;
                case 102:
                    hall = "CSE Seminar Hall";
                    break;
                case 103:
                    hall = "ECE Seminar Hall";
                    break;
                case 104:
                    hall = "Mech Seminar Hall";
                    break;
                case 105:
                    hall = "Aero Seminar Hall";
                    break;
                default: 
                    hall_id = extras.getInt("Seminar Hall");
                    break;
                    
            }
            getSupportActionBar().setTitle(hall);
        }else{getSupportActionBar().setTitle("Hall");}


        btn_admin_dept_main_book = (LinearLayout) findViewById(R.id.btn_admin_dept_main_bookl);
        final int finalHall_id = hall_id;
        btn_admin_dept_main_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Start going to book.....");
                Intent in = new Intent(Hall_Cabin.this, BookingHome.class);
                in.putExtra("hall_id",finalHall_id);
                startActivity(in);
            }
        });

        context = this;
        try {
            new Hall_Cabin.AsyncDeptMain(this).execute(hall_id);
//            Log.i(TAG, "Check Aysnc Main.................");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        hall_cabin_prime_linear = (LinearLayout) findViewById(R.id.hall_cabin_prime_linear);
        inflater_cabin = LayoutInflater.from(this);








    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }



    public class AsyncDeptMain extends AsyncTask<Integer,JSONObject, JSONObject> {

        private Activity activity;
        private String dept_id;
        HttpsURLConnection get_hall_http;
        public AsyncDeptMain (Activity activity) throws MalformedURLException {
            this.activity = activity;
        }
        SSLContext sc = null;

        @Override
        protected void onPreExecute() {
        }
        final URL url = new URL("https://srec-shb.cf/users/hall/id/");
//        final URL url = new URL("http://192.168.1.103/users/hall/id/");




        @Override
        protected JSONObject doInBackground(Integer... strings) {

            CertificateMain CM = new CertificateMain();
            get_hall_http.setDefaultSSLSocketFactory(CM.getMyCertificate());
            get_hall_http.setDefaultHostnameVerifier(CM.all_hosted);

            JSONObject details_object = null;
            try {
                get_hall_http = (HttpsURLConnection) url.openConnection();
                get_hall_http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                get_hall_http.setRequestProperty("Accept-Language","en-US");
                get_hall_http.setRequestMethod("POST");
                get_hall_http.setDoOutput(true);

                Log.i(TAG, "doInBackg hall_id "+strings[0]);
                DataOutputStream out = new DataOutputStream(get_hall_http.getOutputStream());
                out.writeBytes("hall_id="+strings[0]);  //hall_id
                out.flush();
                out.close();


                Log.i(TAG, "check 1" +get_hall_http.getResponseCode());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(get_hall_http.getInputStream()));
                StringBuffer details = new StringBuffer();
                String detail_line;

                while ((detail_line = bufferedReader.readLine())!=null){
                    details.append(detail_line);
                }

                details_object  = new JSONObject(details.toString());
////                        System.out.println(id_post_res);
//                Log.i(TAG, " "+details_object.getJSONArray("head"));
//                Log.i(TAG, " "+details_object.getJSONArray("prime"));
//                Log.i(TAG, " "+details_object.get("ac"));
//                Log.i(TAG, " "+details_object.get("projectors"));
//                Log.i(TAG, " "+details_object.get("seats"));

            }
            catch(Exception e){
                Log.i(TAG, "doInBack Exception "+e);
            }
            return details_object;
        }


        @Override
        public void onPostExecute(JSONObject je){
            Log.i(TAG, "onPostExecute: "+je);

            try {
//                HEAD
                JSONObject jshead = je.getJSONArray("head").getJSONObject(0);
                hc_head_name.setText(jshead.getString("h_name"));
                hc_head_desc.setText("Head of Department");
                hc_head_dept.setText(je.getString("name"));

//                prime's

                int length = je.getJSONArray("prime").length();
                for(int i=0;i<length;i++){
                    JSONObject jsprime = je.getJSONArray("prime").getJSONObject(i);
                    View view  = inflater_cabin.inflate(R.layout.layout_hallcabin_prime,hall_cabin_prime_linear,false);

                    ImageView hallcabin_avatar = view.findViewById(R.id.hallcabin_avatar);
                    hallcabin_avatar.setImageResource(R.drawable.avatar_staff);
                    hc_prime_name = view.findViewById(R.id.hc_prime_name);
                    hc_prime_desc = view.findViewById(R.id.hc_prime_desc);

                    hc_prime_name.setText(jsprime.getString("p_name"));
                    hc_prime_desc.setText("Proffessor");

                    hall_cabin_prime_linear.addView(view);
                }
               progress_hallcabin.setVisibility(View.INVISIBLE);
                progress_hallcabin1.setVisibility(View.INVISIBLE);

            } catch (JSONException e) {
                e.printStackTrace();
                hall_cabin_constraint.setVisibility(View.GONE);
                hall_cabin_constraint_main.setBackgroundResource(R.drawable.connection_down);
            } catch (Exception e) {
                hall_cabin_constraint.setVisibility(View.GONE);
                hall_cabin_constraint_main.setBackgroundResource(R.drawable.connection_down);
                e.printStackTrace();
            }
        }
    }

}



//    final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            X509Certificate[] cArrr = new X509Certificate[0];
//            return cArrr;
//        }
//
//        @Override
//        public void checkServerTrusted(final X509Certificate[] chain,
//                                       final String authType) {
//        }
//
//        @Override
//        public void checkClientTrusted(final X509Certificate[] chain,
//                                       final String authType) {
//        }
//    }};
//
//    SSLContext sslContext = null;
//            try {
//                    sslContext = SSLContext.getInstance("SSL");
//                    sslContext.init(null, trustAllCerts, new SecureRandom());
//                    } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                    }
//                    catch (KeyManagementException e) {
//                    e.printStackTrace();
//                    }
//
//                    JSONObject dept_json = new JSONObject();
//                    Log.i(TAG, "doInBackground: hall_id "+ strings[0]);
//
//                    MediaType mediaType = MediaType.parse("x-www-form-urlencoded");
//
//
//
//                    OkHttpClient client = new OkHttpClient.Builder()
//                    .hostnameVerifier(hostnameVerifier)
//                    .build();
//
//                    OkHttpClient clientBuilder = client.newBuilder().build();
//
//                    okhttp3.RequestBody body = new FormBody.Builder().add("hall_id", strings[0]).build();
//
//final okhttp3.Request request = new okhttp3.Request.Builder()
//        .url("https://srec-shb.cf/users/hall/id/")
//        .post(body)
//        .build();
//final Call call = clientBuilder.newBuilder().build().newCall(request);
//        client.newCall(request).enqueue(new Callback() {
//@Override
//public void onFailure(Call call, IOException e) {
//        Log.i(TAG, "onFailure: "+e);
//        }
//@Override
//public void onResponse(Call call, Response response) throws IOException {
//        Log.i(TAG, "onResponse: "+response);
//        }
//        });

//        ClusterSettings clusterSettings = ClusterSettings.builder()
//                .hosts(asList(new ServerAddress("mongodb+srv://test_admin:gumpy@srec-shb-cluster-0rwpz.mongodb.net/test?retryWrites=true"))).build();
////            Log.i(TAG, "onPreExecute: ");
////            uri = new MongoClientURI(
////                    "mongodb://test_admin:gumpy@srec-shb-cluster-shard-00-00-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-01-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-02-0rwpz.mongodb.net:27017/srec_shb?ssl=false&replicaSet=srec-shb-cluster-shard-0&authSource=admin&retryWrites=true");
////                     mongoClient = new MongoClient(uri);
////            uri = new MongoClientURI("mongodb://danyrupes:danyrupes007@ds119171.mlab.com:19171");
//
//
//
//
//        }
//
//@Override
//protected Document doInBackground(String... strings) {
//
////            Log.i(TAG, "doInBackground "+strings.toString());
//
//        Block<Document> printBlock = new Block<Document>() {
//@Override
//public void apply(final Document document) {
//        Log.i(TAG, "getttt " +document.toJson());
//        }
//        };
//
//        Document document = new Document("name", "Café Con Leche")
//        .append("contact", new Document("phone", "228-555-0149")
//        .append("email", "cafeconleche@example.com")
//        .append("location",Arrays.asList(-73.92502, 40.8279556)))
//        .append("stars", 3)
//        .append("categories", Arrays.asList("Bakery", "Coffee", "Pastries"));
////
//        MongoClientOptions.Builder options = MongoClientOptions.builder()
//        .sslEnabled(true)
//        .sslInvalidHostNameAllowed(true)
//        .connectionsPerHost(4)
//        .maxConnectionIdleTime((60 * 1_000))
//        .maxConnectionLifeTime((120 * 1_000));
//        ;
////
////            MongoClientURI uri = new MongoClientURI("mongodb://test_admin:gumpy@srec-shb-cluster-shard-00-00-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-01-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-02-0rwpz.mongodb.net:27017/admin?replicaSet=srec-shb-cluster-shard-0&authSource=admin&retryWrites=true", options);
////            MongoClientURI uri = new MongoClientURI("mongodb+srv://danyrupes:ForrestGump@srec-shb-cluster-0rwpz.mongodb.net/");
//
//
////            uri = new MongoClientURI(
////                    "mongodb://danyrupes:ForrestGump@srec-shb-cluster-0rwpz.mongodb.net:27017/admin?ssl=false&authSource=admin&retryWrites=true");
//
////            com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(uri);
//        MongoClient mongoClient = new MongoClient("mongodb://localhost");


//            com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings);
//            MongoDatabase database = mongoClient.getDatabase("srec_shb");
////
//////            MongoDatabase database = mongoClient.getDatabase("srec_shb");
//////
//////
//            MongoCollection<Document> collection = database.getCollection("accounts");
//            collection.insertOne(document);
//////            collection.find().forEach(printBlock);
//            database.getCollection("accounts").insertOne(document);
//            Log.i(TAG, "doInBack " +collection.countDocuments());
////            Log.i(TAG, "doInBack"+collection.countDocuments());

//    ConnectionString connectionString = new ConnectionString("mongodb+srv://danyrupes:ForrestGump@srec-shb-cluster-0rwpz.mongodb.net/srec_shb?ssl=false&retryWrites=true");

//            CommandListener myCommandListener = new CommandListener() {
//                @Override
//                public void commandStarted(CommandStartedEvent event) {
//                    Log.i(TAG, "commandStarted: ");
//                }
//
//                @Override
//                public void commandSucceeded(CommandSucceededEvent event) {
//                    Log.i(TAG, "commandSucceeded: ");
//                }
//
//                @Override
//                public void commandFailed(CommandFailedEvent event) {
//                    Log.i(TAG, "commandFailed: ");
//                }
//            };
//            MongoClientSettings settings = MongoClientSettings.builder()
//                    .addCommandListener(myCommandListener)
//                    .applyConnectionString(connectionString)
//                    .build();
//

//            SSLContext sslContext = SSLContext.getInstance("SSL");
//            SslSettings sslSettings = SslSettings.builder().enabled(true).build();
//
//            EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
//
//            MongoClient client = MongoClients.create(MongoClientSettings.builder()
//                    .clusterSettings(ClusterSettings.builder()
//                            .hosts(Arrays.asList(new ServerAddress()))
//                            .build())
//                    .streamFactoryFactory(NettyStreamFactoryFactory.builder()
//                            .eventLoopGroup(eventLoopGroup).build())
//                    .sslSettings(SslSettings.builder()
//                            .enabled(true)
//                            .build())
//                    .build());
//            MongoCredential credential = MongoCredential.createCredential(user, database, password);
//
//            MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();
//
//            MongoClient mongoClient = new MongoClient(new ServerAddress("host1", 27017),
//                    Arrays.asList(credential),
//                    options);
//            MongoClientOptions options = MongoClientOptions.builder().sslEnabled(true).build();

//            System.setProperty("org.mongodb.async.type","netty");     ///SSl supports

//    String uri = "mongodb://test_admin:gumpy@srec-shb-cluster-shard-00-00-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-01-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-02-0rwpz.mongodb.net:27017/test?ssl=true&replicaSet=srec-shb-cluster-shard-0&authSource=admin&retryWrites=true";
//    //            String uri = "mongodb+srv://test_admin:gumpy@srec-shb-cluster-0rwpz.mongodb.net/test?ssl=true&retryWrites=true";
//    MongoClient mongoClient = MongoClients.create(uri);  //"mongodb://localhost"
//
//
//
//
//    MongoDatabase mdb = mongoClient.getDatabase("srec_shb");
//    MongoCollection<Document> collection = mdb.getCollection("seminar_halls");
//            collection.find().forEach(printDocumentBlock, callbackWhenFinished);

//            System.setProperty("org.mongodb.async.type","netty");
//                    MongoClient mongoClient = MongoClients.create("mongodb://test_admin:gumpy@srec-shb-cluster-shard-00-00-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-01-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-02-0rwpz.mongodb.net:27017/test?ssl=true&replicaSet=srec-shb-cluster-shard-0&authSource=admin&retryWrites=true");
//
//                    MongoDatabase database = mongoClient.getDatabase("test1");
//
//                    MongoCollection<Document> collection = database.getCollection("accounts1");
//
//        Document doc = new Document("name","Dannny")
//        .append("college","SREC")
//        .append("branch","IT");
//
//        collection.insertOne(doc, new SingleResultCallback<Void>() {
//@Override
//public void onResult(Void result, Throwable t) {
//        Log.i(TAG, "creation Doc success"+result+"  "+t);
//        }
//        });

//            System.setProperty("org.mongodb.async.type","netty");
//            MongoClient mongoClient = new MongoClient( "mongodb://danyrupes:ForrestGump@srec-shb-cluster-shard-00-00-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-01-0rwpz.mongodb.net:27017,srec-shb-cluster-shard-00-02-0rwpz.mongodb.net:27017/test?ssl=false&replicaSet=srec-shb-cluster-shard-0&authSource=admin&retryWrites=true");
//            MongoClient mongoClient = new MongoClient( "mongodb://danyrupes:danyrupes007@ds119171.mlab.com:19171/gadget-shop");
//
//            MongoDatabase database = mongoClient.getDatabase("trash");
//            MongoCollection<Document> collection = database.getCollection("restaurants");
//
//
//            Document document = new Document("name", "Café Con Leche")
//                    .append("contact", new Document("phone", "228-555-0149")
//                            .append("email", "cafeconleche@example.com")
//                            .append("location",Arrays.asList(-73.92502, 40.8279556)))
//                    .append("stars", 3)
//                    .append("categories", Arrays.asList("Bakery", "Coffee", "Pastries"));
//
//            collection.insertOne(document);
//            try {
////                sslContext = SSLContext.getInstance("SSL");
////            } catch (NoSuchAlgorithmException e) {
////                e.printStackTrace();
////            }


///        MongoClientOptions options = MongoClientOptions.builder().sslContext(sslContext).build();
//
//
////        MongoClientURI uri = new MongoClientURI("mongodb+srv://dany:rupes@testcluster-pfyrv.mongodb.net/test?retryWrites=true");
////            try {}
////
////            catch ()
////            XMPPTCPConnectionConfiguration config = new XMPPTCPConnectionConfiguration.Builder().setHost("").setConnectTimeout(3).setCompressionEnabled(true).build();
//
//        initializeConnection();

//            ConnectionString uri = new ConnectionString("mongodb+srv://dany:rupes@testcluster-pfyrv.mongodb.net/test?retryWrites=true");


//    SSLContext sslContext;
//    private void initializeConnection() {
//        try {
//            XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
//                    .setHost("mongodb+srv://dany:rupes@testcluster-pfyrv.mongodb.net/test?retryWrites=true").setCustomSSLContext(SSLContext.getInstance("SSL")).build();
//
////                System.setProperty()
//
//            XMPPTCPConnection xmpptcpConnection = new XMPPTCPConnection(config);
//
//            MongoClient mongoClient = new MongoClient(xmpptcpConnection.getHost());
//
//            Log.i(TAG, "initializeConnection: "+xmpptcpConnection);
//
////                MongoDatabase database = (XMPPTCPConnection) MongoClient
//
//            MongoDatabase database = mongoClient.getDatabase("FromAndorid");
////
//            MongoCollection<Document> collection = database.getCollection("allowed");
//
//            Document doc1 = new Document().append("Name","xxxxxxxxxxx").append("Name", "yyyyyyyyyyyyyy");
//            Document doc2 = new Document().append("Name","xxxxxxxxxxx").append("Name", "yyyyyyyyyyyyyy");
//
//            ArrayList<Document> docs = new ArrayList<>();
//            docs.add(doc1);
//            docs.add(doc2);
//            collection.insertMany(docs);
//
//            mongoClient.close();
//            Log.i(TAG, "Okayy ");
//
//
//        } catch (NoSuchAlgorithmException e) {
//            Log.i(TAG, "Exception" +e);
//        } catch (MongoException me){
//            Log.i(TAG, "MongoException" +me);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
package com.techie_dany.srecshb.messaging;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.techie_dany.srecshb.R;
import com.techie_dany.srecshb.Superadmin.SuperMain;
import com.techie_dany.srecshb.admin.AdminMain;

public class MyFirebaseMessaging extends FirebaseMessagingService {


    private static final String TAG = "myfbmessaging";

    public MyFirebaseMessaging() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.i(TAG, "onMessageReceived: "+remoteMessage.getData());
        Log.i(TAG, "getBody "+remoteMessage.getNotification().getBody());
        Log.i(TAG, "getTitle "+remoteMessage.getNotification().getTitle());
        Log.i(TAG, "code  "+remoteMessage.getData().get("code"));

        int code = Integer.parseInt(remoteMessage.getData().get("code"));

        switch (code){
            case 101:// super-admin{ inbox notification}
                Log.i(TAG, " 101");
            Intent ain = new Intent(this,SuperMain.class);
            ain.putExtra("route","inbox");

            ain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent Apen = PendingIntent.getActivity(this,0,ain,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builAmen = new NotificationCompat.Builder(this,"Default")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setContentIntent(Apen);
            NotificationManager superAmen = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            superAmen.notify(0,builAmen.build());
            break;

            case 102: //admin device notification #approved by sa
                Log.i(TAG, " 102");
                Intent adminAintent = new Intent(this,AdminMain.class);
                adminAintent.putExtra("route","booked");
                adminAintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent adminApen = PendingIntent.getActivity(this,0,adminAintent,PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder buildAdmin = new NotificationCompat.Builder(this,"Default")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(adminApen);
                NotificationManager adminMan = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                adminMan.notify(0,buildAdmin.build());
                break;
            case 302: //super-admin cancelled admin device notification #cancelled by sa
                Log.i(TAG, " 302");
                Intent adminCintent = new Intent(this,AdminMain.class);
                adminCintent.putExtra("route","cancel");

                adminCintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                PendingIntent adminCPen = PendingIntent.getActivity(this,0,adminCintent,PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder buildAdCbuilder = new NotificationCompat.Builder(this,"Default")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(adminCPen);
                NotificationManager adminCMen = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                adminCMen.notify(0,buildAdCbuilder.build());
                break;


        }






        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//        }
//
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
    }



    public String myToken;

    @Override
    public void onNewToken(String token){
        Log.i(TAG, "onNew_Device_Token: "+token);
        myToken = token;

        SharedPreferences shb = getSharedPreferences("srec-shb", MODE_PRIVATE);
        SharedPreferences.Editor shbEditor = shb.edit();

        shbEditor.putString("device_token",token);
        shbEditor.apply();

    }

    public String getToekn(){
        return myToken;
    }

}

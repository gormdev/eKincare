package com.ekincare.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.EkinCareApplication;
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.PreferenceHelper;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

public class HydrocareNotificationService extends IntentService {
    private static final String YES_ACTION = "com.ekincare.util.YES";

    public HydrocareNotificationService() {
        super("");
    }

    private PreferenceHelper prefs;


    @Override
    protected void onHandleIntent(Intent intent) {

        prefs= new PreferenceHelper(getApplicationContext());
        displayNotification();
        deletePreviousWeekData();
    }



    @SuppressLint("NewApi")
    protected void displayNotification() {
        if(prefs.getallNotification()){
            if(prefs.getisHydrocareSubscriptionEnable()){
                System.out.println("Hydrocare test===="+prefs.getisHydrocareSubscriptionEnable());
                Calendar c2 = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
                String  currentTime = df.format(c2.getTime());
                System.out.println("checktimev test========"+currentTime);
                if(currentTime.toUpperCase().replace(".","").equals("09:00 AM")){
                    if(EkinCareApplication.isActivityVisible()){
                        showAppOpen( "Drink Notification", "Good Morning!");
                    }else{
                        showNotificationBody();
                    }
                }else if(currentTime.toUpperCase().replace(".","").equals("11:00 AM")){
                    if(EkinCareApplication.isActivityVisible()){
                        showAppOpen( "Drink Notification", "Good Morning!");
                    }else{
                        showNotificationBody();
                    }
                }else if(currentTime.toUpperCase().replace(".","").equals("13:00 PM")){
                    if(EkinCareApplication.isActivityVisible()){
                        showAppOpen( "Drink Notification", "Good Afternoon!");
                    }else{
                        showNotificationBody();
                    }
                }else if(currentTime.toUpperCase().replace(".","").equals("15:00 PM")){
                    if(EkinCareApplication.isActivityVisible()){

                        showAppOpen( "Drink Notification", "Good Evening!");
                    }else{
                        showNotificationBody();
                    }
                }else if(currentTime.toUpperCase().replace(".","").equals("17:00 PM")){
                    if(EkinCareApplication.isActivityVisible()){
                        showAppOpen( "Drink Notification", "Good Evening!");
                    }else{
                        showNotificationBody();
                    }
                }else if(currentTime.toUpperCase().replace(".","").equals("19:00 PM")){
                    if(EkinCareApplication.isActivityVisible()){
                        showAppOpen( "Drink Notification", "Good Evening!");
                    }else{
                        showNotificationBody();
                    }
                }else if(currentTime.toUpperCase().replace(".","").equals("21:00 PM")){
                    if(EkinCareApplication.isActivityVisible()){
                        showAppOpen( "Drink Notification", "Good Night!");
                    }else{
                        showNotificationBody();
                    }
                }else{

                }


            }else{

            }
        }else{

        }


    }

    private void showAppOpen(String title,String message) {
        String[] waterBenefits = getResources().getStringArray(R.array.water_benefits);
        String bMessage = "";
        Random random = new Random();
        int index = random.nextInt(waterBenefits.length);
        bMessage = message + "\n" + waterBenefits[index];
        Intent mIntent = new Intent("newNotificationCame");
        mIntent.putExtra("notification_title", title);
        mIntent.putExtra("notification_message", bMessage);
        mIntent.putExtra("notification_chatbot_primary_text", " ");
        mIntent.putExtra("notification_chatbot_primary_action"," ");
        mIntent.putExtra("notification_chatbot_secondary_text", "");
        mIntent.putExtra("notification_chatbot_secondary_action","");
        mIntent.putExtra("notification_image_url", "");
        LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);

        }



    @SuppressLint("NewApi")
    private void showNotificationBody() {
        boolean flag = true;
        String sMessage = "";
        String bMessage = "";
        String sTitle = "";
        String bTitle = "";
        String[] waterBenefits = getResources().getStringArray(R.array.water_benefits);
        Random random = new Random();
        int index = random.nextInt(waterBenefits.length);

        sTitle = "Drink Notification";
        bTitle = "Drink Notification ";

        if (new Date().getHours() == 9) {
            sMessage = "Good Morning!";
            bMessage = "Good Morning!";
        }else if(new Date().getHours() == 11){
            sMessage = "Good Morning!";
            bMessage = "Good Morning!";
        }else if(new Date().getHours() == 13){
            sMessage = "Good Afternoon!";
            bMessage = "Good Afternoon!";
        }else if(new Date().getHours() == 15){
            sMessage = "Good Evening!";
            bMessage = "Good Evening!";
        }else if(new Date().getHours() == 17){
            sMessage = "Good Evening!";
            bMessage = "Good Evening!";
        }else if(new Date().getHours() == 19){
            sMessage = "Good Evening!";
            bMessage = "Good Evening!";
        }else if(new Date().getHours() == 21){
            sMessage = "Good Night!";
            bMessage = "Good Night!";
        }else {
            sMessage = "Good Evening!";
            bMessage = "Good Evening!";
        }

        bMessage = bMessage + "\n" + waterBenefits[index];

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(sTitle);
        mBuilder.setTicker("HydroCare: New Message Received!");
        mBuilder.setContentText(sMessage);
        mBuilder.setSmallIcon(R.drawable.notification_logo_all);
        mBuilder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        mBuilder.setSound(soundUri);
        mBuilder.setAutoCancel(true);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(" you have to drink " + " ml water to reach your target"));
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();

        bigStyle.setBigContentTitle(bTitle);
        bigStyle.bigText(bMessage);
        mBuilder.setStyle(bigStyle);

        // Creates an implicit intent
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("NotificationClick", "WaterNotification");
        resultIntent.putExtra("notification_title", bTitle);
        resultIntent.putExtra("notification_message", bMessage);
        resultIntent.putExtra("notification_type", "WaterNotification");
        resultIntent.putExtra("notification_chatbot_primary_text", " ");
        resultIntent.putExtra("notification_chatbot_primary_action"," ");
        resultIntent.putExtra("notification_chatbot_secondary_text", "");
        resultIntent.putExtra("notification_chatbot_secondary_action","");
        resultIntent.putExtra("notification_image_url", "");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);


/*
Action Stop click
 */
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(YES_ACTION);
        intent.putExtra("NotificationClick", "ActionOK");
       // intent.putExtra("notification_title", bTitle);
        intent.putExtra("notification_message", bMessage);
        intent.putExtra("notification_chatbot_primary_text", " ");
        intent.putExtra("notification_chatbot_primary_action"," ");
        intent.putExtra("notification_chatbot_secondary_text", "");
        intent.putExtra("notification_chatbot_secondary_action","");
        intent.putExtra("notification_image_url", "");


        TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(this);
        stackBuilder1.addParentStack(MainActivity.class);
        stackBuilder1.addNextIntent(intent);
        PendingIntent pIntent2 = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = stackBuilder1.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);


        mBuilder.setContentIntent(resultPendingIntent);

        mBuilder.addAction(android.R.drawable.ic_notification_clear_all, "Stop Notifications", pendingIntent);
        NotificationManager myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (flag)
            myNotificationManager.notify(1, mBuilder.build());



    }


    public static String getDateInString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    public void deletePreviousWeekData() {
        Date date = new Date();
        int day = date.getDay();
        date.setDate(date.getDate() - day - 1);
        String startDate = getDateInString(date);
//        databaseHandler.deletePreviousWeekData(startDate);
    }
}

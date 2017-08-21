package com.ekincare.receiver;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.EkinCareApplication;
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.PreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * Created by RaviTejaN on 07-09-2016.
 */
public class WeightUpdateReceiver extends BroadcastReceiver {


    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    private PreferenceHelper prefs;

    private static final String YES_ACTION = "com.ekincare.util.YES";
    private static final String SETTING_ACTION = "com.ekincare.util.SETTING";


    @Override
    public void onReceive(Context context, Intent intent)
    {
        prefs = new PreferenceHelper(context);
        if(prefs.getallNotification()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                Date todayDate = dateFormat.parse(dateFormat.format(c.getTime()));
                Date installationDate = dateFormat.parse(prefs.getInstallTime());
                long diff = todayDate.getTime() - installationDate.getTime();

                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 30 || TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 60 || TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 90
                        || TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 120|| TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 150|| TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 180
                        || TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 210|| TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 240) {
                         if(EkinCareApplication.isActivityVisible()){

                             Intent mIntent = new Intent("newNotificationCame");
                             mIntent.putExtra("notification_title", "Update your weight");
                             mIntent.putExtra("notification_message", "Update your weight check your BMI Status.");
                             mIntent.putExtra("notification_chatbot_primary_text", "Update");
                             mIntent.putExtra("notification_chatbot_primary_action","@"+CustomerManager.getInstance(context).getCurrentCustomer().getIdentification_token()+"/profile");
                             mIntent.putExtra("notification_chatbot_secondary_text", "");
                             mIntent.putExtra("notification_chatbot_secondary_action","");
                             mIntent.putExtra("notification_image_url", "");
                             LocalBroadcastManager.getInstance(context).sendBroadcast(mIntent);

                    }else{
                        showNotificationBody(context);
                    }

                }
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("NewApi")
    private void showNotificationBody(Context context) {

        boolean flag = true;
        String sMessage = "";
        String bMessage = "";
        String bTitle = "";
        String[] waterBenefits = context.getResources().getStringArray(R.array.water_benefits);
        Random random = new Random();
        int index = random.nextInt(waterBenefits.length);

        bTitle = "Update your weight";
        sMessage = "Update your weight check your BMI Status.";

        bMessage = "Update your weight check your BMI Status.";

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(bTitle);
        mBuilder.setTicker("HydroCare: New Message Received!");
        mBuilder.setContentText(sMessage);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_large_record);

        mBuilder.setLargeIcon(bm);
        mBuilder.setSmallIcon(R.drawable.notification_logo_all);
        mBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimary));

        mBuilder.setSound(soundUri);
        mBuilder.setAutoCancel(true);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(" you have to drink " + " ml water to reach your target"));
        NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();

        bigStyle.setBigContentTitle(bTitle);
        bigStyle.bigText(bMessage);
        mBuilder.setStyle(bigStyle);

        // Creates an implicit intent

        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("NotificationClick", "UpdateWeight");
        resultIntent.putExtra("notification_title", bTitle);
        resultIntent.putExtra("notification_message", sMessage);
        resultIntent.putExtra("notification_type", "WeightNotification");
        resultIntent.putExtra("notification_chatbot_primary_text", "Update");
        resultIntent.putExtra("notification_chatbot_primary_action","@"+CustomerManager.getInstance(context).getCurrentCustomer().getIdentification_token()+"/profile");
        resultIntent.putExtra("notification_chatbot_secondary_text", "");
        resultIntent.putExtra("notification_chatbot_secondary_action","");
        resultIntent.putExtra("notification_image_url", "");


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);


/*
Action Stop click
 */
       /* Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(YES_ACTION);
        intent.putExtra("NotificationClick", "UpdateWeight");
        intent.putExtra("notification_title", bTitle);
        intent.putExtra("notification_message", sMessage);
        intent.putExtra("notification_chatbot_primary_text", "Update");
        intent.putExtra("notification_chatbot_primary_action","@"+CustomerManager.getInstance(context).getCurrentCustomer().getIdentification_token()+"/profile");
        intent.putExtra("notification_chatbot_secondary_text", "");
        intent.putExtra("notification_chatbot_secondary_action","");
        intent.putExtra("notification_image_url", "");
        TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(context);
        stackBuilder1.addParentStack(MainActivity.class);
        stackBuilder1.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder1.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.addAction(R.drawable.ic_edit_black_18dp, "UPDATE", pendingIntent);

*/
        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.setAction(SETTING_ACTION);
        intent2.putExtra("NotificationClick", "SETTING");
        //intent2.putExtra("notification_title", bTitle);
        intent2.putExtra("notification_message", sMessage);
        intent2.putExtra("notification_chatbot_primary_text", "Update");
        intent2.putExtra("notification_chatbot_primary_action","@"+CustomerManager.getInstance(context).getCurrentCustomer().getIdentification_token()+"/profile");
        intent2.putExtra("notification_chatbot_secondary_text", "");
        intent2.putExtra("notification_chatbot_secondary_action","");
        intent2.putExtra("notification_image_url", "");
        TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(context);
        stackBuilder2.addParentStack(MainActivity.class);
        stackBuilder2.addNextIntent(intent2);
        PendingIntent pendingIntent2 = stackBuilder2.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.addAction(R.drawable.ic_settings_icon, "Settings", pendingIntent2);


        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (flag)
            myNotificationManager.notify(1, mBuilder.build());

    }


    public void setAlarm(Context context) {

        System.out.println("in alarm RecordUploadNotificationReciever " );

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, WeightUpdateReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 75, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 12:00 a.m.
        calendar.set(Calendar.HOUR_OF_DAY,10);
        calendar.set(Calendar.MINUTE, 45);
        calendar.set(Calendar.SECOND, 00);

        long t = System.currentTimeMillis();

        if (t <= calendar.getTimeInMillis())
        {
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY , alarmIntent);

        }
    }




}

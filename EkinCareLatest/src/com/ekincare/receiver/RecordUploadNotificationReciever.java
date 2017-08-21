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
import com.ekincare.app.EkinCareApplication;
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.PreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * Created by RaviTejaN on 11-08-2016.
 */
public class RecordUploadNotificationReciever extends BroadcastReceiver {


    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    private PreferenceHelper prefs;

    private static final String YES_ACTION = "com.ekincare.util.YES";
    private static final String SETTING_ACTION = "com.ekincare.util.SETTING";


    @Override
    public void onReceive(Context context, Intent intent)
    {
        prefs = new PreferenceHelper(context);

        System.out.println("in on receiver RecordUploadNotificationReciever" + prefs.getInstallTime());

        if(prefs.getallNotification()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            try {
                Date todayDate = dateFormat.parse(dateFormat.format(c.getTime()));
                Date installationDate = dateFormat.parse(prefs.getInstallTime());
                long diff = todayDate.getTime() - installationDate.getTime();
                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 4 || TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 8 || TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 14) {

                    if(EkinCareApplication.isActivityVisible()){
                        System.out.println("mytime======="+"VISIBLE");

                        Intent mIntent = new Intent("newNotificationCame");
                        mIntent.putExtra("notification_title", "Upload Records");
                        mIntent.putExtra("notification_message", "keep all of your and your family's health records.");
                        mIntent.putExtra("notification_chatbot_primary_text", "Upload Record");
                        mIntent.putExtra("notification_chatbot_primary_action","Upload Record");
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
        String sTitle = "";
        String bTitle = "";
        String[] waterBenefits = context.getResources().getStringArray(R.array.water_benefits);
        Random random = new Random();
        int index = random.nextInt(waterBenefits.length);

        sTitle = "Upload Records";
        bTitle = "Upload Records";
        sMessage = "keep all of your and your family's health records";

        bMessage = "keep all of your and your family's health records at one place and access it from anywhere, anytime.";

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(sTitle);
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
        resultIntent.putExtra("NotificationClick", "UploadKnowMore");
        resultIntent.putExtra("notification_title", bTitle);
        resultIntent.putExtra("notification_message", sMessage);
        resultIntent.putExtra("notification_type", "UploadNotification");
        resultIntent.putExtra("notification_chatbot_primary_text", "Upload Record");
        resultIntent.putExtra("notification_chatbot_primary_action","Upload Record");
        resultIntent.putExtra("notification_chatbot_secondary_text", "");
        resultIntent.putExtra("notification_chatbot_secondary_action","");
        resultIntent.putExtra("notification_image_url", "");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);


/*
Action Stop click
 */
       /* Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(YES_ACTION);
        intent.putExtra("NotificationClick", "UploadKnowMore");
        intent.putExtra("notification_title", bTitle);
        intent.putExtra("notification_message", sMessage);
        intent.putExtra("notification_chatbot_primary_text", "Upload Record");
        intent.putExtra("notification_chatbot_primary_action","Upload Record");
        intent.putExtra("notification_chatbot_secondary_text", "");
        intent.putExtra("notification_chatbot_secondary_action","");
        intent.putExtra("notification_image_url", "");
        TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(context);
        stackBuilder1.addParentStack(MainActivity.class);
        stackBuilder1.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder1.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.addAction(R.drawable.ic_stat_more_icon, "Know More", pendingIntent);

*/

        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.setAction(SETTING_ACTION);
        intent2.putExtra("NotificationClick", "UploadKnowMore");
       // intent2.putExtra("notification_title", bTitle);
        intent2.putExtra("notification_message", sMessage);
        intent2.putExtra("notification_chatbot_primary_text", "Upload Record");
        intent2.putExtra("notification_chatbot_primary_action","Upload Record");
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

        Intent intent = new Intent(context, RecordUploadNotificationReciever.class);
        alarmIntent = PendingIntent.getBroadcast(context, 75, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 12:00 a.m.
        calendar.set(Calendar.HOUR_OF_DAY,10);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        long t = System.currentTimeMillis();

        if (t <= calendar.getTimeInMillis())
        {
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        }
    }




}

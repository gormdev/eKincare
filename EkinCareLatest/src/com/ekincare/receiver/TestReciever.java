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
 * Created by RaviTejaN on 02-03-2017.
 */

public class TestReciever extends BroadcastReceiver {


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
                if (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 0 || TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 1 || TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) == 14) {

                    if(EkinCareApplication.isActivityVisible()){
                        System.out.println("mytime======="+"VISIBLE");

                        Intent resultIntent = new Intent(context, MainActivity.class);
                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                        resultIntent.putExtra("NotificationClick", "UploadKnowMore");
                        resultIntent.putExtra("notificationTitle","Upload Records");
                        resultIntent.putExtra("notificationSubtitle","keep all of your and your family's health records");
                        resultIntent.putExtra("notificationActive",true);
                        context.startActivity(resultIntent);
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
        resultIntent.putExtra("notificationTitle",bTitle);
        resultIntent.putExtra("notificationSubtitle",sMessage);
        resultIntent.putExtra("notificationActive",false);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);


/*
Action Stop click
 */
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(YES_ACTION);
        resultIntent.putExtra("NotificationClick", "UploadKnowMore");
        resultIntent.putExtra("notificationTitle",bTitle);
        resultIntent.putExtra("notificationSubtitle",sMessage);
        resultIntent.putExtra("notificationActive",false);
        TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(context);
        stackBuilder1.addParentStack(MainActivity.class);
        stackBuilder1.addNextIntent(intent);
        PendingIntent pIntent2 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = stackBuilder1.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.addAction(R.drawable.ic_stat_more_icon, "Know More", pendingIntent);



        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.setAction(SETTING_ACTION);
        resultIntent.putExtra("NotificationClick", "UploadKnowMore");
        resultIntent.putExtra("notificationTitle",bTitle);
        resultIntent.putExtra("notificationSubtitle",sMessage);
        resultIntent.putExtra("notificationActive",false);
        TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(context);
        stackBuilder2.addParentStack(MainActivity.class);
        stackBuilder2.addNextIntent(intent2);
        PendingIntent pIntent3 = PendingIntent.getActivity(context, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = stackBuilder2.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.addAction(R.drawable.ic_settings_icon, "Settings", pendingIntent2);


        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (flag)
            myNotificationManager.notify(1, mBuilder.build());

    }


    public void setAlarm(Context context) {

        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, TestReciever.class);

        Calendar firstTurn = Calendar.getInstance();
        Calendar secondTurn = Calendar.getInstance();
        Calendar thirdTurn = Calendar.getInstance();

        firstTurn.set(Calendar.HOUR_OF_DAY,17);
        firstTurn.set(Calendar.MINUTE, 56);
        firstTurn.set(Calendar.SECOND, 00);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 70, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        secondTurn.set(Calendar.HOUR_OF_DAY,17);
        secondTurn.set(Calendar.MINUTE, 57);
        secondTurn.set(Calendar.SECOND, 00);
        PendingIntent alarmIntent2 = PendingIntent.getBroadcast(context, 71, intent, PendingIntent.FLAG_CANCEL_CURRENT);


        thirdTurn.set(Calendar.HOUR_OF_DAY,17);
        thirdTurn.set(Calendar.MINUTE, 58);
        thirdTurn.set(Calendar.SECOND, 00);
        PendingIntent alarmIntent3 = PendingIntent.getBroadcast(context, 72, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmMgr.cancel(alarmIntent);
        alarmMgr.cancel(alarmIntent2);
        alarmMgr.cancel(alarmIntent3);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstTurn.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, secondTurn.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent2);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, thirdTurn.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent3);
    }






}

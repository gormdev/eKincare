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
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.PreferenceHelper;

import java.util.Calendar;
import java.util.Random;


public class HowWasYouDayReceiverMay extends BroadcastReceiver {

    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    private PreferenceHelper prefs;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        prefs = new PreferenceHelper(context);

        System.out.println("in on receiver How was your day broadcast" + prefs.getHasHowWasYourDayDataFilled());

        if(!prefs.getHasHowWasYourDayDataFilled() && prefs.gethowWasDayReminder() && prefs.getallNotification()){
            displayNotification(context);
        }


    }

    private static final String YES_ACTION = "com.ekincare.util.YES";

    @SuppressLint("NewApi")
    private void displayNotification(Context context) {
        boolean flag = true;
        String sMessage = "Tell us how your day was.";
        String bMessage = "Tell us how your day was.";
        String sTitle = "How was your day?";
        String bTitle = "How was your day?";

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setContentTitle(sTitle);
        mBuilder.setTicker("Daily Updates: New Message Received!");
        mBuilder.setContentText(sMessage);
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.how_us_day_notification);
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


        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.putExtra("NotificationClick", "HowWasYourDay");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);


        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction(YES_ACTION);
        intent.putExtra("NotificationClick", "HowWasYourDay");
        TaskStackBuilder stackBuilder1 = TaskStackBuilder.create(context);
        stackBuilder1.addParentStack(MainActivity.class);
        stackBuilder1.addNextIntent(intent);
        PendingIntent pIntent2 = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = stackBuilder1.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);


        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.addAction(R.drawable.ic_stat_more_icon, "Know More", pendingIntent);

        NotificationManager myNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (flag)

            myNotificationManager.notify(1, mBuilder.build());

    }

    public void setAlarm(Context context) {

        System.out.println("in alarm How was your day");

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, HowWasYouDayReceiverMay.class);
        alarmIntent = PendingIntent.getBroadcast(context, 55, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 00);


        long t = System.currentTimeMillis();

        if (t <= calendar.getTimeInMillis())
        {
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        }
    }





}

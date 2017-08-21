package com.ekincare.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import com.ekincare.R;
import com.oneclick.ekincare.MainActivity;

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

public class BloodSOSNotificationService extends IntentService {
	public BloodSOSNotificationService(String name) {
		 super("");
		// TODO Auto-generated constructor stub
	}

	private SharedPreferences rules;
    private SharedPreferences.Editor rulesEditor;
    private LocationManager locationManager;
//    private EkinCareApplication application;


    @Override
    protected void onHandleIntent(Intent intent) {
        Date date = new Date();
        displayNotification();
        Intent receiver = new Intent("com.ekincare.bloodnotification");
        sendBroadcast(receiver);
        deletePreviousWeekData();
    }

    @SuppressLint("NewApi") protected void displayNotification() {
        boolean flag = true;
        String sMessage = "";
        String bMessage = "";
        String sTitle = "";
        String bTitle = "";
        String[] waterBenefits = getResources().getStringArray(R.array.water_benefits);
        Random random = new Random();
        int index = random.nextInt(waterBenefits.length);
        if (new Date().getHours() == 9) {
            sTitle = "Blood Notification";
            bTitle = "Blood Notification";
            sMessage = "Good Morning!";
            bMessage = "Good Morning!";
            bMessage = bMessage + "\n" + waterBenefits[index];
        /* else {
            if (intake <= 0)
                flag = false;
            sTitle = "Drink Notification";
            bTitle = "The reason why:";
            sMessage = "you are lagging behind by " + intake + " ml.";
            bMessage = waterBenefits[index];
        }*/
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            // Invoking the default notification service
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setContentTitle(sTitle);
            mBuilder.setTicker("HydroCare: New Message Received!");
            mBuilder.setContentText(sMessage);
            mBuilder.setSubText("Please keep yourself hydrated");
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
//            int data = (int) intake;
            mBuilder.setSound(soundUri);
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText("Good Morning!"));
            NotificationCompat.BigTextStyle bigStyle = new NotificationCompat.BigTextStyle();

            bigStyle.setBigContentTitle(bTitle);
            bigStyle.bigText(bMessage);
            mBuilder.setStyle(bigStyle);
            mBuilder.setAutoCancel(true);

            // Creates an implicit intent
            Intent resultIntent = new Intent(this, MainActivity.class);
//
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);

            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_ONE_SHOT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (flag)
                myNotificationManager.notify(11, mBuilder.build());

        }

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

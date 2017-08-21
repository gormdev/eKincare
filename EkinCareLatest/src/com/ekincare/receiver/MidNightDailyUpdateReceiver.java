package com.ekincare.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.ekincare.util.MidnightDailyUploadServiceService;

import java.util.Calendar;
import java.util.Random;

public class MidNightDailyUpdateReceiver extends WakefulBroadcastReceiver {
    
	// The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;
  
    @Override
    public void onReceive(Context context, Intent intent) 
    {   
    	System.out.println("in alarm receiver broadcast");
        Intent service = new Intent(context, MidnightDailyUploadServiceService.class);
        startWakefulService(context, service);
    }

    public void setAlarm(Context context) {

    	System.out.println("in alarm Calling service");

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MidNightDailyUpdateReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 50, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 12:00 a.m.
        /*calendar.set(Calendar.HOUR_OF_DAY,16);
        calendar.set(Calendar.MINUTE,45);
        calendar.set(Calendar.SECOND, 00);
*/
        calendar.set(Calendar.HOUR_OF_DAY,22);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 00);

        System.out.println("Time of broadcast Midnight Receiver= " + calendar.getTime());
        System.out.println("Time of broadcast Midnight Receiver= " + System.currentTimeMillis());

        long t = System.currentTimeMillis();

        if (t <= calendar.getTimeInMillis()) {
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }



}

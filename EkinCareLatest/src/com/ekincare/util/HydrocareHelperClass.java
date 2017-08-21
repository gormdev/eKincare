package com.ekincare.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ehc on 15/5/15.
 */
public class HydrocareHelperClass {

    public static Date getAlarmStartTime(Date date) {
        int hour = date.getHours();
        if (hour % 2 == 0) {
            hour++;
        } else {
            hour = hour + 2;
        }
        date.setHours(hour);
        date.setMinutes(0);
        date.setSeconds(0);
        return date;
    }

    public static void setAlarm(Context mContext, int requestCode, long startTime, long interval) {
        Intent myIntent = null;
        myIntent = new Intent(mContext, HydrocareNotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, requestCode, myIntent, 0);
        cancelAlarmIfExists(mContext, requestCode, myIntent);
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime, interval, pendingIntent);

    }

    public static void cancelAlarmIfExists(Context mContext, int requestCode, Intent intent) {
        try {
            PendingIntent pendingIntent = PendingIntent.getService(mContext, requestCode, intent, 0);
            AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            am.cancel(pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

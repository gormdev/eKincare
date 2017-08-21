package com.ekincare.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ekincare.util.SleepPatternService;

import java.util.Calendar;

/**
 * Created by Mayank on 11-08-2016.
 */
public class ScreenReceiver extends BroadcastReceiver {

    private boolean screenOff;
    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;
        }

        System.out.println("ScreenReceiver onReceive screenOff="+screenOff);

        Intent i = new Intent(context, SleepPatternService.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);
    }
}
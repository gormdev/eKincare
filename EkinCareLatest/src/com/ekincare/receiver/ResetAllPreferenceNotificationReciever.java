package com.ekincare.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.oneclick.ekincare.helper.PreferenceHelper;

import java.util.Calendar;

/**
 * Created by RaviTejaN on 26-08-2016.
 */
public class ResetAllPreferenceNotificationReciever extends BroadcastReceiver {


    // The app's AlarmManager, which provides access to the system alarm services.
    private AlarmManager alarmMgr;
    // The pending intent that is triggered when the alarm fires.
    private PendingIntent alarmIntent;
    private PreferenceHelper prefs;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        prefs = new PreferenceHelper(context);

        System.out.println("in on receiver ResetAllPreferenceNotificationReciever" + prefs.getInstallTime());

        prefs.setupdateWater("0");
        prefs.settotalUpdateWater("0");
        prefs.setHasHowWasYourDayDataFilled(false);
        prefs.sethowWasDay("");
        prefs.setDrinkedCoffeeToday(false);
        prefs.setDrinkedAlcoholToday(false);
        prefs.setSmokedToday(false);
        prefs.sethadbreakfast(false);
        prefs.setskippedMeals(false);
        prefs.setAteOnTime(false);
        prefs.setAfterNoonTakenMedicationId("");
        prefs.setMorningTakenMedicationId("");
        prefs.setEveningTakenMedicationId("");

        /*Calendar c = Calendar.getInstance();
        int Hr24=c.get(Calendar.HOUR_OF_DAY);
        SimpleDateFormat df = new SimpleDateFormat("HH:mm a");
        String  currentTime = df.format(c.getTime());
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("timereset======="+currentTime);
        // 10:01 a.m.
        if(currentTime.equals("01:00 a.m.")){
            prefs.setupdateWater("0");
            prefs.settotalUpdateWater("0");
            //prefs.sethowWasDayDone("DAYRESTART");
            prefs.setHasHowWasYourDayDataFilled(false);
            prefs.sethowWasDay("");
        }*/
    }

    public void setAlarm(Context context) {

        System.out.println("in alarm RecordUploadNotificationReciever " );

        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ResetAllPreferenceNotificationReciever.class);

        alarmIntent = PendingIntent.getBroadcast(context, 75, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        // Set the alarm's trigger time to 12:00 a.m.
        calendar.set(Calendar.HOUR_OF_DAY,00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        long t = System.currentTimeMillis();

        if (t <= calendar.getTimeInMillis())
        {
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

        }
    }

}

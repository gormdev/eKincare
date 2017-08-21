package com.ekincare.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ekincare.receiver.FamilyNotificationReciever;
import com.ekincare.receiver.MidNightDailyUpdateReceiver;
import com.ekincare.receiver.RecordUploadNotificationReciever;
import com.ekincare.receiver.ResetAllPreferenceNotificationReciever;
import com.ekincare.receiver.ScreenReceiver;
import com.ekincare.receiver.WeightUpdateReceiver;
import com.oneclick.ekincare.helper.PreferenceHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Mayank on 11-08-2016.
 */
public class SleepPatternService extends Service {

    private String startTime = "";
    private String endTime = "";

    private Calendar calander;
    private SimpleDateFormat simpleDateFormat,simpleHourFormat,simpleMinuteFormat;
    private PreferenceHelper prefs;

    private Date startLateNight,endLateNight;
    long timeDifference =0;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = new PreferenceHelper(this);

        System.out.println("SleepPatternService onCreate");

        // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //LOGIC FOR SLEEP PATTERN
        calander = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm aaa");
        simpleHourFormat = new SimpleDateFormat("HH");
        simpleMinuteFormat = new SimpleDateFormat("mm");

        boolean screenOff = false;
        try{
            screenOff = intent.getBooleanExtra("screen_state", false);
        }catch (Exception e){

        }

        if (!screenOff)
        {
            System.out.println("SleepPatternService onStartCommand screenOff=" + screenOff + " minute="+getCurrentMinutes() + " hour="+ getCurrentHour());

            if(!prefs.getLateNightPhoneUsuage()){
                timeDifference = 0 ;
            }

            setLateNightStartTime();

            setWakeTime();

        }
        else{
            System.out.println("SleepPatternService onStartCommand screenOff=" + screenOff + " minute="+getCurrentMinutes() + " hour="+ getCurrentHour());

            if(!prefs.getLateNightPhoneUsuage()){
                timeDifference = 0 ;
            }

            setLateNightEndTime();

            setSleepTime();

        }

        //HowWasYouDayReceiverMay howWasYouDayReceiverMay = new HowWasYouDayReceiverMay();
        //howWasYouDayReceiverMay.setAlarm(this);

        MidNightDailyUpdateReceiver midNightDailyUpdateReceiver = new MidNightDailyUpdateReceiver();
        midNightDailyUpdateReceiver.setAlarm(this);

        /*BloodSOSReciever bloodSOSReciever = new BloodSOSReciever();
        bloodSOSReciever.setAlarm(this);*/

        FamilyNotificationReciever familyNotificationReciever = new FamilyNotificationReciever();
        familyNotificationReciever.setAlarm(this);

       /* TestReciever testreciever = new TestReciever();
        testreciever.setAlarm(this);*/

        RecordUploadNotificationReciever recordUploadNotificationReciever = new RecordUploadNotificationReciever();
        recordUploadNotificationReciever.setAlarm(this);

        ResetAllPreferenceNotificationReciever resetNotificationReciever = new ResetAllPreferenceNotificationReciever();
        resetNotificationReciever.setAlarm(this);

        System.out.println("weight=========="+"weight");
        WeightUpdateReceiver weightupdatereceiver = new WeightUpdateReceiver();
        weightupdatereceiver.setAlarm(this);


        return Service.START_STICKY;
    }

    private void setSleepTime() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        Date currentTime = parseDate(hour + ":" + minute);
        Date time21 = parseDate("21:59");
        Date time23 = parseDate("23:59");
        System.out.println("setSleepTime " + " currentTime.before( time23 ) = " + currentTime.before( time23 ) + " prefs.getSleepTime() = " + currentTime.after(time21)+ " currentTime.before(time21)=" + prefs.getSleepTime());


        System.out.println("SleepPatternService.setSleepTime current time= "+ currentTime);
        System.out.println("SleepPatternService.setSleepTime currentTime.before( time23 ) = " + currentTime.before( time23 ));
        System.out.println("SleepPatternService.setSleepTime currentTime.after(time23) = "+ currentTime.after(time23));
        System.out.println("SleepPatternService.setSleepTime currentTime.before(time21) = "+ currentTime.before(time21));
        System.out.println("SleepPatternService.setSleepTime currentTime.after(time21) = "+ currentTime.after(time21));
        System.out.println("SleepPatternService.setSleepTime prefs.getSleepTime()= "+prefs.getSleepTime());

        if ( currentTime.before( time23 ) && currentTime.after(time21)) {
            endTime = getCurrentTime();
            prefs.setSleepTime(endTime);
            System.out.println("Screen is off at " + getCurrentTime() + " is sleeping hour" + " in Preference = " + prefs.getSleepTime());

            System.out.println("SleepPatternService.setSleepTime sleeping time is set at "+getCurrentTime());
            System.out.println("SleepPatternService.setSleepTime prefs.getSleepTime()="+prefs.getSleepTime());
        }else{
            System.out.println("SleepPatternService.setSleepTime currentTime.before( time23 ) && currentTime.after(time21)="+(currentTime.before( time23 ) && currentTime.after(time21)));
        }
    }

    private void setWakeTime()
    {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        Date currentTime = parseDate(hour + ":" + minute);
        Date time4 = parseDate("04:00");
        Date time10 = parseDate("10:00");
        System.out.println("setWakeTime " + " currentTime.before( time10 ) " + currentTime.before( time10 ) +  " currentTime.after(time4) " +  currentTime.after(time4) + " " + prefs.getWakeTime().isEmpty() + " " + prefs.getWakeTime().equals("0") + " prefs.getWakeTime()="+prefs.getWakeTime());

        System.out.println("SleepPatternService.setWakeTime currentTime.before( time10 )="+currentTime.before( time10 ));
        System.out.println("SleepPatternService.setWakeTime currentTime.after( time10 )=" + currentTime.after( time10 ));
        System.out.println("SleepPatternService.setWakeTime currentTime.before(time4)="+currentTime.before(time4));
        System.out.println("SleepPatternService.setWakeTime currentTime.after(time4)="+currentTime.after(time4));
        System.out.println("SleepPatternService.setWakeTime prefs.getWakeTime()="+prefs.getWakeTime());

        if ( currentTime.before( time10 ) && currentTime.after(time4)&& (prefs.getWakeTime().isEmpty() || prefs.getWakeTime().equals("0")))
        {
            System.out.println("SleepPatternService.setWakeTime condition is satisfied");
            startTime = getCurrentTime();
            prefs.setWakeTime(startTime);
            System.out.println("Screen is on at " + getCurrentTime() + " is waking hour" + " in Preference = " + prefs.getWakeTime());

            System.out.println("SleepPatternService.setWakeTime wake time is set at prefs.getWakeTime()="+prefs.getWakeTime());
        }else{
            System.out.println("SleepPatternService.setWakeTime condition not satisfied");
        }

    }

    private void setLateNightStartTime() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);
        int a = now.get(Calendar.AM_PM);

        Date currentTime = parseDate(hour + ":" + minute);
        Date time0 = parseDate("00:01");
        Date time3 = parseDate("03:00");
        System.out.println("setLateNightStartTime " + " currentTime.before( time3 ) " + currentTime.before( time3 ) + "  currentTime.after(time0) " + currentTime.after(time0));


        System.out.println("SleepPatternService.setLateNightStartTime currentTime.before( time3 ) "+ currentTime.before( time3 ));
        System.out.println("SleepPatternService.setLateNightStartTime currentTime.after( time3 ) "+ currentTime.after( time3 ));
        System.out.println("SleepPatternService.setLateNightStartTime currentTime.before( time0 ) "+ currentTime.before( time0 ));
        System.out.println("SleepPatternService.setLateNightStartTime currentTime.after( time0 ) "+ currentTime.after( time0 ));
        System.out.println("SleepPatternService.setLateNightStartTime timeDifference="+timeDifference);

        if ( currentTime.before( time3 ) && currentTime.after(time0)) {
            try{
                startLateNight = simpleDateFormat.parse(getCurrentTime());
            }catch (Exception e){

            }
            System.out.println("SleepPatternService.setLateNightStartTime condition mathced startLateNight="+startLateNight);
        }else{
            System.out.println("SleepPatternService.setLateNightStartTime condition failed");
        }
    }

    private void setLateNightEndTime() {
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int minute = now.get(Calendar.MINUTE);

        Date currentTime = parseDate(hour + ":" + minute);
        Date time0 = parseDate("00:01");
        Date time3 = parseDate("03:00");
        System.out.println("setLateNightEndTime " + " currentTime.before( time3 ) " + currentTime.before( time3 ) + " currentTime.after(time0) " + currentTime.after(time0) + " timeDifference " +  timeDifference);

        System.out.println("SleepPatternService.setLateNightEndTime currentTime.before( time3 ) "+ currentTime.before( time3 ));
        System.out.println("SleepPatternService.setLateNightEndTime currentTime.after( time3 ) "+ currentTime.after( time3 ));
        System.out.println("SleepPatternService.setLateNightEndTime currentTime.before( time0 ) "+ currentTime.before( time0 ));
        System.out.println("SleepPatternService.setLateNightEndTime currentTime.after( time0 ) "+ currentTime.after( time0 ));
        System.out.println("SleepPatternService.setLateNightEndTime timeDifference="+timeDifference);

        if(currentTime.before( time3 ) && currentTime.after(time0) && timeDifference==0)
        {
            System.out.println("SleepPatternService.setLateNightEndTime condition matched");
            try{
                endLateNight =simpleDateFormat.parse(getCurrentTime());
            }catch (Exception e){

            }

            timeDifference = endLateNight.getTime() - startLateNight.getTime();

            System.out.println("SleepPatternService.setLateNightEndTime timeDifference="+timeDifference);
            System.out.println("SleepPatternService.setLateNightEndTime endLateNight="+endLateNight);

            if((timeDifference/ (60 * 1000) % 60)>1){
                prefs.setLateNightPhoneUsuage(true);
                System.out.println("SleepPatternService.setLateNightEndTime time is greater then one min and  prefs.getLateNightPhoneUsuage()=" + prefs.getLateNightPhoneUsuage());
            }
            else{
                timeDifference = 0;
                prefs.setLateNightPhoneUsuage(false);
                System.out.println("SleepPatternService.setLateNightEndTime time is less then one min and  prefs.getLateNightPhoneUsuage()=" + prefs.getLateNightPhoneUsuage());

            }

        }else{
            System.out.println("SleepPatternService.setLateNightEndTime condition failed");
        }
    }

    SimpleDateFormat inputParser = new SimpleDateFormat("HH:mm", Locale.US);

    private Date parseDate(String date) {

        try {
            return inputParser.parse(date);
        } catch (java.text.ParseException e) {
            return new Date(0);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private int getCurrentHour(){
        //return calander.get(Calendar.HOUR);
        return Integer.parseInt(simpleHourFormat.format(calander.getTime()));
    }

    private int getCurrentMinutes(){
        //return  calander.get(Calendar.MINUTE);
        return Integer.parseInt(simpleMinuteFormat.format(calander.getTime()));
    }

    private String getCurrentTime(){
        return simpleDateFormat.format(calander.getTime());
    }


    @Override
    public void onDestroy() {
        System.out.println("SleepPatternService onDestroy");

        Intent restartServiceIntent = new Intent(this, SleepPatternService.class);
        startService(restartServiceIntent);



        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        System.out.println("SleepPatternService onTaskRemoved");

        Intent restartServiceIntent = new Intent(this, SleepPatternService.class);
        startService(restartServiceIntent);

        super.onTaskRemoved(rootIntent);
    }


    

}
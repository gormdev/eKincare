package com.ekincare.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ekincare.util.HydrocareHelperClass;
import com.ekincare.util.SleepPatternService;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            HydrocareHelperClass.setAlarm(context, 0, System.currentTimeMillis(), 1*60*1000);

            Intent i = new Intent(context, SleepPatternService.class);
            context.startService(i);
        }
    }
}

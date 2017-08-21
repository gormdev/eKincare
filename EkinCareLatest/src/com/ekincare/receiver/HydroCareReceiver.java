package com.ekincare.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ekincare.app.EkinCareApplication;
import com.ekincare.util.HydrocareHelperClass;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.vo.Customer;


import java.util.Date;


/**
 * Created by ehc on 25/5/15.
 */
public class HydroCareReceiver extends BroadcastReceiver {




    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("", "HydroCareReceiver onReceive");
        HydrocareHelperClass.setAlarm(context, 0, HydrocareHelperClass.getAlarmStartTime(new Date()).getTime(), 1*60*1000);



    }

}

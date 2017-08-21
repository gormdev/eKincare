package com.ekincare.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver{

//	MessageReceivingService mMessageReceivingService;

	public void onReceive(Context context, Intent intent) {
		if(intent!=null){

			ComponentName comp = new ComponentName(context.getPackageName(), GCMNotificationIntentService.class.getName());
			startWakefulService(context, (intent.setComponent(comp)));
			setResultCode(Activity.RESULT_OK);

		}
	}
}



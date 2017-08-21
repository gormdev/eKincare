package com.ekincare.receiver;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ekincare.R;
import com.ekincare.app.EkinCareApplication;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.PreferenceHelper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static android.R.attr.type;
import static android.R.id.message;

public class GCMNotificationIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    public static final String TAG = "GCMNotificationIntentService";
    NotificationCompat.BigPictureStyle notiStyle;
    PreferenceHelper prefs;

    boolean isMixPanelMessage = false;

    public GCMNotificationIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        prefs = new PreferenceHelper(getApplicationContext());

        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        System.out.println("GCMNotificationIntentService.onHandleIntent extras=" + extras.toString());

        if (!extras.isEmpty()) {

            if (extras.containsKey("mp_message")) {
                mixpanelMessageLogic(intent,gcm);
            } else {
                gcmMessageLogic(intent,gcm);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void gcmMessageLogic(Intent intent, GoogleCloudMessaging gcm)
    {
        System.out.println("GCM comeing========" + "second");

        String messageType = gcm.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            sendNotification("Send error: " + extras.toString());
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            sendNotification("Deleted messages on server: " + extras.toString());
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            isMixPanelMessage = false;

            String message = intent.getExtras().getString("message");

            String  type = "default";
            if (intent.getExtras().getString("type") != null) {
                type = intent.getExtras().getString("type");
            }

            String chatbot_primary_text = "";
            if (intent.getExtras().getString("chatbot_primary_text") != null) {
                chatbot_primary_text = intent.getExtras().getString("chatbot_primary_text");
            }

            String chatbot_primary_action = "";
            if (intent.getExtras().getString("chatbot_primary_action") != null) {
                chatbot_primary_action = intent.getExtras().getString("chatbot_primary_action");
            }

            String chatbot_secondary_text = "";
            if (intent.getExtras().getString("chatbot_secondary_text") != null) {
                chatbot_secondary_text = intent.getExtras().getString("chatbot_secondary_text");
            }

            String chatbot_secondary_action = "";
            if (intent.getExtras().getString("chatbot_secondary_action") != null) {
                chatbot_secondary_action = intent.getExtras().getString("chatbot_secondary_action");
            }

            String image_url = "";
            if (intent.getExtras().getString("image_url") != null) {
                image_url = intent.getExtras().getString("image_url");
            }

            String title = "";
            if (intent.getExtras().getString("title") != null) {
                title = intent.getExtras().getString("title");
            }

            System.out.println("GCMNotificationIntentService.gcmMessageLogic title="+title);
            System.out.println("GCMNotificationIntentService.gcmMessageLogic message="+message);
            System.out.println("GCMNotificationIntentService.gcmMessageLogic type="+type);
            System.out.println("GCMNotificationIntentService.gcmMessageLogic chatbot_primary_text="+chatbot_primary_text);
            System.out.println("GCMNotificationIntentService.gcmMessageLogic chatbot_primary_action="+chatbot_primary_action);
            System.out.println("GCMNotificationIntentService.gcmMessageLogic chatbot_secondary_text="+chatbot_secondary_text);
            System.out.println("GCMNotificationIntentService.gcmMessageLogic chatbot_secondary_action="+chatbot_secondary_action);
            System.out.println("GCMNotificationIntentService.gcmMessageLogic image_url="+image_url);

            System.out.println("GCMNotificationIntentService.onHandleIntent" + EkinCareApplication.isActivityVisible());

            if (prefs.getallNotification()) {
                System.out.println("GCMNotificationIntentService.onHandleIntent");
                if (EkinCareApplication.isActivityVisible()) {
                    Intent mIntent = new Intent("newNotificationCame");
                    mIntent.putExtra("notification_title", title);
                    mIntent.putExtra("notification_message", message);
                    mIntent.putExtra("notification_chatbot_primary_text", chatbot_primary_text);
                    mIntent.putExtra("notification_chatbot_primary_action",chatbot_primary_action);
                    mIntent.putExtra("notification_chatbot_secondary_text", chatbot_secondary_text);
                    mIntent.putExtra("notification_chatbot_secondary_action",chatbot_secondary_action);
                    mIntent.putExtra("notification_image_url", image_url);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);

                } else {
                    System.out.println("GCMNotificationIntentService.onHandleIntent Activity Invisible for url");
                    sendNotification(title,message, chatbot_primary_text, chatbot_primary_action,chatbot_secondary_text,chatbot_secondary_action,image_url);
                }
            }
        }
    }

    private void mixpanelMessageLogic(Intent intent, GoogleCloudMessaging gcm)
    {
        Bundle extras = intent.getExtras();
        String messageType = gcm.getMessageType(intent);

        if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            sendNotification("Send error: " + extras.toString());
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            sendNotification("Deleted messages on server: " + extras.toString());
        } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            isMixPanelMessage = true;

            System.out.println("GCMNotificationIntentService.mixpanelMessageLogic");

            String mp_message = intent.getExtras().getString("mp_message");
            String mp_title_type = "default";
            if (intent.getExtras().getString("mp_title") != null) {
                mp_title_type = intent.getExtras().getString("mp_title");
            }

            if (EkinCareApplication.isActivityVisible()) {
                System.out.println("GCMNotificationIntentService.onHandleIntent Activity Visible");
                Intent mIntent = new Intent("newNotificationCame");

                mIntent.putExtra("notification_title", mp_title_type);
                mIntent.putExtra("notification_message", mp_message);
                mIntent.putExtra("notification_chatbot_primary_text", "");
                mIntent.putExtra("notification_chatbot_primary_action","");
                mIntent.putExtra("notification_chatbot_secondary_text", "");
                mIntent.putExtra("notification_chatbot_secondary_action","");
                mIntent.putExtra("notification_image_url", "");

                LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent);
            } else {
                System.out.println("GCMNotificationIntentService.onHandleIntent Activity Not Visible");
                sendNotification(mp_title_type,mp_message, "", "","","","");
            }
        }

    }

    private void sendNotification(String title, String message, String chatbot_primary_text, String chatbot_primary_action, String chatbot_secondary_text, String chatbot_secondary_action, String url) {
        System.out.println("GCMNotificationIntentService.sendNotification msg=" + message);

        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("notification_title", title);
        intent.putExtra("notification_message", message);
        intent.putExtra("notification_chatbot_primary_text", chatbot_primary_text);
        intent.putExtra("notification_chatbot_primary_action",chatbot_primary_action);
        intent.putExtra("notification_chatbot_secondary_text", chatbot_secondary_text);
        intent.putExtra("notification_chatbot_secondary_action",chatbot_secondary_action);
        intent.putExtra("notification_image_url", url);

        if (url != null) {
            Bitmap remote_picture = null;

            NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
            notiStyle.setBigContentTitle(title);
            notiStyle.setSummaryText(message);

            prefs.setimageUrl(url);

            try {
                remote_picture = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            notiStyle.bigPicture(remote_picture);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.notification_logo_all)
                    .setContentTitle(getString(R.string.app_name))
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setAutoCancel(true);

            mBuilder.setContentIntent(contentIntent);

            Notification mNotification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.notification_logo_all)
                    .setAutoCancel(true)
                    .setContentIntent(contentIntent)
                    .setContentTitle(getString(R.string.app_name))
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentText(message)
                    .setSound(defaultSoundUri)
                    .setStyle(notiStyle).build();

            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
        } else {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.notification_logo_all)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
        }
    }

    @SuppressLint("LongLogTag")
    private void sendNotification(String msg) {

        Log.d(TAG, "Preparing to send notification " + msg);
        NotificationManager mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.notification_logo_all)
                .setContentTitle(getString(R.string.app_name))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setAutoCancel(true);

        mBuilder.setContentIntent(contentIntent);


        Notification mNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notification_logo_all)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setContentTitle(getString(R.string.app_name))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg)
                .setStyle(notiStyle).build();

        mNotificationManager.notify(NOTIFICATION_ID, mNotification);

        Log.d(TAG, "Notification sent successfully.");

    }

    private String getTitle(String type) {
        if (type.equals("vaccination")) {
            return getString(R.string.notification_vaccation);
        } else if (type.equals("assessment")) {
            return getString(R.string.notification_assessment);
        } else if (type.equals("wizard")) {
            return getString(R.string.notification_wizard);
        } else if (type.equals("appointment")) {
            return getString(R.string.notification_appointment);
        } else if (type.equals("medication")) {
            return getString(R.string.notification_medication);
        } else if (type.equals("default")) {
            return getString(R.string.app_name);
        } else if (type.equals("health_analytics")) {
            return getString(R.string.app_name);
        } else if (type.equals("digitized")) {
            return getString(R.string.app_name);
        }else if(type.equals("tip")){
            return getString(R.string.app_name);
        } else{
            return getString(R.string.app_name);
        }
    }

    private String getNotificationTypePage(String type) {
        if (type.equals("wizard")) {
            return "OverViewPage";
        } else if (type.equals("digitized")) {
            return "TimeLine";
        } else {
            return "NotificationFragmentPage";
        }
    }
}

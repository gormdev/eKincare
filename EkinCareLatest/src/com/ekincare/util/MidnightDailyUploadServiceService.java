package com.ekincare.util;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.DataStorage.DatabaseOverAllHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.receiver.MidNightDailyUpdateReceiver;
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.SyncModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public class MidnightDailyUploadServiceService extends IntentService {

    private PreferenceHelper prefs;

    public MidnightDailyUploadServiceService()
    {
        super("SchedulingService");
    }

    DatabaseOverAllHandler dbHandler;

    private CustomerManager customerManager;

    @Override
    protected void onHandleIntent(Intent intent)
    {
        prefs = new PreferenceHelper(this);

        this.customerManager = CustomerManager.getInstance(this.getApplicationContext());

        dbHandler = new DatabaseOverAllHandler(this);

        System.out.println("Service MidnightDailyUploadServiceService Class");

        JSONObject object = createJsonObject();

        System.out.println("MidnightDailyUploadServiceService.onHandleIntent object="+object);

        System.out.println("MidnightDailyUploadServiceService.onHandleIntent " +"WakeUpTime==="+prefs.getWakeTime());
        System.out.println("MidnightDailyUploadServiceService.onHandleIntent " + "SleepUpTime==="+prefs.getSleepTime());

        if(NetworkCaller.isInternetOncheck(this))
        {
            System.out.println("MidnightDailyUploadServiceService.onHandleIntent calling server object="+object);
            updateServer(object,"POST",TagValues.GOOGLE_FIT_HISTORY,object.toString());

        }else{
            System.out.println("MidnightDailyUploadServiceService.onHandleIntent No internet");
            SyncModel syncModel = new SyncModel();
            syncModel.setURL(TagValues.GOOGLE_FIT_HISTORY);
            syncModel.setJSON(object.toString());
            syncModel.setMETHOD("POST");
            dbHandler.setSyncData(syncModel);

            resetDailyData();
        }


        // Release the wake lock provided by the BroadcastReceiver.
        MidNightDailyUpdateReceiver.completeWakefulIntent(intent);
    }

    private JSONObject createJsonObject()
    {
        JSONObject jsonObject = new JSONObject();

        JSONArray req = new JSONArray();

        try {


            JSONObject object = new JSONObject();

            object.put("date", getUpdatingDate());
            object.put("count", getStepCount());
            object.put("calories", getCalories());
            object.put("ate_outside", didAteOutside());
            object.put("sleep_hours", getSleepHours());
            object.put("latenight_phone_usage", didUsedLateNightPhone());
            object.put("water_consumption", getWaterConsumption());
            object.put("sleeping_time", getSleepTime());
            object.put("wake_up_time", getWokeUpTime());


            req.put(object);

            jsonObject.put("steps", req);

        } catch (JSONException e) {

            e.printStackTrace();
            System.out.println("exception=============="+"e20");

        }catch (IndexOutOfBoundsException e){

            e.printStackTrace();
            System.out.println("exception=============="+"e201");

        }
        return jsonObject;
    }


    private void resetDailyData(){
        System.out.println("MidnightDailyUploadServiceService.resetDailyData Resetting data");
        prefs.setLateNightPhoneUsuage(false);
        prefs.setSleepTime("0");
        prefs.setWakeTime("0");
        prefs.setSleepTime("0");
    }

    private String getUpdatingDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        return dateFormat.format(c.getTime());
    }

    private int getStepCount(){
        String count = prefs.getStepsCount();
        if(!count.isEmpty()){
            return Integer.parseInt(count);
        }
        return 0;
    }

    private int getCalories(){
        String count = prefs.getCaloriesCount();
        if(!count.isEmpty()){
            return Integer.parseInt(count);
        }
        return 0;
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED ;
    }

    private boolean didAteOutside()
    {
        if(!checkPermission()){
        }
        else{
            int count = 0;
            ContentResolver contentResolver = getContentResolver();
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor cur = contentResolver.query( Uri.parse( "content://sms/inbox" ), projection, null, null, null);
            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                String smsDate ="";

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int int_Type = cur.getInt(index_Type);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(longDate);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                    smsDate = dateFormat.format(calendar.getTime());

                    if (strbody.contains("Just posted") && smsDate.equals(getUpdatingDate())) {
                        System.out.println("SMS Time " + smsDate);
                        count++;
                    }

                } while (cur.moveToNext() && smsDate.equals(getUpdatingDate()));

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                } else {
                }
            }
            //String date =  cur.getString(cur.getColumnIndex("date"));
            //Long timestamp = Long.parseLong(date);
            if(count>0){
                return true;
            }
        }
        return false;
    }


    private boolean didUsedLateNightPhone(){
        return prefs.getLateNightPhoneUsuage();
    }

    private String getSleepTime()
    {
        System.out.println("MidnightDailyUploadServiceService.getSleepTime prefs.getSleepTime()" + prefs.getSleepTime());
        if(!prefs.getLateNightPhoneUsuage()){
            return prefs.getSleepTime();
        }
        return "";
    }

    private String getWokeUpTime(){
        return prefs.getWakeTime();
    }

    private int getSleepHours(){
        return 0;
    }

    private String getWaterConsumption(){
        return prefs.gettotalUpdateWater();
    }

    private void updateServer(JSONObject object, String method , String URL , final String Json) {
        {
            int methodCode = 0;
            if(method.equalsIgnoreCase("POST")){
                methodCode = Request.Method.POST;
            }
            else if(method.equalsIgnoreCase("GET")){
                methodCode = Request.Method.GET;
            }

            JsonObjectRequest jsObjRequest = new JsonObjectRequest(methodCode,URL,object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if(response!=null){
                                System.out.println("MidnightDailyUploadServiceService.onResponse response = "+response.toString());
                                resetDailyData();
                                responseHandeling(response,Json);
                            }else{

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String msg = "";

                        }
                    }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                    params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                    params.put("X-DEVICE-ID", customerManager.getDeviceID(getApplicationContext()));
                    params.put("Content-type", "application/json");
                    params.put("Accept", "application/json");

                    return params;
                }
            };
            VolleyRequestSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsObjRequest);

        }

    }

    private void responseHandeling(JSONObject response,String Json) {
        try
        {
            JSONObject jsonobject = new JSONObject(response.toString());

            if(jsonobject.getString("message").equalsIgnoreCase("Success")){
                dbHandler.clearSyncDatabase(Json);
                System.out.println(" clearing = " + dbHandler.getSyncData().size());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

}

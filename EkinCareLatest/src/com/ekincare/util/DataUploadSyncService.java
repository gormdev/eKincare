package com.ekincare.util;

import android.app.IntentService;
import android.content.Intent;

import com.DataStorage.DatabaseOverAllHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.vo.SyncModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataUploadSyncService extends IntentService{


    public DataUploadSyncService()
    {
        super("SchedulingService");
    }

    DatabaseOverAllHandler dbHandler;

    private CustomerManager customerManager;

    private PreferenceHelper prefs;

    @Override
    protected void onHandleIntent(Intent intent)
    {
        prefs = new PreferenceHelper(this);
        this.customerManager = CustomerManager.getInstance(this.getApplicationContext());

        dbHandler = new DatabaseOverAllHandler(this);

        System.out.println("Service DataUploadSyncService Class");


            ArrayList<SyncModel> syncModelArrayList = dbHandler.getSyncData();

            for (int i =0 ; i < syncModelArrayList.size() ; i++){
                if(NetworkCaller.isInternetOncheck(this))
                {
                    SyncModel syncModel = syncModelArrayList.get(i);
                    JSONObject object1 = null;
                    try{
                        object1 = new JSONObject(syncModel.getJSON().replaceAll(".*\".*", "\\\""));

                    }catch (Exception e){

                    }
                    System.out.println("Service DataUploadSyncService Class============= in loop at i = "+ i + " size = " + syncModelArrayList.size());
                    //JsonObject obj = new JsonParser().parse(syncModel.getJSON()).getAsJsonObject();
                    updateServer(object1,syncModel.getMETHOD(),syncModel.getURL(),syncModel.getJSON());
                }else{
                    break;
                }

            }


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
                                System.out.println("DataUploadSyncService Response===="+response.toString());

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

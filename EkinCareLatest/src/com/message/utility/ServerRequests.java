package com.message.utility;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.DataStorage.DatabaseOverAllHandler;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.google.gson.Gson;
import com.message.custominterface.ServerRequestInterface;
import com.message.model.PackageItem;
import com.message.model.Wallet;
import com.message.model.WalletResponse;
import com.oneclick.ekincare.ActivityEditProfile;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.helper.ThreadAsyncTask;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.UploadDocumentData;
import com.openNoteScanner.helpers.OpenNoteScannerAppConstant;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Ajay on 07-12-2016.
 */

public class ServerRequests {

    Context context;
    ServerRequestInterface serverRequestInterface;
    PreferenceHelper prefs;
    String textMessage;
    UploadDocumentData mUploadDocumentData;
    CustomerManager customerManager;
    DatabaseOverAllHandler dbHandler;

    private static final AndroidHttpClient ANDROID_HTTP_CLIENT = AndroidHttpClient.newInstance(GeocoderHelper.class.getName());

    private boolean running = false;


    public ServerRequests(Context context, ServerRequestInterface serverRequestInterface,
                          PreferenceHelper prefs,  CustomerManager customerManager){

        this.context=context;
        this.serverRequestInterface=serverRequestInterface;
        this.prefs =prefs;
        this.customerManager=customerManager;
        dbHandler = new DatabaseOverAllHandler(context);
    }
    public void callChatServerRequest(JSONObject object,String text){
        System.out.println("ServerRequests.callChatServerRequest object="+object+TagValues.CHAT_BOT_URL+"===="+prefs.getEkinKey()+
        "===="+prefs.getCustomerKey()+"==="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        textMessage = text;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,TagValues.CHAT_BOT_URL,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("ServerRequests.onResponse callChatServerRequest response="+response.toString());
                        serverRequestInterface.onChatServeResponse(response,textMessage);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        System.out.println("ServerRequests.onError3hResponse callChatServerRequest="+error.toString());
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to unauthorized error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to server error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to bad request error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to forbidden error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to session timed out error","");
                        }
                        else{
                            serverRequestInterface.onErrorResponse("I didn't understood. Please try something else.","");
                        }
                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

               /*params.put("X-CUSTOMER-KEY", "be9cc6eaa5dc7fe5680d29de63c28c6f");
                params.put("X-EKINCARE-KEY", "a82eddcfed7c6faf04dff70f4fa0ec98");
                params.put("X-DEVICE-ID", "1");*/

                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        jsObjRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 10000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 10000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void addFamilyMemberServerRequest(JSONObject object)
    {
        String URL = TagValues.ADD_FAMILY_MEMBER_URL ;
        System.out.println("ServerRequests.addFamilyMemberServerRequest======="+object.toString());

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,URL,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("ServerRequests.onResponse addFamilyMember"+response.toString());
                            serverRequestInterface.onAddFamilyResponse(response);
                        }else{
                            System.out.println("ServerRequests.onResponse addFamilyMember null response");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        NetworkResponse errorRes = error.networkResponse;
                        System.out.println("ServerRequests.onErrorResponse addFamilyMember="+errorRes);
                        String stringData = "";
                        if(errorRes != null && errorRes.data != null){
                            try {
                                stringData = new String(errorRes.data,"UTF-8");
                                JSONObject mainObject = new JSONObject(stringData);
                                serverRequestInterface.onErrorResponse(error.toString(),mainObject.getString("message"));
                            }catch (JSONException e){
                                e.printStackTrace();
                                serverRequestInterface.onErrorResponse(error.toString(),"Unfortunately something went wrong. We will fix this issue soon");
                            }catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                serverRequestInterface.onErrorResponse(error.toString(),"Unfortunately something went wrong. We will fix this issue soon");
                            }
                        }else{
                            error.printStackTrace();
                            serverRequestInterface.onErrorResponse(error.toString(),"Unfortunately something went wrong. Unable to add family member");
                        }
                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                String output = headers.get("ETag");
                //etag = output.replaceAll("W/", "");
                return super.parseNetworkResponse(response);

            }



            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    public void addHraRequest(JSONObject object,final String strFamilyMemberKey)
    {
        System.out.println("ServerRequests.addHraRequest object="+object);
        System.out.println("ServerRequests.addHraRequest strFamilyMemberKey="+strFamilyMemberKey);
        String URL = TagValues.ADD_WIZARD_DETAIL_URL;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, object,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        String idToken = "";
                        if (strFamilyMemberKey == "") {
                            idToken = prefs.getCustomerKey();
                        } else {
                            idToken = strFamilyMemberKey;
                        }
                        if(response!=null){
                            System.out.println("ServerRequests.onResponse addHraRequest"+response.toString());
                            serverRequestInterface.onHraComplitionResponse(response,idToken);
                        }else{
                            System.out.println("ServerRequests.onResponse addHraRequest null response");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse errorRes = error.networkResponse;
                        System.out.println("ServerRequests.onErrorResponse addHraRequest="+errorRes);
                        String stringData = "";
                        /*if(errorRes != null && errorRes.data != null){
                            try {
                                stringData = new String(errorRes.data,"UTF-8");
                                JSONObject mainObject = new JSONObject(stringData);
                                serverRequestInterface.onErrorResponse(mainObject.getString("message"),"");
                            }catch (JSONException e){
                                e.printStackTrace();
                                serverRequestInterface.onErrorResponse("","");
                            }catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                                serverRequestInterface.onErrorResponse("","");
                            }
                        }*/
                    }
                }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                for (String s : keySet) {
                    System.out.println("my========"+s+"==========="+headers.get("X-CUSTOMER-KEY"));
                }
                String output = headers.get("ETag");
                //etag = output.replaceAll("W/", "");
                return super.parseNetworkResponse(response);

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }

    public void uploadMultipleReport(HttpEntity entity) {
        System.out.println("ServerRequests.uploadMultipleReport");
        if (NetworkCaller.isInternetOncheck(context)) {
            mUploadDocumentData = new UploadDocumentData();
            getUploadMultipleReportSatus(TagValues.Get_All_Documents_URL, mUploadDocumentData,entity);
        } else {
            //showAlert(getResources().getString(R.string.internet_not_available), MainActivity.this);
            try {
                Toast.makeText(context,"Failed to connect to internet",Toast.LENGTH_LONG).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void getUploadMultipleReportSatus(String methodName, Object mObject,HttpEntity entity) {
        String strFamilyMemberKey="";
        System.out.println("ServerRequests.getUploadMultipleReportSatus");
        try {

            /*if (this.customerManager.isLoggedInCustomer()) {
                strFamilyMemberKey = "";
            } else {
                strFamilyMemberKey = this.customerManager.getCurrentCustomer().getIdentification_token();
            }*/

            ArrayList<Header>  headerList = new ArrayList<Header>();
            headerList.add(new BasicHeader("X-CUSTOMER-KEY", prefs.getCustomerKey()));
            headerList.add(new BasicHeader("X-EKINCARE-KEY", prefs.getEkinKey()));
            headerList.add(new BasicHeader("X-DEVICE-ID", customerManager.getDeviceID(context)));
           if (!strFamilyMemberKey.equalsIgnoreCase(""))
                headerList.add(new BasicHeader("X-FAMILY-MEMBER-KEY", strFamilyMemberKey));

            serverRequestInterface.onDocumentUploadResponse("Document is being uploaded",false);

            ThreadAsyncTask testImple = new ThreadAsyncTask(listenerUpload, context, mObject, null, methodName, null, "", false, headerList, entity);
            testImple.execute("");

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private ThreadAsyncTask.OnTaskCompleted listenerUpload = new ThreadAsyncTask.OnTaskCompleted() {
        @Override
        public void onTaskCompleted(String method, Object result) {
            if (result != null) {
                boolean isAlertShow = true;
                mUploadDocumentData = (UploadDocumentData) result;
                if (mUploadDocumentData != null) {
                    if (mUploadDocumentData.getMessage() != null) {
                        dbHandler.clearDocuments();
                        try {
                            serverRequestInterface.onDocumentUploadResponse("Your profile will be updated" +
                                    " with the new information within 24 hours and will be notified once it's complete",true);
                        }catch (Exception e){
                            serverRequestInterface.onDocumentUploadResponse("Failed to upload picture",true);
                        }
                        try {
                            for(File file: new java.io.File(OpenNoteScannerAppConstant.IMAGE_PATH).listFiles()){
                                if (!file.isDirectory()){
                                    file.delete();
                                }
                            }
                        }catch (Exception e){

                        }
                    }
                }
            } else {
                serverRequestInterface.onDocumentUploadResponse("Failed to upload picture",true);
            }
        }
    };

    public void versionCheckRequest() {
        String URL=TagValues.VERSION_CHECK;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            try {
                                PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                                String version = pInfo.versionName;
                                String updateVersion=response.getString("version");
                                if(updateVersion.equals(version)){

                                }else {
                                    serverRequestInterface.onUpdateAppResponse();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }

                        }else{

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(context));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void fetchCityName(final Location location)
    {
        if (running)
            return;

        new AsyncTask<Void, Void, String>()
        {
            protected void onPreExecute()
            {
                running = true;
            };

            @Override
            protected String doInBackground(Void... params)
            {
                String cityName = null;

                if (Geocoder.isPresent())
                {
                    try
                    {
                        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addresses.size() > 0)
                        {
                            cityName = addresses.get(0).getLocality();
                        }
                    }
                    catch (Exception ignored)
                    {
                        // after a while, Geocoder start to trhow "Service not availalbe" exception. really weird since it was working before (same device, same Android version etc..
                    }
                }

                if (cityName != null) // i.e., Geocoder succeed
                {
                    return cityName;
                }
                else // i.e., Geocoder failed
                {
                    return fetchCityNameUsingGoogleMap();
                }
            }

            // Geocoder failed :-(
            // Our B Plan : Google Map
            private String fetchCityNameUsingGoogleMap()
            {
                String googleMapUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + location.getLatitude() + ","
                        + location.getLongitude() + "&sensor=false&language=fr";

                try
                {
                    JSONObject googleMapResponse = new JSONObject(ANDROID_HTTP_CLIENT.execute(new HttpGet(googleMapUrl),
                            new BasicResponseHandler()));

                    // many nested loops.. not great -> use expression instead
                    // loop among all results
                    JSONArray results = (JSONArray) googleMapResponse.get("results");
                    for (int i = 0; i < results.length(); i++)
                    {
                        // loop among all addresses within this result
                        JSONObject result = results.getJSONObject(i);
                        if (result.has("address_components"))
                        {
                            JSONArray addressComponents = result.getJSONArray("address_components");
                            // loop among all address component to find a 'locality' or 'sublocality'
                            for (int j = 0; j < addressComponents.length(); j++)
                            {
                                JSONObject addressComponent = addressComponents.getJSONObject(j);
                                if (result.has("types"))
                                {
                                    JSONArray types = addressComponent.getJSONArray("types");

                                    // search for locality and sublocality
                                    String cityName = null;

                                    for (int k = 0; k < types.length(); k++)
                                    {
                                        if ("locality".equals(types.getString(k)) && cityName == null)
                                        {
                                            if (addressComponent.has("long_name"))
                                            {
                                                cityName = addressComponent.getString("long_name");
                                            }
                                            else if (addressComponent.has("short_name"))
                                            {
                                                cityName = addressComponent.getString("short_name");
                                            }
                                        }
                                        if ("sublocality".equals(types.getString(k)))
                                        {
                                            if (addressComponent.has("long_name"))
                                            {
                                                cityName = addressComponent.getString("long_name");
                                            }
                                            else if (addressComponent.has("short_name"))
                                            {
                                                cityName = addressComponent.getString("short_name");
                                            }
                                        }
                                    }
                                    if (cityName != null)
                                    {
                                        return cityName;
                                    }
                                }
                            }
                        }
                    }
                }
                catch (Exception ignored)
                {
                    ignored.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(String cityName)
            {
                running = false;
                if (cityName != null)
                {
                    serverRequestInterface.onLocationResponse(cityName);
                    Log.i("GeocoderHelper", cityName);
                }
            };
        }.execute();
    }

    public void getPackageNameData(List<String> items){
        //String[] parts = packageName.split(",");

        boolean isLast = false;
        int size = items.size();
        for(int i = 0;i<items.size();i++){
            System.out.println("ServerRequests.getPackageNameData parts[i]="+items.get(i) + " parts.lenght="+items.size());

            if(i==0){
                isLast = true;
            }else{
                isLast = false;
            }

            if(!items.get(i).isEmpty()){
                getPackageDetails(items.get(i),isLast,size);
            }

        }
    }

    public void getPackageSpecilites(final String part) {
        String query = null;
        try {
            query = URLEncoder.encode(part, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = TagValues.PACKAGE_AUTO_SEARCH + query;
        System.out.println("ServerRequests.getPackageDetails url="+url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("ServerRequests.onResponse response="+response);
                if(response.length()==0){
                    serverRequestInterface.onSpecializationResponse(null,"No such package found");
                }else{

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                serverRequestInterface.onSpecializationResponse(null,"No such package found");

            }
        });

        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

    }

    private void getPackageDetails(final String part, final boolean isLast, final int size) {
        String query = null;
        try {
            query = URLEncoder.encode(part, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = TagValues.PACKAGE_AUTO_SEARCH + query;
        System.out.println("ServerRequests.getPackageDetails url="+url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("ServerRequests.onResponse response="+response);
                if(response.length()==0){
                    serverRequestInterface.onPackageNameData(null,isLast,true,part, size);
                }else{
                    try {
                        JSONObject jsonObject = response.getJSONObject(0);
                        serverRequestInterface.onPackageNameData(new Gson().fromJson(jsonObject.toString(), PackageItem.class),isLast,false,part,size);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                serverRequestInterface.onPackageNameData(null,isLast,true,part, size);

            }
        });

        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

    }


    public void getPackageList(String packageType){
        String url= TagValues.TYPE_PACKAGE_LIST ;
        System.out.println("ServerRequests.getPackageList url="+url);
        System.out.println("ServerRequests.getPackageList prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.getPackageList prefs.getEkinKey()="+prefs.getEkinKey());
        System.out.println("ServerRequests.getPackageList device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null){
                            serverRequestInterface.onGetPackageResponse(response,"");
                        }else{
                            serverRequestInterface.onGetPackageResponse(response,"No package found");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to unauthorized error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to server error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to bad request error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to forbidden error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to session timed out error","");
                        }
                        else{
                            serverRequestInterface.onErrorResponse("Failed to connect to server","");
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }

    public void getPackageDoctorList(String packageID){
       String url= TagValues.PACKAGE_DOCTORLIST + packageID;
       // String url= Config.LIst + packageID;
        System.out.println("ServerRequests.getPackageDoctorList url="+url);
        System.out.println("ServerRequests.getPackageDoctorList prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.getPackageDoctorList prefs.getEkinKey()="+prefs.getEkinKey());
        System.out.println("ServerRequests.getPackageDoctorList device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null){
                            serverRequestInterface.onPackageDoctorList(response,"");
                        }else{
                            serverRequestInterface.onPackageDoctorList(response,"No package found");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to unauthorized error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to server error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to bad request error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to forbidden error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to session timed out error","");
                        }
                        else{
                            serverRequestInterface.onErrorResponse("Failed to connect to server","");
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }


    public void getDoctorProfile(String packageID){
        String url= TagValues.PACKAGE_DOCTOR_PROFILE + packageID;
        // String url= Config.LIst + packageID;
        System.out.println("ServerRequests.getPackageDoctorList url="+url);
        System.out.println("ServerRequests.getPackageDoctorList prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.getPackageDoctorList prefs.getEkinKey()="+prefs.getEkinKey());
        System.out.println("ServerRequests.getPackageDoctorList device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null){
                            serverRequestInterface.onProfileResponse(response);
                        }else{
                            //serverRequestInterface.onPackageDoctorList(response,"No package found");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to unauthorized error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to server error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to bad request error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to forbidden error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to session timed out error","");
                        }
                        else{
                            serverRequestInterface.onErrorResponse("Failed to connect to server","");
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }



    public void bookAppointmentPackage(JSONObject jsonObject){
        String url= TagValues.MAIN_URL + "book_appointment";
        System.out.println("ServerRequests.bookAppointmentPackage url="+url);
        System.out.println("ServerRequests.bookAppointmentPackage jsonObject="+jsonObject);
        System.out.println("ServerRequests.bookAppointmentPackage prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.bookAppointmentPackage prefs.getEkinKey()="+prefs.getEkinKey());
        System.out.println("ServerRequests.bookAppointmentPackage device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null){
                            serverRequestInterface.onDCPackagesResponse(response);
                        }else{
                            serverRequestInterface.onErrorResponse("","Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to unauthorized error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to server error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to bad request error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to forbidden error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to session timed out error","");
                        }
                        else{
                            serverRequestInterface.onErrorResponse("Failed to connect to server","");
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }

    public void getDCProviders(JSONObject jsonObject){
        String url= TagValues.MAIN_URL + "packages_providers";
        System.out.println("ServerRequests.getDCProviders url="+url);
        System.out.println("ServerRequests.getDCProviders jsonObject=ServerRequests.getDCProviders jsonObject="+jsonObject);
        System.out.println("ServerRequests.getDCProviders prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.getDCProviders prefs.getEkinKey()="+prefs.getEkinKey());
        System.out.println("ServerRequests.getDCProviders device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        System.out.println("ServerRequests.onResponse getDCProviders response="+response);
                        if(response!=null){
                            serverRequestInterface.onDCPackagesResponse(response);
                        }else{
                            serverRequestInterface.onErrorResponse("","Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to unauthorized error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to server error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to bad request error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to forbidden error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to session timed out error","");
                        }
                        else{
                            serverRequestInterface.onErrorResponse("Failed to connect to server","");
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }

    public void getPackagePaymentAmount(String url1,String url2,String packageId,String queryType){
        String url= url1+ packageId+url2+queryType;
        System.out.println("ServerRequests.getPackagePaymentAmount url="+url);
        System.out.println("ServerRequests.getPackagePaymentAmount prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.getPackagePaymentAmount prefs.getEkinKey()="+prefs.getEkinKey());
        System.out.println("ServerRequests.getPackagePaymentAmount device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,url,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null){
                            System.out.println("ServerRequests.onResponse=========="+response.toString());
                            serverRequestInterface.onGetPackageFeeResponse(response,"");
                        }else{
                            serverRequestInterface.onGetPackageFeeResponse(response,"No package found");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to unauthorized error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            serverRequestInterface.onErrorResponse(error.getMessage(),"");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to bad request error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to forbidden error","");
                        }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                            serverRequestInterface.onErrorResponse("Failed to connect, due to session timed out error","");
                        }
                        else{
                            serverRequestInterface.onErrorResponse("Failed to connect","");
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }


    public void sendBookRequest(JSONObject s,String url,final String strFamilyMemberKey)
    {
        System.out.println("MainActivity.sendQueryRequest======"+s.toString());
        System.out.println("ServerRequests.sendQueryRequest url="+url);
        System.out.println("ServerRequests.sendQueryRequest prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.sendQueryRequest prefs.getEkinKey()="+strFamilyMemberKey);
        System.out.println("ServerRequests.sendQueryRequest device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                System.out.println("ServerRequests.onResponse sendBookRequest result="+result);
                if (result != null) {
                    serverRequestInterface.onPaymentSuccessServerRequest(result);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                System.out.println("ServerRequests.onErrorResponse========"+error.getMessage());
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to unauthorized error","");
                }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to server error","");
                }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to bad request error","");
                }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to forbidden error","");
                }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to session timed out error","");
                }
                else{
                    serverRequestInterface.onErrorResponse("Failed to connect","");
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleyRequestSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getWalletData()
    {
        System.out.println("ServerRequests.sendQueryRequest prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.sendQueryRequest prefs.getEkinKey()="+prefs.getEkinKey());
        System.out.println("ServerRequests.sendQueryRequest device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,TagValues.CHAT_WALLET_API,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("ServerRequests.onResponse getWalletData response="+response.toString());
                            try{
                                serverRequestInterface.onWalletResponse(new Gson().fromJson(response.toString(), WalletResponse.class));
                            }catch (Exception e){
                                Wallet wallet = new Wallet();
                                wallet.setBalance("0");
                                WalletResponse walletResponse = new WalletResponse();
                                walletResponse.setWallet(wallet);
                                walletResponse.setStatus("failed");
                                serverRequestInterface.onWalletResponse(walletResponse);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Wallet wallet = new Wallet();
                        wallet.setBalance("0");
                        WalletResponse walletResponse = new WalletResponse();
                        walletResponse.setWallet(wallet);
                        walletResponse.setStatus("failed");
                        serverRequestInterface.onWalletResponse(walletResponse);

                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(context));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void getWalletDataFirst()
    {
        System.out.println("ServerRequests.sendQueryRequest prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.sendQueryRequest prefs.getEkinKey()="+prefs.getEkinKey());
        System.out.println("ServerRequests.sendQueryRequest device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,TagValues.CHAT_WALLET_API,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("ServerRequests.onResponse getWalletData response="+response.toString());
                            try{
                                serverRequestInterface.onWalletResponseFirst(new Gson().fromJson(response.toString(), WalletResponse.class));
                            }catch (Exception e){
                                Wallet wallet = new Wallet();
                                wallet.setBalance("0");
                                WalletResponse walletResponse = new WalletResponse();
                                walletResponse.setWallet(wallet);
                                walletResponse.setStatus("failed");
                                serverRequestInterface.onWalletResponseFirst(walletResponse);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Wallet wallet = new Wallet();
                        wallet.setBalance("0");
                        WalletResponse walletResponse = new WalletResponse();
                        walletResponse.setWallet(wallet);
                        walletResponse.setStatus("failed");
                        serverRequestInterface.onWalletResponseFirst(walletResponse);

                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(context));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void notificationCountRequest() {
        String URL=TagValues.NOTIFICATION_COUNT;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        serverRequestInterface.onNotificationResponseFirst(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(context));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    public void sendQueryRequest(JSONObject s,String url) {

        //Toast.makeText(context,"Called",Toast.LENGTH_LONG).show();
        System.out.println("MainActivity.sendQueryRequest jsonObject======"+s.toString());
        System.out.println("ServerRequests.sendQueryRequest url="+url);
        System.out.println("ServerRequests.sendQueryRequest prefs.getCustomerKey()="+prefs.getCustomerKey());
        System.out.println("ServerRequests.sendQueryRequest prefs.getEkinKey()="+prefs.getEkinKey());
        System.out.println("ServerRequests.sendQueryRequest device id="+Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, s, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                System.out.println("ServerRequests.onResponse sendQueryRequest result="+result);
                if (result != null) {
                    serverRequestInterface.onPaymentSuccessServerRequest(result);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                System.out.println("ServerRequests.onErrorResponse========"+error.getMessage());
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to unauthorized error","");
                }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to server error","");
                }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_BAD_REQUEST) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to bad request error","");
                }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_FORBIDDEN) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to forbidden error","");
                }else if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_GATEWAY_TIMEOUT) {
                    serverRequestInterface.onErrorResponse("Failed to connect, due to session timed out error","");
                }
                else{
                    serverRequestInterface.onErrorResponse("Failed to connect","");
                }
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        VolleyRequestSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void updateProfile(String dob, String gender, final String strFamilyMemberKey) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,TagValues.EDIT_PROFILE_URL,createJson(dob,gender),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null)
                        {
                            System.out.println("ServerRequests.onResponse response="+response);
                            serverRequestInterface.profileUpdated();
                        }else{
                            serverRequestInterface.onErrorResponse("Something ent wrong please try again","");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ActivityEditProfile.onErrorResponse =" + error.toString());
                        serverRequestInterface.onErrorResponse("Something ent wrong please try again","");
                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-HTTP-Method-Override","PATCH");
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(context));
                if (!strFamilyMemberKey.equalsIgnoreCase("")) {
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                }
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


    private JSONObject createJson(String dob,String gender){
        JSONObject jsonObject = new JSONObject();

        JSONObject jsonObjectCustomer = new JSONObject();
        JSONObject jsonObjectCustomerAddress = new JSONObject();
        JSONObject jsonObjectCustomerVitals = new JSONObject();

        try {
            jsonObjectCustomer.put("date_of_birth",dob);
            jsonObjectCustomer.put("gender",gender);

            jsonObjectCustomerAddress.put("line1","");
            jsonObjectCustomerAddress.put("line2","");
            jsonObjectCustomerAddress.put("city","");
            jsonObjectCustomerAddress.put("state","");
            jsonObjectCustomerAddress.put("country","India");
            jsonObjectCustomerAddress.put("zip_code","");

            jsonObjectCustomerVitals.put("weight","");
            jsonObjectCustomerVitals.put("feet","");
            jsonObjectCustomerVitals.put("inches","");
            jsonObjectCustomerVitals.put("blood_group_id","");
            //jsonObjectCustomerVitals.put("waist",);

            jsonObject.put("customer",jsonObjectCustomer);
            jsonObject.put("addresses_attributes",jsonObjectCustomerAddress);
            jsonObject.put("customer_vitals_attributes",jsonObjectCustomerVitals);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("ServerRequests.createJson = " + jsonObject.toString());

        return jsonObject;
    }

}

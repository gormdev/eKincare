package com.oneclick.ekincare.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import android.util.Log;

import com.DataStorage.DatabaseOverAllHandler;
import com.DataStorage.DbResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.oneclick.ekincare.vo.AssessmentData;
import com.oneclick.ekincare.vo.GetDocumentsData;
import com.oneclick.ekincare.vo.GraphData;
import com.oneclick.ekincare.vo.Immunization;
import com.oneclick.ekincare.vo.ProfileData;
import com.oneclick.ekincare.vo.Register;
import com.oneclick.ekincare.vo.TimelineData;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

@SuppressWarnings("deprecation")
public class NetworkCaller {

    private static TypeAdapter<Number> LongTypeAdapter = new TypeAdapter<Number>() {

        @Override
        public void write(JsonWriter out, Number value)
                throws IOException {
            out.value(value);
        }

        @Override
        public Number read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return null;
            }
            try {
                String result = in.nextString();
                if ("".equals(result)) {
                    return null;
                }
                return Long.parseLong(result);
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }
    };
    public Context mActivity;
    public boolean isNetError = false;
    public boolean isConnectionTimeError = false;
    public boolean isOtherError = false;
    public boolean isDataError = false;
    public DatabaseOverAllHandler dbHandler = null;
    Object mFillObject;
    HashMap<String, String> mHashMap;
    Gson mGson;
    PreferenceHelper prefs;

    public NetworkCaller(Context context) {
        mActivity = context;
        mGson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        prefs = new PreferenceHelper(mActivity);
        dbHandler = new DatabaseOverAllHandler(mActivity);
    }

    public static boolean isInternetOncheck(Context context) {
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connec.getActiveNetworkInfo() != null && connec.getActiveNetworkInfo().isAvailable() && connec.getActiveNetworkInfo().isConnected())) {
            //            AppLog.logString(TAG+"Internet not avialable");


            return false;
        } else {
            //            AppLog.logString(TAG+"Internet available");
            return true;
        }
    }

    public static String convertResponseToString(HttpResponse response) throws UnsupportedEncodingException, IllegalStateException, IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        String result = "";

        if (response.getAllHeaders().length > 0) {
            Header[] HeaderStatus = response.getHeaders("Status");

            if (HeaderStatus[0].getValue().toString().contains("200") || HeaderStatus[0].getValue().toString().contains("409"))
            //						|| HeaderStatus[0].getValue().toString().contains("400"))
            {

                try {
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    result = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IOException("Unable to read the response from server");
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new IOException("Problem while working with stream");
                    }
                }
            } else {
                //						result = null;
                throw new IllegalStateException("Bad Response from server " + HeaderStatus[0].getValue());
            }
        } else {

            try {
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                result = sb.toString();

            } catch (IOException e) {
                throw new IOException("Unable to read the response from server");
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new IOException("Problem while working with stream");
                }
            }
        }

        return result;

    }

    public boolean check_Internet(Context mContext) {
        ConnectivityManager mConnectivityManager;
        NetworkInfo mNetworkInfo;
        mConnectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting())
            return true;
        else
            return false;
    }

    public Object httpPostData(String url, String json, List<Header> headers) throws UnsupportedEncodingException, IllegalStateException, IOException {

        AndroidHttpClient client = AndroidHttpClient.newInstance("");
        HttpPost postRequest = new HttpPost(url);

        if (headers != null && headers.size() > 0) {
            for (int i = 0; i < headers.size(); i++) {
                postRequest.addHeader(headers.get(i));
            }
        }

        StringEntity params = new StringEntity(json.toString(), "UTF-8");
        params.setContentType("application/json");
        postRequest.setEntity(params);

        HttpResponse response = client.execute(postRequest);

        String result = convertResponseToString(response);

        if (TextUtils.isEmpty(result)) {
            return new IllegalStateException("Problem with the server please try again.");
        }

        return result;
    }

    public Object httpPostData(String method, Object mObject, List<NameValuePair> postParameters, String json, String AddArrayName, List<Header> headerList) {
        BufferedReader in = null;

        isOtherError = false;
        isConnectionTimeError = false;

        mFillObject = null;

        if (check_Internet(mActivity)) {
            isNetError = false;
            isDataError = false;
            try {
                mFillObject = mObject.getClass().newInstance();

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                int timeoutConnection = 30 * 1000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 30 * 1000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //                String url = TagValues.SCU_URL+method;
                String url = method;
                HttpPost request = new HttpPost(url);

                if (headerList != null && headerList.size() > 0) {
                    for (int i = 0; i < headerList.size(); i++) {
                        request.addHeader(headerList.get(i));
                        System.out.println("------header list " + i + headerList.get(i));
                    }
                }

                System.out.println("-----url " + url);
                if (json != null && !json.equalsIgnoreCase("")) {
                    StringEntity params = new StringEntity(json.toString(), "UTF-8");
                    params.setContentType("application/json");
                    request.setEntity(params);
                    System.out.println("-----parameters " + json);
                } else {
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                    request.setEntity(formEntity);
                    System.out.println("-----parameters " + postParameters.toString());
                }

                HttpResponse response = client.execute(request);

                //TODO REMOVE CODE IF NOT NEED
                //				JSONObject HeaderJsonObject = new JSONObject();
                //				if(response.getAllHeaders().length>0){
                //					try {
                //						Header[] AllHeader = response.getAllHeaders();
                //						for (Header header : AllHeader) {
                //							HeaderJsonObject.put(header.getName(), header.getValue());
                //						}
                //					}catch (JSONException e1) {
                //						e1.printStackTrace();
                //					}
                //				}
                //				System.out.println("-----header. "+HeaderJsonObject.toString());

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                String result = "";

                if (response.getAllHeaders().length > 0) {
                    Header[] HeaderStatus = response.getHeaders("Status");

                    Header[] HeaderEkinKey = response.getHeaders("X-EKINCARE-KEY");
                    Header[] HeaderCustomerKey = response.getHeaders("X-CUSTOMER-KEY");

                    if (HeaderEkinKey.length > 0 && !HeaderEkinKey[0].getValue().equalsIgnoreCase("")) {
                        prefs.setEkinKey(HeaderEkinKey[0].getValue());
                    }
                    if (HeaderCustomerKey.length > 0 && !HeaderCustomerKey[0].getValue().equalsIgnoreCase("")) {
                        prefs.setCustomerKey(HeaderCustomerKey[0].getValue());
                    }

                    if (HeaderStatus[0].getValue().toString().contains("200") || HeaderStatus[0].getValue().toString().contains("409"))
                    //						|| HeaderStatus[0].getValue().toString().contains("400"))
                    {

                        try {
                            while ((line = in.readLine()) != null) {
                                sb.append(line + NL);
                            }
                            result = sb.toString();
                            Log.d("Response", result);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        //						result = null;
                        result = "{\"msg\":" + "\"" + HeaderStatus[0].getValue() + "\"" + "}";
                    }
                } else {

                    try {
                        while ((line = in.readLine()) != null) {
                            sb.append(line + NL);
                        }
                        result = sb.toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (result != null) {

                    if (!AddArrayName.equalsIgnoreCase("")) {
                        result = "{" + AddArrayName + ":" + result + "}";
                    }
                }

                //                    Log.i("Appname", "Response: \n" + result);
                System.out.println("Result-------  Final data k: " + result);

                mFillObject = mGson.fromJson(result, mFillObject.getClass());

            } catch (Exception e) {
                e.printStackTrace();
                isOtherError = true;
            }
        } else {
            isNetError = true;
        }
        return mFillObject;
    }

    public Object httpPostDataLogin(String method, Object mObject, List<NameValuePair> postParameters, String json, String AddArrayName) {
        BufferedReader in = null;

        isOtherError = false;
        isConnectionTimeError = false;

        mFillObject = null;

        if (check_Internet(mActivity)) {
            isNetError = false;
            isDataError = false;
            try {
                mFillObject = mObject.getClass().newInstance();

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                int timeoutConnection = 30 * 1000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 30 * 1000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //                String url = TagValues.SCU_URL+method;
                String url = method;
                HttpPost request = new HttpPost(url);
                System.out.println("-----url " + url);
                if (json != null && !json.equalsIgnoreCase("")) {
                    StringEntity params = new StringEntity(json.toString(), "UTF-8");
                    params.setContentType("application/json");
                    request.setEntity(params);
                    System.out.println("-----parameters " + json);
                } else {
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                    request.setEntity(formEntity);
                    System.out.println("-----parameters " + postParameters.toString());
                }

                HttpResponse response = client.execute(request);

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                String result = "";

                if (response.getAllHeaders().length > 0) {
                    Header[] HeaderStatus = response.getHeaders("Status");

                    Header[] HeaderEkinKey = response.getHeaders("X-EKINCARE-KEY");
                    Header[] HeaderCustomerKey = response.getHeaders("X-CUSTOMER-KEY");

                    if (HeaderEkinKey.length > 0 && !HeaderEkinKey[0].getValue().equalsIgnoreCase("")) {
                        prefs.setEkinKey(HeaderEkinKey[0].getValue());
                    }
                    if (HeaderCustomerKey.length > 0 && !HeaderCustomerKey[0].getValue().equalsIgnoreCase("")) {
                        prefs.setCustomerKey(HeaderCustomerKey[0].getValue());
                    }

                    if (HeaderStatus[0].getValue().toString().contains("200") || HeaderStatus[0].getValue().toString().contains("409")) {

                        try {
                            while ((line = in.readLine()) != null) {
                                sb.append(line + NL);
                            }
                            result = sb.toString();


                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        //						result = null;
                        result = "{\"msg\":" + "\"" + HeaderStatus[0].getValue() + "\"" + "}";
                    }
                } else {

                    try {
                        while ((line = in.readLine()) != null) {
                            sb.append(line + NL);
                        }
                        result = sb.toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (result != null) {

                    if (!AddArrayName.equalsIgnoreCase("")) {
                        result = "{" + AddArrayName + ":" + result + "}";
                    }
                }

                System.out.println("Result-------  Final data customer: " + result);

                //				Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new BadDoubleDeserializer()).create();
                mFillObject = mGson.fromJson(result, mFillObject.getClass());

            } catch (Exception e) {
                e.printStackTrace();
                isOtherError = true;
            }
        } else {
            isNetError = true;
        }
        return mFillObject;
    }

    public NetworkResponse httpGetData(String method, Object mObject, String json, String AddArrayName, List<Header> headerList) {
        BufferedReader in = null;
        DbResponse data = new DbResponse();

        NetworkResponse networkResponse = new NetworkResponse();
        isOtherError = false;
        isConnectionTimeError = false;

        mFillObject = null;

        if (check_Internet(mActivity)) {
            isNetError = false;
            isDataError = false;

            try {
                mFillObject = mObject.getClass().newInstance();
                URL obj = new URL(method);
                HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
                con.setConnectTimeout(30 * 1000);
                con.setReadTimeout(60 * 1000);

                if (headerList != null && headerList.size() > 0) {
                    for (int i = 0; i < headerList.size(); i++) {
                        con.setRequestProperty(headerList.get(i).getName(), headerList.get(i).getValue());
                        System.out.println("------header list " + i + headerList.get(i));
                    }
                }
                con.setRequestMethod("GET");
                con.setRequestProperty("Connection", "keep-alive");

                con.setUseCaches(true);
                con.setDefaultUseCaches(true);
                String etag = con.getHeaderField("Etag");
                String output = etag.replaceAll("W/", "");
                networkResponse.setEtag(output);
                data.setEtag(output);

                int responseCode = con.getResponseCode();
                System.out.println("GET Response Code :: " + responseCode);
                networkResponse.setResponsCode(responseCode);

                in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                String result = "";

                try {
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    result = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                if (result != null) {

                    //					result = result.replaceAll("\\\\", "");
                    //					result = result.replaceAll("\"", "");
                    if (result.contains("\"1\":")) {
                        result = result.replaceAll("\"1\":", "\"one\":");
                    }
                    if (result.contains("\"2\":")) {
                        result = result.replaceAll("\"2\":", "\"two\":");
                    }
                    if (result.contains("\"3\":")) {
                        result = result.replaceAll("\"3\":", "\"three\":");
                    }
                    if (result.contains("\"4\":")) {
                        result = result.replaceAll("\"4\":", "\"four\":");
                    }
                    if (result.contains("\"5\":")) {
                        result = result.replaceAll("\"5\":", "\"five\":");
                    }
                    if (result.contains("\"6\":")) {
                        result = result.replaceAll("\"6\":", "\"six\":");
                    }

                    if (method.equalsIgnoreCase(TagValues.GET_CUSTOMER_DETAIL_URL) && !result.equalsIgnoreCase("")) {

                    } else if (!AddArrayName.equalsIgnoreCase("")) {
                        result = "{" + AddArrayName + ":" + result + "}";
                    }


                    //                    Log.i("Appname", "Response: \n" + result);
                    System.out.println("Result-------  Final data: " + result);

//                    mFillObject = mGson.fromJson(result, mFillObject.getClass());
                    networkResponse.setResult(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isOtherError = true;
            }
        } else {
            isNetError = true;
        }

        return networkResponse;
    }

    public String httpPostDataString(String method, List<NameValuePair> postParameters, String json, String AddArrayName) {
        BufferedReader in = null;

        isOtherError = false;
        isConnectionTimeError = false;
        String finalReslt = "";

        if (check_Internet(mActivity)) {
            isNetError = false;
            isDataError = false;
            try {

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                int timeoutConnection = 30 * 1000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 30 * 1000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //                String url = TagValues.SCU_URL+method;
                String url = method;
                HttpPost request = new HttpPost(url);
                System.out.println("-----url " + url);
                if (json != null && !json.equalsIgnoreCase("")) {
                    StringEntity params = new StringEntity(json.toString(), "UTF-8");
                    params.setContentType("application/json");
                    request.setEntity(params);
                    System.out.println("-----parameters " + json);
                } else {
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                    request.setEntity(formEntity);
                    System.out.println("-----parameters " + postParameters.toString());
                }
                HttpResponse response = client.execute(request);

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                //                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                String result = "";

                try {
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    result = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


                if (result != null)

                    if (!AddArrayName.equalsIgnoreCase("")) {
                        result = "{" + AddArrayName + ":" + result + "}";

                    }

                finalReslt = result;
                System.out.println("Result-------  Final data: " + result);

            } catch (Exception e) {
                e.printStackTrace();
                isOtherError = true;
            }
        } else {
            isNetError = true;
        }
        return finalReslt;
    }

    public Object httpPatchData(String method, Object mObject, List<NameValuePair> postParameters, String json, String AddArrayName, List<Header> headerList) {
        BufferedReader in = null;

        isOtherError = false;
        isConnectionTimeError = false;

        mFillObject = null;

        if (check_Internet(mActivity)) {
            isNetError = false;
            isDataError = false;
            try {
                mFillObject = mObject.getClass().newInstance();

                HttpParams httpParameters = new BasicHttpParams();
                // Set the timeout in milliseconds until a connection is established.
                int timeoutConnection = 30 * 1000;
                HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 30 * 1000;
                HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //                String url = TagValues.SCU_URL+method;
                String url = method;
                //				HttpPost request = new HttpPost(url);
                HttpPatch request = new HttpPatch(url);
                //				request.setHeader("X-HTTP-Method-Override", "PATCH");
                //				request.setHeader("Content-Type", "application/json");

                if (headerList != null && headerList.size() > 0) {
                    for (int i = 0; i < headerList.size(); i++) {
                        request.addHeader(headerList.get(i));
                        System.out.println("------header list " + i + headerList.get(i));
                    }
                }

                System.out.println("-----url " + url);
                if (json != null && !json.equalsIgnoreCase("")) {
                    StringEntity params = new StringEntity(json.toString(), "UTF-8");
                    params.setContentType("application/json");
                    request.setEntity(params);
                    System.out.println("-----parameters " + json);
                } else {
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
                    request.setEntity(formEntity);
                    System.out.println("-----parameters " + postParameters.toString());
                }

                HttpResponse response = client.execute(request);

                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                String result = "";

                if (response.getAllHeaders().length > 0) {
                    Header[] HeaderStatus = response.getHeaders("Status");

                    Header[] HeaderEkinKey = response.getHeaders("X-EKINCARE-KEY");
                    Header[] HeaderCustomerKey = response.getHeaders("X-CUSTOMER-KEY");

                    if (HeaderEkinKey.length > 0 && !HeaderEkinKey[0].getValue().equalsIgnoreCase("")) {
                        prefs.setEkinKey(HeaderEkinKey[0].getValue());
                    }
                    if (HeaderCustomerKey.length > 0 && !HeaderCustomerKey[0].getValue().equalsIgnoreCase("")) {
                        prefs.setCustomerKey(HeaderCustomerKey[0].getValue());
                    }

                    if (HeaderStatus[0].getValue().toString().contains("200") || HeaderStatus[0].getValue().toString().contains("409")) {

                        try {
                            while ((line = in.readLine()) != null) {
                                sb.append(line + NL);
                            }
                            result = sb.toString();

                            Log.e("VitalData", result);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                in.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        //						result = null;
                        result = "{\"msg\":" + "\"" + HeaderStatus[0].getValue() + "\"" + "}";
                    }
                } else {

                    try {
                        while ((line = in.readLine()) != null) {
                            sb.append(line + NL);
                        }
                        result = sb.toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (result != null) {

                    if (!AddArrayName.equalsIgnoreCase("")) {
                        result = "{" + AddArrayName + ":" + result + "}";
                    }
                }

                System.out.println("Result------- ------- Final data: " + result);

                //	Gson gson = new GsonBuilder().registerTypeAdapter(Double.class, new BadDoubleDeserializer()).create();
                mFillObject = mGson.fromJson(result, mFillObject.getClass());

            } catch (Exception e) {
                e.printStackTrace();
                isOtherError = true;
            }
        } else {
            isNetError = true;
        }
        return mFillObject;
    }

    public Object httpPatchFile(String url, Object mObject,
                                List<NameValuePair> postParameters, String json,
                                String AddArrayName, List<Header> headerList, HttpEntity entity) {

        HttpPatch httppatch;
        HttpParams httpParameters;
        int timeoutConnection = 20000;
        HttpClient httpclient = null;
        HttpResponse response = null;
        // String data = "";
        isOtherError = false;
        BufferedReader in = null;
        mFillObject = null;

        try {
            mFillObject = mObject.getClass().newInstance();

            httppatch = new HttpPatch(url);
            System.out.println("-------url " + url);

            if (headerList != null && headerList.size() > 0) {
                for (int i = 0; i < headerList.size(); i++) {
                    httppatch.addHeader(headerList.get(i));
                    System.out.println("-------header " + headerList.get(i));
                }
            }

            httpParameters = new BasicHttpParams();
            HttpConnectionParams
                    .setSoTimeout(httpParameters, timeoutConnection);
            httpclient = new DefaultHttpClient(httpParameters);

            httppatch.setEntity(entity);

            response = httpclient.execute(httppatch);
            // data = EntityUtils.toString(response.getEntity());
            //
            //
            // System.out.println("data...."+data);

            // if(data.equalsIgnoreCase("{\"is_device_deleted\":true}"))
            // setDeviceToken(true);

            //			System.out.println("-----header. " + response.toString());

            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent(), "UTF-8"));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            String result = "";

            if (response.getAllHeaders().length > 0) {
                Header[] HeaderStatus = response.getHeaders("Status");


                if (HeaderStatus[0].getValue().toString().contains("200") || HeaderStatus[0].getValue().toString().contains("409")) {

                    try {
                        while ((line = in.readLine()) != null) {
                            sb.append(line + NL);
                        }
                        result = sb.toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // result = null;
                    result = "{\"msg\":" + "\"" + HeaderStatus[0].getValue()
                            + "\"" + "}";
                }
            } else {

                try {
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    result = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //			if (result != null) {
            //
            //				if (!AddArrayName.equalsIgnoreCase("")) {
            //					result = "{" + AddArrayName + ":" + result + "}";
            //				}
            //
            //			}

            // Log.i("Appname", "Response: \n" + result);
            System.out.println("Result------- Final data: " + result);
            Log.e("ServerData", result);
            mFillObject = mGson.fromJson(result, mFillObject.getClass());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-------" + e.toString());
            isOtherError = true;
        }

        return mFillObject;
    }

    public Object httpPostFile(String url, Object mObject,
                               List<NameValuePair> postParameters, String json,
                               String AddArrayName, List<Header> headerList, HttpEntity entity) {

        HttpPost httppost;
        HttpParams httpParameters;
        //5 minuts
        int timeoutConnection = 300 * 1000;
        HttpClient httpclient = null;
        HttpResponse response = null;
        // String data = "";
        isOtherError = false;
        BufferedReader in = null;
        mFillObject = null;

        try {
            mFillObject = mObject.getClass().newInstance();

            httppost = new HttpPost(url);

            if (headerList != null && headerList.size() > 0) {
                for (int i = 0; i < headerList.size(); i++) {
                    httppost.addHeader(headerList.get(i));
                    System.out.println("------header list " + i + headerList.get(i));
                }
            }

            System.out.println("-----url " + url);

            httpParameters = new BasicHttpParams();
            HttpConnectionParams
                    .setSoTimeout(httpParameters, timeoutConnection);
            httpclient = new DefaultHttpClient(httpParameters);

            httppost.setEntity(entity);

            response = httpclient.execute(httppost);
            // data = EntityUtils.toString(response.getEntity());
            //
            //
            // System.out.println("data...."+data);

            // if(data.equalsIgnoreCase("{\"is_device_deleted\":true}"))
            // setDeviceToken(true);

            //			System.out.println("-----header. " + response.toString());

            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent(), "UTF-8"));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            String result = "";

            if (response.getAllHeaders().length > 0) {
                Header[] HeaderStatus = response.getHeaders("Status");


                if (HeaderStatus[0].getValue().toString().contains("200") || HeaderStatus[0].getValue().toString().contains("409")) {

                    try {
                        while ((line = in.readLine()) != null) {
                            sb.append(line + NL);
                        }
                        result = sb.toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // result = null;
                    result = "{\"msg\":" + "\"" + HeaderStatus[0].getValue()
                            + "\"" + "}";
                }
            } else {

                try {
                    while ((line = in.readLine()) != null) {
                        sb.append(line + NL);
                    }
                    result = sb.toString();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //			if (result != null) {
            //
            //				if (!AddArrayName.equalsIgnoreCase("")) {
            //					result = "{" + AddArrayName + ":" + result + "}";
            //				}
            //
            //			}

            // Log.i("Appname", "Response: \n" + result);
            System.out.println("Result------- Final data timeline: " + result);

            mFillObject = mGson.fromJson(result, mFillObject.getClass());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("-------" + e.toString());
            isOtherError = true;
        }

        return mFillObject;
    }

    public Object cachedGet(String method, Context context, Object mObject, String json, String AddArrayName, List<Header> headerList) {
        String token = prefs.getCustomerKey();
        String stringId=prefs.getTrendsId();
        String etag = null;
        DbResponse data = null;
        String result = null;
        for (Header header : headerList) {
            if (header.getName().equalsIgnoreCase("X-FAMILY-MEMBER-KEY")) {
                if (!"".equalsIgnoreCase(header.getName())) {
                    token = header.getValue();
                }
            }
        }

        if (TagValues.Get_All_Documents_URL.equalsIgnoreCase(method)) {
            data = dbHandler.getDocumentsData(token);
        } else if (TagValues.GET_IMMUNIZATION_URL.equalsIgnoreCase(method)) {
            data = dbHandler.getImmunization(token);
        } else if (TagValues.Timeline_URL.equalsIgnoreCase(method)) {
            data = dbHandler.getTimelineData(token);
        } else if (method.contains("/body_assessments/") || method.contains("/vision_assessments/")) {
            data = dbHandler.getAllAssessmentData(stringId);
        } else if (method.contains("/lab_results/trends")) {
            data = dbHandler.getAllTrendsData(stringId);
        }
        if (data != null) {
            etag = data.getEtag();
        }
        // If no internet,
        if (isInternetOncheck(context)) {
            if (data != null && data.getEtag() != null) {
                headerList.add(new BasicHeader("If-None-Match", data.getEtag()));
            }
            NetworkResponse response = this.httpGetData(method, mObject, json, AddArrayName, headerList);
            if (response.getResponsCode() == 200) { // OK response


                etag = response.getEtag();
                String jsonResponse = response.getResult();
                if (TagValues.Get_All_Documents_URL.equalsIgnoreCase(method)) {
                    dbHandler.insertDocuments(jsonResponse, token, etag);
                } else if (TagValues.GET_IMMUNIZATION_URL.equalsIgnoreCase(method)) {
                    dbHandler.insertImmunization(jsonResponse, token, etag);
                } else if (TagValues.Timeline_URL.equalsIgnoreCase(method)) {
                    dbHandler.insertTimeline(jsonResponse, token, etag);
                } else if (method.contains("/body_assessments/") || method.contains("/vision_assessments/")) {
                    dbHandler.insertAssessmentData(jsonResponse, stringId, etag);
                } else if (method.contains("/lab_results/trends")) {
                    dbHandler.insertGraphData(jsonResponse, stringId, etag);

                }
                return convertFromJson(method, jsonResponse);
            } else if (response.getResponsCode() == 304 || (response.getResponsCode() == 500)) { //Cache is ok. not modified
                return convertFromJson(method, data.getResponse());
            } else {
                return null;
            }
        } else {
            // No internet
            return convertFromJson(method, data.getResponse());

        }
    }

    /**
     * Converts a json into appropriate class using the method url
     *
     * @param method
     * @param jsonResponse
     * @return
     */
    public Object convertFromJson(String method, String jsonResponse) {
        if (jsonResponse == null || "".equalsIgnoreCase(jsonResponse)) {
            return null;
        } else {
            if (TagValues.Get_All_Documents_URL.equalsIgnoreCase(method)) {
                return new Gson().fromJson(jsonResponse, GetDocumentsData.class);
            } else if (TagValues.GET_IMMUNIZATION_URL.equalsIgnoreCase(method)) {
                return new Gson().fromJson(jsonResponse, Immunization.class);

            } else if (TagValues.Timeline_URL.equalsIgnoreCase(method)) {

                return new Gson().fromJson(jsonResponse, TimelineData.class);
            } else if (method.contains("/body_assessments/") || method.contains("/vision_assessments/")) {
                return new Gson().fromJson(jsonResponse, AssessmentData.class);

            } else if (method.contains("/lab_results/trends")) {
                return new Gson().fromJson(jsonResponse, GraphData.class);
            } else if (method.contains("/wizard/status")) {
                return new Gson().fromJson(jsonResponse, Register.class);

            } else if (method.contains("/customer_vitals")) {
                return new Gson().fromJson(jsonResponse, Register.class);
            } else if (method.contains(("/passwords/forget_password"))) {
                return new Gson().fromJson(jsonResponse, Register.class);
            } else if (method.contains("/passwords/verify_otp")) {
                return new Gson().fromJson(jsonResponse, Register.class);

            }else if(method.contains("/passwords/verify_otp")){
                return new Gson().fromJson(jsonResponse,Register.class);
            }else if(method.contains("/passwords/verify_password")){
                return new Gson().fromJson(jsonResponse,Register.class);
            }else if(TagValues.GET_CUSTOMER_DETAIL_URL.equalsIgnoreCase(method)){

                return new Gson().fromJson(jsonResponse, ProfileData.class);

            }
        }
        return null;
    }

    public static class BadDoubleDeserializer implements JsonDeserializer<Double> {

        @Override
        public Double deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            try {
                return Double.parseDouble(element.getAsString().replace(',', '.'));
            } catch (NumberFormatException e) {
                throw new JsonParseException(e);
            }
        }

    }
}

/*data = dbHandler.getTimelineData(mMainActivity.getCurrentCustomer().getIdentification_token());
        if(data != null){
            //show spinner
           //show data
            //hide spinner
            // start asyc task to get data
            // save in asynctask
        }else{
           //if internet
            //show spinner
            //start asynctask
            // hide spinner
            // save data in asynctask
            //else
            //show dialog
        }*/
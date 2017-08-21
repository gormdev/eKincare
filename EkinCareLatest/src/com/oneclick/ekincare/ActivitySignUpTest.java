package com.oneclick.ekincare;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;
import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.ui.fragment.FragmentSignUp;
import com.ekincare.ui.fragment.FragmentSignUpOtpVarification;
import com.ekincare.util.SignUpDataInterface;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

/**
 * Created by RaviTejaN on 21-03-2017.
 */

public class ActivitySignUpTest extends AppCompatActivity implements SignUpDataInterface
{
    private Register mRegister;
    private PreferenceHelper prefs;
    MixpanelAPI mixpanel;
    Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;
    Toolbar toolbar;
    TextView buttonNext,termsAndCondition,alreadySignIn;

    private String genderRegister="";
    private String firstNameRegister="";
    private String dob="";
    private String mobileNo="";
    private int registerYear = 0;
    private int registerMonth=0;
    private String registerOtp="";
    private String email = "";
    //Fragment mFragment ;

    String customerKey;
    String ekincareKey;
    boolean isReadingSms = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout_test);
        customerManager = CustomerManager.getInstance(this.getApplicationContext());
        prefs = new PreferenceHelper(this);
        System.out.println("RegisterActivity.onCreate===="+prefs.getIsWizardComplete()+"====="
                +prefs.getCustomerKey()+"==="+prefs.getEkinKey()+"=="+ Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        mixpanel = MixpanelAPI.getInstance(this, TagValues.MIXPANEL_TOKEN);

        mixpanel.timeEvent("Register_Page load success");
        try {
            JSONObject props = new JSONObject();
            props.put("Register", "PageLoad");
            mixpanel.track("Register_Page load success", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        init();

        setUpToolBar();

        setUpFragment(new FragmentSignUp());
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(SmsListener, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivitySignUpTest.this.unregisterReceiver(SmsListener);

        /*if (progressDialog!=null && progressDialog.isShowing() ){
            progressDialog.cancel();
        }*/
    }

    private void setUpFragment(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.a_enter_from_right, R.anim.a_exit_to_left);
        fragmentTransaction.add(R.id.sign_up_question,fragment,null);
        fragmentTransaction.commit();
    }

    private void setUpFragmentExit(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.a_enter_from_left, R.anim.a_exit_to_right);
        fragmentTransaction.add(R.id.sign_up_question,fragment,null);
        fragmentTransaction.commit();
    }

    private void init()
    {

        buttonNext =(TextView) findViewById(R.id.button_next);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        termsAndCondition=(TextView)findViewById(R.id.termes_and_condition);
        alreadySignIn=(TextView)findViewById(R.id.signin_button_already);

        String termsRegister="Already have an account? <font color=#2196F3>LOGIN</font>";
        alreadySignIn.setText(Html.fromHtml(termsRegister));

        String terms="By clicking Register,I Agree to the <font color=#4a4a4a>Terms And Conditions</font>";
        termsAndCondition.setText(Html.fromHtml(terms));
        alreadySignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivitySignUpTest.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        termsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTermsAndCondition newFragment = new DialogTermsAndCondition();
                newFragment.show(ActivitySignUpTest.this.getSupportFragmentManager(), "");
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.sign_up_question);
                if(mFragment instanceof FragmentSignUp){

                    if(((FragmentSignUp) mFragment).isFirstNameEmpty() ){
                        ((FragmentSignUp) mFragment).setError();
                    }else if(((FragmentSignUp) mFragment).isEmailEmpty()) {
                        ((FragmentSignUp) mFragment).setErrorEmail();
                    }else if(((FragmentSignUp) mFragment).isValidMobile()) {
                        if (Build.VERSION.SDK_INT >= 23) {
                                if (!checkPermission()) {
                                    requestPermission();
                                } else {
                                    regesterMethodCall();
                                }
                            } else {
                                regesterMethodCall();
                            }
                        }else{
                            ((FragmentSignUp) mFragment).setErrorMobile();
                        }
                }else if(mFragment instanceof FragmentSignUpOtpVarification) {
                    if(((FragmentSignUpOtpVarification) mFragment).isValidOtp())
                    {
                        verifyOtpRequest(registerOtp);
                    }else{
                        Toast.makeText(ActivitySignUpTest.this, "Invalid otp", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });



    }



    private void verifyOtpRequest(String otps) {
        System.out.println("otp========="+otps);
        if (!mobileNo.equalsIgnoreCase(""))
        {
            System.out.println("otp mobile========="+otps);
            if (!otps.equalsIgnoreCase(""))
            {
                System.out.println("otp reg========="+otps);
                isReadingSms = true;

                JSONObject obj = new JSONObject();
                try {
                    JSONObject object = new JSONObject();
                    object.put("mobile_number", mobileNo);
                    object.put("otp", otps);
                    obj.put("online_customer", object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (NetworkCaller.isInternetOncheck(ActivitySignUpTest.this))
                {
                    mRegister = new Register();
                    verifyOTPVolley(obj);
                } else {
                    CustomeDialog.dispDialog(ActivitySignUpTest.this, "Internet not available");
                }
            } else {
                CustomeDialog.dispDialog(ActivitySignUpTest.this, "Please enter Code");
            }
        } else {
            CustomeDialog.dispDialog(ActivitySignUpTest.this, "Please enter PhoneNumber");
        }
    }

    private void verifyOTPVolley(JSONObject obj)
    {
        System.out.println("ActivitySignUpTe.verifyOTPVolley obj="+obj);
        System.out.println("ActivitySignUp.verifyOTPVolley "+" prefs.getCustomerKey() =" +  prefs.getCustomerKey()  + "  prefs.getEkinKey() "+ prefs.getEkinKey() + " customerManager.getDeviceID(ActivitySignUp.this) " + customerManager.getDeviceID(ActivitySignUpTest.this));
        String URL = TagValues.VERIFY_OTP_URL ;
        showPDialog();

        JsonObjectRequest jsObjRequesttwo = new JsonObjectRequest(Request.Method.POST,URL,obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("login verify otp===="+response.toString());
                            hidePDialog();
                            verifyOtpResposne(response);
                        }else{
                            CustomeDialog.dispDialog(ActivitySignUpTest.this, TagValues.DATA_NOT_FOUND);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ActivitySignUp.onErrorResponse="+error.toString());
                        Toast.makeText(ActivitySignUpTest.this, "Wrong Otp", Toast.LENGTH_SHORT).show();
                        hidePDialog();
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
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivitySignUpTest.this));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequesttwo);

    }

    char[] chars ;
    StringBuilder sb;
    Random random ;
    String randomNumber,randomWord;

    private void verifyOtpResposne(JSONObject response) {
        System.out.println("ActivitySignUp.verifyOtpResposne response="+response);
        mRegister = new Gson().fromJson(response.toString(), Register.class);
        if (mRegister != null && mRegister.getMessage() != null
                && mRegister.getMessage().contains("success")) {
            try {
                Map<String, Object> eventValue = new HashMap<String, Object>();
                eventValue.put(AFInAppEventParameterName.EVENT_START,"Registration Complet");
                AppsFlyerLib.getInstance().trackEvent(this, AFInAppEventType.COMPLETE_REGISTRATION,eventValue);
                Toast.makeText(this, "" + mRegister.getMessage(), Toast.LENGTH_SHORT).show();
                prefs.setAppToken(mRegister.getToken());
                chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                sb = new StringBuilder();
                random = new Random();
                randomNumber = String.valueOf (random.nextInt(96) + 32);
                for (int i = 0; i < 8; i++) {
                    char c = chars[random.nextInt(chars.length)];
                    sb.append(c);
                }
                randomWord = sb.toString().concat(randomNumber);

                resetPasswordVolley(mobileNo,  randomWord, prefs.getAppToken(), Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

            } catch (NullPointerException e) {
                e.printStackTrace();
                CustomeDialog.dispDialog(ActivitySignUpTest.this, "" + e.toString());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                CustomeDialog.dispDialog(ActivitySignUpTest.this, "" + e.toString());
                return;
            }
        } else {
            String msg = "";
            if (mRegister.getMsg() != null && !mRegister.getMsg().equalsIgnoreCase("")) {
                msg = mRegister.getMsg().toString();
                // msg =TagValues.INVALID_RESPONSE+msg;
            } else {
                msg = "Otp verification error";
            }
            CustomeDialog.dispDialog(ActivitySignUpTest.this, msg);
        }
    }

    private void resetPasswordVolley(final String mUserName, final String randomWord, String appToken, String string) {

        System.out.println("allfields======"+appToken+"===="+mUserName+"======="+randomWord+"====="+Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        JSONObject parentData = new JSONObject();
        JSONObject childData = new JSONObject();
        try {
            parentData.put("reset_password_token", appToken);
            parentData.put("channel_id", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            childData.put("mobile_number", mUserName);
            childData.put("password", randomWord);
            parentData.put("online_customer", childData);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("test====="+parentData.toString());

        String URL = TagValues.VERIFY_PASSWORD_URL;
        showPDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,URL,parentData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            hidePDialog();
                            System.out.println("VerifyCodeActivityTest.onResponse======"+response.toString());
                            prefs.setIsLogin(true);
                            prefs.setEkinKey(ekincareKey);
                            prefs.setCustomerKey(customerKey);
                            prefs.setLoggedinUserName(mUserName);
                            prefs.setPassword(randomWord);
                            prefs.setLoggedinUserDOB(dob);

                            prefs.setIsFirstTimeRegister(true);
                            prefs.setCustomerWizardStatus(0);

                            Intent intent = new Intent(ActivitySignUpTest.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }else{

                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                    }
                }){

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                for (String s : keySet) {
                    System.out.println("my========"+s);
                }
                System.out.println("VerifyCodeActivityTest.parseNetworkResponse====="+headers.get("X-CUSTOMER-KEY"));
                System.out.println("VerifyCodeActivityTest.parseNetworkResponse====="+headers.get("X-EKINCARE-KEY"));

                customerKey = headers.get("X-CUSTOMER-KEY");
                ekincareKey=headers.get("X-EKINCARE-KEY");
                prefs.setEkinKey(ekincareKey);
                prefs.setCustomerKey(customerKey);
                System.out.println("login post===="+customerKey+"==="+ekincareKey);

                return super.parseNetworkResponse(response);
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-type", "application/json");
                //params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private BroadcastReceiver SmsListener = new BroadcastReceiver() {
        private String otpCode;

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED") && !isReadingSms) {
                Bundle bundle = intent.getExtras(); // ---get the SMS message
                // passed in---
                SmsMessage[] msgs = null;
                if (bundle != null) {
                    // ---retrieve the SMS message received---
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        for (int i = 0; i < 1; i++) {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            String msgBody = msgs[i].getMessageBody();

                            if (msgBody.contains("OTP is confidential. Sharing it with anyone gives them access to your medical data")) {
                                String[] spitStr = msgBody.split("\\.");
                                otpCode = spitStr[0].replaceAll("[^0-9]", "");
                                Log.i("OTP CODE ", otpCode);
                                System.out.println("-------message received " + otpCode);

                                Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.sign_up_question);
                                if(mFragment instanceof FragmentSignUpOtpVarification)
                                {
                                    ((FragmentSignUpOtpVarification) mFragment).setOtpToEditText(otpCode);
                                }
                                verifyOtpRequest(otpCode);

                            }
                        }
                    } catch (Exception e) {
                        // Log.d("Exception caught",e.getMessage());
                    }
                }
            }
        }
    };




    private void regesterMethodCall() {
        JSONObject object2 = new JSONObject();
        JSONObject object = new JSONObject();
        try {
            object.put("first_name", firstNameRegister);

            object.put("last_name", "");
            object.put("email", email);

            object.put("mobile_number", mobileNo);
           // object.put("date_of_birth", dob);

           /* if (genderRegister.equals("Male")) {
                object.put("gender", "Male");
            } else {
                object.put("gender", "Female");
            }*/
            object2.put("online_customer",object);
        } catch (JSONException e1) {
            e1.printStackTrace();
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        if (NetworkCaller.isInternetOncheck(ActivitySignUpTest.this)) {
            // mParcelabelClass= new Login();
            mRegister = new Register();
            String requestString = "{\"online_customer\":" + object.toString() + "}";
            System.out.println("register========"+object2.toString());

            mixpanel.timeEvent("Register_Register button click");
            try {
                JSONObject props = new JSONObject();
                props.put("Register", "Click Register Button");
                mixpanel.track("Register_Register button click", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }

            registerVolleyRequest(object2);
            //GetWebData(TagValues.REGISTER_URL, requestString, postParameters, mRegister,false);
        } else {
            CustomeDialog.dispDialog(ActivitySignUpTest.this, "Internet not available");
        }
    }

    private void setUpToolBar(){
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backTrackNavagation();
            }
        });

    }

    private void backTrackNavagation() {
        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.sign_up_question);
        if(mFragment instanceof FragmentSignUp){
            Intent intent = new Intent(ActivitySignUpTest.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }else if(mFragment instanceof FragmentSignUpOtpVarification) {
            exitDialog();
        }
    }

    private void exitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySignUpTest.this);

        builder.setTitle("");
        builder.setMessage("You have not entered the otp. Are you sure you want to exit the registration process?");

        String positiveText = "No";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        String negativeText = "Yes";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(ActivitySignUpTest.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        // display dialog
        dialog.show();
    }

    private void registerVolleyRequest(JSONObject object2) {
        System.out.println("myvolley Register======="+object2.toString());
        String URL = TagValues.REGISTER_URL ;
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,URL,object2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hidePDialog();
                        if(response!=null){
                            System.out.println("myvolley register======="+response.toString());

                            registerVolleyResponse(response);
                        }else{
                            CustomeDialog.dispDialog(ActivitySignUpTest.this, TagValues.DATA_NOT_FOUND);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("response======== register"+error);
                        hidePDialog();

                        NetworkResponse errorRes = error.networkResponse;
                        String stringData = "";
                        if(errorRes != null && errorRes.data != null){
                            try {
                                stringData = new String(errorRes.data,"UTF-8");
                                System.out.println("errorRes====="+stringData);
                                JSONObject mainObject = new JSONObject(stringData);
                                if( mainObject.getString("msg").equalsIgnoreCase("duplicate mobile number")){
                                    showDuplicateDialog( mainObject.getString("msg"));
                                }
                                Toast.makeText(ActivitySignUpTest.this,mainObject.getString("msg"),Toast.LENGTH_LONG).show();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
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
                params.put("X-DEVICE-ID", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }
    private void showPDialog() {
        mAlert_Dialog = new Dialog(this);
        mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlert_Dialog.setContentView(R.layout.materialprogressbar);
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mAlert_Dialog.setCancelable(false);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

    private void registerVolleyResponse(JSONObject response) {
        mRegister = new Gson().fromJson(response.toString(),Register.class);
        if (mRegister != null && mRegister.getMsg() != null && mRegister.getMsg().contains("success"))
        {
            System.out.println("ActivitySignUpTest.registerVolleyResponse=========="+"success");
            try {
                mixpanel.timeEvent("Register_Register button success");
                try {
                    mixpanel.identify(mobileNo);
                    mixpanel.getPeople().identify(mobileNo);
                    JSONObject props = new JSONObject();
                    props.put("LoginName", firstNameRegister);
                    props.put("LoginNumber",mobileNo);
                    mixpanel.getPeople().set(props);
                    mixpanel.track("Register_Register button success", props);
                } catch (JSONException e) {
                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                }

                //new ReadSMSAsysnchTask().execute();
                setUpFragment(new FragmentSignUpOtpVarification());

            } catch (NullPointerException e) {
                e.printStackTrace();
                CustomeDialog.dispDialog(this, "" + e.toString());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                CustomeDialog.dispDialog(this, "" + e.toString());
                return;
            }
        } else {
            System.out.println("ActivitySignUpTest.registerVolleyResponse=========="+"success fail");

            String msg = "";
            if (mRegister.getMsg() != null) {
                msg = mRegister.getMsg().toString();
                if (msg.contains("409")) {
                    msg = msg.replaceAll("409", " ");
                }
                if(msg.equalsIgnoreCase("duplicate mobile number")){
                    showDuplicateDialog(msg);
                }
            }
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
        }
    }

    private void showDuplicateDialog(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Alert!");
        builder.setMessage(msg);

        String positiveText = "Change number";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        String negativeText = "Sign In";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent(ActivitySignUpTest.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        // display dialog
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backTrackNavagation();
    }

    @Override
    public void callNextFragmnet() {
        Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.sign_up_question);
        if(mFragment instanceof FragmentSignUp)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        }

    }

    @Override
    public void hideBackButton() {
        alreadySignIn.setVisibility(View.INVISIBLE);
        buttonNext.setText("SUBMIT OTP");
    }

    @Override
    public void showBackButton() {
       // buttonBack.setVisibility(View.VISIBLE);
    }

    @Override
    public void sendOtp() {
        regesterMethodCall();
    }

    @Override
    public void setGenderSignup(String genderSignup) {
        genderRegister=genderSignup;

    }

    @Override
    public String getGenderSignUp() {
        return genderRegister;
    }

    @Override
    public void setName(String firstName) {
        firstNameRegister=firstName;
    }

    @Override
    public String getName() {
        return firstNameRegister;
    }

    @Override
    public void setdateOfBirth(String dateOfBirth) {
        dob=dateOfBirth;
    }

    @Override
    public void setmobileNumber(String mobileNumber) {
        mobileNo=mobileNumber;
    }

    @Override
    public String getdateOfBirth() {
        return dob;
    }

    @Override
    public String getmobileNumber() {
        return mobileNo;
    }

    @Override
    public String getEmailId() {
        return email;
    }

    @Override
    public void setEmailId(String emailId) {
        email = emailId;
    }


    @Override
    public void birthYear(int birthYear) {
        registerYear = birthYear;
    }

    @Override
    public void birthMonth(int birthMonth) {
        registerMonth = birthMonth;
    }

    @Override
    public int getAge() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);

        int diff = year - registerYear;
        if (month > registerMonth || (month == registerMonth)) {
            diff--;
        }
        return diff;
    }

    @Override
    public String getOtp() {
        return registerOtp;
    }

    @Override
    public void setOtp(String otp) {
        registerOtp  = otp;
    }

    @Override
    public void resendOtp() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                resendOtpRequest();
            }
        } else {
            resendOtpRequest();
        }
    }


    private static final int PERMISSION_REQUEST_CODE = 200;

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, RECEIVE_SMS);
        int result1 = ContextCompat.checkSelfPermission(this, READ_SMS);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{RECEIVE_SMS,READ_SMS}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0)
                {
                    boolean recevicSms = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readSms = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (recevicSms && readSms)
                    // setUpFragment(new FragmentSignUpOtpVarification());
                    {
                        regesterMethodCall();
                    }
                    else {
                        Toast.makeText(this, "Permission Denied, You cannot receive SMS.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void resendOtpRequest() {
        try {
            if (!mobileNo.equalsIgnoreCase("")) {
                JSONObject object = new JSONObject();
                if (NetworkCaller.isInternetOncheck(this)) {
                    mRegister = new Register();
                    System.out.println("resend Number===="+mobileNo);
                    resendOtpVolley(mobileNo);
                } else {
                    CustomeDialog.dispDialog(this, "Internet not available");
                }

            } else {
                CustomeDialog.dispDialog(this, "Please enter PhoneNumber");
            }

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }


    private void resendOtpVolley(String mUserName){
        System.out.println("resend=========="+mUserName);
        String URL = TagValues.RESEND_OTP_URL + mUserName ;
        showPDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null)
                        {
                            System.out.println("resend otp success===="+response.toString());
                            hidePDialog();
                            resendOtpResposne(response);
                        }
                        else
                        {
                            CustomeDialog.dispDialog(ActivitySignUpTest.this, TagValues.DATA_NOT_FOUND);
                        }
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                hidePDialog();
            }
        })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
            {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivitySignUpTest.this));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(ActivitySignUpTest.this).addToRequestQueue(jsObjRequest);
    }

    private void resendOtpResposne(JSONObject response){

        mRegister = new Gson().fromJson(response.toString(), Register.class);
        if (mRegister != null && mRegister.getMessage() != null
                && mRegister.getMessage().contains("success")) {
            try {
                System.out.println("comeing=========="+"success");
                Toast.makeText(ActivitySignUpTest.this, "" + mRegister.getMessage(), Toast.LENGTH_SHORT).show();
                //  new ReadSMSAsysnchTask().execute();

            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("comeing Null=========="+"success");
                CustomeDialog.dispDialog(ActivitySignUpTest.this, "" + e.toString());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("comeingExcep=========="+"success");
                CustomeDialog.dispDialog(ActivitySignUpTest.this, "" + e.toString());
                return;
            }
        } else {
            String msg = "";
            if (mRegister.getMessage() != null && !mRegister.getMessage().equalsIgnoreCase("")) {
                msg = mRegister.getMessage().toString();
            } else {
                msg = "code sending error";
            }
            CustomeDialog.dispDialog(ActivitySignUpTest.this, msg);
        }
    }

}
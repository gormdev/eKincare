package com.oneclick.ekincare;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.EkinCareApplication;
import com.ekincare.app.ProfileManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Image;
import com.oneclick.ekincare.vo.ProfileData;
import com.oneclick.ekincare.vo.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

/**
 * Created by RaviTejaN on 22-07-2016.
 */
public class LoginActivity extends AppCompatActivity
{
    private AppCompatEditText editTextUserName;
    private AppCompatEditText editTextPassword;
    private TextInputLayout textInputLayoutUserName;
    private TextInputLayout textInputLayoutPassword;
    private TextView textViewRegister,termsAndConditions;
    private AppCompatButton appCompatButtonLogin;
    Toolbar toolbar;
    private CheckBox checkBoxOTP;
    private RelativeLayout relativeLayoutContainer;

    private Register mRegister;

    private PreferenceHelper prefs;

    MixpanelAPI mixpanel;

    ValueAnimator mAnimator;


    boolean isReadingSms = false;
    Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;
    EditText otpValue;
    TextView btnResendOTP;
    TextView timeleftOtp;
    TextView resendMessage;
    LinearLayout hideSmsTimeLeft;
    private static final int PERMISSION_REQUEST_CODE = 200;
    ProfileManager mProfileManager;
    private ProfileData mProfileData;
    private LinearLayout layoutOtpForm;
    private LinearLayout layoutLogInForm;
    CountDownTimer countDownTimer;
    GoogleCloudMessaging gcm;
    String regId = "";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        this.customerManager = CustomerManager.getInstance(this.getApplicationContext());

        mProfileManager=ProfileManager.getInstance(this);

        prefs = new PreferenceHelper(this);

        mProfileData = new ProfileData();

        mixpanel = MixpanelAPI.getInstance(this, TagValues.MIXPANEL_TOKEN);
        mixpanel.timeEvent("Login_Page load success");
        try {
            JSONObject props = new JSONObject();
            props.put("LoginPage", "PageLoad");
            mixpanel.track("Login_Page load success", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }

        initViews();

        setUpToolBar();

        editTextPasswordLogic();

        editTextUserNameLogic();

        //checkBoxOTPLogic();
        checkBoxOTP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkStatusChangeLogic(isChecked);
            }
        });

        termsAndConditionsLogic();

        appCompatButtonLoginLogic();

        textViewRegisterLogic();

        editTextUserName.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {

                    System.out.println("FragmentFirstAndLastName.onEditorAction===="+"Yesssssss");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                }
                return false;
            }
        });

        editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    checkLogin();
                }
                return false;
            }
        });

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        this.registerReceiver(SmsListener, filter);

        relativeLayoutContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                hideToolbarOnKeyboardUp();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        EkinCareApplication.setActivityVisible(true);
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(context);
            regId = prefs.getRegistrationId();
            if (regId.isEmpty()) {
                registerInBackground();
            } else {

            }

        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }

                    String googleProjectId = getResources().getString(R.string.project_number);
                    regId = gcm.register(googleProjectId);
                    Log.d("RegisterGcmIdInfo", "registerInBackground - regId: " + regId);
                    System.out.println("GCMID============="+regId);
                    msg = "Device registered, registration ID=" + regId;

                    prefs.storeRegistrationId(context, regId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    Log.d("RegisterGcmIdInfo", "Error: " + msg);
                }
                Log.d("RegisterGcmIdInfo", "AsyncTask completed: " + msg);
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {
                    prefs.storeRegistrationId(context, regId);

                } else {

                }
            }
        }.execute(null, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginActivity.this.unregisterReceiver(SmsListener);
    }



    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        } else {

        }
        return true;
    }
    //////////////////////////////clicks logics/////////////////


    private void textViewRegisterLogic() {
        textViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setLoggedinUserDOB("");
                prefs.setCustomerWizardStatus(0);
                prefs.setPREF_RUNTIME_PERMISSION("Register");
                callSignupACtivity();


            }
        });
    }

    private void callSignupACtivity() {
        Intent intent = new Intent(LoginActivity.this, ActivitySignUpTest.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void appCompatButtonLoginLogic(){

        appCompatButtonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if (appCompatButtonLogin.getText().toString().equalsIgnoreCase("Login"))
                {
                    checkLogin();

                } else if(appCompatButtonLogin.getText().toString().equalsIgnoreCase("Get OTP")){
                     if (Build.VERSION.SDK_INT >= 23) {
                        if (!checkPermission()) {
                            requestPermission();
                        } else {
                            checkRequestSms();
                        }
                    } else {
                        checkRequestSms();
                    }

                }else if(appCompatButtonLogin.getText().toString().equalsIgnoreCase("Submit OTP"))
                {
                    checkOTPValidtyLogic();
                }


            }
        });
    }

    private  void checkLogin(){
            String phone = editTextUserName.getText().toString().trim();
            String pwd = editTextPassword.getText().toString().trim();
            try {
                if (!phone.equalsIgnoreCase("") && phone.length()==10 ){
                    if (!pwd.equalsIgnoreCase("")){
                        loginUsingPassword(phone,pwd);
                    }else{
                        textInputLayoutPassword.setError("Invalid password");
                    }
                }else{
                    textInputLayoutUserName.setError("Invalid mobile");
                }
            } catch (Exception e) {
                String errorMessage = e.getMessage();
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
    private void checkRequestSms(){
        String phone = editTextUserName.getText().toString().trim();
        try {
            if (!phone.equalsIgnoreCase("") && phone.length()==10 )
            {
                loginUsingOtpLogic(phone);
            }else{
                textInputLayoutUserName.setError("Invalid mobile");
            }

        }catch (Exception e) {
            String errorMessage = e.getMessage();
            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }



    private void termsAndConditionsLogic()
    {
        termsAndConditions.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogTermsAndCondition newFragment = new DialogTermsAndCondition();
                newFragment.show(LoginActivity.this.getSupportFragmentManager(), "");
            }
        });
    }



    private void editTextUserNameLogic()
    {
        editTextUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTextUserName.getText().toString().isEmpty()){
                    textInputLayoutUserName.setError("Invalid Mobile");
                }else{
                    textInputLayoutUserName.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void editTextPasswordLogic()
    {
        editTextPassword.setTypeface(Typeface.DEFAULT);
        editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(editTextPassword.getText().toString().isEmpty()){
                    textInputLayoutPassword.setError("Invalid password");
                }else{
                    textInputLayoutPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextPassword.getViewTreeObserver().addOnPreDrawListener(  new ViewTreeObserver.OnPreDrawListener()
        {
            @Override
            public boolean onPreDraw()
            {
                editTextPassword.getViewTreeObserver().removeOnPreDrawListener(this);
                editTextPassword.setVisibility(View.VISIBLE);

                final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                editTextPassword.measure(widthSpec, heightSpec);

                mAnimator = slideAnimator(0, editTextPassword.getMeasuredHeight(),editTextPassword);

                return true;
            }
        });
    }

    /////////////////////check box logic/////////////////////
    private void checkStatusChangeLogic(boolean isChecked)
    {
        System.out.println("LoginActivity.checkStatusChangeLogic===="+isChecked);
        if(isChecked){
            textInputLayoutPassword.setErrorEnabled(false);
            collapse(editTextPassword);
            appCompatButtonLogin.setText("Get OTP");
          //  editTextUserName.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }else{
            expand(editTextPassword);
            appCompatButtonLogin.setText("Sign In");
        }
    }

    //////////////////////AnimaTION///////////////////
    private void expand(View layout)
    {
        //set Visible
        layout.setVisibility(View.VISIBLE);
        mAnimator.start();
    }

    private void collapse(final View layout)
    {
        int finalHeight = layout.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0,layout);

        mAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationEnd(Animator animator)
            {
                layout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end,final View layout)
    {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator)
            {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = layout.getLayoutParams();
                layoutParams.height = value;
                layout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    //////////////////////initiliaing View/////////////////////
    private void initViews()
    {
        prefs.ClearAllData();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        checkBoxOTP = (CheckBox) findViewById(R.id.checkbox_password);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.loginButton);


        textViewRegister = (TextView) findViewById(R.id.registerButton);
        String termsRegister="Don't have account? <font color=#2196F3>REGISTER</font>";
        textViewRegister.setText(Html.fromHtml(termsRegister));

        termsAndConditions= (TextView) findViewById(R.id.termsAndCondints);

        String terms="By clicking Login,I Agree to the <font color=#4a4a4a>Terms And Conditions</font>";
        termsAndConditions.setText(Html.fromHtml(terms));

        editTextUserName = (AppCompatEditText) findViewById(R.id.edit_text_user_name);

        relativeLayoutContainer = (RelativeLayout) findViewById(R.id.container_login_layout);

        editTextPassword = (AppCompatEditText) findViewById(R.id.edit_text_password);

        textInputLayoutUserName = (TextInputLayout) findViewById(R.id.floating_user_name);

        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.floating_password);


        layoutLogInForm = (LinearLayout)findViewById(R.id.layout_login_form);

        layoutOtpForm = (LinearLayout)findViewById(R.id.layout_otp_form);

        otpValue = (EditText)findViewById(R.id.otp_value_enter);

        btnResendOTP=(TextView)findViewById(R.id.resend_otp);


        timeleftOtp=(TextView)findViewById(R.id.timer_for_otp);

        hideSmsTimeLeft=(LinearLayout)findViewById(R.id.hide_sms_time_left);

        resendMessage=(TextView)findViewById(R.id.resend_otp_message);
    }

    //////////////////////Toolbar///////////////////////////
    private void setUpToolBar()
    {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_clear_white_24px);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    ///////////////////////////permission logic///////////////
    private boolean checkPermission()
    {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED ;
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{RECEIVE_SMS,READ_SMS}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean recevicSms = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readSms = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (recevicSms && readSms)
                        if(prefs.getPREF_RUNTIME_PERMISSION().equals("Register")){
                            callSignupACtivity();
                        }else{
                            checkRequestSms();
                        }
                    else {
                        Toast.makeText(LoginActivity.this, "Permission Denied, You cannot receive SMS.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    /////////////////////////login logics///////////////////////
    private void loginUsingPassword(String phone, String pwd)
    {
        JSONObject object = new JSONObject();
        try {
            object.put("mobile", phone);
            object.put("password", pwd);
            object.put("channel_id", customerManager.getDeviceID(LoginActivity.this));
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        System.out.println("obj========"+object.toString());
        if (NetworkCaller.isInternetOncheck(LoginActivity.this)) {

            mixpanel.timeEvent("Login_Login button click");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginPage", "Login_Login button click");
                mixpanel.track("Login_Login button click", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }

            loginVolley(object);
        } else {
            CustomeDialog.showToast(LoginActivity.this, "Internet not available");
        }
    }

    private void loginUsingOtpLogic(String phone){
        if (NetworkCaller.isInternetOncheck(LoginActivity.this)) {
            mRegister = new Register();
            otpLoginRequest(phone);
        } else {
            CustomeDialog.showToast(LoginActivity.this, "Internet not available");
        }
    }

    private void otpLoginRequest(String phone) {
            String URL = TagValues.RESEND_OTP_URL + phone ;
            System.out.println("check======"+URL);
            showPDialog();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            if(response!=null)
                            {
                                System.out.println("login verify===="+response.toString());
                                hidePDialog();
                                otpLoginResposne(response);
                            }
                            else
                            {
                                CustomeDialog.showToast(LoginActivity.this, TagValues.DATA_NOT_FOUND);
                            }
                        }
                    }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    hidePDialog();
                    System.out.println("ActivitySignUp.registerVolleyResponse=========="+"success fail");

                    String json = "";
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        switch(response.statusCode){
                            case 400:
                                json = new String(response.data);
                                json = trimMessage(json, "message");
                                if(json != null) displayMessage(json);
                                break;
                            default:
                                json = new String(response.data);
                                json = trimMessage(json, "message");
                                if(json != null) displayMessage(json);
                                break;

                        }
                    }

                    if(json.contains("No record with provided mobile number")){
                        json = json.replace("Bad Request: ","");
                        showDuplicateDialog(json);
                    }
                }
            })
            {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                    params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                    params.put("X-DEVICE-ID", customerManager.getDeviceID(LoginActivity.this));
                    params.put("Content-type", "application/json");
                    params.put("Accept", "application/json");

                    return params;
                }
            };
            VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
        }

    public String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    //Somewhere that has access to a context
    public void displayMessage(String toastString){
        Toast.makeText(this, toastString, Toast.LENGTH_LONG).show();
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

        String negativeText = "Sign Up";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent(LoginActivity.this, ActivitySignUpTest.class);
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


    private void otpLoginResposne(JSONObject response) {
        if (response != null) {
            try {
                if (response.getString("message").contains("success"))
                {

                   // Toast.makeText(this, "" + mRegister.getMessage(), Toast.LENGTH_SHORT).show();

                    //new ReadSMSAsysnchTask().execute();
                    layoutLogInForm.setVisibility(View.GONE);
                    layoutOtpForm.setVisibility(View.VISIBLE);
                    appCompatButtonLogin.setText("Submit OTP");
                    textViewRegister.setVisibility(View.INVISIBLE);

                    countDownTimer = new CountDownTimer(30000, 1000) { // adjust the milli seconds here
                        public void onTick(long millisUntilFinished) {
                            timeleftOtp.setText("Waiting in "+"" +String.format("%d seconds",
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

                            /*timeleftOtp.setText("Fetching code in "+"" + String.format("%d : %d sec ",
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    */    }

                        public void onFinish() {
                            collapse(hideSmsTimeLeft);
                            expand(btnResendOTP);
                            expand(btnResendOTP);
                            timeleftOtp.setVisibility(View.INVISIBLE);
                            resendMessage.setVisibility(View.INVISIBLE);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                btnResendOTP.setTextColor(getResources().getColor(R.color.colorPrimary));
                               // btnResendOTP.setBackground( getResources().getDrawable(R.drawable.pkg_select_btn));
                            }else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
                                btnResendOTP.setTextColor(getResources().getColor(R.color.colorPrimary));
                               // btnResendOTP.setBackgroundDrawable( getResources().getDrawable(R.drawable.pkg_select_btn) );
                            }
                           // btnResendOTP.setBackgroundColor(getResources().getColor(R.color.themeColor));
                            btnResendOTP.setTextColor(getResources().getColor(R.color.colorPrimary));
                            btnResendOTP.setPadding(getResources().getDimensionPixelOffset(R.dimen.margin_5),
                                    getResources().getDimensionPixelOffset(R.dimen.margin_5),
                                    getResources().getDimensionPixelOffset(R.dimen.margin_5),
                                    getResources().getDimensionPixelOffset(R.dimen.margin_5));
                            btnResendOTP.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("LoginActivity.btnResendOTP.onClick");
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
                            });

                        }
                    }.start();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }catch(NullPointerException e){
                CustomeDialog.showToast(LoginActivity.this, "" + e.toString());
            }catch (Exception e){
                CustomeDialog.showToast(LoginActivity.this, "" + e.toString());
            }
        }
    }

    ///////////////////////Keyboard/////////////////////
    private void hideToolbarOnKeyboardUp() {
        Rect r = new Rect();
        relativeLayoutContainer.getWindowVisibleDisplayFrame(r);
        int screenHeight = relativeLayoutContainer.getRootView().getHeight();

        int keypadHeight = screenHeight - r.bottom;

        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad hraHeight.
            ////collapse(imageViewLogin);
        }
        else {
            // keyboard is closed
           //expand(imageViewLogin);
        }
    }

    ///////////////////////////Progress Dialog Logic///////////////////
    private void hidePDialog()
    {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }

    private void showPDialog()
    {
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

    ////////////////////login request//////////////////////////
    private void loginVolley(JSONObject object) {
        {
            String URL = TagValues.LOGIN_URL ;
            showPDialog();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,URL,object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if(response!=null){
                                System.out.println("login===="+response.toString());
                                loginMethod(response);
                            }else{

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidePDialog();
                            String msg = "";
                            if (mProfileData.getMsg() != null) {
                                msg = "Mobile number/Password is incorrect";
                            } else {
                                msg = "Mobile Number/Password is incorrect";
                            }
                            CustomeDialog.showToast(LoginActivity.this, msg);
                        }
                    }){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    Map<String, String> headers = response.headers;
                    Set<String> keySet = headers.keySet();
					/*for (String s : keySet) {
						System.out.println("myvalues======="+s);
					}*/
                    System.out.println("LoginActivity.parseNetworkResponse login======"+headers.get("X-EKINCARE-KEY")+"===="+headers.get("X-CUSTOMER-KEY"));

                    prefs.setEkinKey(headers.get("X-EKINCARE-KEY"));
                    prefs.setCustomerKey(headers.get("X-CUSTOMER-KEY"));
                    System.out.println("LoginActivity.parseNetworkResponse=========");
                    String output = headers.get("ETag");
                    //etag = output.replaceAll("W/", "");
                    return super.parseNetworkResponse(response);

                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                    params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                    params.put("X-DEVICE-ID", customerManager.getDeviceID(LoginActivity.this));
                    params.put("Content-type", "application/json");
                    params.put("Accept", "application/json");

                    return params;
                }
            };
            VolleyRequestSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsObjRequest);
        }
    }

    private void loginWithOtpSet(JSONObject object)
    {
        System.out.println("LoginActivity.loginWithOtpSet");
        String URL = TagValues.LOGIN_URL_OTP ;
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,URL,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("login===="+response.toString());
                            loginMethod(response);
                        }else{

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        String msg = "";
                        if (mProfileData.getMsg() != null) {
                            msg = "Mobile number/Password is incorrect";
                        } else {
                            msg = "Mobile Number/Password is incorrect";
                        }
                        CustomeDialog.showToast(LoginActivity.this, msg);
                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                System.out.println("LoginActivity.parseNetworkResponse otp======"+headers.get("X-EKINCARE-KEY")+"===="+headers.get("X-CUSTOMER-KEY"));
                prefs.setEkinKey(headers.get("X-EKINCARE-KEY"));
                prefs.setCustomerKey(headers.get("X-CUSTOMER-KEY"));
                return super.parseNetworkResponse(response);

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(LoginActivity.this));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(this.getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private void loginMethod(JSONObject response)
    {
        mProfileData = new Gson().fromJson(response.toString(),ProfileData.class);
        System.out.println("LoginActivity.loginMethod======"+response.toString());

        if (mProfileData != null && mProfileData.getCustomer() != null) {
            try {
                prefs.setIsLogin(true);
                System.out.println("customerkey========="+prefs.getCustomerKey()+"===="+prefs.getEkinKey());

                System.out.println("LoginActivity.loginMethod===="+mProfileData.getCustomer().getWizard_status());
                if(mProfileData.getCustomer().getWizard_status().equals("6"))
                    prefs.setIsFIrstWizard(true);
                    mixpanel.timeEvent("Login_Login button success");
                try {
                    JSONObject props = new JSONObject();
                    props.put("LoginName", mProfileData.getCustomer().getFirst_name()+mProfileData.getCustomer().getLast_name());
                    props.put("LoginNumber", editTextUserName.getText().toString().trim());
                    mixpanel.track("Login_Login button success", props);
                } catch (JSONException e) {
                    Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                }

                prefs.setallNotification(true);

                prefs.setLoggedinUserName(editTextUserName.getText().toString().trim());
                prefs.setPassword(editTextPassword.getText().toString().trim());
                mProfileManager.setProfileData(mProfileData);

                //Phalu Change For Hydrocare

                if (mProfileData.getCustomer().getHydrocare_subscripted() == null) {
                    System.out.println("hyderocare======="+"yes null");
                    prefs.setisHydrocareSubscriptionEnable(true);
                }else {
                    if (mProfileData.getCustomer().getHydrocare_subscripted().equals("1")) {
                        System.out.println("hyderocare======="+"yes 1");
                        prefs.setisHydrocareSubscriptionEnable(true);
                    } else {
                        System.out.println("hyderocare======="+"yes no");
                        prefs.setisHydrocareSubscriptionEnable(false);
                    }
                }

                JSONObject object = new JSONObject();
                try {
                    object.put(":device_name", android.os.Build.MODEL);
                    object.put(":device_type", TagValues.deviceType);
                    System.out.println("-----registrationid - " + prefs.getRegistrationId());

                    object.put("token", prefs.getRegistrationId());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                this.customerManager = CustomerManager.getInstance(this.getApplicationContext());
                customerManager.isFromLogin = true;
                customerManager.setCurrentCustomer(mProfileData.getCustomer());
                customerManager.setLoggedInCustomer(true);

                System.out.println("LoginActivity.loginMethod======="+mProfileManager.getLoggedinCustomer()+"==="+prefs.getRegistrationId().equals("")+"===="+prefs.isSentTokenFor(mProfileManager.getLoggedinCustomer().getIdentification_token()));

                if (mProfileManager.getLoggedinCustomer() != null && !prefs.getRegistrationId().equals("") && !prefs.isSentTokenFor(mProfileManager.getLoggedinCustomer().getIdentification_token())) {
                    System.out.println("LoginActivity.loginMethod======"+"Push");
                    pushNotificationToken();
                }else {
                    System.out.println("LoginActivity.loginMethod======"+"else");
                    hidePDialog();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
                CustomeDialog.showToast(LoginActivity.this, "" + e.toString());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                CustomeDialog.showToast(LoginActivity.this, "" + e.toString());
                return;
            }

        }else{
            String msg = "";
            if (mProfileData.getMsg() != null) {
                msg = "Mobile number/Password is incorrect";
            } else {
                msg = "Mobile Number/Password is incorrect";
            }
            CustomeDialog.showToast(LoginActivity.this, msg);
        }

    }

    private void pushNotificationToken() {

        String regId = prefs.getRegistrationId();

        if (mProfileManager.getLoggedinCustomer() != null && !prefs.getRegistrationId().equals("") && !prefs.isSentTokenFor(mProfileManager.getLoggedinCustomer().getIdentification_token())) {


            if (NetworkCaller.isInternetOncheck(context)) {
                JSONObject object2 = new JSONObject();
                JSONObject object = new JSONObject();
                try {
                    object.put("device_name", android.os.Build.MODEL);
                    object.put("device_type", TagValues.deviceType);
                    System.out.println("-----registrationid - " + prefs.getRegistrationId());
                    object.put("token", prefs.getRegistrationId());
                    object2.put("push_token",object);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                String requestString = "{\"push_token\":" + object.toString() + "}";

                pushTokenRequest(object2);
            }


        }
    }

    private void pushTokenRequest(JSONObject object2) {
        String URL = TagValues.PUSH_TOCKENS_URL ;
        JsonObjectRequest jsObjRequesttwo = new JsonObjectRequest(Request.Method.POST,URL,object2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("login register otp===="+response.toString());
                            prefs.sentTokenFor(mProfileManager.getLoggedinCustomer().getIdentification_token());
                            hidePDialog();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            CustomeDialog.dispDialog(LoginActivity.this, TagValues.DATA_NOT_FOUND);
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
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequesttwo);

    }

    ///////////////////back press logic////////////////////////////
    @Override
    public void onBackPressed() {
        exitDialog();
    }

    private void exitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setTitle("Haven't signed in.");
        builder.setMessage("Are you sure you want to exit?");

        String positiveText = "Exit";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        String negativeText = "Dismiss";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        // display dialog
        dialog.show();
    }

    private BroadcastReceiver SmsListener = new BroadcastReceiver()
    {
        private String otpCode;

        @Override
        public void onReceive(Context context, Intent intent)
        {
            System.out.println("LoginActivity.onReceive isReadingSms="+isReadingSms);
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED") && !isReadingSms)
            {
                System.out.println("LoginActivity.onReceive inside if");
                Bundle bundle = intent.getExtras(); // ---get the SMS message
                // passed in---
                SmsMessage[] msgs = null;
                if (bundle != null) {
                    // ---retrieve the SMS message received---
                    System.out.println("LoginActivity.onReceive inside if"+bundle.toString());
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        msgs = new SmsMessage[pdus.length];
                        System.out.println("LoginActivity.onReceive msgs="+msgs.toString());
                        for (int i = 0; i < 1; i++)
                        {
                            msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                            String msgBody = msgs[i].getMessageBody();
                            System.out.println("LoginActivity.onReceive msgBody="+msgBody);
                            if (msgBody.contains("OTP is confidential. Sharing it with anyone gives them access to your medical data"))
                            {
                                System.out.println("LoginActivity.onReceive inside if containing OTP is confidential. Sharing it with anyone gives them access to your medical data");
                                String[] spitStr = msgBody.split("\\.");
                                otpCode = spitStr[0].replaceAll("[^0-9]", "");
                                Log.i("OTP CODE ", otpCode);
                                System.out.println("-------message received " + otpCode);

                                if (otpCode != null && !otpCode.isEmpty())
                                {
                                    otpValue.setText(otpCode);

                                    countDownTimer.cancel();
                                    collapse(hideSmsTimeLeft);
                                    collapse(btnResendOTP);
                                    VerifyOtpWebCall(otpCode.trim());
                                } else {
                                    Toast.makeText(LoginActivity.this,"Please enter verification code and try again.",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Exception caught="+e.getMessage());
                    }
                }
            }
        }
    };

    public void VerifyOtpWebCall(String otps)
    {
        System.out.println("LoginActivity.VerifyOtpWebCall");
        if (!editTextUserName.getText().toString().equalsIgnoreCase(""))
        {
            if (!otps.equalsIgnoreCase(""))
            {
                isReadingSms = true;

                JSONObject obj = new JSONObject();
                try {
                    JSONObject object = new JSONObject();
                    object.put("mobile_number", editTextUserName.getText().toString().trim());
                    object.put("otp", otps);
                    obj.put("online_customer", object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                System.out.println("LoginActivity.VerifyOtpWebCall obj="+obj.toString());

                if (NetworkCaller.isInternetOncheck(LoginActivity.this))
                {
                    mRegister = new Register();
                    loginWithOtpSet(obj);
                } else {
                    CustomeDialog.showToast(LoginActivity.this, "Internet not available");
                }
            } else {
                CustomeDialog.showToast(LoginActivity.this, "Please enter Code");
            }
        } else {
            CustomeDialog.showToast(LoginActivity.this, "Please enter PhoneNumber");
        }
    }

    private void checkOTPValidtyLogic()
    {
        String otp = otpValue.getText().toString();
        try {
            VerifyOtpWebCall(otp);
        }
        catch (Exception e)
        {
            String errorMessage = e.getMessage();
            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void resendOtpRequest()
    {
        System.out.println("LoginActivity.resendOtpRequest");
        try {
            if (!editTextUserName.getText().toString().equalsIgnoreCase("")) {
                if (NetworkCaller.isInternetOncheck(LoginActivity.this)) {
                    mRegister = new Register();
                    resendOtpVolley(editTextUserName.getText().toString());
                } else {
                    CustomeDialog.showToast(LoginActivity.this, "Internet not available");
                }

            } else {
                CustomeDialog.showToast(LoginActivity.this, "Please enter PhoneNumber");
            }

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void resendOtpVolley(String mUserName)
    {
        System.out.println("LoginActivity.resendOtpVolley");
        String URL = TagValues.RESEND_OTP_URL + mUserName ;
        System.out.println("login verify===="+URL);

        showPDialog();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null)
                        {
                            Toast.makeText(LoginActivity.this, "OTP sent", Toast.LENGTH_SHORT).show();
                            System.out.println("resend otp response verify===="+response.toString());
                            hidePDialog();
                            resendOtpResposne(response);
                        }
                        else
                        {
                            CustomeDialog.showToast(LoginActivity.this, TagValues.DATA_NOT_FOUND);
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
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(LoginActivity.this));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    private void resendOtpResposne(JSONObject response)
    {
        mRegister = new Gson().fromJson(response.toString(), Register.class);
        if (mRegister != null && mRegister.getMessage() != null
                && mRegister.getMessage().contains("success")) {
            try {


            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("error one===="+"one");
                CustomeDialog.showToast(LoginActivity.this, "" + e.toString());
                return;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("error two===="+"two");
                CustomeDialog.showToast(LoginActivity.this, "" + e.toString());
                return;
            }
        } else {
            System.out.println("error three===="+"three");
            String msg = "";
            if (mRegister.getMessage() != null && !mRegister.getMessage().equalsIgnoreCase("")) {
                msg = mRegister.getMessage().toString();
            } else {
                msg = "code sending error";
            }
            CustomeDialog.showToast(LoginActivity.this, msg);
        }
    }


}

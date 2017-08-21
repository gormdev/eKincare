package com.ekincare.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.ekincare.app.ProfileManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.util.CircularSeekBar;
import com.ekincare.util.DateUtility;
import com.ekincare.util.FloatingButtonAccess;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.LoginActivity;
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.FamilyAddresses;
import com.oneclick.ekincare.vo.ProfileData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.android.gms.analytics.internal.zzy.e;

/**
 * Created by RaviTejaN on 24-11-2016.
 */

@SuppressLint("ValidFragment")
public class FragmentProfileChildTab extends Fragment {

    // private double waterTarget;

    private View view;
    private CardView contactCardView,infoCardLayout,bioCardLayout;

    private Dialog mAlert_Dialog;

    private PreferenceHelper prefs;
    private String strFamilyMemberKey = "";
    private ProfileManager mProfileManager;
    private Customer mCustomer;

    private ProfileData mProfileData;
    private String TAG = "FamilyProfile";
    MixpanelAPI mixpanel;
    MixpanelAPI.People people;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;
    TextView bioName,bioDob,bioHeight,bioWeight,bioGender,bioBloodGroup;
    TextView infoActivity,infoExcrcis,infoAlcohol,infoSmoking,infoDiet;
    TextView contactPhone,contactEmail,contactAddress,contactCity,contactState,contactCountry,contactPincode;
    Dialog alertDocShare;
    public AppCompatEditText doctor_name;
    public AppCompatEditText doctor_mobile;
    public AppCompatEditText doctor_email;
    TextView expertopFAQ;
    TextView expertopSubmitButton;
    LinearLayout linearLayoutProgress,linearLayoutMain;

    boolean isResponseSuccess = false;
    private String stringHeight = "";
    private List<FamilyAddresses> FamilyAddressList;
    private String stringGender = "";
    private String stringName = "";
    private String stringDoB="";
    private String stringDailyActivity="";
    private String stringDailyDiet="";
    private String stringDailyAlcohol="";
    private String stringDailySmoke="";
    private String stringDailyExercise="";
    private String stringDailyMobile="";
    private String stringEmail="";
    private String stringPincode="";
    private String stringAddress="";
    private String stringCity="";
    private String stringState="";
    private String stringCountry="";
    private String stringWeight="";
    private String strBg="";
    NestedScrollView nestedScrollView;
    FloatingButtonAccess mFloatingButtonAccess;
    private Activity mActivity;

    public FragmentProfileChildTab() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        System.out.println("FragmentProfileChildTab.onAttach mActivity="+mActivity);
    }

    @SuppressLint("ValidFragment")
    public FragmentProfileChildTab(FloatingButtonAccess mFloatingButtonAccess ) {
        this.mFloatingButtonAccess=mFloatingButtonAccess;
        System.out.println("FragmentProfileChildTab.FragmentProfileChildTab");
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        customerManager = CustomerManager.getInstance(mActivity.getApplicationContext());
        mCustomer = customerManager.getCurrentFamilyMember();
        prefs = new PreferenceHelper(mActivity);
        mProfileData = new ProfileData();
        mProfileManager = ProfileManager.getInstance(mActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_family_member_profile_sample, container, false);

            mixpanel = MixpanelAPI.getInstance(mActivity, TagValues.MIXPANEL_TOKEN);
            people = mixpanel.getPeople();
            people.identify(prefs.getUserName());

            initChildview();

           // mFloatingButtonAccess.getProfileFab();

            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY > oldScrollY) {
                        System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll DOWN");
                        mFloatingButtonAccess.getProfileFab().setVisibility(View.GONE);
                    }
                    if (scrollY < oldScrollY) {
                        System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll UP");
                        mFloatingButtonAccess.getProfileFab().setVisibility(View.VISIBLE);
                    }

                    if (scrollY == 0) {
                        System.out.println("FragmentDocumentChildTab.onScrollChange========="+"TOP SCROLL");
                        mFloatingButtonAccess.getProfileFab().setVisibility(View.VISIBLE);
                    }

                    if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                        System.out.println("FragmentDocumentChildTab.onScrollChange========="+"BOTTOM SCROLL");
                        mFloatingButtonAccess.getProfileFab().setVisibility(View.GONE);
                    }
                }
            });

            System.out.println("FragmentFamilyMemberProfileSample.getHeaders keys =" + prefs.getCustomerKey() + " ekin="+ prefs.getEkinKey());

            System.out.println("FragmentFamilyMemberProfileSample.onCreateView" + customerManager.getDeviceID(mActivity));

            Thread thread = new Thread(new FragmentProfileChildTab.GetProfileData());
            thread.start();

            try{
                System.out.println("FragmentProfileChildTab.onCreateView======"+customerManager.getCurrentFamilyMember().getAge());

            }catch (Exception e){
                e.printStackTrace();
            }



            setHasOptionsMenu(false);
        }

        return view;

    }

    private void initChildview() {
        initFindview();
        initBioView();
        initInfoView();
        initContactView();
    }

    private void initFindview() {
        nestedScrollView=(NestedScrollView)view.findViewById(R.id.child_tab_profile_nestedScroll);
        bioCardLayout=(CardView)view.findViewById(R.id.bio_card_view);
        infoCardLayout=(CardView)view.findViewById(R.id.info_card_view) ;
        contactCardView=(CardView)view.findViewById(R.id.contact_card_view) ;
        linearLayoutProgress= (LinearLayout) view.findViewById(R.id.progress_layout) ;
        linearLayoutMain = (LinearLayout) view.findViewById(R.id.main_layout) ;
        linearLayoutMain.setVisibility(View.GONE);
        linearLayoutProgress.setVisibility(View.VISIBLE);

    }

    private void initInfoView(){
        View infoView = View.inflate(mActivity, R.layout.profile_info, null);
        infoActivity = (TextView) infoView.findViewById(R.id.profile_info_activity);
        infoExcrcis = (TextView) infoView.findViewById(R.id.profile_info_exercise);
        infoAlcohol = (TextView) infoView.findViewById(R.id.profile_info_alcohol);
        infoSmoking = (TextView) infoView.findViewById(R.id.profile_info_smoking);
        infoDiet = (TextView) infoView.findViewById(R.id.profile_info_dite);
        infoCardLayout.addView(infoView);
    }

    private void initContactView(){
        View contactView = View.inflate(mActivity, R.layout.profile_contact_info, null);
        contactPhone = (TextView) contactView.findViewById(R.id.profile_contact_phone);
        contactEmail = (TextView) contactView.findViewById(R.id.profile_contact_emial);
        contactAddress = (TextView) contactView.findViewById(R.id.profile_contact_address);
        contactCity = (TextView) contactView.findViewById(R.id.profile_contact_city);
        contactState = (TextView) contactView.findViewById(R.id.profile_contact_state);
        contactCountry = (TextView) contactView.findViewById(R.id.profile_contact_country);
        contactPincode = (TextView) contactView.findViewById(R.id.profile_contact_pincode);
        contactCardView.addView(contactView);
    }

    private void initBioView(){
        View bioView = View.inflate(mActivity, R.layout.profile_bio, null);
        bioName = (TextView) bioView.findViewById(R.id.profile_bio_name);
        bioGender = (TextView) bioView.findViewById(R.id.profile_bio_gender);
        bioDob = (TextView) bioView.findViewById(R.id.profile_bio_dob);
        bioBloodGroup = (TextView) bioView.findViewById(R.id.profile_bio_bloodgroup);
        bioHeight = (TextView) bioView.findViewById(R.id.profile_bio_height);
        bioWeight = (TextView) bioView.findViewById(R.id.profile_bio_weight);
        bioCardLayout.addView(bioView);
    }

    private void showDialog(String errorMessage, String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);

        builder.setTitle(title);
        builder.setMessage(errorMessage);

        String positiveText = "Close";
        builder.setPositiveButton(positiveText,
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

    @Override
    public void onResume() {
        super.onResume();
        bindData();
    }

    private void getCustomerDetailRequest()
    {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,TagValues.GET_CUSTOMER_DETAIL_URL,null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null)
                        {
                            isResponseSuccess= true;
                            Log.d(TAG,"login verify===="+response.toString());
                            getCustomerDetailResponse(response);
                        }else{
                            isResponseSuccess = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.d(TAG,"FragmentFamilyMemberProfileSample.onErrorResponse");
                        isResponseSuccess = false;
                        try{
                            Toast.makeText(mActivity.getApplicationContext(),"Something went wrong.Try again later.",Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                           e.printStackTrace();
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

                if (customerManager.isLoggedInCustomer()) {
                    strFamilyMemberKey = "";
                } else {
                    strFamilyMemberKey = customerManager.getCurrentFamilyMember().getIdentification_token();
                }

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(mActivity));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");


                return params;
            }
        };
        VolleyRequestSingleton.getInstance(mActivity.getApplicationContext()).addToRequestQueue(jsObjRequest);

    }


    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }

    private void showPDialog() {
        mAlert_Dialog = new Dialog(mActivity);
        mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlert_Dialog.setContentView(R.layout.materialprogressbar);
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //mAlert_Dialog.setCancelable(false);
        mAlert_Dialog.setCanceledOnTouchOutside(true);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

    private void getCustomerDetailResponse(JSONObject response)
    {
        mProfileData = new Gson().fromJson(response.toString(), ProfileData.class);
        Log.d(TAG,"FragmentFamilyMemberProfileSample.getCustomerDetailResponse response="+response);
        if (mProfileData.getCustomer() != null && mProfileData.getCustomer().getBlood_sugar() != null) {
            try {
                Gson mGson = new Gson();

                ProfileData mProfileDataNew = mProfileManager.getProfileData();
                List<Customer> familyMembers = new ArrayList<Customer>();

                if (mProfileDataNew != null && mProfileDataNew.getFamily_members() != null)
                {
                    familyMembers = mProfileDataNew.getFamily_members().getMember_list();
                }

                if (customerManager.isLoggedInCustomer() || mProfileDataNew == null)
                {
                    Log.d(TAG,"check1========="+"isLoggedIn");
                    mProfileManager.setProfileData(mProfileData);
                    customerManager.setCurrentFamilyMember(mProfileData.getCustomer());
                } else
                {
                    Log.d(TAG,"check1=========else"+"isLoggedIn");
                    for (int i = 0; i < familyMembers.size(); i++)
                    {
                        Customer temp = familyMembers.get(i);
                        if (temp.getIdentification_token().equalsIgnoreCase(mProfileData.getCustomer().getIdentification_token()))
                        {
                            familyMembers.set(i, mProfileData.getCustomer());
                            customerManager.setCurrentFamilyMember(mProfileData.getCustomer());
                            Log.d(TAG, "found");
                        }
                    }
                    mProfileManager.setProfileData(mProfileDataNew);
                }

                mCustomer = customerManager.getCurrentFamilyMember();
                bindData();
            } catch (NullPointerException e) {
                e.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }

        } else {
            String msg = "";
            if (mProfileData.getMsg() != null) {
                msg = mProfileData.getMsg().toString();
                if (msg.contains("401")) {
                    Intent i=new Intent(mActivity,LoginActivity.class);
                    startActivity(i);
                }
            }
        }
    }

    private void bindData()
    {
        System.out.println("FragmentProfileChildTab.bindData");
        if (mCustomer != null)
        {
            try{
                if(customerManager.getCurrentFamilyMember()!=null)
                {
                    try{


                        if(Integer.parseInt(customerManager.getCurrentFamilyMember().getAge())<18)
                        {
                            bioCardLayout.setVisibility(View.VISIBLE);
                            infoCardLayout.setVisibility(View.GONE);
                            contactCardView.setVisibility(View.GONE);
                        }else{
                            bioCardLayout.setVisibility(View.VISIBLE);
                            infoCardLayout.setVisibility(View.VISIBLE);
                            contactCardView.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                       e.printStackTrace();
                    }

                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

            System.out.println("FragmentProfileChildTab.bindData data is not null");
            FamilyAddressList = customerManager.getCurrentFamilyMember().getAddresses();
            stringGender = mCustomer.getGender();
            stringName = mCustomer.getFirst_name()+ " " + mCustomer.getLast_name();
            stringDoB = DateUtility.getConvertDob(mCustomer.getDate_of_birth());
            if(mCustomer.getDaily_activity()==null){
                stringDailyActivity = "-";
            }else{
                stringDailyActivity = (mCustomer.getDaily_activity());
            }
            if(mCustomer.getDiet()==null){
                stringDailyDiet = ("-");
            }else{
                stringDailyDiet = (mCustomer.getDiet());
            }
            if(mCustomer.getAlcohol()==null){
                stringDailyAlcohol = ("-");
            }else{
                stringDailyAlcohol = (mCustomer.getAlcohol());
            }
            if(mCustomer.getSmoke()==null){
                stringDailySmoke = ("-");
            }else{
                stringDailySmoke = (mCustomer.getSmoke());
            }
            if(mCustomer.getFrequency_of_exercise()==null){
                stringDailyExercise = ("-");
            }else{
                stringDailyExercise = (mCustomer.getFrequency_of_exercise());
            }
            if(mCustomer.getMobile_number()==null|| mCustomer.getMobile_number().equalsIgnoreCase(" ")||mCustomer.getMobile_number().isEmpty()){
                stringDailyMobile = ("-");
            }else{
                stringDailyMobile =(mCustomer.getMobile_number());
            }
            if(mCustomer.getEmail()==null || mCustomer.getEmail().equalsIgnoreCase(" ")||mCustomer.getEmail().isEmpty()){
                stringEmail =("-");
            }else{
                stringEmail =(mCustomer.getEmail());
            }
            if(FamilyAddressList!=null){
                if(FamilyAddressList.isEmpty()){
                    stringAddress =("-");
                    stringCity =("-");
                    stringState =("-");
                    stringCountry =("-");
                    stringPincode =("-");
                }else if(FamilyAddressList.size()==1){
                    stringAddress =(FamilyAddressList.get(0).getLine1()+","+FamilyAddressList.get(0).getLine2());
                    stringCity =(FamilyAddressList.get(0).getCity());
                    stringState =(FamilyAddressList.get(0).getState());
                    stringCountry =(FamilyAddressList.get(0).getCountry());
                    stringPincode =(FamilyAddressList.get(0).getZip_code());
                }else if(FamilyAddressList.size()>1){
                    stringAddress =(FamilyAddressList.get(FamilyAddressList.size() - 1).getLine1()+","+FamilyAddressList.get(FamilyAddressList.size() - 1).getLine2());
                    stringCity =(FamilyAddressList.get(FamilyAddressList.size() - 1).getCity());
                    stringState =(FamilyAddressList.get(FamilyAddressList.size() - 1).getState());
                    stringCountry =(FamilyAddressList.get(FamilyAddressList.size() - 1).getCountry());
                    stringPincode =(FamilyAddressList.get(FamilyAddressList.size() - 1).getZip_code());
                }
            }else{

            }


            if (mCustomer != null && mCustomer.getCustomer_vitals() != null)
            {
                if (mCustomer.getCustomer_vitals().getFeet() != null
                        && !mCustomer.getCustomer_vitals().getFeet().equalsIgnoreCase("")
                        && mCustomer.getCustomer_vitals().getInches() != null
                        && !mCustomer.getCustomer_vitals().getInches().equalsIgnoreCase("")) {
                    try{
                        stringHeight = (mCustomer.getCustomer_vitals().getFeet()+" Feet "+mCustomer.getCustomer_vitals().getInches()+" Inch");
                    }catch(NumberFormatException e){
                        e.printStackTrace();
                    }

                }
                if (mCustomer.getCustomer_vitals().getWeight() != null  && !mCustomer.getCustomer_vitals().getWeight().equalsIgnoreCase(""))
                {
                    try{
                        stringWeight=(mCustomer.getCustomer_vitals().getWeight()+" kgs");
                    }catch(NumberFormatException e){
                        e.printStackTrace();
                    }
                }
                if ((mCustomer.getCustomer_vitals() != null )) {
                    int bgId = 0;
                    try {
                        bgId = Integer.parseInt(mCustomer.getCustomer_vitals().getBlood_group_id());
                    } catch (NumberFormatException e) {
                    }
                    if (bgId == 1) {
                        strBg = "A +";
                    } else if (bgId == 2) {
                        strBg = "A -";
                    } else if (bgId == 3) {
                        strBg = "B +";
                    } else if (bgId == 4) {
                        strBg = "B -";
                    } else if (bgId == 5) {
                        strBg = "AB +";
                    } else if (bgId == 6) {
                        strBg = "AB -";
                    } else if (bgId == 7) {
                        strBg = "O +";
                    } else if (bgId == 8) {
                        strBg = "O -";
                    }else{
                        strBg = "-";
                    }
                }
            } else {
            }

        }

        try{
            System.out.println("FragmentProfileChildTab.bindData mActivity="+mActivity);
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    setUiData();
                }
            });
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }


    }

    private void setUiData(){
        System.out.println("FragmentProfileChildTab.setUiData");

        contactAddress.setText(stringAddress);
        contactCity.setText(stringCity);
        contactState.setText(stringState);
        contactCountry.setText(stringCountry);
        contactPincode.setText(stringPincode);
        contactEmail.setText(stringEmail);
        contactPhone.setText(stringDailyMobile);

        infoExcrcis.setText(stringDailyExercise);
        infoAlcohol.setText(stringDailyAlcohol);
        infoDiet.setText(stringDailyDiet);
        infoSmoking.setText(stringDailySmoke);
        infoActivity.setText(stringDailyActivity);

        bioDob.setText(stringDoB);
        bioName.setText(stringName);
        bioGender.setText(stringGender);
        bioWeight.setText(stringWeight);
        bioHeight.setText(stringHeight);
        bioBloodGroup.setText(strBg);

        linearLayoutMain.setVisibility(View.VISIBLE);
        linearLayoutProgress.setVisibility(View.GONE);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        MenuItem item = menu.findItem(R.id.menu_refresh);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_refresh:
                        linearLayoutMain.setVisibility(View.GONE);
                        linearLayoutProgress.setVisibility(View.VISIBLE);
                        if (NetworkCaller.isInternetOncheck(mActivity)) {
                            mProfileData = new ProfileData();
                            Thread thread = new Thread(new FragmentProfileChildTab.GetProfileData());
                            thread.start();

                        } else {
                            Toast.makeText(mActivity,"No internet.",Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                return false;
            }
        });
        MenuItem itemShare = menu.findItem(R.id.menu_share_doc);
        itemShare.setVisible(true);
        itemShare.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_share_doc:
                        if (NetworkCaller.isInternetOncheck(mActivity)) {
                            shareEdoc();
                        }else{
                            Toast.makeText(mActivity,getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void shareEdoc() {
        System.out.println("checkopen======="+"yes");
        alertDocShare = new Dialog(mActivity);
        alertDocShare.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDocShare.setContentView(R.layout.dialog_edoc_share);
        Window window = alertDocShare.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        alertDocShare.setCancelable(false);
        alertDocShare.setCanceledOnTouchOutside(true);
        expertopFAQ = (TextView)alertDocShare. findViewById(R.id.expertopFAQ);
        doctor_name = (AppCompatEditText)alertDocShare.findViewById(R.id.expertopDoctorName);
        doctor_mobile = (AppCompatEditText)alertDocShare.findViewById(R.id.expertopDoctorMobile);
        doctor_email = (AppCompatEditText)alertDocShare.findViewById(R.id.expertopDoctorEmail);
        expertopSubmitButton = (TextView)alertDocShare.findViewById(R.id.expertopSubmitButton);

        final TextInputLayout textInputLayoutName = (TextInputLayout)alertDocShare.findViewById(R.id.floating_doctor_name);
        final TextInputLayout textInputLayoutMobile = (TextInputLayout)alertDocShare.findViewById(R.id.floating_mobile);
        final TextInputLayout textInputLayoutEmail = (TextInputLayout)alertDocShare.findViewById(R.id.floating_email);

        doctor_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayoutName.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        doctor_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayoutEmail.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        doctor_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayoutMobile.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TextView textViewDismiss = (TextView)alertDocShare.findViewById(R.id.cancel);
        textViewDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDocShare.dismiss();
            }
        });

        expertopFAQ.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://www.ekincare.com/about/faq"));
                startActivity(i);
                alertDocShare.dismiss();
            }
        });

        expertopSubmitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = doctor_name.getText().toString().trim();
                String mobile = doctor_mobile.getText().toString().trim();
                String email = doctor_email.getText().toString().trim();

                if (checkIsEmpty(name) && checkIsEmpty(mobile) && checkIsEmpty(email)) {
                    //valadiationShareDocDialog("Please enter all the required fields", true);
                    textInputLayoutName.setError("Empty Name");
                    textInputLayoutMobile.setError("Empty Mobile Number");
                    textInputLayoutEmail.setError("Empty Email");
                    return;
                }

                if(checkIsEmpty(name)){
                    //valadiationShareDocDialog("Please Enter Name", true);
                    textInputLayoutName.setError("Empty Name");
                    return;

                }
                if(checkIsEmpty(mobile)){
                    //valadiationShareDocDialog( "Please Enter ContactNo", true);
                    textInputLayoutMobile.setError("Empty Mobile Number");
                    return;

                }
                if(checkIsEmpty(email)){
                    textInputLayoutEmail.setError("Empty Email");
                    //valadiationShareDocDialog( "Please Enter Email", true);
                    return;
                }

                if (!checkIsMobileValid(mobile)) {
                    //valadiationShareDocDialog( "Please Enter valid mobile", true);
                    textInputLayoutMobile.setError("Invalid Mobile Number");
                    return;
                }

                if (!checkIsEmailValid(email)) {
                    //valadiationShareDocDialog( "Please Enter valid Email", true);
                    textInputLayoutEmail.setError("Invalid Email");
                    return;
                }

                JSONObject obj = new JSONObject();
                try {
                    JSONObject json = new JSONObject();
                    json.put("doctor_name", name);
                    json.put("doctor_mobile_number", mobile);
                    json.put("doctor_email", email);
                    obj.put("doctor_opinion", json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                shareDocRequest(obj);
                Log.d("ConnetData", obj.toString());
            }
        });


        alertDocShare.show();
    }



    private void shareDocRequest(JSONObject json) {
        System.out.println("docshare====="+json.toString());
        String URL = TagValues.SEND_TO_DOCTOR ;
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,URL,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("message====="+response.toString());
                            hidePDialog();
                            alertDocShare.dismiss();
                        }else{

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("checkresponse======"+error.toString());
                        hidePDialog();
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
                params.put("X-DEVICE-ID", customerManager.getDeviceID(mActivity));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(mActivity.getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    public static boolean checkIsEmpty(String value) {

        if (TextUtils.isEmpty(value)) {
            return true;
        }
        return false;
    }

    public static boolean checkIsMobileValid(String value) {

        if (value.length() == 10) {
            return true;
        }
        return false;
    }

    public static boolean checkIsEmailValid(String value) {

        if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            return true;
        }
        return false;
    }

    public void refreshProfileData() {
        System.out.println("FragmentProfileChildTab.refreshProfileData");
        Thread thread = new Thread(new GetProfileData());
        thread.start();
    }



    public  class GetProfileData implements Runnable{
        @Override
        public void run() {
            try{
                if (NetworkCaller.isInternetOncheck(mActivity.getApplicationContext())) {
                    mProfileData = new ProfileData();
                    getCustomerDetailRequest();
                } else {
                    bindData();
                }
            }catch (Exception e){
                bindData();
            }

        }
    }


}

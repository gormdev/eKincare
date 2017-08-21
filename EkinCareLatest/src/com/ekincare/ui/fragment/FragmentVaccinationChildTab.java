package com.ekincare.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DataStorage.DatabaseOverAllHandler;
import com.DataStorage.DbResponse;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ekincare.R;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.util.PaddingItemDecoration;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.Immunization;
import com.oneclick.ekincare.vo.Register;

import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by RaviTejaN on 24-11-2016.
 */

public class FragmentVaccinationChildTab extends Fragment {

    private View view;

    private Immunization mImmunization;
    private Register mRegister;

    private PreferenceHelper prefs;

    private RecyclerView recyclerView ;
    private FragmentVaccinationChildTab.ItemAdapter itemAdapter;

    private TextView mTextViewThisMonth;
    private TextView mTextViewThisYear;
    private TextView mTextViewMoreYear;

    private TextView mTextViewImmuDate;

    private String strDate = "";

    private Customer mCustomer;

    private String strFamilyMemberKey = "";
    private Dialog Alert_Dialog;
    MixpanelAPI mixpanel;

    Dialog mAlert_Dialog;
    DatabaseOverAllHandler dbHandler;
    String url;
    private DbResponse data;
    String jsonResponse;
    String etag;
    String responseCode;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;
    Dialog alertDocShare;
    public AppCompatEditText doctor_name;
    public AppCompatEditText doctor_mobile;
    public AppCompatEditText doctor_email;
    TextView expertopFAQ;
    TextView expertopSubmitButton;



    public FragmentVaccinationChildTab() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        mCustomer = customerManager.getCurrentFamilyMember();
        prefs = new PreferenceHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_family_member_vaccination, container, false);

        dbHandler = new DatabaseOverAllHandler(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_vaccine);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration dividerItemDecoration = new PaddingItemDecoration(getActivity());
        recyclerView.addItemDecoration(dividerItemDecoration);


        mixpanel = MixpanelAPI.getInstance(getActivity(), TagValues.MIXPANEL_TOKEN);
        if (mCustomer != null && mCustomer.getId() != null
                && !mCustomer.getId().equalsIgnoreCase("")) {

            mixpanel.timeEvent("VaccinationsPage");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                props.put("LoginNumber", prefs.getUserName());
                mixpanel.track("VaccinationsPage", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }
        } else {

        }

        Thread thread = new Thread(new FragmentVaccinationChildTab.GetVaccinationDataThread());
        thread.start();
        setHasOptionsMenu(false);

        return view;

    }


    public void getImmunizationDetails()
    {
        if(mCustomer!=null){
            data = dbHandler.getImmunization(customerManager.getCurrentFamilyMember().getIdentification_token());
            if(data != null && data.getResponse()!=null){
                showDataDbImmunizationDetails(data.getResponse());
                backgoundgthreadStoreData(false);
            }else
            {
                if (NetworkCaller.isInternetOncheck(getActivity())) {
                    backgoundgthreadStoreData(true);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }


    }

    private void backgoundgthreadStoreData(final boolean flag) {
        url=TagValues.GET_IMMUNIZATION_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                if (result != null) {
                    if(responseCode.equals("200 OK")){
                        if(flag){
                            showImmunizationsData(result);
                        }
                        jsonResponse = result.toString();
                        Thread thread = new Thread(new FragmentVaccinationChildTab.MyRunnable());
                        thread.start();
                    }else if(responseCode.equals("304 Not Modified")){

                    }
                }else{
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

            }
        })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
                Map<String,String> headers = networkResponse.headers;
                Set<String> keySet = headers.keySet();
                responseCode=headers.get("Status");
                String output = headers.get("ETag");
                etag = output.replaceAll("W/", "");
                return super.parseNetworkResponse(networkResponse);

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
                params.put("X-DEVICE-ID", customerManager.getDeviceID(getActivity()));
                if (data != null && data.getEtag() != null)
                    params.put("If-None-Match",data.getEtag());

                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");

                return params;


            }
        };
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    private void showDataDbImmunizationDetails(String response)
    {
        mImmunization = new Gson().fromJson(response.toString(), Immunization.class);
        System.out.println("FragmentVaccinationChildTab.showDataDbImmunizationDetails mImmunization="+mImmunization);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayView();
            }
        });

    }

    private void showImmunizationsData(JSONObject result)
    {
        mImmunization = new Gson().fromJson(result.toString(), Immunization.class);
        System.out.println("FragmentVaccinationChildTab.showImmunizationsData mImmunization="+mImmunization);
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    displayView();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void displayView()
    {
        if (mImmunization != null && mImmunization.getmessage() != null) {
            try {
                itemAdapter = new FragmentVaccinationChildTab.ItemAdapter(mImmunization.getmessage());
                recyclerView.setAdapter(itemAdapter);
            } catch (NullPointerException e) {
                e.printStackTrace();
                showDialog("Something went wrong!",""+ e.toString());
            } catch (Exception e) {
                e.printStackTrace();
                showDialog("Something went wrong!","" + e.toString());
            }
        }else
        {
            Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();
            /*mRegister = new Gson().fromJson(result.toString(), Register.class);
            if (mRegister != null && mRegister.getMessage() != null) {
                if (Alert_Dialog != null && Alert_Dialog.isShowing())
                    Alert_Dialog.cancel();
                getImmunizationDetails();
            } else {

            }*/
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        MenuItem item = menu.findItem(R.id.menu_refresh);
        item.setVisible(false);

        MenuItem itemShare = menu.findItem(R.id.menu_share_doc);
        itemShare.setVisible(true);
        itemShare.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_share_doc:
                        if (NetworkCaller.isInternetOncheck(getActivity())) {
                            shareEdoc();
                        }else{
                            Toast.makeText(getActivity(),getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
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
        alertDocShare = new Dialog(getActivity());
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
                params.put("X-DEVICE-ID", customerManager.getDeviceID(getActivity()));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);

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

    public void refreshTab() {
        Thread thread = new Thread(new FragmentVaccinationChildTab.GetVaccinationDataThread());
        thread.start();
    }


    public class ItemAdapter extends RecyclerView.Adapter<FragmentVaccinationChildTab.ItemAdapter.ViewHolder> {

        private List<Immunization.ImmunizationData> immunizationlist;

        public static final int POSITION = R.id.position;
        public static final int OUTOFTOTAL = R.id.outoftotal;


        public ItemAdapter(List<Immunization.ImmunizationData> mImmunizationDatas) {
            Collections.sort(mImmunizationDatas, new FragmentVaccinationChildTab.ImmunizationComparator());
            immunizationlist = mImmunizationDatas;
        }

        @Override
        public FragmentVaccinationChildTab.ItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new FragmentVaccinationChildTab.ItemAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_items_immunizations, parent, false));
        }

        @Override
        public void onBindViewHolder(FragmentVaccinationChildTab.ItemAdapter.ViewHolder holder, int position) {
            holder.mTextName.setText(immunizationlist.get(position).getName());

            String lastDoseValue = "";
            int currentDoses = 0;
            int TotalDoses = 0;
            if (immunizationlist.get(position).getDoses() != null
                    && !immunizationlist.get(position).getDoses().equalsIgnoreCase("")) {
                TotalDoses = Integer.parseInt(immunizationlist.get(position).getDoses().substring(0, 1));
            }
            if (immunizationlist.get(position).getCurrent_doses() != null
                    && !immunizationlist.get(position).getCurrent_doses().equalsIgnoreCase("")) {
                currentDoses = Integer.parseInt(immunizationlist.get(position).getCurrent_doses().substring(0, 1));
            }

            Immunization.scheduleData newScheduleData = immunizationlist.get(position).getSchedule();
            if (newScheduleData != null) {
                if (newScheduleData.getOne() != null && !newScheduleData.getOne().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getOne().toString();
                }
                if (newScheduleData.getTwo() != null && !newScheduleData.getTwo().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getTwo().toString();
                }
                if (newScheduleData.getThree() != null && !newScheduleData.getThree().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getThree().toString();
                }
                if (newScheduleData.getFour() != null && !newScheduleData.getFour().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getFour().toString();
                }
                if (newScheduleData.getFive() != null && !newScheduleData.getFive().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getFive().toString();
                }
                if (newScheduleData.getSix() != null && !newScheduleData.getSix().equalsIgnoreCase("")) {
                    lastDoseValue = newScheduleData.getSix().toString();
                }
            }

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            long BirthTimeInMillisecond = 0;
            Date birthDate = null;
            try {
                if (mCustomer != null && mCustomer.getDate_of_birth() != null
                        && !mCustomer.getDate_of_birth().equalsIgnoreCase(""))
                    birthDate = format.parse(mCustomer.getDate_of_birth());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar birthCalendar = Calendar.getInstance();
            if (birthDate != null) {
                birthCalendar.setTime(birthDate);
            }
            BirthTimeInMillisecond = birthCalendar.getTimeInMillis();

            int minageTime = 0;
            long minageMilisecond = 0;
            long finalMilisecond = 0;
            String dueDateText = "";
            if (currentDoses == 0) {
                if (immunizationlist.get(position).getMinage() != null) {
                    minageTime = Integer.parseInt(immunizationlist.get(position).getMinage());
                    minageMilisecond = TimeUnit.DAYS.toMillis(minageTime * 7);

                    finalMilisecond = BirthTimeInMillisecond + minageMilisecond;
                }
            } else {
                if (immunizationlist.get(position).getCreated_at() != null) {
                    Date createdDate = null;
                    try {
                        createdDate = format.parse(immunizationlist.get(position).getCreated_at());
                        // System.out.println("-----created date "+createdDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Calendar creatCalendar = Calendar.getInstance();
                    if (createdDate != null) {
                        creatCalendar.setTime(createdDate);
                    }
                    minageMilisecond = creatCalendar.getTimeInMillis();

                    long dueTimeMillisecond = 0;
                    int DueMonth = 0;
                    if (lastDoseValue != null && !lastDoseValue.equalsIgnoreCase("")) {
                        try {
                            DueMonth = Integer.parseInt(lastDoseValue);
                        } catch (NumberFormatException e) {
                        }
                        if (DueMonth > 0) {
                            dueTimeMillisecond = TimeUnit.DAYS.toMillis(DueMonth * 7);
                        }
                    }

                    finalMilisecond = dueTimeMillisecond + minageMilisecond;
                }
            }

            // format.setTimeZone(TimeZone.getTimeZone("IST"));
            // strFirstDate = format.format(currentDate);
            // long FinalTimeInmilli = BirthTimeInMillisecond +
            // timeInMillisecond;
            long currentDate = System.currentTimeMillis();
            dueDateText = getDifferentTime(finalMilisecond, currentDate);

            holder.mImageUpdate1.setVisibility(View.INVISIBLE);
            holder.mImageUpdate2.setVisibility(View.INVISIBLE);
            holder.mImageUpdate3.setVisibility(View.INVISIBLE);
            holder.mImageUpdate4.setVisibility(View.INVISIBLE);
            holder.mImageUpdate5.setVisibility(View.INVISIBLE);
            holder.mImageUpdate6.setVisibility(View.INVISIBLE);

            holder.mImageUpdate1Lable.setVisibility(View.INVISIBLE);
            holder.mImageUpdate2Lable.setVisibility(View.INVISIBLE);
            holder.mImageUpdate3Lable.setVisibility(View.INVISIBLE);
            holder.mImageUpdate4Lable.setVisibility(View.INVISIBLE);
            holder.mImageUpdate5Lable.setVisibility(View.INVISIBLE);

            holder.mTextOutof.setText(currentDoses + "/" + TotalDoses);

            if (currentDoses == TotalDoses) {
                // holder.mTextTime.setVisibility(View.GONE);
                holder.mTextTime.setText("All doses are complete");
            } else {
                // holder.mTextTime.setVisibility(View.VISIBLE);

                if (dueDateText.contains("-")) {
                    dueDateText = dueDateText.replaceAll("-", "");
                    holder.mTextTime.setTextColor(getResources().getColor(R.color.red));
                    holder.mTextTime.setText(" Vaccination is due ");
                } else {
                    holder.mTextTime.setTextColor(getResources().getColor(R.color.themeColor));
                    holder.mTextTime.setText(" Vaccination is due" + dueDateText);
                }
            }

            // holder.mTextTime.setText("Next vaccination is due in "+DueMonth+"
            // months");

            holder.mImageInfo.setTag(position);
            holder.mImageInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showDialog( immunizationlist.get(Integer.parseInt(v.getTag().toString())).getName(),immunizationlist.get(Integer.parseInt(v.getTag().toString())).getBenefits());


                }
            });

            holder.mImageUpdate1.setTag(POSITION, position);
            holder.mImageUpdate1.setTag(OUTOFTOTAL, currentDoses);
            holder.mImageUpdate1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(v.getTag(OUTOFTOTAL).toString()) < 1) {

                        mixpanel.timeEvent("Vaccinations_V1_1_button click");
                        try {
                            JSONObject props = new JSONObject();
                            props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                            props.put("LoginNumber", prefs.getUserName());
                            mixpanel.track("Vaccinations_V1_1_button click", props);
                        } catch (JSONException e) {
                            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                        }
                        UpdateDialog(immunizationlist.get(Integer.parseInt(v.getTag(POSITION).toString()))
                                .getImmunization_id(), 1);
                    }
                }
            });

            holder.mImageUpdate2.setTag(POSITION, position);
            holder.mImageUpdate2.setTag(OUTOFTOTAL, currentDoses);
            holder.mImageUpdate2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(v.getTag(OUTOFTOTAL).toString()) < 2) {
                        mixpanel.timeEvent("Vaccinations_V2_2_button click");
                        try {
                            JSONObject props = new JSONObject();
                            props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                            props.put("LoginNumber", prefs.getUserName());
                            mixpanel.track("Vaccinations_V2_2_button click", props);
                        } catch (JSONException e) {
                            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                        }
                        UpdateDialog(immunizationlist.get(Integer.parseInt(v.getTag(POSITION).toString()))
                                .getImmunization_id(), 2);
                    }
                }
            });

            holder.mImageUpdate3.setTag(POSITION, position);
            holder.mImageUpdate3.setTag(OUTOFTOTAL, currentDoses);
            holder.mImageUpdate3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(v.getTag(OUTOFTOTAL).toString()) < 3) {
                        mixpanel.timeEvent("Vaccinations_V3_3_button click");
                        try {
                            JSONObject props = new JSONObject();
                            props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                            props.put("LoginNumber", prefs.getUserName());
                            mixpanel.track("Vaccinations_V3_3_button click", props);
                        } catch (JSONException e) {
                            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                        }
                        UpdateDialog(immunizationlist.get(Integer.parseInt(v.getTag(POSITION).toString()))
                                .getImmunization_id(), 3);
                    }
                }
            });

            holder.mImageUpdate4.setTag(POSITION, position);
            holder.mImageUpdate4.setTag(OUTOFTOTAL, currentDoses);
            holder.mImageUpdate4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(v.getTag(OUTOFTOTAL).toString()) < 4) {
                        mixpanel.timeEvent("Vaccinations_V4_4_button click");
                        try {
                            JSONObject props = new JSONObject();
                            props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                            props.put("LoginNumber", prefs.getUserName());
                            mixpanel.track("Vaccinations_V4_4_button click", props);
                        } catch (JSONException e) {
                            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                        }
                        UpdateDialog(immunizationlist.get(Integer.parseInt(v.getTag(POSITION).toString()))
                                .getImmunization_id(), 4);
                    }
                }
            });

            holder.mImageUpdate5.setTag(POSITION, position);
            holder.mImageUpdate5.setTag(OUTOFTOTAL, currentDoses);
            holder.mImageUpdate5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(v.getTag(OUTOFTOTAL).toString()) < 5) {
                        mixpanel.timeEvent("Vaccinations_V5_5_button click");
                        try {
                            JSONObject props = new JSONObject();
                            props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                            props.put("LoginNumber", prefs.getUserName());
                            mixpanel.track("Vaccinations_V5_5_button click", props);
                        } catch (JSONException e) {
                            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                        }
                        UpdateDialog(immunizationlist.get(Integer.parseInt(v.getTag(POSITION).toString()))
                                .getImmunization_id(), 5);
                    }
                }
            });

            holder.mImageUpdate6.setTag(POSITION, position);
            holder.mImageUpdate6.setTag(OUTOFTOTAL, currentDoses);
            holder.mImageUpdate6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.parseInt(v.getTag(OUTOFTOTAL).toString()) < 6) {
                        mixpanel.timeEvent("Vaccinations_V6_6_button click");
                        try {
                            JSONObject props = new JSONObject();
                            props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                            props.put("LoginNumber", prefs.getUserName());
                            mixpanel.track("Vaccinations_V6_6_button click", props);
                        } catch (JSONException e) {
                            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
                        }
                        UpdateDialog(immunizationlist.get(Integer.parseInt(v.getTag(POSITION).toString()))
                                .getImmunization_id(), 6);
                    }
                }
            });

            if (currentDoses > 0) {
                holder.mImageUpdate1.setImageResource(R.drawable.check);
                if (currentDoses > 1) {
                    holder.mImageUpdate2.setImageResource(R.drawable.check);
                    if (currentDoses > 2) {
                        holder.mImageUpdate3.setImageResource(R.drawable.check);
                        if (currentDoses > 3) {
                            holder.mImageUpdate4.setImageResource(R.drawable.check);
                            if (currentDoses > 4) {
                                holder.mImageUpdate5.setImageResource(R.drawable.check);
                                if (currentDoses > 5) {
                                    holder.mImageUpdate6.setImageResource(R.drawable.check);
                                } else {
                                    holder.mImageUpdate6.setImageResource(R.drawable.update);
                                }
                            } else {
                                holder.mImageUpdate5.setImageResource(R.drawable.update);
                            }
                        } else {
                            holder.mImageUpdate4.setImageResource(R.drawable.update);
                        }
                    } else {
                        holder.mImageUpdate3.setImageResource(R.drawable.update);
                    }
                } else {
                    holder.mImageUpdate2.setImageResource(R.drawable.update);
                }
            } else {
                holder.mImageUpdate1.setImageResource(R.drawable.update);
            }

            if (TotalDoses > 0) {
                holder.mImageUpdate1.setVisibility(View.VISIBLE);
                if (TotalDoses > 1) {
                    holder.mImageUpdate2.setVisibility(View.VISIBLE);
                    holder.mImageUpdate1Lable.setVisibility(View.VISIBLE);
                    if (TotalDoses > 2) {
                        holder.mImageUpdate3.setVisibility(View.VISIBLE);
                        holder.mImageUpdate2Lable.setVisibility(View.VISIBLE);
                        if (TotalDoses > 3) {
                            holder.mImageUpdate4.setVisibility(View.VISIBLE);
                            holder.mImageUpdate3Lable.setVisibility(View.VISIBLE);
                            if (TotalDoses > 4) {
                                holder.mImageUpdate5.setVisibility(View.VISIBLE);
                                holder.mImageUpdate4Lable.setVisibility(View.VISIBLE);
                                if (TotalDoses > 5) {
                                    holder.mImageUpdate6.setVisibility(View.VISIBLE);
                                    holder.mImageUpdate5Lable.setVisibility(View.VISIBLE);
                                } else {
                                    holder.mImageUpdate6.setVisibility(View.INVISIBLE);
                                    holder.mImageUpdate5Lable.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                holder.mImageUpdate5.setVisibility(View.INVISIBLE);
                                holder.mImageUpdate4Lable.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            holder.mImageUpdate4.setVisibility(View.INVISIBLE);
                            holder.mImageUpdate3Lable.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        holder.mImageUpdate3.setVisibility(View.INVISIBLE);
                        holder.mImageUpdate2Lable.setVisibility(View.INVISIBLE);
                    }
                } else {
                    holder.mImageUpdate2.setVisibility(View.INVISIBLE);
                    holder.mImageUpdate1Lable.setVisibility(View.INVISIBLE);
                }
            } else {
                holder.mImageUpdate1.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            return immunizationlist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTextName;
            TextView mTextOutof;
            TextView mTextTime;
            ImageView mImageInfo;

            ImageView mImageUpdate1;
            ImageView mImageUpdate2;
            ImageView mImageUpdate3;
            ImageView mImageUpdate4;
            ImageView mImageUpdate5;
            ImageView mImageUpdate6;

            ImageView mImageUpdate1Lable;
            ImageView mImageUpdate2Lable;
            ImageView mImageUpdate3Lable;
            ImageView mImageUpdate4Lable;
            ImageView mImageUpdate5Lable;


            public ViewHolder(View itemView) {
                super(itemView);
                mTextName = (TextView) itemView.findViewById(R.id.fragment_immunization_text_name);
                mTextOutof = (TextView) itemView.findViewById(R.id.fragment_immunization_text_outof);
                mTextTime = (TextView) itemView.findViewById(R.id.fragment_immunization_text_time);

                mImageInfo = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_info);

                mImageUpdate1 = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_1);
                mImageUpdate2 = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_2);
                mImageUpdate3 = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_3);
                mImageUpdate4 = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_4);
                mImageUpdate5 = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_5);
                mImageUpdate6 = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_6);

                mImageUpdate1Lable = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_1lable);
                mImageUpdate2Lable = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_2lable);
                mImageUpdate3Lable = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_3lable);
                mImageUpdate4Lable = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_4lable);
                mImageUpdate5Lable = (ImageView) itemView.findViewById(R.id.fragment_immunization_img_update_5lable);

            }

        }
    }

    private void showDialog(String title,String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);
        builder.setMessage(message);

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
        dialog.setCancelable(true);
        // display dialog
        dialog.show();
    }

    protected void UpdateDialog(final String immuId, final int currentDoses) {

        if(currentDoses==1){
            mixpanel.timeEvent("Vaccinations_V1_1_saved");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                props.put("LoginNumber", prefs.getUserName());
                mixpanel.track("Vaccinations_V1_1_saved", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }
        }else if(currentDoses==2){
            mixpanel.timeEvent("Vaccinations_V2_2_saved");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                props.put("LoginNumber", prefs.getUserName());
                mixpanel.track("Vaccinations_V2_2_saved", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }

        }else if(currentDoses==3){
            mixpanel.timeEvent("Vaccinations_V3_3_saved");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                props.put("LoginNumber", prefs.getUserName());
                mixpanel.track("Vaccinations_V3_3_saved", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }

        }else if(currentDoses==4){
            mixpanel.timeEvent("Vaccinations_V4_4_saved");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                props.put("LoginNumber", prefs.getUserName());
                mixpanel.track("Vaccinations_V4_4_saved", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }

        }else if(currentDoses==5){
            mixpanel.timeEvent("Vaccinations_V5_5_saved");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                props.put("LoginNumber", prefs.getUserName());
                mixpanel.track("Vaccinations_V5_5_saved", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }

        }else if(currentDoses==6){
            mixpanel.timeEvent("Vaccinations_V6_6_saved");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                props.put("LoginNumber", prefs.getUserName());
                mixpanel.track("Vaccinations_V6_6_saved", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }

        }

        Alert_Dialog = new Dialog(getActivity());
        Alert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Alert_Dialog.setTitle("EkinCare");
        Alert_Dialog.setContentView(R.layout.fragment_dialog_immunization);

        Alert_Dialog.setCancelable(true);
        Alert_Dialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(Alert_Dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        Alert_Dialog.getWindow().setAttributes(lp);
        Alert_Dialog.show();

        mTextViewThisMonth = (TextView) Alert_Dialog.findViewById(R.id.txtThisMonth);
        mTextViewThisYear = (TextView) Alert_Dialog.findViewById(R.id.txtThisYear);
        mTextViewMoreYear = (TextView) Alert_Dialog.findViewById(R.id.txtMoreThanYear);

        mTextViewImmuDate = (TextView) Alert_Dialog.findViewById(R.id.txtDate);

        mTextViewThisMonth.setTag("1");
        mTextViewThisYear.setTag("2");
        mTextViewMoreYear.setTag("3");

        mTextViewImmuDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(mTextViewImmuDate);
            }
        });


        mTextViewThisMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceSelector(v);
            }
        });
        mTextViewThisYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceSelector(v);
            }
        });
        mTextViewMoreYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceSelector(v);
            }
        });

        // SingleChoiceSelector(mTextViewMoreYear);

        ((TextView) Alert_Dialog.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert_Dialog.cancel();
            }
        });
        ((TextView) Alert_Dialog.findViewById(R.id.ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("coming volley=========="+"Yes");

                if (!strDate.equalsIgnoreCase("")) {

                    JSONObject object = new JSONObject();
                    JSONObject objectWizard = new JSONObject();
                    try {
                        object.put("date", strDate);
                        object.put("current_dosage", currentDoses);
                        object.put("instructions", "");
                        objectWizard.put("customer_immunization", object);

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                    if (NetworkCaller.isInternetOncheck(getActivity())) {
                        mRegister = new Register();
                        String URL = TagValues.UPDATE_IMMUNIZATION_URL + immuId;
                        System.out.println("coming volley=========="+"Yes");

                        vaccinationUpdateRequest(URL,objectWizard);

                    } else {
                        showDialog("Internet!","Failed to connect. Internet is not available.");
                        //CustomeDialog.dispDialog(getActivity(), "Internet not available");
                    }
                } else {
                    showDialog("Invalid selection!","Please select at least one.");
                    //CustomeDialog.dispDialog(getActivity(), "Please select atleast one");
                }
            }
        });
    }

    private void vaccinationUpdateRequest(String url, JSONObject objectWizard) {
        System.out.println("ImmunizationsPagerItemFragment.vaccinationUpdateRequest");

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,url,objectWizard,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("update===="+response.toString());
                            immunizationUpdateResponse(response);
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
                String output = headers.get("ETag");
                etag = output.replaceAll("W/", "");
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
                params.put("X-HTTP-Method-Override","PATCH");
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(getActivity()));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY",strFamilyMemberKey);
                params.put("Content-type", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    private void immunizationUpdateResponse(JSONObject response) {

        mRegister = new Gson().fromJson(response.toString(), Register.class);
        if (mRegister != null && mRegister.getMessage() != null) {
            if (Alert_Dialog != null && Alert_Dialog.isShowing())
                Alert_Dialog.cancel();
            updateImmunizationDetails();
        }
    }

    private void updateImmunizationDetails() {
        {
            showPDialog();
            System.out.println("Volley data=========="+"else");
            if (NetworkCaller.isInternetOncheck(getActivity())) {
                url = TagValues.GET_IMMUNIZATION_URL;
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        if (result != null) {
                            System.out.println("Immunizations voll====" + result.toString());
                            mImmunization = new Gson().fromJson(result.toString(), Immunization.class);
                            displayView();
                            jsonResponse = result.toString();
                            Thread thread = new Thread(new FragmentVaccinationChildTab.MyRunnable());
                            thread.start();
                        } else {
                            showDialog("Internet!","Sync failed. Internet is not available.");
                            //CustomeDialog.showToast(getActivity(), "Sync failed. Internet not available");
                        }
                        hidePDialog();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // TODO Auto-generated method stub
                        System.out.println("volley error========"+arg0);

                    }
                }) {
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        Map<String, String> headers = response.headers;
                        Set<String> keySet = headers.keySet();
                        String output = headers.get("ETag");
                        etag = output.replaceAll("W/", "");
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
                        params.put("X-DEVICE-ID", customerManager.getDeviceID(getActivity()));
                        if (!strFamilyMemberKey.equalsIgnoreCase(""))
                            params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                        params.put("Content-type", "application/json");

                        return params;


                    }
                };
                VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);

            } else {
                Toast.makeText(getActivity(), getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void SingleChoiceSelector(View view) {

        mTextViewThisMonth.setBackgroundResource(R.drawable.wizard_multiitem_selectore);
        mTextViewThisYear.setBackgroundResource(R.drawable.wizard_multiitem_selectore);
        mTextViewMoreYear.setBackgroundResource(R.drawable.wizard_multiitem_selectore);

        mTextViewThisMonth.setTextColor(view.getResources().getColor(R.color.light_gray));
        mTextViewThisYear.setTextColor(view.getResources().getColor(R.color.light_gray));
        mTextViewMoreYear.setTextColor(view.getResources().getColor(R.color.light_gray));

        ((TextView) view).setBackgroundResource(R.color.themeColor);
        ((TextView) view).setTextColor(view.getResources().getColor(R.color.white));

        String strTag = view.getTag().toString();

        Calendar mCalendar = Calendar.getInstance();

        Calendar dateToDisplay = Calendar.getInstance();
        SimpleDateFormat dateViewFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        if (strTag.equalsIgnoreCase("1")) {
            dateToDisplay.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), 1);
            // strDate =
            // "01"+""+mCalendar.get(Calendar.MONTH)+""+mCalendar.get(Calendar.YEAR);
            strDate = dateViewFormatter.format(dateToDisplay.getTime());
        } else if (strTag.equalsIgnoreCase("2")) {
            dateToDisplay.set(mCalendar.get(Calendar.YEAR), 1, 1);
            // strDate = "01"+"01"+""+mCalendar.get(Calendar.YEAR);
            strDate = dateViewFormatter.format(dateToDisplay.getTime());
        } else if (strTag.equalsIgnoreCase("3")) {
            dateToDisplay.set((mCalendar.get(Calendar.YEAR) - 1), 1, 1);
            // strDate = "01"+"01"+""+(mCalendar.get(Calendar.YEAR)-1);
            strDate = dateViewFormatter.format(dateToDisplay.getTime());
        }

    }

    public void showDatePicker(final TextView date)
    {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.dialog_date_picker_view, null, false);
        dialog.setContentView(dialogLayout);

        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.dialog_datepicker);
        final TextView dateTextView = (TextView) dialog.findViewById(R.id.dialog_dateview);
       // final TextView dialogTitle = (TextView) dialog.findViewById(R.id.dialog_title);

        final SimpleDateFormat dateViewFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        // Minimum date
        Calendar minDate = Calendar.getInstance();
        try {
            minDate.setTime(formatter.parse("01-01-1970"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePicker.setMinDate(minDate.getTimeInMillis());
        datePicker.setMaxDate(System.currentTimeMillis());
        // View settings
       // dialogTitle.setText("Choose Date of Vaccinations");
        Calendar choosenDate = Calendar.getInstance();
        int year = 1970;
        int month = choosenDate.get(Calendar.MONTH);
        int day = choosenDate.get(Calendar.DAY_OF_MONTH);
        try {
            Date choosenDateFromUI = dateViewFormatter.parse(date.getText().toString());
            choosenDate.setTime(choosenDateFromUI);
            year = choosenDate.get(Calendar.YEAR);
            month = choosenDate.get(Calendar.MONTH);
            day = choosenDate.get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar dateToDisplay = Calendar.getInstance();
        dateToDisplay.set(year, month, day);
        dateTextView.setText(dateViewFormatter.format(dateToDisplay.getTime()));

        // Initialize datepicker in dialog atepicker
        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar choosenDate = Calendar.getInstance();
                choosenDate.set(year, monthOfYear, dayOfMonth);
                dateTextView.setText(dateViewFormatter.format(choosenDate.getTime()));
            }
        });

        // Buttons
        TextView buttonCancel = (TextView) dialog.findViewById(R.id.dialog_close);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView buttonDone = (TextView) dialog.findViewById(R.id.dialog_choose);
        buttonDone.setText("Done");
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar choosen = Calendar.getInstance();
                choosen.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                strDate = formatter.format(choosen.getTime());
                // dateString = choosen.getTime();
                // date.setText(dateViewFormatter.format(choosen.getTime()));
                date.setText(formatter.format(choosen.getTime()));
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public String getDifferentTime(long firstTime, long secondTime) {
        boolean isGraterThanOne = false;
        if (secondTime > firstTime) {
            return "-";
        }
        String returnDate = "";
        // long currentDate = System.currentTimeMillis();
        String strFirstDate = "";
        String strSecondDate = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // format.setTimeZone(TimeZone.getTimeZone("IST"));
        strFirstDate = format.format(firstTime);
        strSecondDate = format.format(secondTime);
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = format.parse(strFirstDate);
            secondDate = format.parse(strSecondDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Period p = new Period(secondDate.getTime(), firstDate.getTime());
        int diffYear = p.getYears();
        int diffMonth = p.getMonths();
        int diffDay = p.getDays();
        int hours = p.getHours();
        int minutes = p.getMinutes();

        if (diffYear > 0) {
            isGraterThanOne = true;
            if (diffYear > 1) {
                returnDate = " in" + " " + diffYear + " Years ";
            } else {
                returnDate = " in" + " " + diffYear + " Year ";
            }
        } else if (diffYear < 0) {
            isGraterThanOne = true;
            if (diffYear < -1) {
                returnDate = " in" + " " + diffYear + " Years ";
            } else {
                returnDate = " in" + " " + diffYear + " Year ";
            }
        } else if (diffMonth > 0) {
            if (diffMonth > 0) {
                if (diffMonth > 1) {
                    returnDate = returnDate + " in" + " " + diffMonth + " Months ";
                } else {
                    returnDate = returnDate + " in" + " " + diffMonth + " Month ";
                }
            }
        } else if (diffDay == 0) {
            returnDate = " " + "today";
        } else if (diffDay == 1) {
            returnDate = " " + "today";
        } else if (diffDay == 2) {
            returnDate = " " + "yesterday";
        } else if (diffDay > 2) {
            returnDate = " in" + " " + diffDay + " days";

        }

	/*
     * if(!isGraterThanOne){ if(diffDay>0){ if(diffDay>1){ returnDate =
	 * returnDate + " "+diffDay + " Days "; }else{ returnDate = returnDate +
	 * " "+diffDay + " Day "; } }else if(diffDay<0){ if(diffDay<-1){
	 * returnDate = returnDate + " "+diffDay + " Days "; }else{ returnDate =
	 * returnDate + " "+diffDay + " Day "; } }
	 */
        if (returnDate.equalsIgnoreCase("")) {
            if (hours > 0) {
                if (hours > 1) {
                    returnDate = "Today";
                } else {
                    returnDate = "Today ";
                }
            } else if (hours < 0) {
                if (hours < -1) {
                    returnDate = "Today ";
                } else {
                    returnDate = "Today";
                }
            }

            if (returnDate.equalsIgnoreCase("")) {
                if (minutes > 0) {
                    if (minutes > 1) {
                        returnDate = "Today";
                    } else {
                        returnDate = "Today";
                    }
                } else if (minutes < 0) {
                    if (minutes < -1) {
                        returnDate = "Today";
                    } else {
                        returnDate = "Today";
                    }
                } else {
                    returnDate = "Today";
                }
            }
        }

        return returnDate;
    }

    private static class ImmunizationComparator implements Comparator<Immunization.ImmunizationData> {

        @Override
        public int compare(Immunization.ImmunizationData lhs, Immunization.ImmunizationData rhs) {
            // TODO Auto-generated method stub

            if (Integer.parseInt(lhs.getMinage()) > Integer.parseInt(rhs.getMinage())) {
                return 1;
            }

            return -1;
        }

    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("volley check===="+etag+"===="+etag);
            dbHandler.insertImmunization(jsonResponse, customerManager.getCurrentFamilyMember().getIdentification_token(), etag);

        }
    }

    private class GetVaccinationDataThread implements Runnable{

        @Override
        public void run() {
            getImmunizationDetails();
        }
    }

    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }
    private void showPDialog() {
        mAlert_Dialog = new Dialog(getActivity());
        mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlert_Dialog.setContentView(R.layout.materialprogressbar);
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //mAlert_Dialog.setCancelable(false);
        mAlert_Dialog.setCanceledOnTouchOutside(true);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

}
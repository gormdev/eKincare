package com.ekincare.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ekincare.ui.custom.WrappingLinearLayoutManager;
import com.ekincare.util.DateUtility;
import com.ekincare.util.ExpandableHeightListView;
import com.ekincare.util.FloatingButtonAccess;
import com.ekincare.util.SimpleDividerItemDecoration;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.ActivityAssessmentMay;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.TimelineData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by RaviTejaN on 24-11-2016.
 */

@SuppressLint("ValidFragment")
public class FragmentTimelineHistoryChildTab extends Fragment {


    View createView;
    LinearLayout linearLayoutProgress;

    FragmentTimelineHistoryChildTab.OutofZoneAdapter outofzoneadapter;
    List<TimelineData.OutOfZone> mOutOfZones;

    String mStringActivityType;
    //ScrollView fragment_overview_main_scrollview;
    RelativeLayout noitemsView;
    private RecyclerView recyclerViewAssesment;
    private CardView cardHistory;
    private FragmentTimelineHistoryChildTab.RecyclerViewAssessmentAdapter mAdapter;
    private String strFamilyMemberKey = "";
    private Customer mCustomer;

    private TimelineData mTimelineData;
    private PreferenceHelper prefs;

    // The BroadcastReceiver that tracks network connectivity changes.
    MixpanelAPI mixpanel;

    Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;
    DatabaseOverAllHandler dbHandler;
    private DbResponse data;
    String jsonResponse;
    String etag;
    String responseCode;

    CustomerManager customerManager;
    FloatingButtonAccess mFloatingButtonAccess;
    Dialog alertDocShare;
    public AppCompatEditText doctor_name;
    public AppCompatEditText doctor_mobile;
    public AppCompatEditText doctor_email;
    TextView expertopFAQ;
    TextView expertopSubmitButton;
    NestedScrollView historyScroll;


    public FragmentTimelineHistoryChildTab() {
    }

    @SuppressLint("ValidFragment")
    public FragmentTimelineHistoryChildTab(FloatingButtonAccess mFloatingButtonAccess ) {
        this.mFloatingButtonAccess=mFloatingButtonAccess;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        mCustomer = customerManager.getCurrentFamilyMember();
        prefs = new PreferenceHelper(getActivity());
        mOutOfZones = new ArrayList<TimelineData.OutOfZone>();
        dbHandler = new DatabaseOverAllHandler(getActivity());

    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        createView = inflater.inflate(R.layout.fragment_family_member_history, container, false);

        initilizeView(createView);

        mixpanel = MixpanelAPI.getInstance(getActivity(), TagValues.MIXPANEL_TOKEN);
        if (mCustomer != null && mCustomer.getId() != null && !mCustomer.getId().equalsIgnoreCase(""))
        {
            mixpanel.timeEvent("TimelinePage");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name()+mCustomer.getLast_name());
                props.put("LoginNumber",prefs.getUserName());
                mixpanel.track("TimelinePage", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }
        }else{

        }

        linearLayoutProgress.setVisibility(View.VISIBLE);
        noitemsView.setVisibility(View.GONE);
        cardHistory.setVisibility(View.GONE);

        Thread thread = new Thread(new FragmentTimelineHistoryChildTab.GetTimeLineData());
        thread.start();

        setHasOptionsMenu(false);
        historyScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll DOWN");
                    mFloatingButtonAccess.getMedicalHistory().setVisibility(View.GONE);
                }
                if (scrollY < oldScrollY) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll UP");
                    mFloatingButtonAccess.getMedicalHistory().setVisibility(View.VISIBLE);
                }

                if (scrollY == 0) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"TOP SCROLL");
                    mFloatingButtonAccess.getMedicalHistory().setVisibility(View.VISIBLE);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"BOTTOM SCROLL");
                    mFloatingButtonAccess.getMedicalHistory().setVisibility(View.GONE);
                }
            }
        });

        return createView;
    }

    public void addTimelineData(){
        Thread thread = new Thread(new FragmentTimelineHistoryChildTab.GetTimeLineData());
        thread.start();

    }

    public void setData(){
        linearLayoutProgress.setVisibility(View.VISIBLE);
        noitemsView.setVisibility(View.GONE);
        cardHistory.setVisibility(View.GONE);
        Thread thread = new Thread(new FragmentTimelineHistoryChildTab.GetTimeLineData());
        thread.start();

    }

    private void initilizeView(View createView) {
        recyclerViewAssesment = (RecyclerView) createView.findViewById(R.id.recyclerview_family_history);
        noitemsView = (RelativeLayout) createView.findViewById(R.id.mainLayout);
        historyScroll=(NestedScrollView)createView.findViewById(R.id.history_scroll);

        recyclerViewAssesment.setLayoutManager(new WrappingLinearLayoutManager(getActivity()));
        recyclerViewAssesment.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAssesment.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        recyclerViewAssesment.setNestedScrollingEnabled(false);

        cardHistory = (CardView) createView.findViewById(R.id.card_medical_history);

        linearLayoutProgress = (LinearLayout) createView.findViewById(R.id.progress_layout);

        linearLayoutProgress.setVisibility(View.VISIBLE);
        cardHistory.setVisibility(View.GONE);
        noitemsView.setVisibility(View.GONE);
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
                        if (NetworkCaller.isInternetOncheck(getActivity()))
                        {
                            linearLayoutProgress.setVisibility(View.VISIBLE);
                            noitemsView.setVisibility(View.GONE);
                            cardHistory.setVisibility(View.GONE);
                            dbHandler.clearTimeline();
                            Thread thread = new Thread(new FragmentTimelineHistoryChildTab.GetTimeLineData());
                            thread.start();
                        }else {
                            Toast.makeText(getActivity(), getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
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
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //mAlert_Dialog.setCancelable(false);
        mAlert_Dialog.setCanceledOnTouchOutside(true);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }
    public void getTimelineMembers()
    {
        System.out.println("Test========="+"IN");

        if(customerManager.getCurrentFamilyMember()!=null)
        {
            System.out.println("Test========="+"IN j");
        data = dbHandler.getTimelineData(customerManager.getCurrentFamilyMember().getIdentification_token());
        if(data != null && data.getResponse()!=null) {
            System.out.println("Test========="+"IN l");
            showDataDbListview(data.getResponse());
            backgoundgthreadStoreData(false);
        }else{
            if (NetworkCaller.isInternetOncheck(getActivity())) {
                System.out.println("Test========="+"IN M");
                backgoundgthreadStoreData(true);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        }else{
            if (NetworkCaller.isInternetOncheck(getActivity())) {
                System.out.println("Test========="+"IN M");
                backgoundgthreadStoreData(true);
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void backgoundgthreadStoreData(final boolean flag)
    {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, TagValues.Timeline_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result)
            {
                System.out.println("FragmentTimelineHistoryChildTab.onResponse========"+"phalu");
                if (result != null) {
                    if(responseCode.equals("200 OK")){
                        if(flag)
                        {
                            showDataListview(result);
                        }
                        jsonResponse = result.toString();
                        Thread thread = new Thread(new FragmentTimelineHistoryChildTab.MyRunnable());
                        thread.start();
                    }else if(responseCode.equals("304 Not Modified")){

                    }
                }else{
                    //Nodata
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

    private void showDataListview(JSONObject result) {
        mTimelineData = new Gson().fromJson(result.toString(),TimelineData.class);
        System.out.println("FragmentTimelineHistoryChildTab.showDataDbListview="+mTimelineData.getTimeline().size());
        mAdapter = new FragmentTimelineHistoryChildTab.RecyclerViewAssessmentAdapter(mTimelineData.getTimeline());

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayView();
            }
        });
    }

    private void displayView()
    {
        linearLayoutProgress.setVisibility(View.GONE);
        System.out.println("FragmentTimelineHistoryChildTab.displayView="+mTimelineData.getTimeline().size());
        if (mTimelineData.getTimeline() != null)
        {
            recyclerViewAssesment.setAdapter(mAdapter);

            if (mTimelineData.getTimeline().size() > 0) {
                System.out.println("FragmentTimelineHistoryChildTab.displayView case1");
                cardHistory.setVisibility(View.VISIBLE);
                noitemsView.setVisibility(View.GONE);
            } else {
                System.out.println("FragmentTimelineHistoryChildTab.displayView case2");
                noitemsView.setVisibility(View.VISIBLE);
                cardHistory.setVisibility(View.GONE);
            }

        }else{
            System.out.println("FragmentTimelineHistoryChildTab.displayView case3");
            noitemsView.setVisibility(View.VISIBLE);
            cardHistory.setVisibility(View.GONE);
        }
    }

    private void showDataDbListview(String result)
    {
        mTimelineData = new Gson().fromJson(result.toString(),TimelineData.class);
        System.out.println("FragmentTimelineHistoryChildTab.showDataDbListview="+mTimelineData.getTimeline().size());
        mAdapter = new FragmentTimelineHistoryChildTab.RecyclerViewAssessmentAdapter(mTimelineData.getTimeline());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayView();
            }
        });
    }

    public void refreshTab() {
        System.out.println("FragmentTimelineHistoryChildTab.refreshTab");
        Thread thread = new Thread(new FragmentTimelineHistoryChildTab.GetTimeLineData());
        thread.start();
    }


    public class RecyclerViewAssessmentAdapter extends RecyclerView.Adapter<FragmentTimelineHistoryChildTab.RecyclerViewAssessmentAdapter.MyViewHolder> {

        List<TimelineData.TimelineDataset> mArrayListAdapter;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mTextDiscription;
            TextView mTextViewDate;
            TextView mTextViewoctorName;
            CardView mCardAssessmentView;
            ImageView mImageViewAssessmentImage;
            LinearLayout outOfZoneView1, outOfZoneView2, moreViewLayout;
            TextView outOfZoneTitle1, outOfZoneTitle2, outOfZoneValue1, outOfZoneValue2;
            TextView more;

            public MyViewHolder(View convertView) {
                super(convertView);
                mTextViewDate = (TextView) convertView.findViewById(R.id.row_item_fragment_timeline_new_list_assessment_date_text);
                mTextViewoctorName = (TextView) convertView.findViewById(R.id.row_item_fragment_timeline_new_list_assessment_doctor_name);
                mTextDiscription = (TextView) convertView.findViewById(R.id.row_item_fragment_timeline_new_list_assessment_discription);
                mCardAssessmentView = (CardView) convertView.findViewById(R.id.card_medical_history);
                more = (TextView) convertView.findViewById(R.id.more);
                outOfZoneView1 = (LinearLayout) convertView.findViewById(R.id.out_of_zone_view1);
                outOfZoneView2 = (LinearLayout) convertView.findViewById(R.id.out_of_zone_view2);
                outOfZoneTitle1 = (TextView) convertView.findViewById(R.id.out_of_zone_view_title1);
                outOfZoneTitle2 = (TextView) convertView.findViewById(R.id.out_of_zone_view_title2);
                outOfZoneValue1 = (TextView) convertView.findViewById(R.id.out_of_zone_view_value1);
                outOfZoneValue2 = (TextView) convertView.findViewById(R.id.out_of_zone_view_value2);
                moreViewLayout = (LinearLayout) convertView.findViewById(R.id.more_view);

                mImageViewAssessmentImage = (ImageView) convertView.findViewById(R.id.row_item_fragment_timeline_list_assessment_type_img);


            }
        }

        public RecyclerViewAssessmentAdapter(List<TimelineData.TimelineDataset> mArrayListAdapter) {
            this.mArrayListAdapter = mArrayListAdapter;
        }

        @Override
        public FragmentTimelineHistoryChildTab.RecyclerViewAssessmentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_medical_history_layout, parent, false);

            return new FragmentTimelineHistoryChildTab.RecyclerViewAssessmentAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FragmentTimelineHistoryChildTab.RecyclerViewAssessmentAdapter.MyViewHolder holder, final int position) {

            holder.mTextViewDate.setText(DateUtility.getconvertdate(mArrayListAdapter.get(position).getRequest_date()));
            mOutOfZones = mArrayListAdapter.get(position).getOut_of_zone();
            if (mOutOfZones == null) {
                mOutOfZones = new ArrayList<TimelineData.OutOfZone>();
            }

            if (mOutOfZones.size() == 0) {
                holder.outOfZoneView1.setVisibility(View.GONE);
                holder.outOfZoneView2.setVisibility(View.GONE);
                holder.moreViewLayout.setVisibility(View.GONE);

            } else if (mOutOfZones.size() == 1) {
                holder.outOfZoneView1.setVisibility(View.VISIBLE);
                holder.outOfZoneView2.setVisibility(View.GONE);
                if (mOutOfZones.get(0).getColor().equals("text-danger")) {
                    holder.outOfZoneValue1.setTextColor(Color.parseColor("#fe4c5a"));
                } else if (mOutOfZones.get(0).getColor().equals("text-warning")) {
                    holder.outOfZoneValue1.setTextColor(Color.parseColor("#ffff900d"));
                }
                holder.outOfZoneTitle1.setText(mOutOfZones.get(0).getTest_component_name());
                if(mOutOfZones.get(0).getUnits()==null){
                    holder.outOfZoneValue1.setText(mOutOfZones.get(0).getResult());
                }else{
                    holder.outOfZoneValue1.setText(mOutOfZones.get(0).getResult() + " " + mOutOfZones.get(0).getUnits());
                }
                holder.moreViewLayout.setVisibility(View.GONE);

            } else if (mOutOfZones.size() == 2) {

                holder.outOfZoneView1.setVisibility(View.VISIBLE);
                holder.outOfZoneView2.setVisibility(View.VISIBLE);

                if (mOutOfZones.get(0).getColor().equals("text-danger")) {
                    holder.outOfZoneValue1.setTextColor(Color.parseColor("#fe4c5a"));
                } else if (mOutOfZones.get(0).getColor().equals("text-warning")) {
                    holder.outOfZoneValue1.setTextColor(Color.parseColor("#ffff900d"));
                }

                if (mOutOfZones.get(1).getColor().equals("text-danger")) {
                    holder.outOfZoneValue2.setTextColor(Color.parseColor("#fe4c5a"));
                } else if (mOutOfZones.get(1).getColor().equals("text-warning")) {
                    holder.outOfZoneValue2.setTextColor(Color.parseColor("#ffff900d"));
                }

                holder.outOfZoneTitle1.setText(mOutOfZones.get(0).getTest_component_name());
                holder.outOfZoneTitle2.setText(mOutOfZones.get(1).getTest_component_name());

                if(mOutOfZones.get(0).getUnits()==null){
                    holder.outOfZoneValue1.setText(mOutOfZones.get(0).getResult());
                }else{
                    holder.outOfZoneValue1.setText(mOutOfZones.get(0).getResult() + " " + mOutOfZones.get(0).getUnits());
                }

                if(mOutOfZones.get(1).getUnits()==null)
                {
                    holder.outOfZoneValue2.setText(mOutOfZones.get(1).getResult());
                }else{
                    holder.outOfZoneValue2.setText(mOutOfZones.get(1).getResult() + " " + mOutOfZones.get(1).getUnits());
                }
                holder.moreViewLayout.setVisibility(View.GONE);

            } else if (mOutOfZones.size() > 2)
            {
                holder.outOfZoneView1.setVisibility(View.VISIBLE);
                holder.outOfZoneView2.setVisibility(View.VISIBLE);

                if (mOutOfZones.get(0).getColor().equals("text-danger")) {
                    holder.outOfZoneValue1.setTextColor(Color.parseColor("#fe4c5a"));
                } else if (mOutOfZones.get(0).getColor().equals("text-warning")) {
                    holder.outOfZoneValue1.setTextColor(Color.parseColor("#ffff900d"));
                }

                if (mOutOfZones.get(1).getColor().equals("text-danger")) {
                    holder.outOfZoneValue2.setTextColor(Color.parseColor("#fe4c5a"));
                } else if (mOutOfZones.get(1).getColor().equals("text-warning")) {
                    holder.outOfZoneValue2.setTextColor(Color.parseColor("#ffff900d"));
                }

                holder.outOfZoneTitle1.setText(mOutOfZones.get(0).getTest_component_name());
                holder.outOfZoneTitle2.setText(mOutOfZones.get(1).getTest_component_name());

                if(mOutOfZones.get(0).getUnits()==null){
                    holder.outOfZoneValue1.setText(mOutOfZones.get(0).getResult());
                }else{
                    holder.outOfZoneValue1.setText(mOutOfZones.get(0).getResult() + " " + mOutOfZones.get(0).getUnits());
                }

                if(mOutOfZones.get(1).getUnits()==null){
                    holder.outOfZoneValue2.setText(mOutOfZones.get(1).getResult());
                }else{
                    holder.outOfZoneValue2.setText(mOutOfZones.get(1).getResult() + " " + mOutOfZones.get(1).getUnits());
                }

                holder.moreViewLayout.setVisibility(View.VISIBLE);
                holder.more.setText(Integer.toString(mOutOfZones.size() - 2) + "+");
            }
            /*
            More Button CLick Action
			 */
            holder.moreViewLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog Alert_Dialog = new Dialog(getActivity());
                    Alert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Alert_Dialog.setContentView(R.layout.more_button_dialog);
                    Alert_Dialog.setCancelable(true);
                    ExpandableHeightListView outZoneListview = (ExpandableHeightListView) Alert_Dialog.findViewById(R.id.out_of_zone_listview);
                    TextView outOfZoneClose = (TextView) Alert_Dialog.findViewById(R.id.out_of_zone_close);
                    mOutOfZones = mArrayListAdapter.get(position).getOut_of_zone();
                    outofzoneadapter = new FragmentTimelineHistoryChildTab.OutofZoneAdapter(mOutOfZones);
                    outZoneListview.setAdapter(outofzoneadapter);
                    outZoneListview.setExpanded(true);
                    outofzoneadapter.notifyDataSetChanged();
                    outofzoneadapter.notifyDataSetInvalidated();
                    outOfZoneClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Alert_Dialog.cancel();
                        }
                    });

                    Alert_Dialog.show();
                }
            });


            if (mArrayListAdapter.get(position).getTitle().equalsIgnoreCase("Body Assessment")) {
                holder.mImageViewAssessmentImage.setBackgroundResource(R.drawable.b_one);
            } else if (mArrayListAdapter.get(position).getTitle().equalsIgnoreCase("Vision Assessment")) {
                holder.mImageViewAssessmentImage.setBackgroundResource(R.drawable.b_three);
            } else if (mArrayListAdapter.get(position).getTitle().equalsIgnoreCase("Dental Assessment")) {
                holder.mImageViewAssessmentImage.setBackgroundResource(R.drawable.b_two);
            }

            if (mArrayListAdapter.get(position).getDoctor_name() != null && !mArrayListAdapter.get(position).getDoctor_name().equalsIgnoreCase("")) {
                holder.mTextViewoctorName.setVisibility(View.VISIBLE);
                holder.mTextViewoctorName.setText("Referred By: " +  mArrayListAdapter.get(position).getDoctor_name());

            } else {
                holder.mTextViewoctorName.setText("");
                holder.mTextViewoctorName.setVisibility(View.GONE);
            }

            if (mArrayListAdapter.get(position).getDescription() != null && !mArrayListAdapter.get(position).getDescription().equalsIgnoreCase("")) {

                holder.mTextDiscription.setVisibility(View.VISIBLE);
                holder.mTextDiscription.setText(mArrayListAdapter.get(position).getDescription() );
            } else {
                holder.mTextDiscription.setText("");
                holder.mTextDiscription.setVisibility(View.GONE);
            }



            holder.mCardAssessmentView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    System.out.println("TimeLineData============" + "Click WOrking"+"========="+mArrayListAdapter.size());
                    // TODO Auto-generated method stub
                    if (mArrayListAdapter.get(position).getActivity_type().equalsIgnoreCase("BodyAssessment")) {
                        mStringActivityType = "body_assessments";
                    } else if (mArrayListAdapter.get(position).getActivity_type().equalsIgnoreCase("VisionAssessment")) {
                        mStringActivityType = "vision_assessments";
                    } else if (mArrayListAdapter.get(position).getActivity_type().equalsIgnoreCase("DentalAssessment")) {
                        mStringActivityType = "dental_assessments";
                    }

                    if (mStringActivityType.equalsIgnoreCase("body_assessments")) {

                        Intent intent = new Intent(getActivity(), ActivityAssessmentMay.class);
                        intent.putExtra("mStringActivityType", mStringActivityType);
                        intent.putExtra("mStringId", mArrayListAdapter.get(position).getAssociated_id());
                        intent.putExtra("mStringFamilyMemberKey", strFamilyMemberKey);
                        intent.putExtra("mStringDate", mArrayListAdapter.get(position).getCreated_at());
                        intent.putExtra("mStringAssessmentType", mArrayListAdapter.get(position).getTitle());
                        intent.putExtra("mStringDoctorName", mArrayListAdapter.get(position).getDoctor_name());
                        intent.putExtra("mStringProviderName", mArrayListAdapter.get(position).getProvider_name());
                        intent.putExtra("mStringBadge", mArrayListAdapter.get(position).getBadge());
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(intent);

                    } else if (mStringActivityType.equalsIgnoreCase("vision_assessments")) {

                    } else if (mStringActivityType.equalsIgnoreCase("dental_assessments")) {

                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return mArrayListAdapter.size();
        }
    }

    public class OutofZoneAdapter extends BaseAdapter {

        FragmentTimelineHistoryChildTab.OutofZoneAdapter.ViewHolder holder = null;
        List<TimelineData.OutOfZone> mArrayListAdapter;

        public OutofZoneAdapter(List<TimelineData.OutOfZone> mArrayListAdapter) {
            this.mArrayListAdapter = mArrayListAdapter;
        }

        @Override
        public int getCount() {
            return mArrayListAdapter.size();

        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                holder = new FragmentTimelineHistoryChildTab.OutofZoneAdapter.ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.out_of_zone_row, parent, false);

                holder.outOfZoneTitle = (TextView) convertView.findViewById(R.id.out_of_zone_title);
                holder.outOfZoneValue = (TextView) convertView.findViewById(R.id.out_of_zone_value);

                convertView.setTag(holder);
            } else {
                holder = (FragmentTimelineHistoryChildTab.OutofZoneAdapter.ViewHolder) convertView.getTag();
            }

            if (mOutOfZones.get(position).getColor().equals("text-danger")) {
                holder.outOfZoneValue.setTextColor(Color.parseColor("#fe4c5a"));
            } else if (mOutOfZones.get(position).getColor().equals("text-warning")) {
                holder.outOfZoneValue.setTextColor(Color.parseColor("#ffff900d"));
            }

            if(mArrayListAdapter.get(position).getUnits()==null){

                holder.outOfZoneValue.setText(mArrayListAdapter.get(position).getResult());
            }else{
                holder.outOfZoneValue.setText(mArrayListAdapter.get(position).getResult() + "" + mArrayListAdapter.get(position).getUnits());
            }


            holder.outOfZoneTitle.setText(mArrayListAdapter.get(position).getTest_component_name());


            return convertView;
        }

        public class ViewHolder {
            TextView outOfZoneTitle;
            TextView outOfZoneValue;
        }

    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            try{
                System.out.println("volley check===="+etag+"===="+etag);
                dbHandler.insertTimeline(jsonResponse, customerManager.getCurrentFamilyMember().getIdentification_token(), etag);
            }catch (Exception e){
                System.out.println("MyRunnable.run Exception in timeline");
            }
        }
    }

    private class GetTimeLineData implements Runnable{

        @Override
        public void run() {
            getTimelineMembers();
        }
    }


}


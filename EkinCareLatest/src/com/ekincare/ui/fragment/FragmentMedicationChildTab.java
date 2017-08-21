package com.ekincare.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
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
import android.widget.ListView;
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
import com.ekincare.util.FloatingButtonAccess;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.ActivityFamilyMemberAddMedication;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.GetMedicationData;
import com.oneclick.ekincare.vo.Medication;
import com.oneclick.ekincare.vo.SyncModel;

import org.joda.time.Period;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Created by RaviTejaN on 24-11-2016.
 */
@SuppressLint("ValidFragment")
public class FragmentMedicationChildTab extends Fragment {

    private LinearLayout linearLayoutAddMedication,linearLayoutProgress;
    private RelativeLayout linearLayoutViewMedication;
    private ListView listViewMorningMedication,listViewEveningMedication,listViewNoonMedication;
    private CardView cardViewMorningMedication,cardViewEveningMedication,cardViewNoonMedication;
    private TextView textViewCurrentDate;

    private FragmentMedicationChildTab.ListAdapter listAdapterMorning,listAdapterEvening,listAdapterNoon;

    private ArrayList<Medication> listMorning,listEvening,listNoon;

    private Customer mCustomer;
    private PreferenceHelper prefs;
    private String strFamilyMemberKey = "";

    MixpanelAPI mixpanel;

    DatabaseOverAllHandler dbHandler;


    private DbResponse data;

    String jsonResponse;

    String etag;

    String responseCode;

    CircleProgressBar progressWithArrow;

    CustomerManager customerManager;

    GetMedicationData getMedicationData;

    Dialog mAlert_Dialog;

    ArrayList<Medication> medicationList = new ArrayList<>();
    private List<String> morningTakenArray,eveningTakenArray, afternoonTakenArray;

   // FloatingButtonAccess mFloatingButtonAccess;
    NestedScrollView medicineNestedScroll;
    private boolean isScrolling = false;
    Dialog alertDocShare;
    public AppCompatEditText doctor_name;
    public AppCompatEditText doctor_mobile;
    public AppCompatEditText doctor_email;
    TextView expertopFAQ;
    TextView expertopSubmitButton;
    FloatingButtonAccess mFloatingButtonAccess;

    public FragmentMedicationChildTab() {
    }

    @SuppressLint("ValidFragment")
    public FragmentMedicationChildTab(FloatingButtonAccess mFloatingButtonAccess ) {
        this.mFloatingButtonAccess=mFloatingButtonAccess;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        prefs = new PreferenceHelper(getActivity());
        mCustomer = customerManager.getCurrentFamilyMember();
        dbHandler = new DatabaseOverAllHandler(getActivity());
        getMedicationData = new GetMedicationData();

        listMorning = new ArrayList<>();
        listEvening = new ArrayList<>();
        listNoon = new ArrayList<>();

        morningTakenArray = new ArrayList<>();
        eveningTakenArray = new ArrayList<>();
        afternoonTakenArray = new ArrayList<>();

        if(!prefs.getMorningTakenMedicationId().isEmpty()){
            morningTakenArray = Arrays.asList(prefs.getMorningTakenMedicationId().split("\\s*,\\s*"));
        }
        if(!prefs.getEveningTakenMedicationId().isEmpty()){
            eveningTakenArray = Arrays.asList(prefs.getEveningTakenMedicationId().split("\\s*,\\s*"));
        }
        if(!prefs.getAfterNoonTakenMedicationId().isEmpty()){
            afternoonTakenArray = Arrays.asList(prefs.getAfterNoonTakenMedicationId().split("\\s*,\\s*"));
        }
    }

    @Override
    @Nullable
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_family_member_medication, container, false);

        setHasOptionsMenu(true);

        listAdapterMorning = new FragmentMedicationChildTab.ListAdapter(listMorning,1);
        listAdapterNoon = new FragmentMedicationChildTab.ListAdapter(listNoon,2);
        listAdapterEvening = new FragmentMedicationChildTab.ListAdapter(listEvening,3);

        initilizeView(mView);

        setCurrentDate();

        mixpanel = MixpanelAPI.getInstance(getActivity(), TagValues.MIXPANEL_TOKEN);
        if (mCustomer != null && mCustomer.getId() != null
                && !mCustomer.getId().equalsIgnoreCase("")) {

            mixpanel.timeEvent("MedicationPage");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                props.put("LoginNumber", prefs.getUserName());
                mixpanel.track("MedicationPage", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }
        } else {

        }

        Thread thread = new Thread(new FragmentMedicationChildTab.GetMedicationDataThread());
        thread.start();

        setHasOptionsMenu(false);

        medicineNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll DOWN");
                    mFloatingButtonAccess.getMedicationFab().setVisibility(View.GONE);
                }
                if (scrollY < oldScrollY) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll UP");
                    mFloatingButtonAccess.getMedicationFab().setVisibility(View.VISIBLE);
                }

                if (scrollY == 0) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"TOP SCROLL");
                    mFloatingButtonAccess.getMedicationFab().setVisibility(View.VISIBLE);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"BOTTOM SCROLL");
                    mFloatingButtonAccess.getMedicationFab().setVisibility(View.GONE);
                }
            }
        });
        return mView;
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
                            listMorning.clear();
                            listNoon.clear();
                            listEvening.clear();
                            medicationList.clear();
                            linearLayoutViewMedication.setVisibility(View.GONE);
                            linearLayoutAddMedication.setVisibility(View.GONE);
                            dbHandler.clearMedication();
                            Thread thread = new Thread(new FragmentMedicationChildTab.GetMedicationDataThread());
                            thread.start();

                        } else {
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

    public void getAllMedication()
    {
        if(mCustomer!=null){
            data = dbHandler.getMedicationData(customerManager.getCurrentFamilyMember().getIdentification_token());

            if (data != null && data.getResponse() != null) {
                System.out.println("myKey db data"+"Yes");
                System.out.println("myKey db data"+ data.getResponse());
                showDbMedication(data.getResponse());
                backgoundSaveMedication(false);
            } else
            {
                if (NetworkCaller.isInternetOncheck(getActivity()))
                {
                    backgoundSaveMedication(true);

                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            linearLayoutViewMedication.setVisibility(View.GONE);
                            linearLayoutAddMedication.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity().getApplicationContext(),getString(R.string.networkloss), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        }

    }

    private void showDbMedication(String result) {
        System.out.println("myKey db data"+"Yes yes");
        getMedicationData = new Gson().fromJson(result.toString(),GetMedicationData.class);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showDataInList(getMedicationData);
            }
        });
    }

    public void showDataInList(GetMedicationData getMedicationData)
    {
        linearLayoutProgress.setVisibility(View.GONE);

        try{
            if (getMedicationData.getMedications() != null) {
                System.out.println("3.7 FragmentMedicationChildTab.showDataInList getMedicationData.getMedications().size()=" + getMedicationData.getMedications().size());
                if (getMedicationData.getMedications().size() > 0)
                {
                    linearLayoutViewMedication.setVisibility(View.VISIBLE);
                    linearLayoutAddMedication.setVisibility(View.GONE);

                    try{
                        medicationList.clear();
                        listMorning.clear();
                        listEvening.clear();
                        listNoon.clear();

                        listAdapterEvening.notifyDataSetChanged();
                        listAdapterNoon.notifyDataSetChanged();
                        listAdapterMorning.notifyDataSetChanged();

                    }catch (Exception e){
                        System.out.println("myKey db data"+"Exception in");
                        e.printStackTrace();
                    }

                    medicationList = getMedicationData.getMedications();
                    System.out.println("FragmentMedicationChildTab.showDataInList medicationList="+medicationList.size());

                    int numberOfDays = 0;
                    int difference = 0;

                    for(int i = 0 ; i< medicationList.size() ; i++)
                    {
                        System.out.println("FragmentMedicationChildTab.showDataInList i=" + i + " " + medicationList.get(i).getRecurring() ) ;

                        numberOfDays = Integer.parseInt(medicationList.get(i).getNumber_of_days());

                        difference = differenceBtwDate(medicationList.get(i).getCreated_at(),new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

                        System.out.println("1111 FragmentMedicationChildTab.showDataInList= medicationList.get(i)!=null"+medicationList.get(i).getRecurring()!=null + "  == " + medicationList.get(i).getRecurring());

                        try{
                            if(numberOfDays == 0 && medicationList.get(i).getRecurring().equalsIgnoreCase("true")){
                                System.out.println("myKey db data"+"1");
                                createListsForAdapter(medicationList.get(i));
                            }else if( (numberOfDays != 0) && (numberOfDays>=difference) ){
                                System.out.println("myKey db data"+"2");
                                createListsForAdapter(medicationList.get(i));
                            }else{

                                System.out.println("myKey db data"+"3");
                                System.out.println("FragmentMedicationChildTab.showDataInList case 3");
                            }
                        }catch (Exception e){
                            System.out.println("myKey db data"+e);
                            System.out.println("FragmentMedicationChildTab.showDataInList innerCatch");
                            continue;
                        }
                    }

                    try{
                        if(listMorning.size()>0 || listEvening.size()>0 || listNoon.size()>0)
                        {
                            setMorningListView();

                            setNoonListView();

                            setEveningListView();
                        } else
                        {
                            linearLayoutViewMedication.setVisibility(View.GONE);
                            linearLayoutAddMedication.setVisibility(View.VISIBLE);
                        }
                    }catch (Exception e){
                        System.out.println("myKey db data"+"Exception second");
                        System.out.println("FragmentMedicationChildTab.showDataInList 2nd innerCatch");
                    }

                } else
                {
                    linearLayoutViewMedication.setVisibility(View.GONE);
                    linearLayoutAddMedication.setVisibility(View.VISIBLE);
                }

            }else{
                linearLayoutViewMedication.setVisibility(View.GONE);
                linearLayoutAddMedication.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            System.out.println("myKey db data"+"Exception");
            System.out.println("FragmentMedicationChildTab.showDataInList e="+ e.toString());
            Toast.makeText(getActivity(),"Something went wrong while fetching medication. Add medication again.",Toast.LENGTH_LONG).show();
            linearLayoutViewMedication.setVisibility(View.GONE);
            linearLayoutAddMedication.setVisibility(View.VISIBLE);
        }
    }

    private void createListsForAdapter(Medication medication) {
        if(medication.getMorning().equalsIgnoreCase("true"))
        {
            System.out.println("FragmentMedicationChildTab.createListsForAdapter medicationList.get(i).getMorning()="+medication.getMorning());
            listMorning.add(medication);
        }
        if(medication.getEvening().equalsIgnoreCase("true"))
        {
            System.out.println("FragmentMedicationChildTab.createListsForAdapter medicationList.get(i).getEvening()="+medication.getEvening());
            listEvening.add(medication);
        }
        if(medication.getAfternoon().equalsIgnoreCase("true"))
        {
            System.out.println("FragmentMedicationChildTab.createListsForAdapter medicationList.get(i).getAfternoon()="+medication.getAfternoon());
            listNoon.add(medication);
        }
    }

    private void showDataMedicationList(JSONObject result)
    {
        getMedicationData = new Gson().fromJson(result.toString(),GetMedicationData.class);
        try{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showDataInList(getMedicationData);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public void backgoundSaveMedication(final boolean showList) {
        System.out.println("111111 FragmentMedicationChildTab.backgoundSaveMedication mykeys==========="+prefs.getCustomerKey()+"==="+prefs.getEkinKey()+"==="+customerManager.getCurrentFamilyMember().getIdentification_token() + " ======== " + customerManager.getDeviceID(getActivity()));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, TagValues.Get_All_Medication_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                System.out.println("1111111 FragmentMedicationChildTab.onResponse result =" + result);
                if (result != null) {

                    if(responseCode.equals("200 OK")){
                        if(showList){
                            showDataMedicationList(result);
                        }
                        jsonResponse = result.toString();
                        Thread thread = new Thread(new FragmentMedicationChildTab.MedicationSaveDb());
                        thread.start();
                    }else if(responseCode.equals("304 Not Modified")){
                        Toast.makeText(getActivity(),"Something went wrong.Try again later.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(),"Something went wrong.Try again later.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                System.out.println("FragmentMedicationChildTab.onErrorResponse");
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
        // Adding request to request queue
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }


    private void setCurrentDate() {
        String todaydate = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date());
        textViewCurrentDate.setText(todaydate);
    }

    private String getCurrentDate() {
        String todaydate = new SimpleDateFormat("EEE, d MMM yyyy").format(new Date());
        return todaydate;
    }


    private void setEveningListView()
    {
        if(listEvening.size()>0){
            System.out.println("FragmentMedicationChildTab.setEveningListView listEvening.size()="+listEvening.size());
            cardViewEveningMedication.setVisibility(View.VISIBLE);
            listViewEveningMedication.setAdapter(listAdapterEvening);
            setListViewHeightBasedOnChildren(listViewEveningMedication);
        }else{
            cardViewEveningMedication.setVisibility(View.GONE);
        }

    }

    private void setNoonListView() {
        if(listNoon.size()>0){
            System.out.println("FragmentMedicationChildTab.setNoonListView listNoon.size()="+listNoon.size());
            cardViewNoonMedication.setVisibility(View.VISIBLE);
            listViewNoonMedication.setAdapter(listAdapterNoon);
            setListViewHeightBasedOnChildren(listViewNoonMedication);
        }else{
            cardViewNoonMedication.setVisibility(View.GONE);
        }

    }

    private void setMorningListView() {
        if(listMorning.size()>0){
            System.out.println("FragmentMedicationChildTab.setMorningListView listMorning.size()="+listMorning.size());
            cardViewMorningMedication.setVisibility(View.VISIBLE);
            listViewMorningMedication.setAdapter(listAdapterMorning);
            setListViewHeightBasedOnChildren(listViewMorningMedication);
        }else{
            cardViewMorningMedication.setVisibility(View.GONE);
        }
    }

    private void initilizeView(View mView) {
        medicineNestedScroll=(NestedScrollView)mView.findViewById(R.id.medicine_nested_scroll);
        linearLayoutAddMedication = (LinearLayout) mView.findViewById(R.id.add_medication_layout);
        linearLayoutProgress = (LinearLayout) mView.findViewById(R.id.progress_layout);
        linearLayoutViewMedication = (RelativeLayout) mView.findViewById(R.id.view_medication_layout);

        listViewMorningMedication = (ListView) mView.findViewById(R.id.listview_morning_medication);
        listViewEveningMedication = (ListView) mView.findViewById(R.id.listview_night_medication);
        listViewNoonMedication = (ListView) mView.findViewById(R.id.listview_noon_medication);

        cardViewMorningMedication = (CardView) mView.findViewById(R.id.card_morning);
        cardViewEveningMedication = (CardView) mView.findViewById(R.id.card_evening);
        cardViewNoonMedication = (CardView) mView.findViewById(R.id.card_noon);

        textViewCurrentDate = (TextView) mView.findViewById(R.id.textview_date);

        linearLayoutAddMedication.setVisibility(View.GONE);
        linearLayoutViewMedication.setVisibility(View.GONE);
        linearLayoutProgress.setVisibility(View.VISIBLE);

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        android.widget.ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount()));
        listView.setLayoutParams(params);
    }


    @SuppressLint("SimpleDateFormat")
    public int differenceBtwDate(String strFirstDates, String strSecondDate) {
        int returnDate = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = format2.parse(strFirstDates);
            secondDate = format.parse(strSecondDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //System.out.println("ListAdapter.differenceBtwDate firstDate="+firstDate + " secondDate= "+secondDate);

        if (firstDate != null && secondDate != null) {

            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(firstDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(secondDate);

            Period p = new Period(firstDate.getTime(), secondDate.getTime());
            int diffYear = p.getYears();
            int diffMonth = p.getMonths();
            int diffDay = p.getDays();
            int hours = p.getHours();
            int minutes = p.getMinutes();

            diffDay+=1;

            returnDate = diffDay;
        }
        return returnDate;
    }

    public String getSuffix(int i) {
        int  j = i % 10,
                k = i % 100;
        if (j == 1 && k != 11) {
            return "st";
        }
        if (j == 2 && k != 12) {
            return "nd";
        }
        if (j == 3 && k != 13) {
            return "rd";
        }
        return "th";
    }

    Dialog dialogOption;

    private void alertDialogOptions(final String id, final Medication medication)
    {
        dialogOption = new Dialog(getActivity());
        dialogOption.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOption.setContentView(R.layout.allergy_update_delete);
        Window window = dialogOption.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        //window.getAttributes().windowAnimations = R.style.DialogAnimationallergy;
        dialogOption.setCancelable(true);
        TextView allergyUpdatelist=(TextView) dialogOption.findViewById(R.id.allergy_update_list);
        TextView allergyDeletelist=(TextView) dialogOption.findViewById(R.id.allergy_delete_list);

        allergyUpdatelist.setText("Update Medication");
        allergyDeletelist.setText("Delete Medication");

        allergyUpdatelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (NetworkCaller.isInternetOncheck(getActivity())) {
                    Intent intent = new Intent(getActivity(),ActivityFamilyMemberAddMedication.class);
                    intent.putExtra("isEdit",true);
                    intent.putExtra("medicineData",medication);
                    startActivity(intent);
                    dialogOption.dismiss();
                }else{
                    dialogOption.dismiss();
                    Toast.makeText(getActivity(),getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
                }
            }
        });
        allergyDeletelist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkCaller.isInternetOncheck(getActivity())) {
                    deleteMedication(medication);
                }else{
                    dialogOption.dismiss();
                    Toast.makeText(getActivity(),getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
                }
            }
        });

        dialogOption.show();
    }

    private void deleteMedication(final Medication medication) {
        String URL = TagValues.Get_All_Medication_URL+"/"+medication.getId() ;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.DELETE,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if(response!=null)
                        {
                            if(responseCode.equals("200 OK"))
                            {
                                dbHandler.clearMedication();
                                dialogOption.dismiss();

                                if(listEvening.contains(medication)){
                                    listEvening.remove(medication);
                                    listAdapterEvening.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren(listViewEveningMedication);

                                    if(listEvening.size()<=0){
                                        cardViewEveningMedication.setVisibility(View.GONE);
                                    }
                                }
                                if(listMorning.contains(medication)){
                                    listMorning.remove(medication);
                                    listAdapterMorning.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren(listViewMorningMedication);

                                    if(listMorning.size()<=0){
                                        cardViewMorningMedication.setVisibility(View.GONE);
                                    }
                                }
                                if(listNoon.contains(medication)){
                                    listNoon.remove(medication);
                                    listAdapterNoon.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren(listViewNoonMedication);

                                    if(listNoon.size()<=0){
                                        cardViewNoonMedication.setVisibility(View.GONE);
                                    }
                                }

                                medicationList.remove(medication);

                                if(medicationList.size()==0){
                                    linearLayoutViewMedication.setVisibility(View.GONE);
                                    linearLayoutAddMedication.setVisibility(View.VISIBLE);
                                }

                                Toast.makeText(getActivity(),"Medication is removed.",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity(),"Something went wrong. Try again later.",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getActivity(),"Something went wrong. Try again later.",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Something went wrong. Try again later.",Toast.LENGTH_LONG).show();
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
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);
    }

    public void takeMedication(JSONObject jsonObject, final TextView textViewMedicationNotTaken, final ImageView imageViewTaken, final String id, final int i)
    {
        System.out.println("AddMedicationActivity.activateMedication jsonObject="+jsonObject);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, TagValues.Get_All_Medication_URL + "/course", jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result)
            {
                System.out.println("5.5 AddMedicationActivity.activateMedication = " + result);
                if (result != null)
                {
                    if(responseCode.equals("200 OK"))
                    {
                        ViewCompat.animate(textViewMedicationNotTaken).alphaBy(0.6f).start();
                        ViewCompat.animate(textViewMedicationNotTaken).alphaBy(0.4f).start();
                        ViewCompat.animate(textViewMedicationNotTaken).alphaBy(0).start();
                        ViewCompat.animate(imageViewTaken).scaleY(0).scaleX(0).start();

                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run() {
                                imageViewTaken.setVisibility(View.VISIBLE);
                                ViewCompat.animate(imageViewTaken).scaleY(1).scaleX(1).start();
                                textViewMedicationNotTaken.setVisibility(View.GONE);
                            }
                        }, 300);

                        try{
                            System.out.println("FragmentMedicationChildTab.onResponse i="+i + " id="+id);
                            switch (i){
                                case 1 :
                                    if(!morningTakenArray.contains(id))
                                    {
                                        morningTakenArray.add(id);
                                        String ids = getConvertedString(morningTakenArray);
                                        prefs.setMorningTakenMedicationId(ids);
                                    }
                                    break;
                                case 2 :
                                    if(!afternoonTakenArray.contains(id))
                                    {
                                        afternoonTakenArray.add(id);
                                        String ids = getConvertedString(afternoonTakenArray);
                                        prefs.setAfterNoonTakenMedicationId(ids);
                                    }

                                    break;
                                case 3 :
                                    if(!eveningTakenArray.contains(id))
                                    {
                                        eveningTakenArray.add(id);
                                        String ids = getConvertedString(eveningTakenArray);
                                        prefs.setEveningTakenMedicationId(ids);
                                    }
                                    break;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else if(responseCode.equals("304 Not Modified")){}

                }else{
                    //Nodata
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) { System.out.println("1.7 ActivityFamilyMember.backgroundTimeLineThreadStoreData error = " + arg0);}
        })
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
                Map<String,String> headers = networkResponse.headers;
                Set<String> keySet = headers.keySet();
                responseCode=headers.get("Status");
                etag = headers.get("ETag");
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
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");

                return params;
            }
        };

        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    private String getConvertedString(List<String> arrayList){
        StringBuilder stringBuilder = new StringBuilder();
        for(String string : arrayList) {
            stringBuilder.append(string);
            stringBuilder.append(",");
        }
        return stringBuilder.length() > 0 ? stringBuilder.substring(0, stringBuilder.length() - 1): "";
    }

    public void refreshTab() {
        Thread thread = new Thread(new FragmentMedicationChildTab.GetMedicationDataThread());
        thread.start();
    }


    private class GetMedicationDataThread implements Runnable{
        @Override
        public void run() {
            getAllMedication();
        }
    }

    private class MedicationSaveDb implements Runnable {
        @Override
        public void run() {
            System.out.println("MedicationSaveDb.run");
            dbHandler.insertMedication(jsonResponse, customerManager.getCurrentFamilyMember().getIdentification_token(), etag);
        }
    }

    public class ListAdapter extends BaseAdapter
    {
        ArrayList<Medication> listData;
        String afterMealBeforeMeal;
        String quantity = "0";
        String medicationType;
        int id,resourceImage;

        public ListAdapter(ArrayList<Medication> listData,int id)
        {
            this.listData = listData;
            this.id = id;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            View row = convertView;
            FragmentMedicationChildTab.ListAdapter.ViewHolder holder = null;
            if(row == null) {
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.row_view_medication_layout, parent, false);
                holder = new FragmentMedicationChildTab.ListAdapter.ViewHolder();
                holder.textViewMedicationName =  (TextView) row.findViewById(R.id.text_medication_name);
                holder.relativeLayoutContainer =  (RelativeLayout) row.findViewById(R.id.container_medication);
                holder.textViewMedicationNotTaken = (TextView)row.findViewById(R.id.text_take);
                holder.imageViewTaken = (ImageView)row.findViewById(R.id.image_done) ;
                holder.imageViewMedicationtype = (ImageView)row.findViewById(R.id.image_medication) ;
                holder.textViewMedicationQuantity = (TextView)row.findViewById(R.id.text_medication_quantity);
                holder.textViewMedicationDays =  (TextView)row.findViewById(R.id.text_medication_time);
                holder.viewDivider = (View)row.findViewById(R.id.divider);
                row.setTag(holder);
            } else {
                holder = (FragmentMedicationChildTab.ListAdapter.ViewHolder) row.getTag();
            }

            if(position == listData.size()-1){
                holder.viewDivider.setVisibility(View.GONE);
            }else{
                holder.viewDivider.setVisibility(View.VISIBLE);
            }

            holder.textViewMedicationName.setText(listData.get(position).getName() + ", " + listData.get(position).getDose_quantity());

            if(listData.get(position).getBefore_meal()!=null){
                if(listData.get(position).getBefore_meal().equalsIgnoreCase("true")){
                    afterMealBeforeMeal  = "before meal";
                }else{
                    afterMealBeforeMeal  = "after meal";
                }
            }else{
                afterMealBeforeMeal  = "after meal";
            }


            if(id == 1){
                if(morningTakenArray.contains(listData.get(position).getId())){
                    holder.textViewMedicationNotTaken.setVisibility(View.GONE);
                    holder.imageViewTaken.setVisibility(View.VISIBLE);
                }else{
                    holder.textViewMedicationNotTaken.setVisibility(View.VISIBLE);
                    holder.imageViewTaken.setVisibility(View.GONE);
                }
                if(listData.get(position).getMorning_quantity()!=null){
                    quantity = listData.get(position).getMorning_quantity();
                }else{
                    quantity ="0";
                }
            }else if(id == 3){
                if(eveningTakenArray.contains(listData.get(position).getId())){
                    holder.textViewMedicationNotTaken.setVisibility(View.GONE);
                    holder.imageViewTaken.setVisibility(View.VISIBLE);
                }else{
                    holder.textViewMedicationNotTaken.setVisibility(View.VISIBLE);
                    holder.imageViewTaken.setVisibility(View.GONE);
                }
                if(listData.get(position).getEvening_quantity()!=null){
                    quantity = listData.get(position).getEvening_quantity();
                }else{
                    quantity ="0";
                }
            }else if(id == 2){
                if(afternoonTakenArray.contains(listData.get(position).getId())){
                    holder.textViewMedicationNotTaken.setVisibility(View.GONE);
                    holder.imageViewTaken.setVisibility(View.VISIBLE);
                }else{
                    holder.textViewMedicationNotTaken.setVisibility(View.VISIBLE);
                    holder.imageViewTaken.setVisibility(View.GONE);
                }

                if(listData.get(position).getAfternoon_quantity()!=null){
                    quantity = listData.get(position).getAfternoon_quantity();
                }else{
                    quantity ="0";
                }
            }

            if(listData.get(position).getMedication_type().equalsIgnoreCase("Syrup")){
                medicationType = "Spoon";
            }else{
                medicationType = listData.get(position).getMedication_type();
            }


            holder.textViewMedicationQuantity.setText(quantity + " " + medicationType + ", " + afterMealBeforeMeal);

            if(listData.get(position).getMedication_type().equalsIgnoreCase("Tablet")){
                resourceImage = R.drawable.tablets;
            }else if(listData.get(position).getMedication_type().equalsIgnoreCase("Capsule")){
                resourceImage = R.drawable.capsule;
            }else if(listData.get(position).getMedication_type().equalsIgnoreCase("Syrup")){
                resourceImage = R.drawable.ic_syrup_24px;
            }else if(listData.get(position).getMedication_type().equalsIgnoreCase("Injection")){
                resourceImage = R.drawable.injection;
            }else{
                resourceImage = R.drawable.fluides;
            }

            System.out.println("ListAdapter.getView" + listData.get(position).getId());

            holder.imageViewMedicationtype.setImageResource(resourceImage);

            final FragmentMedicationChildTab.ListAdapter.ViewHolder finalHolder = holder;

            holder.textViewMedicationNotTaken.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject object = new JSONObject();
                    try {
                        object.put("medication_id",listData.get(position).getId());
                        if(id==1){
                            object.put("morning",listData.get(position).getMorning());
                        }else if(id == 2){
                            object.put("afternoon",listData.get(position).getAfternoon());

                        }else if(id == 3){
                            object.put("evening",listData.get(position).getEvening());
                        }
                        object.put("medication_date",getCurrentDate());

                        jsonObject.put("medication_course",object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    if (NetworkCaller.isInternetOncheck(getActivity())) {
                        takeMedication(jsonObject,finalHolder.textViewMedicationNotTaken,finalHolder.imageViewTaken,listData.get(position).getId(),id);
                    }else{
                        SyncModel syncModel = new SyncModel();
                        syncModel.setURL(TagValues.Get_All_Medication_URL + "/course");
                        syncModel.setJSON(jsonObject.toString());
                        syncModel.setMETHOD("POST");
                        dbHandler.setSyncData(syncModel);


                        //Toast.makeText(getActivity(),getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
                    }
                }
            });

            holder.relativeLayoutContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialogOptions(listData.get(position).getId(),listData.get(position));
                }
            });

            int difference = differenceBtwDate(listData.get(position).getCreated_at(),new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

            boolean isDaily = false;

            if(listData.get(position).getRecurring() != null){
                if(listData.get(position).getRecurring().equalsIgnoreCase("true")){
                    isDaily = true;
                }
            }

            if(isDaily){
                holder.textViewMedicationDays.setText(difference + getSuffix(difference)  + " day of daily medication." );
            }else{
                String day = "";
                if(Integer.parseInt(listData.get(position).getNumber_of_days())>1){
                    day = "days";
                }else{
                    day = day;
                }
                //System.out.println("ListAdapter.getView " + " getCreated_at()="+listData.get(position).getCreated_at());
                holder.textViewMedicationDays.setText( difference + getSuffix(difference) + " day of " + listData.get(position).getNumber_of_days() + " " +day + " medication");
            }

            return row;
        }

        private class ViewHolder {
            private TextView textViewMedicationName;
            private TextView textViewMedicationQuantity;
            private ImageView imageViewMedicationtype;
            private ImageView imageViewTaken;
            private TextView textViewMedicationNotTaken;
            private TextView textViewMedicationDays;
            private RelativeLayout relativeLayoutContainer;
            private View viewDivider;
        }
    }

}

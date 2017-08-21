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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.ekincare.util.FloatingButtonAccess;
import com.ekincare.util.RecyclerViewAllergyAdapter;
import com.ekincare.util.SimpleDividerItemDecoration;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.AllergieData;
import com.oneclick.ekincare.vo.AllergieDataset;
import com.oneclick.ekincare.vo.Customer;

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
public class FragmentAllergiesChildTab extends Fragment implements RecyclerViewAllergyAdapter.OnItemClickListener{
    View createView;
    private RecyclerView recyclerViewAllergy;
    private RecyclerViewAllergyAdapter recyclerViewAllergyAdapter;
    CardView documentCardView;
    RelativeLayout mMainLayout;
    LinearLayout linearLayoutProgress;
    private String strFamilyMemberKey = "";
    private Customer mCustomer;
    private AllergieData mAllergieData;
    List<AllergieDataset> listAllergieDatasets;
    private PreferenceHelper prefs;
    MixpanelAPI mixpanel;
    Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;
    DatabaseOverAllHandler dbHandler;
    String url;
    private DbResponse data;
    String jsonResponse;
    String etag;
    String responseCode;
    CustomerManager customerManager;
    Dialog Alert_Dialog_Update;
    MenuItem refreshMenu;
    Animation rotation ;
    FloatingButtonAccess mFloatingButtonAccess;
    Dialog alertDocShare;
    public AppCompatEditText doctor_name;
    public AppCompatEditText doctor_mobile;
    public AppCompatEditText doctor_email;
    TextView expertopFAQ;
    TextView expertopSubmitButton;
    NestedScrollView allergyNestedScroll;

    public FragmentAllergiesChildTab() {
    }

    @SuppressLint("ValidFragment")
    public FragmentAllergiesChildTab(FloatingButtonAccess mFloatingButtonAccess ) {
        this.mFloatingButtonAccess=mFloatingButtonAccess;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        mCustomer = customerManager.getCurrentFamilyMember();
        prefs = new PreferenceHelper(getActivity());
        listAllergieDatasets = new ArrayList<>();
        rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        createView = inflater.inflate(R.layout.fragment_family_members_allergies2, container, false);

        dbHandler = new DatabaseOverAllHandler(getActivity());

        rotation.setRepeatCount(Animation.INFINITE);

        recyclerViewAllergy = (RecyclerView) createView.findViewById(R.id.recycler_view_allergy);
        allergyNestedScroll=(NestedScrollView)createView.findViewById(R.id.allergy_nested_scroll);
        recyclerViewAllergy.setLayoutManager(new WrappingLinearLayoutManager(getActivity()));
        recyclerViewAllergy.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAllergy.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        recyclerViewAllergy.setNestedScrollingEnabled(false);

        mMainLayout = (RelativeLayout) createView.findViewById(R.id.mainLayout);
        linearLayoutProgress = (LinearLayout) createView.findViewById(R.id.progress_layout);
        documentCardView=(CardView) createView.findViewById(R.id.card_allergy);

        linearLayoutProgress.setVisibility(View.VISIBLE);
        documentCardView.setVisibility(View.GONE);
        mMainLayout.setVisibility(View.GONE);

        mixpanel = MixpanelAPI.getInstance(getActivity(), TagValues.MIXPANEL_TOKEN);
        if (mCustomer != null && mCustomer.getId() != null && !mCustomer.getId().equalsIgnoreCase(""))
        {
            mixpanel.timeEvent("AllergiesPage");
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

        Thread thread = new Thread(new GetAllergyThread());
        thread.start();

        setHasOptionsMenu(false);

        allergyNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll DOWN");
                    mFloatingButtonAccess.getAllergyFab().setVisibility(View.GONE);
                }
                if (scrollY < oldScrollY) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll UP");
                    mFloatingButtonAccess.getAllergyFab().setVisibility(View.VISIBLE);
                }

                if (scrollY == 0) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"TOP SCROLL");
                    mFloatingButtonAccess.getAllergyFab().setVisibility(View.VISIBLE);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"BOTTOM SCROLL");
                    mFloatingButtonAccess.getAllergyFab().setVisibility(View.GONE);
                }
            }
        });



       /* recyclerViewAllergy.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy){
                if (dy > 0 ||dy<0 &&  mFloatingButtonAccess.getAllergyFab().isShown())
                    mFloatingButtonAccess.getAllergyFab().setVisibility(View.GONE);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mFloatingButtonAccess.getAllergyFab().setVisibility(View.VISIBLE);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
*/
        return createView;
    }

    public void addAllergyData(){
        System.out.println("FragmentAllergiesChildTab.addAllergyData");
        Thread thread = new Thread(new FragmentAllergiesChildTab.GetAllergyThread());
        thread.start();

    }

    public void getAllergeData() {
        if(mCustomer!=null){
            data = dbHandler.getallergiesData(customerManager.getCurrentFamilyMember().getIdentification_token());
            if(data != null && data.getResponse()!=null){
                System.out.println("Volley data db=========="+data.getResponse());
                showDataDbListview(data.getResponse());
                backgroundThreadStoreData(false);

            }else{
                if (NetworkCaller.isInternetOncheck(getActivity())) {
                    System.out.println("keys==========="+prefs.getCustomerKey()+"==="+prefs.getEkinKey()+"==="+customerManager.getDeviceID(getActivity()));
                    backgroundThreadStoreData(true);
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            documentCardView.setVisibility(View.GONE);
                            linearLayoutProgress.setVisibility(View.GONE);
                            mMainLayout.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    private void backgroundThreadStoreData(final boolean flag) {
        url=TagValues.Allergi_URL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                if (result != null) {
                    if(responseCode.equals("200 OK")){
                        if(flag){
                            showDataListview(result);
                        }
                        jsonResponse = result.toString();
                        Thread thread = new Thread(new FragmentAllergiesChildTab.MyRunnable());
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
        mAllergieData = new Gson().fromJson(result.toString(),AllergieData.class);
        listAllergieDatasets.clear();
        listAllergieDatasets = mAllergieData.getAllergies();

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

    private void displayView(){
        linearLayoutProgress.setVisibility(View.GONE);
        if (listAllergieDatasets.size() > 0) {
            documentCardView.setVisibility(View.VISIBLE);
            mMainLayout.setVisibility(View.GONE);
            System.out.println("FragmentFamilyMembersAllergies.displayView listAllergieDatasets="+listAllergieDatasets);
            recyclerViewAllergyAdapter = new RecyclerViewAllergyAdapter(listAllergieDatasets,getActivity());
            recyclerViewAllergyAdapter.setOnItemClickListener(this);
            recyclerViewAllergy.setAdapter(recyclerViewAllergyAdapter);
        } else {
            documentCardView.setVisibility(View.GONE);
            mMainLayout.setVisibility(View.VISIBLE);
        }

    }

    private void showDataDbListview(String result)
    {
        System.out.println("data voll allerge===="+result.toString());

        mAllergieData = new Gson().fromJson(result.toString(),AllergieData.class);
        listAllergieDatasets.clear();
        listAllergieDatasets = mAllergieData.getAllergies();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayView();
            }
        });

    }


    @Override
    public void onItemClick(RecyclerViewAllergyAdapter.MyViewHolder item, final int position, View v)
    {

        final PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.getMenuInflater().inflate(R.menu.menu_allergy, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("Edit"))
                {
                    showDialog( mAllergieData.getAllergies().get(position),position);
                    popup.dismiss();
                }else if(item.getTitle().equals("Delete")){
                    deleteAllergy(mAllergieData.getAllergies().get(position).getId(),position);
                    popup.dismiss();
                }
                //Toast.makeText(getActivity(), "You selected the action : " + item.getTitle()+" position "+position, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        popup.show();
    }

    private void deleteAllergy(String id, final int position) {
        String URL = TagValues.Allergi_URL+"/"+id ;
        System.out.println("URL========="+URL);
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.DELETE,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            if(responseCode.equals("200 OK")){
                                System.out.println("put===="+response.toString());
                                //recyclerViewAllergyAdapter.remove(position,listAllergieDatasets.get(position));
                                //Alert_Delete_Update.dismiss();
                                dbHandler.clearAllergy();

                                /*if (listAllergieDatasets.size() == 0) {
                                    linearLayoutProgress.setVisibility(View.GONE);
                                    documentCardView.setVisibility(View.GONE);
                                    mMainLayout.setVisibility(View.VISIBLE);
                                }*/

                                listAllergieDatasets.clear();

                                linearLayoutProgress.setVisibility(View.VISIBLE);
                                documentCardView.setVisibility(View.GONE);
                                mMainLayout.setVisibility(View.GONE);
                                Thread thread = new Thread(new FragmentAllergiesChildTab.GetAllergyThread());
                                thread.start();

                                Toast.makeText(getActivity(),"Allergy is removed.",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getActivity(),"Something went wrong. Try again later.",Toast.LENGTH_LONG).show();
                            }
                            hidePDialog();
                        }else{
                            Toast.makeText(getActivity(),"Something went wrong. Try again later.",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
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
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    private void showDialog(final AllergieDataset allergieDataset, final int position) {
        Alert_Dialog_Update = new Dialog(getActivity());
        Alert_Dialog_Update.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Alert_Dialog_Update.setContentView(R.layout.dialog_allergy);
        Window window = Alert_Dialog_Update.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimationallergy;
        Alert_Dialog_Update.setCancelable(true);

        final AppCompatEditText allergyNamePost=(AppCompatEditText)Alert_Dialog_Update.findViewById(R.id.allergy_name_post);
        final AppCompatEditText  allergyReasonPost=(AppCompatEditText)Alert_Dialog_Update.findViewById(R.id.allergy_reason_post);
        TextView allergyDone=(TextView)Alert_Dialog_Update.findViewById(R.id.allergy_post_done);
        TextView allergyTitle=(TextView)Alert_Dialog_Update.findViewById(R.id.dialog_title);
        TextView allergycancel=(TextView)Alert_Dialog_Update.findViewById(R.id.cancel);

        final TextInputLayout textInputLayoutName = (TextInputLayout)Alert_Dialog_Update.findViewById(R.id.floating_allergy_name);
        final TextInputLayout textInputLayoutReason = (TextInputLayout)Alert_Dialog_Update.findViewById(R.id.floating_reason);
        allergycancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alert_Dialog_Update.dismiss();
            }
        });
        allergyNamePost.addTextChangedListener(new TextWatcher() {
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

        allergyReasonPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textInputLayoutReason.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        allergyNamePost.setText(allergieDataset.getName());
        allergyReasonPost.setText(allergieDataset.getReaction());
        allergyDone.setText("Update allergy");
        allergyTitle.setText("Update allergy");
        allergyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allergyNamePost.getText().toString().isEmpty())
                {
                    textInputLayoutName.setError("Empty Allergy");
                }else if(allergyReasonPost.getText().toString().isEmpty())
                {
                    textInputLayoutReason.setError("Empty Reason");
                }else{
                    JSONObject json = new JSONObject();
                    try {

                        json.put("name", allergyNamePost.getText().toString());
                        json.put("reaction",allergyReasonPost.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    allergieDataset.setName(allergyNamePost.getText().toString());
                    allergieDataset.setReaction(allergyReasonPost.getText().toString());

                    updateAllergy(json,allergieDataset,position);
                }
            }
        });

        Alert_Dialog_Update.show();

    }

    private void updateAllergy(JSONObject json, final AllergieDataset allergieDataset, final int position) {
        String URL = TagValues.Allergi_URL+"/"+allergieDataset.getId() ;
        System.out.println("URL========="+URL);
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.PUT,URL,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null)
                        {
                            if(responseCode.equals("200 OK"))
                            {
                                recyclerViewAllergyAdapter.update(position,allergieDataset);
                                Alert_Dialog_Update.dismiss();
                                System.out.println("put===="+response.toString());
                                Toast.makeText(getActivity(),"Allergy is updated.",Toast.LENGTH_LONG).show();
                                dbHandler.clearAllergy();
                            }else{
                                Toast.makeText(getActivity(),"Something went wrong. Try again later.",Toast.LENGTH_LONG).show();
                            }
                            hidePDialog();
                        }else{
                            Toast.makeText(getActivity(),"Something went wrong. Try again later.",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
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
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);


    }

    public void addAllergyInList(AllergieDataset allergieDataset)
    {
        listAllergieDatasets.clear();

        System.out.println("FragmentAllergiesChildTab.addAllergyInList ="+allergieDataset);
        linearLayoutProgress.setVisibility(View.VISIBLE);
        documentCardView.setVisibility(View.GONE);
        mMainLayout.setVisibility(View.GONE);
        Thread thread = new Thread(new FragmentAllergiesChildTab.GetAllergyThread());
        thread.start();
        //due to wrappingLinearLayout it shows error so refreshing it
        /*if(listAllergieDatasets.size()==0){
            linearLayoutProgress.setVisibility(View.GONE);
            documentCardView.setVisibility(View.VISIBLE);
            mMainLayout.setVisibility(View.GONE);

            listAllergieDatasets.add(allergieDataset);
            recyclerViewAllergyAdapter = new RecyclerViewAllergyAdapter(listAllergieDatasets,getActivity());
            recyclerViewAllergy.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
            recyclerViewAllergyAdapter.setOnItemClickListener(this);
            recyclerViewAllergy.setAdapter(recyclerViewAllergyAdapter);

        }else{
            recyclerViewAllergyAdapter.add(listAllergieDatasets.size(),allergieDataset);
        }*/
    }

    public void refreshTab() {
        Thread thread = new Thread(new GetAllergyThread());
        thread.start();
    }

    private class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("volley check===="+etag+"===="+etag);
            dbHandler.insertAllergieData(jsonResponse, customerManager.getCurrentFamilyMember().getIdentification_token(), etag);
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        refreshMenu = menu.findItem(R.id.menu_refresh);
        refreshMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_refresh:
                        if (NetworkCaller.isInternetOncheck(getActivity()))
                        {
                            linearLayoutProgress.setVisibility(View.VISIBLE);
                            documentCardView.setVisibility(View.GONE);
                            mMainLayout.setVisibility(View.GONE);
                            dbHandler.clearAllergy();
                            Thread thread = new Thread(new FragmentAllergiesChildTab.GetAllergyThread());
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
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //mAlert_Dialog.setCancelable(false);
        mAlert_Dialog.setCanceledOnTouchOutside(true);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

    private class GetAllergyThread implements Runnable{
        @Override
        public void run() {
            getAllergeData();
        }
    }
}


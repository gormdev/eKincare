package com.ekincare.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ekincare.R;
import com.ekincare.app.AppConstants;
import com.ekincare.app.CustomerManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.ui.custom.WrappingLinearLayoutManager;
import com.ekincare.util.DateUtility;
import com.ekincare.util.FileType;
import com.ekincare.util.FloatingButtonAccess;
import com.ekincare.util.SimpleDividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.ReportsImageActivity;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.GetDocumentsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by RaviTejaN on 24-11-2016.
 */
@SuppressLint("ValidFragment")
public class FragmentDocumentChildTab extends Fragment {
    private static final String TAG = "FragmentAllDocumentMembers";
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static List<String> imageTypes = new ArrayList(Arrays.asList(".jpg", ".png", ".jpeg", ".gif"));
    protected static String pdfExtension = ".pdf";

    View createView;
    FragmentDocumentChildTab.RecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    RelativeLayout mMainLayout;
    LinearLayout linearLayoutProgress;
    private GetDocumentsData mGetDocumentsData;
    private Customer mCustomer;
    private PreferenceHelper prefs;
    private String strFamilyMemberKey = "";
    MixpanelAPI mixpanel;

    Dialog mAlert_Dialog;
    DatabaseOverAllHandler dbHandler;
    String url;
    private DbResponse data;
    String jsonResponse;
    String etag;
    String responseCode;
    CardView documentCardView;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;
    Dialog alertDocShare;
    public AppCompatEditText doctor_name;
    public AppCompatEditText doctor_mobile;
    public AppCompatEditText doctor_email;
    TextView expertopFAQ;
    TextView expertopSubmitButton;
    FloatingButtonAccess mFloatingButtonAccess;
    NestedScrollView documenNestedScroll;

    public FragmentDocumentChildTab() {
    }

    @SuppressLint("ValidFragment")
    public FragmentDocumentChildTab(FloatingButtonAccess mFloatingButtonAccess ) {
        this.mFloatingButtonAccess=mFloatingButtonAccess;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        prefs = new PreferenceHelper(getActivity());
        mCustomer = customerManager.getCurrentFamilyMember();
    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("Documents=====FragmentAllDocumentMembers.onCreateView====iscalling");
        createView = inflater.inflate(R.layout.fragment_family_member_document, container, false);

        dbHandler = new DatabaseOverAllHandler(getActivity());
        mGetDocumentsData = new GetDocumentsData();

        documentCardView=(CardView) createView.findViewById(R.id.card_document_history);
        recyclerView = (RecyclerView) createView.findViewById(R.id.recyclerview_doc_history);
        documenNestedScroll=(NestedScrollView)createView.findViewById(R.id.documen_nestedscroll);
        recyclerView.setLayoutManager(new WrappingLinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
        recyclerView.setNestedScrollingEnabled(false);

        adapter = new FragmentDocumentChildTab.RecyclerViewAdapter();

        mMainLayout = (RelativeLayout) createView.findViewById(R.id.mainLayout);
        linearLayoutProgress = (LinearLayout) createView.findViewById(R.id.progress_layout);

        linearLayoutProgress.setVisibility(View.VISIBLE);
        documentCardView.setVisibility(View.GONE);
        mMainLayout.setVisibility(View.GONE);

        mixpanel = MixpanelAPI.getInstance(getActivity(), TagValues.MIXPANEL_TOKEN);
        if (mCustomer != null && mCustomer.getId() != null
                && !mCustomer.getId().equalsIgnoreCase("")) {

            mixpanel.timeEvent("DocumentPage");
            try {
                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name() + mCustomer.getLast_name());
                props.put("LoginNumber", prefs.getUserName());
                mixpanel.track("DocumentPage", props);
            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }
        } else {

        }

        Thread thread = new Thread(new FragmentDocumentChildTab.GetDocData());
        thread.start();
        setHasOptionsMenu(false);

        documenNestedScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll DOWN");
                    mFloatingButtonAccess.getDocumentFab().collapse();
                    mFloatingButtonAccess.getDocumentFab().setVisibility(View.GONE);
                }
                if (scrollY < oldScrollY) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"Scroll UP");
                    mFloatingButtonAccess.getDocumentFab().setVisibility(View.VISIBLE);
                }

                if (scrollY == 0) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"TOP SCROLL");
                    mFloatingButtonAccess.getDocumentFab().setVisibility(View.VISIBLE);
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    System.out.println("FragmentDocumentChildTab.onScrollChange========="+"BOTTOM SCROLL");
                    mFloatingButtonAccess.getDocumentFab().collapse();
                    mFloatingButtonAccess.getDocumentFab().setVisibility(View.GONE);
                }
            }
        });
        return createView;
    }

    public void getAllDocumentsMembers()
    {
        if(mCustomer!=null){
            data = dbHandler.getDocumentsData(customerManager.getCurrentFamilyMember().getIdentification_token());

            if (data != null && data.getResponse() != null) {
                showDbDocuments(data.getResponse());
                backgoundSaveDocument(false);
            } else {
                if (NetworkCaller.isInternetOncheck(getActivity())) {
                    System.out.println("keys==========="+prefs.getCustomerKey()+"==="+prefs.getEkinKey()+"==="+customerManager.getDeviceID(getActivity()));
                    backgoundSaveDocument(true);
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

    private void backgoundSaveDocument(final boolean flag) {
        url = TagValues.Get_All_Documents_URL;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(TagValues.Get_All_Documents_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response != null) {
                            System.out.println("FragmentDocumentChildTab.onResponse==========="+response.toString());
                            System.out.println("FragmentDocumentChildTab.onResponse==========="+responseCode);
                            if(responseCode.equals("200 OK")){
                                if(flag){
                                    showDataDocumentList(response);
                                }
                                jsonResponse = response.toString();
                                Thread thread = new Thread(new FragmentDocumentChildTab.DocumentSaveDb());
                                thread.start();
                            }else if(responseCode.equals("304 Not Modified")){

                            }

                        } else {

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                responseCode=headers.get("Status");
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


        // Adding request to request queue
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonArrayRequest);



    }

    private void showDbDocuments(String response) {
        System.out.println("Documents========FragmentAllDocumentMembers.showDbDocuments===iscalling");
        Gson gson = new Gson();
        Type type = new TypeToken<List<GetDocumentsData.Documents>>() {
        }.getType();
        List<GetDocumentsData.Documents>  contactList = gson.fromJson(response.toString(), type);
        mGetDocumentsData.setDocuments(contactList);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayView();
            }
        });
    }
    private void showDataDocumentList(JSONArray result)
    {
        System.out.println("Documents======="+result.toString());
        Gson gson = new Gson();
        Type type = new TypeToken<List<GetDocumentsData.Documents>>() {
        }.getType();
        List<GetDocumentsData.Documents> contactList = gson.fromJson(result.toString(), type);
        mGetDocumentsData.setDocuments(contactList);

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
        System.out.println("FragmentDocumentChildTab.displayView doc="+mGetDocumentsData.getDocuments().size());
        if (mGetDocumentsData.getDocuments() != null) {
            if (mGetDocumentsData.getDocuments() != null && mGetDocumentsData.getDocuments().size() > 0) {
                documentCardView.setVisibility(View.VISIBLE);
                mMainLayout.setVisibility(View.GONE);
                recyclerView.setAdapter(adapter);
            } else {
                documentCardView.setVisibility(View.GONE);
                mMainLayout.setVisibility(View.VISIBLE);
                String msg = "";
                if (mGetDocumentsData.getMsg() != null) {
                    msg = mGetDocumentsData.getMsg().toString();
                    if (msg.contains("401")) {
                    }
                }
            }
        } else {
            documentCardView.setVisibility(View.GONE);
            mMainLayout.setVisibility(View.VISIBLE);
        }
    }

    protected boolean isDetailsScreenAvailable(FileType fileType) {
        return fileType.equals(FileType.image) || fileType.equals(FileType.pdf)|| fileType.equals(FileType.other);
    }

    protected void launchReportDetails(FileType fileType, String url, String fileName) {
        if (fileType.equals(FileType.image)||fileType.equals(FileType.other)) {
            openReportImage(url, fileName);
        } else if (fileType.equals(FileType.pdf)) {
            launchPdfViewer(url);
        }
    }

    protected void openReportImage(String url, String fileName) {
        if (NetworkCaller.isInternetOncheck(getActivity())) {
            Intent intent = new Intent(getActivity(), ReportsImageActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra(AppConstants.REPORT_URL, url);
            intent.putExtra(AppConstants.REPORT_TITLE, fileName);
            startActivity(intent);
        } else {
            Toast.makeText(getActivity(), getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
        }
    }

    protected void launchPdfViewer(String url) {
        if (url != null) {
            if (NetworkCaller.isInternetOncheck(getActivity())) {

                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setAction(Intent.ACTION_VIEW);
                String googleDocsUrl = "http://docs.google.com/viewer?url=";
                pdfIntent.setDataAndType(Uri.parse(googleDocsUrl + url), "text/html");
                startActivity(pdfIntent);
            } else {
                Toast.makeText(getActivity(), getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static FileType getFileType(String url) {
        String fileExtension = url.substring(url.lastIndexOf("."));
        if (isImageType(fileExtension)) {
            return FileType.image;
        } else if (isPdfType(fileExtension)) {
            return FileType.pdf;
        }
        return FileType.other;
    }

    public static boolean isImageType(String extension) {
        return imageTypes.contains(extension.toLowerCase());
    }

    public static boolean isPdfType(String extension) {
        return pdfExtension.equals(extension.toLowerCase());
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
                            documentCardView.setVisibility(View.GONE);
                            mMainLayout.setVisibility(View.GONE);
                            dbHandler.clearDocuments();
                            Thread thread = new Thread(new FragmentDocumentChildTab.GetDocData());
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

    public void refreshTab() {
        Thread thread = new Thread(new FragmentDocumentChildTab.GetDocData());
        thread.start();
    }

    private class DocumentSaveDb implements Runnable {
        @Override
        public void run() {
            System.out.println("volley check===="+etag+"===="+etag);
            dbHandler.insertDocuments(jsonResponse, customerManager.getCurrentFamilyMember().getIdentification_token(), etag);
        }
    }

    private class GetDocData implements Runnable{

        @Override
        public void run() {
            getAllDocumentsMembers();

        }
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<FragmentDocumentChildTab.RecyclerViewAdapter.MyViewHolder> {

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mTextReportTypeText;
            TextView mTextViewDate;
            ImageView mImageFileType;
            RelativeLayout relativeLayoutContainer;

            public MyViewHolder(View convertView) {
                super(convertView);
                mTextReportTypeText = (TextView) convertView.findViewById(R.id.row_item_fragment_all_document_members_list_name_text);
                mTextViewDate = (TextView) convertView.findViewById(R.id.row_item_fragment_all_document_members_list_date_text);
                mImageFileType = (ImageView) convertView.findViewById(R.id.row_item_fragment_all_document_members_list_image);
                relativeLayoutContainer = (RelativeLayout) convertView.findViewById(R.id.layout_container);
            }
        }

        @Override
        public FragmentDocumentChildTab.RecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_fragment_all_document_memebers_list, parent, false);
            return new FragmentDocumentChildTab.RecyclerViewAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FragmentDocumentChildTab.RecyclerViewAdapter.MyViewHolder holder, final int position)
        {
            try{
                //System.out.println("RecyclerViewAdapter.onBindViewHolder==="+mGetDocumentsData.getDocuments().get(position).getEmr().getUrl());

                String fileUrl = mGetDocumentsData.getDocuments().get(position).getFile_name();
                String mString = "";

                if (mGetDocumentsData.getDocuments().get(position).getFile_name() != null && !mGetDocumentsData.getDocuments().get(position).getFile_name().equalsIgnoreCase(""))
                {
                    mString = mGetDocumentsData.getDocuments().get(position).getFile_name();
                } else
                {
                    mString = "N/A";
                }

                String extension = "";

                if (!fileUrl.equalsIgnoreCase("") && fileUrl.contains("."))
                {
                    extension = fileUrl.substring(fileUrl.lastIndexOf("."));
                } else
                {
                    extension = "";
                }

                if (".jpg".equalsIgnoreCase(extension)) {
                    holder.mImageFileType.setImageResource(R.drawable.ic_image_black_48dp);
                } else if (".pdf".equalsIgnoreCase(extension)) {
                    holder.mImageFileType.setImageResource(R.drawable.ic_picture_as_pdf_black_48dp);
                } else if (".png".equalsIgnoreCase(extension)) {
                    holder.mImageFileType.setImageResource(R.drawable.ic_image_black_48dp);
                } else if (".zip".equalsIgnoreCase(extension)) {
                    holder.mImageFileType.setImageResource(R.drawable.file_zip);
                } else {
                    holder.mImageFileType.setImageResource(R.drawable.file_empty);
                }

                holder.mTextReportTypeText.setText(mString);
                holder.mTextViewDate.setText(DateUtility.getconvertdate(mGetDocumentsData.getDocuments().get(position).getUpdated_at()));

                holder.relativeLayoutContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // String url = mGetDocumentsData.getDocuments().get(position).getEmr().getUrl();
                        String url=TagValues.Get_All_Documents_URL+"/"+mGetDocumentsData.getDocuments().get(position).getId()+"/"+"download";
                        System.out.println("RecyclerViewAdapter.onClick====="+url);
                        FileType fileType;
                        if (url != null) {
                            fileType = getFileType(url);
                            if (isDetailsScreenAvailable(fileType)) {
                                launchReportDetails(fileType, url,
                                        mGetDocumentsData.getDocuments().get(position).getFile_name());
                            } else {
                                customerManager.showAlert("Unsupported File Format", getActivity());
                            }
                        }
                    }
                });
            }catch (NullPointerException e){
                e.printStackTrace();
                /*Intent intent = new Intent(getActivity(), ReportsImageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(AppConstants.REPORT_URL, "YES");
                intent.putExtra(AppConstants.REPORT_TITLE, "TEST");
                startActivity(intent);*/
            }

        }

        @Override
        public int getItemCount() {
            return mGetDocumentsData.getDocuments().size();
        }
    }



}



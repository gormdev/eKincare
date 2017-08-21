package com.ekincare.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DataStorage.DatabaseOverAllHandler;
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
import com.ekincare.util.FloatingButtonAccess;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.oneclick.ekincare.ActivityEditProfile;
import com.oneclick.ekincare.ActivityFamilyMemberAddMedication;
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.helper.ThreadAsyncTask;
import com.oneclick.ekincare.utility.FileUtil;
import com.oneclick.ekincare.utility.FileUtility;
import com.oneclick.ekincare.vo.AllergieDataset;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.ProfileData;
import com.oneclick.ekincare.vo.UploadDocumentData;
import com.openNoteScanner.OpenNoteScannerActivity;
import com.openNoteScanner.helpers.OpenNoteScannerAppConstant;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.google.android.gms.analytics.internal.zzy.a;

/**
 * Created by RaviTejaN on 30-11-2016.
 */

public class FragmentDashboardProfileTest extends Fragment implements FloatingButtonAccess {
    public static final int REQUEST_CODE_TAKE_PICTURE = 100;
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 30;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PICKFILE_RESULT_CODE = 111;
    View rootview;
    com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonProfileEdit;
    com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonAllergies;
    public FloatingActionsMenu floatingActionButtonHistory;
    public FloatingActionsMenu floatingActionButtonDocuments;
    public FloatingActionsMenu floatingActionButtonMedicine;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonCamera, floatingActionButtonGallery;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonUpdateBloodPressure,addMedicine,addPrecaution;
    private com.getbase.floatingactionbutton.FloatingActionButton floatingActionButtonUpdateRandomBlood;
    private CustomerManager customerManager;
    private String strFamilyMemberKey = "";
    private PreferenceHelper prefs;
    UploadDocumentData mUploadDocumentData;
    String mStringFamilyMemberKey;
    private Customer mCustomer;
    private ProfileData mProfileData;
    DatabaseOverAllHandler dbHandler;
    Dialog dialogProgress;

    Dialog dialogAllergy;
    ProfileManager mProfileManager;
    AppCompatEditText allergyNamePost, allergyReasonPost;
    TextView allergyDone,allergyCancel;
    ArrayList<Header> headerList;
    HttpEntity entity;
    private Uri mImageCaptureUri;
    ViewPager viewPagerChild;
    PagerAdapter2 adapter;
    Fragment currentFragment;

    public boolean isNottoast = false;
    public boolean isAlertShow = true;
    protected static List<String> fileTypes = new ArrayList<String>(
            Arrays.asList(".doc", ".docx", ".pdf", ".zip", ".rar"));

    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder build;
    //String uploadDoc;
    //String medicationActivate,allergyActivation;
    //String updateBlood;
    TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.ic_tab_profile,
            R.drawable.ic_tab_timeline,
            R.drawable.ic_tab_medicine,
            R.drawable.ic_tab_documents,
            R.drawable.ic_tab_vaccination,
            R.drawable.ic_tab_allergy
    };

    TextView loginUserProfileName;
    private RelativeLayout relativeLayoutMissingHra;
    private LinearLayout linearLayoutMain;

    public FragmentDashboardProfileTest() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle();
    }

    private void setTitle() {
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        // mToolbar.setTitle("eKincare");
        LinearLayout switchFamilyMemberLayout = (LinearLayout)getActivity().findViewById(R.id.activity_add_family_member_layout);
        switchFamilyMemberLayout.setVisibility(View.VISIBLE);
        loginUserProfileName=(TextView)getActivity().findViewById(R.id.profile_switcher_name);
        loginUserProfileName.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        mCustomer = customerManager.getCurrentFamilyMember();

        if(mCustomer!=null){
            System.out.println("FragmentDashboardProfileTest.onCreate mCustomer="+mCustomer.getFirst_name() + " "+ mCustomer.getBlood_sugar());
        }
        prefs = new PreferenceHelper(getActivity());
        dbHandler = new DatabaseOverAllHandler(getActivity());
        mProfileManager = ProfileManager.getInstance(getActivity());

    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootview = inflater.inflate(R.layout.layout_dashbord_profile_tabs, container, false);

        initilizeView();

        setupViewPager();

        tabLayout.setupWithViewPager(viewPagerChild);
        setupTabIcons();

        currentFragment = adapter.getItem(0);

        viewPagerChild.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerChild.setCurrentItem(tab.getPosition());
                currentFragment = adapter.getItem(tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        floatingActionButtonProfileEdit.setVisibility(View.VISIBLE);
                        floatingActionButtonHistory.setVisibility(View.GONE);
                        floatingActionButtonMedicine.setVisibility(View.GONE);
                        floatingActionButtonDocuments.setVisibility(View.GONE);
                        floatingActionButtonAllergies.setVisibility(View.GONE);
                        break;
                    case 1:
                        floatingActionButtonProfileEdit.setVisibility(View.GONE);
                        floatingActionButtonMedicine.setVisibility(View.GONE);
                        floatingActionButtonDocuments.setVisibility(View.GONE);
                        floatingActionButtonAllergies.setVisibility(View.GONE);
                        if(customerManager.getCurrentFamilyMember().isChild()){
                            floatingActionButtonHistory.setVisibility(View.GONE);
                        }else{
                            floatingActionButtonHistory.setVisibility(View.VISIBLE);
                        }
                        floatingActionButtonMedicine.collapse();
                        floatingActionButtonDocuments.collapse();
                        break;
                    case 2:
                        floatingActionButtonProfileEdit.setVisibility(View.GONE);
                        floatingActionButtonHistory.setVisibility(View.GONE);
                        floatingActionButtonMedicine.setVisibility(View.VISIBLE);
                        floatingActionButtonDocuments.setVisibility(View.GONE);
                        floatingActionButtonAllergies.setVisibility(View.GONE);
                        floatingActionButtonHistory.collapse();
                        floatingActionButtonDocuments.collapse();

                        break;
                    case 3:
                        floatingActionButtonProfileEdit.setVisibility(View.GONE);
                        floatingActionButtonHistory.setVisibility(View.GONE);
                        floatingActionButtonMedicine.setVisibility(View.GONE);
                        floatingActionButtonDocuments.setVisibility(View.VISIBLE);
                        floatingActionButtonAllergies.setVisibility(View.GONE);
                        floatingActionButtonMedicine.collapse();
                        floatingActionButtonHistory.collapse();
                        break;
                    case 4:
                        floatingActionButtonProfileEdit.setVisibility(View.GONE);
                        floatingActionButtonHistory.setVisibility(View.GONE);
                        floatingActionButtonMedicine.setVisibility(View.GONE);
                        floatingActionButtonDocuments.setVisibility(View.GONE);
                        floatingActionButtonAllergies.setVisibility(View.GONE);
                        floatingActionButtonMedicine.collapse();
                        floatingActionButtonHistory.collapse();
                        floatingActionButtonDocuments.collapse();
                        break;
                    case  5:
                        floatingActionButtonProfileEdit.setVisibility(View.GONE);
                        floatingActionButtonHistory.setVisibility(View.GONE);
                        floatingActionButtonMedicine.setVisibility(View.GONE);
                        floatingActionButtonDocuments.setVisibility(View.GONE);
                        floatingActionButtonAllergies.setVisibility(View.VISIBLE);
                        floatingActionButtonMedicine.collapse();
                        floatingActionButtonHistory.collapse();
                        floatingActionButtonDocuments.collapse();
                        break;
                    default:
                        floatingActionButtonProfileEdit.setVisibility(View.VISIBLE);
                        floatingActionButtonHistory.setVisibility(View.GONE);
                        floatingActionButtonMedicine.setVisibility(View.GONE);
                        floatingActionButtonDocuments.setVisibility(View.GONE);
                        floatingActionButtonAllergies.setVisibility(View.GONE);
                        break;

                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        floatingActionButtonProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFloatingActionButtonProfileFunctionality();
            }
        });
        floatingActionButtonAllergies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFloatingActionButtonAllergiesFunctionality();
            }
        });
        floatingActionButtonUpdateBloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBpandBs(true);
            }
        });

        floatingActionButtonUpdateRandomBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBpandBs(false);
            }
        });
        floatingActionButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkCaller.isInternetOncheck(getActivity())) {
                    prefs.setPREF_RUNTIME_PERMISSION("Camera");
                    setFloatingActionButtonCameraFunctionality();
                }else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
                }
            }
        });

        floatingActionButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkCaller.isInternetOncheck(getActivity())) {
                    prefs.setPREF_RUNTIME_PERMISSION("File");
                    setFloatingActionButtonGalleryFunctionality();
                }else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
                }
            }
        });
        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFloatingActionButtonMedicationFunctionality();
            }
        });

        addPrecaution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkCaller.isInternetOncheck(getActivity())) {
                    prefs.setPREF_RUNTIME_PERMISSION("Camera");
                    setFloatingActionButtonCameraFunctionality();
                }else{
                    Toast.makeText(getActivity(),getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
                }
            }
        });

        if(mCustomer!=null &&
                mCustomer.getDate_of_birth()!=null &&
                mCustomer.getAge()!=null &&
                mCustomer.getGender()!=null ){
            relativeLayoutMissingHra.setVisibility(View.GONE);
            linearLayoutMain.setVisibility(View.VISIBLE);
        }else{
            relativeLayoutMissingHra.setVisibility(View.VISIBLE);
            linearLayoutMain.setVisibility(View.GONE);
        }

        setRetainInstance(true);

        return rootview;

    }

    public void tabPosition(){
        tabLayout.getTabAt(0).select();
    }

    private void initilizeView() {

        linearLayoutMain = (LinearLayout)rootview.findViewById(R.id.main_layout);
        relativeLayoutMissingHra = (RelativeLayout)rootview.findViewById(R.id.missing_hra_layout);


        floatingActionButtonProfileEdit = (com.getbase.floatingactionbutton.FloatingActionButton)rootview.findViewById(R.id.fab_profile_edit);
        floatingActionButtonAllergies = (com.getbase.floatingactionbutton.FloatingActionButton)rootview.findViewById(R.id.fab_allergeis_refresh);

        floatingActionButtonHistory = (com.getbase.floatingactionbutton.FloatingActionsMenu)rootview.findViewById(R.id.floating_medical);
        floatingActionButtonUpdateBloodPressure = (com.getbase.floatingactionbutton.FloatingActionButton)rootview.findViewById(R.id.family_menu_update_bp);
        floatingActionButtonUpdateRandomBlood = (com.getbase.floatingactionbutton.FloatingActionButton)rootview.findViewById(R.id.family_menu_update_random_blood_glucose_button);
        floatingActionButtonDocuments = (FloatingActionsMenu)rootview.findViewById(R.id.floating_menu_document);
        floatingActionButtonCamera = (com.getbase.floatingactionbutton.FloatingActionButton)rootview.findViewById(R.id.family_menu_update_old_records_camera);
        floatingActionButtonGallery = (com.getbase.floatingactionbutton.FloatingActionButton)rootview.findViewById(R.id.family_menu_update_old_records_gallery);


        floatingActionButtonMedicine =(FloatingActionsMenu)rootview.findViewById(R.id.floating_menu_medication);
        addMedicine = (com.getbase.floatingactionbutton.FloatingActionButton)rootview.findViewById(R.id.add_medicin_data);
        addPrecaution = (com.getbase.floatingactionbutton.FloatingActionButton)rootview.findViewById(R.id.upload_medicine_precaution);


        viewPagerChild = (ViewPager)rootview.findViewById(R.id.pager_child);
        tabLayout = (TabLayout)rootview.findViewById(R.id.tab_layout);

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
        tabLayout.getTabAt(4).setIcon(tabIcons[4]);
        tabLayout.getTabAt(5).setIcon(tabIcons[5]);
    }

    private void setupViewPager() {
        System.out.println("FragmentDashboardProfileTest.setupViewPager");
        adapter = new PagerAdapter2(getChildFragmentManager());
        adapter.addFrag(new FragmentProfileChildTab(this), "Profile");
        adapter.addFrag(new FragmentTimelineHistoryChildTab(this), "Timeline");
        adapter.addFrag(new FragmentMedicationChildTab(this), "Medication");
        adapter.addFrag(new FragmentDocumentChildTab(this), "Document");
        adapter.addFrag(new FragmentVaccinationChildTab(), "Vaccination");
        adapter.addFrag(new FragmentAllergiesChildTab(this), "Allergies");
        viewPagerChild.setAdapter(adapter);
        viewPagerChild.setOffscreenPageLimit(6);
    }

    private void setFloatingActionButtonMedicationFunctionality() {
        floatingActionButtonMedicine.collapse();
        Intent intent = new Intent(getActivity(), ActivityFamilyMemberAddMedication.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }


    private void setFloatingActionButtonGalleryFunctionality() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                readfileMemory();
            }
        } else {
            readfileMemory();
        }
    }

    public void readfileMemory() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                floatingActionButtonDocuments.collapse();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        }else{
            floatingActionButtonDocuments.collapse();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setType("*/*");
            startActivityForResult(intent, PICKFILE_RESULT_CODE);
        }

    }

    private void setFloatingActionButtonCameraFunctionality() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                prefs.setIsMainDoc(false);
                Intent intent = new Intent(getActivity(), OpenNoteScannerActivity.class);
                startActivity(intent);
            }
        }else{
            prefs.setIsMainDoc(false);
            Intent intent = new Intent(getActivity(), OpenNoteScannerActivity.class);
            startActivity(intent);
        }
        floatingActionButtonDocuments.collapse();
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), CAMERA);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA}, PERMISSION_REQUEST_CODE);
    }


    private void setFloatingActionButtonAllergiesFunctionality() {
        dialogAllergy = new Dialog(getActivity());
        dialogAllergy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAllergy.setContentView(R.layout.dialog_allergy);
        Window window = dialogAllergy.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.DialogAnimationallergy;
        dialogAllergy.setCancelable(true);
        allergyNamePost = (AppCompatEditText) dialogAllergy.findViewById(R.id.allergy_name_post);
        allergyReasonPost = (AppCompatEditText) dialogAllergy.findViewById(R.id.allergy_reason_post);
        final TextInputLayout textInputLayoutName = (TextInputLayout)dialogAllergy.findViewById(R.id.floating_allergy_name);
        final TextInputLayout textInputLayoutReason = (TextInputLayout)dialogAllergy.findViewById(R.id.floating_reason);

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

        allergyDone = (TextView) dialogAllergy.findViewById(R.id.allergy_post_done);
        allergyDone.setText("Done");

        allergyCancel = (TextView) dialogAllergy.findViewById(R.id.cancel);
        allergyCancel.setText("Cancel");

        allergyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAllergy.dismiss();
            }
        });

        allergyDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allergyNamePost.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Invalid Allergy Name", Toast.LENGTH_LONG).show();
                    textInputLayoutName.setError("Empty Allergy");
                } else if (allergyReasonPost.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "Invalid Reason", Toast.LENGTH_LONG).show();
                    textInputLayoutReason.setError("Empty Reason");
                } else {
                    JSONObject json = new JSONObject();
                    try {

                        json.put("name", allergyNamePost.getText().toString());
                        json.put("reaction", allergyReasonPost.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AllergieDataset allergieDataset = new AllergieDataset();
                    allergieDataset.setReaction(allergyReasonPost.getText().toString());
                    allergieDataset.setName(allergyNamePost.getText().toString());
                    System.out.println("check======" + json.toString());

                    if (NetworkCaller.isInternetOncheck(getActivity())) {
                        dialogAllergy.dismiss();
                        Toast.makeText(getActivity(),"Allergy will be added shortly",Toast.LENGTH_LONG).show();
                        allergyRequest(json, allergieDataset);
                    }else{
                        Toast.makeText(getActivity(),getResources().getString(R.string.networkloss),Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

        dialogAllergy.show();
    }

    private void allergyRequest(JSONObject json, final AllergieDataset allergieDataset) {
        String URL = TagValues.Allergi_URL;
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            String id = "";
                            String name = "";
                            String reaction = "";
                            String created_at = "";
                            String updated_at = "";
                            try {
                                id = response.getJSONObject("allergy").get("id").toString();
                                name = response.getJSONObject("allergy").get("name").toString();
                                reaction = response.getJSONObject("allergy").get("reaction").toString();
                                created_at = response.getJSONObject("allergy").get("created_at").toString();
                                updated_at = response.getJSONObject("allergy").get("updated_at").toString();

                                response.getJSONObject("allergy").get("id").toString();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            allergieDataset.setId(id);
                            allergieDataset.setName(name);
                            allergieDataset.setReaction(reaction);
                            allergieDataset.setCreated_at(created_at);
                            allergieDataset.setUpdated_at(updated_at);
                            dbHandler.clearAllergy();
                            hidePDialog();

                            viewPagerChild.setCurrentItem(5);
                            if (currentFragment instanceof FragmentAllergiesChildTab) {
                                ((FragmentAllergiesChildTab) currentFragment).addAllergyData();
                            }
                            Toast.makeText(getActivity(),"Allergy added successfully",Toast.LENGTH_LONG).show();

                            System.out.println("FragmentDashboardProfileTest.onResponse allergies isd====" + id);

                        } else {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Something went wrong,try again.", Toast.LENGTH_LONG).show();
                        hidePDialog();
                    }
                }) {
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

    private void updateBpandBs(final boolean isPressure)
    {
        mCustomer = customerManager.getCurrentFamilyMember();
        System.out.println("FragmentDashboardProfileTest.updateBpandBs mCustomer.getBlood_sugar().getBllod_sugar()="
                +mCustomer.getBlood_sugar().getBllod_sugar());

        floatingActionButtonHistory.collapse();
        final Dialog dialogBpBs = new Dialog(getActivity());
        dialogBpBs.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBpBs.setContentView(R.layout.dailog_update_blood_sugar_may);
        dialogBpBs.setCancelable(true);
        dialogBpBs.setCanceledOnTouchOutside(false);
        dialogBpBs.show();
        final EditText mEditTextSystolic = (EditText) dialogBpBs.findViewById(R.id.systolic);
        final EditText mEditTextDiastolic = (EditText) dialogBpBs.findViewById(R.id.diastolic);
        final EditText mEditTextGlucose = (EditText) dialogBpBs.findViewById(R.id.glucose);

        final TextInputLayout textInputLayoutGlucose,textInputLayoutSystolic,textInputLayoutDistolic;
        textInputLayoutGlucose = (TextInputLayout)dialogBpBs.findViewById(R.id.floating_glucose);
        textInputLayoutSystolic = (TextInputLayout)dialogBpBs.findViewById(R.id.floating_systolic);
        textInputLayoutDistolic = (TextInputLayout)dialogBpBs.findViewById(R.id.floating_diastolic);

        if (isPressure) {
            mEditTextSystolic.setVisibility(View.VISIBLE);
            mEditTextDiastolic.setVisibility(View.VISIBLE);
            mEditTextGlucose.setVisibility(View.GONE);
            textInputLayoutGlucose.setVisibility(View.GONE);
            textInputLayoutSystolic.setVisibility(View.VISIBLE);
            textInputLayoutDistolic.setVisibility(View.VISIBLE);

            if (mCustomer.getSystolic() != null && mCustomer.getSystolic().getResult() != null
                    && !mCustomer.getSystolic().getResult().equals("-")) {
                mEditTextSystolic.setText("" + mCustomer.getSystolic().getResult().toString());
            }
            if (mCustomer.getDiastolic() != null && mCustomer.getDiastolic().getResult() != null
                    && !mCustomer.getDiastolic().getResult().equals("-")) {
                mEditTextDiastolic.setText("" + mCustomer.getDiastolic().getResult().toString());
            }

            ((TextView) dialogBpBs.findViewById(R.id.header_first)).setText("Blood Pressure");

        } else {
            mEditTextSystolic.setVisibility(View.GONE);
            mEditTextDiastolic.setVisibility(View.GONE);
            mEditTextGlucose.setVisibility(View.VISIBLE);
            textInputLayoutGlucose.setVisibility(View.VISIBLE);
            textInputLayoutSystolic.setVisibility(View.GONE);
            textInputLayoutDistolic.setVisibility(View.GONE);

            if (mCustomer.getBlood_sugar() != null && mCustomer.getBlood_sugar().getBllod_sugar() != null
                    && !mCustomer.getBlood_sugar().getBllod_sugar().equals(""))
            {
                mEditTextGlucose.setText("" + mCustomer.getBlood_sugar().getBllod_sugar());
            }
            ((TextView) dialogBpBs.findViewById(R.id.header_first)).setText("Random Blood Glucose");
        }

        ((TextView) dialogBpBs.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                try {
                    String textSystolic = mEditTextSystolic.getText().toString();
                    String textDiastolic = mEditTextDiastolic.getText().toString();
                    String textGlucose = mEditTextGlucose.getText().toString();
                    if (textSystolic.equals("") || textSystolic.equals("-")) {
                        textSystolic = "0";
                    } else if (textDiastolic.equals("") || textDiastolic.equals("-")) {
                        textDiastolic = "0";
                    } else if (textGlucose.equals("") || textGlucose.equals("-")) {
                        textGlucose = "0";
                    }

                    if (isPressure && (mEditTextSystolic.getText().toString().equalsIgnoreCase("")
                            && mEditTextDiastolic.getText().toString().equalsIgnoreCase(""))) {
                        textInputLayoutDistolic.setError("Empty field.");
                        textInputLayoutSystolic.setError("Empty field.");
                        textInputLayoutGlucose.setError("Empty field.");
                    } else if (isPressure && (Double.parseDouble(textSystolic) < 80)) {
                        textInputLayoutSystolic.setError("Systolic  min value 80");
                    } else if (isPressure && (Double.parseDouble(textSystolic) > 200)) {
                        textInputLayoutSystolic.setError("Systolic  max value 200");
                    } else if (isPressure && (Double.parseDouble(textDiastolic) < 60)) {
                        textInputLayoutDistolic.setError("Diastolic  min value 60");
                    } else if (isPressure && (Double.parseDouble(textDiastolic) > 110)) {
                        textInputLayoutDistolic.setError("Diastolic  max value 110");
                    } else if (isPressure && (mEditTextSystolic.getText().toString().equalsIgnoreCase(""))) {
                        textInputLayoutSystolic.setError("Please enter Systolic  value");
                    } else if (isPressure && mEditTextDiastolic.getText().toString().equalsIgnoreCase("")) {
                        textInputLayoutDistolic.setError("Please enter  Diastolic value");
                    } else if (!isPressure && mEditTextGlucose.getText().toString().equalsIgnoreCase("")) {
                        textInputLayoutGlucose.setError("Please enter  Glucose value");
                    } else if (!isPressure && (Double.parseDouble(textGlucose) > 300)) {
                        textInputLayoutGlucose.setError("Glucose max value 300");
                    } else if (!isPressure && (Double.parseDouble(textGlucose) < 50)) {
                        textInputLayoutGlucose.setError("Glucose min value 50");
                    } else {

                        JSONObject object = new JSONObject();
                        JSONObject objectResult = new JSONObject();
                        try {
                            if (isPressure) {
                                objectResult.put("systolic", mEditTextSystolic.getText().toString());
                                objectResult.put("diastolic", mEditTextDiastolic.getText().toString());
                                object.put("blood_pressure", objectResult);
                            } else {
                                objectResult.put("result", mEditTextGlucose.getText().toString());
                                object.put("lab_result", objectResult);
                                //object.put("lonic_code", "2345-7");
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        if (NetworkCaller.isInternetOncheck(getActivity())) {
                            //mRegister = new Register();
                            String url = "";
                            if (isPressure) {
                                url = TagValues.UPDATE_BLOOD_PRESSURE_URL;
                            } else {
                                url = TagValues.UPDATE_GLUCOSE_URL;
                            }
                            System.out.println("FragmentDashboardProfileTest.onClick"+object.toString());

                            oVPageFastingGlucoseUpdateRequest(url, object);

                            dialogBpBs.dismiss();
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(rootview.getWindowToken(), 0);
                            Toast.makeText(getActivity(),"Changes will be updated shortly",Toast.LENGTH_LONG).show();
                            floatingActionButtonHistory.collapse();
                        } else {
                            Toast.makeText(getActivity(), "Internet not available",Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    dialogBpBs.dismiss();
                    Toast.makeText(getActivity(), "Entered data is invalid",Toast.LENGTH_LONG).show();
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootview.getWindowToken(), 0);
                }
            }
        });

        ((TextView) dialogBpBs.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialogBpBs.dismiss();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootview.getWindowToken(), 0);
            }
        });

    }

    private void oVPageFastingGlucoseUpdateRequest(String url, JSONObject object) {
        System.out.println("FragmentDashboardProfileTest.oVPageFastingGlucoseUpdateRequest object.toString()="+object.toString());
        showPDialog();
        JsonObjectRequest jsObjRequesttwo = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (response != null)
                        {
                            System.out.println("FragmentDashboardProfileTest.oVPageFastingGlucoseUpdateRequest respone====" + response.toString());
                            hidePDialog();

                            getRefreshedData();
                        } else {
                            Toast.makeText(getActivity(), TagValues.DATA_NOT_FOUND,Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("FragmentDashboardProfileTest.oVPageFastingGlucoseUpdateRequest error" + error.toString());
                        Toast.makeText(getActivity(),"Failed to update. Please try again later.",Toast.LENGTH_LONG).show();
                        hidePDialog();
                    }
                }) {
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
                params.put("X-HTTP-Method-Override", "PATCH");
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
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequesttwo);
    }

    private void getRefreshedData() {
        System.out.println("FragmentDashboardProfileTest.getRefreshedData");
        if (NetworkCaller.isInternetOncheck(getActivity())) {
            getCustomerDetailRequest();
        } else {
            Toast.makeText(getActivity(), "Internet not available",Toast.LENGTH_LONG).show();
        }
    }

    private void getCustomerDetailRequest() {
        String URL = TagValues.GET_CUSTOMER_DETAIL_URL;
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            System.out.println("FragmentDashboardProfileTest.onResponse" +response.toString());
                            hidePDialog();
                            getCustomerDetailResponse(response);
                        } else {
                            Toast.makeText(getActivity(), TagValues.DATA_NOT_FOUND,Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();
                        Toast.makeText(getActivity(), "Failed to update.Please try later",Toast.LENGTH_LONG).show();
                    }
                }) {
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

    private void getCustomerDetailResponse(JSONObject response) {
        System.out.println("response========="+response.toString());
        mProfileData = new Gson().fromJson(response.toString(), ProfileData.class);
        if (mProfileData.getCustomer() != null)
        {
            try {
                ProfileData mProfileDataNew = mProfileManager.getProfileData();
                List<Customer> familyMembers = new ArrayList<Customer>();

                if (mProfileDataNew != null && mProfileDataNew.getFamily_members() != null)
                {
                    familyMembers = mProfileDataNew.getFamily_members().getMember_list();
                }

                if (customerManager.isLoggedInCustomer() || mProfileDataNew == null)
                {
                    mProfileManager.setProfileData(mProfileData);
                    customerManager.setCurrentFamilyMember(mProfileData.getCustomer());
                } else
                {
                    for (int i = 0; i < familyMembers.size(); i++)
                    {
                        Customer temp = familyMembers.get(i);
                        if (temp.getIdentification_token().equalsIgnoreCase(mProfileData.getCustomer().getIdentification_token()))
                        {
                            familyMembers.set(i, mProfileData.getCustomer());
                            customerManager.setCurrentFamilyMember(mProfileData.getCustomer());
                        }
                    }
                    mProfileManager.setProfileData(mProfileDataNew);
                }

                mCustomer = customerManager.getCurrentFamilyMember();
                dbHandler.clearTimeline();

                viewPagerChild.setCurrentItem(1);
                if (currentFragment instanceof FragmentTimelineHistoryChildTab) {
                    ((FragmentTimelineHistoryChildTab) currentFragment).addTimelineData();
                    Toast.makeText(getActivity(),"Changes updated successfully",Toast.LENGTH_LONG).show();

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;

            }
        }
    }

    private void hidePDialog() {
        if (dialogProgress != null) {
            dialogProgress.dismiss();
            dialogProgress = null;
        }
    }

    private void showPDialog() {
        dialogProgress = new Dialog(getActivity());
        dialogProgress.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialogProgress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProgress.setContentView(R.layout.circular_progressbar);
        dialogProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogProgress.setCancelable(true);
        dialogProgress.setCanceledOnTouchOutside(true);
        dialogProgress.show();
    }


    private void setFloatingActionButtonProfileFunctionality() {
        Intent intent = new Intent(getActivity(), ActivityEditProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }


    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("FragmentDashboardProfileTest.onActivityResult");
        Bitmap mBitmap = null;
        if (resultCode == getActivity().RESULT_OK) {

            isNottoast = false;
            isAlertShow = true;

            switch (requestCode) {
                case PICKFILE_RESULT_CODE:
                    try
                    {
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                Uri uri = item.getUri();
                                String mStringUri = uri.toString();
                                if (mStringUri.contains("content:")) {
                                    if (!FileUtility.isGoogleDriveDocument(uri)) {
                                        String path = FileUtility.getPath(getActivity(), uri);
                                        String extension = null;
                                        Bitmap bitmap = null;
                                        if (path != null) {
                                            if (path != null) {
                                                extension = path.substring(path.lastIndexOf("."));
                                                if (FileUtility.isImageType(extension)) {
                                                    try {
                                                        bitmap = getBitmapFileUpload(uri);
                                                        String fileName =path.substring(path.lastIndexOf("/"));
                                                        File file = savebitmap(bitmap,fileName);
                                                        String url = FileUtil.decodeFile(file.getAbsolutePath(),
                                                                getResources()
                                                                        .getInteger(R.integer.compressedHeightandWidth),
                                                                getResources().getInteger(
                                                                        R.integer.compressedHeightandWidth),
                                                                fileName);
                                                        try {

                                                            entity = getUploadEntity(url, 0, "");
                                                            uploadMultipleReport();

                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                    } catch (IOException e) {
                                                        // block
                                                        e.printStackTrace();
                                                    }
                                                }
                                                if (fileTypes.contains(extension)) {
                                                    entity = getUploadEntity(path, 0, "");
                                                    uploadMultipleReport();
                                                }
                                            }

                                        }
                                    } else if (mStringUri.contains("file:")) {

                                        entity = getUploadEntity(mStringUri, 0, "");
                                        uploadMultipleReport();
                                    }
                                }

                            }
                        } else {

                            Bitmap bitmap = null;
                            String extension = "";
                            if (data != null && data.getDataString() != null) {
                                String dataString = data.getDataString();
                                String path = "";
                                if (dataString.contains("content:")) {
                                    if (!FileUtility.isGoogleDriveDocument(data.getData())) {
                                        path = FileUtility.getPath(getActivity(), data.getData());
                                        if (path != null) {
                                            extension = path.substring(path.lastIndexOf("."));
                                        }
                                        if (FileUtility.isImageType(extension)) {
                                            try
                                            {
                                                bitmap = getBitmapFileUpload(data.getData());
                                                String fileName =path.substring(path.lastIndexOf("/"));
                                                File file = savebitmap(bitmap,fileName);
                                                String url = FileUtil.decodeFile(file.getAbsolutePath(),
                                                        getResources().getInteger(R.integer.compressedHeightandWidth),
                                                        getResources().getInteger(R.integer.compressedHeightandWidth),
                                                        fileName);
                                                try
                                                {
                                                    entity = getUploadEntity(path, 0, url);
                                                    uploadMultipleReport();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            if (fileTypes.contains(extension)) {
                                                entity = getUploadEntity(path, 0, "");
                                                uploadMultipleReport();
                                            } else {
                                                // throw unsupported format
                                                customerManager.showAlert(getResources().getString(R.string.otherformatsnotsupport),
                                                        getActivity());
                                            }
                                        }
                                    } else {
                                        customerManager.showAlert(getResources().getString(R.string.notsupport), getActivity());
                                    }
                                } else if (!FileUtility.isImageType(extension)) {

                                    if (path.contains("file:")) {

                                        String filename = path.substring(path.lastIndexOf("/") + 1);
                                        try {
                                            entity = getUploadEntity(path, 0, filename);
                                            uploadMultipleReport();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }

                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                default:
                    break;
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean readAccept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean camerAccept = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (readAccept && writeAccept && camerAccept) {
                        if(prefs.getPREF_RUNTIME_PERMISSION().equals("Camera")){
                            prefs.setIsMainDoc(false);
                            Intent intent = new Intent(getActivity(), OpenNoteScannerActivity.class);
                            startActivity(intent);
                        }else if(prefs.getPREF_RUNTIME_PERMISSION().equals("File")){
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            intent.setType("*/*");
                            startActivityForResult(intent, PICKFILE_RESULT_CODE);
                        }

                    }else {
                        Toast.makeText(getActivity(), "Permission Denied, You cannot access  camera.", Toast.LENGTH_SHORT).show();

                    }
                }


                break;
        }
    }


    private void uploadDocLogic()
    {
        ArrayList<String> arrayListImages = getActivity().getIntent().getStringArrayListExtra(OpenNoteScannerAppConstant.SCANNED_RESULT_ALL);

        System.out.println("ActivityFamilyMember.uploadDocLogic arrayListImages="+arrayListImages.size());
        if(arrayListImages.size()>0){
            mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            build = new NotificationCompat.Builder(getActivity());
            build.setContentTitle("ekincare medical records")
                    .setContentText("uploading medical record ")
                    .setColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                    .setSmallIcon(R.drawable.notification_logo_all);
            new Download().execute();


            if (arrayListImages != null) {

                for (int count = 0; count < arrayListImages.size(); count++) {

                    if (count == 0) {
                        isNottoast = false;
                        isAlertShow = false;
                    } else {
                        isNottoast = true;
                        isAlertShow = false;
                    }

                    if (count == (arrayListImages.size() - 1)) {
                        isAlertShow = true;
                    }

                    Bitmap bm = null;
                    if (arrayListImages.get(count) != null) {
                        String path = arrayListImages.get(count);
                        if (path != null) {

                            Uri mUri = Uri.parse(path);
                            try {
                                bm = getBitmap(mUri);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }

                            File file = savebitmap(bm,path.substring(path.lastIndexOf("/")));
                            String fileName = path.substring(path.lastIndexOf("/"));

                            try {
                                entity = getUploadEntity(file.getAbsolutePath(), 0, fileName);
                                uploadMultipleReport();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    }

                }

            }
        }else{
            Toast.makeText(getActivity(),"Failed to upload document.",Toast.LENGTH_SHORT).show();
        }

    }

    private Bitmap getBitmap(Uri selectedimg) throws IOException {
        System.out.println("FragmentDashboeard.getBitmap selectedimg="+selectedimg);
        Uri imgUri = Uri.parse("file://"+selectedimg);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        AssetFileDescriptor fileDescriptor = null;
        fileDescriptor = getActivity().getContentResolver().openAssetFileDescriptor(imgUri, "r");
        Bitmap original = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
        //Uri imgUri = Uri.parse("file://"+selectedimg);
        //Bitmap original = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imgUri);
        return original;
    }

    private Bitmap getBitmapFileUpload(Uri selectedimg) throws IOException {
        System.out.println("FragmentProfileDasboardTest.getBitmap selectedimg="+selectedimg);
        //Uri imgUri = Uri.parse("file://"+selectedimg);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        AssetFileDescriptor fileDescriptor = null;
        fileDescriptor = getActivity().getContentResolver().openAssetFileDescriptor(selectedimg, "r");
        Bitmap original = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
        return original;
    }

    private File savebitmap(Bitmap bitmap,String name) {
        System.out.println("FragmentProfileDasboardTest.savebitmap name="+name);
        OutputStream outStream = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destinationFilename = Environment.getExternalStorageDirectory().getPath() + "/eKincare/Images"
                + File.separatorChar + name;
        File file = new File(destinationFilename);
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

    private File savebitmap(Bitmap bitmap) {
        OutputStream outStream = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String destinationFilename = Environment.getExternalStorageDirectory().getPath() + "/eKincare/Images"
                + File.separatorChar + "IMG_" + timeStamp + ".jpg";
        File file = new File(destinationFilename);

        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

    public void uploadMultipleReport() {
        if (NetworkCaller.isInternetOncheck(getActivity())) {
            mUploadDocumentData = new UploadDocumentData();

            getUploadMultipleReportSatus(TagValues.Get_All_Documents_URL, mUploadDocumentData);
        } else {
            //showAlert(getResources().getString(R.string.internet_not_available), MainActivity.this);
            Toast.makeText(getActivity(),"No internet!",Toast.LENGTH_LONG).show();
        }
    }

    public void getUploadMultipleReportSatus(String methodName, Object mObject) {
        try {
            if (this.customerManager.isLoggedInCustomer()) {
                mStringFamilyMemberKey = "";
            } else {
                mStringFamilyMemberKey = this.customerManager.getCurrentFamilyMember().getIdentification_token();
            }

            headerList = new ArrayList<Header>();
            headerList.add(new BasicHeader("X-CUSTOMER-KEY", prefs.getCustomerKey()));
            headerList.add(new BasicHeader("X-EKINCARE-KEY", prefs.getEkinKey()));
            headerList.add(new BasicHeader("X-DEVICE-ID", customerManager.getDeviceID(getActivity())));
            if (!mStringFamilyMemberKey.equalsIgnoreCase(""))
                headerList.add(new BasicHeader("X-FAMILY-MEMBER-KEY", mStringFamilyMemberKey));

            if (!isNottoast) {
                isNottoast = true;
                Toast.makeText(getActivity(), "Your document is being uploaded", Toast.LENGTH_SHORT).show();
            }

            ThreadAsyncTask testImple = new ThreadAsyncTask(listenerUpload, getActivity(), mObject, null, methodName, null, "", false, headerList, entity);
            testImple.execute("");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private ThreadAsyncTask.OnTaskCompleted listenerUpload = new ThreadAsyncTask.OnTaskCompleted() {

        @Override
        public void onTaskCompleted(String method, Object result) {
            if (result != null) {
                mUploadDocumentData = (UploadDocumentData) result;
                if (mUploadDocumentData != null) {

                    if (isAlertShow) {
                        if (mUploadDocumentData.getMessage() != null) {
                            dbHandler.clearDocuments();
                            viewPagerChild.setCurrentItem(3);
                            try {
                                showDialog("Your profile will be updated with the new information within 24 hours and will be notified once it's complete", getResources().getString(R.string.ekinCare));

                                dbHandler.clearDocuments();
                                refreshDocumentData();
                            }catch (Exception e){

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
                }
            } else {
                // showAlert(getResources().getString(R.string.no_documents_available), MainActivity.this);
            }
        }
    };

    private void showDialog(String errorMessage, String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(title);
        builder.setMessage(errorMessage);

        String positiveText = "Okay";
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

    protected HttpEntity getUploadEntity(String fileUrl, int profileId, String fileName) throws Exception {
        System.out.println("ActivityFamilyMember.getUploadEntity fileUrl="+fileUrl);
        File file = new File(fileUrl);
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        entity.addPart("files[]", new FileBody(file));
        entity.addPart("name", new StringBody(file.getName()));
        entity.addPart("id", new StringBody("" + profileId));

        return entity;
    }

    public void refreshProfileData()
    {
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        mCustomer = customerManager.getCurrentFamilyMember();

        if(mCustomer!=null &&
                mCustomer.getDate_of_birth()!=null &&
                mCustomer.getAge()!=null &&
                mCustomer.getGender()!=null ){
            relativeLayoutMissingHra.setVisibility(View.GONE);
            linearLayoutMain.setVisibility(View.VISIBLE);
        }else{
            relativeLayoutMissingHra.setVisibility(View.VISIBLE);
            linearLayoutMain.setVisibility(View.GONE);
        }

        System.out.println("FragmentDashboardProfileTest.refreshProfileData");
        setupViewPager();
        adapter.notifyDataSetChanged();

        viewPagerChild.setCurrentItem(0);

        FragmentProfileChildTab profileChildTab = (FragmentProfileChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 0);
        profileChildTab.refreshProfileData();

        floatingActionButtonProfileEdit.setVisibility(View.VISIBLE);
        floatingActionButtonHistory.setVisibility(View.GONE);
        floatingActionButtonMedicine.setVisibility(View.GONE);
        floatingActionButtonDocuments.setVisibility(View.GONE);
        floatingActionButtonAllergies.setVisibility(View.GONE);
    }

    public void refreshMedicationData() {
        viewPagerChild.setCurrentItem(2);

        FragmentMedicationChildTab fragmentMedicationChildTab = (FragmentMedicationChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 2);
        fragmentMedicationChildTab.refreshTab();

        floatingActionButtonProfileEdit.setVisibility(View.GONE);
        floatingActionButtonHistory.setVisibility(View.GONE);
        floatingActionButtonMedicine.setVisibility(View.VISIBLE);
        floatingActionButtonDocuments.setVisibility(View.GONE);
        floatingActionButtonAllergies.setVisibility(View.GONE);
    }

    public void refreshAllergyData() {
        System.out.println("FragmentDashboardProfileTest.refreshAllergyData");
        viewPagerChild.setCurrentItem(5);
        FragmentAllergiesChildTab fragmentAllergiesChildTab = (FragmentAllergiesChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 5);
        fragmentAllergiesChildTab.refreshTab();

        floatingActionButtonProfileEdit.setVisibility(View.GONE);
        floatingActionButtonHistory.setVisibility(View.GONE);
        floatingActionButtonMedicine.setVisibility(View.GONE);
        floatingActionButtonDocuments.setVisibility(View.GONE);
        floatingActionButtonAllergies.setVisibility(View.VISIBLE);

    }

    public void refreshDocumentData() {
        System.out.println("FragmentDashboardProfileTest.refreshDocumentData");
        viewPagerChild.setCurrentItem(3);

        FragmentDocumentChildTab fragmentDocumentChildTab = (FragmentDocumentChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 3);
        fragmentDocumentChildTab.refreshTab();

        floatingActionButtonProfileEdit.setVisibility(View.GONE);
        floatingActionButtonHistory.setVisibility(View.GONE);
        floatingActionButtonMedicine.setVisibility(View.GONE);
        floatingActionButtonDocuments.setVisibility(View.VISIBLE);
        floatingActionButtonAllergies.setVisibility(View.GONE);

    }

    public void refreshTimelineData() {
        FragmentTimelineHistoryChildTab fragmentTimelineHistoryChildTab = (FragmentTimelineHistoryChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 1);
        fragmentTimelineHistoryChildTab.refreshTab();

        floatingActionButtonProfileEdit.setVisibility(View.GONE);
        floatingActionButtonHistory.setVisibility(View.VISIBLE);
        floatingActionButtonMedicine.setVisibility(View.GONE);
        floatingActionButtonDocuments.setVisibility(View.GONE);
        floatingActionButtonAllergies.setVisibility(View.GONE);
    }

    public void refreshTab(final int position, final Dialog mAlert_Dialog)
    {
        System.out.println("FragmentDashboardProfileTest.refreshTab position="+position);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                viewPagerChild.setCurrentItem(position);
                tabLayout.getTabAt(position).select();
            }
        },30);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                if (mAlert_Dialog != null) {
                    mAlert_Dialog.dismiss();
                }
            }
        },300);


        FragmentProfileChildTab profileChildTab = (FragmentProfileChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 0);
        profileChildTab.refreshProfileData();

        FragmentTimelineHistoryChildTab fragmentTimelineHistoryChildTab = (FragmentTimelineHistoryChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 1);
        fragmentTimelineHistoryChildTab.refreshTab();

        FragmentMedicationChildTab fragmentMedicationChildTab = (FragmentMedicationChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 2);
        fragmentMedicationChildTab.refreshTab();

        FragmentDocumentChildTab fragmentDocumentChildTab = (FragmentDocumentChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 3);
        fragmentDocumentChildTab.refreshTab();

        FragmentVaccinationChildTab fragmentVaccinationChildTab = (FragmentVaccinationChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 4);
        fragmentVaccinationChildTab.refreshTab();

        FragmentAllergiesChildTab fragmentAllergiesChildTab = (FragmentAllergiesChildTab) viewPagerChild.getAdapter().instantiateItem(viewPagerChild, 5);
        fragmentAllergiesChildTab.refreshTab();
    }



    private class Download extends AsyncTask<Void, Integer, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Displays the progress bar for the first time.
            build.setProgress(100, 0, false);
            mNotifyManager.notify(1, build.build());
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // Update progress
            build.setProgress(100, values[0], false);
            mNotifyManager.notify(1, build.build());
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            int i;
            for (i = 0; i <= 100; i += 5) {
                // Sets the progress indicator completion percentage
                publishProgress(Math.min(i, 100));
                try {
                    // Sleep for 5 seconds
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.d("Failure", "sleeping failure");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            build.setContentText("upload medical records complete");
            // Removes the progress bar
            build.setProgress(0, 0, false);
            mNotifyManager.notify(1, build.build());

        }
    }

    @Override
    public FloatingActionButton getAllergyFab() {
        return floatingActionButtonAllergies;
    }

    @Override
    public FloatingActionsMenu getMedicationFab() {
        return floatingActionButtonMedicine;
    }

    @Override
    public FloatingActionsMenu getDocumentFab() {
        return floatingActionButtonDocuments;
    }

    @Override
    public FloatingActionsMenu getMedicalHistory() {
        return floatingActionButtonHistory;
    }

    @Override
    public FloatingActionButton getProfileFab() {
        return floatingActionButtonProfileEdit;
    }

    class PagerAdapter2 extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public PagerAdapter2(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }
}

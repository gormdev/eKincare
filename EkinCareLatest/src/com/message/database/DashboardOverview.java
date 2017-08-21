/*
package com.message.database;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.DataStorage.DatabaseOverAllHandler;
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
import com.ekincare.app.ProfileManager;
import com.ekincare.app.VolleyRequestSingleton;
import com.ekincare.util.DatabaseHandler;
import com.ekincare.util.DateUtility;
import com.ekincare.util.EkinCareSingleButtonDialogFragment;
import com.ekincare.util.TinyDB;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import com.oneclick.ekincare.AdultWizardActivity;
import com.oneclick.ekincare.HowWasYourDayActivity;
import com.oneclick.ekincare.LoginActivity;
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.helper.ThreadAsyncTask;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.FamilyAddresses;
import com.oneclick.ekincare.vo.ProfileData;
import com.oneclick.ekincare.vo.Register;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;




*/
/**
 * Created by jhansi on 31/01/15.
 *//*

public class DashboardOverview extends Fragment implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int PERMISSION_REQUEST_CODE = 200;
    protected DatabaseHandler databaseHandler;
    boolean installed;
    ArrayList<Integer> weeklyStepsList;
    ArrayList<Integer> weeklyCaloriesList;
    ArrayList<String> weeklyStepsDatesList;
    ArrayList<String> weeklyCaloriesDatesList;
    ArrayList<String> weeklyCaloriesDatesListFullFormat;
    ArrayList<String> weeklyStepsDatesListFullFormat;
    ArrayList<Integer> weeklyStepsListPost;
    ArrayList<Integer> weeklyCaloriesListPost;
    ArrayList<String> weeklyStepsDatesListPost;
    ArrayList<String> weeklyCaloriesDatesListPost;
    Integer[] weeklyStepsArray;
    LinearLayout stepsLayoutBar;
    LinearLayout caloriesLayoutBar;
    ImageView refresGgoogleFitData;
    ArrayList<Header> headerList;
    Dialog Alert_Dialog;
    Gson mGson;
    String nfcount;
    TextView gfitdaySteps, gfitCalBurned, gfitDistance;
    LinearLayout googleFitCheckBeforeLayoutView;
    LinearLayout googleFitDataLayout;
    TinyDB tinydb;
    String ekincareKey;
    String customerKey;
    MixpanelAPI mixpanel;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;
    List<Customer> newListMember;
    TextView tellUsdayUpdate;
    TextView recordDigitizeView,uploadDigitizeView,booknowAppointment;
    TextView waterAddButton,waterCardMessageLable;
    TextView recordUploadButton;
    LineChart mChart,mChart1,weightChart;
    ArrayList<String> weeklyDatesSteps1;
    ArrayList<String> weeklyDatesCalories1;
    ArrayList<Integer> weeklyStepsList1;
    ArrayList<Integer> weeklyCaloriesList1;
    ArrayList<Integer> weightList;
    ArrayList<String> weightDateList;
    ProgressBar waterProgressBar;
    TextView textWaterValue;
    int totalWater;
    String s;
    NavigationView  mDrawer;
    DatabaseOverAllHandler dbHandler;
    int weight = 50;
    int totalGlass=0;
    private View view;
    private boolean isWizard = false;
    private TextView connectToGoogleFitlabel;
    private CardView viewProfileListCard,tellUsDayCard,recordDigitiazationcard,uploadRecordCard,
            bookcard,waterCardView, googleFitLayout;
    private ScrollView mScrollViewMain;
    private Dialog mAlert_Dialog;
    private MainActivity mMainActivity;
    private PreferenceHelper prefs;
    private Register mRegister;
    private String strFamilyMemberKey = "";
    private ProfileManager mProfileManager;
    private Customer mCustomer;
    private ThreadAsyncTask testImple;
    private List<Customer> FamilyMemList;
    private boolean isWizardComplete = false;
    private boolean isWizardShowing = false;
    private ProfileData mProfileData;
    private String TAG;
    private GoogleApiClient mGoogleApiClient;
    private List<FamilyAddresses> FamilyAddressList;
    private boolean hasSeenTutorial = false;
    private TextView textViewProfileAddOrView;
    private float waterTarget = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivity = (MainActivity) getActivity();
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        prefs = new PreferenceHelper(mMainActivity);
        System.out.println("Ekincare============="+prefs.getIsFIrstWizard()+"==="+prefs.getEkinKey()+"customer====="+prefs.getCustomerKey()+"==="+customerManager.getDeviceID(mMainActivity));

        dbHandler = new DatabaseOverAllHandler(getActivity());
        mProfileData = new ProfileData();
        //newListMember = new ArrayList<Customer>();
        if (getArguments() != null) {
            ekincareKey=getArguments().getString("ekincareKey");
            customerKey=getArguments().getString("customerKey");
            prefs.setCustomerKey(customerKey);
            prefs.setEkinKey(ekincareKey);
        }

        mGson = new Gson();
        databaseHandler = new DatabaseHandler(mMainActivity);
        mProfileManager = ProfileManager.getInstance(getActivity());
        if(mProfileManager.getProfileData()==null){
            getCustomerDetailRequest();
        }else {
            // mMainActivity.changeProfileName();
        }
        if (mProfileManager.getProfileData() != null && mProfileManager.getProfileData().getFamily_members() != null
                && mProfileManager.getProfileData().getFamily_members().getMember_list() != null) {

            FamilyMemList = mProfileManager.getProfileData().getFamily_members().getMember_list();

            Log.d(TAG, "FROM PROFILE MANAGER SHOW THE FAMILYMEMLIST====================" + FamilyMemList.toString());

        }

        if (customerManager.getCurrentCustomer() == null) {
            customerManager.isWizardShowing = true;
            //call the wizard status service
            // in the listener if wizard is completed, update prefs, show overvidew if data is present. otherwise refresh
            // in the listener if wizard is not completed, show wizard
            mRegister = new Register();
            wizardVolleyRequest();
            // GetWebData(TagValues.WIZARD_STATS_URL, "", null, mRegister, true);
        } else if (prefs.isWizardCompletedFor(customerManager.getCurrentCustomer().getIdentification_token())) {
            mCustomer = mProfileManager.getYouCustomer();
            customerManager.isWizardShowing = false;

        } else if (customerManager.getCurrentCustomer() != null) {
            // mMainActivity.changeProfileName();

        }

        Map<String, Object> eventValue = new HashMap<String, Object>();
        eventValue.put(AFInAppEventParameterName.SUCCESS, "Registration");

        AppsFlyerLib.getInstance().trackEvent(mMainActivity, AFInAppEventType.COMPLETE_REGISTRATION, eventValue);


    }

    @Override
    public void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        setTitle();
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        prefs = new PreferenceHelper(mMainActivity);
        System.out.println("Ekincare============="+prefs.getEkinKey()+"customer====="+prefs.getCustomerKey());

        dbHandler = new DatabaseOverAllHandler(getActivity());
        mProfileData = new ProfileData();
        //newListMember = new ArrayList<Customer>();
        if (getArguments() != null) {
            ekincareKey=getArguments().getString("ekincareKey");
            customerKey=getArguments().getString("customerKey");
            prefs.setCustomerKey(customerKey);
            prefs.setEkinKey(ekincareKey);
        }

        mGson = new Gson();
        databaseHandler = new DatabaseHandler(mMainActivity);
        mProfileManager = ProfileManager.getInstance(getActivity());
        if(mProfileManager.getProfileData()==null){
            getCustomerDetailRequest();
        }else {
            // mMainActivity.changeProfileName();
        }
        if (mProfileManager.getProfileData() != null && mProfileManager.getProfileData().getFamily_members() != null
                && mProfileManager.getProfileData().getFamily_members().getMember_list() != null) {

            FamilyMemList = mProfileManager.getProfileData().getFamily_members().getMember_list();

            Log.d(TAG, "FROM PROFILE MANAGER SHOW THE FAMILYMEMLIST====================" + FamilyMemList.toString());

        }

        if (customerManager.getCurrentCustomer() == null) {
            customerManager.isWizardShowing = true;
            //call the wizard status service
            // in the listener if wizard is completed, update prefs, show overvidew if data is present. otherwise refresh
            // in the listener if wizard is not completed, show wizard
            mRegister = new Register();
            wizardVolleyRequest();
            // GetWebData(TagValues.WIZARD_STATS_URL, "", null, mRegister, true);
        } else if (prefs.isWizardCompletedFor(customerManager.getCurrentCustomer().getIdentification_token())) {
            mCustomer = mProfileManager.getYouCustomer();
            customerManager.isWizardShowing = false;

        } else if (customerManager.getCurrentCustomer() != null) {
            // mMainActivity.changeProfileName();

        }

    }

    private void setTitle(){
        Toolbar mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle("eKincare");
        mDrawer = (NavigationView) getActivity().findViewById(R.id.main_drawer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view == null)
        {
            view = inflater.inflate(R.layout.dash_board_overview_may, container, false);
            installed = appInstalledOrNot("com.google.android.apps.fitness");
            // prefs.setSaveLoc("No");
            prefs.setLocationlatitude("0.0");
            weeklyStepsList = new ArrayList<Integer>();
            weeklyCaloriesList = new ArrayList<Integer>();
            weeklyStepsDatesList = new ArrayList<String>();
            weightList=new ArrayList<Integer>();
            weightDateList=new ArrayList<String>();
            weeklyStepsDatesListFullFormat = new ArrayList<String>();
            weeklyCaloriesDatesListFullFormat = new ArrayList<String>();
            weeklyCaloriesDatesList = new ArrayList<String>();
            tinydb = new TinyDB(mMainActivity);

            if (mMainActivity.deletallNotification.getVisibility() == View.VISIBLE) {
                mMainActivity.deletallNotification.setVisibility(View.GONE);
            } else {
                mMainActivity.deletallNotification.setVisibility(View.GONE);
            }
            mMainActivity.deletallNotification.setVisibility(View.GONE);
            mMainActivity.mNotificationView.setVisibility(View.VISIBLE);
            mMainActivity.switchFamilyMemberLayout.setVisibility(View.VISIBLE);
            mMainActivity.deletallNotification.setVisibility(View.GONE);


            //mMainActivity.mFloatingActionsMenu.setVisibility(View.VISIBLE);
            mixpanel = MixpanelAPI.getInstance(mMainActivity, TagValues.MIXPANEL_TOKEN);


            //people.initPushHan.dling("1006237285130");

            initChildview();

            if (customerManager.getCurrentCustomer() == null) {
                googleFitLayout.setVisibility(View.GONE);
            } else {
                if (installed) {
                    googleFitCheckBeforeLayoutView.setVisibility(View.GONE);
                    googleFitDataLayout.setVisibility(View.VISIBLE);
                    System.out.println("mGoogleApiClient=========="+mGoogleApiClient);
                    if (NetworkCaller.isInternetOncheck(mMainActivity)) {
                        try{
                            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                                    .addApi(Fitness.HISTORY_API)
                                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                                    .addConnectionCallbacks(this)
                                    .enableAutoManage(getActivity(),0,this )
                                    .build();

                        }catch (IllegalStateException e){
                            e.printStackTrace();
                            System.out.println("mGoogleApiClient=========="+"wrong");
                        }


                    } else {
                        try{
                            if (prefs.getStepsCount().length() == 0) {
                                gfitdaySteps.setText("0");
                                gfitCalBurned.setText("0");
                            } else {
                                gfitdaySteps.setText(prefs.getStepsCount());
                                gfitCalBurned.setText(prefs.getCaloriesCount());
                            }
                        }catch (NullPointerException e){
                            e.printStackTrace();
                        }

                    }

                } else {
                    googleFitCheckBeforeLayoutView.setVisibility(View.VISIBLE);
                    googleFitDataLayout.setVisibility(View.GONE);
                }

            }


            if (NetworkCaller.isInternetOncheck(getActivity())) {
                if (prefs.getNotifiCount() != null && !prefs.getNotifiCount().isEmpty() && !prefs.getNotifiCount().equals("null")) {
                    if (prefs.getNotifiCount() == "0") {
                        notificationCountRequest();
                    } else {
                        notificationCountRequest();
                    }

                } else {
                    notificationCountRequest();
                }
            } else {
                mMainActivity.notificationCountValue.setVisibility(View.VISIBLE);
                System.out.println("notification count====="+prefs.getNotifiCount());

                if (prefs.getNotifiCount().equals("0")) {
                    mMainActivity.notificationIconnn.setImageResource(R.drawable.notifications_main_before);
                    mMainActivity.notificationCountValue.setVisibility(View.GONE);
                    mMainActivity.notificationCountValue.setText("");

                } else {
                    */
/*mMainActivity.notificationIconnn.setImageResource(R.drawable.notifications_main);
                    mMainActivity.notificationCountValue.setVisibility(View.VISIBLE);
                    mMainActivity.notificationCountValue.setText(prefs.getNotifiCount());*//*

                }
            }



        } else {
            mMainActivity.deletallNotification.setVisibility(View.GONE);
            System.out.println("-------oncreate view not null");
        }


        hasSeenTutorial = prefs.getHasSeenTutorial();
        if(hasSeenTutorial){
            //do nothing
        }else{
            //setNavigationMenuIntroLogic();
        }

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mixpanel.flush();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("onConnectio=============="+mGoogleApiClient);

        // userSelectAccount();

        new ViewTodaysStepCountTask().execute();
        new ViewWeekStepCountTask().execute();
        new ViewWeekCaloriesCountTask().execute();

        stepsLayoutBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mMainActivity, PedometerGraph.class);
                startActivity(intent);
            }
        });
        caloriesLayoutBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mMainActivity, PedometerGraph.class);
                startActivity(intent);
            }
        });

        refresGgoogleFitData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("checkrefresh onConnectio=========="+"click");

                tinydb = new TinyDB(mMainActivity);
                weeklyStepsListPost = tinydb.getListInt("WeekSteps");
                weeklyCaloriesListPost = tinydb.getListInt("CaloriesWeeklyData");
                weeklyStepsDatesListPost = tinydb.getListString("StepsFullDateFormat");
                weeklyCaloriesDatesListPost = tinydb.getListString("caloriesFullDate");
                JSONObject obj = new JSONObject();
                JSONArray req = new JSONArray();
                try {
                    for (int i = 0; i < weeklyStepsDatesListPost.size(); i++) {
                        JSONObject json = new JSONObject();
                        json.put("date", weeklyStepsDatesListPost.get(i));
                        json.put("count", weeklyStepsListPost.get(i));
                        json.put("calories", weeklyCaloriesListPost.get(i));
                        req.put(json);
                        obj.put("steps", req);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                if (NetworkCaller.isInternetOncheck(mMainActivity)) {
                    googleFitHistoryPost(obj);
                }else {
                    CustomeDialog.dispDialog(mMainActivity, "Internet not available");
                }

            }
        });


    }

    private void userSelectAccount() {

        try {
            if (prefs.getStepsCount().length() == 0) {
                new ViewTodaysStepCountTask().execute();
                new ViewWeekStepCountTask().execute();
                new ViewWeekCaloriesCountTask().execute();

            } else {
                gfitdaySteps.setText(prefs.getStepsCount());
                gfitCalBurned.setText(prefs.getCaloriesCount());
                gfitdaySteps.setText(prefs.getStepsCount());
                gfitCalBurned.setText(prefs.getCaloriesCount());
                final Handler handler = new Handler();
                Timer timer = new Timer();
                TimerTask doAsynchronousTask = new TimerTask() {
                    @Override
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                try {
                                    weeklyCaloriesList.clear();
                                    weeklyStepsDatesList.clear();
                                    weeklyStepsDatesListFullFormat.clear();
                                    weeklyCaloriesDatesListFullFormat.clear();
                                    weeklyStepsList.clear();
                                    weeklyCaloriesDatesList.clear();
                                    new ViewTodaysStepCountTask().execute();
                                    new ViewWeekStepCountTask().execute();
                                    new ViewWeekCaloriesCountTask().execute();
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                }
                            }
                        });
                    }

                };
                timer.schedule(doAsynchronousTask, 0, 1000 * 60 * 180); //execute in every 3 Hours
            }


        } catch (NullPointerException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("onConnectionFailonConnectionSuspended=============="+" ");
    }

    @Override
    public void onConnectionFailed(final ConnectionResult connectionResult) {
        System.out.println("onConnectionFailon=============="+" ");
        userNotSelectAccount();


        refresGgoogleFitData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("checkrefresh onConnectiofail=========="+"click");


                try{
                    mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                            .addApi(Fitness.HISTORY_API)
                            .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                            .addConnectionCallbacks(DashboardOverview.this)
                            .enableAutoManage(getActivity(),0, DashboardOverview.this)
                            //.enableAutoManage(mMainActivity, 0, OverviewPagerItemFragmentTest.this)
                            .build();

                }catch (IllegalStateException e){
                    e.printStackTrace();

                    Toast.makeText(mMainActivity, "Sign in with your Google Account", Toast.LENGTH_SHORT).show();

                }

                //Toast.makeText(mMainActivity, "MyFailure", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void userNotSelectAccount() {
        try{
            if (prefs.getStepsCount().length() == 0) {
                gfitdaySteps.setText("0");
                gfitCalBurned.setText("0");
            } else {
                gfitdaySteps.setText(prefs.getStepsCount());
                gfitCalBurned.setText(prefs.getCaloriesCount());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }



    private void initChildview() {


        initFindview();

        initTellUsDay();
        initDigitizationRecord();
        initUploadRecord();
        initbookAppointment();
        initWaterCardView();
        initViewProfileList();
        initGoogleFit();

        if(prefs.getHasHowWasYourDayDataFilled()){
            System.out.println("dayDone gone======="+prefs.gethowWasDayDone());
            tellUsDayCard.setVisibility(View.GONE);
        }else {
            System.out.println("dayDone visible======="+prefs.gethowWasDayDone());
            tellUsDayCard.setVisibility(View.VISIBLE);
        }
        if(prefs.getIsAnyDigitizedRecords() && prefs.getHasSeenDigitizedRecords()==false){
            recordDigitiazationcard.setVisibility(View.VISIBLE);
        }else{
            recordDigitiazationcard.setVisibility(View.GONE);
        }


    }


    private void initFindview() {

        mScrollViewMain = (ScrollView) view.findViewById(R.id.fragment_overview_main_scrollview);

        viewProfileListCard=(CardView)view.findViewById(R.id.view_profilelist_card_view);
        tellUsDayCard=(CardView)view.findViewById(R.id.tell_us_yourday_card_view);
        recordDigitiazationcard=(CardView)view.findViewById(R.id.record_digitize_card_view);
        uploadRecordCard=(CardView)view.findViewById(R.id.upload_records_card_view);
        bookcard=(CardView)view.findViewById(R.id.book_appointment_card_view);
        waterCardView=(CardView)view.findViewById(R.id.water_card_view);

        googleFitLayout = (CardView) view.findViewById(R.id.google_fit_layout);



    }



    private  void initViewProfileList(){
        View profileview = View.inflate(mMainActivity, R.layout.dash_board_view_profile_test, null);
        TextView textViewProfileTile = (TextView) profileview.findViewById(R.id.add_or_view_profile_title);
        TextView textViewProfileMessage = (TextView) profileview.findViewById(R.id.add_or_view_profile_message);
        textViewProfileAddOrView = (TextView) profileview.findViewById(R.id.add_or_view_family);
        final ImageView imageFamily  = (ImageView)profileview.findViewById(R.id.image_family);
        viewProfileListCard.addView(profileview);
        newListMember = new ArrayList<Customer>();
        newListMember = mProfileManager.getFamilyMembers();
        System.out.println("DashboardOverview.initViewProfileList===="+newListMember.size());
        try{
            prefs.settotalFamilyMemberList(String.valueOf(newListMember.size()));
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }


        if (!customerManager.isLoggedInCustomer()) {
            Iterator<Customer> it = mProfileManager.getFamilyMembers().iterator();
            while(it.hasNext()){
                Customer customer = it.next();
                if (customerManager.getCurrentCustomer().getIdentification_token().equals(customer.getIdentification_token())) {
                    newListMember.remove(it);
                }
            }
            newListMember.add(mProfileManager.getYouCustomer());

        }else if(customerManager.isLoggedInCustomer()){
            mCustomer = mProfileManager.getYouCustomer();
            newListMember.add(0, mCustomer);
        }

        if(newListMember.size()>0){
            textViewProfileTile.setText(getResources().getString(R.string.view_profile_title));
            textViewProfileMessage.setText(getResources().getString(R.string.view_profile_message));
            textViewProfileAddOrView.setText("VIEW");
        }
        else{
            textViewProfileTile.setText(getResources().getString(R.string.add_profile_title));
            textViewProfileMessage.setText(getResources().getString(R.string.add_profile_message));
            textViewProfileAddOrView.setText("ADD");
        }

        System.out.println("DashboardOverview.initViewProfileList newListMember.size="+newListMember.size());

        viewProfileListCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newListMember.size()>0){
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = new Intent(getActivity(),ActivityFamilyMemberList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), (View)imageFamily, "family_image");
                        getActivity().startActivity(intent, options.toBundle());
                        //getActivity().supportFinishAfterTransition();
                    }
                    else{
                        Intent intent = new Intent(getActivity(),ActivityFamilyMemberList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(intent);
                    }
                }else{
                    Intent intent = new Intent(getActivity(),ActivitySelectAgeOfFamilyMemberMay.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);
                }
            }
        });

        textViewProfileAddOrView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newListMember.size()>0){
                    //mMainActivity.showFragmentFamilyMember();

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = new Intent(getActivity(),ActivityFamilyMemberList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), (View)imageFamily, "family_image");
                        getActivity().startActivity(intent, options.toBundle());
                        //getActivity().supportFinishAfterTransition();
                    }
                    else{
                        Intent intent = new Intent(getActivity(),ActivityFamilyMemberList.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        getActivity().startActivity(intent);
                    }

                    */
/*FragmentManager fm = getActivity().getSupportFragmentManager();
                    FragmentFamilyMemberList dialog = new FragmentFamilyMemberList(); // creating new object
                    dialog.show(fm, "dialog");*//*


                }else{
                    Intent intent = new Intent(getActivity(),ActivitySelectAgeOfFamilyMemberMay.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    getActivity().startActivity(intent);

                    //mMainActivity.setUpFamilyAddMemberFragment();
                }
            }
        });
    }



    private void initTellUsDay(){
        View tellUsDay = View.inflate(mMainActivity, R.layout.content_how_was_your_day_test, null);
        tellUsdayUpdate = (TextView) tellUsDay.findViewById(R.id.tell_us_day_update);
        tellUsDayCard.addView(tellUsDay);

        tellUsdayUpdate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mMainActivity, HowWasYourDayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        tellUsDayCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mMainActivity, HowWasYourDayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


    }


    private void initDigitizationRecord()
    {
        System.out.println("prefs of digitization = "+prefs.getIsAnyDigitizedRecords() + prefs.getHasSeenDigitizedRecords());
        if(prefs.getIsAnyDigitizedRecords() && !prefs.getHasSeenDigitizedRecords()){
            recordDigitiazationcard.setVisibility(View.VISIBLE);
        }
        else{
            recordDigitiazationcard.setVisibility(View.GONE);
        }

        View digitalRecordView = View.inflate(mMainActivity, R.layout.record_digitation_card_row, null);
        recordDigitizeView = (TextView) digitalRecordView.findViewById(R.id.record_digitize_view);
        recordDigitiazationcard.addView(digitalRecordView);
        recordDigitizeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout frameContainer = (FrameLayout) getActivity().findViewById(R.id.container);
                ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
                TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
                frameContainer.setVisibility(View.VISIBLE);
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                recordDigitiazationcard.setVisibility(View.GONE);
                //mMainActivity.replacesFragment(new FragmentTimeLineNewMembersTest(),true,null);
                prefs.setHasSeenDigitizedRecords(true);
                prefs.setIsAnyDigitizedRecords(false);
            }
        });
    }

    private void initbookAppointment(){
        View bookView = View.inflate(mMainActivity, R.layout.book_appointment_card_row_test, null);
        booknowAppointment = (TextView) bookView.findViewById(R.id.book_now_button);
        bookcard.addView(bookView);
        booknowAppointment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mMainActivity, PackageActivity.class);
                if(prefs.getSaveLoc().equals("YES")){
                    intent.putExtra("SHOW","HIDE");
                }else{
                    intent.putExtra("SHOW","DISPLAY");
                }
                startActivity(intent);

            }
        });

        bookcard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mMainActivity, PackageActivity.class);
                if(prefs.getSaveLoc().equals("YES")){
                    intent.putExtra("SHOW","HIDE");
                }else{
                    intent.putExtra("SHOW","DISPLAY");
                }
                startActivity(intent);

            }
        });
    }


    private void initUploadRecord(){
        View uploadRecordView = View.inflate(mMainActivity, R.layout.upload_record_card_row_test, null);
        uploadDigitizeView = (TextView) uploadRecordView.findViewById(R.id.upload_record_button);
        uploadRecordCard.addView(uploadRecordView);

        recordUploadButton = (TextView) uploadRecordView.findViewById(R.id.upload_record_button);
        recordUploadButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                */
/*if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkPermission()) {
                        requestPermission();
                    } else {
                        startActivityForResult(new Intent(getActivity(), ScanActivity.class), SCAN_PICTURE_REQUEST_B);
                    }
                    //checkCamerPermission();
                } else {
                    System.out.println("MainActivity.onClick========iscallingscanactivity");
                    startActivityForResult(new Intent(getActivity(), ScanActivity.class), SCAN_PICTURE_REQUEST_B);

                }*//*

                mMainActivity.uploadDialog();

            }
        });

        uploadRecordCard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainActivity.uploadDialog();

            }
        });
    }

    private void initWaterCardView(){
        View waterview = View.inflate(mMainActivity, R.layout.water_card_view_row_test, null);
        waterAddButton = (TextView) waterview.findViewById(R.id.water_addbutton);
        waterCardMessageLable=(TextView)waterview.findViewById(R.id.water_card_view_message);

        waterCardView.addView(waterview);
        s=prefs.gettotalUpdateWater();


        waterProgressBar = (ProgressBar)waterview.findViewById(R.id.water_progress_bar);

        textWaterValue = (TextView) waterview.findViewById(R.id.water_value_text);

        if(prefs.gettotalUpdateWater().equals("")){

            waterProgressBar.setProgress(0);
        }else{
            waterProgressBar.setProgress(Integer.parseInt(prefs.gettotalUpdateWater()));
        }
        try {
            weight = Integer.parseInt(customerManager.getCurrentCustomer().getCustomer_vitals().getWeight());
            System.out.println("DashboardOverview.bindData===="+"exception NO");

        } catch (Exception e) {
            System.out.println("DashboardOverview.bindData===="+"exception");
            weight = 50;
        }
        waterTarget = (int) weight / 0.024f;
        totalGlass=(int)waterTarget/500;
        System.out.println("DashboardOverview.bindData========"+totalGlass);
        System.out.println("DashboardOverview.bindData========"+waterTarget);
        waterCardMessageLable.setText(Html.fromHtml("<font color=#000000>"+"Drink"+" "+totalGlass+" "+"glasses "+"("+ (int) waterTarget+" ml"+")"+" of water every day."+"</font>"));
        if(prefs.gettotalUpdateWater().equals("")){
            textWaterValue.setText("0"+"/"+(int) waterTarget+" ml");
        }else{
            textWaterValue.setText(Integer.parseInt(prefs.gettotalUpdateWater()) + "/"+(int) waterTarget+" ml");
        }



        waterCardView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                updateWaterGlassReding();
                System.out.println("afterdialog===="+prefs.getupdateWater());
            }
        });

        waterAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                updateWaterGlassReding();
                System.out.println("afterdialog===="+prefs.getupdateWater());
            }
        });
    }

    private void updateWaterGlassReding() {
        final LinearLayout onestepWater;
        final LinearLayout twostepWater;
        final LinearLayout threestepWater;
        final LinearLayout fourstepWater;
        String updateWaterValu;


        Alert_Dialog = new Dialog(mMainActivity);
        Alert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Alert_Dialog.setContentView(R.layout.dialog_update_water);
        Window window = Alert_Dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        Alert_Dialog.setCancelable(false);
        Alert_Dialog.setCanceledOnTouchOutside(true);
        Alert_Dialog.show();
        onestepWater=(LinearLayout)Alert_Dialog.findViewById(R.id.glass_150_view);
        twostepWater=(LinearLayout)Alert_Dialog.findViewById(R.id.glass_250_view);
        threestepWater=(LinearLayout)Alert_Dialog.findViewById(R.id.glass_350_view);
        fourstepWater=(LinearLayout)Alert_Dialog.findViewById(R.id.glass_500_view);
        onestepWater.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setupdateWater("150");
                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateimageView(prefs.getupdateWater());
                    }
                });
                Alert_Dialog.dismiss();
            }
        });
        twostepWater.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setupdateWater("250");
                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateimageView(prefs.getupdateWater());
                    }
                });
                Alert_Dialog.dismiss();
            }
        });
        threestepWater.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setupdateWater("350");
                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateimageView(prefs.getupdateWater());
                    }
                });
                Alert_Dialog.dismiss();
            }
        });

        fourstepWater.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.setupdateWater("500");
                mMainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateimageView(prefs.getupdateWater());
                    }
                });
                Alert_Dialog.dismiss();
            }
        });

    }

    private void updateimageView(String ss) {
        if(prefs.gettotalUpdateWater().equalsIgnoreCase("")){
            totalWater=Integer.parseInt(ss);
            prefs.settotalUpdateWater(Integer.toString(totalWater));

        }else{
            totalWater=Integer.parseInt(prefs.gettotalUpdateWater())+Integer.parseInt(ss);
            prefs.settotalUpdateWater(Integer.toString(totalWater));
        }
        prefs.settotalUpdateWater(Integer.toString(totalWater));
        s=Integer.toString(totalWater);
        System.out.println("checkvalue========="+totalWater);

        waterProgressBar.setProgress(totalWater);
        textWaterValue.setText(totalWater + "/"+ (int) waterTarget+" ml");

    }

    private void initGoogleFit() {
        tinydb = new TinyDB(getActivity());
        weeklyStepsList1 = tinydb.getListInt("WeekSteps");
        weeklyCaloriesList1 = tinydb.getListInt("CaloriesWeeklyData");
        weeklyDatesSteps1 = tinydb.getListString("Dayss");
        weeklyDatesCalories1 = tinydb.getListString("DayssCalories");

        View gfitView = View.inflate(mMainActivity, R.layout.check_google_fit_install_test, null);
        connectToGoogleFitlabel = (TextView) gfitView.findViewById(R.id.connet_to_google_fit_view);
        googleFitCheckBeforeLayoutView = (LinearLayout) gfitView.findViewById(R.id.google_fit_check_before_layout);
        googleFitDataLayout = (LinearLayout) gfitView.findViewById(R.id.google_fit_data_layout);
        gfitdaySteps = (TextView) gfitView.findViewById(R.id.today_steps_count_value);
        gfitDistance = (TextView) gfitView.findViewById(R.id.today_distance_travelled);
        gfitCalBurned = (TextView) gfitView.findViewById(R.id.today_calories_burned_value);
        refresGgoogleFitData = (ImageView) gfitView.findViewById(R.id.refresh_google_fitData);
        stepsLayoutBar = (LinearLayout) gfitView.findViewById(R.id.setp_layout_bar);
        caloriesLayoutBar = (LinearLayout) gfitView.findViewById(R.id.calories_layout_bar);
        mChart1 = (LineChart)gfitView.findViewById(R.id.chart2);
        mChart1.setDescription("");
        mChart1.setTouchEnabled(false);
        mChart1.setDragEnabled(false);
        mChart1.setScaleEnabled(false);
        mChart1.setPinchZoom(false);
        mChart1.setDrawGridBackground(false);
        XAxis x1 = mChart1.getXAxis();
        x1.setEnabled(false);
        YAxis y1 = mChart1.getAxisLeft();
        y1.setEnabled(false);
        mChart1.getAxisRight().setEnabled(false);
        setDataCal();
        mChart1.getLegend().setEnabled(false);
        mChart1.animateXY(1000, 1000);
        mChart1.getAxisRight().setEnabled(false);
        List<LineDataSet> sets1 = mChart1.getData()
                .getDataSets();

        for (LineDataSet iSet : sets1) {

            LineDataSet set = (LineDataSet) iSet;
            if (set.isDrawCirclesEnabled())
                set.setDrawCircles(false);
            else
                set.setDrawCircles(true);
        }

        mChart1.invalidate();

        mChart = (LineChart)gfitView.findViewById(R.id.chart1);
        mChart.setDescription("");
        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        XAxis x = mChart.getXAxis();
        x.setEnabled(false);
        YAxis y = mChart.getAxisLeft();
        y.setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        setData();
        mChart.getLegend().setEnabled(false);
        mChart.animateXY(1000, 1000);
        mChart.getAxisRight().setEnabled(false);
        List<LineDataSet> sets = mChart.getData()
                .getDataSets();

        for (LineDataSet iSet : sets) {

            LineDataSet set = (LineDataSet) iSet;
            if (set.isDrawCirclesEnabled())
                set.setDrawCircles(false);
            else
                set.setDrawCircles(true);
        }
        mChart.invalidate();
        //adapter = new RandomizedAdapter();
        googleFitLayout.addView(gfitView);



        googleFitCheckBeforeLayoutView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (installed) {
                    //This intent will help you to launch if the package is already installed
                    Intent LaunchIntent = mMainActivity.getPackageManager()
                            .getLaunchIntentForPackage("com.google.android.apps.fitness");
                    startActivity(LaunchIntent);

                } else {
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness&hl=en"));
                    startActivity(i);
                }
            }
        });



        connectToGoogleFitlabel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (installed) {
                    //This intent will help you to launch if the package is already installed
                    Intent LaunchIntent = mMainActivity.getPackageManager()
                            .getLaunchIntentForPackage("com.google.android.apps.fitness");
                    startActivity(LaunchIntent);

                } else {
                    Intent i = new Intent(android.content.Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.fitness&hl=en"));
                    startActivity(i);
                }
            }
        });

        userSelectAccount();
        */
/* tinydb = new TinyDB(mMainActivity);
                weeklyStepsListPost = tinydb.getListInt("WeekSteps");
                if (weeklyStepsListPost != null && !weeklyStepsListPost.isEmpty()) {
                    System.out.println("DashboardOverview.run========="+ weeklyStepsListPost.get(weeklyStepsListPost.size()-1));
                    gfitdaySteps.setText(String.valueOf(weeklyStepsListPost.get(weeklyStepsListPost.size()-1)));
                }
*//*


        stepsLayoutBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mMainActivity, PedometerGraph.class);
                startActivity(intent);
            }
        });
        caloriesLayoutBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mMainActivity, PedometerGraph.class);
                startActivity(intent);
            }
        });

        refresGgoogleFitData.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("checkrefresh ui=========="+"click");

                tinydb = new TinyDB(mMainActivity);
                weeklyStepsListPost = tinydb.getListInt("WeekSteps");
                weeklyCaloriesListPost = tinydb.getListInt("CaloriesWeeklyData");
                weeklyStepsDatesListPost = tinydb.getListString("StepsFullDateFormat");
                weeklyCaloriesDatesListPost = tinydb.getListString("caloriesFullDate");


                gfitdaySteps.setText(prefs.getStepsCount());
                gfitCalBurned.setText(prefs.getCaloriesCount());


                */
/*if (weeklyStepsListPost != null && !weeklyStepsListPost.isEmpty()) {
                    gfitdaySteps.setText(String.valueOf(weeklyStepsListPost.get(weeklyStepsListPost.size()-1)));

                }

                if (weeklyCaloriesListPost != null && !weeklyCaloriesListPost.isEmpty()) {
                    gfitCalBurned.setText(String.valueOf(weeklyCaloriesListPost.get(weeklyCaloriesListPost.size()-1)));

                }*//*


                JSONObject obj = new JSONObject();
                JSONArray req = new JSONArray();
                try {
                    for (int i = 0; i < weeklyStepsDatesListPost.size(); i++) {
                        JSONObject json = new JSONObject();
                        json.put("date", weeklyStepsDatesListPost.get(i));
                        json.put("count", weeklyStepsListPost.get(i));
                        json.put("calories", weeklyCaloriesListPost.get(i));
                        req.put(json);
                        obj.put("steps", req);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }
                if (NetworkCaller.isInternetOncheck(mMainActivity)) {
                    googleFitHistoryPost(obj);
                }else {
                    CustomeDialog.dispDialog(mMainActivity, "Internet not available");
                }

            }
        });



    }


    private void setDataCal() {
        Integer[] values = weeklyCaloriesList1.toArray(new Integer[weeklyCaloriesList1.size()]);

        ArrayList<Entry> valueSet1 = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            valueSet1.add(new Entry(values[i], i));
        }
        String[] labels2 = weeklyDatesCalories1.toArray(new String[weeklyDatesCalories1.size()]);
        ArrayList<String> xAxis = new ArrayList<String>();
        for (int i = 0; i < labels2.length; i++) {
            xAxis.add(labels2[i]);
        }


        LineDataSet set1;
        if (mChart1.getData() != null &&
                mChart1.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart1.getData().getDataSetByIndex(0);
          */
/*  set1.setYVals(yVals);
            mChart.getData().setXVals(xVals);
          *//*
  mChart1.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(valueSet1, "Weekly calories");
            set1.setCubicIntensity(0.2f);
            set1.setLineWidth(2.8f);
            set1.setColor(getResources().getColor(R.color.steps_graph_color));
            set1.setFillAlpha(100);
            set1.setDrawFilled(true);
            set1.setFillColor(getResources().getColor(R.color.calories_fill_color));

            LineData data = new LineData(xAxis, set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            mChart1.setData(data);
        }

    }


    private void setData() {

        System.out.println("OverviewPagerItemFragment.setData==========iscalling");
        Integer[] values = weeklyStepsList1.toArray(new Integer[weeklyStepsList1.size()]);

        ArrayList<Entry> valueSet1 = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            valueSet1.add(new Entry(values[i], i));
        }


        String[] labels2 = weeklyDatesSteps1.toArray(new String[weeklyDatesSteps1.size()]);
        ArrayList<String> xAxis = new ArrayList<String>();
        for (int i = 0; i < labels2.length; i++) {
            xAxis.add(labels2[i]);
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet)mChart.getData().getDataSetByIndex(0);
          */
/*  set1.setYVals(yVals);
            mChart.getData().setXVals(xVals);
          *//*
  mChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(valueSet1, "DataSet 1");
            set1.setCubicIntensity(0.2f);
            set1.setLineWidth(2.8f);
            set1.setColor(getResources().getColor(R.color.steps_graph_color));
            set1.setFillAlpha(100);
            set1.setDrawFilled(true);
            set1.setFillColor(getResources().getColor(R.color.calories_fill_color));

            LineData data = new LineData(xAxis, set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            mChart.setData(data);
        }
    }


    private void googleFitHistoryPost(JSONObject json) {

        System.out.println("json steps========"+json.toString());
        String URL=TagValues.GOOGLE_FIT_HISTORY;
        showPDialog();
        JsonObjectRequest jsonPostGoogleFit = new JsonObjectRequest(Request.Method.POST,URL,json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("steps otp===="+response.toString());
                            hidePDialog();
                            googleFitHistoryResposne(response);
                        }else{
                            CustomeDialog.dispDialog(mMainActivity, TagValues.DATA_NOT_FOUND);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hidePDialog();

                    }
                }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(mMainActivity));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonPostGoogleFit);

    }

    private void googleFitHistoryResposne(JSONObject response) {


        weeklyStepsDatesListPost.clear();
        weeklyCaloriesList.clear();
        weeklyStepsDatesList.clear();
        weeklyStepsDatesListFullFormat.clear();
        weeklyCaloriesDatesListFullFormat.clear();
        weeklyStepsList.clear();
        weeklyCaloriesDatesList.clear();

        if (mGoogleApiClient != null) {
            new ViewTodaysStepCountTask().execute();
            new ViewWeekStepCountTask().execute();
            new ViewWeekCaloriesCountTask().execute();

        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = mMainActivity.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }

    private void showPDialog() {
        mAlert_Dialog = new Dialog(mMainActivity);
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




    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();


        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!getUserVisibleHint()) {
            return;
        } else {
            System.out.println("DashboardOverview.onResume==="+prefs.getCustomerKey()+"==="+prefs.getEkinKey()+"==="+
                    customerManager.getDeviceID(getActivity()));

            if(prefs.getIsFIrstWizard()==false){
                if (NetworkCaller.isInternetOncheck(mMainActivity)) {
                    mRegister = new Register();
                    wizardVolleyRequest();
                } else {
                    CustomeDialog.dispDialog(mMainActivity, "Internet not available");
                }
            }else{
                InputMethodManager imm = (InputMethodManager) mMainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                mCustomer = customerManager.getCurrentCustomer();
                bindData(false);

            }


        }
    }

    private void wizardVolleyRequest() {
        String URL = TagValues.WIZARD_STATS_URL ;
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){

                            System.out.println("login verify wizard===="+response.toString());
                            hidePDialog();
                            wizardVolleyResponse(response);
                        }else{
                            CustomeDialog.dispDialog(mMainActivity, TagValues.DATA_NOT_FOUND);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

                if (customerManager.isLoggedInCustomer()) {
                    strFamilyMemberKey = "";
                } else {
                    strFamilyMemberKey = customerManager.getCurrentCustomer().getIdentification_token();
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(mMainActivity));
                if (!strFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    private void wizardVolleyResponse(JSONObject response) {
        Register register = new Gson().fromJson(response.toString(), Register.class);
        String wizardStatus = register.getStatus();
        System.out.println("DashboardOverview.wizardVolleyResponse======"+wizardStatus);
        if ("6".equalsIgnoreCase(wizardStatus) || "0".equalsIgnoreCase(wizardStatus)) {
            if (mCustomer != null) {
                prefs.setWizardCompleteFor(mCustomer.getIdentification_token(), true);
            } else {
                prefs.setWizardCompleteFor(prefs.getCustomerKey(), true);
                weeklyStepsList.clear();
                weeklyCaloriesList.clear();
                weeklyCaloriesDatesList.clear();
                weeklyStepsDatesList.clear();
                getRefreshedData();
            }
        } else {
            Intent adult = new Intent(getContext(), AdultWizardActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("isFrom", "Register");
            bundle.putInt("question", Integer.parseInt(wizardStatus));
            bundle.putString("FamilyMemberKey", "");
            adult.putExtras(bundle);
            startActivity(adult);
        }
    }


    */
/**
     * Gets the latest customer data from the server.
     *//*

    public void getRefreshedData() {

        if (NetworkCaller.isInternetOncheck(mMainActivity)) {
            mProfileData = new ProfileData();

            getCustomerDetailRequest();
            // GetWebData(TagValues.GET_CUSTOMER_DETAIL_URL, "", null, mProfileData, true);
        } else {
            CustomeDialog.dispDialog(mMainActivity, "Internet not available");
        }

    }

    private void getCustomerDetailRequest() {
        String URL =TagValues.GET_CUSTOMER_DETAIL_URL;
        showPDialog();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET,URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("login verify===="+response.toString());

                            hidePDialog();
                            getCustomerDetailResponse(response);
                        }else{
                            CustomeDialog.dispDialog(mMainActivity, TagValues.DATA_NOT_FOUND);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

                if (customerManager.isLoggedInCustomer()) {
                    strFamilyMemberKey = "";
                } else {
                    strFamilyMemberKey = customerManager.getCurrentCustomer().getIdentification_token();
                }
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(mMainActivity));
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

        mProfileData = new Gson().fromJson(response.toString(), ProfileData.class);
        System.out.println("getCustomer========="+mProfileData.getCustomer()+"==="+mProfileData.getCustomer().getBlood_sugar());
        if (mProfileData.getCustomer() != null && mProfileData.getCustomer().getBlood_sugar() != null) {

            try {
                Gson mGson = new Gson();
                //mProfileManager.setBasicCustomer(mProfileData.getCustomer());

                ProfileData mProfileDataNew = mProfileManager.getProfileData();
                List<Customer> familyMembers = new ArrayList<Customer>();

                if (mProfileDataNew != null && mProfileDataNew.getFamily_members() != null) {
                    familyMembers = mProfileDataNew.getFamily_members().getMember_list();
                }

                if (customerManager.isLoggedInCustomer() || mProfileDataNew == null) {

                    mProfileManager.setProfileData(mProfileData);

                               */
/* prefs.setProfileData(mGson.toJson(mProfileData));
                                prefs.setYouCustomer(mGson.toJson(mProfileData.getCustomer()));*//*

                    customerManager.setCurrentCustomer(mProfileData.getCustomer());
                    //mMainActivity.changeProfileName();
                } else {
                    for (int i = 0; i < familyMembers.size(); i++) {
                        Customer temp = familyMembers.get(i);
                        if (temp.getIdentification_token().equalsIgnoreCase(mProfileData.getCustomer().getIdentification_token())) {
                            familyMembers.set(i, mProfileData.getCustomer());
                            customerManager.setCurrentCustomer(mProfileData.getCustomer());
                            Log.d(TAG, "found");
                        }
                    }
                    mProfileManager.setProfileData(mProfileDataNew);
                    // prefs.setProfileData(mGson.toJson(mProfileDataNew));
                }


                mCustomer = customerManager.getCurrentCustomer();
                bindData(true);
                if (!isWizardComplete) {

                    if (mProfileData.getCustomer().getBlood_sugar().getBllod_sugar() != null
                            && !mProfileData.getCustomer().getBlood_sugar().getBllod_sugar()
                            .equalsIgnoreCase("")) {

                    } else {
                        // TODO callwebservice here
                        if (NetworkCaller.isInternetOncheck(mMainActivity)) {

                            mRegister = new Register();
                            wizardVolleyRequest();
                            // GetWebData(TagValues.WIZARD_STATS_URL, "", null, mRegister, true);
                        } else {
                            CustomeDialog.dispDialog(mMainActivity, "Internet not available");
                        }
                    }
                }

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
                    Intent i=new Intent(mMainActivity,LoginActivity.class);
                    startActivity(i);
                }
            }
        }
    }

    private void bindData(boolean isRefresh) {

        if (mCustomer != null) {
            FamilyAddressList = customerManager.getCurrentCustomer().getAddresses();
            try {
                weight = Integer.parseInt(customerManager.getCurrentCustomer().getCustomer_vitals().getWeight());
                System.out.println("DashboardOverview.bindData===="+"exception NO");

            } catch (Exception e) {
                System.out.println("DashboardOverview.bindData===="+"exception");
                weight = 50;
            }
            waterTarget = (int) weight / 0.024f;
            totalGlass=(int)waterTarget/500;
            System.out.println("DashboardOverview.bindData========"+totalGlass);
            System.out.println("DashboardOverview.bindData========"+waterTarget);
            waterCardMessageLable.setText(Html.fromHtml("<font color=#000000>"+"Drink"+" "+totalGlass+" "+"glasses "+"("+ (int) waterTarget+" ml"+")"+" of water every day."+"</font>"));
            if(prefs.gettotalUpdateWater().equals("")){
                textWaterValue.setText("0"+"/"+(int) waterTarget+" ml");
            }else{
                textWaterValue.setText(Integer.parseInt(prefs.gettotalUpdateWater()) + "/"+(int) waterTarget+" ml");
            }

            if(FamilyAddressList.isEmpty()){


            }else if(FamilyAddressList.size()==1){
                prefs.setSaveGoogleLocAddress(FamilyAddressList.get(0).getLine1() + "<br />" + FamilyAddressList.get(0).getLine2() + "<br />" + FamilyAddressList.get(0).getCity() + FamilyAddressList.get(0).getState() + "<br/>" + FamilyAddressList.get(0).getCountry() + FamilyAddressList.get(0).getZip_code());
                prefs.setSaveLoc("YES");
                prefs.setSaveLatitude(FamilyAddressList.get(0).getLatitude());
                prefs.setSavelongitude(FamilyAddressList.get(0).getLongitude());
            }else  if(FamilyAddressList.size()>1){
                prefs.setSaveGoogleLocAddress(FamilyAddressList.get(FamilyAddressList.size()-1).getLine1() + "<br />" + FamilyAddressList.get(FamilyAddressList.size()-1).getLine2() + "<br />" + FamilyAddressList.get(FamilyAddressList.size()-1).getCity() + FamilyAddressList.get(FamilyAddressList.size()-1).getState() + "<br/>" + FamilyAddressList.get(FamilyAddressList.size()-1).getCountry() + FamilyAddressList.get(FamilyAddressList.size()-1).getZip_code());
                prefs.setSaveLoc("YES");
                prefs.setSaveLatitude(FamilyAddressList.get(FamilyAddressList.size()-1).getLatitude());
                prefs.setSavelongitude(FamilyAddressList.get(FamilyAddressList.size()-1).getLongitude());
            }

            try{
                System.out.println("DashboardOverview.bindData============"+mProfileData.getCustomer().getMobile_number());

                mixpanel.timeEvent("DashboardOverviewPage");

                prefs.setCustomerName(mCustomer.getFirst_name()+mCustomer.getLast_name());

                JSONObject props = new JSONObject();
                props.put("LoginName", mCustomer.getFirst_name()+mCustomer.getLast_name());
                props.put("LoginNumber",prefs.getUserName());
                mixpanel.track("DashboardOverviewPage", props);

            }catch (NullPointerException e){

            } catch (JSONException e) {
                Log.e("MYAPP", "Unable to add properties to JSONObject", e);
            }

        } else {

        }
        pushNotificationToken();
    }

    private void pushNotificationToken() {

        String regId = prefs.getRegistrationId();

        if (mProfileManager.getYouCustomer() != null && !prefs.getRegistrationId().equals("") && !prefs.isSentTokenFor(mProfileManager.getYouCustomer().getIdentification_token())) {


            if (NetworkCaller.isInternetOncheck(getActivity())) {
                Register mRegister = new Register();
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
        showPDialog();
        JsonObjectRequest jsObjRequesttwo = new JsonObjectRequest(Request.Method.POST,URL,object2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if(response!=null){
                            System.out.println("login verify otp===="+response.toString());
                            hidePDialog();
                            prefs.sentTokenFor(mProfileManager.getYouCustomer().getIdentification_token());
                        } else {
                            CustomeDialog.dispDialog(mMainActivity, TagValues.DATA_NOT_FOUND);
                        }



                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                params.put("X-DEVICE-ID", customerManager.getDeviceID(mMainActivity));
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequesttwo);

    }

    protected void showErrorDialog(String errorMessage, String title) {
        EkinCareSingleButtonDialogFragment fragment = new EkinCareSingleButtonDialogFragment(R.string.ok, errorMessage,
                title, true);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fragment.show(fm, EkinCareSingleButtonDialogFragment.class.toString());
    }

    private void displayLastWeeksCaloriesData() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));

        System.out.println("mytime========="+dateFormat.format(startTime));

        //Check how many steps were walked and recorded in the last 7 days
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();
        try{
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);
            //Used for aggregated data
            if (dataReadResult.getBuckets().size() > 0) {
                Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        showWeeklyCaloriesDataSet(dataSet);
                    }
                }
            }
            //Used for non-aggregated data
            else if (dataReadResult.getDataSets().size() > 0) {
                Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
                for (DataSet dataSet : dataReadResult.getDataSets()) {
                    showWeeklyCaloriesDataSet(dataSet);
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }




    }

    private void displayLastWeeksData() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));

        //Check how many steps were walked and recorded in the last 7 days
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        try{
            DataReadResult dataReadResult = Fitness.HistoryApi.readData(mGoogleApiClient, readRequest).await(1, TimeUnit.MINUTES);

            //Used for aggregated data
            if (dataReadResult.getBuckets().size() > 0) {
                Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
                for (Bucket bucket : dataReadResult.getBuckets()) {
                    List<DataSet> dataSets = bucket.getDataSets();
                    for (DataSet dataSet : dataSets) {
                        showWeeklyStepsDataSet(dataSet);
                    }
                }
            }
            //Used for non-aggregated data
            else if (dataReadResult.getDataSets().size() > 0) {
                Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
                for (DataSet dataSet : dataReadResult.getDataSets()) {
                    showWeeklyStepsDataSet(dataSet);
                }
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }

    }

    private void showWeeklyCaloriesDataSet(DataSet dataSet) {
        Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {

            for (Field field : dp.getDataType().getFields()) {
                Log.e("History", "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                Value todaySteps = dp.getValue(field);
                updateWeekCaloriesData(todaySteps.asFloat(), dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));


            }


        }
    }

    private void showWeeklyStepsDataSet(DataSet dataSet) {
        Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();
        for (DataPoint dp : dataSet.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                Log.e("History", "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
                Value todaySteps = dp.getValue(field);
                updateWeekData(todaySteps.asInt(), dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));

            }


        }
    }

    private void updateWeekCaloriesData(final float numberOfSteps, final String data) {
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String[] resultCalories;
                System.out.println("date=============new=="+data);
                if(data.contains("-")){
                    System.out.println("DashboardOverview.run====="+"-");
                    resultCalories = data.split("-");
                    if(weeklyCaloriesDatesList.size()<7){
                        weeklyCaloriesDatesList.add(resultCalories[0]);
                    }else{

                    }

                }else if(data.contains("/")){
                    System.out.println("DashboardOverview.run====="+"/");

                    resultCalories = data.split("/");
                    if(weeklyCaloriesDatesList.size()<7){
                        weeklyCaloriesDatesList.add(resultCalories[0]);
                    }else{

                    }
                }else if(data.contains(",") && data.contains(" ") ){
                    System.out.println("DashboardOverview.run====="+",");

                    resultCalories = data.split(",");
                    System.out.println("DashboardOverview.run====="+resultCalories[0]);
                    if(weeklyCaloriesDatesList.size()<7){
                        weeklyCaloriesDatesList.add(resultCalories[0].substring(4));
                    }else{

                    }
                }else if(data.contains(" ")){
                    System.out.println("DashboardOverview.run====="+" empty");

                    resultCalories = data.split("\\s+");
                    if(weeklyCaloriesDatesList.size()<7){
                        weeklyCaloriesDatesList.add(resultCalories[0]);
                    }else{

                    }
                }

                if(weeklyCaloriesDatesListFullFormat.size()<7){
                    weeklyCaloriesDatesListFullFormat.add(DateUtility.googleFitDate(data));
                }else{

                }

                if(weeklyCaloriesList.size()<7){
                    weeklyCaloriesList.add((int) numberOfSteps);
                }else{

                }



                tinydb.putListInt("CaloriesWeeklyData", weeklyCaloriesList);
                tinydb.putListString("DayssCalories", weeklyCaloriesDatesList);
                tinydb.putListString("caloriesFullDate", weeklyCaloriesDatesListFullFormat);
                if (mAlert_Dialog != null) {
                    mAlert_Dialog.dismiss();
                }

            }
        });
    }

    private void updateWeekData(final int numberOfSteps, final String data) {
        mMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] result;
                if(data.contains("-")){
                    result = data.split("-");
                    if(weeklyStepsDatesList.size()<7){
                        weeklyStepsDatesList.add(result[0]);
                    }else{

                    }

                }else if(data.contains("/")){
                    result = data.split("/");
                    if(weeklyStepsDatesList.size()<7){
                        weeklyStepsDatesList.add(result[0]);
                    }else{

                    }

                }else if(data.contains(",") && data.contains(" ")){
                    result = data.split(",");
                    if(weeklyStepsDatesList.size()<7){
                        weeklyStepsDatesList.add(result[0].substring(4));
                    }else{

                    }
                }else if(data.contains(" ")){
                    result = data.split("\\s+");
                    if(weeklyStepsDatesList.size()<7){
                        weeklyStepsDatesList.add(result[0]);
                    }else{

                    }
                }



                if(weeklyStepsDatesListFullFormat.size()<7){
                    weeklyStepsDatesListFullFormat.add(DateUtility.googleFitDate(data));
                }else{

                }

                if(weeklyStepsList.size()<7){
                    weeklyStepsList.add(numberOfSteps);
                }else{

                }


                weeklyStepsArray = weeklyStepsList.toArray(new Integer[weeklyStepsList.size()]);
                tinydb.putListInt("WeekSteps", weeklyStepsList);
                tinydb.putListString("Dayss", weeklyStepsDatesList);
                tinydb.putListString("StepsFullDateFormat", weeklyStepsDatesListFullFormat);



            }
        });
    }

    private void displayStepDataForToday() {

        System.out.println("checkid======"+mGoogleApiClient);

        try{
            DailyTotalResult resultForSteps = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_STEP_COUNT_DELTA).await(1, TimeUnit.MINUTES);
            DailyTotalResult resultForCalories = Fitness.HistoryApi.readDailyTotal(mGoogleApiClient, DataType.TYPE_CALORIES_EXPENDED).await(1, TimeUnit.MINUTES);
            showStepsData(resultForSteps.getTotal());
            showCaloriesData(resultForCalories.getTotal());
        }catch (NullPointerException e){
            e.printStackTrace();
        }


    }

    private void showStepsData(DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                Value todaySteps = dp.getValue(field);
                updateTextViewWithStepCounter(todaySteps.asInt());
            }
        }
    }

    private void showCaloriesData(DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                Value todayCalories = dp.getValue(field);
                updateTextViewWithCaloriesCounter(todayCalories.asFloat());
            }
        }
    }

    private void updateTextViewWithStepCounter(final int numberOfSteps) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("steps=========="+String.valueOf(numberOfSteps));
                    gfitdaySteps.setText(String.valueOf(numberOfSteps));
                    prefs.setStepsCount(String.valueOf(numberOfSteps));

                }catch(NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void updateTextViewWithCaloriesCounter(final float numberOfSteps) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("steps calories=========="+String.valueOf(numberOfSteps));
                    gfitCalBurned.setText(String.valueOf((int) numberOfSteps));
                    prefs.setCaloriesCount(String.valueOf((int) numberOfSteps));

                }catch (NullPointerException e){
                    e.printStackTrace();
                }


            }
        });
    }

    private void setUpdateTellUsYourDayIntroLogic()
    {
        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(tellUsdayUpdate)
                .setPrimaryText("Your daily activities matter")
                .setSecondaryText("Help us to help you to make a healthy future.")
                .setCaptureTouchEventOnFocal(true)
                .setCaptureTouchEventOutsidePrompt(true)
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        setViewProfileIntroLogic();
                    }
                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();
    }

    private void setViewProfileIntroLogic()
    {
        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(textViewProfileAddOrView)
                .setPrimaryText("View your family")
                .setSecondaryText("Check out family members and add more.")
                .setCaptureTouchEventOnFocal(true)
                .setCaptureTouchEventOutsidePrompt(true)
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        focusOnWaterAddView();
                        setWaterAddIntroLogic();
                    }
                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();
    }

    public void showOverflowPrompt()
    {
        final MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(getActivity())
                .setPrimaryText("Settings")
                .setSecondaryText("Some more settings are here.")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.margin_350)
                .setCaptureTouchEventOnFocal(true)
                .setCaptureTouchEventOutsidePrompt(true)
                .setIcon(R.drawable.ic_more_vert);
        final Toolbar tb = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tapTargetPromptBuilder.setTarget(tb.getChildAt(3));

        tapTargetPromptBuilder.setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
        {
            @Override
            public void onHidePrompt(MotionEvent event, boolean tappedTarget)
            {
                setNotifactionIntroLogic();
            }

            @Override
            public void onHidePromptComplete()
            {

            }
        });
        tapTargetPromptBuilder.show();
    }


    */
/*private void setNavigationMenuIntroLogic()
    {
        final MaterialTapTargetPrompt.Builder tapTargetPromptBuilder = new MaterialTapTargetPrompt.Builder(getActivity())
                .setPrimaryText("Your Menu")
                .setSecondaryText("Tap menu icon for features like Profile,Blood SOS ...")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                .setCaptureTouchEventOnFocal(true)
                .setCaptureTouchEventOutsidePrompt(true)
                .setIcon(R.drawable.ic_menu);
        final Toolbar tb = (Toolbar) getActivity().findViewById(R.id.toolbar);
        tapTargetPromptBuilder.setTarget(tb.getChildAt(1));

        tapTargetPromptBuilder.setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
        {
            @Override
            public void onHidePrompt(MotionEvent event, boolean tappedTarget)
            {
                //Do something such as storing a value so that this prompt is never shown again
                showOverflowPrompt();
            }

            @Override
            public void onHidePromptComplete()
            {

            }
        });
        tapTargetPromptBuilder.show();
    }*//*


    public void setNotifactionIntroLogic()
    {
        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(getActivity().findViewById(R.id.activity_notification))
                .setPrimaryText("Notification")
                .setSecondaryText("Access your important notifications here.")
                .setAnimationInterpolator(new FastOutSlowInInterpolator())
                .setMaxTextWidth(R.dimen.margin_350)
                .setIcon(R.drawable.notifications_main_before)
                .setCaptureTouchEventOutsidePrompt(true)
                .setCaptureTouchEventOnFocal(true)
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        setUpdateTellUsYourDayIntroLogic();
                    }
                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();
    }

    private final void focusOnWaterAddView()
    {
        mScrollViewMain.post(new Runnable() {
            @Override
            public void run() {
                mScrollViewMain.scrollTo(0, waterCardView.getTop());
            }
        });
    }

    private void setWaterAddIntroLogic()
    {
        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(waterAddButton)
                .setPrimaryText("Body is made up of 70 water.")
                .setSecondaryText("Track your daily water intake here.")
                .setCaptureTouchEventOnFocal(true)
                .setCaptureTouchEventOutsidePrompt(true)
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        setUploadRecordIntroLogic();
                    }
                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();
    }

    private final void focusOnBookNowView()
    {
        mScrollViewMain.post(new Runnable() {
            @Override
            public void run() {
                mScrollViewMain.scrollTo(0, bookcard.getBottom());
            }
        });
    }

    private void setBooknowAppointmentIntroLogic()
    {
        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(booknowAppointment)
                .setPrimaryText("No need to go out,get everything here.")
                .setSecondaryText("Book appoinment......bla bla text")
                .setCaptureTouchEventOnFocal(true)
                .setCaptureTouchEventOutsidePrompt(true)
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        */
/*Intent intent = new Intent(getActivity(),ActivityFamilyMember.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        customerManager.setCurrentFamilyMember(customerManager.getCurrentCustomer());
                        startActivity(intent);*//*

                    }
                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();
    }

    private void setUploadRecordIntroLogic()
    {
        new MaterialTapTargetPrompt.Builder(getActivity())
                .setTarget(recordUploadButton)
                .setPrimaryText("Keep your medical records safe")
                .setSecondaryText("Upload your medical records here.")
                .setCaptureTouchEventOnFocal(true)
                .setCaptureTouchEventOutsidePrompt(true)
                .setOnHidePromptListener(new MaterialTapTargetPrompt.OnHidePromptListener()
                {
                    @Override
                    public void onHidePrompt(MotionEvent event, boolean tappedTarget) {
                        focusOnBookNowView();
                        setBooknowAppointmentIntroLogic();
                    }
                    @Override
                    public void onHidePromptComplete()
                    {

                    }
                })
                .show();
    }

    private class ViewTodaysStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayStepDataForToday();
            return null;
        }


    }

    private class ViewWeekStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayLastWeeksData();
            return null;
        }
    }

    private class ViewWeekCaloriesCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            displayLastWeeksCaloriesData();
            return null;
        }
    }

}
*/

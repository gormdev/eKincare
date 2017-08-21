package com.ekincare.ui.fragment;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
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
import com.ekincare.ui.custom.CustomSeekBar;
import com.ekincare.ui.custom.ProgressItem;
import com.ekincare.util.CalculationUtility;
import com.ekincare.util.CircularSeekBar;
import com.ekincare.util.DateUtility;
import com.ekincare.util.TinyDB;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import com.oneclick.ekincare.MainActivity;
import com.oneclick.ekincare.PedometerGraph;

import com.oneclick.ekincare.MainActivity2;

import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.BmiSeriese;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.CustomerVitals;
import com.oneclick.ekincare.vo.HealthRisksData;
import com.oneclick.ekincare.vo.InsightCustomerHealthScore;
import com.oneclick.ekincare.vo.InsightDataModel;
import com.oneclick.ekincare.vo.ProfileData;
import com.oneclick.ekincare.vo.WaistSeriese;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


/**
 * Created by RaviTejaN on 23-11-2016.
 */

public class FragmentDashboardRisks extends Fragment {
    View mView;
    private CustomerManager customerManager;
    private Customer mCustomer;
    private ProfileData mProfileData;
    DatabaseOverAllHandler dbHandler;
    PreferenceHelper prefs;
    ProfileManager mProfileManager;
    String url;
    private String strFamilyMemberKey = "";
    private InsightDataModel insightdatamodel;
    RelativeLayout insightMainLayout;
    LinearLayout insightDataLayout;
    CardView cardBehaviourPercentile,cardHealthRisksData,cardWaistGraph,cardBmiGraph,
            cardHealthScoreDataGraph,cardWeightDataGraph,cardHeightDataGraph,
            healthScoreCardLayout,cardGoogleFit;
    TextView insightLifeStyleLable,insightDietLable,insightStressLable,insightLifeStyleValue,insightDietValue,insightStreeValue;
    TextView gfitdaySteps, gfitCalBurned,unconnectedToGoogleFitlabel,connectToGoogleFitButton;
    LineChart mChartSteps,mChart1;
    ImageView refresGgoogleFitData;
    LinearLayout linearLayoutInstalled,linearLayoutNotInstalled;
    LinearLayout stepsLayoutBar;
    LinearLayout caloriesLayoutBar;
    ProgressBar insightLifeStyleProgress,insightDietStyleProgress,insightStressStyleProgress;
    ListView insightHealthListView;
    InsightHealthRiskAdapter adapter;
    List<HealthRisksData> mArrayListAssessmentType;
    List<WaistSeriese> mArrayListWaistData;
    List<InsightCustomerHealthScore> mArrayListHealthScoreData;
    List<String> mArrayListWaistDataValue1;
    List<String> mArrayListWaistDataValue2;
    List<BmiSeriese> mArrayListBmiData;
    List<String> mArrayListBmiDataValue1;
    List<String> mArrayListBmiDataValue2;

    List<String> mArrayListhealthScoreDataValue1;
    List<String> mArrayListhealthScoreDataValue2;
    List<String> ageXaxiesValues;
    List<String> waistMonthsXaxiesValues;
    List<String> bmitMonthsXaxiesValues;
    com.github.mikephil.charting.charts.LineChart waistchart;
    com.github.mikephil.charting.charts.LineChart bmichart;
    com.github.mikephil.charting.charts.LineChart healthScorechart;
    com.github.mikephil.charting.charts.LineChart lineChartWeight;
    com.github.mikephil.charting.charts.LineChart lineChartHeight;
    ArrayList<Entry> waistdataset1;
    ArrayList<Entry> waistdataset2;
    ArrayList<Entry> waistdataset3;

    ArrayList<Entry> stepsYaxiesValues;

    ArrayList<Entry> bmiDataset1;
    ArrayList<Entry> bmiDataset2;
    ArrayList<Entry> bmiDataset3;

    ArrayList<Entry> childHeightEntry;

    ArrayList<Entry> healthScoreDataset1;
    ArrayList<Entry> healthScorebmiDataset2;
    ArrayList<Entry> healthScorebmiDataset3;
    ScrollView insightDataLayoutScrollview;
    ArrayList<Entry> yValsChildWeight;
    String[] mMonths = new String[] {
            "<18","18-25", "26-30","31-35", "36-40","41-45","45-50","51-55","55>"
    };
    String[] mMonthsBmi;
    String[] mMonthsWaist;
    String[] xAxishealth;
    String[] mMonthsChildWeight;
    String[] mMonthsChildHeight;
   /* String[] ageSpans = new String[] {
            "36","37","38","39","40"
    };*/
    int ageInt = 0;
    YAxis leftAxisHeight;
    int minuserWeight = 0;
    int maxuserWeight = 0;
    int monthStart = 0;
    int waistPosition=0;
    int healthScorePosition=0;
    int bmiPosition=0;
    Double bmi_text = 0.0;
    private ImageView healthScoreInfo;
    private CustomSeekBar seekbar;
    private ArrayList<ProgressItem> progressItemList;
    private ProgressItem mProgressItem;

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
    TinyDB tinydb;
    ArrayList<String> weeklyDatesSteps1;
    ArrayList<String> weeklyDatesCalories1;
    ArrayList<Integer> weeklyStepsList1;
    ArrayList<Integer> weeklyCaloriesList1;

    Animation rotate;

    LinearLayout waistmaleLayout,waistFemaleLayout,bmiMaleLayout,bmiFemaleLayout;

    private int tempHealtScore = 22;
    TextView textViewOrigionalHs,textViewOptimalHs,progressLimitValue;
    FrameLayout frameLayoutTextHealthScore,frameLayoutTextOptimalHealthScore;
    private SeekBar seekbarHealthScore,seekbarOptimalHealthScore;
    private boolean firstTime = true;
    private RelativeLayout relativeLayoutMissingHra;
    private FrameLayout frameLayoutFIlledHra;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setTitle();
    }

    public FragmentDashboardRisks() {
    }

    private void setTitle() {
        LinearLayout switchFamilyMemberLayout = (LinearLayout) getActivity().findViewById(R.id.activity_add_family_member_layout);
        switchFamilyMemberLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        mCustomer = customerManager.getCurrentFamilyMember();
        prefs = new PreferenceHelper(getActivity());
        dbHandler = new DatabaseOverAllHandler(getActivity());
        mProfileManager = ProfileManager.getInstance(getActivity());
        rotate = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.insight_layout, container, false);
        insightDataLayoutScrollview=(ScrollView)mView.findViewById(R.id.insight_data_layout_scrollview);
        insightMainLayout=(RelativeLayout)mView.findViewById(R.id.insight_main_layout);
        insightDataLayout=(LinearLayout)mView.findViewById(R.id.insight_data_layout);
        cardBehaviourPercentile=(CardView)mView.findViewById(R.id.card_behaviour_percentile);
        cardHealthRisksData=(CardView)mView.findViewById(R.id.card_health_risks_data);
        cardWaistGraph=(CardView)mView.findViewById(R.id.card_waist_graph_insight);
        cardBmiGraph=(CardView)mView.findViewById(R.id.card_bmi_graph_insight);
        cardHealthScoreDataGraph=(CardView)mView.findViewById(R.id.card_healthScore_graph_insight);
        cardWeightDataGraph=(CardView)mView.findViewById(R.id.card_chaild_weight_graph_insight);
        cardHeightDataGraph=(CardView)mView.findViewById(R.id.card_chaild_height_graph_insight);
        healthScoreCardLayout=(CardView)mView.findViewById(R.id.health_score_card_view);
        cardGoogleFit=(CardView)mView.findViewById(R.id.google_fit_card_view);

        relativeLayoutMissingHra=(RelativeLayout)mView.findViewById(R.id.missing_hra_layout);
        frameLayoutFIlledHra=(FrameLayout)mView.findViewById(R.id.filled_hra_layout);

        initBehaviourPercentile();
        initHealthRisksData();
        initBmiGraph();
        initWaistGraph();
        initHealthScoreGraph();
        initChildWeightChart();
        initChildHeightChart();
        initHealthScoreView();
        initGoogleFitView();
        weeklyStepsList = new ArrayList<Integer>();
        weeklyCaloriesList = new ArrayList<Integer>();
        weeklyStepsDatesList = new ArrayList<String>();
        weeklyStepsDatesListFullFormat = new ArrayList<String>();
        weeklyCaloriesDatesListFullFormat = new ArrayList<String>();
        weeklyCaloriesDatesList = new ArrayList<String>();
        tinydb= new TinyDB(getActivity());


        refreshRiskFragment();

        setRetainInstance(true);
        return mView;

    }

    private void initBehaviourPercentile() {
        View insightView = View.inflate(getActivity(), R.layout.insight_behaviour_percentile, null);
        insightLifeStyleLable = (TextView) insightView.findViewById(R.id.insight_life_style_lable);
        insightDietLable = (TextView) insightView.findViewById(R.id.insight_diet_lable);
        insightStressLable = (TextView) insightView.findViewById(R.id.insight_stress_lable);
        insightLifeStyleValue = (TextView) insightView.findViewById(R.id.insight_life_style_lable_value);
        insightDietValue = (TextView) insightView.findViewById(R.id.insight_diet_lable_value);
        insightStreeValue = (TextView) insightView.findViewById(R.id.insight_stress_lable_value);
        insightLifeStyleProgress=(ProgressBar)insightView.findViewById(R.id.insight_life_style_progressbar);
        insightDietStyleProgress=(ProgressBar)insightView.findViewById(R.id.insight_diet_progressbar);
        insightStressStyleProgress=(ProgressBar)insightView.findViewById(R.id.insight_stress_progressbar);
        cardBehaviourPercentile.addView(insightView);
    }

    private void initHealthRisksData(){
        View insightHealthView = View.inflate(getActivity(), R.layout.insight_health_risk_data, null);
        insightHealthListView = (ListView) insightHealthView.findViewById(R.id.insight_health_listview);
        cardHealthRisksData.addView(insightHealthView);

    }

    private void initBmiGraph(){
        View insightBmi = View.inflate(getActivity(), R.layout.insight_bmi_graph_data, null);
        bmichart = (com.github.mikephil.charting.charts.LineChart) insightBmi.findViewById(R.id.chart_bmi_graph);
        bmiFemaleLayout=(LinearLayout)insightBmi.findViewById(R.id.bmi_female_layout);
        bmiMaleLayout=(LinearLayout)insightBmi.findViewById(R.id.bmi_male_layout);
        cardBmiGraph.addView(insightBmi);

    }

    private void initWaistGraph(){
        View insightwaist = View.inflate(getActivity(), R.layout.insight_waist_graph_data, null);
        waistchart = (com.github.mikephil.charting.charts.LineChart) insightwaist.findViewById(R.id.chart_waist_graph);
        waistFemaleLayout=(LinearLayout)insightwaist.findViewById(R.id.waist_female_layout);
        waistmaleLayout=(LinearLayout)insightwaist.findViewById(R.id.waist_male_layout);
        cardWaistGraph.addView(insightwaist);

    }

    private void initHealthScoreGraph(){
        View insighthealthScore = View.inflate(getActivity(), R.layout.insight_health_score_graph_data, null);
        healthScorechart = (com.github.mikephil.charting.charts.LineChart) insighthealthScore.findViewById(R.id.chart_health_score_graph);
        cardHealthScoreDataGraph.addView(insighthealthScore);

    }

    private void initChildWeightChart(){
        View insightChildWeightScore = View.inflate(getActivity(), R.layout.insight_child_weight_chart, null);
        lineChartWeight = (com.github.mikephil.charting.charts.LineChart) insightChildWeightScore.findViewById(R.id.lineChart_child_weight);
        cardWeightDataGraph.addView(insightChildWeightScore);
    }

    private void initChildHeightChart(){
        View insightChildHeightScore = View.inflate(getActivity(), R.layout.insight_cild_height_chart, null);
        lineChartHeight = (com.github.mikephil.charting.charts.LineChart) insightChildHeightScore.findViewById(R.id.lineChart_child_height);
        cardHeightDataGraph.addView(insightChildHeightScore);
    }

    private void initHealthScoreView(){
        View healthScoreView = View.inflate(getActivity(), R.layout.profile_health_score, null);

        seekbar = (CustomSeekBar) healthScoreView.findViewById(R.id.seekBar0);
        healthScoreInfo = (ImageView) healthScoreView.findViewById(R.id.image_health_score_info);

        seekbarHealthScore = (SeekBar) healthScoreView.findViewById(R.id.seekbar_origional_hs);


        seekbarOptimalHealthScore = (SeekBar) healthScoreView.findViewById(R.id.seekbar_optimal_hs);

        textViewOrigionalHs = (TextView) healthScoreView.findViewById(R.id.text_original_hs_seek);
        textViewOptimalHs = (TextView) healthScoreView.findViewById(R.id.text_view_optimal_hs);

        progressLimitValue=(TextView)healthScoreView.findViewById(R.id.progress_limit_show);

        frameLayoutTextHealthScore = (FrameLayout) healthScoreView.findViewById(R.id.text_origional_hs_container);
        frameLayoutTextOptimalHealthScore = (FrameLayout) healthScoreView.findViewById(R.id.optimal_hs_container);


        healthScoreInfo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog("The eKincare wellness score is a single score representative of your overall health. " +
                        "Wellness score helps you get actionable insights on the areas you need to focus on," +
                        " to improve your health.","What is health score");
            }
        });
        healthScoreCardLayout.addView(healthScoreView);
        progressItemList = new ArrayList<ProgressItem>();
        // red span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 33;
        mProgressItem.color = R.color.progress_first;
        progressItemList.add(mProgressItem);
        // blue span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 33;
        mProgressItem.color = R.color.progress_second;
        progressItemList.add(mProgressItem);
        // green span
        mProgressItem = new ProgressItem();
        mProgressItem.progressItemPercentage = 33;
        mProgressItem.color = R.color.progress_three;
        progressItemList.add(mProgressItem);
        // yellow span


        seekbar.initData(progressItemList);
        seekbar.invalidate();


    }

    private void initGoogleFitView()
    {
        tinydb = new TinyDB(getActivity());
        weeklyStepsList1 = tinydb.getListInt("WeekSteps");
        weeklyCaloriesList1 = tinydb.getListInt("CaloriesWeeklyData");
        weeklyDatesSteps1 = tinydb.getListString("Dayss");
        weeklyDatesCalories1 = tinydb.getListString("DayssCalories");

        View gFitView = View.inflate(getActivity(), R.layout.check_google_fit_install, null);
        cardGoogleFit.addView(gFitView);

        linearLayoutInstalled = (LinearLayout) gFitView.findViewById(R.id.google_fit_data_layout);
        linearLayoutNotInstalled = (LinearLayout) gFitView.findViewById(R.id.google_fit_check_before_layout);

        unconnectedToGoogleFitlabel = (TextView) gFitView.findViewById(R.id.textview_google_fit_unconnected_message);

        connectToGoogleFitButton = (TextView) gFitView.findViewById(R.id.connet_to_google_fit_view);

        refresGgoogleFitData = (ImageView) gFitView.findViewById(R.id.refresh_google_fitData);

        gfitdaySteps = (TextView) gFitView.findViewById(R.id.today_steps_count_value);
        gfitCalBurned = (TextView) gFitView.findViewById(R.id.today_calories_burned_value);

        stepsLayoutBar = (LinearLayout) gFitView.findViewById(R.id.setp_layout_bar);
        caloriesLayoutBar = (LinearLayout) gFitView.findViewById(R.id.calories_layout_bar);

        mChart1 = (LineChart)gFitView.findViewById(R.id.chart2);

        mChartSteps = (LineChart)gFitView.findViewById(R.id.chart1);

        stepsLayoutBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("FragmentDashboardRisks.onClick======"+weeklyCaloriesList.size());
                if(weeklyStepsList.size()!=0){
                    Intent intent = new Intent(getActivity(), PedometerGraph.class);
                    startActivity(intent);
                }

            }
        });
        caloriesLayoutBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(weeklyCaloriesList.size()!=0){
                    Intent intent = new Intent(getActivity(), PedometerGraph.class);
                    startActivity(intent);
                }
            }
        });




        refresGgoogleFitData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshGoogleFitData();

            }
        });

        connectToGoogleFitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGoogleFitDialog();
            }
        });
        System.out.println("checkVisible========="+linearLayoutInstalled.getVisibility());

         //
        if(prefs.getIsLogin()){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    try{
                        refreshGoogleFitData();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    handler.postDelayed(this, 60000*60*2); //now is every 2 Hours
                }
            }, 60000*60*2);

        }


        mChartSteps.getDescription().setEnabled(false);
        mChartSteps.setTouchEnabled(false);
        mChartSteps.setDragDecelerationFrictionCoef(0.9f);
        mChartSteps.setDragEnabled(true);
        mChartSteps.setScaleEnabled(true);
        mChartSteps.setDrawGridBackground(false);
        mChartSteps.setPinchZoom(false);
        mChartSteps.setDoubleTapToZoomEnabled(false);
        mChartSteps.setBackgroundColor(Color.WHITE);
        setDataStepsData();
        mChartSteps.animateX(2500);
        Legend stepsLegend = mChartSteps.getLegend();
        stepsLegend.setForm(Legend.LegendForm.LINE);
        stepsLegend.setTextSize(11f);
        stepsLegend.setTextColor(Color.BLACK);
        stepsLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        stepsLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        stepsLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        stepsLegend.setDrawInside(false);
        XAxis xAxis = mChartSteps.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(getResources().getColor(R.color.card_second));
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(45);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
       // xAxis.setLabelCount(ageSpans.length, true);

        xAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));



        YAxis leftAxis = mChartSteps.getAxisLeft();
        leftAxis.setTextColor(getResources().getColor(R.color.card_second));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setLabelCount(4, true);
                    /*leftAxis.setAxisMinimum(25f); // start at zero
                    leftAxis.setAxisMaximum(40f);*/
        leftAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));

        mChartSteps.getLegend().setEnabled(false);
        mChartSteps.getAxisRight().setEnabled(false);
        mChartSteps.animateXY(2000, 2000);
        mChartSteps.getAxisLeft().setDrawGridLines(true);
        mChartSteps.getXAxis().setDrawGridLines(true);
        mChartSteps.setAutoScaleMinMaxEnabled(true);
                    /*Highlight highlight = new Highlight(50f,50f, 0);
                    healthScorechart.highlightValue(highlight, true);*/








    }

    private void refreshGoogleFitData() {
        refresGgoogleFitData.startAnimation(rotate);
        refresGgoogleFitData.setEnabled(false);
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
        if (NetworkCaller.isInternetOncheck(getActivity())) {

            Activity activity = getActivity();
            if(activity instanceof MainActivity)
            {
                final MainActivity myactivity = (MainActivity) activity;
                myactivity.googleFitHistoryPost(obj);
            }
        }else {
            CustomeDialog.dispDialog(getActivity(), "Internet not available");
        }

    }


    private void setDataStepsData() {

        stepsYaxiesValues = new ArrayList<Entry>();
        System.out.println("json=========="+Arrays.toString(weeklyCaloriesList1.toArray()));
        for (int i = 0; i < weeklyCaloriesList1.size(); i++) {
            stepsYaxiesValues.add(new Entry(i,weeklyCaloriesList1.get(i)));
        }


        LineDataSet set1;
        if (mChartSteps.getData() != null &&
                mChartSteps.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChartSteps.getData().getDataSetByIndex(0);
            set1.setValues(stepsYaxiesValues);
            mChartSteps.getData().notifyDataChanged();
            mChartSteps.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(stepsYaxiesValues, "Male");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.red));
            set1.setLineWidth(3f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            // set1.setHighlightLineWidth(3f);
            set1.setFillColor(getResources().getColor(R.color.red));
            set1.setDrawCircleHole(true);
            set1.setCircleColor(getResources().getColor(R.color.red));
            LineData data = new LineData(set1);
            data.setDrawValues(false);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(11f);
            // set data
            mChartSteps.setData(data);
            mChartSteps.invalidate();

        }
    }

    private void showGoogleFitDialog()
    {
        Intent intent= new Intent(getActivity(), MainActivity2.class);
        startActivity(intent);

    }

    private void showDialog(String errorMessage, String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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



    private void requestToInsightData() {
        {
            System.out.println("FragmentDashboardRisks.requestToInsightData");
            url= TagValues.INSIGHTS_CUSTOMER;
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject result) {
                    System.out.println("FragmentDashboardRisks.onResponse yyy=========="+result.toString());
                    if (result != null) {
                        responseInsightData(result);

                    }else{

                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError arg0) {
                    System.out.println("FragmentDashboardRisks.onResponse=========="+arg0.toString());

                }
            })
            {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse networkResponse) {
                    Map<String,String> headers = networkResponse.headers;
                    Set<String> keySet = headers.keySet();

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
    }

    private void responseInsightData(JSONObject result) {
        System.out.println("FragmentDashboardRisks.responseInsightData Result==========="+result.toString());

        int age = 0;
        boolean isChild = false;
        try{
            age  = Integer.parseInt(mCustomer.getAge());
            if(age<18){
                isChild = true;
            }
        }catch (Exception e){
            e.printStackTrace();
            isChild = mCustomer.isChild();
        }

        System.out.println("FragmentDashboardRisks.responseInsightData age="+age + " isChild="+isChild);
        if (isChild) {
            cardWeightDataGraph.setVisibility(View.VISIBLE);
            cardHeightDataGraph.setVisibility(View.VISIBLE);
            cardBehaviourPercentile.setVisibility(View.GONE);
            cardHealthRisksData.setVisibility(View.GONE);
            cardWaistGraph.setVisibility(View.GONE);
            cardBmiGraph.setVisibility(View.GONE);
            cardHealthScoreDataGraph.setVisibility(View.GONE);
            healthScoreCardLayout.setVisibility(View.GONE);

            boolean isBoy = false;
            if (mCustomer.getGender() != null && mCustomer.getGender().equalsIgnoreCase("Male")) {
                isBoy = true;
            }
            if ((mCustomer.getAge() != null && !mCustomer.getAge().toString().equalsIgnoreCase(""))) {
                try {
                    ageInt = Integer.parseInt(mCustomer.getAge());
                } catch (NumberFormatException e) {
                    System.out.println("FragmentDashboardRisks.onResponse=========" + "ex");

                }
            }
            setGraphsChildWeight(isBoy);
            setGraphsHeight(isBoy);

        } else {

            try {
                cardWeightDataGraph.setVisibility(View.GONE);
                cardHeightDataGraph.setVisibility(View.GONE);
                cardBehaviourPercentile.setVisibility(View.GONE);
                cardHealthRisksData.setVisibility(View.VISIBLE);
                cardWaistGraph.setVisibility(View.VISIBLE);
                cardBmiGraph.setVisibility(View.VISIBLE);
                healthScoreCardLayout.setVisibility(View.VISIBLE);
                cardHealthScoreDataGraph.setVisibility(View.VISIBLE);

                try{

                    //seekbar.setProgress(Integer.parseInt(mCustomer.getHealth_score()));
                    tempHealtScore = Integer.parseInt(mCustomer.getHealth_score());
                    textViewOrigionalHs.setText(String.valueOf(tempHealtScore));
                    seekbarHealthScore.setProgress(tempHealtScore);
                    float x = seekbarHealthScore.getThumb().getBounds().left;
                    //set the left value to textview x value
                    frameLayoutTextHealthScore.setX(x);
                    seekbarHealthScore.setEnabled(false);

                    System.out.println("FragmentDashboardRisks.responseInsightData========"+ Integer.parseInt(mCustomer.getOptimum_health_score()));
                    textViewOptimalHs.setText(String.valueOf(Integer.parseInt(mCustomer.getOptimum_health_score())));
                    seekbarOptimalHealthScore.setProgress(Integer.parseInt(mCustomer.getOptimum_health_score()));
                    float optimalX = seekbarOptimalHealthScore.getThumb().getBounds().left;
                    frameLayoutTextOptimalHealthScore.setX(optimalX);
                    seekbarOptimalHealthScore.setEnabled(false);
                    seekbar.setEnabled(false);
                    seekbar.getThumb().mutate().setAlpha(0);
                    if(mCustomer.getHealth_score().length()<=2){
                        seekbarOptimalHealthScore.setMax(100);
                        seekbarHealthScore.setMax(100);
                        progressLimitValue.setText("100");
                    }else if(mCustomer.getHealth_score().length()>=3){
                        seekbarOptimalHealthScore.setMax(1000);
                        seekbarHealthScore.setMax(1000);
                        progressLimitValue.setText("1000");
                    }


                }catch (Exception e){
                    e.printStackTrace();
                    healthScoreCardLayout.setVisibility(View.GONE);
                }
                insightdatamodel = new Gson().fromJson(result.toString(), InsightDataModel.class);
                if (insightdatamodel.getBehaviour_percentile().size() == 0 && insightdatamodel.getWaist().getSeries() == null
                        && insightdatamodel.getBmi().getSeries() == null && insightdatamodel.getCustomer_health_score().getWorst_score() == null &&
                        insightdatamodel.getHealth_risks_data().size() == 0) {
                    System.out.println("FragmentDashboardRisks.responseInsightData======" +
                            "YES");

                    insightMainLayout.setVisibility(View.VISIBLE);
                    insightDataLayoutScrollview.setVisibility(View.GONE);
                    insightDataLayout.setVisibility(View.GONE);
                } else {
                    System.out.println("FragmentDashboardRisks.responseInsightData======" +
                            "NO");
                    insightMainLayout.setVisibility(View.GONE);
                    insightDataLayoutScrollview.setVisibility(View.VISIBLE);
                    cardWeightDataGraph.setVisibility(View.GONE);
                    insightDataLayout.setVisibility(View.VISIBLE);


                    //Behaviour
                    if (insightdatamodel.getBehaviour_percentile().size() > 0) {
                        try {
                            System.out.println("FragmentDashboardRisks.responseInsightData==========="+"Behaviour");

                            insightLifeStyleValue.setText(insightdatamodel.getBehaviour_percentile().get(0).getPercentile() + " %");
                            insightDietValue.setText(insightdatamodel.getBehaviour_percentile().get(1).getPercentile() + " %");
                            insightStreeValue.setText(insightdatamodel.getBehaviour_percentile().get(2).getPercentile() + " %");
                            insightLifeStyleProgress.setProgress(Integer.parseInt(insightdatamodel.getBehaviour_percentile().get(0).getPercentile()));
                            insightDietStyleProgress.setProgress(Integer.parseInt(insightdatamodel.getBehaviour_percentile().get(1).getPercentile()));
                            insightStressStyleProgress.setProgress(Integer.parseInt(insightdatamodel.getBehaviour_percentile().get(2).getPercentile()));
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                            System.out.println("FragmentDashboardRisks.responseInsightData==========="+"BehaviourException");

                        }
                    } else {
                        cardBehaviourPercentile.setVisibility(View.GONE);
                    }

                    if (insightdatamodel.getHealth_risks_data() != null) {
                        System.out.println("FragmentDashboardRisks.responseInsightData==========="+"HealthRisk");

                        if (insightdatamodel.getHealth_risks_data().size() > 0) {
                            try {
                                System.out.println("FragmentDashboardRisks.responseInsightData==========="+"HealthRisk for");
                                mArrayListAssessmentType = new ArrayList<HealthRisksData>();
                                mArrayListAssessmentType = insightdatamodel.getHealth_risks_data();
                                adapter = new InsightHealthRiskAdapter(getActivity(), mArrayListAssessmentType);
                                insightHealthListView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                setListViewHeightBasedOnChildren(insightHealthListView);

                            }catch (ArrayIndexOutOfBoundsException e){
                                e.printStackTrace();
                                System.out.println("FragmentDashboardRisks.responseInsightData==========="+"HealthRisk for Exception");

                            }


                        } else {
                            cardHealthRisksData.setVisibility(View.GONE);
                        }

                    } else {
                        cardHealthRisksData.setVisibility(View.GONE);
                    }

                    try{
                    mArrayListWaistData = new ArrayList<WaistSeriese>();
                    if (insightdatamodel.getWaist().getSeries() != null) {
                        System.out.println("FragmentDashboardRisks.responseInsightData======" + "waist");
                        mArrayListWaistData = insightdatamodel.getWaist().getSeries();
                        mArrayListWaistDataValue1 = new ArrayList<String>();
                        mArrayListWaistDataValue1 = mArrayListWaistData.get(0).getData();
                        mArrayListWaistDataValue2 = new ArrayList<String>();
                        mArrayListWaistDataValue2 = mArrayListWaistData.get(1).getData();

                        System.out.println("waist values=========="+Arrays.toString(mArrayListWaistDataValue2.toArray()));

                        waistMonthsXaxiesValues = insightdatamodel.getWaist().getCategories_x();
                        mMonthsWaist = new String[waistMonthsXaxiesValues.size()];
                        mMonthsWaist = waistMonthsXaxiesValues.toArray(mMonthsWaist);

                        waistchart.setTouchEnabled(false);
                        waistchart.setDragDecelerationEnabled(false);
                        waistchart.setDragDecelerationFrictionCoef(0.9f);
                        waistchart.setDragEnabled(false);
                        waistchart.setScaleEnabled(true);
                        waistchart.getDescription().setEnabled(false);
                        waistchart.setDrawGridBackground(false);
                        waistchart.setPinchZoom(false);
                        waistchart.setDoubleTapToZoomEnabled(false);
                        waistchart.setBackgroundColor(Color.WHITE);

                        System.out.println("CHeck my array==="+customerManager.getCurrentFamilyMember().getAge());
                        if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) < 18) {
                            waistPosition=0;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 18 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 25) {
                            waistPosition=1;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 26 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 30) {
                            waistPosition=2;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 31 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 35) {
                            waistPosition=3;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 36 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 40) {
                            waistPosition=4;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 41 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 45) {
                            waistPosition=5;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 46 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 50) {
                            waistPosition=6;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 55) {
                            waistPosition=7;
                        }

                        if(mCustomer.getGender().equalsIgnoreCase("Female")){
                            System.out.println("checkgender====="+"Female");
                            setDataFemailWaist(waistPosition);
                        }else if(mCustomer.getGender().equalsIgnoreCase("Male")){
                            System.out.println("checkgender====="+"MAle");
                            setDataWaist(waistPosition);
                        }


                        waistchart.animateX(2500);
                        Legend l = waistchart.getLegend();
                        l.setForm(Legend.LegendForm.LINE);
                        l.setTextSize(11f);
                        l.setTextColor(Color.BLACK);
                        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        l.setDrawInside(false);
                        XAxis xAxis = waistchart.getXAxis();
                        xAxis.setTextSize(11f);
                        xAxis.setTextColor(getResources().getColor(R.color.card_second));
                        xAxis.setDrawGridLines(false);
                        xAxis.setLabelRotationAngle(45);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));

                        xAxis.setValueFormatter(new IAxisValueFormatter() {
                            public int getDecimalDigits() {
                                return 0;
                            }

                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return mMonthsWaist[(int) value % mMonthsWaist.length];
                            }
                        });

                        YAxis leftAxis = waistchart.getAxisLeft();
                        leftAxis.setTextColor(getResources().getColor(R.color.card_second));
                        leftAxis.setDrawGridLines(true);
                        leftAxis.setGranularityEnabled(true);
                        leftAxis.setDrawZeroLine(true);
                        leftAxis.setLabelCount(4, true);

                       /* leftAxis.setAxisMaximum(40);
                        leftAxis.setAxisMinimum(29);*/
                        leftAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));

                        waistchart.getLegend().setEnabled(false);
                        waistchart.getAxisRight().setEnabled(false);
                        waistchart.animateXY(2000, 2000);
                        waistchart.getAxisLeft().setDrawGridLines(true);
                        waistchart.getXAxis().setDrawGridLines(true);
                        waistchart.setAutoScaleMinMaxEnabled(true);
                        waistchart.setHighlightPerTapEnabled(false);

                    } else {
                        cardWaistGraph.setVisibility(View.GONE);
                    }
                    }catch(Exception e){
                        System.out.println("FragmentDashboardRisks.responseInsightData======" + e);


                        System.out.println("FragmentDashboardRisks.responseInsightData======" + "Waist Exception");
                    }

                    try{

                    if (insightdatamodel.getCustomer_health_score().getBest_score() != null && insightdatamodel.getCustomer_health_score().getWorst_score() != null) {
                        System.out.println("FragmentDashboardRisks.responseInsightData======" + "HEalth");
                        mArrayListhealthScoreDataValue1 = new ArrayList<String>();
                        mArrayListhealthScoreDataValue1 = insightdatamodel.getCustomer_health_score().getBest_score();
                        mArrayListhealthScoreDataValue2 = new ArrayList<String>();
                        mArrayListhealthScoreDataValue2 = insightdatamodel.getCustomer_health_score().getWorst_score();

                        ageXaxiesValues = insightdatamodel.getCustomer_health_score().getAge_span();
                        xAxishealth = new String[ageXaxiesValues.size()];
                        xAxishealth = ageXaxiesValues.toArray(xAxishealth);

                        healthScorechart.getDescription().setEnabled(false);
                        healthScorechart.setTouchEnabled(false);
                        healthScorechart.setDragDecelerationFrictionCoef(0.9f);
                        healthScorechart.setDragEnabled(true);
                        healthScorechart.setScaleEnabled(true);
                        healthScorechart.setDrawGridBackground(false);
                        healthScorechart.setPinchZoom(false);
                        healthScorechart.setDoubleTapToZoomEnabled(false);
                        healthScorechart.setBackgroundColor(Color.WHITE);

                        System.out.println("FragmentDashboardRisks.responseInsightData====="+mCustomer.getAge()+"======="+ageXaxiesValues.size());
                        System.out.println("FragmentDashboardRisks.responseInsightData====="+ Arrays.toString(ageXaxiesValues.toArray()));

                        for(int i=0;i<=ageXaxiesValues.size()-1;i++){
                            if (ageXaxiesValues.get(i) .equalsIgnoreCase(mCustomer.getAge())) {
                                healthScorePosition=i;
                                break;
                            }
                        }

                        setDataHealthScore(healthScorePosition);
                        healthScorechart.animateX(2500);
                        Legend healthLegend = healthScorechart.getLegend();
                        healthLegend.setForm(Legend.LegendForm.LINE);
                        healthLegend.setTextSize(11f);
                        healthLegend.setTextColor(Color.BLACK);
                        healthLegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        healthLegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        healthLegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        healthLegend.setDrawInside(false);
                        XAxis xAxis = healthScorechart.getXAxis();
                        xAxis.setTextSize(11f);
                        xAxis.setTextColor(getResources().getColor(R.color.card_second));
                        xAxis.setDrawGridLines(false);
                        xAxis.setLabelRotationAngle(45);
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setLabelCount(xAxishealth.length, true);

                        xAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));

                        System.out.println("Age Span Values============" + Arrays.toString(xAxishealth));
                        xAxis.setValueFormatter(new IAxisValueFormatter() {
                            public int getDecimalDigits() {
                                return 0;
                            }

                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return xAxishealth[(int) value % xAxishealth.length];
                            }
                        });

                        YAxis leftAxis = healthScorechart.getAxisLeft();
                        leftAxis.setTextColor(getResources().getColor(R.color.card_second));
                        leftAxis.setDrawGridLines(true);
                        leftAxis.setGranularityEnabled(true);
                        leftAxis.setDrawZeroLine(true);
                        leftAxis.setLabelCount(4, true);
                        leftAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));

                        healthScorechart.getLegend().setEnabled(false);
                        healthScorechart.getAxisRight().setEnabled(false);
                        healthScorechart.animateXY(2000, 2000);
                        healthScorechart.getAxisLeft().setDrawGridLines(true);
                        healthScorechart.getXAxis().setDrawGridLines(true);
                        healthScorechart.setAutoScaleMinMaxEnabled(true);


                    } else {
                        System.out.println("FragmentDashboardRisks.responseInsightData======" + "Health Exception");

                        cardHealthScoreDataGraph.setVisibility(View.GONE);
                    }
                    }catch(Exception e){
                        System.out.println("FragmentDashboardRisks.responseInsightData======" + "Health Exception");
                    }

                    try{

                    if (insightdatamodel.getBmi().getSeries() != null) {
                        System.out.println("FragmentDashboardRisks.responseInsightData======" + "BMI IN");
                        mArrayListBmiData = new ArrayList<BmiSeriese>();
                        mArrayListBmiData = insightdatamodel.getBmi().getSeries();
                        mArrayListBmiDataValue1 = new ArrayList<String>();
                        mArrayListBmiDataValue1 = mArrayListBmiData.get(0).getData();
                        mArrayListBmiDataValue2 = new ArrayList<String>();
                        mArrayListBmiDataValue2 = mArrayListBmiData.get(1).getData();

                        bmitMonthsXaxiesValues = insightdatamodel.getBmi().getCategories_x();
                        mMonthsBmi = new String[bmitMonthsXaxiesValues.size()];
                        mMonthsBmi = bmitMonthsXaxiesValues.toArray(mMonthsBmi);

                        bmichart.setTouchEnabled(false);
                        bmichart.setDragDecelerationFrictionCoef(0.9f);
                        bmichart.setDragEnabled(true);
                        bmichart.setScaleEnabled(true);
                        bmichart.setDrawGridBackground(false);
                        bmichart.setPinchZoom(false);
                        bmichart.setDoubleTapToZoomEnabled(false);
                        bmichart.setBackgroundColor(Color.WHITE);
                        bmichart.getDescription().setEnabled(false);
                        System.out.println("Age======="+customerManager.getCurrentFamilyMember().getAge());
                        if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) < 18) {
                            bmiPosition=0;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 18 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 25) {
                            bmiPosition=1;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 26 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 30) {
                            bmiPosition=2;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 31 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 35) {
                            bmiPosition=3;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 36 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 40) {
                            bmiPosition=4;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 41 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 45) {
                            bmiPosition=5;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 46 && Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) <= 50) {
                            bmiPosition=6;
                        } else if (Integer.parseInt(customerManager.getCurrentFamilyMember().getAge()) >= 55) {
                            bmiPosition=7;
                        }
                        System.out.println("Age======="+bmiPosition);
                        if(mCustomer.getGender().equalsIgnoreCase("Female")){
                            setDataBmiFemale(bmiPosition);
                        }else if(mCustomer.getGender().equalsIgnoreCase("Male")){
                            setDataBmi(bmiPosition);
                        }


                        bmichart.animateX(2500);
                        Legend bmilegend = bmichart.getLegend();
                        bmilegend.setForm(Legend.LegendForm.LINE);
                        bmilegend.setTextSize(11f);
                        bmilegend.setTextColor(Color.BLACK);
                        bmilegend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                        bmilegend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                        bmilegend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                        bmilegend.setDrawInside(false);
                        XAxis xAxisBmi = bmichart.getXAxis();
                        xAxisBmi.setTextSize(11f);
                        xAxisBmi.setTextColor(getResources().getColor(R.color.card_second));
                        xAxisBmi.setDrawGridLines(false);
                        xAxisBmi.setLabelRotationAngle(45);
                        xAxisBmi.setPosition(XAxis.XAxisPosition.BOTTOM);

                        xAxisBmi.setGridColor(getResources().getColor(R.color.chart_border_lines));

                        xAxisBmi.setValueFormatter(new IAxisValueFormatter() {
                            public int getDecimalDigits() {
                                return 0;
                            }

                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return mMonthsBmi[(int) value % mMonthsBmi.length];
                            }
                        });

                        YAxis leftAxisBmi = bmichart.getAxisLeft();
                        leftAxisBmi.setTextColor(getResources().getColor(R.color.card_second));
                        leftAxisBmi.setDrawGridLines(true);
                        leftAxisBmi.setGranularityEnabled(true);
                        leftAxisBmi.setDrawZeroLine(true);
                        leftAxisBmi.setLabelCount(4, true);
                        leftAxisBmi.setGridColor(getResources().getColor(R.color.chart_border_lines));

                        bmichart.getLegend().setEnabled(false);
                        bmichart.getAxisRight().setEnabled(false);
                        bmichart.animateXY(2000, 2000);
                        bmichart.getAxisLeft().setDrawGridLines(true);
                        bmichart.getXAxis().setDrawGridLines(true);
                        bmichart.setAutoScaleMinMaxEnabled(true);
                   /* Highlight highlightbmi = new Highlight(50f,50f, 0);
                    bmichart.highlightValue(highlightbmi, true);*/


                    } else {
                        System.out.println("FragmentDashboardRisks.responseInsightData======" + "BMIException");

                        cardBmiGraph.setVisibility(View.GONE);
                    }
                    }catch(Exception e){
                        System.out.println("FragmentDashboardRisks.responseInsightData======" + "BMIException");
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
                insightMainLayout.setVisibility(View.VISIBLE);
                insightDataLayout.setVisibility(View.GONE);
                System.out.println("FragmentDashboardRisks.responseInsightData======" + "Exception");
            }


        }
    }




    private void setGraphsHeight(boolean isBoy) {
        {
            int minuserHeight = 0;
            int maxuserHeight = 0;

            lineChartHeight.setTouchEnabled(false);
            lineChartHeight.setDragDecelerationFrictionCoef(0.9f);
            lineChartHeight.setDragEnabled(true);
            lineChartHeight.getDescription().setEnabled(false);
            lineChartHeight.setScaleEnabled(true);
            lineChartHeight.setDrawGridBackground(false);
            lineChartHeight.setPinchZoom(false);
            lineChartHeight.setDoubleTapToZoomEnabled(false);
            lineChartHeight.setBackgroundColor(getResources().getColor(R.color.white));

            int monthStart = 0;
            if (ageInt > 0) {
                monthStart = (ageInt * 12) - 7;
            }

            mMonthsChildHeight = new String[5];
            for (int j = monthStart; j < monthStart + 5; j++) {
                // if(j%5==0){
                mMonthsChildHeight[j - monthStart] = "" + (j);
                // }else{
                // mMonths[j] ="";
                // }
                // mMonths[j] = "2015-12-1"+j;
            }
           /* ArrayList<String> xVals = new ArrayList<String>();
            for (int i = 0; i < mMonths.length; i++) {
                xVals.add(mMonths[i]);
                // xVals.add(mMonths[i % 13]);
            }*/

            System.out.println("CheckValues========"+Arrays.toString(mMonthsChildHeight));

            ArrayList<Entry> yVals = new ArrayList<Entry>();

            ArrayList<Entry> aboveLimitVals = new ArrayList<Entry>();
            ArrayList<Entry> averageLimitVals = new ArrayList<Entry>();
            ArrayList<Entry> bellowLimitVals = new ArrayList<Entry>();

            ArrayList<Double> aboveLimitValsMaxHeight = new ArrayList<>();
            ArrayList<Double> averageLimitValsMaxHeight = new ArrayList<>();
            ArrayList<Double> bellowLimitValsMaxHeight = new ArrayList<>();
            ArrayList<Entry> childHeightEntry= new ArrayList<Entry>();
            int currentHeight=Integer.parseInt(mCustomer.getCustomer_vitals().getFeet())*12;
            int currentFeet=Integer.parseInt(mCustomer.getCustomer_vitals().getInches());
            int currentHeightLable=currentHeight+currentFeet;
            System.out.println("FragmentDashboardRisks.setGraphsHeigh========"+currentHeightLable*2.54);

            childHeightEntry.add(new Entry(1, (float) (currentHeightLable*2.54)));


            ArrayList<Float> yValues = new ArrayList<Float>();

            ArrayList<CustomerVitals.Values> filledValue = new ArrayList<CustomerVitals.Values>();

            if (mCustomer != null && mCustomer.getCustomer_vitals() != null) {
                if (mCustomer.getCustomer_vitals().getFormatted_height_trends() != null) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                    String firstDate = "1";
                    for (int k = 0; k < mCustomer.getCustomer_vitals().getFormatted_height_trends().size(); k++) {
                        if (mCustomer.getCustomer_vitals().getFormatted_height_trends().size() > 0) {
                            if (k < mCustomer.getCustomer_vitals().getFormatted_height_trends().size()) {

                                Date secondDate = new Date();
                                try {
                                    secondDate = format.parse(
                                            mCustomer.getCustomer_vitals().getFormatted_weight_trends().get(k).getDate());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // 15770000000 = 6 month
                                // 31540000000 = 1 year
                                long OneMonthMilli = 15770000000L;
                                long currentTimeInMilli = System.currentTimeMillis();
                                long lastYearTimeInMilli = currentTimeInMilli - OneMonthMilli;

                                long TimeInMilisecond = secondDate.getTime();

                                if (TimeInMilisecond > lastYearTimeInMilli) {
                                    if (isMonthGrater(
                                            mCustomer.getCustomer_vitals().getFormatted_height_trends().get(k).getDate(),
                                            firstDate)) {
                                        if (filledValue.size() > 6) {
                                            filledValue.remove(0);
                                        }
                                        filledValue.add(mCustomer.getCustomer_vitals().getFormatted_height_trends().get(k));
                                    } else {
                                        if (filledValue.size() > 0) {
                                            filledValue.set(filledValue.size() - 1,
                                                    mCustomer.getCustomer_vitals().getFormatted_height_trends().get(k));
                                        } else {
                                            filledValue.add(0,
                                                    mCustomer.getCustomer_vitals().getFormatted_height_trends().get(k));
                                        }
                                    }
                                    firstDate = mCustomer.getCustomer_vitals().getFormatted_height_trends().get(k)
                                            .getDate();
                                }
                            }
                        }
                    }
                }
            }

            for (int k = 0; k < filledValue.size(); k++) {
                Log.e("Height USer", filledValue.get(k).getValue());
                if (filledValue.size() > 0) {

                    if (k < filledValue.size()) {
                        float value = (float) (Float.parseFloat(filledValue.get(k).getValue()) * 2.54);
                        if (minuserHeight == 0 && maxuserHeight == 0) {
                            minuserHeight = (int) value;
                            maxuserHeight = (int) value;
                        } else if (minuserHeight > (int) value) {
                            minuserHeight = (int) value;
                        } else if (maxuserHeight < (int) value) {
                            maxuserHeight = (int) value;
                        }
                        yVals.add(new Entry(value, k));
                        yValues.add(Float.parseFloat(filledValue.get(k).getValue()));

                    } else {
                        yVals.add(new Entry(-1, 0));
                        yValues.add(-1f);
                    }
                }
            }

            int newK = 0;

            double maxHeight = 0;
            double minHeight = 0;

            if (ageInt > 0) {
                newK = (12 * ageInt) - 7;
                if (newK < 0) {
                    newK = 0;
                }
                if (newK > 210) {
                    newK = 210;
                }
            }

            if (isBoy) {
                minHeight =  TagValues.boys_heights_under_average[newK];
                for (int k = 0; k < mMonthsChildHeight.length; k++) {
                    if (newK <  TagValues.boys_heights_above_average.length) {
                        aboveLimitValsMaxHeight.add(TagValues.boys_heights_above_average[newK]);
                        averageLimitValsMaxHeight.add(TagValues.boys_heights_avg[newK]);
                        bellowLimitValsMaxHeight.add(TagValues.boys_heights_under_average[newK]);
                        aboveLimitVals.add(new Entry((float)  TagValues.boys_heights_above_average[newK], k));
                        averageLimitVals.add(new Entry((float)  TagValues.boys_heights_avg[newK], k));
                        bellowLimitVals.add(new Entry((float)  TagValues.boys_heights_under_average[newK], k));
                    }
                    newK = newK + 1;
                }
                maxHeight =  TagValues.boys_heights_above_average[newK];
            } else {
                minHeight =  TagValues.girls_heights_under_average[newK];
                for (int k = 0; k < mMonthsChildHeight.length; k++) {
                    if (newK <  TagValues.girls_heights_above_average.length) {
                        aboveLimitValsMaxHeight.add(TagValues.girls_heights_above_average[newK]);
                        averageLimitValsMaxHeight.add(TagValues.girls_heights_avg[newK]);
                        bellowLimitValsMaxHeight.add(TagValues.girls_heights_under_average[newK]);
                        aboveLimitVals.add(new Entry((float)  TagValues.girls_heights_above_average[newK], k));
                        averageLimitVals.add(new Entry((float)  TagValues.girls_heights_avg[newK], k));
                        bellowLimitVals.add(new Entry((float)  TagValues.girls_heights_under_average[newK], k));
                    }
                    newK = newK + 1;
                }
                maxHeight =  TagValues.girls_heights_above_average[newK];
            }

            if (maxuserHeight > maxHeight) {
                maxHeight = maxuserHeight;
            }

            Float x = 0f;
            if (yValues.size() > 0) {
                x = Collections.max(yValues);
            } else {
                x = 100f;
            }
            if (x < 100) {
                x = 100f;
            }

            if (minuserHeight < minHeight) {
                minHeight = minuserHeight;
            }



            LineDataSet set1Height;
            if (lineChartHeight.getData() != null &&
                    lineChartHeight.getData().getDataSetCount() > 0) {
                set1Height = (LineDataSet) lineChartHeight.getData().getDataSetByIndex(0);
                set1Height.setValues(childHeightEntry);
                lineChartHeight.getData().notifyDataChanged();
                lineChartHeight.notifyDataSetChanged();
            } else {
                set1Height = new LineDataSet(childHeightEntry, "Height");
                set1Height.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1Height.setColor(getResources().getColor(R.color.green));
                set1Height.setLineWidth(5f);
                set1Height.setCircleRadius(3f);
                set1Height.setFillAlpha(65);
                set1Height.setFillColor(getResources().getColor(R.color.green));
                set1Height.setDrawCircleHole(true);
                set1Height.setCircleColor(getResources().getColor(R.color.green));

                LineData dataHealth = new LineData(set1Height);
                dataHealth.setDrawValues(false);
                dataHealth.setValueTextColor(Color.BLACK);
                dataHealth.setValueTextSize(11f);
                // set data
                lineChartHeight.setData(dataHealth);
                lineChartHeight.invalidate();

            }
            lineChartHeight.animateX(2500);
            Legend l = lineChartHeight.getLegend();
            l.setForm(Legend.LegendForm.LINE);
            l.setTextSize(11f);
            l.setTextColor(Color.RED);
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);

            double aboveHeight = Collections.max(aboveLimitValsMaxHeight);
            float aboveHeightLImit = (float) aboveHeight;
            double avgHeight = Collections.max(averageLimitValsMaxHeight);
            float avgHeightLImit = (float) avgHeight;
            double belowHeight = Collections.max(bellowLimitValsMaxHeight);
            float belowHeightLImit = (float) belowHeight;

            LimitLine ll1 = new LimitLine(aboveHeightLImit, " ");
            ll1.setLineWidth(1f);
            ll1.setLineColor(getResources().getColor(R.color.red));
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
            ll1.setTextSize(7f);

            LimitLine ll3 = new LimitLine(avgHeightLImit, " ");
            ll3.setLineWidth(1f);
            ll3.setLineColor(getResources().getColor(R.color.green));
            ll3.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
            ll3.setTextSize(7f);

            LimitLine ll2 = new LimitLine(belowHeightLImit, " ");
            ll2.setLineWidth(1f);
            ll2.setLineColor(getResources().getColor(R.color.yellow));
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
            ll2.setTextSize(7f);

            XAxis xAxisHeight = lineChartHeight.getXAxis();
            xAxisHeight.setTextSize(11f);
            xAxisHeight.setTextColor(getResources().getColor(R.color.card_second));
            xAxisHeight.setDrawGridLines(false);
            xAxisHeight.setLabelRotationAngle(45);
            xAxisHeight.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxisHeight.setLabelCount(mMonthsChildHeight.length, true);
            xAxisHeight.setValueFormatter(new IAxisValueFormatter() {
                public int getDecimalDigits() {
                    return 0;
                }
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return mMonthsChildHeight[(int)value % mMonthsChildHeight.length];
                }
            });

            YAxis leftAxisHeight = lineChartHeight.getAxisLeft();
            leftAxisHeight.setTextColor(getResources().getColor(R.color.card_second));
            leftAxisHeight.setStartAtZero(false);
            leftAxisHeight.setAxisMaxValue((float) maxHeight + (int) Math.ceil((maxHeight - minHeight) * 0.1));
            leftAxisHeight.setAxisMinValue((float) minHeight - (int) Math.ceil((maxHeight - minHeight) * 0.1));
            leftAxisHeight.setDrawGridLines(true);
            leftAxisHeight.setGranularityEnabled(true);
            leftAxisHeight.setLabelCount(4, true);
            leftAxisHeight.addLimitLine(ll1);
            leftAxisHeight.addLimitLine(ll2);
            leftAxisHeight.addLimitLine(ll3);


            YAxis rightAxisHeight = lineChartHeight.getAxisRight();
            rightAxisHeight.setTextColor(getResources().getColor(R.color.card_second));
            rightAxisHeight.setAxisMinValue(0);
            rightAxisHeight.setAxisMaxValue(100);
            rightAxisHeight.setDrawGridLines(false);
            rightAxisHeight.setDrawZeroLine(false);
            rightAxisHeight.setGranularityEnabled(false);

            lineChartHeight.getAxisRight().setEnabled(false);
            lineChartHeight.animateXY(2000, 2000);
            lineChartHeight.getAxisLeft().setDrawGridLines(false);
            lineChartHeight.getXAxis().setDrawGridLines(false);
            lineChartHeight.setMaxVisibleValueCount(4);






        }



    }

    private void setGraphsChildWeight(boolean isBoy) {

        if (ageInt > 0) {
            monthStart = (ageInt * 12) - 7;
        }
        mMonthsChildWeight = new String[5];
        for (int j = monthStart; j < monthStart + 5; j++) {
            mMonthsChildWeight[j - monthStart] = "" + (j);
        }
        lineChartWeight.setTouchEnabled(false);
        lineChartWeight.setDragDecelerationFrictionCoef(0.9f);
        lineChartWeight.setDragEnabled(true);
        lineChartWeight.getDescription().setEnabled(false);
        lineChartWeight.setScaleEnabled(true);
        lineChartWeight.setDrawGridBackground(false);
        lineChartWeight.setPinchZoom(false);
        lineChartWeight.setDoubleTapToZoomEnabled(false);
        lineChartWeight.setBackgroundColor(getResources().getColor(R.color.white));
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        ArrayList<Entry> aboveLimitVals = new ArrayList<Entry>();
        ArrayList<Entry> averageLimitVals = new ArrayList<Entry>();
        ArrayList<Entry> bellowLimitVals = new ArrayList<Entry>();
        ArrayList<Double> aboveLimitMaxWeightVals = new ArrayList<>();
        ArrayList<Double> averageLimitMaxWeightVals = new ArrayList<>();
        ArrayList<Double> bellowLimitMaxWeightVals = new ArrayList<>();
        ArrayList<Float> yValues = new ArrayList<Float>();
        ArrayList<Entry> childWeightEntry= new ArrayList<Entry>();
        childWeightEntry.add(new Entry(1, Integer.parseInt(mCustomer.getCustomer_vitals().getWeight())));

        ArrayList<CustomerVitals.Values> filledValue = new ArrayList<CustomerVitals.Values>();

        if (mCustomer != null && mCustomer.getCustomer_vitals() != null) {
            if (mCustomer.getCustomer_vitals().getFormatted_weight_trends() != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                String firstDate = "1";
                // if(mCustomer.getCustomer_vitals().getFormatted_weight_trends().size()>12){
                for (int k = 0; k < mCustomer.getCustomer_vitals().getFormatted_weight_trends().size(); k++) {
                    if (mCustomer.getCustomer_vitals().getFormatted_weight_trends().size() > 0) {

                        if (k < mCustomer.getCustomer_vitals().getFormatted_weight_trends().size()) {

                            Date secondDate = null;
                            try {
                                secondDate = format.parse(
                                        mCustomer.getCustomer_vitals().getFormatted_weight_trends().get(k).getDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            // 15770000000 = 6 month
                            // 31540000000 = 1 year
                            long OneMonthMilli = 15770000000L;
                            long currentTimeInMilli = System.currentTimeMillis();
                            long lastYearTimeInMilli = currentTimeInMilli - OneMonthMilli;

                            long TimeInMilisecond = secondDate.getTime();

                            if (TimeInMilisecond > lastYearTimeInMilli) {
                                if (isMonthGrater(
                                        mCustomer.getCustomer_vitals().getFormatted_weight_trends().get(k).getDate(),
                                        firstDate)) {
                                    if (filledValue.size() > 6) {
                                        filledValue.remove(0);
                                    }
                                    filledValue.add(mCustomer.getCustomer_vitals().getFormatted_weight_trends().get(k));
                                } else {
                                    if (filledValue.size() > 0) {
                                        filledValue.set(filledValue.size() - 1,
                                                mCustomer.getCustomer_vitals().getFormatted_weight_trends().get(k));
                                    } else {
                                        filledValue.add(0,
                                                mCustomer.getCustomer_vitals().getFormatted_weight_trends().get(k));
                                    }
                                }
                                firstDate = mCustomer.getCustomer_vitals().getFormatted_weight_trends().get(k)
                                        .getDate();
                            }

                        }
                    }
                }

            }
        }

        for (int k = 0; k < filledValue.size(); k++) {
            if (filledValue.size() > 0) {

                if (k < filledValue.size()) {
                    float value = Float.parseFloat(filledValue.get(k).getValue());
                    if (minuserWeight == 0 && maxuserWeight == 0) {
                        minuserWeight = (int) value;
                        maxuserWeight = (int) value;
                    } else if (minuserWeight > (int) value) {
                        minuserWeight = (int) value;
                    } else if (maxuserWeight < (int) value) {
                        maxuserWeight = (int) value;
                    }
                    yVals.add(new Entry(value, k));
                    yValues.add(Float.parseFloat(filledValue.get(k).getValue()));

                } else {
                    yVals.add(new Entry(-1, 0));
                    yValues.add(-1f);
                }
            }
        }

        double minWeight = 0;
        double maxWeight = 0;

        int newK = 0;
        if (ageInt > 0) {
            newK = (12 * ageInt) - 7;
        }
        if (isBoy) {
            minWeight = TagValues.boys_weights_underweight[newK];
            for (int k = 0; k < mMonths.length; k++) {
                if (newK < TagValues. boys_weights_overweight.length) {
                    aboveLimitMaxWeightVals.add(TagValues.boys_weights_overweight[newK]);
                    averageLimitMaxWeightVals.add(TagValues.boys_weights_avg[newK]);
                    bellowLimitMaxWeightVals.add(TagValues.boys_weights_underweight[newK]);
                    aboveLimitVals.add(new Entry((float)  TagValues.boys_weights_overweight[newK], k));
                    averageLimitVals.add(new Entry((float)  TagValues.boys_weights_avg[newK], k));
                    bellowLimitVals.add(new Entry((float)  TagValues.boys_weights_underweight[newK], k));
                }

                newK = newK + 1;
            }
            maxWeight =  TagValues.boys_weights_overweight[newK];

        } else {
            minWeight =  TagValues.girls_weights_underweight[newK];
            for (int k = 0; k < mMonths.length; k++) {
                if (newK <  TagValues.girls_weights_overweight.length) {
                    aboveLimitMaxWeightVals.add(TagValues.girls_weights_overweight[newK]);
                    averageLimitMaxWeightVals.add(TagValues.girls_weights_avg[newK]);
                    bellowLimitMaxWeightVals.add(TagValues.girls_weights_underweight[newK]);
                    aboveLimitVals.add(new Entry((float)  TagValues.girls_weights_overweight[newK], k));
                    averageLimitVals.add(new Entry((float) TagValues. girls_weights_avg[newK], k));
                    bellowLimitVals.add(new Entry((float)  TagValues.girls_weights_underweight[newK], k));
                }
                newK = newK + 1;
            }
            maxWeight =  TagValues.girls_weights_overweight[newK];
        }

        if (maxuserWeight > maxWeight) {
            maxWeight = maxuserWeight;
        }

        Float x = 0f;
        if (yValues.size() > 0) {
            // x = Collections.max(yValues);
            x = (float) maxWeight;
        } else {
            x = 0f;
        }

		/*
         * if(yValues.size() > 0){ minWeight = Collections.min(yValues);
		 * if(minuserWeight < minWeight){ minWeight = minuserWeight; } }
		 */

        if (minuserWeight < minWeight) {
            minWeight = minuserWeight;
        }
        //nssajfjafs
        System.out.println("FragmentDashboardRisks.setGraphsHeight============"+Collections.max(aboveLimitMaxWeightVals)+"====="+
                Collections.max(averageLimitMaxWeightVals)+"======"+Collections.max(bellowLimitMaxWeightVals));

        LineDataSet set1Weights;
        if (lineChartWeight.getData() != null &&
                lineChartWeight.getData().getDataSetCount() > 0) {
            set1Weights = (LineDataSet) lineChartWeight.getData().getDataSetByIndex(0);
            set1Weights.setValues(childWeightEntry);
            lineChartWeight.getData().notifyDataChanged();
            lineChartWeight.notifyDataSetChanged();
        } else {
            set1Weights = new LineDataSet(childWeightEntry, "Weight");
            set1Weights.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1Weights.setColor(getResources().getColor(R.color.green));
            set1Weights.setLineWidth(5f);
            set1Weights.setCircleRadius(3f);
            set1Weights.setFillAlpha(65);
            set1Weights.setFillColor(getResources().getColor(R.color.green));
            set1Weights.setDrawCircleHole(true);
            set1Weights.setCircleColor(getResources().getColor(R.color.green));

            LineData dataHealth = new LineData(set1Weights);
            dataHealth.setDrawValues(false);
            dataHealth.setValueTextColor(Color.BLACK);
            dataHealth.setValueTextSize(11f);
            // set data
            lineChartWeight.setData(dataHealth);
            lineChartWeight.invalidate();

        }
        lineChartWeight.animateX(2500);
        Legend l = lineChartWeight.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.RED);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        System.out.println("myarray above==========="+Arrays.toString(aboveLimitMaxWeightVals.toArray()));
        System.out.println("myarray average==========="+Arrays.toString(averageLimitMaxWeightVals.toArray()));
        System.out.println("myarray above==========="+Arrays.toString(aboveLimitMaxWeightVals.toArray()));

        double aboveWeight = Collections.max(aboveLimitMaxWeightVals);
        float aboveWeightLImit = (float) aboveWeight;
        double avgWeight = Collections.max(averageLimitMaxWeightVals);
        float avgWeightLImit = (float) avgWeight;
        double belowWeight = Collections.max(bellowLimitMaxWeightVals);
        float belowWeightLImit = (float) belowWeight;
        LimitLine ll1 = new LimitLine(aboveWeightLImit, " ");
        ll1.setLineWidth(1f);
        ll1.setLineColor(getResources().getColor(R.color.red));
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        ll1.setTextSize(7f);

        LimitLine ll3 = new LimitLine(avgWeightLImit, " ");
        ll3.setLineWidth(1f);
        ll3.setLineColor(getResources().getColor(R.color.green));
        ll3.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        ll3.setTextSize(7f);

        LimitLine ll2 = new LimitLine(belowWeightLImit, " ");
        ll2.setLineWidth(1f);
        ll2.setLineColor(getResources().getColor(R.color.yellow));
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        ll2.setTextSize(7f);

        XAxis xAxis = lineChartWeight.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(getResources().getColor(R.color.card_second));
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(45);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        System.out.println("FragmentDashboardRisks.setGraphsChildWeight=========="+Arrays.toString(mMonthsChildWeight));
        xAxis.setLabelCount(mMonthsChildWeight.length, true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            public int getDecimalDigits() {
                return 0;
            }
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mMonthsChildWeight[(int)value % mMonthsChildWeight.length];
            }
        });

        YAxis leftAxis = lineChartWeight.getAxisLeft();
        leftAxis.setTextColor(getResources().getColor(R.color.card_second));
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue((float) maxWeight + (int) Math.ceil((maxWeight - minWeight) * 0.1));
        leftAxis.setAxisMinValue((float) minWeight - (int) Math.ceil((maxWeight - minWeight) * 0.1));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setLabelCount(4, true);
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.addLimitLine(ll3);


        YAxis rightAxis = lineChartWeight.getAxisRight();
        rightAxis.setTextColor(getResources().getColor(R.color.card_second));
        rightAxis.setAxisMinValue(0);
        rightAxis.setAxisMaxValue(100);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

        lineChartWeight.getAxisRight().setEnabled(false);
        lineChartWeight.animateXY(2000, 2000);
        lineChartWeight.getAxisLeft().setDrawGridLines(false);
        lineChartWeight.getXAxis().setDrawGridLines(false);

    }

    @SuppressLint("SimpleDateFormat")
    private boolean isMonthGrater(String strFirstDates, String strSecondDate) {
        boolean isMonthGreater = false;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        // String strFirstDate = format.format(currentDate);
        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = format.parse(strFirstDates);
            secondDate = format.parse(strSecondDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar startCalendar = new GregorianCalendar();
        if (firstDate != null)
            startCalendar.setTime(firstDate);

        Calendar endCalendar = new GregorianCalendar();
        if (secondDate != null)
            endCalendar.setTime(secondDate);

        System.out.println("------month difference end " + endCalendar.get(Calendar.MONTH) + " start "
                + startCalendar.get(Calendar.MONTH));

        if (startCalendar.get(Calendar.YEAR) != endCalendar.get(Calendar.YEAR)) {
            isMonthGreater = true;
        } else if (startCalendar.get(Calendar.MONTH) != endCalendar.get(Calendar.MONTH)) {
            isMonthGreater = true;
        } else {
            isMonthGreater = false;
        }

        if (firstDate == null)
            isMonthGreater = true;

        if (secondDate == null)
            isMonthGreater = true;

        return isMonthGreater;
    }

    private void setDataHealthScore(int healthScorePosition) {
        try{
        healthScoreDataset1 = new ArrayList<Entry>();
        for (int i = 0; i < mArrayListhealthScoreDataValue1.size(); i++) {
            if(healthScoreDataset1.contains(mArrayListhealthScoreDataValue1.get(i))){

            }else {
                healthScoreDataset1.add(new Entry(i, Integer.parseInt(mArrayListhealthScoreDataValue1.get(i))));
            }
        }
        healthScorebmiDataset2 = new ArrayList<Entry>();
        for (int i = 0; i < mArrayListhealthScoreDataValue2.size(); i++) {
            if(healthScorebmiDataset2.contains(mArrayListhealthScoreDataValue2.get(i))){

            }else {
                healthScorebmiDataset2.add(new Entry(i, Integer.parseInt(mArrayListhealthScoreDataValue2.get(i))));
            }
        }
        healthScorebmiDataset3 = new ArrayList<Entry>();
        healthScorebmiDataset3.add(new Entry(healthScorePosition, Integer.parseInt(mCustomer.getHealth_score())));

        LineDataSet set1health,set2health,set3health;
        if (healthScorechart.getData() != null &&
                healthScorechart.getData().getDataSetCount() > 0) {
            set1health = (LineDataSet) healthScorechart.getData().getDataSetByIndex(0);
            set2health = (LineDataSet) healthScorechart.getData().getDataSetByIndex(1);
            set3health = (LineDataSet) healthScorechart.getData().getDataSetByIndex(2);
            set1health.setValues(healthScoreDataset1);
            set2health.setValues(healthScorebmiDataset2);
            set3health.setValues(healthScorebmiDataset3);
            healthScorechart.getData().notifyDataChanged();
            healthScorechart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1health = new LineDataSet(healthScoreDataset1, "Best score"+" ");
            set1health.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1health.setColor(getResources().getColor(R.color.red));
            set1health.setLineWidth(3f);
            set1health.setCircleRadius(3f);
            set1health.setFillAlpha(65);
            set1health.setFillColor(getResources().getColor(R.color.red));
            set1health.setDrawCircleHole(true);
            set1health.setDrawValues(false);
            set1health.setCircleColor(getResources().getColor(R.color.red));

            set2health = new LineDataSet(healthScorebmiDataset2, "Worst score"+" ");
            set2health.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2health.setColor(getResources().getColor(R.color.colorPrimary));
            set2health.setLineWidth(3f);
            set2health.setCircleRadius(3f);
            set2health.setFillAlpha(65);
            set2health.setFillColor(getResources().getColor(R.color.colorPrimary));
            set2health.setDrawCircleHole(true);
            set2health.setDrawValues(false);
            set2health.setCircleColor(getResources().getColor(R.color.colorPrimary));

            set3health = new LineDataSet(healthScorebmiDataset3, "You");
            set3health.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3health.setColor(getResources().getColor(R.color.green));
            set3health.setLineWidth(5f);
            set3health.setCircleRadius(5f);
            set3health.setFillAlpha(65);
            set3health.setFillColor(getResources().getColor(R.color.green));
            set3health.setDrawCircleHole(true);
            set3health.setDrawValues(true);
            set3health.setCircleColor(getResources().getColor(R.color.green));

            LineData dataHealth = new LineData(set1health, set2health,set3health);
            dataHealth.setValueTextColor(getResources().getColor(R.color.green));
            dataHealth.setValueTextSize(14f);
            //dataHealth.setDrawValues(false);
            // set data
            healthScorechart.setData(dataHealth);
            healthScorechart.invalidate();
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setDataBmiFemale(int bmiPosition) {
        try {
            bmiFemaleLayout.setVisibility(View.VISIBLE);
            bmiMaleLayout.setVisibility(View.GONE);
            bmiDataset2 = new ArrayList<Entry>();
            for (int i = 0; i < mArrayListBmiDataValue2.size(); i++) {
                bmiDataset2.add(new Entry(i, Integer.parseInt(mArrayListBmiDataValue2.get(i))));
            }
            bmiDataset3 = new ArrayList<Entry>();
            try {
                  bmi_text = CalculationUtility.calculateBMI(
                        Integer.parseInt(mCustomer.getCustomer_vitals().getFeet()),
                        Integer.parseInt(mCustomer.getCustomer_vitals().getInches()),
                        Double.parseDouble(mCustomer.getCustomer_vitals().getWeight()));
                bmiDataset3.add(new Entry(bmiPosition, Integer.parseInt(String.valueOf(bmi_text.intValue()))));
            } catch (NumberFormatException e) {
            }
            LineDataSet set2bmi, set3bmi;
            if (bmichart.getData() != null &&
                    bmichart.getData().getDataSetCount() > 0) {
                set2bmi = (LineDataSet) bmichart.getData().getDataSetByIndex(0);
                set3bmi = (LineDataSet) bmichart.getData().getDataSetByIndex(1);
                set2bmi.setValues(bmiDataset2);
                set3bmi.setValues(bmiDataset3);
                bmichart.getData().notifyDataChanged();
                bmichart.notifyDataSetChanged();
                bmichart.invalidate();
            } else {

                set2bmi = new LineDataSet(bmiDataset2, "Female");
                set2bmi.setAxisDependency(YAxis.AxisDependency.LEFT);
                set2bmi.setColor(getResources().getColor(R.color.colorPrimary));
                set2bmi.setLineWidth(3f);
                set2bmi.setCircleRadius(3f);
                set2bmi.setFillAlpha(65);
                set2bmi.setFillColor(getResources().getColor(R.color.colorPrimary));
                set2bmi.setDrawCircleHole(true);
                set2bmi.setDrawValues(false);
                set2bmi.setCircleColor(getResources().getColor(R.color.colorPrimary));

                set3bmi = new LineDataSet(bmiDataset3, "You");
                set3bmi.setAxisDependency(YAxis.AxisDependency.LEFT);
                set3bmi.setColor(getResources().getColor(R.color.green));
                set3bmi.setLineWidth(5f);
                set3bmi.setCircleRadius(5f);
                set3bmi.setFillAlpha(65);
                set3bmi.setFillColor(getResources().getColor(R.color.green));
                set3bmi.setDrawCircleHole(false);
                set3bmi.setDrawValues(true);
                set3bmi.setCircleColor(getResources().getColor(R.color.green));
                LineData dataBmi = new LineData(set2bmi, set3bmi);
                //dataBmi.setDrawValues(false);
                dataBmi.setValueTextColor(getResources().getColor(R.color.green));
                dataBmi.setValueTextSize(14f);
                bmichart.setData(dataBmi);
                bmichart.invalidate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void setDataBmi(int bmiPosition) {
        try {
            bmiFemaleLayout.setVisibility(View.GONE);
            bmiMaleLayout.setVisibility(View.VISIBLE);
            bmiDataset1 = new ArrayList<Entry>();
            for (int i = 0; i < mArrayListBmiDataValue1.size(); i++) {
                bmiDataset1.add(new Entry(i, Integer.parseInt(mArrayListBmiDataValue1.get(i))));
            }
            bmiDataset3 = new ArrayList<Entry>();
            try {
                bmi_text = CalculationUtility.calculateBMI(
                        Integer.parseInt(mCustomer.getCustomer_vitals().getFeet()),
                        Integer.parseInt(mCustomer.getCustomer_vitals().getInches()),
                        Double.parseDouble(mCustomer.getCustomer_vitals().getWeight()));

                bmiDataset3.add(new Entry(bmiPosition, Integer.parseInt(String.valueOf(bmi_text.intValue()))));
            } catch (NumberFormatException e) {
            }
            LineDataSet set1bmi, set3bmi;
            if (bmichart.getData() != null &&
                    bmichart.getData().getDataSetCount() > 0) {
                set1bmi = (LineDataSet) bmichart.getData().getDataSetByIndex(0);
                set3bmi = (LineDataSet) bmichart.getData().getDataSetByIndex(1);
                set1bmi.setValues(bmiDataset1);
                set3bmi.setValues(bmiDataset3);
                bmichart.getData().notifyDataChanged();
                bmichart.notifyDataSetChanged();
                bmichart.invalidate();
            } else {
                // create a dataset and give it a type
                set1bmi = new LineDataSet(bmiDataset1, "Male");
                set1bmi.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1bmi.setColor(getResources().getColor(R.color.colorPrimary));
                set1bmi.setLineWidth(3f);
                set1bmi.setCircleRadius(3f);
                set1bmi.setFillAlpha(65);
                set1bmi.setFillColor(getResources().getColor(R.color.colorPrimary));
                set1bmi.setDrawCircleHole(true);
                set1bmi.setCircleColor(getResources().getColor(R.color.colorPrimary));
                set1bmi.setDrawValues(false);


                set3bmi = new LineDataSet(bmiDataset3, "You");
                set3bmi.setAxisDependency(YAxis.AxisDependency.LEFT);
                set3bmi.setColor(getResources().getColor(R.color.green));
                set3bmi.setLineWidth(5f);
                set3bmi.setCircleRadius(5f);
                set3bmi.setFillAlpha(65);
                set3bmi.setFillColor(getResources().getColor(R.color.green));
                set3bmi.setDrawCircleHole(true);
                set3bmi.setDrawValues(true);
                set3bmi.setCircleColor(getResources().getColor(R.color.green));


                LineData dataBmi = new LineData(set1bmi, set3bmi);
                //dataBmi.setDrawValues(false);
                dataBmi.setValueTextColor(getResources().getColor(R.color.green));
                dataBmi.setValueTextSize(14f);
                // set data
                bmichart.setData(dataBmi);
                bmichart.invalidate();

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDataFemailWaist(int waistPosition) {
            try {
                System.out.println("checkgender====" + "setDataWaist");
                waistFemaleLayout.setVisibility(View.VISIBLE);
                waistmaleLayout.setVisibility(View.GONE);
                waistdataset2 = new ArrayList<Entry>();
                for (int i = 0; i < mArrayListWaistDataValue2.size(); i++) {
                    waistdataset2.add(new Entry(i, Integer.parseInt(mArrayListWaistDataValue2.get(i))));
                }

                waistdataset3 = new ArrayList<Entry>();
                try {
                    waistdataset3.add(new Entry(waistPosition, Integer.parseInt(mCustomer.getCustomer_vitals().getWaist())));
                } catch (NumberFormatException e) {
                    waistdataset3.add(new Entry(waistPosition, 0));
                }

                LineDataSet set4, set3;
                if (waistchart.getData() != null &&
                        waistchart.getData().getDataSetCount() > 0) {
                    set4 = (LineDataSet) waistchart.getData().getDataSetByIndex(0);
                    set3 = (LineDataSet) waistchart.getData().getDataSetByIndex(1);
                    set4.setValues(waistdataset2);
                    set3.setValues(waistdataset3);
                    waistchart.getData().notifyDataChanged();
                    waistchart.notifyDataSetChanged();
                    waistchart.invalidate();
                } else {
                    set4 = new LineDataSet(waistdataset2, "Male");
                    set4.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set4.setColor(getResources().getColor(R.color.colorPrimary));
                    set4.setLineWidth(3f);
                    set4.setCircleRadius(3f);
                    set4.setFillAlpha(65);
                    set4.setFillColor(getResources().getColor(R.color.colorPrimary));
                    set4.setDrawCircleHole(true);
                    set4.setDrawValues(false);
                    set4.setCircleColor(getResources().getColor(R.color.colorPrimary));

                    set3 = new LineDataSet(waistdataset3, "You");
                    set3.setAxisDependency(YAxis.AxisDependency.LEFT);
                    set3.setColor(getResources().getColor(R.color.green));
                    set3.setLineWidth(5f);
                    set3.setCircleRadius(5f);
                    set3.setFillAlpha(65);
                    set3.setFillColor(getResources().getColor(R.color.green));
                    set3.setDrawCircleHole(true);
                    set3.setDrawValues(true);
                    set3.setCircleColor(getResources().getColor(R.color.green));
                    LineData data = new LineData(set4, set3);
                    //data.setDrawValues(false);
                    data.setValueTextColor(getResources().getColor(R.color.green));
                    data.setValueTextSize(14f);
                    waistchart.setData(data);
                    waistchart.invalidate();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

    }


    private void setDataWaist(int waistPosition) {
        try {
            System.out.println("checkgender====" + "setDataWaist");
            waistFemaleLayout.setVisibility(View.GONE);
            waistmaleLayout.setVisibility(View.VISIBLE);
            waistdataset1 = new ArrayList<Entry>();
            for (int i = 0; i < mArrayListWaistDataValue1.size(); i++) {
                waistdataset1.add(new Entry(i, Integer.parseInt(mArrayListWaistDataValue1.get(i))));
            }

            waistdataset3 = new ArrayList<Entry>();
            try {
                waistdataset3.add(new Entry(waistPosition, Integer.parseInt(mCustomer.getCustomer_vitals().getWaist())));
            } catch (NumberFormatException e) {
                waistdataset3.add(new Entry(waistPosition, 0));
            }

            LineDataSet set1, set3;
            if (waistchart.getData() != null &&
                    waistchart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) waistchart.getData().getDataSetByIndex(0);
                set3 = (LineDataSet) waistchart.getData().getDataSetByIndex(1);
                set1.setValues(waistdataset1);
                set3.setValues(waistdataset3);
                waistchart.getData().notifyDataChanged();
                waistchart.notifyDataSetChanged();
                waistchart.invalidate();
            } else {
                set1 = new LineDataSet(waistdataset1, "Male");
                set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                set1.setColor(getResources().getColor(R.color.colorPrimary));
                set1.setLineWidth(3f);
                set1.setCircleRadius(3f);
                set1.setFillAlpha(65);
                set1.setFillColor(getResources().getColor(R.color.colorPrimary));
                set1.setDrawCircleHole(true);
                set1.setDrawValues(false);
                set1.setCircleColor(getResources().getColor(R.color.colorPrimary));

                set3 = new LineDataSet(waistdataset3, "You");
                set3.setAxisDependency(YAxis.AxisDependency.LEFT);
                set3.setColor(getResources().getColor(R.color.green));
                set3.setLineWidth(5f);
                set3.setCircleRadius(5f);
                set3.setFillAlpha(65);
                set3.setFillColor(getResources().getColor(R.color.green));
                set3.setDrawCircleHole(true);
                set3.setDrawValues(true);
                set3.setCircleColor(getResources().getColor(R.color.green));
                LineData data = new LineData(set1, set3);
                //data.setDrawValues(false);
                data.setValueTextColor(getResources().getColor(R.color.green));
                data.setValueTextSize(14f);
                waistchart.setData(data);
                waistchart.invalidate();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
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

    public void refreshRiskFragment()
    {
        System.out.println("FragmentDashboardRisks.refreshRiskFragment");
        customerManager = CustomerManager.getInstance(getActivity().getApplicationContext());
        mCustomer = customerManager.getCurrentFamilyMember();

        if(mCustomer!=null &&
                mCustomer.getDate_of_birth()!=null &&
                mCustomer.getAge()!=null &&
                mCustomer.getGender()!=null )
        {
            System.out.println("FragmentDashboardRisks.refreshRiskFragment age is present");

            relativeLayoutMissingHra.setVisibility(View.GONE);
            frameLayoutFIlledHra.setVisibility(View.VISIBLE);

            if (NetworkCaller.isInternetOncheck(getActivity())) {
                if(mCustomer!=null){
                    System.out.println("keys===========" + prefs.getCustomerKey() + "===" + prefs.getEkinKey() + "===" +
                            customerManager.getDeviceID(getActivity())+
                            "==========="+customerManager.getCurrentFamilyMember().getIdentification_token()+"======"
                    );
                    requestToInsightData();
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            System.out.println("FragmentDashboardRisks.onCreateView isLoggedIn="+(customerManager.getCurrentCustomer() == customerManager.getCurrentFamilyMember()));

            if(mCustomer.getIdentification_token().equals(customerManager.getCurrentCustomer().getIdentification_token())){
                isGoogleFitVisible(true);
            }else{
                isGoogleFitVisible(false);
            }
        }else{
            System.out.println("FragmentDashboardRisks.refreshRiskFragment age is not present");

            relativeLayoutMissingHra.setVisibility(View.VISIBLE);
            frameLayoutFIlledHra.setVisibility(View.GONE);
        }



    }

    public void isGoogleFitConnected(boolean b, String text) {
        if(b){
            linearLayoutInstalled.setVisibility(View.VISIBLE);
            linearLayoutNotInstalled.setVisibility(View.GONE);
        }else{
            linearLayoutInstalled.setVisibility(View.GONE);
            linearLayoutNotInstalled.setVisibility(View.VISIBLE);
            unconnectedToGoogleFitlabel.setText(text);
        }
    }

    public void isGoogleFitVisible(boolean b) {
        try{
            if(b){
                cardGoogleFit.setVisibility(View.VISIBLE);
            }else{
                cardGoogleFit.setVisibility(View.GONE);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }



    public void showStepsData(DataSet dataSet) {
        System.out.println("FragmentDashboardRisks.showStepsData dataSet="+dataSet.toString());
        for (DataPoint dp : dataSet.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                Value todaySteps = dp.getValue(field);
                updateTextViewWithStepCounter(todaySteps.asInt());
            }
        }
    }

    public void showCaloriesData(DataSet dataSet) {
        for (DataPoint dp : dataSet.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                Value todayCalories = dp.getValue(field);
                updateTextViewWithCaloriesCounter(todayCalories.asFloat());
            }
        }
    }


    public void showWeeklyStepsDataSet(DataSet dataSet) {
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

    public void showWeeklyCaloriesDataSet(DataSet dataSet) {
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


    private void updateTextViewWithStepCounter(final int numberOfSteps) {
        System.out.println("FragmentDashboardRisks.updateTextViewWithStepCounter");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("FragmentDashboardRisks.updateTextViewWithStepCounter steps=========="+String.valueOf(numberOfSteps));
                    gfitdaySteps.setText(String.valueOf(numberOfSteps));
                    prefs.setStepsCount(String.valueOf(numberOfSteps));

                }catch(NullPointerException e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void updateWeekCaloriesData(final float numberOfSteps, final String data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("FragmentDashboardRisks.run==========="+data);
                String[] resultCalories;
                if(data.contains("-")){
                    resultCalories = data.split("-");
                    if(weeklyCaloriesDatesList.size()<7){
                        weeklyCaloriesDatesList.add(resultCalories[0]);
                    }else{

                    }

                }else if(data.contains("/")){
                    resultCalories = data.split("/");
                    if(weeklyCaloriesDatesList.size()<7){
                        weeklyCaloriesDatesList.add(resultCalories[0]);
                    }else{

                    }
                }else if(data.contains(" ")){
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

                System.out.println("FragmentDashboardRisks.runlist=========="+Arrays.toString(weeklyCaloriesDatesList.toArray()));
                System.out.println("hdsdhsha==========="+Arrays.toString(weeklyCaloriesDatesListFullFormat.toArray()));


            }
        });
    }

    private void updateWeekData(final int numberOfSteps, final String data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("FragmentDashboardRisks.run==========="+data);
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

                }else if(data.contains(" ")){
                    result = data.split("\\s+");
                    if(weeklyStepsDatesList.size()<7){
                        System.out.println("FragmentDashboardRisks.run==========="+result[0]);
                        weeklyStepsDatesList.add(result[0]);
                    }else{

                    }
                }

                System.out.println("data=============="+data);

                if(weeklyStepsDatesListFullFormat.size()<7){
                    weeklyStepsDatesListFullFormat.add(DateUtility.googleFitDate(data));
                }else{

                }

                if(weeklyStepsList.size()<7){
                    weeklyStepsList.add(numberOfSteps);
                }else{

                }
                System.out.println("hdsdhsha==========="+Arrays.toString(weeklyStepsDatesListFullFormat.toArray()));
                weeklyStepsArray = weeklyStepsList.toArray(new Integer[weeklyStepsList.size()]);
                tinydb.putListInt("WeekSteps", weeklyStepsList);
                tinydb.putListString("Dayss", weeklyStepsDatesList);
                tinydb.putListString("StepsFullDateFormat", weeklyStepsDatesListFullFormat);





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

    public void clearGFitData() {
        System.out.println("FragmentDashboardRisks.clearGFitData");
        refresGgoogleFitData.clearAnimation();
        refresGgoogleFitData.setEnabled(true);
        weeklyStepsDatesListPost.clear();
        weeklyCaloriesList.clear();
        weeklyStepsDatesList.clear();
        weeklyStepsDatesListFullFormat.clear();
        weeklyCaloriesDatesListFullFormat.clear();
        weeklyStepsList.clear();
        weeklyCaloriesDatesList.clear();

    }


    public class InsightHealthRiskAdapter extends BaseAdapter {
        Context context;
        LayoutInflater mInflater;
        ViewHolder holder = null;
        List<HealthRisksData> mArrayListAdapter;
        String chatURL;

        public InsightHealthRiskAdapter(Context context, List<HealthRisksData> mArrayListAdapter) {
            this.context = context;
            this.mArrayListAdapter = mArrayListAdapter;
        }

        @Override
        public int getCount() {
            // System.out.println("PackageSearchListAdapter2.getCount===========getcount==" + mArrayListAdapter.size());
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
            LayoutInflater mInflater = (LayoutInflater)
                    getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.insight_health_risk_adapter_row, null);
                holder.insightTagname = (TextView) convertView.findViewById(R.id.insight_health_tag_name);
                holder.insightTagPercentage = (TextView) convertView.findViewById(R.id.insight_health_risk_percentage);
                holder.insightTagBehaviour = (TextView) convertView.findViewById(R.id.insight_health_type_behaviour);
                holder.insightStatusImage = (ImageView) convertView.findViewById(R.id.insight_health_status_image);
                holder.insightTagLevels=(TextView)convertView.findViewById(R.id.insight_health_type_leveles) ;
                holder.dividerhealth=(View)convertView.findViewById(R.id.devider);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            try{
                holder.insightTagname.setText(mArrayListAdapter.get(position).getName());
                holder.insightTagPercentage.setText(mArrayListAdapter.get(position).getPercentage()+"%");
                holder.insightTagLevels.setText("have high "+mArrayListAdapter.get(position).getTag());
                if(position == mArrayListAdapter.size()-1){
                    holder.dividerhealth.setVisibility(View.GONE);
                }
                if(mArrayListAdapter.get(position).getColor().equalsIgnoreCase("red")){
                    holder.insightStatusImage.setBackgroundResource(R.drawable.ic_warning_24px);
                    holder.insightTagBehaviour.setTextColor(Color.parseColor("#f73838"));
                    holder.insightTagBehaviour.setText("is not normal");
                }else if(mArrayListAdapter.get(position).getColor().equalsIgnoreCase("yellow")){
                    holder.insightStatusImage.setBackgroundResource(R.drawable.ic_upload_24px);
                    holder.insightTagBehaviour.setTextColor(Color.parseColor("#fe4c5a"));
                    holder.insightTagBehaviour.setText("is missing");
                }else if(mArrayListAdapter.get(position).getColor().equalsIgnoreCase("green")){
                    holder.insightStatusImage.setBackgroundResource(R.drawable.ic_green_tick_24px);
                    holder.insightTagBehaviour.setTextColor(Color.parseColor("#15c26b"));
                    holder.insightTagBehaviour.setText("is normal");
                }else if(mArrayListAdapter.get(position).getColor().equalsIgnoreCase("upload")){
                    holder.insightStatusImage.setBackgroundResource(R.drawable.ic_upload_24px);
                    holder.insightTagBehaviour.setTextColor(Color.parseColor("#9a9a9a"));
                    holder.insightTagBehaviour.setText("is missing");
                }

            }catch(NullPointerException e){
                e.printStackTrace();
            }

            return convertView;
        }

        public class ViewHolder {

            TextView insightTagname;
            TextView insightTagPercentage;
            TextView insightTagBehaviour;
            ImageView insightStatusImage;
            TextView insightTagLevels;
            View dividerhealth;

        }

    }

}
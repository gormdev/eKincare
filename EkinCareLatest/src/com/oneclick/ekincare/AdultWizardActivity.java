package com.oneclick.ekincare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.ProfileData;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by RaviTejaN on 18-10-2016.
 */

public class AdultWizardActivity extends AppCompatActivity implements DialogWeightInterface,DialogHeightInterface,DialogWaistInterface,DialogBloodPressureInterface,DialogBloodGlcousInterface {

    private static final String DIABETICS = "Diabetics";
    private static final String HYPERTNSION = "Hypertnsion";
    private static final String HEARTCONDITION = "Heartcondition";
    private static final String CANCER = "Cancer";
    private static final String NONE = "";

    private RelativeLayout mLayoutMainQuestions;
    private PreferenceHelper prefs;



    private LinearLayout lifeStyleViewWizard,familyViewWizard;

    private RelativeLayout moreInfoView;
    int stepSize = 1;

    TextView txtbloodgroupA;
    TextView txtbloodgroupB;
    TextView txtbloodgroupAB;
    TextView txtbloodgroupO;

    TextView txtbloodgroupA_;
    TextView txtbloodgroupB_;
    TextView txtbloodgroupAB_;
    TextView txtbloodgroupO_;

    CheckBox diabetics;
    CheckBox cancer;
    CheckBox heartdisease;
    CheckBox none;
    CheckBox hypertension;

    CheckBox fdiabetics;
    CheckBox fcancer;
    CheckBox fheartdisease;
    CheckBox fnone;
    CheckBox fhypertension;

    TextView updateBloodSugarTextView,updateBloodSugarDimension,systolicDiastolicSeparation,systolicDiastolicDimensions;
    CheckBox IdontKnowBp;
    TextView systolic;
    TextView diastolic;
    CheckBox IdontKnowBs;
    CheckBox bloodPresure;
    CheckBox Hypertension;
    CheckBox Diabetes;
    CheckBox cbNone;
    String WeghtText="";
    String strInches="";
    String strFfeetvalue="";
    String strWiast="";
    String strSmoke="";
    String strAlcohol="";
    String strExcersize="";
    String checkedValuesFather="";
    String checkedValuesMother="";
    String strBloodSugar="";
    int bloodGroupId=0;
    int questionNumber=0;
    int TotalCount=0;
    private String strFamilyMemberKey="";
    RelativeLayout.LayoutParams params;
    private ProfileManager mProfileManager;
    TextView nextButton;
    Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;
    Toolbar toolbar;
    private LinearLayout heightWwightLayoutView,waistLayoutView;
    TextView wizardWeightValue,wizardHeightfeetValue,wizardHeightInchValue,wizardWasitValue;
    TextView wizardWeightValueDimension,wizardHeightfeetValueDimension,wizardHeightInchValueDimension,wizardWasitValueDimension;
    private com.getbase.floatingactionbutton.FloatingActionButton nextButtonView;
    SeekBar exerciseSeekbar,smokingSeekbar,alcholoSeekbar;
    TextView seekbarExerciseValue,seekbarSmokingValu,seekbarAlcholoValue;
    LinearLayout weightLayoutClick,heightLayoutClick,waistLayoutClick,wizerdBloodpressureLayout,wizerdBloodGlucoseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_questions_two);

        prefs = new PreferenceHelper(getApplicationContext());


        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            questionNumber = bundle.getInt("question");
            strFamilyMemberKey = bundle.getString("FamilyMemberKey");
        }
        System.out.println("customerkey========="+prefs.getIsFIrstWizard()+"==="+prefs.getCustomerKey()+"===="+prefs.getEkinKey()+"==="+strFamilyMemberKey);

        nextButton=(TextView) findViewById(R.id.profile_next_two);
        nextButtonView=(com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.wizard_next_layout);
        mLayoutMainQuestions = (RelativeLayout)findViewById(R.id.ll_main_questions);

        initViews();

        setUpToolBar();

        nextButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(questionNumber==1)
                {
                    WeghtText = wizardWeightValue.getText().toString();
                    strInches = wizardHeightInchValue.getText().toString();
                    strFfeetvalue = wizardHeightfeetValue.getText().toString();
                    if(WeghtText.isEmpty()){
                        valadiationDialog2(getResources().getString(R.string.weight_validation_msg));
                    }else if(strFfeetvalue.isEmpty()){
                        valadiationDialog2(getResources().getString(R.string.height_validation_msg));
                    }else if(strInches.isEmpty()){
                        valadiationDialog2(getResources().getString(R.string.height_validation_msg));
                    }else {
                        questionNumber = 2;
                        TotalCount = 1;
                        hideKeyboard(waistLayoutView);
                        mLayoutMainQuestions.addView(waistLayoutView,params);

                    }
                }else if(questionNumber==2 || questionNumber==3){

                    WeghtText = wizardWeightValue.getText().toString();
                    strInches = wizardHeightInchValue.getText().toString();
                    strFfeetvalue = wizardHeightfeetValue.getText().toString();
                    strWiast = wizardWasitValue.getText().toString();

                    if(WeghtText.isEmpty())
                    {
                        valadiationDialog2(getResources().getString(R.string.weight_validation_msg));
                    }else if(strFfeetvalue.isEmpty())
                    {
                        valadiationDialog2(getResources().getString(R.string.height_validation_msg));
                    }
                    else if(strInches.isEmpty())
                    {
                        valadiationDialog2(getResources().getString(R.string.height_validation_msg));
                    }
                    else if(strWiast.isEmpty())
                    {
                        valadiationDialog2(getResources().getString(R.string.waist_error));
                    }
                    else
                    {
                        JSONObject object = new JSONObject();
                        JSONObject objectWizard = new JSONObject();
                        try {

                            object.put("weight", WeghtText);
                            object.put("feet", strFfeetvalue);
                            object.put("inches", strInches);
                            //			object.put("profileId", BasicCustomer.getId());
                            object.put("waist", strWiast);
                            //object.put("blood_group_id", bloodGroupId);
                            objectWizard.put("wizard", object);
                            objectWizard.put("calculate_health_score", "false");

                        }catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        String requestString = objectWizard.toString();
                        SendWizardData(objectWizard);
                    }
                }else if(questionNumber==4){
                    strAlcohol = seekbarAlcholoValue.getText().toString();
                    strSmoke=seekbarSmokingValu.getText().toString();
                    strExcersize=seekbarExerciseValue.getText().toString();

                    if(strAlcohol.isEmpty()){
                        valadiationDialog2("Please Select Any Alcohol Option");
                    }else if(strSmoke.isEmpty()){
                        valadiationDialog2("Please Select Any Smoke Option");
                    }else if(strExcersize.isEmpty()){
                        valadiationDialog2("Please Select Any Excercise Option");
                    }else{
                        JSONObject object = new JSONObject();
                        JSONObject objectWizard = new JSONObject();
                        try {

                            //							object.put("smoking", strSmoke);
                            object.put("smoke", seekbarSmokingValu.getText().toString());
                            //				object.put("profileId", BasicCustomer.getId());
                            object.put("alcohol", seekbarAlcholoValue.getText().toString());
                            object.put("exercise", seekbarExerciseValue.getText().toString());
                            objectWizard.put("wizard", object);
                            objectWizard.put("calculate_health_score", "false");

                        }catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                        String requestString = objectWizard.toString();
                        SendWizardData(objectWizard);
                    }
                }else if(questionNumber==5){

                    if (none.isChecked() || cancer.isChecked() || diabetics.isChecked() || heartdisease.isChecked() || hypertension.isChecked()) {
                        checkedValuesFather = "";
                        if (none.isChecked()) {
                            checkedValuesFather = NONE;
                        } else {
                            if (cancer.isChecked()) {
                                checkedValuesFather = CANCER + ",";
                            }
                            if (diabetics.isChecked()) {
                                checkedValuesFather = checkedValuesFather + DIABETICS + ",";
                            }
                            if (heartdisease.isChecked()) {
                                checkedValuesFather = checkedValuesFather + HEARTCONDITION + ",";
                            }
                            if (hypertension.isChecked()) {
                                checkedValuesFather = checkedValuesFather + HYPERTNSION + ",";
                            }
                        }

                        if (fnone.isChecked() || fcancer.isChecked() || fdiabetics.isChecked() || fheartdisease.isChecked() || hypertension.isChecked()) {
                            checkedValuesMother = "";
                            if (fnone.isChecked()) {
                                checkedValuesMother = NONE;
                            } else {
                                if (fcancer.isChecked()) {
                                    checkedValuesMother = CANCER + ",";
                                }
                                if (fdiabetics.isChecked()) {
                                    checkedValuesMother = checkedValuesMother + DIABETICS + ",";
                                }
                                if (fheartdisease.isChecked()) {
                                    checkedValuesMother = checkedValuesMother + HEARTCONDITION + ",";
                                }
                                if (fhypertension.isChecked()) {
                                    checkedValuesMother = checkedValuesMother + HYPERTNSION + ",";
                                }
                            }

                            System.out.println("checkedValuesFather======"+checkedValuesFather);
                            System.out.println("checkedValuesMother======"+checkedValuesMother);
                            JSONObject object = new JSONObject();
                            JSONObject objectWizard = new JSONObject();
                            try {
                                object.put("father_medical_history", checkedValuesFather);
                                object.put("mother_medical_history", checkedValuesMother);
                                objectWizard.put("wizard", object);
                                objectWizard.put("calculate_health_score", "false");

                            }catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            String requestString = objectWizard.toString();
                            SendWizardData(objectWizard);
                        } else
                        {
                            valadiationDialog2("Please check atleast one");
                        }
                    }
                    else
                    {
                        valadiationDialog2("Please check atleast one");
                    }


                }else if(questionNumber==6){

                    if (cbNone.isChecked() || bloodPresure.isChecked() || Hypertension.isChecked() || Diabetes.isChecked()) {

                        boolean isBloodPressure=false;
                        if (bloodPresure.isChecked()) {
                            isBloodPressure=true;
                        }
                        boolean isHuperTension=false;
                        if (Hypertension.isChecked()) {
                            isHuperTension=true;
                        }
                        boolean isDiabetes=false;
                        if (Diabetes.isChecked()) {
                            isDiabetes=true;
                        }

                        strBloodSugar = updateBloodSugarTextView.getText().toString();

                        if (!strBloodSugar.isEmpty() || IdontKnowBs.isChecked()) {

                            String dia = diastolic.getText().toString();
                            String sys = systolic.getText().toString();
                            if ((!dia.isEmpty() && !sys.isEmpty()) || IdontKnowBp.isChecked()) {

                                JSONObject objectWizard = new JSONObject();
                                try {

                                    JSONObject object = new JSONObject();
                                    object.put("result", strBloodSugar);
                                    //				object.put("profileId", BasicCustomer.getId());
                                    objectWizard.put("lab_result", object);

                                    JSONObject object2 = new JSONObject();
                                    object2.put("systolic", sys);
                                    object2.put("diastolic", dia);
                                    objectWizard.put("blood_pressure", object2);

                                    objectWizard.put("father_medical_history", checkedValuesFather);

                                    JSONObject objectTreatment = new JSONObject();
                                    objectTreatment.put("diabetic_treatment", isDiabetes);
                                    objectTreatment.put("hypertensive_treatment", isHuperTension);
                                    //									objectTreatment.put("blood_pressure_treatment", isBloodPressure);
                                    objectTreatment.put("cvd_treatment", isBloodPressure);
                                    objectWizard.put("wizard", objectTreatment);

                                    objectWizard.put("calculate_health_score", true);

                                }catch (JSONException e1) {
                                    e1.printStackTrace();
                                }

                                String requestString = objectWizard.toString();
                                SendWizardData(objectWizard);

                            } else {
                                valadiationDialog2(getResources().getString(R.string.bp_validation_msg));
                            }
                        } else {
                            valadiationDialog2("Please enter Blood Sugar");
                        }
                    } else {
                        valadiationDialog2("Please check atleast one Treated");
                    }
                }


            }
        });
    }

    private void setUpToolBar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("PROFILE SETUP");

    }

    /*public void gotoBack(){
        mMainActivity.mTextViewTitle.setText(getString(R.string.overview));

        mMainActivity.onBackPressed();

        //		if (RefreshListener != null)
        //			RefreshListener.onWizardCompleteRefresh(true);
    }*/
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        exitDialog();
    }

    private void exitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AdultWizardActivity.this);

        builder.setTitle("Incomplete Data!");
        builder.setMessage("You have not filled data.Are you sure about leaving the process?");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AdultWizardActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
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


    private void initViews() {

        toolbar=(Toolbar)findViewById(R.id.toolbar) ;
        toolbar.setNavigationIcon(R.drawable.navagation_launch);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TotalCount = 1;
        initHeightWeightView();
        initWaistBloodGroupView();
        initWizardLifeStyleView();
        initWizardFamilyView();

        initMoreInfoView();

        exerciseSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int) Math.round(progress / stepSize)) * stepSize;
                if(progress>=0 && progress<40){
                    seekbarExerciseValue.setText("Never");
                }else if(progress>=40 && progress<70){
                    seekbarExerciseValue.setText("Occasionally");
                }else if(progress>=70 && progress<=100){
                    seekbarExerciseValue.setText("Frequently");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Smoking Seekbar

        smokingSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int) Math.round(progress / stepSize)) * stepSize;
                if(progress>=0 && progress<40){
                    seekbarSmokingValu.setText("Never");
                }else if(progress>=40 && progress<70){
                    seekbarSmokingValu.setText("Occasionally");
                }else if(progress>=70 && progress<=100){
                    seekbarSmokingValu.setText("Frequently");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Alcholo seekbar

        alcholoSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int) Math.round(progress / stepSize)) * stepSize;
                if(progress>=0 && progress<40){
                    seekbarAlcholoValue.setText("Never");
                }else if(progress>=40 && progress<70){
                    seekbarAlcholoValue.setText("Occasionally");
                }else if(progress>=70 && progress<=100){
                    seekbarAlcholoValue.setText("Frequently");
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        waistLayoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogWaistPicker newFragment = new DialogWaistPicker(AdultWizardActivity.this,waistValuesList());
                newFragment.show(AdultWizardActivity.this.getSupportFragmentManager(), "");
            }
        });

        weightLayoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("AdultWizardActivity.onClick====="+"yes");
                DialogWeightPicker newFragment = new DialogWeightPicker(AdultWizardActivity.this,waightValuesList());
                newFragment.show(AdultWizardActivity.this.getSupportFragmentManager(), "");
            }
        });
        heightLayoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHeightPicker newFragment = new DialogHeightPicker(AdultWizardActivity.this,heightFeetList(),heightInchList());
                newFragment.show(AdultWizardActivity.this.getSupportFragmentManager(), "");

            }
        });

        wizerdBloodGlucoseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBloodGlucosePicker newFragment = new DialogBloodGlucosePicker(AdultWizardActivity.this,glucouseList());
                newFragment.show(AdultWizardActivity.this.getSupportFragmentManager(), "");
            }
        });
        wizerdBloodpressureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBloodPressurePicker newFragment = new DialogBloodPressurePicker(AdultWizardActivity.this,systolicList(),diastolicList());
                newFragment.show(AdultWizardActivity.this.getSupportFragmentManager(), "");
            }
        });


        nextButton.setText("Next");
        if(questionNumber==1 || questionNumber==2){
            questionNumber = questionNumber+0;
            mLayoutMainQuestions.addView(heightWwightLayoutView,params);
        }else if(questionNumber==3){
            questionNumber = questionNumber+1;
            mLayoutMainQuestions.addView(lifeStyleViewWizard,params);
        }else if(questionNumber==4){
            questionNumber = questionNumber+1;
            mLayoutMainQuestions.addView(familyViewWizard,params);
        }else if(questionNumber==5){
            nextButton.setText("Done");
            questionNumber = questionNumber+1;
            mLayoutMainQuestions.addView(moreInfoView,params);
        }



    }
    private ArrayList<String> glucouseList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 50; i < 300; i++) {
            list.add("" + i);
        }
        return list;
    }

    private ArrayList<String> systolicList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 80; i < 200; i++) {
            list.add("" + i);
        }
        return list;
    }
    private ArrayList<String> diastolicList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 60; i < 110; i++) {
            list.add("" + i);
        }
        return list;
    }
    private ArrayList<String> heightFeetList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i < 8; i++) {
                list.add("" + i);
        }
        return list;
    }
    private ArrayList<String> heightInchList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 12; i++) {
                list.add("" + i);

        }
        return list;
    }

    private ArrayList<String> waightValuesList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 1; i < 220; i++) {
            if (i < 10) {
                list.add("0" + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private ArrayList<String> waistValuesList() {
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 26; i < 48; i=i+2) {
            if (i < 26) {
                list.add(" " + i);
            } else {
                list.add("" + i);
            }
        }
        return list;
    }

    private void initWizardFamilyView() {
        familyViewWizard  = (LinearLayout) View.inflate(AdultWizardActivity.this, R.layout.family_history_wizard, null);
        diabetics = (CheckBox) familyViewWizard.findViewById(R.id.diabetes_checkbox);
        cancer = (CheckBox) familyViewWizard.findViewById(R.id.cancer_checkbox);
        heartdisease = (CheckBox) familyViewWizard.findViewById(R.id.cardio_disease_checkbox);
        none = (CheckBox) familyViewWizard.findViewById(R.id.none_checkbox);
        hypertension = (CheckBox) familyViewWizard.findViewById(R.id.hypertension_checkbox);

        cancer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    none.setChecked(false);
            }
        });
        diabetics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    none.setChecked(false);
            }
        });
        heartdisease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    none.setChecked(false);
            }
        });
        hypertension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    none.setChecked(false);
            }
        });

        none.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    cancer.setChecked(false);
                    diabetics.setChecked(false);
                    heartdisease.setChecked(false);
                    hypertension.setChecked(false);

                    //					cancer.setEnabled(false);
                    //					diabetics.setEnabled(false);
                    //					heartdisease.setEnabled(false);
                    //					hypertension.setEnabled(false);
                } else {

                    //					cancer.setEnabled(true);
                    //					diabetics.setEnabled(true);
                    //					heartdisease.setEnabled(true);
                    //					hypertension.setEnabled(true);
                }
            }
        });


        fdiabetics = (CheckBox) familyViewWizard.findViewById(R.id.diabetes_checkboxFather);
        fcancer = (CheckBox) familyViewWizard.findViewById(R.id.cancer_checkboxFather);
        fheartdisease = (CheckBox) familyViewWizard.findViewById(R.id.cardio_disease_checkboxFather);
        fnone = (CheckBox) familyViewWizard.findViewById(R.id.none_checkboxFather);
        fhypertension = (CheckBox) familyViewWizard.findViewById(R.id.hypertension_checkboxFather);

        fcancer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fnone.setChecked(false);
            }
        });
        fdiabetics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fnone.setChecked(false);
            }
        });
        fheartdisease.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fnone.setChecked(false);
            }
        });
        fhypertension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    fnone.setChecked(false);
            }
        });


        fnone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    fcancer.setChecked(false);
                    fdiabetics.setChecked(false);
                    fheartdisease.setChecked(false);
                    fhypertension.setChecked(false);

                    //					fcancer.setEnabled(false);
                    //					fdiabetics.setEnabled(false);
                    //					fheartdisease.setEnabled(false);
                    //					fhypertension.setEnabled(false);
                } else {
                    //					fcancer.setEnabled(true);
                    //					fdiabetics.setEnabled(true);
                    //					fheartdisease.setEnabled(true);
                    //					fhypertension.setEnabled(true);
                }
            }
        });



    }

    private void initWizardLifeStyleView() {
        lifeStyleViewWizard = (LinearLayout)View.inflate(AdultWizardActivity.this,R.layout.life_style_wizard, null);

        seekbarExerciseValue = (TextView)lifeStyleViewWizard.findViewById(R.id.exercies_seek_bar_value);
        seekbarSmokingValu = (TextView)lifeStyleViewWizard.findViewById(R.id.smoking_seek_bar_value);
        seekbarAlcholoValue = (TextView)lifeStyleViewWizard.findViewById(R.id.alcohol_seek_bar_value);

        exerciseSeekbar=(SeekBar)lifeStyleViewWizard.findViewById(R.id.seek_bar_exercise);
        alcholoSeekbar=(SeekBar)lifeStyleViewWizard.findViewById(R.id.seek_bar_alcohol);
        smokingSeekbar=(SeekBar)lifeStyleViewWizard.findViewById(R.id.seek_bar_smoking);



    }

    private void initWaistBloodGroupView() {
        waistLayoutView = (LinearLayout)View.inflate(AdultWizardActivity.this, R.layout.wizard_waist_blood_group_layout, null);
        wizardWasitValue=(TextView)waistLayoutView.findViewById(R.id.waist_wizard_value);
        wizardWasitValueDimension=(TextView)waistLayoutView.findViewById(R.id.waist_wizard_value_dimension);
        waistLayoutClick=(LinearLayout)waistLayoutView.findViewById(R.id.wizerd_waist_layout);
        txtbloodgroupA  = (TextView)waistLayoutView.findViewById(R.id.txtbloodgroupA);
        txtbloodgroupB  = (TextView)waistLayoutView.findViewById(R.id.txtbloodgroupB);
        txtbloodgroupAB = (TextView)waistLayoutView.findViewById(R.id.txtbloodgroupAB);
        txtbloodgroupO  = (TextView)waistLayoutView.findViewById(R.id.txtbloodgroupO);

        txtbloodgroupA_ = (TextView)waistLayoutView.findViewById(R.id.txtbloodgroupA_);
        txtbloodgroupB_ = (TextView)waistLayoutView.findViewById(R.id.txtbloodgroupB_);
        txtbloodgroupAB_= (TextView)waistLayoutView.findViewById(R.id.txtbloodgroupAB_);
        txtbloodgroupO_ = (TextView)waistLayoutView.findViewById(R.id.txtbloodgroupO_);

        txtbloodgroupA.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
        txtbloodgroupA_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
        txtbloodgroupB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
        txtbloodgroupB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
        txtbloodgroupAB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
        txtbloodgroupAB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
        txtbloodgroupO.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
        txtbloodgroupO_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);

        txtbloodgroupA.setTextColor(ContextCompat.getColor(this, R.color.card_second));
        txtbloodgroupA_.setTextColor(ContextCompat.getColor(this, R.color.card_second));
        txtbloodgroupB.setTextColor(ContextCompat.getColor(this, R.color.card_second));
        txtbloodgroupB_.setTextColor(ContextCompat.getColor(this, R.color.card_second));
        txtbloodgroupAB.setTextColor(ContextCompat.getColor(this, R.color.card_second));
        txtbloodgroupAB_.setTextColor(ContextCompat.getColor(this, R.color.card_second));
        txtbloodgroupO.setTextColor(ContextCompat.getColor(this, R.color.card_second));
        txtbloodgroupO_.setTextColor(ContextCompat.getColor(this, R.color.card_second));

        txtbloodgroupA.setTag(1);
        txtbloodgroupA_.setTag(2);
        txtbloodgroupB.setTag(3);
        txtbloodgroupB_.setTag(4);
        txtbloodgroupAB.setTag(5);
        txtbloodgroupAB_.setTag(6);
        txtbloodgroupO.setTag(7);
        txtbloodgroupO_.setTag(8);

        txtbloodgroupA.setOnClickListener(new AdultWizardActivity.BloodGroupClickListener());
        txtbloodgroupB.setOnClickListener(new AdultWizardActivity.BloodGroupClickListener());
        txtbloodgroupAB.setOnClickListener(new AdultWizardActivity.BloodGroupClickListener());
        txtbloodgroupO.setOnClickListener(new AdultWizardActivity.BloodGroupClickListener());
        txtbloodgroupA_.setOnClickListener(new AdultWizardActivity.BloodGroupClickListener());
        txtbloodgroupB_.setOnClickListener(new AdultWizardActivity.BloodGroupClickListener());
        txtbloodgroupAB_.setOnClickListener(new AdultWizardActivity.BloodGroupClickListener());
        txtbloodgroupO_.setOnClickListener(new AdultWizardActivity.BloodGroupClickListener());

    }

    private void initHeightWeightView() {
        heightWwightLayoutView = (LinearLayout)View.inflate(AdultWizardActivity.this, R.layout.height_weight_wizard_layout, null);
        wizardWeightValue=(TextView) heightWwightLayoutView.findViewById(R.id.weight_wizard_value);
        weightLayoutClick=(LinearLayout)heightWwightLayoutView.findViewById(R.id.wizerd_weight_layout);
        heightLayoutClick=(LinearLayout)heightWwightLayoutView.findViewById(R.id.wizerd_height_layout);
        wizardHeightfeetValue = (TextView) heightWwightLayoutView.findViewById(R.id.height_feet_value_wizard);
        wizardHeightInchValue = (TextView) heightWwightLayoutView.findViewById(R.id.height_inch_value_wizard);
        wizardWeightValueDimension=(TextView) heightWwightLayoutView.findViewById(R.id.weight_wizard_value_dimension);
        wizardHeightfeetValueDimension = (TextView) heightWwightLayoutView.findViewById(R.id.height_feet_value_wizard_dimension);
        wizardHeightInchValueDimension = (TextView) heightWwightLayoutView.findViewById(R.id.height_inch_value_wizard_dimension);


    }

    @Override
    public void setFeet(String data, int position) {
        wizardHeightfeetValue.setText(data);
        wizardHeightfeetValueDimension.setText("Ft");

    }

    @Override
    public void setInch(String data, int position) {
        wizardHeightInchValue.setText(data);
        wizardHeightInchValueDimension.setText("Inch");
    }

    @Override
    public void setWeight(String data, int position) {
        wizardWeightValue.setText(data);
        wizardWeightValueDimension.setText("Kgs");
    }

    @Override
    public void setWaist(String data, int position) {
        wizardWasitValue.setText(data);
        wizardWasitValueDimension.setText("Inch");
    }

    @Override
    public void setBloodGlcouse(String data, int position) {
        updateBloodSugarTextView.setText(data);
        updateBloodSugarDimension.setText("mg/dl");
    }



    @Override
    public void setBloodSystolic(String data, int position) {
        systolic.setText(data);
    }

    @Override
    public void setBloodDiastolic(String data, int position) {
        diastolic.setText(data);
        systolicDiastolicSeparation.setText("/");
        systolicDiastolicDimensions.setText("mm Hg");
    }

    private class BloodGroupClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.txtbloodgroupA:
                    txtbloodgroupA.setBackgroundResource(R.drawable.blood_group_selected_drawable);
                    txtbloodgroupB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupA_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.white));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));

                    break;
                case R.id.txtbloodgroupA_:
                    txtbloodgroupA.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupA_.setBackgroundResource(R.drawable.blood_group_selected_drawable);
                    txtbloodgroupB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.white));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));


                    break;
                case R.id.txtbloodgroupB:
                    txtbloodgroupA.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB.setBackgroundResource(R.drawable.blood_group_selected_drawable);
                    txtbloodgroupAB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupA_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.white));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));

                    break;
                case R.id.txtbloodgroupB_:
                    txtbloodgroupA.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupA_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB_.setBackgroundResource(R.drawable.blood_group_selected_drawable);
                    txtbloodgroupAB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.white));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));


                    break;
                case R.id.txtbloodgroupAB:
                    txtbloodgroupA.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB.setBackgroundResource(R.drawable.blood_group_selected_drawable);
                    txtbloodgroupO.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupA_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.white));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));

                    break;
                case R.id.txtbloodgroupAB_:
                    txtbloodgroupA.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupA_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB_.setBackgroundResource(R.drawable.blood_group_selected_drawable);
                    txtbloodgroupO_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.white));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));

                    break;
                case R.id.txtbloodgroupO:
                    txtbloodgroupA.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO.setBackgroundResource(R.drawable.blood_group_selected_drawable);
                    txtbloodgroupA_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.white));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));

                    break;
                case R.id.txtbloodgroupO_:
                    txtbloodgroupA.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupA_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupAB_.setBackgroundResource(R.drawable.blood_group_unselected_drawable);
                    txtbloodgroupO_.setBackgroundResource(R.drawable.blood_group_selected_drawable);

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(AdultWizardActivity.this, R.color.white));

                    break;
            }

        /*    txtbloodgroupA.setHintTextColor(v.getResources().getColor(R.color.title_textcolor));
            txtbloodgroupB.setHintTextColor(v.getResources().getColor(R.color.title_textcolor));
            txtbloodgroupAB.setHintTextColor(v.getResources().getColor(R.color.title_textcolor));
            txtbloodgroupO.setHintTextColor(v.getResources().getColor(R.color.title_textcolor));
            txtbloodgroupA_.setHintTextColor(v.getResources().getColor(R.color.title_textcolor));
            txtbloodgroupB_.setHintTextColor(v.getResources().getColor(R.color.title_textcolor));
            txtbloodgroupAB_.setHintTextColor(v.getResources().getColor(R.color.title_textcolor));
            txtbloodgroupO_.setHintTextColor(v.getResources().getColor(R.color.title_textcolor));

            txtbloodgroupA.setBackgroundResource(R.drawable.wizard_multiitem_selectore);
            txtbloodgroupB.setBackgroundResource(R.drawable.wizard_multiitem_selectore);
            txtbloodgroupAB.setBackgroundResource(R.drawable.wizard_multiitem_selectore);
            txtbloodgroupO.setBackgroundResource(R.drawable.wizard_multiitem_selectore);
            txtbloodgroupA_.setBackgroundResource(R.drawable.wizard_multiitem_selectore);
            txtbloodgroupB_.setBackgroundResource(R.drawable.wizard_multiitem_selectore);
            txtbloodgroupAB_.setBackgroundResource(R.drawable.wizard_multiitem_selectore);
            txtbloodgroupO_.setBackgroundResource(R.drawable.wizard_multiitem_selectore);

            ((TextView)v).setBackgroundResource(R.color.themeColor);
            ((TextView)v).setHintTextColor(v.getResources().getColor(R.color.white));*/

            bloodGroupId = Integer.parseInt(v.getTag().toString());
            System.out.println("bloodid========"+bloodGroupId);

        }
    }


    public void initMoreInfoView(){

        moreInfoView = (RelativeLayout)View.inflate(AdultWizardActivity.this, R.layout.fragment_bloodpressure_lyout, null);
        wizerdBloodpressureLayout=(LinearLayout)moreInfoView.findViewById(R.id.wizerd_bloodpressure_layout);
        wizerdBloodGlucoseLayout=(LinearLayout)moreInfoView.findViewById(R.id.wizerd_bloodGlucose_layout);
        updateBloodSugarTextView = (TextView)moreInfoView.findViewById(R.id.update_bloodsugar);
        systolicDiastolicSeparation=(TextView)moreInfoView.findViewById(R.id.systolic_diastolic_separation);
        systolicDiastolicDimensions=(TextView)moreInfoView.findViewById(R.id.systolic_diastolic_dimension);
        updateBloodSugarDimension=(TextView)moreInfoView.findViewById(R.id.update_bloodsugar_dimension);
        IdontKnowBs = (CheckBox)moreInfoView.findViewById(R.id.txtdntknwsugar);
        //		Button skipButton = (Button) moreInfoView.findViewById(R.id.skipButton);

        systolic = (TextView)moreInfoView.findViewById(R.id.systolic);
        diastolic = (TextView)moreInfoView.findViewById(R.id.diastolic);
        IdontKnowBp = (CheckBox)moreInfoView.findViewById(R.id.txtdntknw);
        //		Button skipButton2 = (Button) moreInfoView.findViewById(R.id.skipButtonglucose);


        bloodPresure = (CheckBox) moreInfoView.findViewById(R.id.blood_pressure_checkbox);
        Hypertension = (CheckBox) moreInfoView.findViewById(R.id.hypertension_checkbox);
        Diabetes = (CheckBox) moreInfoView.findViewById(R.id.diabetes_checkbox);
        cbNone = (CheckBox) moreInfoView.findViewById(R.id.none_checkbox);

        bloodPresure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cbNone.setChecked(false);
            }
        });
        Hypertension.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cbNone.setChecked(false);
            }
        });
        Diabetes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    cbNone.setChecked(false);
            }
        });

        cbNone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    bloodPresure.setChecked(false);
                    Hypertension.setChecked(false);
                    Diabetes.setChecked(false);
                } else {
                }
            }
        });

    }

	/*
	 * validation dialog
	 * @ author phalu
	 * 14-03-2016
	 */

    protected void valadiationDialog2(String title)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AdultWizardActivity.this);

        builder.setTitle("");
        builder.setMessage(title);

        String positiveText = "Ok";
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

    public void hideKeyboard(View view){

        //		imgBack.setVisibility(View.VISIBLE);

        startAnimation(view);

        InputMethodManager imm = (InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void startAnimation(View mview){
        Animation animation = AnimationUtils.loadAnimation(AdultWizardActivity.this, R.anim.slide_out_left);
        animation.setStartOffset(0);
        mview.startAnimation(animation);
    }

    public void SendWizardData(JSONObject json) {

        if (NetworkCaller.isInternetOncheck(AdultWizardActivity.this)) {
            SendWizardAdultData(json);
        }else{
            CustomeDialog.dispDialog(AdultWizardActivity.this, "Internet not available");
        }
    }

    private void SendWizardAdultData(JSONObject json) {
        {

            String URL = TagValues.ADD_WIZARD_DETAIL_URL;
            showPDialog();
            JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, URL, json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hidePDialog();
                            String idToken = "";
                            if (strFamilyMemberKey == "") {
                                idToken = prefs.getCustomerKey();
                            } else {
                                idToken = strFamilyMemberKey;
                            }
                            if (response != null) {
                                System.out.println("update====" + response.toString());
                                wiardDataAdutResponse(response, idToken);
                            } else {

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hidePDialog();
                        }
                    }) {
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    Map<String, String> headers = response.headers;
                    Set<String> keySet = headers.keySet();
                    for (String s : keySet) {
                        System.out.println("my========"+s+"==========="+headers.get("X-CUSTOMER-KEY"));

                    }
                    String output = headers.get("ETag");
                    //etag = output.replaceAll("W/", "");
                    return super.parseNetworkResponse(response);

                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                    params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                    params.put("X-DEVICE-ID", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                    if (!strFamilyMemberKey.equalsIgnoreCase(""))
                        params.put("X-FAMILY-MEMBER-KEY", strFamilyMemberKey);

                    params.put("Content-type", "application/json");
                    params.put("Accept", "application/json");

                    return params;
                }
            };
            VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

        }
    }

    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }
    private void showPDialog() {
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

    private void wiardDataAdutResponse(JSONObject response, String idToken) {

        System.out.println("Question==============="+questionNumber);

        if(questionNumber==2 || questionNumber == 3){

            questionNumber = 4;
            TotalCount = TotalCount+1;
            prefs.setWizardCompleteFor(idToken,false);

            hideKeyboard(lifeStyleViewWizard);
            mLayoutMainQuestions.addView(lifeStyleViewWizard,params);

        }else if(questionNumber==4){

            questionNumber = 5;
            TotalCount = TotalCount+1;
            prefs.setWizardCompleteFor(idToken,false);

            hideKeyboard(familyViewWizard);
            mLayoutMainQuestions.addView(familyViewWizard,params);


        }
        else if(questionNumber==5){

            nextButton.setText("Done");

            questionNumber = 6;
            TotalCount = TotalCount+1;
            prefs.setWizardCompleteFor(idToken,false);
            hideKeyboard(moreInfoView);
            mLayoutMainQuestions.addView(moreInfoView,params);

        }
        else if(questionNumber==6){
            ProfileData mProfileData = new Gson().fromJson(response.toString(), ProfileData.class);

            if(mProfileData != null){

                Gson mGson=new Gson();
                prefs.setIsLogin(true);
                customerManager = CustomerManager.getInstance(getApplicationContext());
                mProfileManager=ProfileManager.getInstance(getApplicationContext());
                ProfileData currentProfileData = mProfileManager.getProfileData();
                Customer updatedCustomer = mProfileData.getCustomer();
                System.out.println("AdultWizardActivity.wiardDataAdutResponse==========+"+mProfileManager.getLoggedinCustomer()+"=="+
                        prefs.getIsFIrstWizard());
                if(mProfileManager.getLoggedinCustomer() == null || prefs.getIsFIrstWizard()==false){
                    mProfileManager.setProfileData(mProfileData);
                    System.out.println("condition====="+"nulll");
                    System.out.println("profil=========="+mProfileData);
                    mProfileManager.setProfileData(mProfileData);
                    customerManager.setCurrentCustomer(mProfileData.getCustomer());
                    customerManager.isWizardShowing = false;
                    customerManager.setLoggedInCustomer(true);
                    prefs.setIsFIrstWizard(true);
                }else
                {
                     if(currentProfileData.getFamily_members() != null)
                     {

                            System.out.println("condition====="+"not ulll");
                            List<Customer> m = currentProfileData.getFamily_members().getMember_list();
                            for (int i = 0; i < m.size(); i++) {
                                if(updatedCustomer.getIdentification_token().equalsIgnoreCase(m.get(i).getIdentification_token())){
                                    m.set(i, updatedCustomer);
                                    ProfileManager profileManager = ProfileManager.getInstance(getApplicationContext());
                                    profileManager.setProfileData(currentProfileData);
                                    customerManager.setCurrentFamilyMember(mProfileData.getCustomer());
                                    //prefs.setProfileData(new Gson().toJson(currentProfileData));
                                    break;
                                }
                            }
                     }

                }


            }

            System.out.println("idToken====="+strFamilyMemberKey);
            //mMainActivity.VaccinationClickListener(true);
            prefs.setWizardCompleteFor(idToken,true);
            Intent intent = new Intent(AdultWizardActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("AdultWidgeard", "Adultok");
            intent.putExtra("idToken",strFamilyMemberKey);
            startActivity(intent);
            finish();




        }
    }

}

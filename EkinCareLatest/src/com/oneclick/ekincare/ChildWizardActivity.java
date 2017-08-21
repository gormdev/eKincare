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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.helper.CustomeDialog;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.ProfileData;

import org.apache.http.Header;
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

public class ChildWizardActivity extends AppCompatActivity implements DialogWeightInterface,DialogHeightInterface {

    private RelativeLayout mLayoutMainQuestions;
    private PreferenceHelper prefs;
    TextView txtbloodgroupA;
    TextView txtbloodgroupB;
    TextView txtbloodgroupAB;
    TextView txtbloodgroupO;

    TextView txtbloodgroupA_;
    TextView txtbloodgroupB_;
    TextView txtbloodgroupAB_;
    TextView txtbloodgroupO_;
    String WeghtText="";
    String strInches="";
    String strFfeetvalue="";
    int bloodGroupId=0;
    int questionNumber=0;
    private String strFamilyMemberKey="";
    RelativeLayout.LayoutParams params;
    private ProfileManager mProfileManager;

    TextView nextButton;
    Dialog mAlert_Dialog;
    CircleProgressBar progressWithArrow;
    MixpanelAPI mixpanel;
    CustomerManager customerManager;
    Toolbar toolbar;
    private LinearLayout heightWwightLayoutView,waistLayoutView;
    TextView wizardWeightValue,wizardHeightfeetValue,wizardHeightInchValue;
    TextView wizardWeightValueDimension,wizardHeightfeetValueDimension,wizardHeightInchValueDimension;
    private com.getbase.floatingactionbutton.FloatingActionButton nextButtonView;
    LinearLayout weightLayoutClick,heightLayoutClick;
    String strAllergies = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_questions_two);
        prefs = new PreferenceHelper(getApplicationContext());
        mixpanel = MixpanelAPI.getInstance(this, TagValues.MIXPANEL_TOKEN);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            questionNumber = bundle.getInt("question");
            strFamilyMemberKey = bundle.getString("FamilyMemberKey");
            //prefs.setCustomerKey(customerTokenUpdate);
        }
        System.out.println("customerkey========="+strFamilyMemberKey);

        nextButton=(TextView) findViewById(R.id.profile_next_two);
        nextButtonView=(com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.wizard_next_layout);
        mLayoutMainQuestions = (RelativeLayout)findViewById(R.id.ll_main_questions);

        initViews();
        setUpToolBar();

        nextButtonView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(questionNumber==1){
                    nextButton.setText("Done");
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
                        JSONObject object = new JSONObject();
                        JSONObject objectWizard = new JSONObject();
                        try {
                            object.put("weight", WeghtText);
                            object.put("feet", strFfeetvalue);
                            object.put("inches", strInches);
                            object.put("allergies", strAllergies);
                            //			object.put("profileId", BasicCustomer.getId());
                            // object.put("waist", strWiast);
                            // object.put("blood_group_id", bloodGroupId);

                            objectWizard.put("wizard", object);
                            objectWizard.put("calculate_health_score", "false");


                        }catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                        String requestString = objectWizard.toString();
                        SendWizardData(objectWizard);
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

    private void initViews() {

        toolbar=(Toolbar)findViewById(R.id.toolbar) ;
        toolbar.setNavigationIcon(R.drawable.navagation_launch);
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        //		params.leftMargin = 107;

        initHeightWeightView();
       // initWaistBloodGroupView();






        weightLayoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogWeightPicker newFragment = new DialogWeightPicker(ChildWizardActivity.this,waightValuesList());
                newFragment.show(ChildWizardActivity.this.getSupportFragmentManager(), "");
            }
        });
        heightLayoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogHeightPicker newFragment = new DialogHeightPicker(ChildWizardActivity.this,heightFeetList(),heightInchList());
                newFragment.show(ChildWizardActivity.this.getSupportFragmentManager(), "");

            }
        });




        nextButton.setText("Next");
        if(questionNumber==1 || questionNumber==2){
            questionNumber = questionNumber+0;
            mLayoutMainQuestions.addView(heightWwightLayoutView,params);
        }

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
        for (int i = 1; i < 12; i++) {
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




    private void initWaistBloodGroupView() {
        waistLayoutView = (LinearLayout)View.inflate(ChildWizardActivity.this, R.layout.wizard_waist_child_blood_group_layout, null);
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

        txtbloodgroupA.setOnClickListener(new ChildWizardActivity.BloodGroupClickListener());
        txtbloodgroupB.setOnClickListener(new ChildWizardActivity.BloodGroupClickListener());
        txtbloodgroupAB.setOnClickListener(new ChildWizardActivity.BloodGroupClickListener());
        txtbloodgroupO.setOnClickListener(new ChildWizardActivity.BloodGroupClickListener());
        txtbloodgroupA_.setOnClickListener(new ChildWizardActivity.BloodGroupClickListener());
        txtbloodgroupB_.setOnClickListener(new ChildWizardActivity.BloodGroupClickListener());
        txtbloodgroupAB_.setOnClickListener(new ChildWizardActivity.BloodGroupClickListener());
        txtbloodgroupO_.setOnClickListener(new ChildWizardActivity.BloodGroupClickListener());

    }

    private void initHeightWeightView() {
        heightWwightLayoutView = (LinearLayout)View.inflate(ChildWizardActivity.this, R.layout.height_weight_wizard_layout, null);
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

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.white));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));

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


                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.white));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));



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


                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.white));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));


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


                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.white));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));

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


                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.white));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));


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

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.white));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));


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


                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.white));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));


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

                    txtbloodgroupA.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupA_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupAB_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.card_second));
                    txtbloodgroupO_.setHintTextColor(ContextCompat.getColor(ChildWizardActivity.this, R.color.white));




                    break;
            }



            bloodGroupId = Integer.parseInt(v.getTag().toString());
            System.out.println("bloodid========"+bloodGroupId);

        }
    }



    protected void valadiationDialog2(String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ChildWizardActivity.this);

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
        dialog.setCancelable(false);
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
        Animation animation = AnimationUtils.loadAnimation(ChildWizardActivity.this, R.anim.slide_out_left);
        animation.setStartOffset(0);
        mview.startAnimation(animation);
    }

    public void SendWizardData(JSONObject json) {

        if (NetworkCaller.isInternetOncheck(ChildWizardActivity.this)) {

            wizardDataVolley(json);

        } else {
            CustomeDialog.dispDialog(ChildWizardActivity.this, "Internet not available");
        }
    }

    private void wizardDataVolley(JSONObject json) {
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
                                wiardDataResponse(response, idToken);
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
    private void wiardDataResponse(JSONObject response, String idToken) {

        ProfileData mProfileData = new Gson().fromJson(response.toString(), ProfileData.class);
        prefs.setWizardCompleteFor(idToken, true);
        Gson mGson = new Gson();
        customerManager = CustomerManager.getInstance(getApplicationContext());

        mProfileManager = ProfileManager.getInstance(ChildWizardActivity.this);
        ProfileData currentProfileData = mProfileManager.getProfileData();
        Customer updatedCustomer = mProfileData.getCustomer();
        if(currentProfileData.getFamily_members() != null){
            List<Customer> m = currentProfileData.getFamily_members().getMember_list();
            for (int i = 0; i < m.size(); i++) {
                if(updatedCustomer.getIdentification_token().equalsIgnoreCase(m.get(i).getIdentification_token())){
                    m.set(i, updatedCustomer);
                    ProfileManager profileManager = ProfileManager.getInstance(getApplicationContext());
                    profileManager.setProfileData(currentProfileData);
                    customerManager.setCurrentFamilyMember(mProfileData.getCustomer());
                    break;
                }
            }
        }

        mixpanel.timeEvent("Add Family Member_Added Family member");
        try {
            JSONObject props = new JSONObject();
            props.put("Add Family Member", "Add Family Member_Added Family member");
            mixpanel.track("Add Family Member_Added Family member", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }
        Intent intent = new Intent(ChildWizardActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("chaildWidgeard", "ok");
        intent.putExtra("idToken",strFamilyMemberKey);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        exitDialog();
    }

    private void exitDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ChildWizardActivity.this);

        builder.setTitle("Incomplete Data!");
        builder.setMessage("You have not filled data.Are you sure about leaving the process?");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ChildWizardActivity.this, MainActivity.class);
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







}

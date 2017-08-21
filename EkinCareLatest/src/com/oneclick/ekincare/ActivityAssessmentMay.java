package com.oneclick.ekincare;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.ekincare.util.DateUtility;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.AssessmentData;
import com.oneclick.ekincare.vo.Customer;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ajay on 04-08-2016.
 */
public class ActivityAssessmentMay extends AppCompatActivity
{

    TextView mTextViewDateText;
    TextView mTextViewDoctorNameText;
    TextView mTextViewProviderText;
    TextView mTextViewBadgeText;

    Toolbar toolbar;

    RelativeLayout emptyLayout;
    RelativeLayout mMainLayout;
    LinearLayout headerLayout;
    TextView mTextNoData;
    CardView mCardInformation;
    CardView mCardDoctorComment;
    CardView mCardAssesment;

    ListView mListviewTest;
    ListView mListviewDocComment;


    String mStringId;
    String mStringActivityType;
    String mStringDate;
    String mStringAssessmentType;
    String mStringDoctorName;
    String mStringProviderName;
    String mStringBadge;
    AssessmentData mAssessmentData;
    AssessmentAdapter assessmentAdapter;
    DoctorCommentAdapter mCommentAdapter;
    List<AssessmentData.AssessmentInfo.AssessmentsLabInfoData.TestComponentData> mTestList;
    List<AssessmentData.Comments> mTestCommentList;
    PreferenceHelper prefs;
    private String mStringFamilyMemberKey = "";
    Dialog mAlert_Dialog;
    DatabaseOverAllHandler dbHandler;
    String url;
    private DbResponse data;
    String jsonResponse;
    String etag;
    String responseCode;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_new_may);

        dbHandler = new DatabaseOverAllHandler(this);
        customerManager = CustomerManager.getInstance(this);
        prefs = new PreferenceHelper(this);

        mTestList = new ArrayList<AssessmentData.AssessmentInfo.AssessmentsLabInfoData.TestComponentData>();
        mTestCommentList = new ArrayList<AssessmentData.Comments>();

        mStringId = getIntent().getStringExtra("mStringId");
        prefs.setTrendsId(mStringId);

        mStringActivityType =  getIntent().getStringExtra("mStringActivityType");
        mStringFamilyMemberKey =  getIntent().getStringExtra("mStringFamilyMemberKey");
        mStringDate =  getIntent().getStringExtra("mStringDate");
        mStringAssessmentType =  getIntent().getStringExtra("mStringAssessmentType");
        mStringDoctorName =  getIntent().getStringExtra("mStringDoctorName");
        mStringProviderName =  getIntent().getStringExtra("mStringProviderName");
        mStringBadge =  getIntent().getStringExtra("mStringBadge");

        initilizeView();

        setUpToolBar();

        setAppBarData();

        //floatingActinButtonShareFunctionality();


        getAssessementDataInfo();

    }



    private void initilizeView() {
        //shareProfileButton=(FloatingActionButton)findViewById(R.id.profile_share_button);
        //shareProfileButton.hide();
        mTextViewDateText = (TextView)findViewById(R.id.fragment_assessment_new_date_text);
        mTextViewDoctorNameText = (TextView) findViewById(R.id.fragment_assessment_new_doctor_name);
        mTextViewProviderText = (TextView) findViewById(R.id.fragment_assessment_new_provider_name);
        mTextViewBadgeText = (TextView) findViewById(R.id.fragment_assessment_new_assessment_id);
        mCardInformation = (CardView) findViewById(R.id.fragment_assessment_new_information_card);
        mCardDoctorComment = (CardView) findViewById(R.id.card_doctor_comment);
        mCardAssesment = (CardView) findViewById(R.id.card_assesment);
        mListviewDocComment = (ListView) findViewById(R.id.mListDoctorComments);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mListviewTest = (ListView) findViewById(R.id.mListAssessment);

        mTextNoData = (TextView) findViewById(R.id.mTextNoData);
        emptyLayout = (RelativeLayout) findViewById(R.id.layout);
        headerLayout = (LinearLayout) findViewById(R.id.fragment_assessment_new_linear_header);
        mMainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        mTextViewDateText.setText(DateUtility.getconvertdate(mStringDate));
        //mTextViewDateAssessmentTypeText.setText(mStringAssessmentType);
    }

    private void setAppBarData() {
        if (mStringDoctorName != null && !mStringDoctorName.equalsIgnoreCase("")) {
            mTextViewDoctorNameText.setText("Referred By: "+mStringDoctorName);
        } else {
            mTextViewDoctorNameText.setVisibility(View.GONE);
            // mTextViewDoctorNameText.setText("N/A");
            mTextViewDoctorNameText.setText(" ");
        }
        if (mStringProviderName != null && !mStringProviderName.equalsIgnoreCase("")) {
            mTextViewProviderText.setText(mStringProviderName);
        } else {
            // mTextViewProviderText.setText("N/A");
            mTextViewProviderText.setVisibility(View.GONE);
            mTextViewProviderText.setText(" ");
        }

        mTextViewBadgeText.setText(mStringBadge);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpToolBar(){
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (mStringAssessmentType.equalsIgnoreCase("Body Assessment")) {
            toolbar.setTitle("Body Checkup");
        } else if (mStringAssessmentType.equalsIgnoreCase("Vision Assessment")) {
            toolbar.setTitle("Vision Checkup");
        } else if (mStringAssessmentType.equalsIgnoreCase("Dental Assessment")) {
            toolbar.setTitle("Dental Checkup");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityAssessmentMay.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                ActivityAssessmentMay.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                finish();
            }
        });
    }

    private void getAssessementDataInfo() {

        data=dbHandler.getAllAssessmentData(mStringId);

        if(data != null && data.getResponse()!=null){
            System.out.println("Volley data database=========="+data.getResponse());
            showPDialog();
            showAssessmentDbData(data.getResponse());
            hidePDialog();
            assessementBackgoundgStoreData(false);

        }else{
            System.out.println("Volley data=========="+"DBelse");
            if (NetworkCaller.isInternetOncheck(ActivityAssessmentMay.this)) {
                assessementBackgoundgStoreData(true);
            }else{
                Toast.makeText(ActivityAssessmentMay.this, getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void assessementBackgoundgStoreData(final boolean falg) {
        url=TagValues.MAIN_URL + mStringActivityType + "/" + mStringId;
        System.out.println("ActivityAssessmentMay.assessementBackgoundgStoreData===="+url);
        if(falg){
            showPDialog();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                if (result != null) {
                    if(responseCode.equals("200 OK")){
                        if(falg){
                            showAssessmentData(result);
                            hidePDialog();
                        }
                        jsonResponse = result.toString();
                        Thread thread = new Thread(new AssessmentDataInsert());
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
                Toast.makeText(ActivityAssessmentMay.this,"Something went wrong.Try again later.",Toast.LENGTH_LONG).show();
                hidePDialog();
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

                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityAssessmentMay.this));
                if (!mStringFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", mStringFamilyMemberKey);
                params.put("Content-type", "application/json");

                return params;
            }
        };
        VolleyRequestSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void showAssessmentDbData(String response) {
        System.out.println("volley==="+response);
        mAssessmentData = new Gson().fromJson(response.toString(),AssessmentData.class);

        generateListViews();

    }

    private void generateListViews(){
        if (mAssessmentData != null && mAssessmentData.getAssessment_info() != null) {
            mMainLayout.setVisibility(View.VISIBLE);
            mTextNoData.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
            headerLayout.setVisibility(View.VISIBLE);

            if (mAssessmentData.getAssessment_info().getComments() != null && mAssessmentData.getAssessment_info().getComments().size() > 0) {
                mTestCommentList = mAssessmentData.getAssessment_info().getComments();
            } else {
            }

            if(mTestCommentList.size()>0){
                mCardDoctorComment.setVisibility(View.VISIBLE);
                mCommentAdapter = new DoctorCommentAdapter();
                mListviewDocComment.setAdapter(mCommentAdapter);
                setListViewHeightBasedOnChildren(mListviewDocComment);
            }else{
                mCardDoctorComment.setVisibility(View.GONE);
            }


            for (int i = 0; i < mAssessmentData.getAssessment_info().getAssessments_lab_info().size(); i++) {

                String mStringLabTestName = mAssessmentData.getAssessment_info().getAssessments_lab_info()
                        .get(i).getLab_test_name();

                for (int j = 0; j < mAssessmentData.getAssessment_info().getAssessments_lab_info().get(i)
                        .getTest_component().size(); j++) {

                    AssessmentData.AssessmentInfo.AssessmentsLabInfoData.TestComponentData mTestData = new AssessmentData.AssessmentInfo.AssessmentsLabInfoData.TestComponentData();

                    if (j == 0) {
                        mTestData.setHeader(mAssessmentData.getAssessment_info().getAssessments_lab_info()
                                .get(i).getLab_test_name());
                    } else {
                        mTestData.setHeader("");
                    }

                    mTestData.setTest_component_name(mAssessmentData.getAssessment_info().getAssessments_lab_info().get(i).getTest_component().get(j).getTest_component_name());
                    mTestData.setResult_value(mAssessmentData.getAssessment_info().getAssessments_lab_info().get(i).getTest_component().get(j).getResult_value());
                    mTestData.setUnits(mAssessmentData.getAssessment_info().getAssessments_lab_info().get(i).getTest_component().get(j).getUnits());
                    mTestData.setLab_result_id(mAssessmentData.getAssessment_info().getAssessments_lab_info().get(i).getTest_component().get(j).getLab_result_id());
                    mTestData.setTest_component_info(mAssessmentData.getAssessment_info().getAssessments_lab_info().get(i).getTest_component().get(j).getTest_component_info());
                    mTestData.setIdealRange(mAssessmentData.getAssessment_info().getAssessments_lab_info().get(i).getTest_component().get(j).getIdealRange());
                    mTestData.setColor(mAssessmentData.getAssessment_info().getAssessments_lab_info().get(i).getTest_component().get(j).getColor());

                    if (mAssessmentData.getAssessment_info().getAssessment() != null && mAssessmentData.getAssessment_info().getAssessment().getRequest_date() != null) {
                        mTestData.setRequest_date(mAssessmentData.getAssessment_info().getAssessment().getRequest_date());
                    } else {
                        mTestData.setRequest_date("");
                    }

                    mTestList.add(mTestData);

                }
            }

            if (mAssessmentData.getAssessment_info().getRecommendation() != null && mAssessmentData.getAssessment_info().getRecommendation().size() > 0) {
                mCardInformation.setVisibility(View.VISIBLE);
            } else {
                mCardInformation.setVisibility(View.GONE);

            }

            if(mTestList.size()>0){
                mCardAssesment.setVisibility(View.VISIBLE);
                assessmentAdapter = new AssessmentAdapter();
                mListviewTest.setAdapter(assessmentAdapter);
                setListViewHeightBasedOnChildren(mListviewTest);
            }else{
                mCardAssesment.setVisibility(View.GONE);
            }

        }else{
            mMainLayout.setVisibility(View.GONE);
            mTextNoData.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.VISIBLE);
            headerLayout.setVisibility(View.GONE);
        }
    }

    private void showAssessmentData(JSONObject result) {
        System.out.println("volley==="+result);
        mAssessmentData = new Gson().fromJson(result.toString(),AssessmentData.class);
        generateListViews();
    }

    private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }

    private void showPDialog() {
        mAlert_Dialog = new Dialog(ActivityAssessmentMay.this);
        mAlert_Dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mAlert_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mAlert_Dialog.setContentView(R.layout.materialprogressbar);
        mAlert_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mAlert_Dialog.setCancelable(true);
        mAlert_Dialog.setCanceledOnTouchOutside(true);
        progressWithArrow = (CircleProgressBar)mAlert_Dialog.findViewById(R.id.progressWithArrow);
        progressWithArrow.setColorSchemeResources(android.R.color.holo_blue_light);
        mAlert_Dialog.show();
    }

    public class AssessmentAdapter extends BaseAdapter {

        ViewHolder holder = null;

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mTestList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private void startCountAnimation(int value,final TextView textview) {
            ValueAnimator animator = new ValueAnimator();
            animator.setObjectValues(0, value);
            animator.setDuration(1000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    textview.setText("" + (int) animation.getAnimatedValue());
                }
            });
            animator.start();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.raw_assessment_new_test, parent, false);
                holder.mTextViewLabComponentNameText = (TextView) convertView.findViewById(R.id.fragment_assessment_new_test_component_name_text);
                holder.mTextViewResultValueText = (TextView) convertView.findViewById(R.id.fragment_assessment_new_result_value_text);
                holder.mTextViewPressureUnit = (TextView) convertView.findViewById(R.id.fragment_assessment_new_result_unit_text);
                holder.mTextViewLabNameText = (TextView) convertView.findViewById(R.id.fragment_assessment_new_lab_test_name_text);
                holder.mLinearHeaderLayout = (LinearLayout) convertView.findViewById(R.id.fragment_assessment_new_header_layout);
                holder.mTextViewIdealRangeText = (TextView) convertView.findViewById(R.id.fragment_assessment_ideal_range_value);
                holder.mLinearRowLayout = (LinearLayout) convertView.findViewById(R.id.fragment_assessment_new_linear_card);
                holder.divider = (ImageView) convertView.findViewById(R.id.divider);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if(position == mTestList.size()-1){
                holder.divider.setVisibility(View.GONE);
            }

            if (mTestList.get(position).getTest_component_name().length() > 21) {
                holder.mTextViewLabComponentNameText.setText(mTestList.get(position).getTest_component_name().subSequence(0, 21) + "...");
            } else {
                holder.mTextViewLabComponentNameText.setText(mTestList.get(position).getTest_component_name());
            }

            holder.mTextViewPressureUnit.setText(mTestList.get(position).getUnits());

            try {
                startCountAnimation(Integer.parseInt(mTestList.get(position).getResult_value()),holder.mTextViewResultValueText);
            }catch (Exception e){
                holder.mTextViewResultValueText.setText(mTestList.get(position).getResult_value());
            }

           // holder.mTextViewResultValueText.setText(mTestList.get(position).getResult());
            holder.mTextViewIdealRangeText.setText(mTestList.get(position).getIdealRange());

            if (mTestList.get(position).getHeader() != null && !mTestList.get(position).getHeader().equalsIgnoreCase("")) {
                holder.mTextViewLabNameText.setText(mTestList.get(position).getHeader());
                holder.mLinearHeaderLayout.setVisibility(View.VISIBLE);
            } else {
                holder.mLinearHeaderLayout.setVisibility(View.GONE);
            }

            if (mTestList.get(position).getColor() != null && !mTestList.get(position).getColor().equalsIgnoreCase("")) {
                if (mTestList.get(position).getColor().contains("success")) {
                    holder.mTextViewResultValueText.setTextColor(getResources().getColor(R.color.green));
                    holder.mTextViewPressureUnit.setTextColor(getResources().getColor(R.color.green));

                } else if (mTestList.get(position).getColor().contains("danger")) {
                    holder.mTextViewResultValueText.setTextColor(getResources().getColor(R.color.red));
                    holder.mTextViewPressureUnit.setTextColor(getResources().getColor(R.color.red));

                } else if(mTestList.get(position).getColor().contains("warning")) {
                    holder.mTextViewResultValueText.setTextColor(getResources().getColor(R.color.yellow));
                    holder.mTextViewPressureUnit.setTextColor(getResources().getColor(R.color.yellow));

                }else{
                    holder.mTextViewResultValueText.setTextColor(getResources().getColor(R.color.yellow));
                    holder.mTextViewPressureUnit.setTextColor(getResources().getColor(R.color.yellow));
                }
            }

            holder.mLinearRowLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityAssessmentMay.this, ActivityAssessmentGraphMay.class);
                    intent.putExtra("mStringDate", mTestList.get(position).getRequest_date());
                    intent.putExtra("mStringResultId", mTestList.get(position).getLab_result_id());
                    intent.putExtra("mStringLabTestName", mTestList.get(position).getTest_component_name());
                    intent.putExtra("mStringLabDescription", mTestList.get(position).getTest_component_info());
                    intent.putExtra("mStringUnit", mTestList.get(position).getUnits());
                    intent.putExtra("mStringResult", mTestList.get(position).getResult_value());
                    intent.putExtra("mStringFamilyMemberKey", mStringFamilyMemberKey);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        public class ViewHolder {

            TextView mTextViewLabComponentNameText;
            TextView mTextViewResultValueText;
            TextView mTextViewIdealRangeText;
            TextView mTextViewPressureUnit;
            TextView mTextViewLabNameText;

            LinearLayout mLinearHeaderLayout;
            LinearLayout mLinearRowLayout;

            ImageView divider;
        }

    }

    public class DoctorCommentAdapter extends BaseAdapter {

        ViewHolder holder = null;

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mTestCommentList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.raw_doctor_comment_item, parent,
                        false);

                holder.mTextViewLabCommentText = (TextView) convertView
                        .findViewById(R.id.row_doctor_name_comment_value_txt);
                holder.mTextViewLabDescText = (TextView) convertView.findViewById(R.id.row_doctor_name_des_value_txt);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (mTestCommentList.get(position).getDoctor_name() != null && !mTestCommentList.get(position).getDoctor_name().equalsIgnoreCase("")) {
                holder.mTextViewLabCommentText.setText(mTestCommentList.get(position).getDoctor_name());
            } else {
                holder.mTextViewLabCommentText.setText("N/A");
            }

            if (mTestCommentList.get(position).getDescription() != null && !mTestCommentList.get(position).getDescription().equalsIgnoreCase("")) {
                holder.mTextViewLabDescText.setText(mTestCommentList.get(position).getDescription());
            } else {
                holder.mTextViewLabDescText.setText("-");
            }

            return convertView;
        }

        public class ViewHolder {

            TextView mTextViewLabCommentText;
            TextView mTextViewLabDescText;

        }

    }

    private class AssessmentDataInsert implements Runnable {
        @Override
        public void run() {
            System.out.println("volley check===="+etag+"===="+etag);
            dbHandler.insertAssessmentData(jsonResponse, mStringId, etag);
            hidePDialog();
        }
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

}

package com.oneclick.ekincare;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.oneclick.ekincare.helper.NetworkCaller;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;
import com.oneclick.ekincare.vo.Customer;
import com.oneclick.ekincare.vo.GraphData;

import org.apache.http.Header;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Ajay on 04-08-2016.
 */
public class ActivityAssessmentGraphMay extends AppCompatActivity {
    TextView mTextGraphHistorylbl;
    TextView mTextViewLabName;
    TextView mTextViewLabDesc;
    TextView mTextGraphLable;
    TextView mTextGraphMeasure;
    TextView mTextGraphUnit;
    Toolbar toolbar;
    RelativeLayout mLyout;
    RelativeLayout mMainLayout;
    TextView mTextNoData;
    LineChart lineChart;
    ListView mListHistory;
    YAxis leftAxis;
    String mStringLabTestName;
    String mStringLabDescription;
    String mStringLabResultId;
    String mStringResult;
    String mStringUnit;
    PreferenceHelper prefs;
    GraphData mGraphData;
    List<GraphData.Values> values;
    String mStringFamilyMemberKey;
    Dialog mAlert_Dialog;
    DatabaseOverAllHandler dbHandler;
    String url;
    private DbResponse data;
    String jsonResponse;
    String etag;
    String responseCode;
    CircleProgressBar progressWithArrow;
    CustomerManager customerManager;
    ArrayList<Entry> yVals;
    ArrayList<Entry> yValsOnline;
    List<String> xaxiesLablesDates;
    List<String> yaxiesLableValues;
    ArrayList<Entry> valuesEntry;
    String[] mMonthsXValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assesment_graph);

        customerManager = CustomerManager.getInstance(this);
        prefs = new PreferenceHelper(this);
        values = new ArrayList<GraphData.Values>();
        mStringLabTestName = getIntent().getStringExtra("mStringLabTestName");
        mStringLabDescription = getIntent().getStringExtra("mStringLabDescription");
        mStringLabResultId = getIntent().getStringExtra("mStringResultId");
        mStringResult = getIntent().getStringExtra("mStringResult");
        mStringUnit = getIntent().getStringExtra("mStringUnit");
        mStringFamilyMemberKey = getIntent().getStringExtra("mStringFamilyMemberKey");
        System.out.println("url graph==" + TagValues.MAIN_URL + TagValues.TRENDS_URL + mStringLabResultId + " mStringUnit="+mStringUnit);

        dbHandler = new DatabaseOverAllHandler(this);
        //grapViewTrends=(CardView)findViewById(R.id.grap_view_trends);

        initializeViews();


        setUpToolBar();

        //mMainActivity.mTextViewTitle.setText("Trends");
        mTextViewLabName.setText("What is " + mStringLabTestName + " ?");
        getAssessmentsTrendsData();

        try {

            mTextViewLabDesc.setText(mStringLabDescription);
            mTextGraphUnit.setText(mStringUnit);
            mTextGraphMeasure.setText(mStringResult);
            mTextGraphHistorylbl.setText(mStringLabTestName + " - History");
            mTextGraphLable.setText("" + mStringLabTestName);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }

    private void initializeViews() {
        mTextViewLabName = (TextView) findViewById(R.id.labtestname);
        mTextViewLabDesc = (TextView) findViewById(R.id.desc);
        mTextGraphLable = (TextView) findViewById(R.id.GraphLabelText);
        mTextGraphMeasure = (TextView) findViewById(R.id.GraphData);
        mTextGraphUnit = (TextView) findViewById(R.id.GraphUnit);

        mTextGraphHistorylbl = (TextView) findViewById(R.id.historyText);

        mListHistory = (ListView) findViewById(R.id.mListHistory);

        mTextNoData = (TextView) findViewById(R.id.mTextNoData);
        mLyout = (RelativeLayout) findViewById(R.id.layout);
        mMainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        lineChart = (LineChart) findViewById(R.id.lineChart);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }


    private void setUpToolBar() {
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(getString(R.string.trends));

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityAssessmentGraphMay.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                ActivityAssessmentGraphMay.this.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void getAssessmentsTrendsData() {
        getRecordsInBackground();

    }

    private void getRecordsInBackground() {

        data = dbHandler.getAllTrendsData(mStringLabResultId);
        if (data != null && data.getResponse() != null) {
            showPDialog();
            showGraphDbData(data.getResponse());
            hidePDialog();
            backgoundGraphStoreData();

        } else {
            System.out.println("Volley data==========" + "else");
            if (NetworkCaller.isInternetOncheck(ActivityAssessmentGraphMay.this)) {
                System.out.println("url graph==" + TagValues.MAIN_URL + TagValues.TRENDS_URL + mStringLabResultId);
                url = TagValues.MAIN_URL + TagValues.TRENDS_URL + mStringLabResultId;
                showPDialog();
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        // TODO Auto-generated method stub

                        if (result != null) {
                            showGraphData(result);
                            hidePDialog();
                            jsonResponse = result.toString();
                            Thread thread = new Thread(new GraphDataSaveDb());
                            thread.start();
                        } else {

                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError arg0) {
                        // TODO Auto-generated method stub
                        hidePDialog();
                        System.out.println("volley error==" + arg0);
                    }
                }) {
                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        Map<String, String> headers = response.headers;
                        Set<String> keySet = headers.keySet();
                        String output = headers.get("ETag");
                        etag = output.replaceAll("W/", "");
                        return super.parseNetworkResponse(response);

                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                        params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                        params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityAssessmentGraphMay.this));
                        if (!mStringFamilyMemberKey.equalsIgnoreCase(""))
                            params.put("X-FAMILY-MEMBER-KEY", mStringFamilyMemberKey);
                        params.put("Content-type", "application/json");
                        params.put("Accept", "application/json");
                        return params;
                    }
                };
                VolleyRequestSingleton.getInstance(ActivityAssessmentGraphMay.this.getApplicationContext()).addToRequestQueue(request);


            } else {
                Toast.makeText(ActivityAssessmentGraphMay.this, getString(R.string.networkloss), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void backgoundGraphStoreData() {
        url = TagValues.MAIN_URL + TagValues.TRENDS_URL + mStringLabResultId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                // TODO Auto-generated method stub

                if (result != null) {
                    showGraphData(result);
                    hidePDialog();
                    if (responseCode.equals("200 OK")) {
                        jsonResponse = result.toString();
                        Thread thread = new Thread(new GraphDataSaveDb());
                        thread.start();
                    } else if (responseCode.equals("304 Not Modified")) {

                    }
                } else {

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {
                // TODO Auto-generated method stub
                hidePDialog();
                System.out.println("volley error==" + arg0);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> headers = response.headers;
                Set<String> keySet = headers.keySet();
                responseCode = headers.get("Status");
                String output = headers.get("ETag");
                etag = output.replaceAll("W/", "");
                return super.parseNetworkResponse(response);

            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-CUSTOMER-KEY", prefs.getCustomerKey());
                params.put("X-EKINCARE-KEY", prefs.getEkinKey());
                params.put("X-DEVICE-ID", customerManager.getDeviceID(ActivityAssessmentGraphMay.this));
                if (!mStringFamilyMemberKey.equalsIgnoreCase(""))
                    params.put("X-FAMILY-MEMBER-KEY", mStringFamilyMemberKey);
                params.put("Content-type", "application/json");
                params.put("Accept", "application/json");
                return params;
            }
        };
        VolleyRequestSingleton.getInstance(ActivityAssessmentGraphMay.this).addToRequestQueue(request);

    }

    private void showGraphDbData(String response) {

        mGraphData = new Gson().fromJson(response.toString(), GraphData.class);
        if (mGraphData != null && mGraphData.getValues().size() > 0) {
            values.clear();
            values = mGraphData.getValues();
            Collections.reverse(values);
            try {
                mMainLayout.setVisibility(View.VISIBLE);
                mTextNoData.setVisibility(View.GONE);
                mLyout.setVisibility(View.GONE);

                final String[] mMonths = new String[mGraphData.getValues().size()];
                for (int j = 0; j < mGraphData.getValues().size(); j++) {
                    //mMonths[j] = mGraphData.getValues().get(j).getDate();
                    mMonths[j] = DateUtility.getconvertdate(mGraphData.getValues().get(j).getDate()).substring(0, 6);
                }
                ArrayList<String> xVals = new ArrayList<String>();
                for (int i = 0; i < mGraphData.getValues().size(); i++) {
                    xVals.add(mMonths[i % mGraphData.getValues().size()]);
                }

                yVals = new ArrayList<Entry>();
                ArrayList<Float> yValues = new ArrayList<Float>();
                int i = 0;
                for (GraphData.Values graphModel : mGraphData.getValues()) {
                    yVals.add(new Entry(i, Float.parseFloat(graphModel.getResult())));
                    yValues.add(Float.parseFloat(graphModel.getResult()));
                    i = i + 1;
                }
                Float x = Collections.max(yValues);

                lineChart.setTouchEnabled(false);
                lineChart.setDragDecelerationFrictionCoef(0.9f);
                lineChart.setDragEnabled(false);
                lineChart.setScaleEnabled(true);
                lineChart.getDescription().setEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.setPinchZoom(false);
                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.getLegend().setEnabled(false);
                lineChart.setBackgroundColor(Color.WHITE);
                setData();
                lineChart.animateX(2500);
                Legend l = lineChart.getLegend();
                l.setForm(Legend.LegendForm.LINE);
                l.setTextSize(11f);
                l.setTextColor(Color.BLACK);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setTextSize(11f);
                xAxis.setTextColor(getResources().getColor(R.color.card_second));
                xAxis.setDrawGridLines(false);
                xAxis.setLabelRotationAngle(45);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelCount(mMonths.length, true);
                xAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));

                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    public int getDecimalDigits() {
                        return 0;
                    }

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return mMonths[(int) value % mMonths.length];
                    }
                });

                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.setTextColor(getResources().getColor(R.color.card_second));
                leftAxis.setDrawGridLines(true);
                leftAxis.setGranularityEnabled(true);
                leftAxis.setDrawZeroLine(true);
                leftAxis.setLabelCount(5, true);
                leftAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));


                lineChart.getAxisRight().setEnabled(false);
                lineChart.animateXY(2000, 2000);
                lineChart.getAxisLeft().setDrawGridLines(true);
                lineChart.getXAxis().setDrawGridLines(true);
                lineChart.setAutoScaleMinMaxEnabled(true);



            } catch (NumberFormatException e) {
                e.printStackTrace();
                mTextNoData.setVisibility(View.GONE);
                mLyout.setVisibility(View.GONE);
            }
            HistoryListAdapter adapter = new HistoryListAdapter(values);
            mListHistory.setAdapter(adapter);
            setListViewHeightBasedOnChildren(mListHistory);

        } else {
            mTextNoData.setVisibility(View.GONE);
            mLyout.setVisibility(View.GONE);
        }


    }

    private void setData() {

        LineDataSet set1;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(yVals, "DataSet 1");
            set1.setDrawValues(false);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.colorPrimary));
            set1.setLineWidth(3f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(getResources().getColor(R.color.colorPrimary));
            set1.setDrawCircleHole(true);
            set1.setCircleColor(getResources().getColor(R.color.colorPrimary));

            LineData data = new LineData(set1);
            data.setDrawValues(false);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(11f);
            lineChart.setData(data);
        }
    }

    private void showGraphData(JSONObject result) {
        mGraphData = new Gson().fromJson(result.toString(), GraphData.class);
        if (mGraphData != null && mGraphData.getValues().size() > 0) {
            values.clear();
            values = mGraphData.getValues();
            Collections.reverse(values);
            try {
                mMainLayout.setVisibility(View.VISIBLE);
                mTextNoData.setVisibility(View.GONE);
                mLyout.setVisibility(View.GONE);
                final String[] mMonths = new String[mGraphData.getValues().size()];
                for (int j = 0; j < mGraphData.getValues().size(); j++) {
                    mMonths[j] = DateUtility.getconvertdate(mGraphData.getValues().get(j).getDate()).substring(0, 6);
                }
                ArrayList<String> xVals = new ArrayList<String>();
                for (int i = 0; i < mGraphData.getValues().size(); i++) {
                    xVals.add(mMonths[i % mGraphData.getValues().size()]);
                }
                yValsOnline = new ArrayList<Entry>();
                ArrayList<Float> yValues = new ArrayList<Float>();
                int i = 0;
                for (GraphData.Values graphModel : mGraphData.getValues()) {
                    yValsOnline.add(new Entry(Float.parseFloat(graphModel.getResult()), i));
                    yValues.add(Float.parseFloat(graphModel.getResult()));
                    i = i + 1;
                }
                Float x = Collections.max(yValues);
                xaxiesLablesDates = new ArrayList<String>();
                yaxiesLableValues = new ArrayList<String>();
                xaxiesLablesDates.clear();
                yaxiesLableValues.clear();
                if (mGraphData.getValues().size() > 0) {
                    for (int j = 0; j< mGraphData.getValues().size(); j++) {

                        xaxiesLablesDates.add(DateUtility.getconvertdate(mGraphData.getValues().get(j).getDate()).substring(0, 6));
                    }
                }
                if (mGraphData.getValues().size() > 0) {
                    for (int k = 0; k < mGraphData.getValues().size(); k++) {
                        yaxiesLableValues.add(mGraphData.getValues().get(k).getResult());
                    }
                }

                mMonthsXValues = new String[xaxiesLablesDates.size()];
                mMonthsXValues = xaxiesLablesDates.toArray(mMonthsXValues);
                System.out.println("list============="+ Arrays.toString(mMonthsXValues));
                lineChart.setTouchEnabled(false);
                lineChart.setDragDecelerationFrictionCoef(0.9f);
                lineChart.setDragEnabled(false);
                lineChart.setScaleEnabled(true);
                lineChart.getDescription().setEnabled(false);
                lineChart.setDrawGridBackground(false);
                lineChart.setPinchZoom(false);
                lineChart.setDoubleTapToZoomEnabled(false);
                lineChart.getLegend().setEnabled(false);
                lineChart.setBackgroundColor(Color.WHITE);
                setDataOnline();
                lineChart.animateX(2500);
                Legend l = lineChart.getLegend();
                l.setForm(Legend.LegendForm.LINE);
                l.setTextSize(11f);
                l.setTextColor(Color.BLACK);
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                l.setDrawInside(false);
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setTextSize(11f);
                xAxis.setTextColor(getResources().getColor(R.color.card_second));
                xAxis.setDrawGridLines(false);
                xAxis.setLabelRotationAngle(45);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelCount(mMonths.length, true);
                xAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));

                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    public int getDecimalDigits() {
                        return 0;
                    }

                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return mMonthsXValues[(int) value % mMonthsXValues.length];
                    }
                });

                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.setTextColor(getResources().getColor(R.color.card_second));
                leftAxis.setDrawGridLines(true);
                leftAxis.setGranularityEnabled(true);
                leftAxis.setDrawZeroLine(true);
                leftAxis.setLabelCount(5, true);
                leftAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));


                lineChart.getAxisRight().setEnabled(false);
                lineChart.animateXY(2000, 2000);
                lineChart.getAxisLeft().setDrawGridLines(true);
                lineChart.getXAxis().setDrawGridLines(true);
                lineChart.setAutoScaleMinMaxEnabled(true);


            } catch (NumberFormatException e) {
                e.printStackTrace();
                mTextNoData.setVisibility(View.GONE);
                mLyout.setVisibility(View.GONE);
            }
            HistoryListAdapter adapter = new HistoryListAdapter(values);
            mListHistory.setAdapter(adapter);
            setListViewHeightBasedOnChildren(mListHistory);

        } else {
            mTextNoData.setVisibility(View.GONE);
            mLyout.setVisibility(View.GONE);
        }

    }

    private void setDataOnline() {
        valuesEntry = new ArrayList<Entry>();
        System.out.println("list============="+Arrays.toString(yaxiesLableValues.toArray()));
        for (int i = 0; i < yaxiesLableValues.size(); i++) {
            try{
                valuesEntry.add(new Entry(i, Integer.parseInt(yaxiesLableValues.get(i))));
            }catch(NumberFormatException e){
                valuesEntry.add(new Entry(i, Float.parseFloat(yaxiesLableValues.get(i))));
            }
        }

        LineDataSet set1;
        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(valuesEntry);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(valuesEntry, "Male");
            set1.setDrawValues(false);
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.colorPrimary));
            set1.setLineWidth(3f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(getResources().getColor(R.color.colorPrimary));
            set1.setDrawCircleHole(true);
            set1.setCircleColor(getResources().getColor(R.color.colorPrimary));
            LineData data = new LineData(set1);
            data.setDrawValues(false);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(11f);
            // set data
            lineChart.setData(data);
        }
    }



             private void hidePDialog() {
        if (mAlert_Dialog != null) {
            mAlert_Dialog.dismiss();
            mAlert_Dialog=null;
        }
    }
    private void showPDialog() {
        mAlert_Dialog = new Dialog(ActivityAssessmentGraphMay.this);
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


    public class HistoryListAdapter extends BaseAdapter {

        ViewHolder holder = null;
        List<GraphData.Values> values;

        public HistoryListAdapter(List<GraphData.Values> values) {
            this.values=values;
        }

        public class ViewHolder {
            TextView mTextHistoryDate;
            TextView mTextHistoryResult;
            TextView mTextHistoryUnit;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return values.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.row_graph_value, parent, false);
                holder.mTextHistoryDate = (TextView) convertView.findViewById(R.id.TextHistoryDate);
                holder.mTextHistoryResult = (TextView) convertView.findViewById(R.id.TextHistoryResult);
                holder.mTextHistoryUnit = (TextView) convertView.findViewById(R.id.TextHistoryUnit);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.mTextHistoryDate.setText(DateUtility.getconvertdate(values.get(position).getDate()));

            holder.mTextHistoryResult.setText("" + values.get(position).getResult());
            System.out.println("value========"+values.get(position).getResult());
            holder.mTextHistoryUnit.setText(mStringUnit);

            //setting colors in text
            if (values.get(position).getTextColor().equalsIgnoreCase("text-success")) {
                holder.mTextHistoryResult.setTextColor(getResources().getColor(R.color.green));
                holder.mTextHistoryUnit.setTextColor(getResources().getColor(R.color.green));

            } else if (values.get(position).getTextColor().equalsIgnoreCase("text-danger")) {
                holder.mTextHistoryResult.setTextColor(getResources().getColor(R.color.red));
                holder.mTextHistoryUnit.setTextColor(getResources().getColor(R.color.red));

            } else if (values.get(position).getTextColor().equalsIgnoreCase("text-warning")) {
                holder.mTextHistoryResult.setTextColor(getResources().getColor(R.color.yellow));
                holder.mTextHistoryUnit.setTextColor(getResources().getColor(R.color.yellow));

            } else {
                holder.mTextHistoryResult.setTextColor(getResources().getColor(R.color.yellow));
                holder.mTextHistoryUnit.setTextColor(getResources().getColor(R.color.yellow));

            }

            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_assesment_graph, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_info:
                showInfoDialog("What is " + mStringLabTestName + " ?",mStringLabDescription);
                return true;
        }
        return true;
    }

    private void showInfoDialog(String title,String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAssessmentGraphMay.this);

        builder.setTitle(title);
        builder.setMessage(message);

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
        dialog.setCancelable(true);
        // display dialog
        dialog.show();
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


    private class GraphDataSaveDb implements Runnable {
        @Override
        public void run() {
            dbHandler.insertGraphData(jsonResponse, mStringLabResultId, etag);
            hidePDialog();
        }
    }
}

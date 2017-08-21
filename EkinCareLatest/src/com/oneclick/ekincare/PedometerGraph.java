package com.oneclick.ekincare;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;
import com.ekincare.util.TinyDB;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.oneclick.ekincare.helper.PreferenceHelper;
import com.oneclick.ekincare.helper.TagValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


/**
 * Created by PHALU on 27-04-2016.
 */
public class PedometerGraph extends Activity {
    TinyDB tinydb;
    ArrayList<String> weeklyDatesSteps;
    ArrayList<String> weeklyDatesCalories;

    ArrayList<Integer> weeklyStepsList;
    ArrayList<Integer> weeklyCaloriesList;


    ImageView typeImagesteps, typeImageCalories;
    TextView graphTitleSteps, graphTitleCalories;
    TextView graphTitleValueSteps, graphTitleValuCalories;
    PreferenceHelper prefs;
    MixpanelAPI mixpanel;

    com.github.mikephil.charting.charts.LineChart calorieschart;
    com.github.mikephil.charting.charts.LineChart stepschart;
    com.github.mikephil.charting.charts.BarChart chart_steps_bar;




    ArrayList<Entry> stepDataSet;
    ArrayList<Entry> caloriesDataSet;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedometer_graph);
        mixpanel = MixpanelAPI.getInstance(this, TagValues.MIXPANEL_TOKEN);
        prefs = new PreferenceHelper(PedometerGraph.this);
        mixpanel.timeEvent("PedometerGraph");
        try {
            JSONObject props = new JSONObject();
            props.put("LoginName",prefs.getCustomerName());
            props.put("LoginNumber",prefs.getUserName());
            mixpanel.track("PedometerGraph", props);
        } catch (JSONException e) {
            Log.e("MYAPP", "Unable to add properties to JSONObject", e);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        tinydb = new TinyDB(getApplicationContext());
        weeklyStepsList = tinydb.getListInt("WeekSteps");
        weeklyCaloriesList = tinydb.getListInt("CaloriesWeeklyData");
        weeklyDatesSteps = tinydb.getListString("StepsFullDateFormat");
        weeklyDatesCalories = tinydb.getListString("caloriesFullDate");


        System.out.println("My array steps======="+Arrays.toString(weeklyDatesSteps.toArray()));

        System.out.println("My array======="+Arrays.toString(weeklyDatesCalories.toArray()));

        typeImagesteps = (ImageView) findViewById(R.id.type_image);
        graphTitleSteps = (TextView) findViewById(R.id.title_hearder_graph);
        graphTitleValueSteps = (TextView) findViewById(R.id.title_value_graph);

        typeImageCalories = (ImageView) findViewById(R.id.type_image_calories);
        graphTitleCalories = (TextView) findViewById(R.id.title_hearder_graph_calories);
        graphTitleValuCalories = (TextView) findViewById(R.id.title_value_graph_calories);
        stepschart = (com.github.mikephil.charting.charts.LineChart) findViewById(R.id.chart_steps);
        calorieschart = (com.github.mikephil.charting.charts.LineChart) findViewById(R.id.chart_calories_weekly);
        chart_steps_bar = (com.github.mikephil.charting.charts.BarChart) findViewById(R.id.chart_steps_bar);

        ImageView expertopBack = (ImageView) findViewById(R.id.expertopBack);
        expertopBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                weeklyDatesSteps.clear();
                weeklyDatesCalories.clear();
                onBackPressed();
            }
        });





        graphTitleSteps.setText("STEPS");
        graphTitleValueSteps.setText(prefs.getStepsCount());

        graphTitleCalories.setText("CALORIES");
        graphTitleValuCalories.setText(prefs.getCaloriesCount());


        stepschart.setTouchEnabled(false);
        stepschart.setDragDecelerationEnabled(false);
        stepschart.setDragDecelerationFrictionCoef(0.9f);
        stepschart.setDragEnabled(false);
        stepschart.setScaleEnabled(true);
        stepschart.getDescription().setEnabled(false);
        stepschart.setDrawGridBackground(false);
        stepschart.setPinchZoom(false);
        stepschart.setDoubleTapToZoomEnabled(false);
        stepschart.setBackgroundColor(Color.WHITE);
        setData();
        stepschart.animateX(2500);
        Legend l = stepschart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        XAxis xAxis = stepschart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(getResources().getColor(R.color.card_second));
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(45);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));

        xAxis.setLabelCount( weeklyDatesSteps.toArray(new String[weeklyDatesSteps.size()]).length, true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            public int getDecimalDigits() {
                return 0;
            }

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return weeklyDatesSteps.toArray(new String[weeklyDatesSteps.size()])[(int) value % weeklyDatesSteps.toArray(new String[weeklyDatesSteps.size()]).length];
            }
        });

        YAxis leftAxis = stepschart.getAxisLeft();
        leftAxis.setTextColor(getResources().getColor(R.color.card_second));
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawZeroLine(true);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setLabelCount(5, true);
        leftAxis.setGridColor(getResources().getColor(R.color.chart_border_lines));

        stepschart.getLegend().setEnabled(false);
        stepschart.getAxisRight().setEnabled(false);
        stepschart.animateXY(2000, 2000);
        stepschart.getAxisLeft().setDrawGridLines(true);
        stepschart.getXAxis().setDrawGridLines(true);
        stepschart.setAutoScaleMinMaxEnabled(true);
        stepschart.setHighlightPerTapEnabled(false);





        calorieschart.setTouchEnabled(false);
        calorieschart.setDragDecelerationEnabled(false);
        calorieschart.setDragDecelerationFrictionCoef(0.9f);
        calorieschart.setDragEnabled(false);
        calorieschart.setScaleEnabled(true);
        calorieschart.getDescription().setEnabled(false);
        calorieschart.setDrawGridBackground(false);
        calorieschart.setPinchZoom(false);
        calorieschart.setDoubleTapToZoomEnabled(false);
        calorieschart.setBackgroundColor(Color.WHITE);
        setDataCaloriesTest();
        // setDataCaloriesTest();
        //setDataCalories();
        calorieschart.animateX(2500);
        Legend lCalories = calorieschart.getLegend();
        lCalories.setForm(Legend.LegendForm.LINE);
        lCalories.setTextSize(11f);
        lCalories.setTextColor(Color.BLACK);
        lCalories.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lCalories.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        lCalories.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        lCalories.setDrawInside(false);
        XAxis xAxisCalories = calorieschart.getXAxis();
        xAxisCalories.setTextSize(11f);
        xAxisCalories.setTextColor(getResources().getColor(R.color.card_second));
        xAxisCalories.setDrawGridLines(false);
        xAxisCalories.setLabelRotationAngle(45);
        xAxisCalories.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisCalories.setGridColor(getResources().getColor(R.color.chart_border_lines));
        xAxisCalories.setLabelCount( weeklyDatesCalories.toArray(new String[weeklyDatesCalories.size()]).length, true);
        xAxisCalories.setValueFormatter(new IAxisValueFormatter() {
            public int getDecimalDigits() {
                return 0;
            }

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return weeklyDatesCalories.toArray(new String[weeklyDatesCalories.size()])[(int) value % weeklyDatesCalories.toArray(new String[weeklyDatesCalories.size()]).length];
            }
        });

        YAxis leftAxisCalories = calorieschart.getAxisLeft();
        leftAxisCalories.setTextColor(getResources().getColor(R.color.card_second));
        leftAxisCalories.setDrawGridLines(true);
        leftAxisCalories.setGranularityEnabled(true);
        leftAxisCalories.setDrawZeroLine(true);
        leftAxisCalories.setAxisMinimum(0f);
        leftAxisCalories.setLabelCount(5, true);
        leftAxisCalories.setGridColor(getResources().getColor(R.color.chart_border_lines));

        calorieschart.getLegend().setEnabled(false);
        calorieschart.getAxisRight().setEnabled(false);
        calorieschart.animateXY(2000, 2000);
        calorieschart.getAxisLeft().setDrawGridLines(true);
        calorieschart.getXAxis().setDrawGridLines(true);
        calorieschart.setAutoScaleMinMaxEnabled(true);
        calorieschart.setHighlightPerTapEnabled(false);








        /*stepschartBar.setTouchEnabled(false);
        stepschartBar.setDragDecelerationEnabled(false);
        stepschartBar.setDragDecelerationFrictionCoef(0.9f);
        stepschartBar.setDragEnabled(false);
        stepschartBar.setScaleEnabled(true);
        stepschartBar.getDescription().setEnabled(false);
        stepschartBar.setDrawGridBackground(false);
        stepschartBar.setPinchZoom(false);
        stepschartBar.setDoubleTapToZoomEnabled(false);
        stepschartBar.setBackgroundColor(Color.WHITE);
        // setDataCaloriesTest();
        //setDataCalories();
        stepschartBar.animateX(2500);
        Legend lCalories = stepschartBar.getLegend();
        lCalories.setForm(Legend.LegendForm.LINE);
        lCalories.setTextSize(11f);
        lCalories.setTextColor(Color.BLACK);
        lCalories.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        lCalories.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        lCalories.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        lCalories.setDrawInside(false);
        XAxis xAxisCalories = stepschartBar.getXAxis();
        xAxisCalories.setTextSize(11f);
        xAxisCalories.setTextColor(getResources().getColor(R.color.card_second));
        xAxisCalories.setDrawGridLines(false);
        xAxisCalories.setLabelRotationAngle(45);
        xAxisCalories.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisCalories.setGridColor(getResources().getColor(R.color.chart_border_lines));
        xAxisCalories.setLabelCount( weeklyDatesCalories.toArray(new String[weeklyDatesCalories.size()]).length, true);
        xAxisCalories.setValueFormatter(new IAxisValueFormatter() {
            public int getDecimalDigits() {
                return 0;
            }

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return weeklyDatesCalories.toArray(new String[weeklyDatesCalories.size()])[(int) value % weeklyDatesCalories.toArray(new String[weeklyDatesCalories.size()]).length];
            }
        });

        YAxis leftAxisCalories = stepschartBar.getAxisLeft();
        leftAxisCalories.setTextColor(getResources().getColor(R.color.card_second));
        leftAxisCalories.setDrawGridLines(true);
        leftAxisCalories.setGranularityEnabled(true);
        leftAxisCalories.setDrawZeroLine(true);
        leftAxisCalories.setAxisMinimum(0f);
        leftAxisCalories.setLabelCount(5, true);
        leftAxisCalories.setGridColor(getResources().getColor(R.color.chart_border_lines));

        stepschartBar.getLegend().setEnabled(false);
        stepschartBar.getAxisRight().setEnabled(false);
        stepschartBar.animateXY(2000, 2000);
        stepschartBar.getAxisLeft().setDrawGridLines(true);
        stepschartBar.getXAxis().setDrawGridLines(true);
        stepschartBar.setAutoScaleMinMaxEnabled(true);
        stepschartBar.setHighlightPerTapEnabled(false);*/

    }


   /* @Override
    public void onBackPressed() {
        super.onBackPressed();
        weeklyDatesSteps.clear();
        weeklyDatesCalories.clear();
        onBackPressed();
    }*/

   /* public void AddValuesToBARENTRY(){

        BARENTRY.add(new BarEntry(2f, 0));
        BARENTRY.add(new BarEntry(4f, 1));
        BARENTRY.add(new BarEntry(6f, 2));
        BARENTRY.add(new BarEntry(8f, 3));
        BARENTRY.add(new BarEntry(7f, 4));
        BARENTRY.add(new BarEntry(3f, 5));

    }

    public void AddValuesToBarEntryLabels(){

        BarEntryLabels.add("January");
        BarEntryLabels.add("February");
        BarEntryLabels.add("March");
        BarEntryLabels.add("April");
        BarEntryLabels.add("May");
        BarEntryLabels.add("June");

    }*/

    private void setDataCaloriesTest() {
        caloriesDataSet = new ArrayList<Entry>();
        for (int i = 0; i < weeklyCaloriesList.size(); i++) {
            caloriesDataSet.add(new Entry(i, weeklyCaloriesList.get(i)));
        }
        LineDataSet set1Calories;
        if (calorieschart.getData() != null &&
                calorieschart.getData().getDataSetCount() > 0) {
            set1Calories = (LineDataSet) calorieschart.getData().getDataSetByIndex(0);
            set1Calories.setValues(caloriesDataSet);
            calorieschart.getData().notifyDataChanged();
            calorieschart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1Calories = new LineDataSet(caloriesDataSet, "Male");
            set1Calories.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1Calories.setColor(getResources().getColor(R.color.calories_color));
            set1Calories.setLineWidth(3f);
            set1Calories.setCircleRadius(3f);
            set1Calories.setFillAlpha(65);
            // set1.setHighlightLineWidth(3f);
            set1Calories.setFillColor(getResources().getColor(R.color.calories_color));
            set1Calories.setDrawCircleHole(true);
            set1Calories.setCircleColor(getResources().getColor(R.color.calories_color));
            LineData data = new LineData(set1Calories);
            data.setDrawValues(false);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(11f);
            // set data
            calorieschart.setData(data);
        }
    }

    private void setData() {
        stepDataSet = new ArrayList<Entry>();
        for (int i = 0; i < weeklyStepsList.size(); i++) {
            stepDataSet.add(new Entry(i,weeklyStepsList.get(i)));
        }
        LineDataSet set1;
        if (stepschart.getData() != null &&
                stepschart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) stepschart.getData().getDataSetByIndex(0);
            set1.setValues(stepDataSet);
            stepschart.getData().notifyDataChanged();
            stepschart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(stepDataSet, "Male");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(getResources().getColor(R.color.steps_color));
            set1.setLineWidth(3f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            // set1.setHighlightLineWidth(3f);
            set1.setFillColor(getResources().getColor(R.color.steps_color));
            set1.setDrawCircleHole(true);
            set1.setCircleColor(getResources().getColor(R.color.steps_color));
            LineData data = new LineData(set1);
            data.setDrawValues(false);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(11f);
            // set data
            stepschart.setData(data);

        }
    }





}
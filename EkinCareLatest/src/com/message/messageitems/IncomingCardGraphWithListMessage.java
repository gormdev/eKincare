package com.message.messageitems;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekincare.R;
import com.ekincare.util.DateUtility;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.message.custominterface.CardWithListButtonClickEvent;
import com.message.model.CardViewModel;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.model.TrendsModelData;
import com.message.viewholder.IncomingCardGraphWithListViewHolder;
import com.message.viewholder.MessageViewHolder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by RaviTejaN on 09-12-2016.
 */

public class IncomingCardGraphWithListMessage extends MessageItem {

    ArrayList<TrendsModelData> mTestList;
    private Context context;
    private Adapter adapter;
    private CardWithListButtonClickEvent cardWithListButtonClickEvent;
    private CardViewModel cardViewModel;
    String[]  mMonthsXValues;


    public IncomingCardGraphWithListMessage(Context context, ArrayList<TrendsModelData> list, CardWithListButtonClickEvent cardWithListButtonClickEvent, CardViewModel cardViewModel,String[]  mMonthsXValuesx)
    {
        super();
        this.context = context;
        this.mTestList=new ArrayList<>();
        this.mTestList.clear();
        this.mTestList = list;
        adapter = new Adapter();
        this.cardWithListButtonClickEvent = cardWithListButtonClickEvent;
        this.cardViewModel=cardViewModel;
        mMonthsXValues = new String[mMonthsXValuesx.length];
        mMonthsXValues=mMonthsXValuesx;
        System.out.println("IncomingCardWithListMessage.IncomingCardWithListMessage list"+list.size());
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder)
    {
        System.out.println("IncomingCardWithListMessage.buildMessageItem =="+ mTestList.get(0).getDate()+(messageViewHolder instanceof IncomingCardGraphWithListViewHolder));
        if (messageViewHolder != null && messageViewHolder instanceof IncomingCardGraphWithListViewHolder) {
            System.out.println("IncomingCardWithListMessage.buildMessageItem");
            IncomingCardGraphWithListViewHolder incomingcardgraphwithlistviewholder = (IncomingCardGraphWithListViewHolder) messageViewHolder;
            incomingcardgraphwithlistviewholder.textViewHeader.setText(cardViewModel.getTitle());
            incomingcardgraphwithlistviewholder.listView.setAdapter(adapter);
            incomingcardgraphwithlistviewholder.textViewButton1.setVisibility(View.GONE);
            incomingcardgraphwithlistviewholder.textViewButton2.setVisibility(View.GONE);
            setListViewHeightBasedOnChildren(incomingcardgraphwithlistviewholder.listView);
            System.out.println("length Adapter======"+mMonthsXValues.length+"==="+mTestList.size());

            if (mTestList.size() > 0) {
                Collections.reverse(mTestList);
                try{

                    List<String> xaxiesLablesDates = new ArrayList<String>();
                    List<String> yaxiesLableValues = new ArrayList<String>();
                    xaxiesLablesDates.clear();
                    yaxiesLableValues.clear();
                    if (mTestList.size() > 0) {
                        for (int j = 0; j< mTestList.size(); j++) {

                            xaxiesLablesDates.add(DateUtility.getconvertdate(mTestList.get(j).getDate()).substring(0, 6));
                        }
                    }
                    if (mTestList.size() > 0) {
                        for (int k = 0; k < mTestList.size(); k++) {
                            yaxiesLableValues.add(mTestList.get(k).getResult());
                        }
                    }


                    incomingcardgraphwithlistviewholder.lineChart.setTouchEnabled(false);
                    incomingcardgraphwithlistviewholder.lineChart.setDragDecelerationFrictionCoef(0.9f);
                    incomingcardgraphwithlistviewholder.lineChart.setDragEnabled(false);
                    incomingcardgraphwithlistviewholder.lineChart.setScaleEnabled(true);
                    incomingcardgraphwithlistviewholder.lineChart.setDrawGridBackground(false);
                    incomingcardgraphwithlistviewholder.lineChart.setPinchZoom(false);
                    incomingcardgraphwithlistviewholder.lineChart.setBackgroundColor(context.getResources().getColor(R.color.card_background_first));
                    incomingcardgraphwithlistviewholder.lineChart.getDescription().setEnabled(false);

                    System.out.println("mylist============="+Arrays.toString(yaxiesLableValues.toArray()));
                    System.out.println("mylist============="+Arrays.toString(xaxiesLablesDates.toArray()));
                    ArrayList<Entry> valuesEntry = new ArrayList<Entry>();
                    valuesEntry.clear();
                    for (int k = 0; k < yaxiesLableValues.size(); k++) {
                        try{
                         valuesEntry.add(new Entry(k, Integer.parseInt(yaxiesLableValues.get(k))));

                        }catch(NumberFormatException e){
                            valuesEntry.add(new Entry(k, Float.parseFloat(yaxiesLableValues.get(k))));
                        }


                    }

                    LineDataSet set1;
                    if (incomingcardgraphwithlistviewholder.lineChart.getData() != null &&
                            incomingcardgraphwithlistviewholder.lineChart.getData().getDataSetCount() > 0) {
                        set1 = (LineDataSet) incomingcardgraphwithlistviewholder.lineChart.getData().getDataSetByIndex(0);

                        set1.setValues(valuesEntry);
                        incomingcardgraphwithlistviewholder.lineChart.getData().notifyDataChanged();
                        incomingcardgraphwithlistviewholder.lineChart.notifyDataSetChanged();
                    } else {
                        // create a dataset and give it a type
                        set1 = new LineDataSet(valuesEntry, "Male");
                        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
                        set1.setColor(context.getResources().getColor(R.color.colorPrimary));
                        set1.setLineWidth(3f);
                        set1.setCircleRadius(3f);
                        set1.setFillAlpha(65);
                        // set1.setHighlightLineWidth(3f);
                        set1.setFillColor(context.getResources().getColor(R.color.steps_fill_color));
                        set1.setDrawCircleHole(true);
                        set1.setDrawFilled(true);
                        set1.setCircleColor(context.getResources().getColor(R.color.colorPrimary));
                        LineData data = new LineData(set1);
                        data.setDrawValues(false);
                        data.setValueTextColor(Color.BLACK);
                        data.setValueTextSize(11f);
                        // set data
                        incomingcardgraphwithlistviewholder.lineChart.setData(data);
                    }
                    incomingcardgraphwithlistviewholder.lineChart.animateX(2500);
                    Legend l = incomingcardgraphwithlistviewholder.lineChart.getLegend();
                    incomingcardgraphwithlistviewholder.lineChart.getLegend().setEnabled(false);
                    l.setForm(Legend.LegendForm.LINE);
                    l.setTextSize(11f);
                    l.setTextColor(context.getResources().getColor(R.color.white));
                    l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                    l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
                    l.setDrawInside(false);
                    XAxis xAxis = incomingcardgraphwithlistviewholder.lineChart.getXAxis();
                    xAxis.setEnabled(false);
                    xAxis.setTextSize(11f);
                    xAxis.setTextColor(context.getResources().getColor(R.color.card_second));
                    xAxis.setDrawGridLines(false);
                    xAxis.setLabelRotationAngle(45);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setLabelCount(mMonthsXValues.length, true);
                    xAxis.setGridColor(context.getResources().getColor(R.color.chart_border_lines));
                    Collections.reverse(Arrays.asList(mMonthsXValues));
                    /*try{

                        xAxis.setValueFormatter(new IAxisValueFormatter() {
                            public int getDecimalDigits() {
                                return 0;
                            }
                            @Override
                            public String getFormattedValue(float value, AxisBase axis) {
                                return mMonthsXValues[(int)value % mMonthsXValues.length];
                            }
                        });


                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }*/
                   /* IAxisValueFormatter formatter = new IAxisValueFormatter() {

                        @Override
                        public String getFormattedValue(float value, AxisBase axis) {
                            System.out.println("IncomingCardGraphWithListMessage.getFormattedValue======"+value);
                            if((int) value==-1){
                                return mMonthsXValues[1];
                            }else {
                                return mMonthsXValues[(int) value];
                            }
                        }


                    };
                    xAxis.setValueFormatter(formatter);*/

                    YAxis leftAxis = incomingcardgraphwithlistviewholder.lineChart.getAxisLeft();
                    leftAxis.setTextColor(R.color.card_second);
                    leftAxis.setDrawGridLines(true);
                    leftAxis.setGranularityEnabled(true);
                    leftAxis.setDrawZeroLine(true);
                    leftAxis.setLabelCount(4, true);
                    leftAxis.setGridColor(context.getResources().getColor(R.color.chart_border_lines));


                    incomingcardgraphwithlistviewholder.lineChart.getAxisRight().setEnabled(false);
                    incomingcardgraphwithlistviewholder.lineChart.animateXY(2000, 2000);
                    incomingcardgraphwithlistviewholder.lineChart.getAxisLeft().setDrawGridLines(true);
                    incomingcardgraphwithlistviewholder.lineChart.getXAxis().setDrawGridLines(true);
                    incomingcardgraphwithlistviewholder.lineChart.setAutoScaleMinMaxEnabled(true);
                   // incomingcardgraphwithlistviewholder.lineChart.setVisibleXRange(0, mTestList.size() - 1);


                }catch(NumberFormatException e){
                    e.printStackTrace();

                }


            }else{

            }

        }
    }



    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_CARD_LIST_WITH_GRAPH;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }

    public class Adapter extends BaseAdapter {

        ViewHolder holder = null;

        @Override
        public int getCount() {
            return mTestList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                holder = new ViewHolder();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                convertView = inflater.inflate( R.layout.row_graph_value, null );
                holder.mTextHistoryDate = (TextView) convertView.findViewById(R.id.TextHistoryDate);
                holder.mTextHistoryResult = (TextView) convertView.findViewById(R.id.TextHistoryResult);
                holder.mTextHistoryUnit = (TextView) convertView.findViewById(R.id.TextHistoryUnit);

                convertView.setTag(holder);
                System.out.println("Adapter.getView pos="+position);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            System.out.println("Adapter.getView==========="+mTestList.get(position).getDate());

            holder.mTextHistoryDate.setText(DateUtility.getconvertdate(mTestList.get(position).getDate()));

            holder.mTextHistoryResult.setText("" + mTestList.get(position).getResult());
            System.out.println("value========"+mTestList.get(position).getResult());
            holder.mTextHistoryUnit.setText("mg/dL");

            //setting colors in text
            if (mTestList.get(position).getColor().equalsIgnoreCase("text-success")) {
                holder.mTextHistoryResult.setTextColor(context.getResources().getColor(R.color.green));
                holder.mTextHistoryUnit.setTextColor(context.getResources().getColor(R.color.green));

            } else if (mTestList.get(position).getColor().equalsIgnoreCase("text-danger")) {
                holder.mTextHistoryResult.setTextColor(context.getResources().getColor(R.color.red));
                holder.mTextHistoryUnit.setTextColor(context.getResources().getColor(R.color.red));

            } else if (mTestList.get(position).getColor().equalsIgnoreCase("text-warning")) {
                holder.mTextHistoryResult.setTextColor(context.getResources().getColor(R.color.yellow));
                holder.mTextHistoryUnit.setTextColor(context.getResources().getColor(R.color.yellow));

            } else {
                holder.mTextHistoryResult.setTextColor(context.getResources().getColor(R.color.yellow));
                holder.mTextHistoryUnit.setTextColor(context.getResources().getColor(R.color.yellow));

            }



            return convertView;
        }

        public class ViewHolder {
            TextView mTextHistoryDate;
            TextView mTextHistoryResult;
            TextView mTextHistoryUnit;
        }

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

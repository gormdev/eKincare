package com.message.viewholder;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ekincare.R;

/**
 * Created by RaviTejaN on 09-12-2016.
 */

public class IncomingCardGraphWithListViewHolder extends MessageViewHolder {

    public ListView listView;
    public com.github.mikephil.charting.charts.LineChart lineChart;
    public TextView textViewButton1,textViewButton2,textViewHeader;
    public IncomingCardGraphWithListViewHolder(View itemView) {
        super(itemView);
        listView = (ListView) itemView.findViewById(R.id.listview_message_item_trends);
        lineChart=(com.github.mikephil.charting.charts.LineChart)itemView.findViewById(R.id.lineChart_chat);
        textViewButton1 = (TextView) itemView.findViewById(R.id.textview_button1);
        textViewButton2 = (TextView) itemView.findViewById(R.id.textview_button2);
        textViewHeader = (TextView) itemView.findViewById(R.id.textview_heading);

    }
}

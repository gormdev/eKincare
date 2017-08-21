package com.message.viewholder;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ekincare.R;

public class IncomingCardWithListViewHolder extends MessageViewHolder {

    public ListView listView;
    public TextView textViewButton1,textViewButton2,textViewHeader,textViewSelected,textViewCount;
    public  View divider,dividerBottom;
    public IncomingCardWithListViewHolder(View itemView) {
        super(itemView);
        divider=(View)itemView.findViewById(R.id.divider_for_med);
        dividerBottom=(View)itemView.findViewById(R.id.divider_for_bottom);
        listView = (ListView) itemView.findViewById(R.id.listview_message_item);
        textViewButton1 = (TextView) itemView.findViewById(R.id.textview_button1);
        textViewButton2 = (TextView) itemView.findViewById(R.id.textview_button2);
        textViewHeader = (TextView) itemView.findViewById(R.id.textview_heading);
        textViewSelected = (TextView) itemView.findViewById(R.id.textview_selected);
        textViewCount = (TextView) itemView.findViewById(R.id.textview_count);
    }
}

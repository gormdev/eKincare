package com.message.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ekincare.R;

public class IncomingDatePickerViewHolder extends MessageViewHolder {

    public FrameLayout frameLayoutAvatar;
    public CardView cardView;
    public TextView textViewTitle,textViewButton1,textViewButton2;
    public DatePicker datePicker;

    public IncomingDatePickerViewHolder(View itemView) {
        super(itemView);
        frameLayoutAvatar = (FrameLayout) itemView.findViewById(R.id.view_group_avatar);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        textViewTitle = (TextView) itemView.findViewById(R.id.textview_title);
        textViewButton1 = (TextView) itemView.findViewById(R.id.button1);
        textViewButton2 = (TextView) itemView.findViewById(R.id.button2);
        datePicker = (DatePicker) itemView.findViewById(R.id.datepicker);
    }
}

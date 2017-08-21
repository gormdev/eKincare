package com.message.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ekincare.R;

public class IncomingCardWithTextViewHolder extends MessageViewHolder {

    public FrameLayout frameLayoutAvatar;
    public LinearLayout cardView;
    public TextView textViewTitle,textViewDiscription,textViewButton1,textViewButton2,textViewAdditional;
    public LinearLayout titleView;

    public IncomingCardWithTextViewHolder(View itemView) {
        super(itemView);
        frameLayoutAvatar = (FrameLayout) itemView.findViewById(R.id.view_group_avatar);
        cardView = (LinearLayout) itemView.findViewById(R.id.card_view);
        textViewTitle = (TextView) itemView.findViewById(R.id.textview_title);
        textViewDiscription = (TextView) itemView.findViewById(R.id.textview_discription);
        textViewButton1 = (TextView) itemView.findViewById(R.id.button1);
        textViewButton2 = (TextView) itemView.findViewById(R.id.button2);
        textViewAdditional = (TextView) itemView.findViewById(R.id.textview_additional);
        titleView=(LinearLayout)itemView.findViewById(R.id.card_text_button_titleview);
    }
}

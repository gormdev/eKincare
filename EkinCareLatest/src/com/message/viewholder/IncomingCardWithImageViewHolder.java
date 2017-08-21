package com.message.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class IncomingCardWithImageViewHolder extends MessageViewHolder {

    public FrameLayout frameLayoutAvatar;
    public CardView cardView;
    public TextView textViewTitle,textViewDiscription,textLanguage,textSpecilites,textViewButton1,textViewButton2;
    public CircleImageView imageView;

    public IncomingCardWithImageViewHolder(View itemView) {
        super(itemView);
        frameLayoutAvatar = (FrameLayout) itemView.findViewById(R.id.view_group_avatar);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        textViewTitle = (TextView) itemView.findViewById(R.id.textview_title);
        textViewDiscription = (TextView) itemView.findViewById(R.id.textview_discription);
        textViewButton1 = (TextView) itemView.findViewById(R.id.button1_fee);
        textViewButton2 = (TextView) itemView.findViewById(R.id.button2_fee);
        textLanguage=(TextView)itemView.findViewById(R.id.textview_language);
        textSpecilites=(TextView)itemView.findViewById(R.id.textview_specialities);
        imageView = (CircleImageView) itemView.findViewById(R.id.imageview_background2);
    }
}

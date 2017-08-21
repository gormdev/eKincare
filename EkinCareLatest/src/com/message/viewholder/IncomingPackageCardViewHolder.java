package com.message.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ekincare.R;

public class IncomingPackageCardViewHolder extends MessageViewHolder {

    public CardView cardView;
    public TextView textViewTitle, textViewAdditional,textViewTestCount,buttonToggle;

    public IncomingPackageCardViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        textViewTitle = (TextView) itemView.findViewById(R.id.textview_title);
        textViewTestCount = (TextView) itemView.findViewById(R.id.textview_test_number);
        textViewAdditional = (TextView) itemView.findViewById(R.id.textview_additional);
    }
}

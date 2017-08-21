package com.message.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;
import com.google.android.gms.maps.MapView;


/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class OutgoingDcCardViewHolder extends MessageViewHolder {

    public CardView cardView;
    public TextView textViewTitle, textViewAddress1,textViewAddress2,textViewAddress3,textViewPhone,textViewDistance;
    public ImageView circleImageViewTick;

    public OutgoingDcCardViewHolder(View itemView)
    {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        textViewTitle = (TextView) itemView.findViewById(R.id.textview_title);
        textViewAddress1 = (TextView) itemView.findViewById(R.id.textview_address1);
        textViewAddress2 = (TextView) itemView.findViewById(R.id.textview_address2);
        textViewAddress3 = (TextView) itemView.findViewById(R.id.textview_address3);
        textViewPhone = (TextView) itemView.findViewById(R.id.textview_phone);
        textViewDistance = (TextView) itemView.findViewById(R.id.textview_distance);
        circleImageViewTick = (ImageView) itemView.findViewById(R.id.image_view_status);
    }
}

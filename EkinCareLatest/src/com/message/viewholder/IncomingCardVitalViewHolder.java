package com.message.viewholder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;

/**
 * Created by RaviTejaN on 02-12-2016.
 */

public class IncomingCardVitalViewHolder extends MessageViewHolder {

    public FrameLayout frameLayoutAvatar;
    public CardView cardView;
    public TextView vitalTitle,vitalUpdateTime,vitalUpdateValue,vitalUpdateDimension,vitalValueInches,vitalUpdateDimensionInches;

    public IncomingCardVitalViewHolder(View itemView) {
        super(itemView);
        frameLayoutAvatar = (FrameLayout) itemView.findViewById(R.id.view_group_avatar);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        vitalTitle = (TextView) itemView.findViewById(R.id.vital_lable_title);
        vitalUpdateTime = (TextView) itemView.findViewById(R.id.vital_update_time);
        vitalUpdateValue = (TextView) itemView.findViewById(R.id.vital_value_lable);
        vitalUpdateDimension = (TextView) itemView.findViewById(R.id.vital_dimension_valu);
        vitalValueInches=(TextView)itemView.findViewById(R.id.vital_value_lable_inches);
        vitalUpdateDimensionInches=(TextView)itemView.findViewById(R.id.vital_dimension_valu_inches);

    }
}

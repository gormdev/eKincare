package com.message.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;


/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class OutgoingTextViewHolder extends MessageViewHolder {

    public TextView text;
    public FrameLayout bubble;
    public ImageView circleImageViewTick;

    public OutgoingTextViewHolder(View itemView)
    {
        super(itemView);

        System.out.println("OutgoingTextViewHolder.OutgoingTextViewHolder");
        text = (TextView) itemView.findViewById(R.id.text_view_outgoing_text);
        bubble = (FrameLayout) itemView.findViewById(R.id.view_group_outgoing_text_bubble);
        circleImageViewTick = (ImageView) itemView.findViewById(R.id.image_view_status);
    }
}

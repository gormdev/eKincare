package com.message.viewholder;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;


/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class IncomingTextViewHolder extends MessageViewHolder
{
    public ImageView avatar;
    public TextView text;
    public FrameLayout bubble, avatarContainer;

    public IncomingTextViewHolder(View itemView) {
        super(itemView);

        //System.out.println("IncomingTextViewHolder.IncomingTextViewHolder");
        text = (TextView) itemView.findViewById(R.id.text_view_incoming_text);
        bubble = (FrameLayout) itemView.findViewById(R.id.view_group_incoming_text_bubble);
        avatar = (ImageView) itemView.findViewById(R.id.image_view_incoming_text_avatar);
        avatarContainer = (FrameLayout) itemView.findViewById(R.id.view_group_avatar_incoming_text);
    }
}

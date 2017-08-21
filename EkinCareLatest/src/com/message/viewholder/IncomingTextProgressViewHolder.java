package com.message.viewholder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;


/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class IncomingTextProgressViewHolder extends MessageViewHolder
{
    public ImageView avatar;
    public TextView progress;
    public FrameLayout bubble, avatarContainer;

    public IncomingTextProgressViewHolder(View itemView) {
        super(itemView);

        System.out.println("IncomingTextViewHolder.IncomingTextViewHolder");
        bubble = (FrameLayout) itemView.findViewById(R.id.view_group_incoming_text_bubble);
        avatar = (ImageView) itemView.findViewById(R.id.image_view_incoming_text_avatar);
        avatarContainer = (FrameLayout) itemView.findViewById(R.id.view_group_avatar_incoming_text);
        progress = (TextView) itemView.findViewById(R.id.text_view_progress);
    }
}

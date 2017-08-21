package com.message.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ekincare.R;
import com.message.view.GlideRoundedImageView;

/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class IncomingImageViewHolder extends MessageViewHolder {
    public ImageView avatar;
    public ViewGroup avatarContainer;

    public TextView progress;
    public FrameLayout bubble;
    public GlideRoundedImageView glideRoundedImageView;

    public IncomingImageViewHolder(View itemView) {
        super(itemView);

        System.out.println("MessageExternalUserTextViewHolder.MessageExternalUserTextViewHolder");
        bubble = (FrameLayout) itemView.findViewById(R.id.view_group_incoming_image_bubble);
        glideRoundedImageView = (GlideRoundedImageView) itemView.findViewById(R.id.image_view_incoming_image);
        progress = (TextView) itemView.findViewById(R.id.text_view_progress);
        avatar = (ImageView) itemView.findViewById(R.id.image_view_incoming_image_avatar);
        avatarContainer = (FrameLayout) itemView.findViewById(R.id.view_group_avatar_incoming_image);
    }
}

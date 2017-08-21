package com.message.viewholder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ekincare.R;
import com.message.view.GlideRoundedImageView;


/**
 * Created by John C. Hunchar on 5/12/16.
 */
public class OutgoingImageViewHolder extends MessageViewHolder {
    public FrameLayout bubble;
    public GlideRoundedImageView glideRoundedImageView;
    public ImageView circleImageViewTick;
    public ProgressBar progressBar;

    public OutgoingImageViewHolder(View itemView) {
        super(itemView);

        System.out.println("OutgoingImageViewHolder.OutgoingImageViewHolder");
        bubble = (FrameLayout) itemView.findViewById(R.id.view_group_outgoing_image_bubble);
        glideRoundedImageView = (GlideRoundedImageView) itemView.findViewById(R.id.image_view_outgoing_image);
        circleImageViewTick = (ImageView) itemView.findViewById(R.id.image_view_status);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progress_upload);
    }
}

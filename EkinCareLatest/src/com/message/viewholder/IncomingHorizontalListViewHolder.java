package com.message.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ekincare.R;

public class IncomingHorizontalListViewHolder extends MessageViewHolder
{
    public RecyclerView recyclerView;
    public ImageView avatar;
    public FrameLayout  avatarContainer;

    public IncomingHorizontalListViewHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_message_type);
        avatar = (ImageView) itemView.findViewById(R.id.image_view_avatar);
        avatarContainer = (FrameLayout) itemView.findViewById(R.id.view_group_avatar);

    }
}

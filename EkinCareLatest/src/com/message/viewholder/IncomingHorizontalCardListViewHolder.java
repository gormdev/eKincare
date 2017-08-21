package com.message.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.ekincare.R;

public class IncomingHorizontalCardListViewHolder extends MessageViewHolder {

    public RecyclerView recyclerView;
    public FrameLayout  avatarContainer;
    public IncomingHorizontalCardListViewHolder(View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view_message_type);
        avatarContainer = (FrameLayout) itemView.findViewById(R.id.view_group_avatar);
    }
}

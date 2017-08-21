package com.message.viewholder;

import android.view.View;
import android.widget.TextView;

import com.ekincare.R;

public class MessageGeneralTextViewHolder extends MessageViewHolder {
    public TextView messageTextView;

    public MessageGeneralTextViewHolder(View itemView) {
        super(itemView);

        messageTextView = (TextView) itemView.findViewById(R.id.text_view_general_text);
    }
}

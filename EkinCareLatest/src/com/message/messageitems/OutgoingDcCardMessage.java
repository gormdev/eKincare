package com.message.messageitems;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.ekincare.R;
import com.message.model.Labs;
import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.utility.Config;
import com.message.viewholder.MessageViewHolder;
import com.message.viewholder.OutgoingDcCardViewHolder;
import com.message.viewholder.OutgoingTextViewHolder;

/**
 * Created by matthewpage on 6/21/16.
 */
public class OutgoingDcCardMessage extends MessageItem {

    private Context context;
    private int sendingStatus;
    Labs labs;

    public OutgoingDcCardMessage(Labs labs, Context context)
    {
        super();
        this.context = context;
        this.labs=labs;
    }

    public void setStatus(int status){
        this.sendingStatus = status;
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof OutgoingDcCardViewHolder)
        {
            final OutgoingDcCardViewHolder messageGeneralTextViewHolder = (OutgoingDcCardViewHolder) messageViewHolder;

            switch (sendingStatus){
                case Config.MESSAGE_SENT:
                    messageGeneralTextViewHolder.circleImageViewTick.setImageResource((R.drawable.ic_done_white_24dp));
                    break;
                case Config.MESSAGE_DEIVERED:
                    messageGeneralTextViewHolder.circleImageViewTick.setImageResource((R.drawable.ic_done_all_white_24px));
                    break;
                case Config.MESSAGE_FAILED:
                    messageGeneralTextViewHolder.circleImageViewTick.setImageResource((R.drawable.ic_cloud_off_black_24px));
                    break;
                default:
                    break;
            }

            messageGeneralTextViewHolder.textViewAddress1.setText(labs.getLocality());
            messageGeneralTextViewHolder.textViewAddress2.setText(labs.getCity());
            messageGeneralTextViewHolder.textViewAddress3.setVisibility(View.GONE);


            messageGeneralTextViewHolder.textViewTitle.setText(labs.getEnterprise_name());
            messageGeneralTextViewHolder.textViewDistance.setText(labs.getDistance() + " km");
            messageGeneralTextViewHolder.textViewPhone.setVisibility(View.GONE);
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.OUTGOING_DC_CARD;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.SELF;
    }

    public Labs getLabs() {
        return labs;
    }
}
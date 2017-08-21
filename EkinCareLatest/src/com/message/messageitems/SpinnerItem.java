package com.message.messageitems;

import android.view.View;

import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.MessageViewHolder;
import com.message.viewholder.SpinnerViewHolder;

import net.frakbot.jumpingbeans.JumpingBeans;

/**
 * Created by matthewpage on 7/5/16.
 */
public class SpinnerItem extends MessageItem
{
    private boolean flag = false;

    public SpinnerItem() {
        super();
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof SpinnerViewHolder)
        {
            final SpinnerViewHolder spinnerViewHolder = (SpinnerViewHolder) messageViewHolder;
            JumpingBeans jumpingBeans1 = JumpingBeans.with(spinnerViewHolder.textView)
                    .appendJumpingDots()
                    .setLoopDuration(3000)
                    .build();

            if(flag){
                spinnerViewHolder.textView.setVisibility(View.VISIBLE);
            }else {
                spinnerViewHolder.textView.setVisibility(View.GONE);
            }
        }
    }

    public void showProgress(boolean flag){
        this.flag = flag;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.SPINNER;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }
}

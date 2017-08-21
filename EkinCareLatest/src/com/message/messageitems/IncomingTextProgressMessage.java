package com.message.messageitems;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.IncomingTextProgressViewHolder;
import com.message.viewholder.IncomingTextViewHolder;
import com.message.viewholder.MessageViewHolder;

import net.frakbot.jumpingbeans.JumpingBeans;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IncomingTextProgressMessage extends MessageItem {

    JumpingBeans jumpingBeans;

    Context context;

    public IncomingTextProgressMessage(Context context){
        super();
        //System.out.println("IncomingTextProgressMessage.IncomingTextProgressMessage");
        this.context=context;
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof IncomingTextProgressViewHolder)
        {
            final IncomingTextProgressViewHolder incomingTextProgressViewHolder = (IncomingTextProgressViewHolder) messageViewHolder;

            //incomingTextProgressViewHolder.progress.setText("Loading");

            jumpingBeans = JumpingBeans.with(incomingTextProgressViewHolder.progress)
                    .appendJumpingDots()
                    .build();

            //System.out.println("IncomingTextProgressMessage.buildMessageItem isFirstConsecutiveMessageFromSource="+isFirstConsecutiveMessageFromSource);

            incomingTextProgressViewHolder.avatar.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);
            incomingTextProgressViewHolder.avatarContainer.setVisibility(isFirstConsecutiveMessageFromSource ? View.VISIBLE : View.INVISIBLE);

        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INCOMING_TEXT_PROGRESS;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.BOT;
    }
}
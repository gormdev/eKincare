package com.message.messageitems;

import android.content.Context;

import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.MessageGeneralTextViewHolder;
import com.message.viewholder.MessageViewHolder;

public class GeneralTextMessage extends MessageItem
{
    private final Context context;
    private String text;

    public GeneralTextMessage(String text, Context context)
    {
        super();
        this.context = context;
        this.text=text;
        System.out.println("GeneralTextMessage.GeneralTextMessage");
    }

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {
        if (messageViewHolder != null && messageViewHolder instanceof MessageGeneralTextViewHolder)
        {
            final MessageGeneralTextViewHolder messageGeneralTextViewHolder = (MessageGeneralTextViewHolder) messageViewHolder;

            messageGeneralTextViewHolder.messageTextView.setText(text);
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.GENERAL_TEXT;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }

}

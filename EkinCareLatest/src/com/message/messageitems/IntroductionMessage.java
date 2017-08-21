package com.message.messageitems;

import com.message.model.MessageItem;
import com.message.model.MessageSource;
import com.message.model.MessageType;
import com.message.viewholder.MessageViewHolder;

/**
 * Created by matthewpage on 6/21/16.
 */
public class IntroductionMessage extends MessageItem {

    @Override
    public void buildMessageItem(MessageViewHolder messageViewHolder) {

    }

    @Override
    public MessageType getMessageType() {
        return MessageType.INTRODUCTION_TEXT;
    }

    @Override
    public MessageSource getMessageSource() {
        return MessageSource.GENERAL;
    }
}
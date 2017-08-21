package com.message.model;

import com.message.viewholder.MessageViewHolder;

/**
 * Created by Ajay on 23-11-2016.
 */

public abstract class MessageItem {


    protected boolean isFirstConsecutiveMessageFromSource;
    protected boolean isLastConsecutiveMessageFromSource;

    public boolean isFirstConsecutiveMessageFromSource() {
        return isFirstConsecutiveMessageFromSource;
    }

    public void setFirstConsecutiveMessageFromSource(boolean firstConsecutiveMessageFromSource) {
        isFirstConsecutiveMessageFromSource = firstConsecutiveMessageFromSource;
    }

    public boolean isLastConsecutiveMessageFromSource() {
        return isLastConsecutiveMessageFromSource;
    }

    public void setLastConsecutiveMessageFromSource(boolean lastConsecutiveMessageFromSource) {
        isLastConsecutiveMessageFromSource = lastConsecutiveMessageFromSource;
    }

    public abstract void buildMessageItem(MessageViewHolder messageViewHolder);

    public abstract MessageType getMessageType();

    public abstract MessageSource getMessageSource();

    public int getMessageItemTypeOrdinal() {
        return getMessageType().ordinal();
    }
}

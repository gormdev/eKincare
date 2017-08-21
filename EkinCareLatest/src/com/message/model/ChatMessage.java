

/**
    Class:  ChatMessage
    Defines fields required to display chatmessage information required in adapter
    
   
 *
 */



package com.message.model;


import android.os.Parcel;
import android.os.Parcelable;

public class ChatMessage {
    private MessageStatus messageMessageStatus;
    private String time;
    private long date;
    private MessageItem messageItem;


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public MessageItem getMessageItem() {
        return messageItem;
    }

    public void setMessageItem(MessageItem messageItem) {
        this.messageItem = messageItem;
    }

    public MessageStatus getMessageMessageStatus() {
        return messageMessageStatus;
    }

    public void setMessageMessageStatus(MessageStatus messageMessageStatus) {
        this.messageMessageStatus = messageMessageStatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}

package com.message.model;

/**
 * Created by Ajay on 12-12-2016.
 */

public class ChatDatabaseModel {
    String chatType;
    String chatSource;
    String chatJson;

    public ChatDatabaseModel(String type,String source,String json){
        chatType = type;
        chatSource = source;
        chatJson = json;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public String getChatSource() {
        return chatSource;
    }

    public void setChatSource(String chatSource) {
        this.chatSource = chatSource;
    }

    public String getChatJson() {
        return chatJson;
    }

    public void setChatJson(String chatJson) {
        this.chatJson = chatJson;
    }
}

package com.message.model;

import com.google.gson.JsonElement;

/**
 * Created by Ajay on 30-11-2016.
 */

public class JsonMessageResponse {
    private String id ="";
    private String timestamp ="";
    private JsonElement contexts;
    private String intentName = "";
    private String sessionId ="";
    private JsonElement payload;
    private JsonElement parameters;


    public JsonElement getParameters() {
        return parameters;
    }

    public void setParameters(JsonElement parameters) {
        this.parameters = parameters;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIntentName() {
        return intentName;
    }

    public void setIntentName(String intentName) {
        this.intentName = intentName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public JsonElement getContexts() {
        return contexts;
    }

    public void setContexts(JsonElement contexts) {
        this.contexts = contexts;
    }

    public JsonElement getPayload() {
        return payload;
    }

    public void setPayload(JsonElement payload) {
        this.payload = payload;
    }
}

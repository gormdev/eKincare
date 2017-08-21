package com.message.model;

/**
 * Created by Ajay on 30-11-2016.
 */

public class JsonMessage {
    private String data;
    private String type;
    private String data_type;
    private String message;
    private String primary_button_name;
    private String primary_button_action;
    private String seconday_button_name;
    private String seconday_button_action;
    private String header;
    private String subheader;
    private String imageurl;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPrimary_button_name() {
        return primary_button_name;
    }

    public void setPrimary_button_name(String primary_button_name) {
        this.primary_button_name = primary_button_name;
    }

    public String getPrimary_button_action() {
        return primary_button_action;
    }

    public void setPrimary_button_action(String primary_button_action) {
        this.primary_button_action = primary_button_action;
    }

    public String getSeconday_button_name() {
        return seconday_button_name;
    }

    public void setSeconday_button_name(String seconday_button_name) {
        this.seconday_button_name = seconday_button_name;
    }

    public String getSeconday_button_action() {
        return seconday_button_action;
    }

    public void setSeconday_button_action(String seconday_button_action) {
        this.seconday_button_action = seconday_button_action;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getSubheader() {
        return subheader;
    }

    public void setSubheader(String subheader) {
        this.subheader = subheader;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}

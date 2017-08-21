package com.oneclick.ekincare.vo;

import java.util.Date;

/**
 * Created by ehc on 6/6/15.
 */
public class WaterConsumptionResponse implements IResponse {
    String message;
    int userId;
    Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "WaterConsumptionResponse{" +
                "message='" + message + '\'' +
                ", userId=" + userId +
                ", date=" + date +
                '}';
    }
}

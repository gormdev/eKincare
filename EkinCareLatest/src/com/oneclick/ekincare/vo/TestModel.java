package com.oneclick.ekincare.vo;

/**
 * Created by RaviTejaN on 27-06-2016.
 */
public class TestModel {

    private String date;
    private String times;

    public TestModel() {
        // TODO Auto-generated constructor stub
    }

    public TestModel(String date, String times) {
        this.date = date;
        this.times = times;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}

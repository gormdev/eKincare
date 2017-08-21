package com.oneclick.ekincare.vo;

/**
 * Created by Ajay on 10-08-2016.
 */
public class SyncModel {
    private String URL;
    private String JSON;
    private String METHOD;

    public String getJSON() {
        return JSON;
    }

    public void setJSON(String JSON) {
        this.JSON = JSON;
    }

    public String getMETHOD() {
        return METHOD;
    }

    public void setMETHOD(String METHOD) {
        this.METHOD = METHOD;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}

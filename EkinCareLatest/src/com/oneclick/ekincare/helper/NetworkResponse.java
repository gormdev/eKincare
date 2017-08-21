package com.oneclick.ekincare.helper;

/**
 * Created by Ajay on 20-04-2016.
 */
public class NetworkResponse {
    private int responsCode;
    private String result;
    private String etag;

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public int getResponsCode() {
        return responsCode;
    }

    public void setResponsCode(int responsCode) {
        this.responsCode = responsCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

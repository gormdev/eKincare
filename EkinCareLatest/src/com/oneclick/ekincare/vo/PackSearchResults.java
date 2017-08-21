package com.oneclick.ekincare.vo;

/**
 * Created by RaviTejaN on 17-05-2016.
 */
public class PackSearchResults {

    private int imageId;
    private String title;
    private String heart;
    private String diabetes;
    private String eyes;
    private String price;


    public PackSearchResults(int imageId, String title, String heart, String diabetes, String eyes, String price) {
        this.imageId = imageId;
        this.title = title;
        this.heart = heart;
        this.diabetes = diabetes;
        this.eyes = eyes;
        this.price = price;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }



}

package com.message.model;

/**
 * Created by Ajay on 28-11-2016.
 */

public class CardViewModel {
    private String title;
    private String subTitle;
    private String buttonText1,buttonText2;
    private String url;
    private int resource;
    private String additional;
    private String buttonAction1,buttonAction2;

    public String getButtonAction1() {
        return buttonAction1;
    }

    public void setButtonAction1(String buttonAction1) {
        this.buttonAction1 = buttonAction1;
    }

    public String getButtonAction2() {
        return buttonAction2;
    }

    public void setButtonAction2(String buttonAction2) {
        this.buttonAction2 = buttonAction2;
    }

    public CardViewModel(String title, String subTitle, String buttonText1, String buttonText2, String url, int resource, String additional, String buttonText1Action, String buttonText2Action){
        this.title=title;
        this.subTitle=subTitle;
        this.buttonText1=buttonText1;
        this.buttonText2=buttonText2;
        this.url=url;
        this.resource=resource;
        this.additional = additional;
        this.buttonAction1=buttonText1Action;
        this.buttonAction2=buttonText2Action;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getButtonText1() {
        return buttonText1;
    }

    public void setButtonText1(String buttonText1) {
        this.buttonText1 = buttonText1;
    }

    public String getButtonText2() {
        return buttonText2;
    }

    public void setButtonText2(String buttonText2) {
        this.buttonText2 = buttonText2;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }
}

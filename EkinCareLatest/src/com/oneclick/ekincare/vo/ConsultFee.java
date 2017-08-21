package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RaviTejaN on 23-12-2016.
 */

public class ConsultFee implements Parcelable {
    private String inr_val = "";
    private String alt_currency_val = "";
    private String alt_currency_symbol = "";
    private String alt_currency_label = "";

    protected ConsultFee(Parcel in) {
        inr_val = in.readString();
        alt_currency_val = in.readString();
        alt_currency_symbol = in.readString();
        alt_currency_label = in.readString();
    }

    public static final Creator<ConsultFee> CREATOR = new Creator<ConsultFee>() {
        @Override
        public ConsultFee createFromParcel(Parcel in) {
            return new ConsultFee(in);
        }

        @Override
        public ConsultFee[] newArray(int size) {
            return new ConsultFee[size];
        }
    };

    public String getInr_val() {
        return inr_val;
    }

    public void setInr_val(String inr_val) {
        this.inr_val = inr_val;
    }

    public String getAlt_currency_val() {
        return alt_currency_val;
    }

    public void setAlt_currency_val(String alt_currency_val) {
        this.alt_currency_val = alt_currency_val;
    }

    public String getAlt_currency_symbol() {
        return alt_currency_symbol;
    }

    public void setAlt_currency_symbol(String alt_currency_symbol) {
        this.alt_currency_symbol = alt_currency_symbol;
    }

    public String getAlt_currency_label() {
        return alt_currency_label;
    }

    public void setAlt_currency_label(String alt_currency_label) {
        this.alt_currency_label = alt_currency_label;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(inr_val);
        dest.writeString(alt_currency_val);
        dest.writeString(alt_currency_symbol);
        dest.writeString(alt_currency_label);
    }
}

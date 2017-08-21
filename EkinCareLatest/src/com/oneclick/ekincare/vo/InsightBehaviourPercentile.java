package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by RaviTejaN on 14-12-2016.
 */

public class InsightBehaviourPercentile implements Parcelable {

    private String customer_id;
    private String behavior;
    private String percentile;


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getPercentile() {
        return percentile;
    }

    public void setPercentile(String percentile) {
        this.percentile = percentile;
    }

    protected InsightBehaviourPercentile(Parcel in) {
        customer_id=in.readString();
        behavior=in.readString();
        percentile=in.readString();
    }

    public static final Creator<InsightBehaviourPercentile> CREATOR = new Creator<InsightBehaviourPercentile>() {
        @Override
        public InsightBehaviourPercentile createFromParcel(Parcel in) {
            return new InsightBehaviourPercentile(in);
        }

        @Override
        public InsightBehaviourPercentile[] newArray(int size) {
            return new InsightBehaviourPercentile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customer_id);
        dest.writeString(behavior);
        dest.writeString(percentile);
    }
}

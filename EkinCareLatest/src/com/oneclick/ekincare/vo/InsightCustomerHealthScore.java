package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by RaviTejaN on 14-12-2016.
 */

public class InsightCustomerHealthScore implements Parcelable {
    private String min_score;
    private String max_score;
    private String dash_line_score;
    private List<String> age_span;
    private List<String> best_score;
    private List<String> worst_score;

    public List<String> getBest_score() {
        return best_score;
    }

    public void setBest_score(List<String> best_score) {
        this.best_score = best_score;
    }

    public List<String> getWorst_score() {
        return worst_score;
    }

    public void setWorst_score(List<String> worst_score) {
        this.worst_score = worst_score;
    }

    public String getMin_score() {
        return min_score;
    }

    public void setMin_score(String min_score) {
        this.min_score = min_score;
    }

    public String getMax_score() {
        return max_score;
    }

    public void setMax_score(String max_score) {
        this.max_score = max_score;
    }

    public String getDash_line_score() {
        return dash_line_score;
    }

    public void setDash_line_score(String dash_line_score) {
        this.dash_line_score = dash_line_score;
    }

    public List<String> getAge_span() {
        return age_span;
    }

    public void setAge_span(List<String> age_span) {
        this.age_span = age_span;
    }

    protected InsightCustomerHealthScore(Parcel in) {
        min_score = in.readString();
        max_score = in.readString();
        dash_line_score = in.readString();
        age_span = in.createStringArrayList();
        best_score = in.createStringArrayList();
        worst_score = in.createStringArrayList();
    }

    public static final Creator<InsightCustomerHealthScore> CREATOR = new Creator<InsightCustomerHealthScore>() {
        @Override
        public InsightCustomerHealthScore createFromParcel(Parcel in) {
            return new InsightCustomerHealthScore(in);
        }

        @Override
        public InsightCustomerHealthScore[] newArray(int size) {
            return new InsightCustomerHealthScore[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(min_score);
        dest.writeString(max_score);
        dest.writeString(dash_line_score);
        dest.writeStringList(age_span);
        dest.writeStringList(best_score);
        dest.writeStringList(worst_score);
    }
}

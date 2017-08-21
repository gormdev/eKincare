package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaviTejaN on 12-08-2016.
 */
public class InsightModel implements Parcelable {

    private List<InsightModelDataset> analytics;


    public InsightModel() {
        // TODO Auto-generated constructor stub
        analytics = new ArrayList<InsightModelDataset>();

    }

    public List<InsightModelDataset> getAnalytics() {
        return analytics;
    }



    public class InsightModelDataset implements Parcelable {
        private String customer_id;
        private String grade;
        private String analytics_type;
        private String week;
        private String created_at;
        private String updated_at;
        private String url;
        //IMageFile file;


        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getGrade() {
            return grade;
        }
        public String getWeek() {
            return week;
        }
        public String getCreated_at() {
            return created_at;
        }
        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
        public String getUpdated_at() {
            return updated_at;
        }
        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        @Override
        public int describeContents() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            // TODO Auto-generated method stub

        }


    }


    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub

    }



}

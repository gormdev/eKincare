package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RaviTejaN on 13-06-2016.
 */
public class ProviderData implements Parcelable {

    private List<ProviderDataSet> labs;
    private String msg;


    public ProviderData() {
        // TODO Auto-generated constructor stub
        labs = new ArrayList<ProviderDataSet>();

    }

    public List<ProviderDataSet> getLabs() {
        return labs;
    }

    public void setLabs(List<ProviderDataSet> labs) {
        this.labs = labs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public class ProviderDataSet implements Parcelable {

        private String id;
        private String line1;

        private String latitude;
        private String longitude;
        private String distance;
        private String land_mark;
        ProviderAddress addressee;

        public ProviderAddress getAddressee() {
            return addressee;
        }

        public void setAddressee(ProviderAddress addressee) {
            this.addressee = addressee;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLine1() {
            return line1;
        }

        public void setLine1(String line1) {
            this.line1 = line1;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getLand_mark() {
            return land_mark;
        }

        public void setLand_mark(String land_mark) {
            this.land_mark = land_mark;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }
      /*  @Override
        public int describeContents() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            // TODO Auto-generated method stub

        }*/


    }

    public class ProviderAddress implements Parcelable {

        private String id;
        private String name;
        private String time_week;

        private String time_sunday;
        private String vendor_code;
        private String branch;
        private String land_mark;

        private  String poc;
        private String offline_number;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPoc() {
            return poc;
        }


        public String getOffline_number() {
            return offline_number;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTime_week() {
            return time_week;
        }

        public String getTime_sunday() {
            return time_sunday;
        }

        public String getBranch() {
            return branch;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }
    }

}
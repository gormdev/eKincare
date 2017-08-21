package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PHALU on 15-07-2016.
 */
public class AppointmentData implements Parcelable {

    private List<AppointmentDataSet> appointments;
    private String chat_url;


    public AppointmentData() {
        // TODO Auto-generated constructor stub
        appointments = new ArrayList<AppointmentDataSet>();

    }

    public List<AppointmentDataSet> getAppointments() {
        return appointments;
    }




    public String getChat_url() {
        return chat_url;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }



    public class AppointmentDataSet implements Parcelable {

        private String description;
        private String provider_id;
        private String package_id;
        private String unique_id;
        private String time;
        private  String diagnostic_center;
        private  String package_type;

        public String getPackage_type() {
            return package_type;
        }

        public void setPackage_type(String package_type) {
            this.package_type = package_type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getProvider_id() {
            return provider_id;
        }

        public void setProvider_id(String provider_id) {
            this.provider_id = provider_id;
        }

        public String getPackage_id() {
            return package_id;
        }

        public void setPackage_id(String package_id) {
            this.package_id = package_id;
        }

        public String getUnique_id() {
            return unique_id;
        }

        public void setUnique_id(String unique_id) {
            this.unique_id = unique_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDiagnostic_center() {
            return diagnostic_center;
        }

        public void setDiagnostic_center(String diagnostic_center) {
            this.diagnostic_center = diagnostic_center;
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

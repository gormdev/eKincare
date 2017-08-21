package com.message.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.oneclick.ekincare.vo.Immunization;

/**
 * Created by RaviTejaN on 06-12-2016.
 */

public class VaccaniationDataModel {

    String id;
    String name;
    String immunization_type;
    String minage;
    String maxage;
    String gender;
    String disease;
    String benefits;
    String dose;
    String active;
    String current_doses;
    private scheduleData schedule;
    String created_at;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public scheduleData getSchedule() {
        return schedule;
    }

    public void setSchedule(scheduleData schedule) {
        this.schedule = schedule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImmunization_type() {
        return immunization_type;
    }

    public void setImmunization_type(String immunization_type) {
        this.immunization_type = immunization_type;
    }

    public String getMinage() {
        return minage;
    }

    public void setMinage(String minage) {
        this.minage = minage;
    }

    public String getMaxage() {
        return maxage;
    }

    public void setMaxage(String maxage) {
        this.maxage = maxage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCurrent_doses() {
        return current_doses;
    }

    public void setCurrent_doses(String current_doses) {
        this.current_doses = current_doses;
    }

    public static class scheduleData implements Parcelable {

        private String one = "";
        private String two = "";
        private String three = "";
        private String four = "";
        private String five = "";
        private String six = "";
        public String getOne() {
            return one;
        }
        public String getTwo() {
            return two;
        }
        public String getThree() {
            return three;
        }
        public String getFour() {
            return four;
        }
        public String getFive() {
            return five;
        }
        public String getSix() {
            return six;
        }
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(one);
            dest.writeString(two);
            dest.writeString(three);
            dest.writeString(four);
            dest.writeString(five);
            dest.writeString(six);
        }

        public static Parcelable.Creator<Immunization.scheduleData> CREATOR = new Creator<Immunization.scheduleData>() {
            @Override
            public Immunization.scheduleData[] newArray(final int size) {
                return new Immunization.scheduleData[size];
            }

            @Override
            public Immunization.scheduleData createFromParcel(final Parcel source) {
                return new Immunization.scheduleData(source);
            }
        };

        public scheduleData(final Parcel in) {
            one = in.readString();
            two = in.readString();
            three = in.readString();
            four = in.readString();
            five = in.readString();
            six = in.readString();

        }
    }
}



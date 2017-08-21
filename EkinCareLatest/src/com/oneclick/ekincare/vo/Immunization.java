package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Immunization implements Parcelable {

    private List<ImmunizationData> message;
    private String result = "";
    private String msg = "";

    public List<ImmunizationData> getmessage() {
        return message;
    }



    public Immunization() {
        message = new ArrayList<ImmunizationData>();
        //        AutoCompleteIdName = new ArrayList<VoucherDataList>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeTypedList(message);
        arg0.writeString(result);
        arg0.writeString(msg);
        //        arg0.writeTypedList(AutoCompleteIdName);

    }

    public static Parcelable.Creator<Immunization> CREATOR = new Creator<Immunization>() {

        @Override
        public Immunization[] newArray(final int size) {
            return new Immunization[size];
        }

        @Override
        public Immunization createFromParcel(final Parcel source) {
            return new Immunization(source);
        }
    };

    public Immunization(final Parcel in) {
        in.readTypedList(message, ImmunizationData.CREATOR);
        result = in.readString();
        msg = in.readString();
    }
    public static class ImmunizationData implements Parcelable {

        private String immunization_id = "";
        private String name = "";
        private String benefits = "";
        private String doses = "";
        private scheduleData schedule;
        private String minage = "";
        private String has_taken = "";
        private String current_doses = "";
        private String customer_immunization_id = "";
        private String created_at = "";

        public String getImmunization_id() {
            return immunization_id;
        }
        public String getName() {
            return name;
        }
        public String getBenefits() {
            return benefits;
        }

        public String getDoses() {
            return doses;
        }

        public String getMinage() {
            return minage;
        }

        public String getCurrent_doses() {
            return current_doses;
        }

        public String getCreated_at() {
            return created_at;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(immunization_id);
            dest.writeString(name);
            dest.writeString(benefits);
            dest.writeString(doses);
            //			dest.writeString(schedule);
            dest.writeString(minage);
            dest.writeString(has_taken);
            dest.writeString(current_doses);
            dest.writeString(customer_immunization_id);
            dest.writeParcelable(schedule, flags);

        }

        public static Parcelable.Creator<ImmunizationData> CREATOR = new Creator<ImmunizationData>() {

            @Override
            public ImmunizationData[] newArray(final int size) {
                return new ImmunizationData[size];
            }

            @Override
            public ImmunizationData createFromParcel(final Parcel source) {
                return new ImmunizationData(source);
            }
        };

        public ImmunizationData(final Parcel in) {

            immunization_id = in.readString();
            immunization_id = in.readString();
            name = in.readString();
            benefits = in.readString();
            doses = in.readString();
            minage = in.readString();
            has_taken = in.readString();
            current_doses = in.readString();
            customer_immunization_id = in.readString();
            in.readParcelable(schedule.getClass().getClassLoader());

        }


        public scheduleData getSchedule() {
            return schedule;
        }
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

        public static Parcelable.Creator<scheduleData> CREATOR = new Creator<scheduleData>() {
            @Override
            public scheduleData[] newArray(final int size) {
                return new scheduleData[size];
            }

            @Override
            public scheduleData createFromParcel(final Parcel source) {
                return new scheduleData(source);
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
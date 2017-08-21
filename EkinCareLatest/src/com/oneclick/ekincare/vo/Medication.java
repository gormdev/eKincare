package com.oneclick.ekincare.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ajay on 22-09-2016.
 */
public class Medication implements Parcelable
{
    public String getMorning_quantity() {
        return morning_quantity;
    }

    public void setMorning_quantity(String morning_quantity) {
        this.morning_quantity = morning_quantity;
    }

    public String getAfternoon_quantity() {
        return afternoon_quantity;
    }

    public void setAfternoon_quantity(String afternoon_quantity) {
        this.afternoon_quantity = afternoon_quantity;
    }

    public String getEvening_quantity() {
        return evening_quantity;
    }

    public void setEvening_quantity(String evening_quantity) {
        this.evening_quantity = evening_quantity;
    }

    private String id,date,drug_id,provider_id,medication_type,instructions,name,morning,afternoon,evening,end_date;
    private String customer_id,created_at,updated_at,dose_quantity,active,prescriber_name,before_meal,recurring;
    private String morning_quantity,afternoon_quantity,evening_quantity;
    private String number_of_days;

    public String getNumber_of_days() {
        return number_of_days;
    }

    public void setNumber_of_days(String number_of_days) {
        this.number_of_days = number_of_days;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDrug_id() {
        return drug_id;
    }

    public void setDrug_id(String drug_id) {
        this.drug_id = drug_id;
    }

    public String getProvider_id() {
        return provider_id;
    }



    public String getMedication_type() {
        return medication_type;
    }

    public void setMedication_type(String medication_type) {
        this.medication_type = medication_type;
    }

    public String getInstructions() {
        return instructions;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getAfternoon() {
        return afternoon;
    }



    public String getEvening() {
        return evening;
    }


    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }



    public String getCreated_at() {
        return created_at;
    }



    public String getDose_quantity() {
        return dose_quantity;
    }

    public void setDose_quantity(String dose_quantity) {
        this.dose_quantity = dose_quantity;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getPrescriber_name() {
        return prescriber_name;
    }

    public String getBefore_meal() {
        return before_meal;
    }

    public void setBefore_meal(String before_meal) {
        this.before_meal = before_meal;
    }

    public String getRecurring() {
        return recurring;
    }

    public void setRecurring(String recurring) {
        this.recurring = recurring;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(customer_id);
        parcel.writeString(date);
        parcel.writeString(drug_id);
        parcel.writeString(provider_id);
        parcel.writeString(medication_type);
        parcel.writeString(instructions );
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeString(dose_quantity);
        parcel.writeString(active);
        parcel.writeString(prescriber_name);
        parcel.writeString(name );
        parcel.writeString(morning);
        parcel.writeString(afternoon);
        parcel.writeString(evening);
        parcel.writeString(before_meal );
        parcel.writeString(recurring);
        parcel.writeString(end_date);

        parcel.writeString(morning_quantity );
        parcel.writeString(afternoon_quantity);
        parcel.writeString(evening_quantity);

        parcel.writeString(number_of_days);

    }

    public static final Parcelable.Creator<Medication> CREATOR = new Creator<Medication>() {
        public Medication createFromParcel(Parcel source) {
            Medication medication = new Medication();

            medication.id = source.readString();
            medication.customer_id = source.readString();
            medication.date = source.readString();
            medication.drug_id = source.readString();
            medication.provider_id = source.readString();
            medication.medication_type = source.readString();
            medication.instructions = source.readString();
            medication.created_at = source.readString();
            medication.updated_at = source.readString();
            medication.dose_quantity = source.readString();
            medication.active = source.readString();
            medication.prescriber_name = source.readString();
            medication.name = source.readString();
            medication.morning = source.readString();
            medication.afternoon = source.readString();

            medication.evening = source.readString();
            medication.before_meal = source.readString();
            medication.recurring = source.readString();
            medication.end_date = source.readString();

            medication.morning_quantity = source.readString();
            medication.afternoon_quantity = source.readString();
            medication.evening_quantity = source.readString();
            medication.number_of_days = source.readString();

            return medication;
        }
        @Override
        public Medication[] newArray(int size)
        {
            return new Medication[size];
        }
    };
}
package com.message.model;

/**
 * Created by RaviTejaN on 08-12-2016.
 */

public class MedicationsChatModel {

    private String id,date,drug_id,provider_id,medication_type,instructions,name,morning,afternoon,evening,end_date;
    private String customer_id,created_at,updated_at,dose_quantity,active,prescriber_name,before_meal,recurring;
    private String morning_quantity,afternoon_quantity,evening_quantity;
    private String number_of_days;


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

    public void setProvider_id(String provider_id) {
        this.provider_id = provider_id;
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

    public void setInstructions(String instructions) {
        this.instructions = instructions;
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

    public void setAfternoon(String afternoon) {
        this.afternoon = afternoon;
    }

    public String getEvening() {
        return evening;
    }

    public void setEvening(String evening) {
        this.evening = evening;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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

    public void setPrescriber_name(String prescriber_name) {
        this.prescriber_name = prescriber_name;
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

    public String getNumber_of_days() {
        return number_of_days;
    }

    public void setNumber_of_days(String number_of_days) {
        this.number_of_days = number_of_days;
    }
}

package com.message.model;

import com.google.gson.JsonElement;

/**
 * Created by Ajay on 24-12-2016.
 */

public class PackageItem {
    private String id,name,package_info,gender,package_type,mrp,selling_price,home_collection_enable,empty_stomach,minage,tests_count;
    private String maxage,cut_from_pay,sponsor_id;
    private JsonElement jsonElement;
    private  JsonElement home_collection_prices;

    public String getTests_count() {
        return tests_count;
    }

    public void setTests_count(String tests_count) {
        this.tests_count = tests_count;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackage_info() {
        return package_info;
    }

    public void setPackage_info(String package_info) {
        this.package_info = package_info;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getSelling_price() {
        return selling_price;
    }

    public void setSelling_price(String selling_price) {
        this.selling_price = selling_price;
    }

    public String getHome_collection_enable() {
        return home_collection_enable;
    }

    public void setHome_collection_enable(String home_collection_enable) {
        this.home_collection_enable = home_collection_enable;
    }

    public String getEmpty_stomach() {
        return empty_stomach;
    }

    public void setEmpty_stomach(String empty_stomach) {
        this.empty_stomach = empty_stomach;
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

    public String getCut_from_pay() {
        return cut_from_pay;
    }

    public void setCut_from_pay(String cut_from_pay) {
        this.cut_from_pay = cut_from_pay;
    }

    public String getSponsor_id() {
        return sponsor_id;
    }

    public void setSponsor_id(String sponsor_id) {
        this.sponsor_id = sponsor_id;
    }

    public JsonElement getHome_collection_prices() {
        return home_collection_prices;
    }

    public void setHome_collection_prices(JsonElement home_collection_prices) {
        this.home_collection_prices = home_collection_prices;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    public void setJsonElement(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
    }
}

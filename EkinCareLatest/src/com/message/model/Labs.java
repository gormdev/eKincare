package com.message.model;

import com.google.gson.JsonElement;

/**
 * Created by Ajay on 26-12-2016.
 */

public class Labs
{
    private String enterprise_name;

    private String distance;

    private String provider_id;

    private JsonElement slots;

    private String enterprise_id;

    public JsonElement getSlots() {
        return slots;
    }

    public String getHome_collection_enabled() {
        return home_collection_enabled;
    }

    public void setHome_collection_enabled(String home_collection_enabled) {
        this.home_collection_enabled = home_collection_enabled;
    }

    private String home_collection_enabled;



    public void setSlots(JsonElement slots) {
        this.slots = slots;
    }

    private String locality;

    private String city;

    public String getEnterprise_name ()
    {
        return enterprise_name;
    }

    public void setEnterprise_name (String enterprise_name)
    {
        this.enterprise_name = enterprise_name;
    }

    public String getDistance ()
    {
        return distance;
    }

    public void setDistance (String distance)
    {
        this.distance = distance;
    }

    public String getProvider_id ()
    {
        return provider_id;
    }

    public void setProvider_id (String provider_id)
    {
        this.provider_id = provider_id;
    }

    public String getLocality ()
    {
        return locality;
    }

    public void setLocality (String locality)
    {
        this.locality = locality;
    }

    public String getCity ()
    {
        return city;
    }

    public void setCity (String city)
    {
        this.city = city;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [enterprise_name = "+enterprise_name+", distance = "+distance+", provider_id = "+provider_id+", slots = "+slots+", locality = "+locality+", city = "+city+"]";
    }

    public String getEnterprise_id() {
        return enterprise_id;
    }

    public void setEnterprise_id(String enterprise_id) {
        this.enterprise_id = enterprise_id;
    }
}

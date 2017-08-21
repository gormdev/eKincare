package com.message.model;

import com.google.gson.JsonElement;

/**
 * Created by Ajay on 31-01-2017.
 */

public class Wallet {
    private String id;

    private String balance;

    private String updated_at;

    private String created_at;

    private String customer_id;

    private JsonElement preferences;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getBalance ()
    {
        return balance;
    }

    public void setBalance (String balance)
    {
        this.balance = balance;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getCustomer_id ()
    {
        return customer_id;
    }

    public void setCustomer_id (String customer_id)
    {
        this.customer_id = customer_id;
    }

    public JsonElement getPreferences ()
    {
        return preferences;
    }

    public void setPreferences (JsonElement preferences)
    {
        this.preferences = preferences;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", balance = "+balance+", updated_at = "+updated_at+", created_at = "+created_at+", customer_id = "+customer_id+", preferences = "+preferences+"]";
    }
}

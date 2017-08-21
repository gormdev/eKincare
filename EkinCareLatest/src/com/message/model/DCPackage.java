package com.message.model;

import java.util.ArrayList;

/**
 * Created by Ajay on 26-12-2016.
 */

public class DCPackage {
    private String message;

    private ArrayList<Labs> labs;

    private String status;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public ArrayList<Labs> getLabs ()
    {
        return labs;
    }

    public void setLabs (ArrayList<Labs> labs)
    {
        this.labs = labs;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "DCPackage [message = "+message+", labs = "+labs+", status = "+status+"]";
    }
}

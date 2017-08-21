package com.message.model;

import java.util.ArrayList;

/**
 * Created by Ajay on 17-01-2017.
 */

public class BookAppointmentResponse {

    private String message;

    private ArrayList<String> instructions;

    private Appointment appointment;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public ArrayList<String>  getInstructions ()
    {
        return instructions;
    }

    public void setInstructions (ArrayList<String>  instructions)
    {
        this.instructions = instructions;
    }

    public Appointment getAppointment ()
    {
        return appointment;
    }

    public void setAppointment (Appointment appointment)
    {
        this.appointment = appointment;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", instructions = "+instructions+", appointment = "+appointment+"]";
    }

    public class Appointment
    {
        private String two_d_echo_present;

        private String ct_present;

        private String offline_number;

        private String home_collection_enable;

        private String time_week;

        private String provider_id;

        private String mammography_present;

        private String point_of_contact;

        private String unique_id;

        private String mri_present;

        private String diagnostic_center;

        private String ultra_sound_present;

        private String time;

        private String bmd_present;

        private String xray_present;

        private String[] package_type;

        private String description;

        private String package_id;

        private String time_sunday;

        private String telephone;

        public String getTwo_d_echo_present ()
        {
            return two_d_echo_present;
        }

        public void setTwo_d_echo_present (String two_d_echo_present)
        {
            this.two_d_echo_present = two_d_echo_present;
        }

        public String getCt_present ()
        {
            return ct_present;
        }

        public void setCt_present (String ct_present)
        {
            this.ct_present = ct_present;
        }

        public String getOffline_number ()
    {
        return offline_number;
    }

        public void setOffline_number (String offline_number)
        {
            this.offline_number = offline_number;
        }

        public String getHome_collection_enable ()
        {
            return home_collection_enable;
        }

        public void setHome_collection_enable (String home_collection_enable)
        {
            this.home_collection_enable = home_collection_enable;
        }

        public String getTime_week ()
    {
        return time_week;
    }

        public void setTime_week (String time_week)
        {
            this.time_week = time_week;
        }

        public String getProvider_id ()
        {
            return provider_id;
        }

        public void setProvider_id (String provider_id)
        {
            this.provider_id = provider_id;
        }

        public String getMammography_present ()
        {
            return mammography_present;
        }

        public void setMammography_present (String mammography_present)
        {
            this.mammography_present = mammography_present;
        }

        public String getPoint_of_contact ()
    {
        return point_of_contact;
    }

        public void setPoint_of_contact (String point_of_contact)
        {
            this.point_of_contact = point_of_contact;
        }

        public String getUnique_id ()
        {
            return unique_id;
        }

        public void setUnique_id (String unique_id)
        {
            this.unique_id = unique_id;
        }

        public String getMri_present ()
        {
            return mri_present;
        }

        public void setMri_present (String mri_present)
        {
            this.mri_present = mri_present;
        }

        public String getDiagnostic_center ()
        {
            return diagnostic_center;
        }

        public void setDiagnostic_center (String diagnostic_center)
        {
            this.diagnostic_center = diagnostic_center;
        }

        public String getUltra_sound_present ()
        {
            return ultra_sound_present;
        }

        public void setUltra_sound_present (String ultra_sound_present)
        {
            this.ultra_sound_present = ultra_sound_present;
        }

        public String getTime ()
        {
            return time;
        }

        public void setTime (String time)
        {
            this.time = time;
        }

        public String getBmd_present ()
        {
            return bmd_present;
        }

        public void setBmd_present (String bmd_present)
        {
            this.bmd_present = bmd_present;
        }

        public String getXray_present ()
        {
            return xray_present;
        }

        public void setXray_present (String xray_present)
        {
            this.xray_present = xray_present;
        }

        public String[] getPackage_type ()
        {
            return package_type;
        }

        public void setPackage_type (String[] package_type)
        {
            this.package_type = package_type;
        }

        public String getDescription ()
        {
            return description;
        }

        public void setDescription (String description)
        {
            this.description = description;
        }

        public String getPackage_id ()
    {
        return package_id;
    }

        public void setPackage_id (String package_id)
        {
            this.package_id = package_id;
        }

        public String getTime_sunday ()
    {
        return time_sunday;
    }

        public void setTime_sunday (String time_sunday)
        {
            this.time_sunday = time_sunday;
        }

        public String getTelephone ()
        {
            return telephone;
        }

        public void setTelephone (String telephone)
        {
            this.telephone = telephone;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [two_d_echo_present = "+two_d_echo_present+", ct_present = "+ct_present+", offline_number = "+offline_number+", home_collection_enable = "+home_collection_enable+", time_week = "+time_week+", provider_id = "+provider_id+", mammography_present = "+mammography_present+", point_of_contact = "+point_of_contact+", unique_id = "+unique_id+", mri_present = "+mri_present+", diagnostic_center = "+diagnostic_center+", ultra_sound_present = "+ultra_sound_present+", time = "+time+", bmd_present = "+bmd_present+", xray_present = "+xray_present+", package_type = "+package_type+", description = "+description+", package_id = "+package_id+", time_sunday = "+time_sunday+", telephone = "+telephone+"]";
        }
    }
}

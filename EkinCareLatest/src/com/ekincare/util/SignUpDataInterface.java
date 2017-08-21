package com.ekincare.util;

import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.oneclick.ekincare.vo.Medication;

/**
 * Created by RaviTejaN on 13-10-2016.
 */

public interface SignUpDataInterface
{
    public void callNextFragmnet();

    public void hideBackButton();

    public void showBackButton();



    public void sendOtp();

    public void   setGenderSignup(String genderSignup);

    public String  getGenderSignUp();

    public void setName(String firstName);

    public String getName();

    public void  setdateOfBirth(String dateOfBirth);

    public void  setmobileNumber(String mobileNumber);

    public String  getdateOfBirth();

    public String  getmobileNumber();

    public String getEmailId();

    public void setEmailId(String emailId);

    public void birthYear(int birthYear);

    public void birthMonth(int birthMonth);

    public int getAge();

    public String getOtp();

    public void setOtp(String otp);

    public void resendOtp();
}

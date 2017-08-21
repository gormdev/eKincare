package com.ekincare.util;

import java.util.regex.Pattern;

import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

public class ValidationClass {
    private  static String emailPATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z]+(\\.[A-Za-z]+)*(\\.[A-Za-z]{2,3})$";
    private  static String phonePATTERN = "^[0-9]{10}$";
    private  static String zipPATTERN = "^[0-9]{5}$";
    


    private  static String NamePATTERN = "[A-Za-z]{2,20}";

    
    private  static String emailERRORMESSAGE = "Invalid Email";
    private  static String phoneERRORMESSAGE = " Invalid Mobie Number";
    private  static String firstNameERRORMESSAGE = "Invalid Name";
    private static String zipERRORMESSAGE = "Invalid Zipcode";


    public static boolean isEmailAddress(EditText editText,TextInputLayout textInputLayout, boolean required) {
        return IsValidField(editText,textInputLayout, emailPATTERN, emailERRORMESSAGE, required);
        
    }


    
    
    public static boolean IsPhoneNumber(EditText editText,TextInputLayout textInputLayout, boolean required)
    {
        return IsValidField(editText,textInputLayout, phonePATTERN, phoneERRORMESSAGE, required);
    }

    public static boolean IsValidField(EditText editText,TextInputLayout textInputLayout, String regex, String errMsg, boolean required)
    {
 
        String text = editText.getText().toString().trim();
        
        editText.setError(null);
        
        if ( required && !HasSomeText(editText,textInputLayout) ) return false;
        
        if (required && !Pattern.matches(regex, text))
        {
            textInputLayout.setError(errMsg);
            return false;
        }
        if(text.length() !=0 && !Pattern.matches(regex, text)){
            textInputLayout.setError(errMsg);
            return false;
       }
        return true;
    }
    
    public static boolean HasSomeText(EditText editText, TextInputLayout textInputLayout)
    {
        String text = editText.getText().toString().trim();
        textInputLayout.setError(null);
        if (text.length() == 0)
        {
            textInputLayout.setError("Empty field is not allowed.");
            return false;
        }
        textInputLayout.setErrorEnabled(false);
        return true;
    }

}

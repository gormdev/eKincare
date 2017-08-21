package com.ekincare.util;

import android.util.Log;

/**
 * Created by RaviTejaN on 05-01-2017.
 */

public class CalculationUtility {
    public static boolean between(Double num, int lower, int upper, boolean inclusive)
    {
        return inclusive
                ? lower <= num && num <= upper
                : lower < num && num < upper;
    }

    public static boolean between2(Double num, Double lower, Double upper, boolean inclusive)
    {
        return inclusive
                ? lower <= num && num <= upper
                : lower < num && num < upper;
    }


    public static int convertTOInches(int feet,int inch){
        int inches = (feet *12) + inch ;
        Log.e("HeightInInches", ""+inches);
        return inches;
    }

    public static double convertToMeters(int feet, int inch){
        return convertTOInches(feet,inch) * 0.0254;
    }

    public static double calculateBMI(int feet,int inch, double weight){
        double height = convertToMeters(feet,inch);
        double bmi  = weight/(height*height);
        return bmi;
    }
    public static double convertFeetandInchesToCentimeter(String feet, String inches) {
        double heightInFeet = 0;
        double heightInInches = 0;
        try {
            if (feet != null && feet.trim().length() != 0) {
                heightInFeet = Double.parseDouble(feet);
            }
            if (inches != null && inches.trim().length() != 0) {
                heightInInches = Double.parseDouble(inches);
            }
        } catch (NumberFormatException nfe) {
            return 0;
        }
        Log.e("HeightInCentimeter",""+(heightInFeet * 30.48) + (heightInInches * 2.54));
        return (heightInFeet * 30.48) + (heightInInches * 2.54);
    }
    public static String convertCentimeterToHeight(double d) {
        int feetPart = 0;
        int inchesPart = 0;
        if (String.valueOf(d) != null && String.valueOf(d).trim().length() != 0) {
            feetPart = (int) Math.ceil((d / 2.54) / 12);
            inchesPart = (int) Math.ceil((d / 2.54) - (feetPart * 12));
        }
        return String.format("%d' %d''", feetPart, inchesPart);
    }

}

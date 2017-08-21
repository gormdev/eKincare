package com.ekincare.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rakeshk on 19-02-2015.
 */
public class DateUtility {

    public static String getConvertDob(String time){

        String inputPattern = "yyyy-MM-dd";

        String outputPattern = "MMM dd, yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);

        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);        Date date = null;

        String str = null;        try {

            date = inputFormat.parse(time);

            str = outputFormat.format(date);

        } catch (ParseException e) {

            e.printStackTrace();

        }        System.out.println("Date====DateUtility.getConvertedDateNow==="+str);

        return str;

    }

    public static String parseTodaysDate(String time) {
        String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";

        String outputPattern = "MMM dd, yyyy";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String getconvertdate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy");
        Date parsed = null;
        try {
            parsed = inputFormat.parse(date);
        } catch (ParseException e) {
        	e.printStackTrace();
            return "Input Date invalid";
        }
        String outputText = outputFormat.format(parsed);
        //System.out.println("outputText....." + outputText);
        return outputText;
    }




    public static String getconvertNotificationdate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd");

        Date parsed = null;
        try {
            parsed = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Input Date invalid";
        }
        String outputText = outputFormat.format(parsed);
        //System.out.println("outputText....." + outputText);
        return outputText;
    }


    public static String googleFitDate(String time) {
        String inputPattern;
        String outputPattern;
            if(time.contains("/")){
                 inputPattern = "yyyy/MM/dd";
                 outputPattern = "MMM dd";
            }else if(time.contains("-")){
                 inputPattern = "dd-MMM-yyyy";
                 outputPattern = "MMM dd";
            }else if(time.contains(",")){
                inputPattern = "MMM dd,yyyy";
                outputPattern = "MMM dd";
            }else{
                inputPattern = "dd MMM yyyy";
                outputPattern = "MMM dd";
            }

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }




}

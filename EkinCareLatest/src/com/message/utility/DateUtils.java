package com.message.utility;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ekincare.R;

import org.joda.time.Period;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by matthewpage on 6/27/16.
 */
public class DateUtils {

    public static String getTimestamp(Context context, long then)
    {
        long now = System.currentTimeMillis();

        // convert to seconds
        long nowSeconds = now / 1000;
        long thenSeconds = then / 1000;

        int minutesAgo = ((int) (nowSeconds - thenSeconds)) / (60);
        if (minutesAgo < 1)
            return context.getString(R.string.date_now);
        else if (minutesAgo == 1)
            return context.getString(R.string.date_a_minute_ago);
        else if (minutesAgo < 60)
            return minutesAgo + " " + context.getString(R.string.date_minutes_ago);

        // convert to minutes
        long nowMinutes = nowSeconds / 60;
        long thenMinutes = thenSeconds / 60;

        int hoursAgo = (int) (nowMinutes - thenMinutes) / 60;
        int thenDayOfMonth = getDayOfMonth(then);
        int nowDayOfMonth = getDayOfMonth(now);
        if (hoursAgo < 7 && thenDayOfMonth == nowDayOfMonth) {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            return simpleDateFormat.format(date);
        }

        // convert to hours
        long nowHours = nowMinutes / 60;
        long thenHours = thenMinutes / 60;

        int daysAgo = (int) (nowHours - thenHours) / 24;
        if (daysAgo == 1) {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String yesterdayString = context.getString(R.string.date_yesterday);
            return yesterdayString + " " + simpleDateFormat.format(date);
        } else if (daysAgo < 7) {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE h:mm a");
            return simpleDateFormat.format(date);
        }

        int thenYear = getYear(then);
        int nowYear = getYear(now);
        if (thenYear == nowYear) {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, h:mm a");
            return simpleDateFormat.format(date);
        } else {
            Date date = new Date(then);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, h:mm a");
            return simpleDateFormat.format(date);
        }
    }

    private static int getDayOfMonth(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormate = new SimpleDateFormat("d");
        return Integer.parseInt(simpleDateFormate.format(date));
    }

    private static int getYear(long time) {
        Date date = new Date(time);
        SimpleDateFormat simpleDateFormate = new SimpleDateFormat("yyyy");
        return Integer.parseInt(simpleDateFormate.format(date));
    }

    public static boolean dateNeedsUpdated(Context context, long time, String date) {
        return date == null || date.equals(getTimestamp(context, time));
    }

    @SuppressLint("SimpleDateFormat")
    public static int differenceBtwDate(String strFirstDates, String strSecondDate) {
        int returnDate = 0;
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = format.parse(strFirstDates);
            secondDate = format.parse(strSecondDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("ListAdapter.differenceBtwDate firstDate="+firstDate + " secondDate= "+secondDate);

        if (firstDate != null && secondDate != null) {

            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(firstDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(secondDate);

            Period p = new Period(firstDate.getTime(), secondDate.getTime());
            int diffYear = p.getYears();
            int diffMonth = p.getMonths();
            int diffDay = p.getDays();
            int hours = p.getHours();
            int minutes = p.getMinutes();

            diffYear+=1;

            returnDate = diffYear;
        }
        return returnDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static int differenceBtwDay(String strFirstDates, String strSecondDate) {
        int returnDate = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

        Date firstDate = null;
        Date secondDate = null;
        try {
            firstDate = format.parse(strFirstDates);
            secondDate = format.parse(strSecondDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("ListAdapter.differenceBtwDay firstDate="+firstDate + " secondDate= "+secondDate);

        if (firstDate != null && secondDate != null) {

            Calendar startCalendar = new GregorianCalendar();
            startCalendar.setTime(firstDate);
            Calendar endCalendar = new GregorianCalendar();
            endCalendar.setTime(secondDate);

            Period p = new Period(firstDate.getTime(), secondDate.getTime());
            int diffYear = p.getYears();
            int diffMonth = p.getMonths();
            int diffDay = p.getDays();
            int hours = p.getHours();
            int minutes = p.getMinutes();

            hours+=1;

            returnDate = hours;
        }
        return returnDate;
    }
}

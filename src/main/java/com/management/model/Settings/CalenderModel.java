package com.management.model.Settings;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Lukman.Arogundade on 8/22/2016.
 */
public class CalenderModel {
    private int month;
    private int year;
    private int dayOfWeek;
    private int dayOfMonth;
    private int dayOfYear;
    private int day;
    private String date;
    private String monthString;
    private String firstDayOfMonth;
    private String lastDayOfMonth;

    public CalenderModel() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Africa/Lagos"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        SimpleDateFormat formatExtra = new SimpleDateFormat("ddMMyyyy");
        //getTime() returns the current date in default time zone

        Date date = calendar.getTime();

        int day = calendar.get(Calendar.DATE);
        //Note: +1 the month for current month
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);


        this.dayOfMonth = dayOfMonth;
        this.dayOfWeek = dayOfWeek;
        this.year = year;
        this.month = month;
        this.dayOfWeek = dayOfYear;
        this.day = day;
        this.setDate(format.format(date));
        //this.monthString = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH) - 1];
        this.monthString = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH) == 0 ? 11 : calendar.get(Calendar.MONTH) - 1];


        //TODO CLEAN THIS UP

        int yearpart = calendar.get(Calendar.YEAR);
        int monthPart = calendar.get(Calendar.MONTH);
        int dateDay = 1;
        calendar.set(yearpart, monthPart, dateDay);
        int numOfDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        //System.out.println("Number of Days: " + numOfDaysInMonth);
        //System.out.println("First Day of month: " + calendar.getTime());

        this.firstDayOfMonth = formatExtra.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, numOfDaysInMonth - 1);


        // System.out.println("Last Day of month: " + calendar.getTime());
        this.lastDayOfMonth = formatExtra.format(calendar.getTime());


    }

    public String getFirstDayOfMonth() {
        return firstDayOfMonth;
    }

    public void setFirstDayOfMonth(String firstDayOfMonth) {
        this.firstDayOfMonth = firstDayOfMonth;
    }

    public String getLastDayOfMonth() {
        return lastDayOfMonth;
    }

    public void setLastDayOfMonth(String lastDayOfMonth) {
        this.lastDayOfMonth = lastDayOfMonth;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public String getMonthString() {
        return monthString;
    }

    public void setMonthString(String monthString) {
        this.monthString = monthString;
    }


}

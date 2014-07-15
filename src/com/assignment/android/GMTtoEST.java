package com.assignment.android;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GMTtoEST {
    public static void main(String args[]) {

        Date date = new Date();
        DateFormat estFormat = new SimpleDateFormat();
        DateFormat gmtFormat = new SimpleDateFormat();
        TimeZone gmtTime = TimeZone.getTimeZone("GMT");
        TimeZone estTime = TimeZone.getTimeZone("EST");
        estFormat.setTimeZone(gmtTime);
        gmtFormat.setTimeZone(estTime);
        System.out.println("GMT Time: " + estFormat.format(date));
        System.out.println("EST Time: " + gmtFormat.format(date));

    }

    public String ReturnMeEst(Date GmtTime) {
        // Date date = new Date();

        DateFormat estFormat = new SimpleDateFormat();
        DateFormat gmtFormat = new SimpleDateFormat();

        TimeZone gmtTime = TimeZone.getTimeZone("GMT");
        TimeZone estTime = TimeZone.getTimeZone("EST");

        estFormat.setTimeZone(gmtTime);

        String timeInGmt = "";
        gmtFormat.setTimeZone(estTime);

        // System.out.println("GMT Time: " + estFormat.format(date));
        System.out.println("EST Time: " + gmtFormat.format(GmtTime));

        timeInGmt = gmtFormat.format(GmtTime);

        return timeInGmt;
    }

}

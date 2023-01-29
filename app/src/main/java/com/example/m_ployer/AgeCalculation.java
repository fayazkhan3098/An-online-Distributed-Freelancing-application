package com.example.m_ployer;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
public class AgeCalculation {
    public static int cage;
   // public int yrs=cage;
    public static Date StringToDate(String dob) throws ParseException{
        //Instantiating the SimpleDateFormat class
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Parsing the given String to Date object
        Date date = formatter.parse(dob);
        System.out.println("Date object value: "+date);
        return date;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void give(String db) throws ParseException {
        //Reading name and date of birth from the user
        String dob = db;
        //Converting String to Date
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = formatter.parse(dob);
        //Converting obtained Date object to LocalDate object
        Instant instant = date.toInstant();
        ZonedDateTime zone = instant.atZone(ZoneId.systemDefault());
        LocalDate givenDate = zone.toLocalDate();
        //Calculating the difference between given date to current date.
        Period period = Period.between(givenDate, LocalDate.now()).normalized();
        cage=period.getYears();
        //(period.getYears()+" years "+period.getMonths()+" and "+period.getDays()+" days");
    }
}

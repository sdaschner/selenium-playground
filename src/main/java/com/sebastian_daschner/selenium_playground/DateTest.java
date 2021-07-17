package com.sebastian_daschner.selenium_playground;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DateTest {

    public static void main(String[] args) {
        String dateString = "monday, june 28, 2021";
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .appendPattern("EEEE, MMMM dd, yyyy")
                .toFormatter().withLocale(Locale.ENGLISH);
        LocalDate date = formatter.parse(dateString, LocalDate::from);
        System.out.println("date = " + date);
    }

}

package ru.autoopt.constat.util.common;

import com.fasterxml.jackson.databind.JsonNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonHelper {

    public static String getString(JsonNode node) {
        return String.valueOf(node).replaceAll("^\"|\"$", "").replaceAll("\\\\", "");
    }

    public static Boolean dateIsBeforeYear(JsonNode date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatter.parse(getString(date)));
            calendar.add(Calendar.YEAR, 1);
            return calendar.getTime().before(new Date());
        } catch (ParseException e) {
            return null;
        }

    }

    public static Date getDatePlusNMonth(int n) {
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.MONTH, n);
        return currentDate.getTime();
    }

}

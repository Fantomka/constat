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

    public static Boolean dateIsBefore2022_07_01(JsonNode date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(getString(date)).before(formatter.parse("2022-07-01"));
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

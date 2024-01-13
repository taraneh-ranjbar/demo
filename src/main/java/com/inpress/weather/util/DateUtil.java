package com.inpress.weather.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static LocalDateTime convertDate(String dateStr) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr);
        return localDateTime;
    }

    public static LocalTime convertTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(time, formatter);
    }
}

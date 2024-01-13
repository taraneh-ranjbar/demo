package com.inpress.weather.util;

import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;

public class Validate {
    private static final Logger log = LogManager.getLogger(Validate.class);

    public static int checkCityValidate(String value) {
        if (isNull(value)) {
            String error = "value ===> (" + value + ") not valid, or empty ===> " + value;
            log.error(error);
            return ConstantResponse.CITY_NAME_NULL_EXCEPTION;
        }
        if (isNumeric(value)) {
            String error = "value not valid or is numeric, value ===> " + value;
            log.error(error);
            return ConstantResponse.CITY_NAME_NUMERIC_EXCEPTION;
        }
        return ConstantResponse.OK;
    }


    public static boolean isNull(String data) {
        return !StringUtils.hasText(data);
    }

    public static boolean isNumeric(String str) {
        if (str == null || str.trim().length() == 0) {
            return false;
        }
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidName(String name) {
        return !TextUtils.isEmpty(name) && name.length() > 2;
    }

    public static String formatPersianDate(String date) {

        if (date.length() == 6) {
            return date.substring(0, 2) + "-" + date.substring(2, 4) + "-" + date.substring(4, 6);
        }
        if (date.length() == 8) {
            return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
        }
        return date;
    }

    public static String formatPersianTime(String time) {
        if (time.length() == 6) {
            return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
        }
        return time;
    }

    public static boolean isValidateLat(float lat) {

        if (lat < -90 || lat > 90) {
            return false;
        }
        return true;
    }

    public static boolean isValidateLng(float lng) {
        if (lng < -180 || lng > 180) {
            return false;
        }
        return true;
    }
}

package com.dmitrytarianyk.dailytasks.util;

import java.util.Calendar;

public class DateTimeHelper {

    public static long getNowInMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);

        return calendar.getTimeInMillis();
    }
}

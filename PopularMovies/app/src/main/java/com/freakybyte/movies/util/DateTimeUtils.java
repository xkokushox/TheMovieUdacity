package com.freakybyte.movies.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Jose Torres on 01/12/2016.
 */

public class DateTimeUtils {

    public static String getYearByDate(String date) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date));
        } catch (ParseException e) {
            DebugUtils.logError("GetYearByDate", e.getLocalizedMessage());
        }

        return String.valueOf(cal.get(Calendar.YEAR));
    }
}

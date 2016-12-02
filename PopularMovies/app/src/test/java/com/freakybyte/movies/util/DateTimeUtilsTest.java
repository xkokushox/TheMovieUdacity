package com.freakybyte.movies.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Jose Torres on 01/12/2016.
 */

public class DateTimeUtilsTest {

    @Test
    public void testGetYearByDate() throws Exception {
        String expectedYear = "2015";
        String testDate = "2015-12-16";
        assertEquals("The Date is not good parsed", expectedYear, DateTimeUtils.getYearByDate(testDate));
    }
}

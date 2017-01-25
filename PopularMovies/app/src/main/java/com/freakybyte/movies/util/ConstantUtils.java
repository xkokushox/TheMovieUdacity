package com.freakybyte.movies.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Jose Torres on 09/11/2016.
 */

public class ConstantUtils {

    public static final int POPULAR = 0;
    public static final int NEW = 1;
    public static final int TOP_RATED = 2;
    public static final int UPCOMING = 3;
    public static final int FAVORITE = 4;

    @IntDef({POPULAR, NEW, TOP_RATED, UPCOMING, FAVORITE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FilterMovie{}

}

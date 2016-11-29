package com.freakybyte.movies.util;

import android.net.Uri;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

/**
 * Created by Jose Torres on 28/11/2016.
 */

public class ImageUtils {

    public static ImagePipeline getImagePipeline() {
        return Fresco.getImagePipeline();
    }

    public static boolean isImageInCache(Uri uri) {
        return getImagePipeline().isInBitmapMemoryCache(uri);
    }
    /*
    public static boolean isImageInMemoryCache(Uri uri){

    }
    public static boolean isImageInDiskCache(Uri uri){

    }*/

    public static String getMovieUri(boolean isPoster, String path) {
        if (isPoster)
            return "https://image.tmdb.org/t/p/w500/" + path;
        else
            return "https://image.tmdb.org/t/p/w780/" + path;
    }

}

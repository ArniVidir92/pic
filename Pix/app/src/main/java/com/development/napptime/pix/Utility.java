package com.development.napptime.pix;

import android.util.Log;

import java.util.List;

/**
 * Created by Napptime on 3/16/15.
 */
public class Utility {
    public static double calculateAverage(List<Integer> ratings) {
        Integer sum = 0;
        if(!ratings.isEmpty()) {
            for (Integer rating : ratings) {
                sum += rating;
            }
            return sum.doubleValue() / ratings.size();
        }
        return sum;
    }

    public static int indexOfIn(String theSearched, String theSearchable) {
        theSearched = theSearched.toLowerCase();
        //theSearchable = theSearchable.toLowerCase();
        String[] array = theSearchable.split("\\|");

        for (int i = 0; i < array.length; i++) {
            if(theSearched.equals(array[i].toLowerCase()))
            {return i;}
        }

        return -1;
    }

    public static String addToStringList(String list, String added) {
        if(list.equals("")) return added;
        return list+"|"+added;
    }
}

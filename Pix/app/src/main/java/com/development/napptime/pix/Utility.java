package com.development.napptime.pix;

import java.util.List;

/**
 * Created by Napptime on 3/16/15.
 */
public class Utility {
    public static double calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if(!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return sum.doubleValue() / marks.size();
        }
        return sum;
    }
}

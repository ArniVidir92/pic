package com.development.napptime.pix;

import android.graphics.Bitmap;

/**
 * Created by SnorriAgust on 14/04/2015.
 * This is a helper class that allows us to scale bitmaps
 */
public class BitmapScaler
{
    // Scale and maintain aspect ratio given a desired width
    // BitmapScaler.scaleToFitWidth(bitmap, 100);
    public static Bitmap scaleToFitWidth(Bitmap b, int width)
    {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width/4, (int) (b.getHeight() * factor)/4, true);
    }


    // Scale and maintain aspect ratio given a desired height
    // BitmapScaler.scaleToFitHeight(bitmap, 100);
    public static Bitmap scaleToFitHeight(Bitmap b, int height)
    {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor)/4, height/4, true);
    }
}
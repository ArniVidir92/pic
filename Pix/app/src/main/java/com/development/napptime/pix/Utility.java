package com.development.napptime.pix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.widget.Toast;
import com.parse.LogInCallback;
import com.parse.ParseUser;
import java.util.List;

/**
 * Created by Napptime on 3/16/15.
 *
 * Þessi klasi skilgreinir föll sem aðrir klasar geta nýtt sér og virkar sem einskonar library.
 * Hugsa má um þennan klasa sem extension á java.util pakkann
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

    //Checks if the name input has our standards:
    //only letters and numbers and not empty
    //returns true if the name is allowed
    public static boolean CheckName(String name, Context ac){
        if(!name.matches("[a-zA-Z1234567890]*")){
            Toast toast = Toast.makeText(ac, "Please enter a username with only letters and numbers.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if (name.equals("")) {
            Toast toast = Toast.makeText(ac, "Please enter a username", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    //Checks if the name input has our standards:
    //password length with minimum of 6 letters and maximum of 26 letters,
    //password matches the "confirmed" password
    //returns true if the password is allowed
    public static boolean CheckPassword(String password, String passwordConfirm, Context ac){


        if(password.length() < 6){
            Toast toast = Toast.makeText(ac, "password must contain at least 6 letters", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        if(password.length() > 26){
            Toast toast = Toast.makeText(ac, "Password can only contain 25 letters or less", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }


        if(!password.equals(passwordConfirm))
        {
            Toast toast = Toast.makeText(ac, "Password's don't match.", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }


    public static Bitmap rotate(Bitmap img, int defaultCameraId){
        Matrix matrix = new Matrix();
        if( defaultCameraId == Camera.CameraInfo.CAMERA_FACING_BACK ){
            matrix.setRotate(90,img.getWidth()/2,img.getHeight()/2);
        }else{
            matrix.setRotate(270,img.getWidth()/2,img.getHeight()/2);
        }

        return Bitmap.createBitmap(img , 0, 0, img.getWidth(), img.getHeight(), matrix, false);
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float ratio = (float) height/width;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ratio * ((float) newHeight ) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    //Checks if the email input has our standards:
    //Not empty.
    //returns true if the email is allowed
    public static boolean CheckEmail(String email, Context ac){

        if (email.equals("")) {
            Toast toast = Toast.makeText(ac, "Please enter an email", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }
        return true;
    }

    //Change the password of current user if he inputs the right current password
    public void ChangePassword(String username, final String password, final String passwordConfirm, final String newPassword, final Context ac) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {

                if (user != null) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (Utility.CheckPassword(password, passwordConfirm, ac) == true) {
                        currentUser.setPassword(newPassword);
                        currentUser.saveInBackground();
                        Toast toast = Toast.makeText(ac, "Password successfully changed.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(ac, "New password was either too short or too long.", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    Toast toast = Toast.makeText(ac, "Wrong current password", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public static boolean listContains(String s, List<String> list){
        for(int i = 0; i < list.size(); i++ ){
            if( s.equals(list.get(i)) ){
                return true;
            }
        }
        return false;
    }
}

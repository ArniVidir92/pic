package com.development.napptime.pix;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;


/**
 * Created by Napptime on 2/19/15.
 *
 *  Klasinn sýnir þá mynd sem er valin og hægt er að gefa myndinni einkun ef þú ert hluti af grúppunni
 *  sem myndin er í
 */

public class SelectedPhotoActivity extends SuperSettingsActivity {

    private String thumbId = "";
    private String title = "";
    private String description = "";
    private String groupId = "";
    private double rating = 0;
    private ParseUser user;
    private boolean waiting = true;
    private String users = "";
    private ArrayList<Integer> ratings;
    private ParseObject photoObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_photo);

        Bundle extras = getIntent().getExtras();

        fetchExtras(extras);


        RatingBar bar = (RatingBar) findViewById(R.id.ratingBar);
        bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()
        {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                TextView titleView = (TextView) findViewById(R.id.Title);
                titleView.setText("Change!"+rating);
                rate((int)rating);
            }
        });



        user = ParseUser.getCurrentUser();

        getImageAndPutIntoImageView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    public void setImage(Bitmap img){

        ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setImageBitmap(img);

        TextView titleView = (TextView) findViewById(R.id.Title);
        titleView.setText(title);

        TextView descrView = (TextView) findViewById(R.id.InformationTitle);
        descrView.setText(description);
    }

    public void getImageAndPutIntoImageView(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Picture");
        query.whereEqualTo("thumbnailId", thumbId);
        Log.d("asd", "" + thumbId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("Picture is Null fuck", "This is not good.");
                } else {
                    byte[] file;
                    Bitmap image;

                    try {
                        file = object.getParseFile("file").getData();
                        image = BitmapFactory.decodeByteArray(file, 0, file.length);
                        setImage(image);
                    } catch (ParseException error) {
                        error.printStackTrace();
                    }
                }
            }
        });



        getRatings();

    }

    public void fetchExtras(Bundle ex){
        if (ex != null) {
            thumbId = ex.getString("thumbnailId");
            title = ex.getString("title");
            description = ex.getString("description");
            groupId = ex.getString("groupId");
        }else{
            Log.d("Nothing in Bundle", "Error");
        }
    }

    public void getRatings()
    {

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Thumbnail");
        query2.getInBackground(thumbId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    waiting = false;
                    users = object.getString("Raters");
                    photoObject = object;
                    ratings = (ArrayList)object.getList("ratings");
                    int index = Utility.indexOfIn(user.getUsername(),users);
                    if(index!=-1)
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "You have already rated this photo "+ratings.get(index), Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Failed to retrieve data", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }

    public void rate(int x)
    {
        if(waiting)
            return;
        int index = Utility.indexOfIn(user.getUsername(),users);
        if(index!=-1)
        {
            ratings.set(index,x);
        }
        else
        {
            users = Utility.addToStringList(users,user.getUsername());
            ratings.add(x);
        }
        photoObject.put("ratings",ratings);
        photoObject.put("Raters",users);
        photoObject.saveInBackground();
    }

    public void Logout(MenuItem v){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ParseUser.logOut();
                        SelectedPhotoActivity.this.startActivity(new Intent(SelectedPhotoActivity.this, LoginActivity.class));

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

}



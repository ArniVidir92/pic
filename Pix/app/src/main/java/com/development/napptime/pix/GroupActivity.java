package com.development.napptime.pix;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Napptime on 2/7/15.
 *
 *  Klasi sem sýnir einstaka valda grúppu. Þetta inniheldur þá takka sem þarf til að framkvæma
 *  aðgerðir innan hóps og sýnir þær myndir sem eru í núverandi keppni.
 */


public class GroupActivity extends Activity {

    private String groupId = "2";
    private final int maxPicNr = 15;
    private int numberOfPics = 0;

    private String[] titles;
    private String[] description;
    private Bitmap[] pictures;
    private String[] pictureIds;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            groupId = extras.getString("groupId");
        }

        setContentView(R.layout.activity_group);

        getPhotos(groupId, maxPicNr, numberOfPics);

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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(null);
    }

    public void goToPicture(View view)
    {
        Intent i = new Intent( this, SelectedPhotoActivity.class);
        startActivity(i);
    }

    public void goToSignup(View view)
    {
        Intent i = new Intent( this, SignupActivity.class);
        startActivity(i);
    }

    public void goToCreateGroup(View view)
    {
        Intent i = new Intent( this, CreateGroupActivity.class);
        startActivity(i);
    }

    public void initializeListView(){
        list = (ListView) findViewById(R.id.list);
        groupPhotoListAdapter cus = new groupPhotoListAdapter(this,titles, description, pictures);
        list.setAdapter(cus);

        //Button listener for a button that sends the user to Chosen picture if clicked.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent i = new Intent( view.getContext(), SelectedPhotoActivity.class);
                i.putExtra("groupId", groupId);
                i.putExtra("title", titles[position]);
                i.putExtra("description", description[position]);
                i.putExtra("thumbnailId", pictureIds[position]);
                view.getContext().startActivity(i);
            }
        });
    }

    public void updatePhotos(List<ParseObject> photoList, int size) throws ParseException {
        // Temporary arrays that are used to make the titles, description and pictures arrays
        // dynamically bigger
        String[] tempTitles = new String[numberOfPics + size];
        String[] tempDscr = new String[numberOfPics + size];
        Bitmap[] tempPictures = new Bitmap[numberOfPics + size];
        String[] tempPictureId = new String[numberOfPics + size];
        ParseObject photo;
        byte[] file;
        for (int i = 0; i < numberOfPics + size; i++) {
            if( i < numberOfPics){
                tempTitles[i] = titles[i];
                tempDscr[i] = description[i];
                tempPictures[i] = pictures[i];
                tempPictureId[i] = pictureIds[i];
            }else{
                photo = photoList.get(i - numberOfPics);
                tempTitles[i] = photo.getString("title");
                ArrayList<Integer> al = new ArrayList<Integer>();
                al = (ArrayList)photo.getList("ratings");
                double x = Utility.calculateAverage(al);
                tempDscr[i] = photo.getString("description") + " Rating is " + x;
                tempPictureId[i] = photo.getObjectId();
                file = photo.getParseFile("file").getData();
                tempPictures[i] = BitmapFactory.decodeByteArray(file, 0, file.length);
            }
        }

        numberOfPics += size;
        titles = new String[numberOfPics];
        description = new String[numberOfPics];
        pictures = new Bitmap[numberOfPics];
        pictureIds = new String[numberOfPics];
        for(int j = 0; j < numberOfPics; j++){
            titles[j] = tempTitles[j];
            description[j] = tempDscr[j];
            pictures[j] = tempPictures[j];
            pictureIds[j] = tempPictureId[j];
        }

        initializeListView();
    }

    public void getPhotos( String id, int max, int skip ){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Thumbnail");
        query.whereEqualTo("groupId", id);
        query.setLimit(max);
        query.setSkip(skip);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> photoList, ParseException e) {
                if (e == null) {
                    Log.d("picture", "Retrieved " + photoList.size() + " pictures");
                    try {
                        updatePhotos(photoList, photoList.size());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Log.d("picture retrieved", "Error: " + e.getMessage());
                }
            }
        });

    }

}
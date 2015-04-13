package com.development.napptime.pix;

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
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Napptime on 2/7/15.
 *
 *  Klasi sem sýnir einstaka valda grúppu. Þetta inniheldur þá takka sem þarf til að framkvæma
 *  aðgerðir innan hóps og sýnir þær myndir sem eru í núverandi keppni.
 */


public class GroupActivity extends SuperSettingsActivity {

    private String groupId = "2";
    private String groupTheme = "2";
    private String groupName = "2";
    private String groupThemeInfo = "2";
    private final int maxPicNr = 15;
    private int groupMembers = 10;
    private int numberOfPics = 0;

    private String[] titles;
    private String[] description;
    private Bitmap[] pictures;
    private String[] pictureIds;

    private final int RESULT_CODE_THEME = 1;

    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            groupId = extras.getString("groupId");
            groupTheme = extras.getString("groupTheme");
            groupName = extras.getString("groupName");
            groupThemeInfo = extras.getString("groupThemeInfo");
        }

        setContentView(R.layout.activity_group);

        ((TextView)findViewById(R.id.challenge)).setText(groupTheme);
        ((TextView)findViewById(R.id.textViewTitle)).setText(groupName);

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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListView list = (ListView) findViewById(R.id.list);
        list.setAdapter(null);
    }



    public int goToTheme(View view)
    {
        Intent i = new Intent( this, SetThemeActivity.class);
        i.putExtra("groupId", groupId);
        i.putExtra("groupTheme", groupTheme);
        i.putExtra("groupThemeInfo", groupThemeInfo);
        i.putExtra("groupName", groupName);
        startActivityForResult(i, RESULT_CODE_THEME);
        return 0;
    }

    public void goToMembers(View view){
        Intent i = new Intent(this, MembersActivity.class);
        i.putExtra("groupId",groupId);
        startActivity(i);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_CODE_THEME) {
            if (resultCode == RESULT_OK) {
                groupTheme = data.getStringExtra("groupTheme");
                groupThemeInfo = data.getStringExtra("groupThemeInfo");
                ((TextView)findViewById(R.id.challenge)).setText(groupTheme);

            }
        }
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
        int newSize = numberOfPics + size;
        String[] tempTitles = new String[newSize];
        String[] tempDscr = new String[newSize];
        Bitmap[] tempPictures = new Bitmap[newSize];
        String[] tempPictureId = new String[newSize];
        //double[][] tempRatings = new double[newSize][groupMembers];
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
                ArrayList<Integer> al = (ArrayList)photo.getList("ratings");
                double avgRating = Utility.calculateAverage(al);
                tempDscr[i] = photo.getString("description") + " Rating is " + avgRating;
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
        query.whereEqualTo("hasWon", false);
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
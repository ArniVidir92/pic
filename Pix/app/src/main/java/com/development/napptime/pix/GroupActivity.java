package com.development.napptime.pix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;


public class GroupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        //ListView list;
        String[] titles = {
                "Lakehunting",
                "Sunrise",
                "Toon world",
                "AAABB Startx9 helix fossil",
                "My house"
        } ;

        final String[] comments = {
                "Uploaded by John whiskers - 4.65 stars",
                "Uploaded by Solomon - 3.76 stars",
                "Uploaded by Maximillion Pegasus - 4.2 stars",
                "Uploaded by Twitch - 3.7 stars",
                "Uploaded by Steve - 2.4 stars"
        } ;


        int[] images = {
                R.drawable.samplepic1,
                R.drawable.samplepic2,
                R.drawable.samplepic3,
                R.drawable.samplepic4,
                R.drawable.samplepic5
        };


        Bitmap[] bitmaps = ImageHandler.decodeArrayBitmapFromResource(getResources(),images,100,100);



        ListView list = (ListView) findViewById(R.id.list);
        groupPhotoListAdapter cus = new groupPhotoListAdapter(this,titles, comments, bitmaps);
        list.setAdapter(cus);
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
}

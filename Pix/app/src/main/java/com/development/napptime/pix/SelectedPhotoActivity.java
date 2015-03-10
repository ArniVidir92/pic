package com.development.napptime.pix;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


/**
 * Created by Napptime on 2/19/15.
 *
 *  Klasinn sýnir þá mynd sem er valin og hægt er að gefa myndinni einkun ef þú ert hluti af grúppunni
 *  sem myndin er í
 */

public class SelectedPhotoActivity extends Activity {

    private String thumbId = "";
    private String title = "";
    private String description = "";
    private String groupId = "";
    private double rating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_photo);

        Bundle extras = getIntent().getExtras();

        fetchExtras(extras);

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
        if (id == R.id.action_settings) {
            return true;
        }
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

}

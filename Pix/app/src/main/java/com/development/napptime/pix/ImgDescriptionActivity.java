package com.development.napptime.pix;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ImgDescriptionActivity extends Activity {

    private String groupId;
    private String groupName;
    private String imgPath;
    private Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_description);

        fetchExtras(getIntent());

        bmp = BitmapFactory.decodeFile(imgPath);

        setButtonText();

    }

    private void fetchExtras(Intent i){
        groupId = i.getStringExtra("id");
        groupName = i.getStringExtra("name");
        imgPath = i.getStringExtra("imgPath");
    }

    private void setButtonText(){
        Button btn = (Button) findViewById(R.id.saveButton);
        btn.setText(btn.getText().toString() + " " + groupName);
    }

    public void saveToGroup(View v){
        uploadPictures();
    }

    public void uploadPictures() {

        //Use the picture date to provide each .jpg file with
        //a unique name
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String fileName = "Image-" + dateFormat.format(date) + ".jpg";
        String bigFileName = "Big-" + fileName;

        //Thumbnail
        Bitmap smallImage = Utility.getResizedBitmap(bmp, 200, 200);

        //Bigger image
        Bitmap bigImage = Utility.getResizedBitmap(bmp, 500, 500);

        // This method uploads both a thumbnail and a big picture
        uploadToParseCloud(smallImage, bigImage, fileName, bigFileName);


    }

    public void uploadToParseCloud(Bitmap smallImage, final Bitmap bigImage, String filename, final String bigFilename) {
        String description = getImgDescription();

        // Make thumbnail
        ByteArrayOutputStream streamSmall = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100§
        smallImage.compress(Bitmap.CompressFormat.JPEG, 100, streamSmall);

        // Make bigger picture which we also store
        ByteArrayOutputStream streamBig = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100§
        bigImage.compress(Bitmap.CompressFormat.JPEG, 100, streamBig);

        byte[] thumbnailPic = streamSmall.toByteArray();
        final byte[] picture = streamBig.toByteArray();

        // Create the ParseFile
        ParseFile file = new ParseFile(filename, thumbnailPic);
        // Upload the image into Parse Cloud
        final ParseObject thumbnail = new ParseObject("Thumbnail");
        thumbnail.put("file", file);
        thumbnail.put("groupId", groupId);
        thumbnail.put("title", "");
        thumbnail.put("Raters", "");
        thumbnail.put("ratings", new ArrayList<Integer>());
        thumbnail.put("user", ParseUser.getCurrentUser().getUsername());
        thumbnail.put("description", description);
        thumbnail.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    String thumbnailId = thumbnail.getObjectId();
                    uploadBig(thumbnailId, bigFilename, picture);

                } else {
                    // Failed
                    Log.d("Error", " Exception:" + ex.toString());
                }
            }
        });
    }

    public void uploadBig(String thumbId, String bigFilename, byte[] pic){
        ParseFile fileBig = new ParseFile(bigFilename, pic);
        // Upload the image into Parse Cloud
        ParseObject picture = new ParseObject("Picture");
        picture.put("file", fileBig);
        picture.put("thumbnailId", thumbId);
        picture.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);

                    startActivity(myIntent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed upploading big pic", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getImgDescription(){
        EditText descr = (EditText) findViewById(R.id.editTextDescription);
        return descr.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_img_description, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

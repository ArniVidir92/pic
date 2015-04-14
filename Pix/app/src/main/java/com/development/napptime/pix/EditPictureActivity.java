package com.development.napptime.pix;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Napptime on 13/04/15.
 *
 */


public class EditPictureActivity extends SuperSettingsActivity implements SurfaceHolder.Callback {

    private String imgPath;
    private Bitmap bmp;
    private Rect src;
    private Rect bmpRect;
    int defaultCameraId;

    ArrayList<String> list = new ArrayList<String>();

    private Spinner grpSpinner;

    private String[] groupIds;
    private String[] groupNames;

    private SurfaceHolder holder;
    private SurfaceView surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            imgPath = extras.getString("imgPath");
            defaultCameraId = extras.getInt("defaultCameraId");
        }
        setContentView(R.layout.activity_edit_picture);


        ActionBar aBar = getActionBar();
        aBar.hide();

        //getGroups();

        bmp = BitmapFactory.decodeFile(imgPath);

        src = new Rect(0,0, bmp.getWidth(), bmp.getHeight());

        surface = (SurfaceView) findViewById(R.id.surface);
        holder = surface.getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas c = holder.lockCanvas(null);
                onDraw(c);
                holder.unlockCanvasAndPost(c);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });

        //populateSpinnerWidget();

        File file = new File(imgPath);

        if (file.exists()) {
            file.delete();
        } else {
            System.err.println(
                    "I cannot find '" + imgPath);
        }
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        bmpRect = new Rect(0,0, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bmp, src, bmpRect, null);
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

        //Store pictures in Documents
        File folder = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File pictureFile = new File(folder, fileName);
        if (pictureFile.exists()) pictureFile.delete();

        // This method uploads both a thumbnail and a big picture
        uploadToParseCloud(smallImage, bigImage, fileName, bigFileName);


    }

    public void uploadToParseCloud(Bitmap smallImage, final Bitmap bigImage, String filename, final String bigFilename) {
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
        thumbnail.put("groupId", randomId());
        thumbnail.put("title", randomName());
        thumbnail.put("Raters", "");
        thumbnail.put("ratings", new ArrayList<Integer>());
        thumbnail.put("user", ParseUser.getCurrentUser().getUsername());
        thumbnail.put("description", "Ekkert rosalega fin mynd af " + randomName());
        thumbnail.put("rating", 4.5);
        thumbnail.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    String thumbnailId = thumbnail.getObjectId();
                    ParseFile fileBig = new ParseFile(bigFilename, picture);
                    // Upload the image into Parse Cloud
                    ParseObject picture = new ParseObject("Picture");
                    picture.put("file", fileBig);
                    picture.put("thumbnailId", thumbnailId);

                    picture.saveInBackground();
                } else {
                    // Failed
                    Log.d("Error"," Exception:"+ ex.toString());
                }
            }
        });
    }

    public String randomName(){
        String[] names = {"Arni", "Laama", "Ivan", "Snorri", "Lexi", "Android", "Potential winner", "Sunset", "Minecraft"
                , "Lego kubbur", "Platypus", "Giraffe", "Purple", "Purple Picture", "Purple drank", "Hnetusmjör", "Peanut butter", "Dora", "Clock"
                , "Shawarma", "Taco", "Pizza", "Toast", "HM 5: Swim", "Pogy master", "Nii-san", "Sleepless", "Groot", "Tauren chieftain"};

        return names[(int) Math.floor(Math.random() * names.length)];
    }

    public String randomId() {
        String[] Ids = {"sr5x4osaBS"};

        return Ids[(int) Math.floor(Math.random() * Ids.length)];
    }


    /*
Spinner widget stuff for adding picture to a grp
*/
    public void getGroups(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupMembers", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> groupList, ParseException e) {
                if (e == null) {
                    Log.d("Groups", "Retrieved " + groupList.size() + " Groups");
                    prepareTheSpinner(groupList);
                } else {
                    Log.d("Groups retrieved", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void prepareTheSpinner(List<ParseObject> groupList){
        ParseObject group;
        int listLength = groupList.size();
        groupIds = new String[listLength];
        groupNames = new String[listLength];
        for(int i = 0; i < listLength; i++){
            group = groupList.get(i);
            groupIds[i] = group.getObjectId();
            groupNames[i] = group.getString("groupName");
            list.add(group.getString("groupName"));
        }
    }

    public void populateSpinnerWidget() {

        //create an adapter and use it to move our list into the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(EditPictureActivity.this,
                R.layout.grp_spinner_textview,list) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                // Typeface externalFont=Typeface.createFromAsset(getActivity().getAssets(), "fonts/test.ttf");
                // ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextColor(getResources().getColorStateList((R.color.white)));
                ((TextView) v).setTextSize(16);


                return v;
            }

            public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                //  Typeface externalFont=Typeface.createFromAsset(getActivity().getAssets(), "fonts/test.ttf");
                // ((TextView) v).setTypeface(externalFont);
                v.setBackgroundColor(Color.GREEN);
                ((TextView) v).setTextColor(getResources().getColorStateList((R.color.white)));
                ((TextView) v).setTextSize(16);


                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        grpSpinner.setAdapter(adapter);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas();
        canvas.drawColor(Color.RED);
        holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
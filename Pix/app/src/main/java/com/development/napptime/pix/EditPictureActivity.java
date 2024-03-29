package com.development.napptime.pix;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Napptime on 13/04/15.
 * If a picture is taken with the camera this class manages showing that picture to the user and
 * allows him to edit it (editing is yet to be implemented)
 */


public class EditPictureActivity extends SuperSettingsActivity implements SurfaceHolder.Callback {

    private String imgPath;
    private Bitmap bmp = null;
    private Rect src;
    private Rect bmpRect;
    private ByteArrayOutputStream stream;

    int defaultCameraId;

    private SurfaceHolder holder;
    private SurfaceView surface;

    int screenWidth;
    int screenHeight;

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

        screenWidth = DeviceDimensionsHelper.getDisplayWidth(this);
        screenHeight = DeviceDimensionsHelper.getDisplayHeight(this);

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


    }

    protected void onDraw(Canvas canvas) {

        stream = new ByteArrayOutputStream();
        bmp = BitmapFactory.decodeFile(imgPath);
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bmp = BitmapScaler.scaleToFitHeight(bmp, screenHeight);

        bmp = Utility.rotate(bmp, defaultCameraId);

        bmpRect = new Rect(0,0, canvas.getWidth(), canvas.getHeight());
        src = new Rect(0,0, bmp.getWidth(), bmp.getHeight());

        canvas.drawBitmap(bmp, src, bmpRect, null);
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

    public void chooseGroup(View v){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("groupMembers", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> groupList, ParseException e) {
                if (e == null) {
                    Log.d("Groups", "Retrieved " + groupList.size() + " Groups");
                    changeToGroupSelect(groupList);
                } else {
                    Log.d("Groups retrieved", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void changeToGroupSelect(List<ParseObject> groups){

        int n = groups.size();
        String[] groupIds = new String[n];
        String[] groupNames = new String[n];
        ParseObject group;
        for( int i = 0; i < n; i++ ){
            group = groups.get(i);
            groupIds[i] = group.getObjectId();
            groupNames[i] = group.getString("groupName");
        }

        bmp.recycle();
        bmp = null;

        Intent myIntent = new Intent(this, ChooseGroupActivity.class);
        myIntent.putExtra("names",groupNames);
        myIntent.putExtra("ids", groupIds);
        myIntent.putExtra("imgPath", imgPath);
        myIntent.putExtra("defaultCameraId", defaultCameraId);
        startActivity(myIntent);
    }
}
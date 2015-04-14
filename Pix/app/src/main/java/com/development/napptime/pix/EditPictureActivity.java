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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
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

    ArrayList<String> list = new ArrayList<String>();


    private SurfaceHolder holder;
    private SurfaceView surface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            imgPath = extras.getString("imgPath");
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
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);
        bmpRect = new Rect(0,0, canvas.getWidth(), canvas.getHeight());
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
        File file = new File(imgPath);

        if (file.exists()) {
            file.delete();
        } else {
            System.err.println(
                    "I cannot find '" + imgPath);
        }

        int n = groups.size();
        String[] groupIds = new String[n];
        String[] groupNames = new String[n];
        ParseObject group;
        for( int i = 0; i < n; i++ ){
            group = groups.get(i);
            groupIds[i] = group.getObjectId();
            groupNames[i] = group.getString("groupName");
        }

        Intent myIntent = new Intent(this, ChooseGroupActivity.class);
        myIntent.putExtra("names",groupNames);
        myIntent.putExtra("ids", groupIds);
        startActivity(myIntent);
    }

}
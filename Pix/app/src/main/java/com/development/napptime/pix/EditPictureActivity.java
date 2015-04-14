package com.development.napptime.pix;

import android.app.ActionBar;
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
    private Spinner grpSpinner;

    ArrayList<String> list = new ArrayList<String>();

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
        }
        setContentView(R.layout.activity_edit_picture);

        ActionBar aBar = getActionBar();
        aBar.hide();

        getGroups();

        bmp = BitmapFactory.decodeFile(imgPath);

        src = new Rect(0,0, bmp.getWidth(), bmp.getHeight());

        grpSpinner = (Spinner) findViewById(R.id.spinner);

        grpSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v, int position, long id)
            {
                Toast.makeText(getBaseContext(), "Clickity clack!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                Log.v("routes", "nothing selected");
            }
        });

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

        populateSpinnerWidget();

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
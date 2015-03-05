package com.development.napptime.pix;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Napptime on 2/10/15.
 */

public class FragmentDiscover extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_discover,container,false);

        ListView listView = (ListView) view.findViewById(R.id.list);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };
        //ListView list;
        String[] titles = {
                "TheBoyZ",
                "BigBangTheory",
                "Family Vacation",
                "Public"
        } ;

        String[] comments = {
                "It's just monkey business",
                "The big bang theory cast",
                "Silly vacation photos",
                "Everyone can vote, everyone can participate"
        } ;

        Integer[] imageId = {
                R.drawable.samplepic1,
                R.drawable.samplepic2,
                R.drawable.samplepic3,
                R.drawable.samplepic4
        };


        //==============================================
        //            Test for Parse begin
        //==============================================
/*
        // Make and upload thumbnail
        Bitmap map = ImageHandler.decodeSampledBitmapFromResource(getResources(),R.drawable.samplepic3,100,100);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100§
        map.compress(Bitmap.CompressFormat.JPEG,100 ,stream );
        byte[] image = stream.toByteArray();

        // Create the ParseFile
        ParseFile file  = new ParseFile("picture_1.jpeg", image);
        // Upload the image into Parse Cloud
        final ParseObject thumbnail = new ParseObject("Thumbnail");
        thumbnail.put("file", file);
        thumbnail.put("groupId", "2");
        thumbnail.put("title", "Drasl");
        thumbnail.put("user", "Arni Vidir");
        thumbnail.put("description", "Ekkert rosalega fin mynd");
        thumbnail.put("rating", 4.5);
        thumbnail.saveInBackground(new SaveCallback () {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    String thumbnailId = thumbnail.getObjectId();

                    // Make full image and upload
                    Bitmap mapFull = ImageHandler.decodeSampledBitmapFromResource(getResources(),R.drawable.samplepic3,500,500);
                    ByteArrayOutputStream streamFull = new ByteArrayOutputStream();
                    // Compress image to lower quality scale 1 - 100§
                    mapFull.compress(Bitmap.CompressFormat.JPEG,100 ,streamFull );
                    byte[] imageFull = streamFull.toByteArray();


                    ParseFile fileBig  = new ParseFile("picture_1Full.jpeg", imageFull);
                    // Upload the image into Parse Cloud
                    ParseObject picture = new ParseObject("Picture");
                    picture.put("file", fileBig);
                    picture.put("thumbnailId", thumbnailId);
                    Log.d("thumbnailID", "" +thumbnailId);

                    picture.saveInBackground();
                } else {
                    // Failed
                    Log.d("Faiile", "asdfasdf");
                }
            }
        });

*/
        //==============================================
        //            Test for Parse ends
        //==============================================



        ListView list = (ListView) view.findViewById(R.id.groupList);
        groupListAdapter cus = new groupListAdapter(getActivity(),titles, comments,imageId);
        list.setAdapter(cus);

        return view;
    }
}

package com.development.napptime.pix;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Napptime on 2/10/15.
 *
 * Klasi þessi sér um að sýna notanda þær public grúbbur sem hann gæti haft áhuga á að skoða
 * og/eða taka þátt í. Eins og er gerir þessi klasi ekki það enþá og er þetta bara placeholder
 * fyrir það sem koma skal.
 */

public class FragmentDiscover extends Fragment
{

    private String[] groupIds;
    private String[] groupNames;
    private String[] groupThemes;
    private String[] groupThemeInfos;
    private Bitmap[] covers;
    private String[] coverId;

    private View view;
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_groups, container, false);

        getGroups();


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(null);
    }


    public void getGroups(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("Private", false);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> groupList, ParseException e) {
                if (e == null) {
                    Log.d("Groups", "Retrieved " + groupList.size() + " Groups");
                    prepareTheListView(groupList);
                } else {
                    Log.d("Groups retrieved", "Error: " + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getGroups();
    }

    public void prepareTheListView(List<ParseObject> groupList){
        ParseObject group;
        int listLength = groupList.size();
        coverId = new String[listLength];
        groupIds = new String[listLength];
        groupThemes = new String[listLength];
        groupThemeInfos = new String[listLength];
        groupNames = new String[listLength];
        covers = new Bitmap[listLength];
        for(int i = 0; i < listLength; i++){
            group = groupList.get(i);
            groupIds[i] = group.getObjectId();
            coverId[i] = group.getString("coverPhoto");
            groupNames[i] = group.getString("groupName");
            groupThemes[i] = group.getString("themeName");
            groupThemeInfos[i] = group.getString("themeInfo");
        }


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Thumbnail");
        query.whereContainedIn("objectId", Arrays.asList(coverId));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> coverList, ParseException e) {
                if (e == null) {
                    Log.d("coverPhotos", "Retrieved " + coverList.size() + " cover");
                    try {
                        initializeListView(coverList);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    Log.d("coverPhotos retrieved", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void initializeListView(List<ParseObject> photos) throws ParseException {
        byte[] file;
        ParseObject photo;
        for(int i = 0; i < photos.size(); i++){
            photo = photos.get(i);
            for(int j = 0; j < coverId.length; j++){
                if( photo.getObjectId().equals(coverId[j]) ){
                    file = photo.getParseFile("file").getData();
                    covers[j] = BitmapFactory.decodeByteArray(file, 0, file.length);
                }
            }
        }

        GroupGridAdapter adapter = new GroupGridAdapter(getActivity(), groupNames, covers);
        gridView =(GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "You Clicked at " + groupNames[position], Toast.LENGTH_SHORT).show();

                Intent i = new Intent( view.getContext(), GroupActivity.class);
                i.putExtra("groupId", groupIds[position]);
                i.putExtra("groupName", groupNames[position]);
                i.putExtra("groupTheme", groupThemes[position]);
                i.putExtra("groupThemeInfo", groupThemeInfos[position]);
                view.getContext().startActivity(i);
            }
        });

    }
}

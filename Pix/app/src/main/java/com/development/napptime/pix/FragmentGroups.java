package com.development.napptime.pix;

import android.app.Fragment;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Napptime on 2/10/15.
 */
public class FragmentGroups extends Fragment {

    private String[] groupIds;
    private String[] groupNames;
    private Bitmap[] covers;

    private View view;
    private GridView gridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_groups, container, false);
        //setContentView(R.layout.main);


/*
        GridView gridview = (GridView) view.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity()));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });
*/

        getGroups("UserId");


        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(null);
    }


    public void getGroups(String userId){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
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

    public void prepareTheListView(List<ParseObject> groupList){
        ParseObject group;
        int listLength = groupList.size();
        String[] coverId = new String[listLength];
        groupIds = new String[listLength];
        groupNames = new String[listLength];
        covers = new Bitmap[listLength];
        for(int i = 0; i < listLength; i++){
            group = groupList.get(i);
            groupIds[i] = group.getObjectId();
            coverId[i] = group.getString("coverPhoto");
            groupNames[i] = group.getString("groupName");
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
            file = photo.getParseFile("file").getData();
            covers[i] = BitmapFactory.decodeByteArray(file, 0, file.length);
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
                view.getContext().startActivity(i);
            }
        });

    }
}

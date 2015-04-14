package com.development.napptime.pix;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Napptime on 2/10/15.
 *
 * Klasi þessi sér um að sýna þá hópa sem notandi er nú þegar skráður í og er því miðsvæði appsins
 * og upphafsskjár appsins að inn/nýskráningu undanskildri þegar appið er komið lengra.
 */
public class FragmentPictures extends Fragment {

    private View view;
    private GridView gridView;

    //The ArrayAdapter for the listView
    private ArrayAdapter<String> adapter;

    private ListView listView;

    private boolean isAMember = false;

    private String groupId = "";

    private List<String> memberNames = new ArrayList<String>();
    private List<Integer> listIds = new ArrayList<Integer>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_pictures, container, false);


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



        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(null);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void setVisibility( int opt ){
        Button btn = (Button) getActivity().findViewById(R.id.addMember);
        btn.setVisibility(opt);

        EditText edTex = (EditText) getActivity().findViewById(R.id.userName);
        edTex.setVisibility(opt);
    }

    private void updateListView(){
        final List<String> res = new ArrayList<String>();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");
        query.whereEqualTo("objectId", groupId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObj, ParseException e) {
                ParseRelation<ParseUser> relation = parseObj.getRelation("groupMembers");
                ParseQuery q = relation.getQuery();
                q.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> members, ParseException e) {
                        String userName;
                        for (int i = 0; i < members.size(); i++) {
                            userName = members.get(i).getUsername();
                            if (userName.equals(ParseUser.getCurrentUser().getUsername())) {
                                userName += " (you)";
                                isAMember = true;
                            }
                            res.add(i, userName);
                        }

                        if( isAMember ){
                            setVisibility(View.VISIBLE);
                        }

                        memberNames = res;
                        //Adapts the listItems to our list view using lay_contacts_row
                        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                                R.layout.activity_members_row, R.id.listText, res);
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public void saveInGroup(View view)
    {
        EditText userName = (EditText) getActivity().findViewById(R.id.userName);
        String s = userName.getText().toString();
        userName.clearFocus();
        userName.setText("");
        userName.setHint("Username");
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
        if(s != null && !s.isEmpty()){
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", s);
            query.getFirstInBackground(new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if( parseUser != null ){
                        if( Utility.listContains(parseUser.getUsername(),memberNames) ) {
                            Toast.makeText(getActivity().getApplicationContext(), "This user is already in the group!", Toast.LENGTH_SHORT).show();
                        }else{
                            makeRelation(parseUser);
                        }
                    }else{
                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "This user does not exist", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            });
        }
    }

    public void makeRelation(final ParseUser user){
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Groups");
        q.whereEqualTo("objectId", groupId);
        q.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObj, ParseException e) {
                ParseRelation<ParseUser> relation = parseObj.getRelation("groupMembers");
                relation.add(user);
                parseObj.saveInBackground();
                addToListView(user);
            }
        });

    }

    private void addToListView(ParseUser u){
        String userName = u.getUsername();
        memberNames.add(userName);
        adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                R.layout.activity_members_row, R.id.listText, memberNames);
        listView.setAdapter(adapter);
    }


}

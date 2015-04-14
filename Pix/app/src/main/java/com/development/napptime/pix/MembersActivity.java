package com.development.napptime.pix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;

/**
 * Created by Napptime on 13/04/2015.
 * This activity shows the members in a group
 */

public class MembersActivity extends SuperSettingsActivity {

    //The ArrayAdapter for the listView
    private ArrayAdapter<String> adapter;

    private ListView listView;

    private boolean isAMember = false;

    private String groupId = "";

    private List<String> memberNames = new ArrayList<String>();
    private List<Integer> listIds = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);

        setVisibility(View.GONE);

        listView = (ListView) findViewById(R.id.members_list);

        Intent i = getIntent(); // gets the previously created intent
        groupId = i.getStringExtra("groupId");

        updateListView();
    }

    private void setVisibility( int opt ){
        Button btn = (Button) findViewById(R.id.addMember);
        btn.setVisibility(opt);

        EditText edTex = (EditText) findViewById(R.id.userName);
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
                        adapter = new ArrayAdapter<String>(getBaseContext(),
                                R.layout.activity_members_row, R.id.listText, res);
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public void saveInGroup(View view)
    {
        EditText userName = (EditText) findViewById(R.id.userName);
        String s = userName.getText().toString();
        userName.clearFocus();
        userName.setText("");
        userName.setHint("Username");
        InputMethodManager imm = (InputMethodManager)getSystemService(
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
                            Toast.makeText(getApplicationContext(), "This user is already in the group!", Toast.LENGTH_SHORT).show();
                        }else{
                            makeRelation(parseUser);
                        }
                    }else{
                        Toast toast = Toast.makeText(getApplicationContext(), "This user does not exist", Toast.LENGTH_SHORT);
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
        adapter = new ArrayAdapter<String>(getBaseContext(),
                R.layout.activity_members_row, R.id.listText, memberNames);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_members, menu);
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

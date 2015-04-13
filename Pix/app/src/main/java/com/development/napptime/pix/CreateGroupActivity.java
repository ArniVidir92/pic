package com.development.napptime.pix;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Napptime on 3/9/15.
 *
 * Klasinn gerir notanda kleift að búa til nýjann hóp. Klasinn sér einnig um að vista hópinn á
 * vefþjóninum sem við notum (Parse).
 *
 */


public class CreateGroupActivity extends SuperSettingsActivity {

    public boolean waiting;
    public boolean priv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        waiting = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_private:
                if (checked)
                    priv = true;
                    break;
            case R.id.radio_public:
                if (checked)
                    priv = false;
                    break;
        }
    }

    public void createGroup(View view)
    {
        if(waiting == true)
            return;

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", "Admin");
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    saveGroup(user);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Something went wrong when fetching ADMIN", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });



    }

    public void saveGroup(ParseUser admin){
        ParseObject group = new ParseObject("Groups");

        String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();

        group.put("groupName", name);
        group.put("Private", priv);
        group.put("coverPhoto","bDmC1sXKxf");

        ParseRelation<ParseUser> relation = group.getRelation("groupMembers");

        ParseUser user = ParseUser.getCurrentUser();
        relation.add(admin);
        relation.add(user);

        waiting = true;
        group.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    waiting = false;
                    Toast toast = Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
                    toast.show();

                    // Hooray! Let them use the app now.
                } else {
                    waiting = false;
                    Toast toast = Toast.makeText(getApplicationContext(), "nooSuccess", Toast.LENGTH_SHORT);
                    toast.show();
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}

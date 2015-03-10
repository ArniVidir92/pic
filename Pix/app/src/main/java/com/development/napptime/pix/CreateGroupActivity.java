package com.development.napptime.pix;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

/**
 * Created by Napptime on 3/9/15.
 *
 * Klasinn gerir notanda kleift að búa til nýjann hóp. Klasinn sér einnig um að vista hópinn á
 * vefþjóninum sem við notum (Parse).
 *
 */


public class CreateGroupActivity extends Activity {

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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

        String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
        //ParseUser user = ParseUser.getCurrentUser();

        final ParseObject groups = new ParseObject("Groups");

        groups.put("groupName", name);
        groups.put("Private", priv);

        waiting = true;
        groups.saveInBackground(new SaveCallback() {
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

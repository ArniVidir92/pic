package com.development.napptime.pix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;


public class ChangePasswordActivity extends SuperSettingsActivity {

    public boolean waiting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        waiting = false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_password, menu);
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


    public void ChangePassword(View view) {

        if (waiting == true) return;

        ParseUser user = ParseUser.getCurrentUser();
        String User = user.getUsername();
        String OldPass = ((EditText) findViewById(R.id.OldPasswordBox)).getText().toString();
        String NewPass = ((EditText) findViewById(R.id.NewPasswordBox)).getText().toString();
        String NewPassConfirm = ((EditText) findViewById(R.id.NewPasswordConfirmBox)).getText().toString();

        waiting = true;
        ChangePassword(User, OldPass, NewPass, NewPassConfirm, getApplicationContext());


    }

    //Change the password of current user if he inputs the right current password
    public void ChangePassword(String username, final String password, final String newPassword, final String newPasswordConfirm, final Context ac) {


        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {

                if (user != null) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (Utility.CheckPassword(newPassword, newPasswordConfirm, ac) == true) {

                        currentUser.setPassword(newPassword);
                        currentUser.saveInBackground();
                        Toast toast = Toast.makeText(ac, "Password successfully changed.", Toast.LENGTH_SHORT);
                        toast.show();
                        waiting = false;
                        ChangePasswordActivity.this.startActivity(new Intent(ChangePasswordActivity.this, SettingsActivity.class));
                    }

                } else {
                    waiting = false;
                    Toast toast = Toast.makeText(ac, "Wrong current password", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}

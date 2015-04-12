package com.development.napptime.pix;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import java.text.ParseException;


public class SettingsActivity extends SuperSettingsActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }



    //Change the password of current user if he inputs the right current password
    public void ChangePassword(String username, final String password, final String passwordConfirm, final String newPassword) {

        ParseUser.logInInBackground(username, password, new  LogInCallback() {
            public void done(ParseUser user, com.parse.ParseException e) {

                if (user != null) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (Utility.CheckPassword(password, passwordConfirm, getApplicationContext()) == true) {
                        currentUser.setPassword(newPassword);
                        currentUser.saveInBackground();
                        Toast toast = Toast.makeText(getApplicationContext(), "Password successfully changed.", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "New password was either too short or too long.", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong current password", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }



    //logs the current user out and returns to the login screen.
    public void Logout(View view){
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ParseUser.logOut();
                        SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, LoginActivity.class));

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void ChangePasswordActivity(View view) {
        SettingsActivity.this.startActivity(new Intent(SettingsActivity.this, ChangePasswordActivity.class));
    }
}

package com.development.napptime.pix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;


public class LoginActivity extends Activity {

    public boolean waiting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            Intent i = new Intent( this, MainActivity.class);
            startActivity(i);
            finish();
        }


        setContentView(R.layout.activity_login);
        waiting = false;
        TextView Register_Link = (TextView) findViewById(R.id.Register_Link);
        Register_Link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(View view){

        if(waiting == true)
            return;


        String user = ((EditText) findViewById(R.id.username_box)).getText().toString();
        String pass = ((EditText) findViewById(R.id.password_box)).getText().toString();
        waiting = true;
        login_check(user, pass);

    }

    public void login_check(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, com.parse.ParseException e) {
                if (parseUser != null) {
                    waiting = false;
                    login_func();
                } else {
                    waiting = false;
                    Toast toast = Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    public void login_func(){
        Intent i = new Intent( this, MainActivity.class);
        startActivity(i);
    }




}
package com.development.napptime.pix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignupActivity extends Activity {

    public boolean waiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        waiting = false;

        TextView Login_Link = (TextView) findViewById(R.id.Login_Link);
        Login_Link.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SignupActivity.this.startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });
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

    public void signUp(View view)
    {
        if(waiting ==true)
        return;


        String name = ((EditText) findViewById(R.id.editTextName)).getText().toString();
        String email = ((EditText) findViewById(R.id.editTextEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.editTextPassword)).getText().toString();
        String passwordConfirm = ((EditText) findViewById(R.id.editTextPasswordConfirm)).getText().toString();

        if(!password.equals(passwordConfirm))
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Password's don't match.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        ParseUser user = new ParseUser();
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);



// other fields can be set just like with ParseObject
        waiting = true;
        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    waiting = false;
                    Toast toast = Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT);
                    toast.show();
                    SignupActivity.this.startActivity(new Intent(SignupActivity.this, MainActivity.class));
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

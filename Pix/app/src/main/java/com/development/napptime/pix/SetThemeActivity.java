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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class SetThemeActivity extends Activity {

    private String groupId = "2";
    private String groupTheme = "2";
    private String groupThemeInfo = "2";
    private String groupName = "2";
    private boolean waiting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            groupId = extras.getString("groupId");
            groupTheme = extras.getString("groupTheme");
            groupThemeInfo = extras.getString("groupThemeInfo");
            groupName = extras.getString("groupName");
        }

        setContentView(R.layout.activity_set_theme);
        ((EditText)findViewById(R.id.themeName)).setText(groupTheme);
        ((TextView)findViewById(R.id.groupName)).setText(groupName);
        ((EditText)findViewById(R.id.themeInfo)).setText(groupThemeInfo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_theme, menu);
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


    public int updateTheme(View view)
    {
        if(waiting == true)
            return -1;
        waiting = true;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Groups");


        query.getInBackground(groupId, new GetCallback<ParseObject>() {
            public void done(ParseObject group, ParseException e) {
                if (e == null) {
                    String name = ((EditText)findViewById(R.id.themeName)).getText().toString();
                    String info = ((EditText)findViewById(R.id.themeInfo)).getText().toString();

                    group.put("themeName", name );
                    group.put("themeInfo", info );
                    group.saveInBackground();
                    waiting = false;
                    Toast toast = Toast.makeText(getApplicationContext(), "Theme updated", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent();
                    intent.putExtra("groupTheme",name);
                    intent.putExtra("groupThemeInfo",info);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        return 1;
    }
}

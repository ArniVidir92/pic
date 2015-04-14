package com.development.napptime.pix;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ChooseGroupActivity extends Activity {

    private String[] groupIds;
    private String[] groupNames;
    private String imgPath;
    private int defaultCameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_group);

        fetchExtras(getIntent());
        initListView(groupNames);

    }

    private void fetchExtras(Intent i){
        groupIds = i.getStringArrayExtra("ids");
        groupNames = i.getStringArrayExtra("names");
        imgPath = i.getStringExtra("imgPath");
        defaultCameraId = i.getExtras().getInt("defaultCameraId");
    }

    private void initListView(String[] names){
        ListView ls = (ListView) findViewById(R.id.group_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                R.layout.choosegroup_row, R.id.groupText, groupNames);
        ls.setAdapter(adapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent myIntent = new Intent(getApplicationContext(), ImgDescriptionActivity.class);
                myIntent.putExtra("name",groupNames[position]);
                myIntent.putExtra("id", groupIds[position]);
                myIntent.putExtra("imgPath", imgPath);
                myIntent.putExtra("defaultCameraId",defaultCameraId);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_group, menu);
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

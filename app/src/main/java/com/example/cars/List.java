package com.example.cars;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class List extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this.getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //define variables and navigation drawer
        listView = findViewById(R.id.listview);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Get car data from sample class
        SampleClass sampleClass = new SampleClass();
        sampleClass.setOnDataListener(new SampleClass.DataInterface() {
            @Override
            public void responseData(java.util.List<Car> cars) {
                String[] url = new String[cars.size()];
                String[] title = new String[cars.size()];
                int i=0;
                for(Car car : cars){
                    url[i] = car.getModel().getPhotoURL();
                    title[i] = car.getModel().getTitle();
                    i++;
                }
                //Set adapter to list
                CustomListAdapter adapter = new CustomListAdapter(context, title, url);
                listView.setAdapter(adapter);
            }
        });
        sampleClass.getDataForId(context);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Item selection in navigation drawer
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.nav_map:
                //start map activity (main activity)
                Intent m = new Intent(List.this, MainActivity.class);
                startActivity(m);
                //end current activity
                finish();
                break;
            case R.id.nav_list:
                //start list activity
                Intent l = new Intent(List.this, List.class);
                startActivity(l);
                //end current activity
                finish();
                break;
            case R.id.nav_filter:
                //start filter activity
                Intent f = new Intent(List.this, Filter.class);
                startActivity(f);
                //end current activity
                finish();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

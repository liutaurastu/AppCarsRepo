package com.example.cars;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Locale;

public class Filter extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Toolbar toolbar;
    ListView listView;
    EditText editText;
    private CustomFilterAdapter adapter;
    ArrayList<FilterArrayListClass> arraylist = new ArrayList<FilterArrayListClass>();
    double[] longit, latit;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this.getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        //define variables and navigation drawers
        listView = findViewById(R.id.listview);
        toolbar = findViewById(R.id.toolbar);
        editText = findViewById(R.id.editText);
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
                String[] plateNum = new String[cars.size()];
                int[] batteryLeft = new int[cars.size()];
                longit = new double[cars.size()];
                latit = new double[cars.size()];
                int i=0;

                for(Car car : cars){
                    url[i] = car.getModel().getPhotoURL();
                    title[i] = car.getModel().getTitle();
                    plateNum[i] = car.getPlateNumber();
                    batteryLeft[i] = car.getBatteryPercentage();
                    longit[i] = car.getLocation().getLongitude();
                    latit[i] = car.getLocation().getLatitude();
                    //create class with car values and and add it to an array list
                    FilterArrayListClass filterArrayListClass = new FilterArrayListClass(url[i], title[i], plateNum[i], batteryLeft[i], latit[i], longit[i]);
                    arraylist.add(filterArrayListClass);
                    i++;
                }
                //Setting adapter to list
                adapter = new CustomFilterAdapter(context, i, arraylist, longit, latit);
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
                Intent m = new Intent(Filter.this, MainActivity.class);
                startActivity(m);
                //end current activity
                finish();
                break;
            case R.id.nav_list:
                //start list activity
                Intent l = new Intent(Filter.this, List.class);
                startActivity(l);
                //end current activity
                finish();
                break;
            case R.id.nav_filter:
                //start filter activity
                Intent f = new Intent(Filter.this, Filter.class);
                startActivity(f);
                //end current activity
                finish();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_main_menu, menu);
        return true;
    }

    //menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //define edit text
        if(editText.isEnabled()){
            editText.setEnabled(false);
            editText.setHint(null);
            editText.setText(null);
        }

        //Filter by Plate number
        if (id == R.id.action_filter_by_plate) {
            editText.setEnabled(true);
            editText.setHint("Filter by Plate number");
            editText.requestFocus();

            //edit text listener
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    //get text from edit text and use when calling a filter
                    String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.filterByNumberPlate(text);
                }
            });
            return true;
        }

        //Filter by remaining battery
        if (id == R.id.action_filter_by_battery) {
            editText.setEnabled(true);
            editText.setHint("Filter by remaining battery");
            editText.requestFocus();

            //edit text listener
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    //get text from edit text and use when calling a filter
                    String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.filterByBatteryPercentage(text);
                }
            });
            return true;
        }

        //Sort by distance from users
        if (id == R.id.action_sort_by_distance) {
            client = LocationServices.getFusedLocationProviderClient(this);
            try{
                Task<Location> task = client.getLastLocation();
                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null) {
                            adapter.sortByDistance(location.getLatitude(), location.getLongitude());
                        }
                    }
                });
            }
            catch (SecurityException e)
            {
                Toast.makeText(this, "Access denied: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

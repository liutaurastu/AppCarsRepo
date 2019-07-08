package com.example.cars;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    GoogleMap map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this.getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //define variables and navigation drawer
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //Get car data from sample class
        SampleClass sampleClass = new SampleClass();
        sampleClass.setOnDataListener(new SampleClass.DataInterface() {
            @Override
            public void responseData(java.util.List<Car> cars) {
                double latitSum=0, longitSum=0,latitAvg, longitAvg;
                double[] longit = new double[cars.size()];
                double[] latit = new double[cars.size()];
                String[] address = new String[cars.size()];
                String[] numPlate = new String[cars.size()];
                int i=0;

                for(Car car : cars){
                    latit[i] = car.getLocation().getLatitude();
                    longit[i] = car.getLocation().getLongitude();
                    numPlate[i] = car.getPlateNumber();
                    address[i] = car.getLocation().getAddress();

                    latitSum = latitSum + latit[i];
                    longitSum = longitSum + longit[i];

                    createMarker(latit[i], longit[i], numPlate[i], address[i]); // call marker adding function
                    i++;
                }
                latitAvg = latitSum/i;
                longitAvg = longitSum/i;
                updateCamera(latitAvg, longitAvg); // call camera update function
            }
        });
        sampleClass.getDataForId(context);

        //Map
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
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
                Intent m = new Intent(MainActivity.this, MainActivity.class);
                startActivity(m);
                //end current activity
                finish();
                break;
            case R.id.nav_list:
                //start list activity
                Intent l = new Intent(MainActivity.this, List.class);
                startActivity(l);
                //end current activity
                finish();
                break;
            case R.id.nav_filter:
                //start filter activity
                Intent f = new Intent(MainActivity.this, Filter.class);
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
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

    //add markers to map
    protected Marker createMarker(double latitude, double longitude, String numPlate, String address) {

        return map.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(numPlate)
                .snippet(address));
    }
    //move and zoom map camera
    private void updateCamera(double latitAvg, double longitAvg){
        CameraPosition.Builder cameraPositionBuilder = new CameraPosition.Builder().target(new LatLng(latitAvg,longitAvg));
        cameraPositionBuilder.zoom(10);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPositionBuilder.build());
        map.moveCamera(cameraUpdate);
    }
}
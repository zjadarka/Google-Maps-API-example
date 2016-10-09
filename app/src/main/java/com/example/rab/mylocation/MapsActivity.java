package com.example.rab.mylocation;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private static final int MY_LATITUDE_AND_LONGITUDE_REQUEST_CODE = 2;
    private static final int EDIT_REQUEST_CODE = 0;
    private static final String LATITUDE = "latitude";
    private static final String LONGTITUDE = "longtitude";
    private static final String SNIPPET = "snippet";
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Location location;
    private String latitude = "";
    private String longtitude = "";
    private String snippet = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        addFloatingButtonListener();
        // Restore the position of the marker
        SharedPreferences locationPref = getPreferences(MODE_PRIVATE);
        latitude = locationPref.getString(LATITUDE, "");
        longtitude = locationPref.getString(LONGTITUDE, "");
        snippet = locationPref.getString(SNIPPET, "");
    }


    @Override
    protected void onPause() {
        super.onPause();
        // Save current marker location in shared preferences
        SharedPreferences locationPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = locationPref.edit();
        editor.putString(LATITUDE, latitude);
        editor.putString(LONGTITUDE, longtitude);
        editor.putString(SNIPPET, snippet);
        editor.commit();
        Log.i("TAG", "Sharedpreferences zapisane");
    }

    private void addFloatingButtonListener() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Toast.makeText(MapsActivity.this, "Favorite button clicked", Toast.LENGTH_LONG).show();
//                // Getting LocationManager object from System Service LOCATION_SERVICE
//                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//                // Creating a criteria object to retrieve provider
//                Criteria criteria = new Criteria();
//                // Getting the name of the best provider
//                String provider = locationManager.getBestProvider(criteria, true);
//                // Getting current location
//                if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    location = locationManager.getLastKnownLocation(provider);
//                    //Toast.makeText(MapsActivity.this, "Szerokosc - " + Double.toString(location.getLatitude()) + " " + "Dlugosc - " +  Double.toString(location.getLongitude()), Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Restore last marker
        if (latitude != ""){
            LatLng loc = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude));
            Log.i("TAG", latitude);
            //MarkerOptions marker = new MarkerOptions().position(latLng);
            MarkerOptions markerOptions = new MarkerOptions().position(loc).title("Latitude: " + latitude + " " + "Longtitude: " +  longtitude);
            //Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title("Latitude: " + latitude + " " + "Longtitude: " +  longtitude));
            //marker.showInfoWindow();
            if (snippet.trim().length() > 0) {
                markerOptions.snippet(snippet);
            }
            mMap.addMarker(markerOptions).showInfoWindow();
        }

        //OnMapClickListener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng loc) {

                // Clear map from
                mMap.clear();

                // Shorten latitude and longtitude
                latitude = Double.toString(loc.latitude);
                longtitude = Double.toString(loc.longitude);
                latitude = String.format("%.6s", latitude);
                longtitude = String.format("%.6s", longtitude);
                //Add marker
                Marker marker = mMap.addMarker(new MarkerOptions().position(loc).title("Latitude: " + latitude + " " + "Longtitude: " +  longtitude));
                marker.showInfoWindow();
            }
        });

        //Set onMarkerClickListener to this activity
        googleMap.setOnMarkerClickListener(this);

        // Check if location permission is granted if not then request for permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Request location permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }
    }


    @Override
    //Location permission result
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Check again if location permission is granted
        switch (requestCode){
            case MY_LOCATION_REQUEST_CODE:
                // if yes then reload the map, if not show the toast
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                    Toast.makeText(this, "Access denied", Toast.LENGTH_LONG).show();
                }
            case MY_LATITUDE_AND_LONGITUDE_REQUEST_CODE:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Access denied", Toast.LENGTH_LONG).show();
                }
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng latLng = marker.getPosition();
        Intent intent = new Intent(MapsActivity.this, EditActivity.class);
        intent.putExtra("location", latLng);
        MapsActivity.this.startActivityForResult(intent, EDIT_REQUEST_CODE);
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (EDIT_REQUEST_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    mMap.clear();
                    LatLng position = data.getParcelableExtra("position");
                    snippet = data.getStringExtra("snippet");
                    Toast.makeText(this, snippet, Toast.LENGTH_LONG).show();
                    String lat = Double.toString(position.latitude);
                    String lon = Double.toString(position.longitude);
                    lat = String.format("%.6s", lat);
                    lon = String.format("%.6s", lon);
                    //Add marker
                    Marker marker = mMap.addMarker(new MarkerOptions().position(position).title("Latitude: " + lat + " " + "Longtitude: " +  lon).snippet(snippet));
                    marker.showInfoWindow();
                }
            }
        }
    }
}

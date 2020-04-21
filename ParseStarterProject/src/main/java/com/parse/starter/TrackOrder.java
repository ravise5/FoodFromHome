package com.parse.starter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class TrackOrder extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LocationManager locationManager;

    LocationListener locationListener;

    TextView infoTextView;

    private ParseUser user = ParseUser.getCurrentUser();

    Handler handler = new Handler();

    Boolean driverActive = false;

    public void checkForUpdates(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Request");
        query.whereEqualTo("Name",user.getString("Name"));
        query.whereExists("DeliveryPerson");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e==null && objects.size()>0) {

                    driverActive = true;

                    ParseQuery<ParseUser> query = ParseUser.getQuery();

                    query.whereEqualTo("Name", objects.get(0).getString("DeliveryPerson"));

                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> objects, ParseException e) {

                            if(e==null && objects.size()>0)
                            {
                                ParseGeoPoint driverLocation = objects.get(0).getParseGeoPoint("Location");

                                if(Build.VERSION.SDK_INT<23 || ContextCompat.checkSelfPermission(TrackOrder.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                                {
                                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                    if(lastKnownLocation!=null)
                                    {
                                        ParseGeoPoint userLocation = new ParseGeoPoint(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());

                                        Double distanceInKms = userLocation.distanceInKilometersTo(driverLocation);

                                        Double distance = (double)Math.round(distanceInKms);

                                        infoTextView.setText("Your order is "+ distance.toString()+" kms away!");

                                        LatLng driverLocationLatLng =  new LatLng(driverLocation.getLatitude(),driverLocation.getLongitude());

                                        LatLng userLocationLatLng =  new LatLng(userLocation.getLatitude(),userLocation.getLongitude());

                                        ArrayList<Marker> markers = new ArrayList<>();

                                        markers.add(mMap.addMarker(new MarkerOptions().position(driverLocationLatLng).title("Delivery Boy").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
                                        markers.add(mMap.addMarker(new MarkerOptions().position(userLocationLatLng).title("Your Location")));

                                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                        for(Marker marker:markers)
                                        {
                                            builder.include(marker.getPosition());
                                        }

                                        LatLngBounds bounds = builder.build();

                                        int padding = 70;
                                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,padding);
                                        mMap.animateCamera(cu);
                                        PolylineOptions line =  new PolylineOptions().add(userLocationLatLng,driverLocationLatLng).width(5).color(Color.RED);
                                        mMap.addPolyline(line);


                                    }
                                }
                            }
                        }
                    });


                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        infoTextView = findViewById(R.id.infoTextView);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkForUpdates();
            }
        },2000);
        checkForUpdates();
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1)
        {
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    updateMap(lastKnownLocation);



                }


            }
        }
    }

    public void updateMap(Location location){

        if(driverActive==false) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
            mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location")).showInfoWindow();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap  = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateMap(location);

                ParseUser.getCurrentUser().put("Location",new ParseGeoPoint(location.getLatitude(),location.getLongitude()));

                ParseUser.getCurrentUser().saveInBackground();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(Build.VERSION.SDK_INT<23)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
        else
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if(lastKnownLocation!=null)
                {
                    updateMap(lastKnownLocation);
                }



            }
        }





    }


}

package com.example.rico.aitmaps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Start extends AppCompatActivity {

    private Button b1 , b2;
    String s1 ;
    private LocationManager locationManager;
    private LocationListener locationListener;
    Geocoder geocoder ;
    private static final String[] add = new String[]{
            "Vishwesvariya Boys Hostel" , "Abdul Kalam Boys Hostel" , "Boys Mess" , "Cricket Ground" , "Football Ground" , "Volley Ball Court" , "Food Court" , "Shopping Complex" , "Kalpana Hostel" , "Academic Block" , "Central Gazeebo" , "Aryabhatta Centre" , "MG ROad" , "Main Gate" , "Badminton Court" , "Open Air Cafeteria"
    };

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2) ;


        final AutoCompleteTextView edittext = findViewById(R.id.from) ;
        final AutoCompleteTextView edittext1 = findViewById(R.id.to) ;
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1 , add);
        edittext.setAdapter(adapter);
        edittext1.setAdapter(adapter);


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String start = edittext.getText().toString();
                final String dest = edittext1.getText().toString();
                Intent intent = new Intent(Start.this , Route.class);
                intent.putExtra("Start" , start);
                intent.putExtra("dest" , dest);
                startActivity(intent);
            }
        });

        geocoder = new Geocoder(this, Locale.getDefault());
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double d1 = location.getLatitude() ;
                double d2 = location.getLongitude() ;
                double MAX = 10000 ; int pos = 0 ;
                double coordinates[][] = {{18.604730, 73.875366} , {18.605569, 73.876181} , {18.604659, 73.876258} , {18.605495, 73.875276} , {18.605589, 73.874225} , {18.604900, 73.874341} , {18.604684, 73.874223} , {18.604552, 73.874287} , {18.607763, 73.874751} , {18.606958, 73.874824} ,{18.606275, 73.874771} , {18.606178, 73.875022} , {18.605649, 73.874636} ,{18.606996, 73.873898} , {18.606055, 73.876188} , {18.606348, 73.874802} };
                for(int i=0; i<16 ; i++){
                    double dist = haversine(coordinates[i][0] , coordinates[i][1] , d1 , d2 );
                    if(dist<MAX){
                        MAX = dist ;
                        pos = i ;
                    }
                }
                edittext.setText(add[pos]);
            }

            private double haversine(double lat1, double long1, double lat2, double long2) {
                final int R1 = 6371 ;
                double latdist = toRAd(lat2 - lat1) ;
                double londist = toRAd(long2-long1) ;
                double a = Math.sin(latdist / 2) * Math.sin(latdist / 2) +
                        Math.cos(toRAd(lat1)) * Math.cos(toRAd(lat2)) *
                                Math.sin(londist / 2) * Math.sin(londist / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
                return R1*c ;
            }
            private double toRAd(double v) {
                return v*Math.PI/180 ;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent intent= new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET
                }, 10);
            }
            else{
                configureButton();
            }
        }else{
            configureButton();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;
        }
    }

    private void configureButton() {
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 1000, 10, locationListener);
            }
        });
    }
}

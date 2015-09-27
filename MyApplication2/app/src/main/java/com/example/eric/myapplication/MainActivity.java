package com.example.eric.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    Button btnShowLocation;

    GPSTracker gps;
    double lat;
    double lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowLocation = (Button) findViewById(R.id.show_location);

        btnShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gps = new GPSTracker(MainActivity.this);

                if(gps.canGetLocation()) {
                    lat = gps.getLatitude();
                    lon = gps.getLongitude();

                    Toast.makeText(
                            getApplicationContext(),
                            "-\nLat: " + lat + " Long: "
                                    + lon, Toast.LENGTH_LONG).show();
                }
                if(lat == 0 && lon == 0) {
                    gps.showSettingsAlert();
                }
            }
        });
    }
}

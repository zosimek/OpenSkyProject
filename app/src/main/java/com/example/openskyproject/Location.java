package com.example.openskyproject;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.ib.openskyproject.R;

/**
 * The activity class providing latitude and longitude and state vector of a plane.
 */

public class Location extends AppCompatActivity {

    protected FusedLocationProviderClient fusedLocationProviderClient;
    protected Context context;

    protected double latitude;
    protected double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Initializing method of the class.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_with_location);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(Location.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Location.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
            @Override
            public void onComplete(@NonNull Task<android.location.Location> task) {
                android.location.Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(Location.this,
                                Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);
                        setLatitude(addresses.get(0).getLatitude());
                        setLongitude(addresses.get(0).getLongitude());
                        System.out.println(addresses.get(0).getLatitude());
                        System.out.println(addresses.get(0).getLongitude());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Button btnDistance = findViewById(R.id.btnChooseDistance);
        btnDistance.setOnClickListener(this::mapDisplay);
    }

    /**
     * The method that turns distance provided in km
     * to the corresponding value of latitude.
     *
     * @param distance double val
     * @return double latitude in decimal degrees
     */
    private double degreeOfLat(double distance) {

        int oneDegreeVal = 111;
        double oneMinLat = 1.85;

        if (distance % oneDegreeVal == 0) {
            return distance / oneDegreeVal;
        } else {
            double r = distance % (double) oneDegreeVal;
            double d = (distance - r) / 111;
            double m = (r / oneMinLat) / 60;
            return d + m;
        }
    }

    /**
     * The method that turns distance provided in km
     * to the corresponding value of longitude.
     *
     * @param distance double val
     * @param degree latitude decimal degree
     * @return double longitude
     */
    public static double degreeOfLon(double distance, double degree) {

        int oneDegreeVal = 111;
        double oneMinLat = 1.85;

        if (distance % oneDegreeVal == 0) {
            return (distance / oneDegreeVal) * Math.cos(Math.toRadians(degree));
        } else {
            double r = distance % (double) oneDegreeVal;
            double d = (distance - r) / 111;
            double m = (r / oneMinLat) / 60;
            return (d + m) * Math.cos(Math.toRadians(degree));
        }
    }

    /**
     * The method that displays map with plane pin(s)
     * when correct or incorrect input was provided.
     *
     * @param view
     */

    private void mapDisplay(View view) {
        EditText distanceText = findViewById(R.id.edtDistance);
        TextView tv = findViewById(R.id.tvDistanceError);
        TextInputLayout flightNumberField = findViewById(R.id.flight_number);
        flightNumberField.setHelperText("Requested *");
        flightNumberField.setBoxStrokeColor(getResources().getColor(R.color.purple_500));
        try {
            double distance = Double.parseDouble(distanceText.getText().toString());
            double decimalDegLat = degreeOfLat(distance);
            double decimalDegLon = degreeOfLon(distance, decimalDegLat);
            String url = "https://opensky-network.org/api/states/all?lamin=" + (getLatitude() - decimalDegLat)
                    + "&lomin=" + (getLongitude() - decimalDegLon) + "&lamax=" +
                    (getLatitude() + decimalDegLat) + "&lomax=" + (getLongitude() + decimalDegLon);

            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            com.example.openskyproject.MapsFragment mapsFragment = new com.example.openskyproject.MapsFragment();
            mapsFragment.setArguments(bundle);
            System.out.println(getLongitude());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragmentContainerMap, mapsFragment).commit();
        }catch(Exception e){
            flightNumberField.setHelperText("Provide distance !");
            flightNumberField.setBoxStrokeColor(getResources().getColor(R.color.red));
        }
    }
}
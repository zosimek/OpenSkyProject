package com.example.openskyproject;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import edu.ib.openskyproject.R;

/**
 * The class finding and displaying planes location on googlemap in specified radius
 */


public class ShowFlightsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_flights);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.googlemap);

        mapFragment.getMapAsync(this);
    }

    /**
     * The method adding icons in specific plane locations
     * or notification when something went wrong.
     *
     * @param googleMap
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
       String url = (String) getIntent().getStringExtra("passUrl");
            RequestQueue queue = Volley.newRequestQueue(this);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            res -> {
                                Results1 results1 = gson.fromJson(res.toString(), Results1.class);
                            try {

                                TextView tv = findViewById(R.id.tvDistanceError);
                                TextInputLayout flightNumberField = findViewById(R.id.flight_number);
                                flightNumberField.setHelperText("Requested *");
                                flightNumberField.setBoxStrokeColor(getResources().getColor(R.color.purple_500));

                                ArrayList<com.example.openskyproject.Flights> flightsArrayList = new ArrayList<>();
                                ArrayList<ArrayList> statesList = new ArrayList<>();
                                statesList.addAll(results1.getStates());
                                ArrayList<String> countriesList = new ArrayList<>();
                                for (int i = 0; i < statesList.size(); i++) {
                                    try {
                                        double latitude = Double.parseDouble(statesList.get(i).get(6).toString());
                                        double longitude = Double.parseDouble(statesList.get(i).get(5).toString());
                                        String country = statesList.get(i).get(2).toString();
                                        flightsArrayList.add(new com.example.openskyproject.Flights(longitude, latitude));
                                        countriesList.add(country);
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                                for (int i = 0; i < flightsArrayList.size(); i++) {
                                    LatLng point = new LatLng(flightsArrayList.get(i).getLatitude(),
                                            flightsArrayList.get(i).getLongitude());
                                    MarkerOptions markerOptions = new MarkerOptions().position(point).title("Origin country: " +
                                            countriesList.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.plane));
                                    mMap.addMarker(markerOptions);
                                }
                            }catch(Exception e){
                                TextInputLayout flightNumberField = findViewById(R.id.flight_number);
                                flightNumberField.setHelperText("No flight found in given radius !");
                                flightNumberField.setBoxStrokeColor(getResources().getColor(R.color.red));
                                LatLng pointZero = new LatLng(0, 0);
                                MarkerOptions mZero = new MarkerOptions().position(pointZero).title("No flight has been found !")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.alert));
                                googleMap.addMarker(mZero);}
                            }, error -> {
                        System.out.println("Error");
                    });
                    queue.add(stringRequest);
                    handler.postDelayed(this, 5000);
                }
            };
            handler.postDelayed(runnable, 5000);
        }
}

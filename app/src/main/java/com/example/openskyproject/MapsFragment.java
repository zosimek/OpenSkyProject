package com.example.openskyproject;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import edu.ib.openskyproject.R;

/**
 * The map fragment used in DealWithLocation activity
 */
public class MapsFragment extends Fragment {

    protected String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * The method displaying planes icons on map.
         *
         * @param googleMap
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            System.out.println(getUrl());
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url = getUrl();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try{

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            res -> {
                                Results1 results1 = gson.fromJson(res.toString(), Results1.class);
                                try {
                                    ArrayList<com.example.openskyproject.Flights> flightsArrayList = new ArrayList<>();
                                ArrayList<ArrayList> statesList = new ArrayList<>();
                                statesList.addAll(results1.getStates());
                                ArrayList<String> countriesList = new ArrayList<>();
                                for (int i = 0; i < statesList.size(); i++) {

                                        double latitude = Double.parseDouble(statesList.get(i).get(6).toString());
                                        double longitude = Double.parseDouble(statesList.get(i).get(5).toString());
                                        String country = statesList.get(i).get(2).toString();
                                        flightsArrayList.add(new com.example.openskyproject.Flights(longitude, latitude));
                                        countriesList.add(country);

                                    }
                                    for (int i = 0; i < flightsArrayList.size(); i++) {
                                        LatLng point = new LatLng(flightsArrayList.get(i).getLatitude(),
                                                flightsArrayList.get(i).getLongitude());
                                        MarkerOptions markerOptions = new MarkerOptions().position(point).title("Origin country: " +
                                                countriesList.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.plane));
                                        googleMap.addMarker(markerOptions);

                                    }
                                } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                            }, error -> {
                        System.out.println("Error");

                    });
                    queue.add(stringRequest);
                    handler.postDelayed(this::run, 5000);

                }catch(Exception exception){
                        exception.printStackTrace();
                    }
                }
            };
            handler.postDelayed(runnable, 5000);

        }
    };

    /**
     * Initializing method.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle b = getArguments();

        View view =  inflater.inflate(R.layout.fragment_maps, container, false);
        if(b!=null){
            setUrl(b.getString("url"));}


return view;
    }

    /**
     * "To be contunued..." of onCreateView()
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}
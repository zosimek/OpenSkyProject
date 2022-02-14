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
 * The class finding and displaying plane location (plane of given icao24) on googlemap
 */

public class ShowPlaneMapFragment extends Fragment {

    protected String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * The method adding icons in specific plane locations
         * or notification when something went wrong.
         *
         * @param googleMap
         */

        @Override
        public void onMapReady(GoogleMap googleMap) {

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            String url = getUrl();
            System.out.println(url);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            res -> {
                                Results1 results1 = gson.fromJson(res.toString(), Results1.class);

                               try {
                                   ArrayList<ArrayList> path = results1.getStates();
                                   double latitude = Double.parseDouble(String.valueOf(path.get(0).get(6)));
                                   double longtitude = Double.parseDouble(String.valueOf(path.get(0).get(5)));
                                   LatLng point = new LatLng(latitude, longtitude);
                                   System.out.println(longtitude);
                                   System.out.println(latitude);
                                   MarkerOptions markerOptions = new MarkerOptions().position(point)
                                           .icon(BitmapDescriptorFactory.fromResource(R.drawable.plane));
                                   googleMap.addMarker(markerOptions);
                               }catch(Exception e){
                                   LatLng pointZero = new LatLng(0, 0);
                                   MarkerOptions mZero = new MarkerOptions().position(pointZero).title("No flight has been found !")
                                           .icon(BitmapDescriptorFactory.fromResource(R.drawable.alert));
                                   googleMap.addMarker(mZero);
                               }
                            }, error -> {
                        System.out.println("error");

                    });
                    queue.add(stringRequest);
                    handler.postDelayed(this, 5000);
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        View view = inflater.inflate(R.layout.fragment_show_plane_map, container, false);
        if (bundle != null) {
            setUrl(bundle.getString("plane_url"));
        }
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
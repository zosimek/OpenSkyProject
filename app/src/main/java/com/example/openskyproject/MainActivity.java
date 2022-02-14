package com.example.openskyproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import edu.ib.openskyproject.R;

/**
 * Class of the Main Activity of the app;
 * Welcoming page.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The method initializing the app.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * The method that switches to window that enables to find planes in the given radius
     * (Location).
     *
     * @param view
     */
    public void showArea(View view) {
        Intent intent = new Intent(this, Location.class);
        startActivity(intent);
    }

    /**
     * The method that switches to window that enables to find a plane of given icao24
     * (ShowPlaneActivity).
     * @param view
     */

    public void trackFlightsClicked(View view) {
        Intent intent = new Intent(this, com.example.openskyproject.ShowPlaneActivity.class);
        startActivity(intent);
    }
}
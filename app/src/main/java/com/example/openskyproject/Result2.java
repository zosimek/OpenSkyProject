package com.example.openskyproject;

import java.util.ArrayList;

/**
 * The Class maps result obtained from API when choosing "Locate Flight"
 */
public class Result2 {
    protected String icao;
    protected ArrayList<ArrayList> path;

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public ArrayList<ArrayList> getPath() {
        return path;
    }

    public void setPath(ArrayList<ArrayList> path) {
        this.path = path;
    }

    public Result2(String icao, ArrayList<ArrayList> path) {
        this.icao = icao;
        this.path = path;
    }
}

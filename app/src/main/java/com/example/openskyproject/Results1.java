package com.example.openskyproject;

import java.util.ArrayList;

/**
 * The Classs maps result obtained from API when choosing "Hind in radius"
 */
public class Results1 {

    protected int time;
    protected ArrayList<ArrayList> states;

    public Results1(int time, ArrayList<ArrayList> states) {
        this.time = time;
        this.states = states;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ArrayList<ArrayList> getStates() {
        return states;
    }

    public void setStates(ArrayList<ArrayList> states) {
        this.states = states;
    }
}

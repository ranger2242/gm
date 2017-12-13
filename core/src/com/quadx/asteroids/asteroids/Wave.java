package com.quadx.asteroids.asteroids;

import com.quadx.asteroids.tools.Delta;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 12/11/2017.
 */
public class Wave {
    Delta length;
    ArrayList<Asteroid> rocks;


    public Wave(float lim, ArrayList<Asteroid> rocks) {
        length=new Delta(lim);
        this.rocks=rocks;
    }

    public Wave(Wave wave) {
        this.length=new Delta(wave.getDelta().getLimit());
        rocks=new ArrayList<>();
        for(Asteroid a : wave.getRocks()){
            rocks.add(new Asteroid(a));
        }
    }

    public Delta getDelta() {
        return length;
    }

    public ArrayList<Asteroid> getRocks() {
        return rocks;
    }
}

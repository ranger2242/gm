package com.quadx.asteroids.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Wave;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Chris Cavazos on 12/11/2017.
 */
public class FileHandler {
    public static ArrayList<Wave> initFile() {

        FileHandle file = Gdx.files.internal("data/levels.txt");
        String s;
        ArrayList<String> f=new ArrayList<>();
        ArrayList<Wave> levels = new ArrayList<>();
        s = file.readString();
        String[] st=s.split("\r\n");
        f.addAll(new ArrayList<>(Arrays.asList(st)));
        int c=0;
        s=f.get(c++);
        while (s != null ) {
            if (!s.equals("")) {
                float lim = 0;

                String[] ars = s.split(" ");
                float[] parsed = new float[ars.length];
                int q = 0;
                for (String sa : ars) {
                        parsed[q++] = Float.parseFloat(sa);
                }
                if (ars.length == 2) {
                    lim = parsed[0];
                    ArrayList<Asteroid> rocks = new ArrayList<>();
                    for (int i = 0; i < parsed[1]; i++) {
                        s = f.get(c++);
                        String[] ars2 = s.split(" ");
                        float[] parsed2 = new float[ars2.length];
                        for (int j = 0; j < ars2.length; j++) {
                            parsed2[j] = Float.parseFloat(ars2[j]);
                        }
                        Asteroid p = new Asteroid(new Vector2(Game.res.x * parsed2[0], Game.res.y * parsed2[1]),
                                new Vector2(parsed2[2], parsed2[3]), parsed2[4], parsed2[5], parsed2[6], parsed2[7]);

                        rocks.add(p);
                    }
                    Wave w = new Wave(lim, rocks);
                    levels.add(w);
                }
            }
            if(c<f.size())
            s=f.get(c++);
            else s=null;
        }
        return levels;
    }
}

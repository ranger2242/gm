package com.quadx.asteroids.tools;

import java.text.DecimalFormat;

/**
 * Created by Chris Cavazos on 12/11/2017.
 */
public class LevelGenerator {
    static DecimalFormat f3 = new DecimalFormat("#.000");

    public static void gen2() {
        float n = 30;
        float xsep = Game.res.x / 4 / n;
        float r = (Game.res.y / 2);
        float ang = ((360) / n);
        for (int i = 0; i < n; i++) {

            float x = (xsep * i * 4) / Game.res.x;
            float y = (r / Game.res.y) + (float) (r * Math.sin(Math.toRadians(ang * i * 4))) / Game.res.y;
            if (y < .55 && y > .45)
                y += .2;
            int a = i % 2 == 0 ? -73 : 73;
            System.out.println(f3.format(x) + " " + f3.format(y) + " 100 " + a + " 100 3 3 0");
        }
    }

    public static void gen3() {
        int n = 15;
        float xsep = Game.res.x / n;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < n; j++) {
                float x = (xsep * j) / Game.res.x;
                float y = .05f * (i);
                int a = i % 2 == 0 ? -30 : 30;

                System.out.println(f3.format(x) + " " + f3.format(y) + " "+ a +" 60 100 4 3 45");

            }
        }
    }
}

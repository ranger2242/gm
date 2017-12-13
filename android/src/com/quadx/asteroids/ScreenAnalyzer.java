package com.quadx.asteroids;

import android.content.res.Resources;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Chris on 12/2/2017.
 */

public class ScreenAnalyzer {
    public static Vector2 getScreenResolution() {
        float x = Resources.getSystem().getDisplayMetrics().widthPixels;
        float y = Resources.getSystem().getDisplayMetrics().heightPixels;
        return new Vector2(x, y);
    }
}

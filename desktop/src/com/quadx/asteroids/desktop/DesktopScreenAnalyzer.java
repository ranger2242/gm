package com.quadx.asteroids.desktop;

import com.badlogic.gdx.math.Vector2;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * Created by Chris on 12/2/2017.
 */

class DesktopScreenAnalyzer {
    public static Vector2 getScreenResolution() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        float y= (float) screenSize.getHeight();
// the screen width
        float x= (float) screenSize.getWidth();
        return new Vector2(x, y);
    }

}

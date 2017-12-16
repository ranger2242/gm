package com.quadx.asteroids.asteroids;

import com.quadx.asteroids.asteroids.powerups.Powerup;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.states.AsteroidState;

public class Collision {

    public void collide(Asteroid a, Ship s) {
        for (Triangle t : Triangle.triangulate(a.getShape())) {
            boolean a1 = s.getShape().overlaps(t);
            boolean b = t.overlaps(s.getShape());
            boolean c = !s.isGameOver();
            if ((a1 || b) && c) {
                s.hit();
            }
        }
    }

    public void collide(Asteroid a, Powerup p){
        if (p.collide(a)) {
            AsteroidState.rockHit(a);
            a.wasShot();
        }
    }
}

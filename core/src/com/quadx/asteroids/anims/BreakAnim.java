package com.quadx.asteroids.anims;

import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.shapes1_2.Ngon;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.shapes1_2.Triangle;

import java.util.ArrayList;

import static com.quadx.asteroids.states.AsteroidState.rn;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by Chris Cavazos on 10/14/2017.
 */
public class BreakAnim extends Anim {

    private Ngon shape;
    private ArrayList<Triangle> split;

    public BreakAnim(Ngon ngon) {
        this.shape = ngon;
        split = Triangle.triangulate(shape);
        color.set((rn.nextInt(190) + 65) / 255f,
                (rn.nextInt(190) + 65) / 255f,
                (rn.nextInt(190) + 65) / 255f,
                1f);
    }

    public void update(float dt) {

        for (int i = 0; i < split.size(); i++) {
            Triangle t = split.get(i);
            Vector2 a = t.getCenter();
            float angle = -a.angleRad(shape.getCenter());
            float r = a.dst(t.a());
            Vector2 pos = new Vector2(a.x + (r * (float) cos(angle)), a.y + (r * (float) sin(angle)));

            split.set(i, Triangle.updatePoints(pos, rn.nextInt(360), r));
        }
        if (run.isDone()) {
            death = true;
            split.clear();
        }
    }

    public void render(ShapeRendererExt sr) {
        sr.setColor(color);

        if (!run.isDone()) {
            for (Triangle t : split) {
                sr.triangle(t);
            }
        }
    }

}

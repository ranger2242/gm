package com.quadx.asteroids.asteroids.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.anims.RechargeAnim;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.shapes1_2.Circle;
import com.quadx.asteroids.shapes1_2.Line;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.tools.Delta;

import java.util.ArrayList;

import static com.quadx.asteroids.states.AsteroidState.player;
import static com.quadx.asteroids.tools.Game.ft;

/**
 * Created by Chris Cavazos on 12/14/2017.
 */
public class Lightning extends Powerup {
    ArrayList<Circle> circles = new ArrayList<>();
    ArrayList<Line> lines = new ArrayList<>();

    float rlim=2*60*ft;
    public Lightning() {
        loadTexture("lightning");
        dtUse=new Delta(rlim);
        dtDeath = new Delta(5 * ft);
        dtrechargeAnim = new RechargeAnim(rlim, new Vector2());
        dtUse.finish();
        dtrechargeAnim.finish();

       name= "LIGHTNING";
        price=2000;

    }

    @Override
    public void use(Ship s) {
        dtrechargeAnim = new RechargeAnim(rlim, new Vector2());

        dtUse.reset();
        dtDeath.reset();
        mark(player.getPos(), 3, 200);
    }

    @Override
    public void update(float dt) {
        dtDeath.update(dt);
        dtUse.update(dt);
        if (dtDeath.isDone()) {
            circles.clear();
            lines.clear();
            dtrechargeAnim.update(dt);
            dtrechargeAnim.setCenter(player.getPos());


        }
    }

    void mark(Vector2 v, int depth, float r) {
        if (depth >= 0)
            for (Asteroid a : AsteroidState.getRocks()) {
                float dst = v.dst(a.getPos());
                if (dst < r  && !a.isMarked()) {
                    a.setMarked(true);
                    circles.add(new Circle(a.getPos(), 10));
                    lines.add(new Line(v, a.getPos()));
                    mark(a.getPos(), depth - 1, r * .75f);
                }
            }
    }

    @Override
    public void render(ShapeRendererExt sr) {
        if (!dtDeath.isDone()) {
            sr.setColor(Color.WHITE);
            for (Circle c : circles) {
                sr.circle(c);
            }
            for (Line l : lines) {
                sr.line(l);
            }
        } else {
            Color c = new Color(1, .1f, 0, 1);
            c.r = 1 - (1 * (dtrechargeAnim.getAngle() / 360));
            c.g = (float) (1 * Math.exp(9 * (dtrechargeAnim.getAngle() / 360) - 9));
            dtrechargeAnim.setColor(c);

            dtrechargeAnim.render(sr);
        }
    }

    @Override
    public boolean collide(Asteroid a) {
        return a.isMarked();
    }

    @Override
    public void upgrade() {

    }
}

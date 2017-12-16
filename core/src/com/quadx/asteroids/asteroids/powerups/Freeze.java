package com.quadx.asteroids.asteroids.powerups;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.shapes1_2.Circle;
import com.quadx.asteroids.shapes1_2.Line;
import com.quadx.asteroids.shapes1_2.Ngon;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.tools.Delta;
import com.quadx.asteroids.tools.Sounds;

import static com.quadx.asteroids.states.AsteroidState.player;
import static com.quadx.asteroids.tools.Game.ft;

public class Freeze extends Powerup {

    Line shape = new Line(new Vector2(), new Vector2());

    public Freeze() {
        dtUse = new Delta(20 * ft);
        dtDeath=new Delta(20*ft);
        index = 6;
        loadTexture("freeze");
        dtDeath.finish();
        dtUse.finish();
        name="SLOW";
        price=50;
    }

    public void update(float dt) {
        dtUpgrade.update(dt);
        dtDeath.update(dt);
        if (!dtDeath.isDone())
            makeLine();
        else {
            dtUse.update(dt);

            shape = new Line();
        }
    }

    void makeLine() {
        Vector2 v = player.getPos();
        shape.a.set(v);
        shape.b.set(Circle.point(v, 500, player.getAngle()));
    }

    public void render(ShapeRendererExt sr) {
        sr.setColor(new Color(.6f, .6f, 1, 1));
        sr.line(shape);
    }


    @Override
    public boolean collide(Asteroid a) {
        Ngon n = a.getShape();

        if (shape.intersects(n)) {
            a.setVel(a.getVel().scl(.98f));
        }
        return false;

    }

    @Override
    public void upgrade() {
    }

    @Override
    public void use(Ship s) {
        Sounds.bleep.play(.8f * Sounds.mainVolume);
        makeLine();
        dtUse.reset();
        dtDeath.reset();
    }

    public static Circle setPos(float dt, Circle shape, Vector2 vel) {
        shape.getCenter().add(vel.x * dt, vel.y * dt);
        return shape;
    }
}

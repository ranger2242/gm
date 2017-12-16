package com.quadx.asteroids.asteroids.powerups;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.shapes1_2.Circle;
import com.quadx.asteroids.shapes1_2.Ngon;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.tools.Delta;
import com.quadx.asteroids.tools.Sounds;

import java.util.ArrayList;

import static com.quadx.asteroids.tools.Game.ft;

public class Bullet extends Powerup {
    private static final ArrayList<Shot> bullets = new ArrayList<>(4);
    private float n =1;

    public Bullet() {
        dtUse=new Delta(10*ft);
        index=0;
        loadTexture("bullet");
        name="BULLET";
        cost=new int[]{100,250,420,700,1000,1500};
        price=cost[level];
    }

    public void update(float dt){
        dtUpgrade.update(dt);
        dtUse.update(dt);
        for(Shot b: bullets){
            b.updateIndividual(dt);
        }
        removeDead();
    }

    public void render(ShapeRendererExt sr) {
        sr.setColor(Color.GREEN);
        for(Shot b: bullets){
            sr.circle((Circle) b.getShape());
        }
    }

    private void removeDead(){
        for (int i = bullets.size() - 1; i >= 0; i--) {
            if (bullets.get(i).isDeath()) {
                bullets.get(i).setDeath(true);
                bullets.remove(i);
            }
        }
    }
    @Override
    public boolean collide(Asteroid a) {
        Ngon n= a.getShape();
        for(Shot b: bullets) {
            for (Triangle t : Triangle.triangulate(n)) {
                if (((Circle)b.getShape()).overlaps(t)) {
                    b.collided();
                    return true;
                }
            }
        }
        return false;

    }

    @Override
    public void upgrade() {
        if(dtUpgrade.isDone()) {
            n++;
            dtUpgrade.reset();
        }    }

    @Override
    public void use(Ship s) {
        Sounds.bleep.play(.8f* Sounds.mainVolume);
        if (bullets.size() < 4*n) {
            for(int i=0;i<n;i++) {
                float offset = 180/(n+1);
                float o= n==1?  0:(i*offset)-(n*offset/2);

                Shot shot=new Shot(s.getPos(), s.getVel(), s.getAngle(),o);
                shot.setType(getClass());
                bullets.add(shot);
            }
        }
        dtUse.reset();
    }

    @Override
    public int getPrice() {
        return cost[level];
    }

    public static Circle setPos(float dt, Circle shape, Vector2 vel){
        shape.getCenter().add(vel.x * dt, vel.y * dt);
        return shape;
    }
}

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

/**
 * Created by Chris Cavazos on 12/10/2017.
 */
public class Mine extends Powerup {
    private static final ArrayList<Shot> bullets = new ArrayList<>(4);

    public Mine() {
        dtUse = new Delta(60 * ft);
        index = 3;
        loadTexture("mine");
        name="MINE";
        price=700;
    }

    @Override
    public void use(Ship s) {
        Sounds.bleep.play(.8f * Sounds.mainVolume);

        float r=20;
        Vector2 v= new Vector2(s.getPos());
        float a= (float) Math.toRadians(s.getAngle()+180);
        v.x+=r*Math.cos(a);
        v.y+=r*Math.sin(a);

        Shot shot = new Shot(v);
        shot.setType(getClass());
        if(bullets.size()<20) {
            bullets.add(shot);
            System.out.println(bullets.size());
        }

        dtUse.reset();
    }

    @Override
    public void update(float dt) {
        dtUse.update(dt);
        dtrechargeAnim.update(dt);
        for (Shot b : bullets) {
            if(!b.isDeath())
            b.updateIndividual(dt);
        }
        removeDead();

    }

    @Override
    public void render(ShapeRendererExt sr) {
        sr.setColor(Color.GREEN);
        try {
            for (Shot b : bullets) {
                if(b.getShape()!=null)
                sr.circle((Circle) b.getShape());
            }
        }catch (ArrayIndexOutOfBoundsException e){}
    }

    private void removeDead() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            if (bullets.get(i).isDeath()) {
                bullets.get(i).setDeath(true);
                bullets.remove(i);
            }
        }
        while (bullets.size()>20){
            bullets.remove(0);
        }
    }

    @Override
    public boolean collide(Asteroid a) {
        Ngon n= a.getShape();

        for (Shot b : bullets) {
            for (Triangle t : Triangle.triangulate(n)) {
                if(b.getShape()!=null)
                if (((Circle) b.getShape()).overlaps(t)) {
                    b.mineCollide();
                    b=null;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void upgrade() {

    }
}

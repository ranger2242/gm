package com.quadx.asteroids.asteroids.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.anims.RechargeAnim;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.shapes1_2.Line;
import com.quadx.asteroids.shapes1_2.Ngon;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.tools.Delta;

import java.util.ArrayList;

import static com.quadx.asteroids.tools.Game.ft;

/**
 * Created by Chris Cavazos on 12/6/2017.
 */
public class Laser extends Powerup {
    private static final ArrayList<Shot> bullets = new ArrayList<>(4);
    private float dlim = 20 * ft;
    private float r = 200;
    private final Delta dtDeath = new Delta(dlim);
    private final Vector2 pos = new Vector2();
    private int n=1;

    public Laser() {
        lim = 30 * ft;
        dtUse.finish();
        dtUse = new Delta(dlim);
        index = 1;
        loadTexture("laser");
    }


    @Override
    public void use(Ship s) {
        if (bullets.size() < 4 * n) {
            for (int i = 0; i < n; i++) {
                float offset = 180 / (n + 1);
                float o= n==1?  0:(i*offset)-(n*offset/2);
                Shot shot=new Shot(s.getPos(), s.getAngle(), o, r);
                shot.setType(getClass());
                bullets.add(shot);
            }
        }

        Vector2 a = new Vector2(s.getShape().getPoints()[0], (s.getShape().getPoints()[1]));
        dtrechargeAnim = new RechargeAnim(dlim, a);
        pos.set(a);

        dtUse.reset();
    }

    public static Line setPos(float dt, Line l, float ang,float r) {
        Vector2 a = l.a;
        Vector2 b = l.b;
        float r1 = (r * dt * 10);
        float dx1 = a.x + r1 * (float) Math.cos(Math.toRadians(ang));
        float dy1 = a.y + r1 * (float) Math.sin(Math.toRadians(ang));
        float dx2 = b.x + r1 * (float) Math.cos(Math.toRadians(ang));
        float dy2 = b.y + r1 * (float) Math.sin(Math.toRadians(ang));
        Vector2 a1 = new Vector2(dx1, dy1);
        Vector2 b1 = new Vector2(dx2, dy2);
        return new Line(a1, b1);
    }

    @Override
    public void update(float dt) {
        dtrechargeAnim.setCenter(AsteroidState.player.getPos());
        dtrechargeAnim.update(dt);
        dtDeath.update(dt);
        dtUse.update(dt);
        dtUpgrade.update(dt);

        if (dtDeath.isDone()) {
            dtDeath.reset();
        } else {
            for(Shot b: bullets){
                b.updateIndividual(dt);
            }
        }
        Color c = new Color(1,.1f,0,1);
        c.r=1-(1*(dtrechargeAnim.getAngle()/360));
        c.g= (float) (1*Math.exp(9*(dtrechargeAnim.getAngle()/360)-9));
        dtrechargeAnim.setColor(c);
        removeDead();
    }

    @Override
    public void render(ShapeRendererExt sr) {
        dtrechargeAnim.render(sr);
        sr.setColor(Color.GREEN);
        for(Shot l: bullets) {
            Line a= (Line)l.getShape();
            sr.line(a);
        }
    }


    public boolean collide(Ngon n) {
        for (Triangle t : Triangle.triangulate(n)) {
            for (Line a : Line.asLines(t)) {
                for(Shot b: bullets) {
                    if (Line.intersectsLine(a,(Line) b.getShape()))
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
        }
    }

    public void removeDead(){
        for (int i = bullets.size() - 1; i >= 0; i--) {
            if (bullets.get(i).isDeath()) {
                bullets.remove(i);
            }
        }
    }
}

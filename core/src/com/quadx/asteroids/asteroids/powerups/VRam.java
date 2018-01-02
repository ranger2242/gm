package com.quadx.asteroids.asteroids.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.anims.RechargeAnim;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.shapes1_2.Line;
import com.quadx.asteroids.shapes1_2.Ngon;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.tools.Delta;

import static com.quadx.asteroids.states.AsteroidState.player;
import static com.quadx.asteroids.tools.Game.ft;

/**
 * Created by Chris Cavazos on 12/13/2017.
 */
public class VRam extends Powerup {

    Vector2 a = new Vector2();
    Vector2 b = new Vector2();
    Vector2 c = new Vector2();
    Line l1 = new Line();
    Line l2 = new Line();
    float rlim=5*60*ft;
    float ulim= 90*ft;
    RechargeAnim rech;

    public VRam() {
        loadTexture("vram");
        rech= new RechargeAnim(ulim,new Vector2());
        index=4;
        dtUse = new Delta(rlim);
        dtDeath = new Delta( ulim);
        dtUse.finish();
        dtDeath.finish();
        name="VRAM";
        price=1000;
    }

    @Override
    public void use(Ship s) {
        rech= new RechargeAnim(ulim,new Vector2());

        dtrechargeAnim = new RechargeAnim(rlim, new Vector2());
        dtDeath.reset();
        dtUse.reset();


    }

    @Override
    public void update(float dt) {
        dtUse.update(dt);
        dtDeath.update(dt);
        a = new Vector2(player.getPos());
        rech.update(dt);
        rech.setCenter(a);
        if (!dtDeath.isDone()) {
            float r1 = 50;
            float r2 = 150;

            float sep = 30;
            float ang = (float) Math.toRadians(player.getAngle());
            a.x = (float) (a.x + (r1 * Math.cos(ang)));
            a.y = (float) (a.y + (r1 * Math.sin(ang)));
            b.x = (float) (a.x + (r2 * Math.cos(ang + Math.toRadians(180 - sep))));
            b.y = (float) (a.y + (r2 * Math.sin(ang + Math.toRadians(180 - sep))));
            c.x = (float) (a.x + (r2 * Math.cos(ang + Math.toRadians(180 + sep))));
            c.y = (float) (a.y + (r2 * Math.sin(ang + Math.toRadians(180 + sep))));
            l1 = new Line(a, b);
            l2 = new Line(a, c);
        }else{
            dtrechargeAnim.update(dt);
            dtrechargeAnim.setCenter(player.getPos());
            l1=new Line();
            l2=new Line();
        }
        Color c2= new Color(.1f,.1f,1f,1f);
        //c2.b=1-(1*(rech.getAngle()/360));
        c2.g= rech.getAngle()/360;
        c2.a= c2.g;
        rech.setColor(c2);

        Color c = new Color(1,.1f,0,1);
        c.r=1-(1*(dtrechargeAnim.getAngle()/360));
        c.g= (float) (1*Math.exp(9*(dtrechargeAnim.getAngle()/360)-9));
        dtrechargeAnim.setColor(c);
    }

    @Override
    public void render(ShapeRendererExt sr) {
        sr.setColor(Color.GREEN);
        if (!dtDeath.isDone()) {
            rech.render(sr);
            sr.line(l1);
            sr.line(l2);
        }else {
            dtrechargeAnim.render(sr);
        }
    }

    @Override
    public boolean collide(Asteroid a) {
        Ngon n= a.getShape();
        for (Triangle t : Triangle.triangulate(n)) {
            for (Line l : Line.asLines(t)) {
                if (l.intersects(l1) ||l.intersects(l2)) {
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

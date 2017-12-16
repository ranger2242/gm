package com.quadx.asteroids.asteroids.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.anims.RechargeAnim;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.shapes1_2.*;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.tools.Delta;

import java.util.ArrayList;

import static com.quadx.asteroids.states.AsteroidState.player;
import static com.quadx.asteroids.states.AsteroidState.rn;
import static com.quadx.asteroids.tools.Game.*;

/**
 * Created by Chris Cavazos on 12/14/2017.
 */
public class Warp extends Powerup {
    ArrayList<Line> lines = new ArrayList<>();
    ArrayList<Triangle> tris = new ArrayList<>();
    float rlim=2*60*ft;

    public Warp() {
        dtrechargeAnim=new RechargeAnim(rlim,new Vector2(-999,-999));
        dtUse=new Delta(rlim);
        dtDeath = new Delta(60*ft);
        loadTexture("warp");
        dtDeath.finish();
        dtUse.finish();
        dtrechargeAnim.finish();
        index=5;
        name="WARP";
        price=100;

    }

    @Override
    public void use(Ship s) {
        dtrechargeAnim=new RechargeAnim(rlim,player.getPos());

        lines.clear();
        tris.clear();
        ArrayList<Asteroid> rocks = AsteroidState.getRocks();
        for (Asteroid a : rocks) {
            for (Asteroid a2 : rocks) {
                float d = a.getPos().dst(a2.getPos());
                if (d < 400)
                    lines.add(new Line(a.getPos(), a2.getPos()));
            }
        }
        for (Line l : lines) {
            for (Line l2 : lines) {
                if (lines.indexOf(l) != lines.indexOf(l2)) {
                    if (l.a.equals(l2.a)) {
                        tris.add(new Triangle(l.a, l.b, l2.b));
                    }
                    if (l.a.equals(l2.b)) {
                        tris.add(new Triangle(l.a, l.b, l2.a));
                    }
                    if (l.b.equals(l2.a)) {
                        tris.add(new Triangle(l.a, l.b, l2.b));
                    }
                    if (l.b.equals(l2.b)) {
                        tris.add(new Triangle(l.a, l.b, l2.a));
                    }

                }
            }
        }
        Vector2 np = new Vector2(rn.nextInt(WIDTH),rn.nextInt(HEIGHT));
        float r=200;
        Circle c = new Circle(np,r);
        for(Triangle t : tris){
            if(c.overlaps(t)){
                np = new Vector2(rn.nextInt(WIDTH),rn.nextInt(HEIGHT));
                c = new Circle(np,r);
            }
        }
        player.setPos(np);
        dtDeath.reset();
    dtUse.reset();
    }

    @Override
    public void update(float dt) {
        dtUse.update(dt);
        dtDeath.update(dt);
        if(!dtDeath.isDone()){
            player.setImmune(true);
        }else{
            player.setImmune(false);
            Color c = new Color(1,.1f,0,1);
            c.r=1-(1*(dtrechargeAnim.getAngle()/360));
            c.g= (float) (1*Math.exp(9*(dtrechargeAnim.getAngle()/360)-9));
            dtrechargeAnim.setColor(c);

            dtrechargeAnim.update(dt);
            dtrechargeAnim.setCenter(player.getPos());
        }

    }


    @Override
    public void render(ShapeRendererExt sr) {
/*        sr.setColor(Color.WHITE);
        for (Line l : lines) {
            sr.line(l);
        }
        sr.setColor(Color.GREEN);
        for (Triangle t : tris) {
            sr.triangle(t);
        }*/

        dtrechargeAnim.render(sr);

    }

    @Override
    public boolean collide(Asteroid a) {
        return false;
    }

    @Override
    public void upgrade() {

    }
}

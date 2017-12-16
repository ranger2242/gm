package com.quadx.asteroids.asteroids.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.anims.RechargeAnim;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.shapes1_2.*;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.tools.Delta;

import static com.quadx.asteroids.tools.Game.ft;

/**
 * Created by Chris Cavazos on 12/9/2017.
 */
public class CBomb extends Powerup {
    private Circle shape= new Circle();

    public CBomb(){
        lim=300*ft;
        dtUse=new Delta(lim);
        dtDeath=new Delta(40*ft);
        dtUse.finish();
        dtDeath.finish();
        index=2;
        loadTexture("cbomb");
        name="CBOMB";
        price=1250;
    }
    @Override
    public void use(Ship s) {
        dtrechargeAnim = new RechargeAnim(lim, s.getPos());
        shape=new Circle(s.getPos(),0);
        dtUse.reset();
        dtDeath.reset();
    }

    @Override
    public void update(float dt) {
        Vector2 p= AsteroidState.player.getPos();

        dtDeath.update(dt);
        dtUse.update(dt);
        if (dtDeath.isDone()) {
            shape=new Circle(p,0);
            dtrechargeAnim.setCenter(p);
            dtrechargeAnim.update(dt);
            Color c = new Color(1, .1f, 0, 1);
            c.r = 1 - (1 * (dtrechargeAnim.getAngle() / 360));
            c.g = (float) (1 * Math.exp(9 * (dtrechargeAnim.getAngle() / 360) - 9));
            dtrechargeAnim.setColor(c);
        } else {
                shape.center.set(p);
                shape.radius+=300*dt;
        }
    }

    @Override
    public void render(ShapeRendererExt sr) {
        dtrechargeAnim.render(sr);

        sr.setColor(Color.GREEN);
        sr.circle(shape);
    }

    @Override
    public boolean collide(Asteroid a) {
        Ngon n= a.getShape();

        for(Triangle t: Triangle.triangulate(n)){
            if(shape.overlaps(t)){
                    return true;
            }
        }
        return false;
    }

    @Override
    public void upgrade() {

    }
}

package com.quadx.asteroids.anims;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.shapes1_2.Arc;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.tools.Delta;

/**
 * Created by Chris Cavazos on 12/7/2017.
 */
public class RechargeAnim extends Anim {
    private final Vector2 pos=new Vector2();
    private float dtPassed=0;
    private final Arc arc= new Arc(pos,50,90,0,20,1);

    public RechargeAnim(float lim, Vector2 p){
        run =new Delta(lim);
        this.pos.set(p);
        this.lim=lim;
    }

    @Override
    public void update(float dt) {
        dtPassed+=dt;
        run.update(dt);
    }

    @Override
    public void render(ShapeRendererExt sr) {
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.setColor(color);
        if (!run.isDone()) {
            float deg = (360 * (dtPassed / lim));
            arc.setAngle(deg);
            arc.render(sr);

        }
    }

    public void setCenter(Vector2 center) {
        this.arc.setPos(center);
    }

    public void setWidth(float width) {
        this.arc.setW(width);
    }

    public void setRadius(float r) {
        this.arc.setRadius(r);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getAngle() {
        return arc.getAngle();
    }

    public void finish() {
        dtPassed=Float.MAX_VALUE;
    }
}

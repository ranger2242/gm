package com.quadx.asteroids.anims;

import com.badlogic.gdx.graphics.Color;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.tools.Delta;

import static com.quadx.asteroids.tools.Game.ft;

/**
 * Created by Chris Cavazos on 12/7/2017.
 */
@SuppressWarnings("WeakerAccess")
public abstract class Anim {
    protected boolean death = false;
    protected float lim=0;

    protected Delta run = new Delta(30 * ft);
    protected Color color= new Color();
    public abstract void update(float dt);
    public abstract void render(ShapeRendererExt sr);
    public boolean isDead(){
        return death;
    }

}

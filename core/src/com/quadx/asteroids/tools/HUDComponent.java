package com.quadx.asteroids.tools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Chris on 12/4/2017.
 */

abstract class HUDComponent {
    abstract void update(float dt);
    abstract Actor getActor();
    public abstract void render(SpriteBatch sb);
}

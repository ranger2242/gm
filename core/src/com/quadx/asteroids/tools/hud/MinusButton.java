package com.quadx.asteroids.tools.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Chris Cavazos on 12/15/2017.
 */
public class MinusButton extends ImButton{
    public MinusButton(){
        create("minus");
    }
    @Override
    void update(float dt) {

    }

    @Override
    Actor getActor() {
        return button;
    }

    @Override
    public void render(SpriteBatch sb) {

    }
}

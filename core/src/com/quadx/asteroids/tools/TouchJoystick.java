package com.quadx.asteroids.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

/**
 * Created by Chris on 12/2/2017.
 */


public class TouchJoystick extends HUDComponent {
    private Touchpad touchpad;

    private final Vector2 direction=new Vector2();

    public TouchJoystick(){
        init();
    }

    private void init(){
        //Create a touchpad skin
        Skin touchpadSkin = new Skin();
        //Set background image
        touchpadSkin.add("touchBackground", new Texture("hud/joystick/touchBackground.png"));
        //Set knob image
        touchpadSkin.add("touchKnob", new Texture("hud/joystick/touchKnob.png"));
        //Create TouchPad Style
        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        //Create Drawable's from TouchPad skin
        Drawable touchBackground = touchpadSkin.getDrawable("touchBackground");
        Drawable touchKnob = touchpadSkin.getDrawable("touchKnob");
        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        touchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        touchpad.setBounds(400, 200, 500, 500);

    }
    public Vector2 getDirection(){
        return direction;
    }


    public Actor getActor() {
        return touchpad;
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    public void update(float dt) {
        direction.set(touchpad.getKnobPercentX(),touchpad.getKnobPercentY());
    }

}

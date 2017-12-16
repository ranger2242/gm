package com.quadx.asteroids.tools.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Chris Cavazos on 12/15/2017.
 */
public abstract class ImButton extends HUDComponent {
    protected Texture myTexture;
    protected TextureRegion myTextureRegion;
    protected TextureRegionDrawable myTexRegionDrawable;
    protected ImageButton button;

    @Override
    abstract void update(float dt);
    public void create(String s){
        myTexture = new Texture(Gdx.files.internal("hud/button/shop/"+s+".png"));
        myTextureRegion = new TextureRegion(myTexture);
        myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        button = new ImageButton(myTexRegionDrawable); //Set the button up

    }

    @Override
    public void setPos(Vector2 v) {
        button.setPosition(v.x,v.y);

    }
}

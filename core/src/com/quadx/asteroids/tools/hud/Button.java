package com.quadx.asteroids.tools.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Button extends HUDComponent {
    private TextButton button;

    public Button(String texture, Vector2 pos, Vector2 size){
        BitmapFont font = new BitmapFont();
        Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas(Gdx.files.internal("hud/button/" + texture));
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle.checked = skin.getDrawable("checked-button");
        button = new TextButton("Button1", textButtonStyle);
        button.setPosition(pos.x,pos.y);
        button.setSize(size.x,size.y);
    }


    public void update (float dt) {
    }
    public void render(SpriteBatch sb){

    }

    @Override
    public void setPos(Vector2 v) {

    }

    public boolean isPressed() {
        return button.isPressed();
    }

    public Actor getActor() {
        return button;
    }

}
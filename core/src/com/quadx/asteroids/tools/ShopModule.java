package com.quadx.asteroids.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.quadx.asteroids.command.Command;

import static com.quadx.asteroids.tools.Game.scl;

/**
 * Created by Chris on 12/4/2017.
 */

public class ShopModule extends HUDComponent {
    private com.badlogic.gdx.scenes.scene2d.ui.Button button;
    private TextureAtlas buttonAtlas;
    private Texture icon;
    private TextButton.TextButtonStyle textButtonStyle;
    private Skin skin;
    private String text;
    private int price;
    private Vector2 pos = new Vector2();
    private final float inset=5* scl.dst(new Vector2());

    public ShopModule(String s, int p, Vector2 pos) {
        Command.cls=this.getClass();
        text = s;
        price = p;
        this.pos.set(pos);


        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("hud/button/shop/shopModule.pack"));
        icon=new Texture(Gdx.files.internal("hud/button/shop/"+text.toLowerCase()+".png"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = Fonts.getFont();
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle.checked = skin.getDrawable("checked-button");
        button = new TextButton(s, textButtonStyle);

        button.setPosition(pos.x, pos.y);
    }

    @Override
    public void update(float dt) {

    }

    public void dispose() {
        button = null;
        buttonAtlas = null;
        textButtonStyle = null;
        skin = null;
        text = null;
        price = 0;
        pos = null;
        icon=null;
    }

    @Override
    public Actor getActor() {
        return button;
    }

    public void render(SpriteBatch sb) {
        sb.draw(icon,pos.x+inset,pos.y+inset);
    }

    public boolean getPressed() {
        return button.isChecked();
    }

    public void setPressed(boolean pressed) {
        this.button.setChecked(pressed);
    }
}

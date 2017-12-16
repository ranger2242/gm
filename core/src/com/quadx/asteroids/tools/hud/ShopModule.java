package com.quadx.asteroids.tools.hud;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Chris on 12/4/2017.
 */

public class ShopModule extends HUDComponent {
    PlusButton p = new PlusButton();
    MinusButton m = new MinusButton();
    BuyButton b = new BuyButton();
    UpgradeButton u= new UpgradeButton();

    public ShopModule(Vector2 v) {

        p.setPos(new Vector2(v));
        m.setPos(new Vector2(v.x+50,v.y));
        b.setPos(new Vector2(v.x+100,v.y));
        u.setPos(new Vector2(v.x+150,v.y));
    }

    @Override
    public void update(float dt) {

    }

    public void dispose() {

    }

    @Override
    public Actor getActor() {
        return null;
    }

    public void render(SpriteBatch sb) {
    }

    @Override
    public void setPos(Vector2 v) {

    }

    public Actor[] getActors() {
        return new Actor[]{p.getActor(),m.getActor(),b.getActor(),u.getActor()};
    }
}

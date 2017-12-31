package com.quadx.asteroids.tools.hud;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.quadx.asteroids.asteroids.powerups.Powerup;
import com.quadx.asteroids.shapes1_2.Line;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.states.ShopState;
import com.quadx.asteroids.tools.Fonts;
import com.quadx.asteroids.tools.HoverText;

import java.util.ArrayList;
import java.util.Arrays;

import static com.quadx.asteroids.tools.Game.HEIGHT;
import static com.quadx.asteroids.tools.Game.WIDTH;
import static com.quadx.asteroids.tools.Game.ft;

/**
 * Created by Chris on 12/4/2017.
 */

public class ShopModule extends HUDComponent {
    PlusButton p = new PlusButton();
    MinusButton m = new MinusButton();
    BuyButton b = new BuyButton();
    UpgradeButton u = new UpgradeButton();
    boolean[] flag = new boolean[]{true, false, false, false};
    Line baseLine;
    Powerup powerup;
    Vector2 pos = new Vector2();

    public ShopModule(Vector2 v, Powerup pow) {
        pos.set(v);
        powerup = pow;
        Vector2 bshift = new Vector2(-(WIDTH * .15f), +(HEIGHT * .02f));
        Vector2 a = new Vector2(v.x + bshift.x, v.y + bshift.y);

        baseLine = new Line(v, new Vector2(v.x - (WIDTH * .15f), v.y));

        b.setPos(a);
        p.setPos(new Vector2(a.x + 50, a.y));
        m.setPos(new Vector2(a.x + 50, a.y));
        u.setPos(new Vector2(a.x + 100, a.y));
    }

    @Override
    public void update(float dt) {
        if (((Button) b.getActor()).isPressed()) {
            buy();
        }
        if (((Button) p.getActor()).isPressed()) {
            add();
        }
        if (((Button) m.getActor()).isPressed()) {
            sub();
        }
        if (((Button) u.getActor()).isPressed()) {
            upgrade();
        }
    }

    private void upgrade() {

        Powerup p = AsteroidState.player.getPowerup(powerup.getClass());
        if (AsteroidState.player.canBuy(p.getPrice())) {
            ShopState.refactorButtons();
            AsteroidState.player.addMoney(-p.getPrice());
            new HoverText(-p.getPrice() + "", 120 * ft, Color.RED, WIDTH * .2f, HEIGHT * .90f, true);
            AsteroidState.player.upgrade(p);

        }
    }

    private void add() {
        AsteroidState.player.addActive(powerup);
        enableAdd(false);
        enableSub(true);
        ShopState.refactorButtons();

    }

    void sub() {
        AsteroidState.player.removeActive(powerup);
        enableAdd(true);
        enableSub(false);
        ShopState.refactorButtons();

    }

    public void dispose() {

    }

    void buy() {
        if (AsteroidState.player.canBuy(powerup.getPrice())) {


            enableBuy(false);
            AsteroidState.player.buyPowerup(powerup);
            enableAdd(true);
            ShopState.refactorButtons();
            AsteroidState.player.addMoney(-powerup.getPrice());
            new HoverText(-powerup.getPrice() + "", 120 * ft, Color.RED, WIDTH * .2f, HEIGHT * .90f, true);
        }
    }

    @Override
    public Actor getActor() {
        return null;
    }

    public void render(SpriteBatch sb) {

        Fonts.getFont().setColor(Color.WHITE);
        Fonts.resetAlpha();
        Powerup p = AsteroidState.player.getPowerup(powerup.getClass());

        String s = (!(p == null) && p.isUpgradable()) ? (p.getLevel() + 1) + "" : "";
        Fonts.getFont().draw(sb, powerup.getName() + " " + s, pos.x - (WIDTH * .15f), pos.y);
        Fonts.getFont().setColor(Color.RED);
        Fonts.resetAlpha();

        if (AsteroidState.player.owns(powerup.getClass())) {
            if (AsteroidState.player.getPowerup(powerup.getClass()).isUpgradable()) {
                if(AsteroidState.player.canBuy(p.getPrice()))
                    Fonts.getFont().setColor(Color.WHITE);
                Fonts.getFont().draw(sb, "$" + AsteroidState.player.getPowerup(powerup.getClass()).getPrice(), pos.x, pos.y);
            }
        } else {
            if(AsteroidState.player.canBuy(powerup.getPrice()))
                Fonts.getFont().setColor(Color.WHITE);
            Fonts.getFont().draw(sb, "$" + powerup.getPrice(), pos.x, pos.y);

        }
        sb.draw(powerup.getIcon(), baseLine.a.x, pos.y);

    }

    public void renderSR(ShapeRendererExt sr) {
        sr.setColor(Color.BLUE);
        sr.line(baseLine);
    }

    @Override
    public void setPos(Vector2 v) {

    }

    public Actor[] getActors() {
        ArrayList<Actor> list = new ArrayList<>();
        if (flag[0]) {
            list.add(b.getActor());
        }
        if (flag[1]) {
            list.add(p.getActor());
        }
        if (flag[2]) {
            list.add(u.getActor());
        }
        if (flag[3]) {
            list.add(m.getActor());
        }
        return Arrays.copyOf(list.toArray(), list.size(), Actor[].class);
    }

    public void enableBuy(boolean b) {
        flag[0] = b;
    }

    public void enableAdd(boolean b) {
        flag[1] = b;

    }

    public void enableUpgrade(boolean b) {
        flag[2] = b;
    }

    public void enableSub(boolean b) {
        flag[3] = b;
    }
}

package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quadx.asteroids.asteroids.powerups.*;
import com.quadx.asteroids.command.Command;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.tools.Fonts;
import com.quadx.asteroids.tools.Game;
import com.quadx.asteroids.tools.HoverText;
import com.quadx.asteroids.tools.hud.ShopModule;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static com.quadx.asteroids.states.AsteroidState.player;
import static com.quadx.asteroids.tools.Game.*;
import static com.quadx.asteroids.tools.Text.strWidth;

/**
 * Created by Chris on 12/3/2017.
 */

public class ShopState extends State {
    private static final ArrayList<ShopModule> modules = new ArrayList<>();
    private final ShapeRendererExt sr = new ShapeRendererExt();
    private float edge;
    private static final ArrayList<Powerup> powerups = new ArrayList<>();
    private ArrayList<Powerup> pl = new ArrayList<>();
    Texture base = new Texture("hud/button/shop/base.png");
    private static Stage stage = null;

    public ShopState(GameStateManager gsm) {
        super(gsm);
        powerups.clear();
        edge = 25 * sclf;
        sr.setAutoShapeType(true);
        powerups.add(new Bullet());
        powerups.add(new Laser());
        powerups.add(new Lightning());
        powerups.add(new VRam());
        powerups.add(new Warp());
        powerups.add(new Freeze());
        powerups.add(new Shield());
        powerups.add(new CBomb());
        powerups.add(new Mine());
        ScreenViewport viewport = new ScreenViewport();

        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);
        refactorButtons();

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.F12)) {
            player.addMoney(5);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
            stage.clear();
            AsteroidState.nextWave();
            gsm.pop();
            Command.cls = gsm.peek().getClass();
        }
    }

    private float[] fitLineToWord(String s) {
        //x1,y1,x2,y2
        float[] arr = new float[8];
        arr[0] = -10;
        arr[1] = -15;
        arr[2] = strWidth(s) + 20;
        arr[3] = -15;
        arr[4] = -10;
        arr[5] = -12;
        arr[6] = strWidth(s) + 35;
        arr[7] = -12;
        return arr;
    }

    private void titleLine(String st, Vector2 t) {
        float[] s = fitLineToWord(st);
        float x = t.x;
        float y = t.y;
        sr.line(x + s[0], y + s[1], x + s[2], y + s[3]);
        sr.line(x + s[4], y + s[5], x + s[6], y + s[7]);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void update(float dt) {
        handleInput();
        for (int i = 0; i < modules.size(); i++)
            modules.get(i).update(dt);
        try {

            for (HoverText h : HoverText.texts) {
                h.update();
            }
        } catch (ConcurrentModificationException ex) {

        }
    }

    //////////////////////////////////////////
    //render
    //////////////////////////////////////////
    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


        sr.begin(ShapeRenderer.ShapeType.Filled);
        drawBackground(sr);


        sr.set(ShapeRenderer.ShapeType.Line);

        Vector2 t = new Vector2(WIDTH * .6f, HEIGHT * .95f);
        Fonts.setFontSize(6);
        sr.setColor(Color.BLUE);
        titleLine("SHOP", t);
        titleLine("EQUIPMENT", new Vector2(WIDTH * .05f, t.y));

        sr.line(new Vector2(WIDTH * .3f, HEIGHT * .1f), new Vector2(WIDTH * .3f, HEIGHT * .9f));

        for (ShopModule m : modules) {
            m.renderSR(sr);
        }
        sr.end();
        Gdx.gl.glDisable(GL_BLEND);
        sb.begin();
        Fonts.getFont().setColor(Color.WHITE);
        Fonts.resetAlpha();
        Fonts.getFont().draw(sb, "SHOP", t.x, t.y);
        Fonts.getFont().draw(sb, "EQUIPMENT", WIDTH * .05f, t.y);
        Fonts.getFont().draw(sb, "MONEY: " + player.getMoney(), WIDTH * .05f, t.y - 50);
        Fonts.getFont().draw(sb,"EXIT:TAB",WIDTH*.05f,HEIGHT*.075f);


        Fonts.setFontSize(4);

        for (ShopModule m : modules) {
            m.render(sb);
        }
        try {
            for (HoverText h : HoverText.texts) {
                h.render(sb);
            }
        } catch (ConcurrentModificationException ex) {

        }
        drawPlayerSlots(sb);
        sb.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    private void drawPlayerSlots(SpriteBatch sb) {
        Fonts.getFont().setColor(Color.WHITE);
        Fonts.resetAlpha();
        Fonts.setFontSize(4);
        for (int i = 0; i < 4; i++) {
            Vector2 pos = new Vector2(WIDTH * .05f, HEIGHT * (.7f - (i * .2f)));
            ArrayList<Powerup> ups = player.getActivePowerups();
            if (i < ups.size()) {
                Texture t = ups.get(i).getIcon();
                sb.draw(t, pos.x, pos.y);
                Fonts.getFont().draw(sb, ups.get(i).getName() + " L:" + (ups.get(i).getLevel() + 1), pos.x + (WIDTH * .1f), pos.y + (HEIGHT * .05f));
            } else
                sb.draw(base, pos.x, pos.y);


        }
    }

    void drawBackground(ShapeRendererExt sr) {
        float scl2 = 1.4f;
        sr.setColor(.01f, .01f, .1f, .1f);
        sr.rect(edge * Game.scl.x, edge * Game.scl.y, WIDTH - (2 * edge * Game.scl.x), HEIGHT - (2 * edge * Game.scl.y));
        sr.setColor(.01f, .01f, .1f, .04f);
        sr.rect((edge * scl2) * Game.scl.x, (edge * scl2) * Game.scl.y, WIDTH - (2 * (edge * scl2) * Game.scl.x), HEIGHT - (2 * (edge * scl2) * Game.scl.y));
        sr.setColor(.1f, .1f, .4f, .2f);
        sr.rect(edge * Game.scl.x, edge * Game.scl.y, WIDTH - (2 * edge * Game.scl.x), HEIGHT - (2 * edge * Game.scl.y));
        sr.rect((edge * scl2) * Game.scl.x, (edge * scl2) * Game.scl.y, WIDTH - (2 * (edge * scl2) * Game.scl.x), HEIGHT - (2 * (edge * scl2) * Game.scl.y));

    }


    public static void refactorButtons() {
        modules.clear();
        stage.clear();
        ArrayList<Powerup> owned = player.getPowerups();

        float n = powerups.size();
        for (int i = 0; i < n; i++) {
            float b = (int) (n / 2) + 1;
            float r = i < b ? .5f : .8f;
            Vector2 v = new Vector2(WIDTH * r, HEIGHT * (.8f - ((i % b) * .18f)) - (HEIGHT * .01f));
            ShopModule m = new ShopModule(v, powerups.get(i));
            for (Powerup p : owned) {
                if (p.getClass().equals(powerups.get(i).getClass())) {
                    m.enableBuy(false);
                    m.enableAdd(true);
                    if (p.isUpgradable())
                        m.enableUpgrade(true);
                    else
                        m.enableUpgrade(false);

                }
            }
            for (Powerup p : player.getActivePowerups()) {
                if (p.getClass().equals(powerups.get(i).getClass())) {
                    m.enableAdd(false);
                    m.enableSub(true);

                }
            }
            if (player.getActivePowerups().size() == 4)
                m.enableAdd(false);
            for (Actor a : m.getActors()) {
                stage.addActor(a);
            }
            modules.add(m);
        }
    }
}

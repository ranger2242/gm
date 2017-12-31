package com.quadx.asteroids.tools;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.asteroids.Wave;
import com.quadx.asteroids.command.*;
import com.quadx.asteroids.states.GameStateManager;
import com.quadx.asteroids.states.MenuState;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {
    public static SpriteBatch batch;
    public static GameStateManager gsm;
    public static int HEIGHT = 900;
    public static int WIDTH = 1800;
    public final static float ft = 1f / 60f;
    public static final ArrayList<Command> queue = new ArrayList<>();
    public static Mode mode = Mode.DESKTOP;
    public static float sclf=0;
    public static Vector2 scl = new Vector2(1, 1);
    public static final Vector2 res = new Vector2();
    public static final Vector2 bres= new Vector2(1920,1080);
    public static ArrayList<Wave> waves = new ArrayList<>();

    public static void setMode(Mode mode) {
        Game.mode = mode;
    }




    public static Vector2 wrap(Vector2 pos) {
        float x=pos.x %= WIDTH + 1;
        float y=pos.y %= HEIGHT+1;
        return new Vector2((x < 0) ? x + WIDTH : x,(y < 0) ? y + HEIGHT : y);
    }
    public static Vector2 relPos(Vector2 v) {
        return new Vector2(v.scl(scl));
    }

    public enum Mode {
        ANDROID, IOS, DESKTOP
    }

    @Override
    public void create() {
        waves=FileHandler.initFile();
        //LevelGenerator.gen2();
        Sounds.load();
        Fonts.initFonts();
        Gdx.graphics.setTitle("TicTacToe");
        batch = new SpriteBatch();
        gsm = new GameStateManager();
        //gsm.push(new GameState());
        //gsm.push(new AsteroidState(gsm));
        gsm.push(new MenuState(gsm));

        queue.add(new UpComm());
        queue.add(new DownComm());
        queue.add(new LeftComm());
        queue.add(new RightComm());
        queue.add(new ShootComm());
        queue.add(new SwapComm());
        queue.add(new UpgradeComm());
        queue.add(new JoystickMoveComm());


    }

    @Override
    public void render() {
        Command.cls=gsm.peek().getClass();
        gsm.update(Gdx.graphics.getDeltaTime());
        //hud.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
        //hud.render(batch);

    }

    @Override
    public void dispose() {
        batch.dispose();

    }


    public static void setScreenRes(Vector2 v) {
        WIDTH = (int) v.x;
        HEIGHT = (int) v.y;
        res.set(v);
        scl =new Vector2(res.x/bres.x,res.y/bres.y);
        sclf=scl.dst(new Vector2());
        Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);
    }

}

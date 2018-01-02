package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.anims.BreakAnim;
import com.quadx.asteroids.anims.RechargeAnim;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Collision;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.asteroids.Wave;
import com.quadx.asteroids.command.Command;
import com.quadx.asteroids.shapes1_2.Circle;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.tools.Delta;
import com.quadx.asteroids.tools.FPSModule;
import com.quadx.asteroids.tools.Game;
import com.quadx.asteroids.tools.Sounds;
import com.quadx.asteroids.tools.hud.HUD;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static com.quadx.asteroids.tools.Game.*;


public class AsteroidState extends State {
    public static final Random rn = new Random();
    public static Ship player;
    private final ShapeRendererExt sr;
    private static final Collision collision = new Collision();
    private static ArrayList<Asteroid> rocks = new ArrayList<>();
    private static Delta dtSpawn;
    private static Delta dtWave;
    private static final ArrayList<BreakAnim> anims = new ArrayList<>();
    private static RechargeAnim waveCounter;
    private static boolean started = false;
    private static boolean gameover = false;
    private static final FPSModule fpsModule = new FPSModule();
    private static GameStateManager gsm;
    public static int level =0;
    public static HUD hud;

    //////////////////////////////////////////
    //init
    //////////////////////////////////////////
    public AsteroidState(GameStateManager gsm) {
        super(gsm);
        this.gsm = gsm;
        sr = new ShapeRendererExt();
        sr.setAutoShapeType(true);
        startGame();

    }

    public static void startGame() {
        Wave w= new Wave(waves.get(level));

        dtSpawn = new Delta(2 * 120 * ft);
        dtWave = new Delta(w.getDelta().getLimit()*ft);
        player = new Ship();
        rocks = w.getRocks();

        waveCounter = new RechargeAnim(dtWave.getLimit(), new Vector2());
        waveCounter.setWidth(1f);
        waveCounter.setRadius(48);
        waveCounter.setColor(Color.WHITE);

        started=true;
        gameover = false;
    }

    public static ArrayList<Asteroid> getRocks() {
        return rocks;
    }

    //////////////////////////////////////////
    //update
    //////////////////////////////////////////
    @Override
    protected void handleInput() {
        input();
    }

    public static void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            gsm.push(new PauseState(gsm));
        }
        if (!started && (
                (mode == Game.Mode.DESKTOP && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) ||
                        (mode == Game.Mode.ANDROID && hud.getMainButton().isPressed()))) {
            started = true;
            gameover = false;
            //connect();
            //socket.emit("start");
            startGame();
            player.setHost(true);

            //socket.emit("syncRocks", gson.toJson(AsteroidState.getRocks()));

        }
        if (Gdx.input.isKeyPressed(Input.Keys.F12)) {
            startGame();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            player.getPowerUp().upgrade();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            gsm.push(new ShopState(gsm));
        }

        for (Command c : Game.queue) {
            c.execute();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateLoop(dt);
    }

    public static void updateLoop(float dt) {
        checkGameOver(gsm);
        updateVars(dt);
        //spawn();
        collisionHandler();
        refactorRockList();
        removeDeadAnims();
        checkRoundEnd();
        fpsModule.update(dt);

    }

    private static void updateVars(float dt) {
        dtSpawn.update(dt);
        dtWave.update(dt);
        waveCounter.update(dt);
        waveCounter.setCenter(player.getPos());
        player.update(dt);
        updateAnims(dt);
        updateRocks(dt);
    }

    private static void updateAnims(float dt) {
        for (BreakAnim a : anims) {
            a.update(dt);
        }
    }

    private static void updateRocks(float dt) {
        for (Asteroid a : rocks) {
            a.update(dt);
        }
    }

    //////////////////////////////////////////
    //render
    //////////////////////////////////////////
    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        drawScene(sb, sr);
    }

    static void drawScene(SpriteBatch sb, ShapeRendererExt sr) {
        sb.begin();
        player.renderSB(sb);
        sb.end();

        sr.begin();
        srBatch(sr);
        sr.end();
        fpsModule.render(sb, sr, new Vector2(WIDTH * .8f, HEIGHT * .8f));

    }

    private static void srBatch(ShapeRendererExt sr) {
        drawPlayer(sr);
        drawRocks(sr);
        drawAnims(sr);
    }

    private static void drawPlayer(ShapeRendererExt sr) {
        if (!player.isGameOver()) {
            waveCounter.render(sr);
            player.render(sr);
        }
    }

    private static void drawRocks(ShapeRendererExt sr) {
        for (Asteroid a : rocks) {
            a.render(sr);
        }
    }

    private static void drawAnims(ShapeRendererExt sr) {
        for (BreakAnim a : anims) {
            a.render(sr);
        }
    }

    //////////////////////////////////////////
    //misc
    //////////////////////////////////////////
    public static boolean inGame() {
        return !gameover && started && player != null;
    }

    public static void setRocks(Asteroid[] r) {
        rocks = new ArrayList<>(Arrays.asList(r));
    }

    private static void removeDeadAnims() {
        for (int i = anims.size() - 1; i >= 0; i--) {
            if (anims.get(i).isDead()) {
                anims.remove(i);
            }
        }
    }

    private static void checkGameOver(GameStateManager gsm) {
        if (player.isGameOver()) {
            Sounds.gameOver.play(1f * Sounds.mainVolume);
            gsm.pop();
        }
    }

    private static void spawn() {
        if (dtSpawn.isDone() && !dtWave.isDone()) {
            AsteroidState.rocks.addAll(Asteroid.generate(2));
            dtSpawn.reset();
        }
    }

    public static void checkRoundEnd() {
        if (dtWave.isDone() && rocks.isEmpty()) {
            dtWave.reset();
            gsm.push(new ShopState(gsm));
        }
    }

    private static void refactorRockList() {
        ArrayList<Asteroid> newRocks = new ArrayList<>();
        for (int y = rocks.size() - 1; y >= 0; y--) {
            if (rocks.get(y).isDead()) {
                newRocks.addAll(Arrays.asList(rocks.get(y).split()));
                rocks.remove(y);
            }
        }
        newRocks.addAll(rocks);
        rocks.clear();
        rocks.addAll(newRocks);
    }

    public static void rockHit(Asteroid a) {
        anims.add(new BreakAnim(a.getShape()));
        player.addScore(150f / a.getSize());
        player.addMoney(4 - a.getSize());
        Sounds.boom.play(.5f * Sounds.mainVolume);
    }

    private static void collisionHandler() {
        for (Asteroid a : rocks) {
            //player collision
            collision.collide(a, player);
            //power collision
            collision.collide(a, player.getPowerUp());

        }
    }


    @Override
    public void dispose() {

    }


    public static boolean isSpawnClear() {
        Circle c = new Circle(new Vector2(WIDTH / 2, HEIGHT / 2), player.getR()*3);
        boolean b = true;
        for (Asteroid a : rocks) {
            for (Triangle t : Triangle.triangulate(a.getShape())) {
                if (c.overlaps(t)) {
                    b = false;
                }
            }
        }
        return b;
    }

    public static void nextWave() {
        level++;
        Wave w= new Wave(waves.get(level));

        dtSpawn = new Delta(2 * 120 * ft);
        dtWave = new Delta(w.getDelta().getLimit()*ft);
        rocks = w.getRocks();
        waveCounter = new RechargeAnim(dtWave.getLimit(), new Vector2());
        waveCounter.setWidth(1f);
        waveCounter.setRadius(48);
        waveCounter.setColor(Color.WHITE);

        gameover = false;
    }

    public static void start() {
        started=true;
    }
}

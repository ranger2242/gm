package com.quadx.asteroids.asteroids;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.asteroids.powerups.*;
import com.quadx.asteroids.shapes1_2.Circle;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.tools.Delta;
import com.quadx.asteroids.tools.Fonts;
import com.quadx.asteroids.tools.Sounds;

import java.util.ArrayList;

import static com.quadx.asteroids.states.AsteroidState.player;
import static com.quadx.asteroids.tools.Fonts.setFontSize;
import static com.quadx.asteroids.tools.Game.*;

public class Ship {
    private Triangle shape;
    private Vector2 pos = new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
    private float r = 30f;
    private float angle = 0;
    private final Vector2 vel = new Vector2();
    private final Delta dt_shoot;
    private final Delta dt_deathcount;
    private final Delta dtSwap;
    private boolean death = false;
    private boolean host = false;
    private int finalScore = 0;
    private int score = 0;
    private int lives = 3;
    private int tempScore = 0;
    private int money = 0;
    private final ArrayList<Powerup> powerups = new ArrayList<>();
    private Powerup powerup = new Bullet();
    private final Color color;

    public Ship() {
        this(Color.RED);
    }

    private Ship(Color c) {
        shape = Triangle.updatePoints(pos, angle, r);
        dt_shoot = new Delta(12 * ft);
        dtSwap = new Delta(20 * ft);
        lives = 3;
        death = false;
        dt_deathcount = new Delta(3 * ft);
        color = c;
        powerups.add(new Bullet());
        powerups.add(new Laser());
        powerups.add(new CBomb());
        powerups.add(new Mine());

    }

    public void render(ShapeRendererExt sr) {

        sr.set(ShapeRendererExt.ShapeType.Filled);
        sr.setColor(color);
        if (host)
            sr.setColor(Color.GREEN);

        if(isDead()){
            sr.set(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.WHITE);
            sr.circle(new Circle(new Vector2(WIDTH/2,HEIGHT/2),getR()*3));
        }

    for(int i=0; i<lives;i++){
            Triangle t = Triangle.updatePoints(pos,90,r);
            sr.setColor(1,1,1,1);

            t.setPos(new Vector2((40*sclf)*i+WIDTH*(.1f),HEIGHT*.8f));
            sr.triangle(t);

        }
        sr.setColor(Color.RED);

        Triangle t = new Triangle(shape);
        t.setPos(t.getCenter());
        sr.triangle(t);
        powerup.render(sr);
    }

    public void renderSB(SpriteBatch sb) {
        sb.draw(powerup.getIcon(), WIDTH * .05f, HEIGHT * .8f);
        Fonts.getFont().setColor(1, 1, 1, 1);
        setFontSize(10);
        if(isDead()) {
            String s="WAIT FOR RESPAWN";
            Fonts.getFont().draw(sb, s, (WIDTH/2) -(Fonts.getWidth(s)/2), (HEIGHT/2) +(Fonts.getHeight(s)/2));

        }
        Fonts.getFont().draw(sb, "Score: " + player.getScore(), WIDTH * .05f, HEIGHT * .95f);
        Fonts.getFont().draw(sb, "Money: " + player.getMoney(), WIDTH * .05f, HEIGHT * .90f);
    }

    private void updateShape() {
        shape = Triangle.updatePoints(pos, angle, r * scl.dst(new Vector2()));

    }

    public Triangle getShape() {
        return shape;
    }

    public void setShape(Triangle shape) {
        this.shape = shape;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public float getR() {
        return r;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getAngle() {
        return angle;
    }

    private void setAngle(float angle) {
        this.angle = angle;
    }

    public static void move(Ship s) {
        if(!s.isDead()) {
            float m = .5f;
            float x = (float) (m * Math.cos(Math.toRadians((s.getAngle()))));
            float y = (float) (m * Math.sin(Math.toRadians((s.getAngle()))));
            s.vel.add(x, y);
        }
    }


    private void rotate(float angle) {
        this.angle += angle;
    }

    public static void rotate(Ship s, float angle) {
        s.rotate(angle);
    }


    public void update(float dt) {//optimized 12-7-17
        dt_shoot.update(dt);
        dt_deathcount.update(dt);
        dtSwap.update(dt);
        powerup.update(dt);
        if(!isDead()) {
            pos.add(vel.scl(.97f));
            pos.set(wrap(pos));
        }
        updateShape();
        respawn();
        finalScore = score;

    }

    public static void shoot(Ship s) {
        s.shoot();
    }

    public boolean isHost() {
        return host;
    }

    private void shoot() {
        if (powerup.canUse()) {
            powerup.use(this);
        }
    }

    public void setPowerUp(Powerup p) {
        powerup = p;
    }

    private void setDeath(boolean death) {
        this.death = death;
        if(death && dt_deathcount.isDone()){
            lives -= 1;
            pos.set(-999,-999);
            dt_deathcount.reset();

        }
    }

    private void respawn() {
        if (AsteroidState.isSpawnClear() && death
                && lives>0) {
            vel.set(0,0);
            pos.set(WIDTH / 2, HEIGHT / 2);
            this.death = false;
            angle = 0;
            System.out.println("Fuck");
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public boolean getDeath() {
        return death;
    }

    public void setTempScore(int score) {
        tempScore += score;
    }

    public boolean isGameOver() {
        return lives == 0;
    }


    public static void move(Ship s, Vector2 direction) {
        s.setAngle(direction.angle());
        s.vel.add(direction.x * .5f, direction.y * .5f);
    }

    public void addMoney(int i) {
        money += i;
    }

    public int getMoney() {
        return money;
    }

    public void addScore(float v) {
        score += v;
    }

    public Powerup getPowerUp() {
        return powerup;
    }

    public void hit() {
        setDeath(true);
        Sounds.boom.play(.7f * Sounds.mainVolume);

    }

    public int[] getActivePowers() {
        return new int[]{powerup.getIndex()};
    }

    public Vector2 getVel() {
        return vel;
    }

    public static void swapPower(Ship player) {
        player.swap();
    }

    private int pindex = 0;

    private void swap() {
        if (dtSwap.isDone()) {
            pindex++;
            pindex %= powerups.size();
            powerup = powerups.get(pindex);
            dtSwap.reset();
        }
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public static void upgradePower(Ship player) {
        player.upgrade();
    }

    private void upgrade() {
        powerup.upgrade();
    }

    public boolean isDead() {
        return death;
    }
}

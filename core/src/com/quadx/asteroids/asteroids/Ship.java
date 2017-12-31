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
    private boolean immune = false;
    private int finalScore = 0;
    private int score = 0;
    private int lives = 3;
    private int tempScore = 0;
    private int money = 0;
    private int pindex = 0;
    private final ArrayList<Powerup> powerups = new ArrayList<>();
    private final ArrayList<Powerup> activePowerups = new ArrayList<>();

    private Powerup powerup = new Freeze();
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
        powerup = powerups.get(0);
        activePowerups.addAll(powerups);

    }
    //////////////////////////////////////////
    //getters
    //////////////////////////////////////////
    public ArrayList<Powerup> getPowerups(){
        return powerups;
    }
    public Triangle getShape() {
        return shape;
    }
    public Vector2 getPos() {
        return pos;
    }

    public ArrayList<Powerup> getActivePowerups() {
        return activePowerups;
    }

    public Vector2 getVel() {
        return vel;
    }
    public Powerup getPowerUp() {
        return powerup;
    }
    public boolean getDeath() {
        return death;
    }
    public boolean isHost() {
        return host;
    }
    public boolean isDead() {
        return death;
    }
    public boolean isGameOver() {
        return lives == 0;
    }
    public float getAngle() {
        return angle;
    }
    public float getR() {
        return r;
    }
    public int getMoney() {
        return money;
    }
    public int getScore() {
        return score;
    }
    public int getFinalScore() {
        return finalScore;
    }
    public Vector2 getBulletVel(){
        try{
            Bullet b =( Bullet)powerup;
            return b.getFistBullet();

        }catch (Exception ex){
            return new Vector2();
        }
    }

    //////////////////////////////////////////
    //setters
    //////////////////////////////////////////
    public void setR(float r) {
        this.r = r;
    }
    public void setPos(Vector2 pos) {
        this.pos = pos;
    }
    public void setHost(boolean host) {
        this.host = host;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public void setShape(Triangle shape) {
        this.shape = shape;
    }
    public void setImmune(boolean immune) {
        this.immune = immune;
    }
    public void setPowerUp(Powerup p) {
        powerup = p;
    }
    public void setTempScore(int score) {
        tempScore += score;
    }
    private void setDeath(boolean death) {
        this.death = death;
        if (death && dt_deathcount.isDone()) {
            lives -= 1;
            pos.set(-999, -999);
            dt_deathcount.reset();

        }
    }
    private void setAngle(float angle) {
        this.angle = angle;
    }

    //////////////////////////////////////////
    //render
    //////////////////////////////////////////
    public void render(ShapeRendererExt sr) {

        sr.set(ShapeRendererExt.ShapeType.Filled);
        sr.setColor(color);
        if (host)
            sr.setColor(Color.GREEN);

        if (isDead()) {
            sr.set(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.WHITE);
            sr.circle(new Circle(new Vector2(WIDTH / 2, HEIGHT / 2), getR() * 3));
        }

        for (int i = 0; i < lives; i++) {
            Triangle t = Triangle.updatePoints(pos, 90, r);
            sr.setColor(1, 1, 1, 1);

            t.setPos(new Vector2((40 * sclf) * i + WIDTH * (.1f), HEIGHT * .8f));
            sr.triangle(t);

        }
        sr.setColor(Color.RED);

        Triangle t = new Triangle(shape);
        t.setPos(t.getCenter());
        if (immune)
            sr.setColor(Color.BLUE);
        else
            sr.setColor(Color.RED);
        sr.triangle(t);
        powerup.render(sr);
    }

    public void renderSB(SpriteBatch sb) {
        sb.draw(powerup.getIcon(), WIDTH * .05f, HEIGHT * .65f);
        Fonts.getFont().setColor(1, 1, 1, 1);
        setFontSize(10);
        if (isDead()) {
            String s = "WAIT FOR RESPAWN";
            Fonts.getFont().draw(sb, s, (WIDTH / 2) - (Fonts.getWidth(s) / 2), (HEIGHT / 2) + (Fonts.getHeight(s) / 2));

        }
        Fonts.getFont().draw(sb, "Score: " + player.getScore(), WIDTH * .05f, HEIGHT * .95f);
        Fonts.getFont().draw(sb, "Money: " + player.getMoney(), WIDTH * .05f, HEIGHT * .90f);
      // Fonts.getFont().render(sb, "vel player: " + player.getVel().x+" "+player.getVel().y, WIDTH * .05f, HEIGHT * .06f);
        Fonts.getFont().draw(sb, "vel: " + player.getBulletVel().toString()+" "+player.getVel().y, WIDTH * .05f, HEIGHT * .06f);
    }



    private void updateShape() {
        shape = Triangle.updatePoints(pos, angle, r * scl.dst(new Vector2()));

    }

    public static void move(Ship s) {
        if (!s.isDead()) {
            s.vel.add(Circle.point(new Vector2(0, 0), .5f, s.getAngle()));
        }
    }
    public static void rotate(Ship s, float angle) {
        s.rotate(angle);
    }
    public static void shoot(Ship s) {
        s.shoot();
    }
    public static void move(Ship s, Vector2 direction) {
        s.setAngle(direction.angle());
        s.vel.add(direction.x * .5f, direction.y * .5f);
    }
    public static void swapPower(Ship player) {
        player.swap();
        player.setImmune(false);
    }
    public static void upgradePower(Ship player) {
        player.upgrade();
    }
    private void rotate(float angle) {
        this.angle += angle;
    }
    private void shoot() {
        if (powerup.canUse()) {
            powerup.use(this);
        }
    }
    private void respawn() {
        if (AsteroidState.isSpawnClear() && death
                && lives > 0) {
            vel.set(0, 0);
            pos.set(WIDTH / 2, HEIGHT / 2);
            this.death = false;
            angle = 0;
            System.out.println("Fuck");
        }
    }
    private void swap() {
        if (dtSwap.isDone()) {
            pindex++;
            pindex %= activePowerups.size();
            powerup = activePowerups.get(pindex);
            dtSwap.reset();
        }
    }
    private void upgrade() {
        powerup.upgrade();
    }
    public void update(float dt) {//optimized 12-7-17
        dt_shoot.update(dt);
        dt_deathcount.update(dt);
        dtSwap.update(dt);
        powerup.update(dt);
        if (!isDead()) {
            pos.add(vel.scl(.97f));
            pos.set(wrap(pos));
        }else{
            AsteroidState.level=0;
        }
        updateShape();
        respawn();
        finalScore = score;

    }
    public void addMoney(int i) {
        money += i;
    }
    public void addScore(float v) {
        score += v;
    }
    public void hit() {
        if (!immune) {
            setDeath(true);
            Sounds.boom.play(.7f * Sounds.mainVolume);
        }

    }


    public void buyPowerup(Powerup powerup) {
        powerups.add(powerup);
    }

    public void addActive(Powerup powerup) {
        for(Powerup a:powerups){
            if(a.getClass().equals(powerup.getClass())){
                activePowerups.add(a);
            }
        }
    }

    public void removeActive(Powerup powerup) {
        for(int i=0;i<activePowerups.size();i++){
            if(activePowerups.get(i).getClass().equals(powerup.getClass())){
                activePowerups.remove(i);
            }
        }
    }

    public void upgrade(Powerup powerup) {
        for(Powerup a:powerups){
            if(a.getClass().equals(powerup.getClass())){
                a.upgrade();
            }
        }
    }

    public boolean owns(Class<? extends Powerup> aClass) {
        for(Powerup p: powerups){
            if(p.getClass().equals(aClass))return true;
        }
        return false;
    }
    public Powerup getPowerup(Class<? extends Powerup> aClass){
        for(Powerup p: powerups){
            if(p.getClass().equals(aClass))return p;
        }
        return null;
    }

    public static void hold(Ship player) {
        player.hold();
    }

    private void hold() {
        vel.set(0,0);
    }

    public boolean canBuy(int price) {
        return (money>=price);
    }


}

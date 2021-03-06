package com.quadx.asteroids.asteroids;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.shapes1_2.Ngon;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.tools.Delta;

import java.util.ArrayList;

import static com.quadx.asteroids.states.AsteroidState.player;
import static com.quadx.asteroids.states.AsteroidState.rn;
import static com.quadx.asteroids.tools.Game.*;

public class Asteroid {
    private final Color c = new Color();
    private Ngon shape;
    private int n = 8;
    private int r = 20;
    private Vector2 pos = new Vector2();
    private final Vector2 vel = new Vector2();
    private float angle = 0;
    private boolean death = false;
    private int size = 3;
    private final Delta dt_flash = new Delta(10 * ft);
    private Delta bumped = new Delta(60 * ft);
    private boolean marked = false;
    private Asteroid(Vector2 pos, float r, float n) {
        this(pos,new Vector2(), r, n, 3,0);

    }

    public Asteroid(Vector2 pos, float r, float n, float size) {
        this(pos,new Vector2(),r,n,size,0);
    }

    public Asteroid(Vector2 pos, Vector2 vel, float r, float n, float size) {
        this(pos,vel,r,n,size,0);


    }

    public Asteroid(Asteroid a) {
        this(new Vector2(a.getPos()),new Vector2(a.getVel()),a.getR(),a.getN(),a.getSize(),a.getAngle());
    }

    public Asteroid(Vector2 pos, Vector2 vel, float r, float n, float size, float angle) {
        this.pos.set(pos);
        this.r =(int) r;
        this.n =(int) n;
        this.angle=angle;
        this.shape = new Ngon(pos, (int)r, (int)n,angle);
        this.shape.scl(scl);
        this.vel.set(vel);
        this.size = (int) size;
        this.c.set((rn.nextInt(190) + 65) / 255f, (rn.nextInt(190) + 65) / 255f, (rn.nextInt(190) + 65) / 255f, 1);
        bumped.finish();
    }

    private Vector2 ranVel() {
        Vector2 pos = new Vector2();
        float m = 2.2f;
        pos.x = (float) (m * Math.cos(Math.toRadians(rn.nextInt(360))));
        pos.y = (float) (m * Math.sin(Math.toRadians(rn.nextInt(360))));

        return pos;
    }


    public void render(ShapeRendererExt sr) {
        if (dt_flash.isDone()) {
            c.set((rn.nextInt(190)+65)/255f,(rn.nextInt(190)+65)/255f,(rn.nextInt(190)+65)/255f, 1);
            dt_flash.reset();
        }

        sr.setColor(c);
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.ngon(shape);
    }

    public void update(float dt) {
        bumped.update(dt);

        dt_flash.update(dt);
        angle+=1f;
       // shape.setAngle(angle);
        pos.set(wrap(pos));
        move(dt);
        if (isBumped()) {
            bumped();
        }

    }


    public void bumped() {
        Vector2 v = new Vector2(player.getVel());
        v.scl(20);

        Vector2 cs = shape.getCenter();
        Vector2 cp = player.getPos();

        Vector2 nv = new Vector2();
        float dx = cs.x - cp.x;
        float dy = cs.y - cp.y;
        float theta = (float) Math.atan2(dy, dx);


        float dst = cs.dst(cp);
        float nm = player.getR() + 60 + shape.getR();
        if (dst < nm) {
            Vector2 n = new Vector2();
            n.x = (float) (cp.x + (nm*1.1 * Math.cos(theta)));
            n.y = (float) (cp.y + (nm*1.1 * Math.sin(theta)));
            shape.setPos(n);
        }
        bumped.finish();
        if (bumped.isDone()) {
            bumped.reset();

        }
    }


    public Ngon getShape() {
        return shape;
    }

    @SuppressWarnings("unused")
    public void setShape(Ngon shape) {
        this.shape = shape;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public Vector2 getVel() {
        return vel;
    }

    public void setVel(Vector2 vel) {
        this.vel.set(vel);
    }

    public float setRanAngle() {
        return (float) Math.random() * (360);
    }

    private void move(float dt) {
        float x=vel.x*scl.x*dt;
        float y=vel.y*scl.y*dt;
        pos.add(new Vector2(x,y));
        shape.setPos(pos);
    }



    public static ArrayList<Asteroid> generate(int n) {
        ArrayList<Asteroid> rocks = new ArrayList<>();
/*
        for (int x = 0; x < n; x++) {
            Vector2 pos = new Vector2();
            int r = 100;
            int num = rn.nextInt(8) + 3;
            rocks.add(new Asteroid(pos, r, num));
        }*/
        return rocks;
    }

    public void setSize(int size) {
        this.size = size;
    }


    public Asteroid[] split() {
        if (size > 1) {
            float r=1.2f;
            Asteroid r1 = new Asteroid(this.pos.add(20,0), new Vector2(vel.x*r,vel.y*r),this.r / 2, n, size - 1,angle);
            Asteroid r2 = new Asteroid(this.pos.add(-20,0),new Vector2(vel.y*r,vel.x*r),this.r / 2, n, size - 1,angle);
            return new Asteroid[]{r1, r2};

        }
        return new Asteroid[0];

    }

    public void wasShot() {
        this.death = true;

    }

    public boolean isDead() {
        return death;
    }

    public int getSize() {
        return size;
    }

    public void setAngle(float ang) {
        shape.setAngle(ang);
    }

    public float getAngle() {
        return angle;
    }


    public boolean isBumped() {
        return !bumped.isDone();
    }
    public void setMarked(boolean b){
        marked=b;
    }

    public boolean isMarked() {
        return marked;
    }
}

package com.quadx.asteroids.asteroids.powerups;

import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.shapes1_2.Circle;
import com.quadx.asteroids.shapes1_2.Line;
import com.quadx.asteroids.shapes1_2.Shape;
import com.quadx.asteroids.tools.Delta;

import static com.quadx.asteroids.tools.Game.ft;
import static com.quadx.asteroids.tools.Game.scl;

/**
 * Created by Chris Cavazos on 12/9/2017.
 */
class Shot {
    private Vector2 vel = new Vector2(100, 100);
    private Delta dt = new Delta(50 * ft);
    private Delta dtKill = new Delta(10*ft);
    private Shape shape;
    private Class type;
    private boolean death = false;
    private boolean flag= false;
    private float angle;
    private float radius=0;

    public Shot(Vector2 pos, Vector2 vel, float theta, float off) {//Bullet
        shape = new Circle(new Vector2(pos), 2f * scl.x);

        float m = 500;
        angle=theta + (-off / 2);
        float vx = m * (float) (Math.cos(Math.toRadians(angle)));
        float vy = m * (float) (Math.sin(Math.toRadians(angle)));
        this.vel.set(vel);
        this.vel.add(vx, vy);
    }


    public Shot(Vector2 pos, float theta, float off,float len) {//laser
        angle=theta + (-off / 2);
        radius=len;
        float x = pos.x + (len) * (float) Math.cos(Math.toRadians(angle));
        float y = pos.y + (len) * (float) Math.sin(Math.toRadians(angle));
        Vector2 b = new Vector2(x, y);
        shape = new Line(pos, b);
    }

    public Shot(Vector2 pos) {//mines
        radius=3;
        shape=new Circle(pos,radius);
        dt=new Delta(2000*ft);
    }


    public void updateIndividual(float dt) {
        if(type.equals(Laser.class)){
           shape= Laser.setPos(dt,(Line) shape,angle,radius);
        }
        if(type.equals(Bullet.class)) {
            shape=Bullet.setPos(dt,(Circle) shape, vel);
        }
        this.dt.update(dt);
        if (this.dt.isDone())
            death = true;
        if(flag){
            dtKill.update(dt);
            ((Circle) shape).radius*=1.5f;
            if(dtKill.isDone()){
                death=true;
                kill();
                dtKill.reset();
                flag=false;
            }
        }
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Vector2 getVel() {
        return vel;
    }

    public void setVel(Vector2 vel) {
        this.vel = vel;
    }


    public void collided() {
        this.death = true;
    }
    public void setType(Class c){
        type=c;
    }
    public boolean isDeath() {
        return death;
    }

    public void setDeath(boolean death) {
        this.death = death;

    }
    void kill(){
        if(this.death){
            ((Circle) shape).radius=0f;
            ((Circle) shape).center=new Vector2(-999,-999);
            vel=null;
            dt=null;
            shape=null;
            type=null;
            angle=0;
            radius=0;
        }
    }
    public Shape getShape() {
        return shape;
    }

    public void mineCollide() {
        flag=true;
    }
}

package com.quadx.asteroids.asteroids.powerups;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.shapes1_2.Circle;
import com.quadx.asteroids.shapes1_2.Ngon;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.states.AsteroidState;

public class Shield extends Powerup {
    public Color color = new Color();
    public int numHits = 3;
    public Circle shape = new Circle();
    Vector2 playerPos = new Vector2();
    public Shield(){
        loadTexture("shield");
        name="SHIELD";
        price=500;
    }

    @Override
    public void use(Ship s) {

    }

    @Override
    public void update(float dt) {
        playerPos = AsteroidState.player.getPos();
        if(numHits!=0){
            shape = new Circle(playerPos,AsteroidState.player.getR()+60);
        }


    }

    @Override
    public void render(ShapeRendererExt sr) {
        sr.set(ShapeRenderer.ShapeType.Line);
        sr.circle(shape);
    }

    @Override
    public boolean collide(Asteroid a) {
        Ngon n = a.getShape();
        for(Triangle t: Triangle.triangulate(n)){
            if(shape.overlaps(t)){

                if(!a.isBumped()){
                    a.bumped();
                }
                return false;
            }
        }
        return false;
    }


    @Override
    public void upgrade() {

    }

}

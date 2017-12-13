package com.quadx.asteroids.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.states.AsteroidState;


public class LeftComm extends Command {
    public LeftComm(){
        name="Left";
        keyboard= Input.Keys.A;

    }
    @Override
    public void execute() {
        if(pressed()){

        }
        if (cls.equals(AsteroidState.class)) {
            if(Gdx.input.isKeyPressed(Input.Keys.A)
                    ){
               // Ship.move(AsteroidState.player,new Vector2(-10,0));
                Ship.rotate(AsteroidState.player,4f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){


            }
        }
    }
}

package com.quadx.asteroids.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.states.AsteroidState;


public class DownComm extends Command {
    public DownComm(){
        name="Down";
        keyboard= Input.Keys.S;
    }
    @Override
    public void execute() {
        if(pressed()){

        }
        if (cls.equals(AsteroidState.class)) {
            if(Gdx.input.isKeyPressed(Input.Keys.S)){
                Ship.move(AsteroidState.player);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){


            }
        }
    }
}

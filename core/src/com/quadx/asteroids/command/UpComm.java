package com.quadx.asteroids.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.states.AsteroidState;

public class UpComm extends Command {
    public UpComm(){
        name="Up";
        keyboard= Input.Keys.W;
    }
    @Override
    public void execute() {
        if(pressed()){

        }
        if (cls.equals(AsteroidState.class)) {
            if(Gdx.input.isKeyPressed(Input.Keys.W)){
                Ship.move(AsteroidState.player);
            }

        }
    }
}

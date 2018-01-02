package com.quadx.asteroids.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.states.MultiplayerState;

public class RightComm extends Command {
    public RightComm(){
        name="Right";
        keyboard= Input.Keys.D;

    }
    @Override
    public void execute() {
        if (pressed()) {

        }
        if(cls.equals(AsteroidState.class)||cls.equals(MultiplayerState.class)){
            if(Gdx.input.isKeyPressed(Input.Keys.D)  ){
                Ship.rotate(AsteroidState.player,-4f);
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){


            }
        }
    }
}

package com.quadx.asteroids.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.tools.Game;

import static com.quadx.asteroids.states.AsteroidState.hud;
import static com.quadx.asteroids.tools.Game.mode;

/**
 * Created by Chris Cavazos on 12/10/2017.
 */
public class SwapComm extends Command {
    public SwapComm() {
    }

    @Override
    public void execute() {
        if(cls.equals(AsteroidState.class)){
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)  && mode== Game.Mode.DESKTOP){
                Ship.swapPower(AsteroidState.player);
            }
            if(mode== Game.Mode.ANDROID&& hud.getSwapButton().isPressed() ){
                Ship.swapPower(AsteroidState.player);
            }
        }
    }
}

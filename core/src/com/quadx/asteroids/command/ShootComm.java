package com.quadx.asteroids.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.states.MenuState;
import com.quadx.asteroids.tools.Game;

import static com.quadx.asteroids.states.AsteroidState.hud;
import static com.quadx.asteroids.tools.Game.mode;

public class ShootComm extends Command {
    public ShootComm(){

    }
    @Override
    public void execute() {
        if (cls.equals(MenuState.class)) {

            if (mode == Game.Mode.ANDROID &&hud.getMainButton().isPressed()) {
                MenuState.startGame();
            }
        }

        if (cls.equals(AsteroidState.class)) {

            if(Gdx.input.isKeyPressed(Input.Keys.SPACE)  && mode== Game.Mode.DESKTOP){
                Ship.shoot(AsteroidState.player);
            }
            if(mode== Game.Mode.ANDROID&&hud.getMainButton().isPressed() ){
                Ship.shoot(AsteroidState.player);
            }

        }
    }
}

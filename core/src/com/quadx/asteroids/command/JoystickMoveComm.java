package com.quadx.asteroids.command;

import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.tools.Game;

import static com.quadx.asteroids.states.AsteroidState.hud;
import static com.quadx.asteroids.tools.Game.mode;

/**
 * Created by Chris on 12/3/2017.
 */

public class JoystickMoveComm extends Command {
    public JoystickMoveComm() {
    }

    @Override
    public void execute() {

        if (cls.equals(AsteroidState.class)) {
            if(mode== Game.Mode.ANDROID)
            if (hud.getTouchJoyStick().getDirection().x != 0 ||hud.getTouchJoyStick().getDirection().y != 0) {
                Ship.move(AsteroidState.player, hud.getTouchJoyStick().getDirection());
            }

        }
    }
}


package com.quadx.asteroids.command;

import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.states.AsteroidState;
import com.quadx.asteroids.tools.Game;

import static com.quadx.asteroids.states.AsteroidState.hud;
import static com.quadx.asteroids.tools.Game.mode;

/**
 * Created by Chris Cavazos on 12/10/2017.
 */
public class UpgradeComm extends Command {
    public UpgradeComm() {
    }

    @Override
    public void execute() {
        if(cls.equals(AsteroidState.class)){
            if(mode== Game.Mode.ANDROID&& hud.getIncButton().isPressed() ){
                Ship.upgradePower(AsteroidState.player);
            }
        }
    }
}

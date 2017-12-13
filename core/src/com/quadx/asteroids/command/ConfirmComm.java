package com.quadx.asteroids.command;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;


public class ConfirmComm extends Command {
    public ConfirmComm(){
        name="Confirm";
        keyboard= Input.Keys.ENTER;
    }
    @Override
    public void execute() {
        if(pressed()){

        }
            if(Gdx.input.isKeyPressed(Input.Keys.C)){

            }
            if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)){

            }
    }
}

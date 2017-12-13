package com.quadx.asteroids.tools;

import com.badlogic.gdx.Input;


public class TextBox implements Input.TextInputListener {
    private String input = "";

    @Override
    public void input(String text) {
    /*    Com.setIP(text);
        MultiplayerState.connect();*/

    }

    @Override
    public void canceled() {
    }

    public String getInput() {
        return input;
    }
}
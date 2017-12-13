package com.quadx.asteroids.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by Chris Cavazos on 12/7/2017.
 */
public class Sounds {
    public static float mainVolume =0;
    public static com.badlogic.gdx.audio.Sound boom;
    public static com.badlogic.gdx.audio.Sound gameOver;
    public static Sound bleep;

    public static void load(){
        boom = Gdx.audio.newSound(Gdx.files.internal("sfx/boom.mp3"));
        gameOver = Gdx.audio.newSound(Gdx.files.internal("sfx/gameover.mp3"));
        bleep = Gdx.audio.newSound(Gdx.files.internal("sfx/bleep.mp3"));
    }
}

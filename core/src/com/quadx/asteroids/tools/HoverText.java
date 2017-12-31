package com.quadx.asteroids.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import static com.quadx.asteroids.tools.Game.ft;

/**
 * HoverText 1.1
 * Created by Chris Cavazos on 5/28/2016.
 */
public class HoverText {
    public static ArrayList<HoverText> texts = new ArrayList<>();
    private Color color;
    private Color initcolor;

    private final String text;
    private boolean active = false;
    private final float x;
    private final float y;
    private float px = 0;
    private float py = 0;
    private int ymod;
    private float dtHov;
    private float dtFlash;
    private final boolean flash;
    private boolean cycle;
    private float time;
    private float alpha =1;

    public HoverText(String s, float t, Color c, float x1, float y1, boolean flash) {

        active = true;
        time = t;
        text = s;
        color = c;
        initcolor = c;
        x = x1;
        y = y1;
        this.flash = flash;
        if (flash) {
            dtFlash = 0;
            cycle = true;
        }
        texts.add(this);
    }


    public void update() {
        dtHov += Gdx.graphics.getDeltaTime();
        if (flash) dtFlash += Gdx.graphics.getDeltaTime();
        //delete inactive hoverText
        boolean[] index;
        if (!texts.isEmpty()) {
            index = new boolean[texts.size()];
            texts.stream().filter(h -> !h.isActive()).forEach(h -> index[texts.indexOf(h)] = true);
            for (int i = texts.size() - 1; i >= 0; i--) {
                try {
                    if (index[i]) {
                        texts.remove(i);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
            while (HoverText.texts.size() > 10) HoverText.texts.remove(0);

            if (active) {
                float time = 1.2f;
                if (dtHov < time) {
                    //  Game.setFontSize(3);
                    CharSequence cs = text;
                    GlyphLayout gl = new GlyphLayout();
                    gl.setText(Fonts.getFont(), cs);
                    if (dtFlash > .1) {
                        if (cycle) {
                            color = (Color.WHITE);
                        } else color = (initcolor);
                        cycle = !cycle;
                        dtFlash = 0;
                    }
                    px = (int) (x - (gl.width / 2));
                    py = y + ymod;

                    ymod++;
                } else {
                    dtHov = 0;
                    ymod = 0;
                    active = false;
                }

            }
        }
        fade();
    }

    public void fade() {
        float t = time / ft;
        alpha -= (1 / t);
    }

    public void render(SpriteBatch sb) {
        if (active) {
            Fonts.setFontSize(6);
            if (dtHov < time) {
                color.a=alpha;
                Fonts.getFont().setColor(color);

                Fonts.getFont().draw(sb, text, px, py);

            }
        }


    }

    public boolean isActive() {
        return active;
    }

    public float getTime() {
        return time;
    }
}

package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.asteroids.command.Command;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.tools.Com;
import com.quadx.asteroids.tools.TextBox;

import java.util.ArrayList;

import static com.quadx.asteroids.tools.Fonts.setFontSize;


/**
 * Created by Chris Cavazos on 8/14/2017.
 */
public class MultiplayerState extends State {
    private final ShapeRendererExt sr = new ShapeRendererExt();
    private final ArrayList<String> output = new ArrayList<>();
    private static final Triangle[] otherPlayers = new Triangle[0];
    private static Com com = new Com();

    public MultiplayerState(GameStateManager gsm) {
        super(gsm);
        Command.cls = AsteroidState.class;
        this.gsm = gsm;
        output.clear();
        sr.setAutoShapeType(true);
        TextBox box = new TextBox();
        Gdx.input.getTextInput(box, "ip", "", "");
        setFontSize(10);
    }

    @Override
    protected void handleInput() {
        AsteroidState.input();
    }

    @Override
    public void update(float dt) {
        handleInput();
        if (AsteroidState.inGame()) {
            Command.cls = AsteroidState.class;
            AsteroidState.updateLoop(dt);
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if (AsteroidState.inGame()) {
            AsteroidState.drawScene(sb, sr);
            drawOtherPlayers(sr);

        }
    }


    private void drawOtherPlayers(ShapeRendererExt sr) {
        for (Triangle t : otherPlayers) {
            sr.setColor(Color.BLUE);
            if (t != null && t.getPoints() != null)
                sr.triangle(t);
        }
    }

    /*    private void sbdrawStringOutput(SpriteBatch sb) {
     *//* for(HoverText t:HoverText.texts){
            t.draw(sb);
        }*//*
        for (int i = 0; i < output.size() && i < 10; i++) {
            try {
                Color c;
                if (i == output.size() - 1) {
                    c = Color.WHITE;
                } else
                    c = Color.GRAY;
                Fonts.getFont().setColor(c);
                Fonts.getFont().draw(sb, output.get(i), (2 * WIDTH / 3) - 30, HEIGHT - (i * 20) - 30);
            } catch (IndexOutOfBoundsException | NullPointerException ignored) {
            }
        }
    }*/

    @Override
    public void dispose() {

    }
}
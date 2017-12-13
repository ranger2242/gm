package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.asteroids.command.Command;
import com.quadx.asteroids.tools.Fonts;

import static com.quadx.asteroids.tools.Game.HEIGHT;
import static com.quadx.asteroids.tools.Game.WIDTH;

public class PauseState extends State {
    public PauseState(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            gsm.pop();
            Command.cls=AsteroidState.class;
        }
    }

    @Override
    public void update(float dt) {
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        Fonts.getFont().getData().setScale(4);
        GlyphLayout layout = new GlyphLayout(Fonts.getFont(), "PAUSED");
        Fonts.getFont().draw(sb, layout, (WIDTH / 2) - (layout.width / 2), HEIGHT / 2);
        sb.end();
    }

    @Override
    public void dispose() {

    }
}

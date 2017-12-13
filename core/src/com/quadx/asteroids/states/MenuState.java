package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.asteroids.command.Command;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.tools.Delta;
import com.quadx.asteroids.tools.Fonts;
import com.quadx.asteroids.tools.Game;

import static com.quadx.asteroids.tools.Fonts.setFontSize;
import static com.quadx.asteroids.tools.Game.HEIGHT;
import static com.quadx.asteroids.tools.Game.WIDTH;
import static com.quadx.asteroids.tools.Game.ft;
import static com.quadx.asteroids.tools.Game.mode;

public class MenuState extends State {
    private static final Delta dtStart = new Delta(30*ft);
    private static int numPlayers = 0;
    private final ShapeRendererExt sr = new ShapeRendererExt();

    public MenuState(GameStateManager gsm) {
        super(gsm);
        this.gsm=gsm;
    }

    @Override
    protected void handleInput() {

            if(Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) && dtStart.isDone()){
                startGame();
            }
        for (Command c : Game.queue) {
            c.execute();
        }

    }

    @Override
    public void update(float dt) {
        Command.cls=getClass();

        handleInput();
        dtStart.update(dt);
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        drawTitleScene(sb);
    }
    private void drawTitleScene(SpriteBatch sb) {
        sb.begin();
        setFontSize(10);
        float y = (HEIGHT - 100);
        GlyphLayout layout = new GlyphLayout(Fonts.getFont(), "MULTIPLAYER");
        Fonts.getFont().draw(sb, layout, (WIDTH / 2) - (layout.width / 2), y);
        setFontSize(20);

        layout = new GlyphLayout(Fonts.getFont(), "ASTEROIDS");
        Fonts.getFont().draw(sb, layout, (WIDTH / 2) - (layout.width / 2), y - 100);
        setFontSize(5);
        layout = new GlyphLayout(Fonts.getFont(), "PLAYERS: " + numPlayers);
        Fonts.getFont().draw(sb, layout, (WIDTH / 2) - (layout.width / 2), (HEIGHT / 2) - 300);

        String startmsg = "";
        switch (mode) {

            case ANDROID:
            case IOS:
                startmsg = "PRESS BUTTON TO BEGIN";
                break;
            case DESKTOP:
                startmsg = "PRESS ENTER TO BEGIN";
                break;
        }

        layout = new GlyphLayout(Fonts.getFont(), startmsg);
        Fonts.getFont().draw(sb, layout, (WIDTH / 2) - (layout.width / 2), (HEIGHT / 2) - 100);
        if(AsteroidState.player !=null) {
            int a = AsteroidState.player.getFinalScore();
            if (a != 0) {
                layout.setText(Fonts.getFont(), "FINAL SCORE:" + a);
                Fonts.getFont().draw(sb, layout, (WIDTH / 2) - (layout.width / 2), (HEIGHT / 2));
            }
        }
        sb.end();
    }

    @Override
    public void dispose() {

    }

    public static void startGame() {
        dtStart.reset();
        Game.gsm.push(new AsteroidState(Game.gsm));
    }
}

package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quadx.asteroids.command.Command;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.tools.*;

import static com.quadx.asteroids.tools.Fonts.setFontSize;
import static com.quadx.asteroids.tools.Game.*;

public class MenuState extends State {
    private static final Delta dtStart = new Delta(30*ft);
    private static int numPlayers = 0;
    private final ShapeRendererExt sr = new ShapeRendererExt();
    private final Stage stage;
    boolean printErr=false;
    TextField ipField;
    public MenuState(GameStateManager gsm) {
        super(gsm);
        this.gsm=gsm;
        ScreenViewport viewport = new ScreenViewport();
        stage = new Stage(viewport,Game.batch);
        Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        ipField = new TextField("192.168.0.11",skin);
        ipField.setPosition(WIDTH*.05f,HEIGHT*.1f);
        ipField.setSize(WIDTH*.1f, HEIGHT*.05f);
        
        stage.addActor(ipField);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            decideSingleOrMulti();
        }
        for (Command c : Game.queue) {
            c.execute();
        }

    }

    private void decideSingleOrMulti() {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT) ||Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            startGame(new AsteroidState(Game.gsm));
        }else {
            String ip = ipField.getText();
            if (Com.validIP(ip)) {
                startGame(new LobbyState(Game.gsm,ip));
            }else{
                printErr=true;
            }
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
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
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
        String err = "UNABLE TO CONNECT";
        layout = new GlyphLayout(Fonts.getFont(), err);
        if(printErr)
        Fonts.getFont().draw(sb, layout, (WIDTH / 2) - (layout.width / 2), (HEIGHT / 2) - 200);

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

    public static void startGame(State s) {
        Game.gsm.push(s);
    }
}

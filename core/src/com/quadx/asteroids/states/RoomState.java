package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quadx.asteroids.tools.Fonts;
import com.quadx.asteroids.tools.Game;
import com.quadx.asteroids.tools.hud.Button;

import java.util.ArrayList;

import static com.quadx.asteroids.states.LobbyState.comm;
import static com.quadx.asteroids.tools.Game.HEIGHT;
import static com.quadx.asteroids.tools.Game.WIDTH;

/**
 * Created by Chris Cavazos on 12/30/2017.
 */
public class RoomState extends State {
    String name="";
    private  Stage stage;
    Skin skin;
    boolean init=false;
    Button exitRoomB;
    Button startGameB;
    static boolean load= false;

    public RoomState(GameStateManager gsm,String name) {
        super(gsm);
        this.name=name;
        init=true;
    }

    @Override
    protected void handleInput() {
        if(exitRoomB.isPressed()){
            comm.emit("dropPlayer",comm.room,comm.serverID);
            gsm.pop();
            gsm.push(new LobbyState(gsm));
        }
        if(startGameB.isPressed()){
            comm.emit("start",comm.room);
        }
    }

    @Override
    public void update(float dt) {
        if(init)
            initStage();
        handleInput();
        if(load)
            load();

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        sb.begin();
        Fonts.getFont().draw(sb,name,(WIDTH/2)- (Fonts.getWidth(name)/2),HEIGHT*.8f);
        ArrayList<String> list = comm.getRoom();
        for(int i=0;i<list.size();i++){
            Fonts.getFont().draw(sb,list.get(i),(WIDTH/2)- (Fonts.getWidth(list.get(i))/2),HEIGHT*(.7f-(.05f*i)));
        }
        sb.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {

    }

    public void initStage() {
        ScreenViewport viewport = new ScreenViewport();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(viewport, Game.batch);
        exitRoomB = new Button("EXIT ROOM", skin, new Vector2(WIDTH * .1f, HEIGHT * .2f));
        exitRoomB.setSize(new Vector2(WIDTH*.1f, HEIGHT * .05f));

        startGameB = new Button("START", skin, new Vector2(WIDTH * .8f, HEIGHT * .2f));
        startGameB.setSize(new Vector2(WIDTH*.1f, HEIGHT * .05f));
        stage.addActor(exitRoomB.getActor());
        stage.addActor(startGameB.getActor());
        Gdx.input.setInputProcessor(stage);
        init=false;
    }

    public void load(){
        gsm.push(new MultiplayerState(gsm));
    }
    public static void loadGame() {
        load=true;
    }
}

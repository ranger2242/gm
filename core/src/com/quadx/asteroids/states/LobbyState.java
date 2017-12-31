package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quadx.asteroids.tools.Com;
import com.quadx.asteroids.tools.Fonts;
import com.quadx.asteroids.tools.Game;
import com.quadx.asteroids.tools.hud.Button;

import java.util.ArrayList;

import static com.quadx.asteroids.tools.Game.HEIGHT;
import static com.quadx.asteroids.tools.Game.WIDTH;

/**
 * Created by Chris Cavazos on 12/30/2017.
 */
public class LobbyState extends State {
    static Com com = new Com();
    ArrayList<Button> roomButtons = new ArrayList<>();
    private static Stage stage;
    static Skin skin;
    static TextField nameField;
    static Button makeRoomB;
    public static boolean setRooms = false;

    LobbyState(GameStateManager gsm, String ip) {
        super(gsm);
        com.connect(ip);
        ScreenViewport viewport = new ScreenViewport();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage = new Stage(viewport, Game.batch);

        setRoomButtons();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    protected void handleInput() {

        if (makeRoomB.isPressed()) {
            String n = nameField.getText();
            nameField.clearSelection();
            com.emit("makeRoom", n);
        }
        for(Button b: roomButtons){
            if(b.isPressed()){
                com.emit("joinRoom",b.getText());
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        if(setRooms)
            setRoomButtons();
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        ArrayList<String> list = com.getLobbied();
        sb.begin();
        for (int i = 0; i < list.size(); i++) {
            Fonts.getFont().draw(sb, list.get(i), WIDTH * .1f, HEIGHT * (.8f - (.05f * i)));
        }
        sb.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void setRoomButtons() {
        stage.clear();
        roomButtons.clear();
        makeRoomB = new Button("CREATE ROOM", skin, new Vector2(WIDTH * .8f, HEIGHT * .8f));
        makeRoomB.setSize(new Vector2(250, HEIGHT * .05f));

        nameField = new TextField("Room Name", skin);
        nameField.setPosition(WIDTH * .5f, HEIGHT * .8f);
        nameField.setSize(WIDTH * .3f, HEIGHT * .05f);
        stage.addActor(makeRoomB.getActor());
        stage.addActor(nameField);

        ArrayList<String> list = com.getRooms();
        Button b;

        for (int i =0;i <list.size();i++) {
            String s= list.get(i);
            b = new Button(s, skin, new Vector2(WIDTH * .5f, HEIGHT * (.8f-(.05f*(i+1)))));
            b.setSize(new Vector2(WIDTH*.3f, HEIGHT * .05f));
            stage.addActor(b.getActor());
            roomButtons.add(b);
        }
        setRooms=false;
    }

    @Override
    public void dispose() {

    }
}

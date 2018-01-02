package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.command.Command;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.shapes1_2.Triangle;
import com.quadx.asteroids.tools.Game;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.quadx.asteroids.tools.Fonts.setFontSize;


/**
 * Created by Chris Cavazos on 8/14/2017.
 */
public class MultiplayerState extends State {
    private ShapeRendererExt sr = new ShapeRendererExt();
    private ArrayList<String> output = new ArrayList<>();
    private static Triangle[] otherPlayers = new Triangle[0];
    boolean init;

    public MultiplayerState(GameStateManager gsm) {
        super(gsm);
        init = true;
    }

    void init() {
        Command.cls = MultiplayerState.class;
        output.clear();
        sr.setAutoShapeType(true);
        setFontSize(10);
        init=false;
        AsteroidState.startGame();
    }

    @Override
    protected void handleInput() {
        AsteroidState.input();
    }

    @Override
    public void update(float dt) {
        handleInput();
        if (AsteroidState.inGame()) {
            AsteroidState.updateLoop(dt);
            sendLocation();
        }
        if(init)
            init();
        requestData();
    }

    private void requestData() {
        LobbyState.comm.emit("requestData",LobbyState.comm.room);

    }
    public static void setOtherPlayers(Triangle[] p){
        otherPlayers=p;
    }
    DecimalFormat df= new DecimalFormat(".000");
    private void sendLocation() {
        Vector2 v=AsteroidState.player.getPos();
        float a=AsteroidState.player.getAngle();
        String s=df.format(v.x/ Game.res.x)+" "+df.format(v.y/Game.res.y)+" "+df.format((a%360)/1000);
        LobbyState.comm.emit("sendPos",LobbyState.comm.room,s);
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl20.glClearColor(0, 0, 0, 1);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        if (AsteroidState.inGame()) {
            sr.begin(ShapeRenderer.ShapeType.Filled);
            drawOtherPlayers(sr);
            sr.end();
            AsteroidState.drawScene(sb, sr);

        }
    }


    Color[] oc=new Color[]{Color.RED,Color.GREEN,Color.BLUE,Color.ORANGE};
    private void drawOtherPlayers(ShapeRendererExt sr) {
        for (int i=0;i<otherPlayers.length;i++){
            sr.setColor(oc[i]);
            Triangle t=otherPlayers[i];
            if (t != null && t.getPoints() != null)
                sr.triangle(t);
        }
    }

    @Override
    public void dispose() {

    }
}
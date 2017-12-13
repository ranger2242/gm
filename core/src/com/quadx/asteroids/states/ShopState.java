package com.quadx.asteroids.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.command.Command;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.tools.Fonts;
import com.quadx.asteroids.tools.Game;
import com.quadx.asteroids.tools.ShopModule;

import java.util.ArrayList;

import static com.badlogic.gdx.graphics.GL20.GL_BLEND;
import static com.quadx.asteroids.tools.Game.*;
import static com.quadx.asteroids.tools.Text.strWidth;

/**
 * Created by Chris on 12/3/2017.
 */

public class ShopState extends State {
    private final ArrayList<ShopModule> modules = new ArrayList<>();
    private final ShapeRendererExt sr= new ShapeRendererExt();
    private int edge = 100;
    private final ShopModule laserSM = new ShopModule("Laser",0,new Vector2(WIDTH*.3f,HEIGHT*.3f));
    private final ShopModule bulletSM = new ShopModule("Bullet",0,new Vector2(WIDTH*.3f,HEIGHT*.4f));
    private final ShopModule cbombSM = new ShopModule("CBomb",0,new Vector2(WIDTH*.3f,HEIGHT*.5f));


    public ShopState(GameStateManager gsm) {
        super(gsm);
        sr.setAutoShapeType(true);
        modules.add(bulletSM);
        modules.add(laserSM);
        modules.add(cbombSM);
        hud.addShopComponents(modules);
        hud.setSelected(AsteroidState.player.getActivePowers());
        for(ShopModule m:modules) {
            hud.addToStage(m.getActor());
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.TAB)){
            hud.removeShopComponents();
            AsteroidState.player.setPowerUp(hud.getSelected());
            AsteroidState.nextWave();
            gsm.pop();
            Command.cls=gsm.peek().getClass();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glEnable(GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//start draw
        sr.begin(ShapeRenderer.ShapeType.Filled);
        float scl= 1.4f;

        sr.setColor(.01f,.01f,.3f,.02f);
        sr.rect(edge* Game.scl.x,edge* Game.scl.y,WIDTH-(2*edge* Game.scl.x),HEIGHT-(2*edge* Game.scl.y));
        sr.setColor(.01f,.01f,.3f,.04f);

        sr.rect((edge*scl)* Game.scl.x,(edge*scl)* Game.scl.y,WIDTH-(2*(edge*scl)* Game.scl.x),HEIGHT-(2*(edge*scl)* Game.scl.y));

        Vector2 t= new Vector2((edge+20)* Game.scl.x,HEIGHT-((edge+25)* Game.scl.y));
        sr.set(ShapeRenderer.ShapeType.Line);
        Fonts.setFontSize(18);
        titleLine("SHOP",t);
        sr.setColor(.1f,.1f,.4f,.05f);
        sr.rect(edge* Game.scl.x,edge* Game.scl.y,WIDTH-(2*edge* Game.scl.x),HEIGHT-(2*edge* Game.scl.y));
        sr.rect((edge*scl)* Game.scl.x,(edge*scl)* Game.scl.y,WIDTH-(2*(edge*scl)* Game.scl.x),HEIGHT-(2*(edge*scl)* Game.scl.y));
        sr.end();
        Gdx.gl.glDisable(GL_BLEND);
        sb.begin();
        Fonts.getFont().draw(sb,"SHOP",t.x,t.y);
        for(ShopModule m:modules){
            m.render(sb);
        }
        sb.end();
    }
    private float[] fitLineToWord(String s){
        //x1,y1,x2,y2
        float[] arr=new float[8];
        arr[0]= -10;
        arr[1]=-15;
        arr[2]=strWidth(s)+20;
        arr[3]=-15;
        arr[4]= -10;
        arr[5]=-12;
        arr[6]=strWidth(s)+35;
        arr[7]=-12;
        return arr;
    }
    private void titleLine(String st, Vector2 t){
        float[] s = fitLineToWord(st);
        float x=t.x;
        float y=t.y;
        sr.setColor(Color.WHITE);
        sr.line( x + s[0], y+ s[1] ,x + s[2], y+ s[3]);
        sr.line( x + s[4], y+ s[5] ,x + s[6], y+ s[7]);
    }
    @Override
    public void dispose() {

    }
}

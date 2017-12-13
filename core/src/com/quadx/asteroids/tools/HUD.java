package com.quadx.asteroids.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.quadx.asteroids.asteroids.powerups.Bullet;
import com.quadx.asteroids.asteroids.powerups.CBomb;
import com.quadx.asteroids.asteroids.powerups.Laser;
import com.quadx.asteroids.asteroids.powerups.Powerup;

import java.util.ArrayList;

import static com.quadx.asteroids.tools.Game.*;

/**
 * Created by Chris on 12/2/2017.
 */

public class HUD {
    private final ArrayList<HUDComponent> components = new ArrayList<>();
    private final ArrayList<Vector2> touches = new ArrayList<>();
    private final Stage stage;
    private ButtonGroup<com.badlogic.gdx.scenes.scene2d.ui.Button> group;


    public HUD(SpriteBatch sb){

        //Create a Stage and add TouchPad
        ScreenViewport viewport = new ScreenViewport();

        stage = new Stage(viewport,sb);


        if(mode == Game.Mode.ANDROID) {
            TouchJoystick joy1 = new TouchJoystick();
            float s=150*sclf;
            Button b = new Button("button.pack", new Vector2(res.x *.7f, res.y*.1f), new Vector2(s, s));
            Button b2 = new Button("button.pack", new Vector2(res.x *.85f, res.y*.1f), new Vector2(s, s));
            Button b3 = new Button("button.pack", new Vector2(res.x *.85f, res.y*.30f), new Vector2(s, s));

            components.add(joy1);
            components.add(b);
            components.add(b2);
            components.add(b3);
        }

        Gdx.input.setInputProcessor(stage);
        for(HUDComponent obj: components){
            stage.addActor(obj.getActor());
        }

    }
    public void addToStage(Actor a){
        stage.addActor(a);
    }

    public void update(float dt){
        touches.clear();
        setTouches();
        for(HUDComponent obj: components){
            obj.update(dt);
        }
    }

    private void setTouches() {
        for(int i=0;i<3;i++){
            if(Gdx.input.isTouched(i))
                touches.add(new Vector2(Gdx.input.getX(i),Gdx.input.getY(i)));
            else
                touches.add(new Vector2(-999,-999));

        }
    }

    public void render(SpriteBatch sb){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        sb.begin();
        for(HUDComponent c: components){
            c.render(sb);
        }
        sb.end();
    }

    public Button getMainButton() {
        return (Button) components.get(1);
    }

    public TouchJoystick getTouchJoyStick(){
        return (TouchJoystick) components.get(0);
    }
    public void addShopComponents(ArrayList<ShopModule> modules){
        group= new ButtonGroup<>();
        group.setMaxCheckCount(1);
        group.setMinCheckCount(1);
        group.setUncheckLast(true);
        for(ShopModule m: modules){
            com.badlogic.gdx.scenes.scene2d.ui.Button b= (com.badlogic.gdx.scenes.scene2d.ui.Button) m.getActor();
            group.add(b);
        }
        components.addAll(modules);
    }

    public void removeShopComponents() {
        for(int i=components.size()-1;i>=0;i--){
            if(components.get(i) instanceof ShopModule){
                ((ShopModule) components.get(i)).dispose();
                stage.getActors().removeIndex(i);
                components.remove(i);
            }
        }
    }

    public Powerup getSelected() {
        switch (group.getCheckedIndex()){
            case 0:
                return new Bullet();
            case 1:
                return new Laser();
            case 2:
                return new CBomb();
        }
        return null;
    }

    public void setSelected(int[] selected) {
        for(int i=0;i<selected.length;i++) {
            ((com.badlogic.gdx.scenes.scene2d.ui.Button)components.get(selected[i]).getActor()).setChecked(true);
        }
    }

    public Button getSwapButton() {
        return (Button) components.get(2);
    }
    public Button getIncButton() {
        return (Button) components.get(3);
    }

}

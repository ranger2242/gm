package com.quadx.asteroids.asteroids.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.quadx.asteroids.anims.RechargeAnim;
import com.quadx.asteroids.asteroids.Asteroid;
import com.quadx.asteroids.asteroids.Ship;
import com.quadx.asteroids.shapes1_2.ShapeRendererExt;
import com.quadx.asteroids.tools.Delta;

import static com.quadx.asteroids.tools.Game.ft;

/**
 * Created by Chris Cavazos on 12/6/2017.
 */
@SuppressWarnings("WeakerAccess")
public abstract class Powerup {
    protected float lim=0;
    protected int index= -1;
    protected int price= 0;
    protected int level=0;
    protected int maxlevel=0;

    protected int[] cost= null;
    protected Delta dtUse= new Delta(ft);
    protected Texture icon;
    protected RechargeAnim dtrechargeAnim = new RechargeAnim(0, new Vector2(-999, -999));
    protected Delta dtDeath = new Delta(30*ft);
    protected Delta dtUpgrade = new Delta(10*ft);
    protected String name ="";
    public abstract void use(Ship s);
    public abstract void update(float dt);
    public abstract void render(ShapeRendererExt sr);
    public abstract boolean collide(Asteroid a);
    public abstract void upgrade();

    public void loadTexture(String s){
        icon=new Texture(Gdx.files.internal("hud/button/shop/"+s+".png"));

    }
    public boolean canUse(){
        return dtUse.isDone();
    }

    public float getRechargeTime() {
        return lim;
    }

    public int getIndex() {
        return index;
    }

    public Texture getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean isUpgradable(){
        return level<maxlevel;
    }
    public int getLevel() {
        return level;
    }
}

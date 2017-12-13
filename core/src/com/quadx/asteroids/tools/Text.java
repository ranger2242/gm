package com.quadx.asteroids.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import static com.quadx.asteroids.tools.Game.WIDTH;

/**
 * Created by range_000 on 1/5/2017.
 */
public class Text {
    private int size;
    private Color c;
    private Vector2 pos;
    private String text;
    private Text(String s,Vector2 v,Color c, int size){
        text=s;
        pos=v;
        this.c=c;
        this.size=size;
    }
    public static float strWidth(String s){
        GlyphLayout gl = new GlyphLayout();

        gl.setText(Fonts.getFont(), s);
        return gl.width;
    }
    public void render(SpriteBatch sb){
        sb.setColor(c);
        Fonts.setFontSize(size);
        Fonts.getFont().draw(sb,text,pos.x,pos.y);
    }
    public static float centerString(String s){
        return (WIDTH/2)- (strWidth(s)/2);
    }

}

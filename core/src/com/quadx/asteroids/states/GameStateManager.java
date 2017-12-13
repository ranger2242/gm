package com.quadx.asteroids.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.asteroids.command.Command;

import java.util.Stack;


@SuppressWarnings("unused")
public class GameStateManager {

    private final Stack<State> states;

    public GameStateManager(){
        states = new Stack<>();
    }

    public void push(State state){
        Command.cls=state.getClass();
        states.push(state);
    }
    public void clear(){
        states.clear();
    }
    public void pop(){
        states.pop().dispose();

    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);

    }

    public void render(SpriteBatch sb){
        states.peek().render(sb);
    }

    public State peek() {
        return states.peek();
    }
}

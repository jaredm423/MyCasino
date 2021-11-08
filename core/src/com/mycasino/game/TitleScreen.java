package com.mycasino.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;

public class TitleScreen extends ScreenAdapter {
    CasinoGame game;

    public TitleScreen(CasinoGame game) {
        this.game = game;
    }
    //this will be input listener for switching to game screen
    /*@Override
    public void show(){
        Gdx.input.setInputProcessor((InputAdapter) touchDown(screenX, screenY, pointer, button){
            game.setScreen(new GameScreen(game));
            return true;
        });
    }*/
    @Override
    public void render(float d){
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to the Casino!", 150, 680);
        game.batch.end();
    }
}

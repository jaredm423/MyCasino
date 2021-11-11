package com.mycasino.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BlackJack extends ScreenAdapter {
    CasinoGame game;

    Camera camera;
    Viewport viewport;

    public BlackJack(CasinoGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(game.screenWidth, game.screenHeight, camera);
    }
    @Override
    public void render(float d){
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.font.draw(game.batch,"Blackjack",  -150, 550 );
        game.batch.end();
    }
}

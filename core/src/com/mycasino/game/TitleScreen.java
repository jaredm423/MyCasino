package com.mycasino.game;

import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.touchDown;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TitleScreen extends ScreenAdapter {
    CasinoGame game;
    String title = "Welcome to the Casino!";

    Camera camera;
    Viewport viewport;

    //touchregion for button1
    float touchX1 = 200;
    float touchY1 = 700;
    //button2
    float touchX2 = 200;
    float touchY2 = 500;
    //button3
    float touchX3 = 200;
    float touchY3 = 300;
    //touch "radius"
    float touchZone;

    public TitleScreen(CasinoGame game) {
        this.game = game;
        camera = new OrthographicCamera(720, 1280);
        viewport = new StretchViewport(game.screenWidth, game.screenHeight, camera);
    }
    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputAdapter()
        {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button)
            {
                if((int)touchX1 < screenX && screenX < (int)touchX1 + 200)
                {
                    if((int)touchY1 - 200 < screenY && screenY < (int)touchY1 - 100)
                    {
                        game.setScreen(new BallDrop(game));
                        return true;
                    }

                 }
                if((int)touchX1 < screenX && screenX < (int)touchX1 + 200)
                {
                    if((int)touchY1 < screenY && screenY < (int)touchY1 + 100 )
                    {
                        game.setScreen(new BallDrop(game));
                        return true;
                    }

                }
                if((int)touchX1 < screenX && screenX < (int)touchX1 + 200)
                {
                    if((int)touchY1 + 200 < screenY && screenY < (int)touchY1 + 300)
                     {
                         game.setScreen(new BallDrop(game));
                         return true;
                     }

                }
                return false;
             }
         });
    }

    @Override
    public void render(float d){
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        GlyphLayout textSize = new GlyphLayout();
        textSize.setText(game.font, title);
        float x = game.screenWidth/2 - textSize.width/2;
        float y = game.screenHeight/2 - textSize.height/2;

        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.getData().setScale(5);
        game.font.draw(game.batch, "Welcome to the Casino!", 0, 800);
        game.batch.end();

        game.shapeRenderer.setColor(Color.WHITE);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.rect(touchX1, touchY1, 300, 100);
        game.shapeRenderer.rect(touchX2, touchY2, 300, 100);
        game.shapeRenderer.rect(touchX3, touchY3, 300, 100);
        game.shapeRenderer.end();


    }

}

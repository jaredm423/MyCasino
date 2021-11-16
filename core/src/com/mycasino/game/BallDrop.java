package com.mycasino.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BallDrop extends ScreenAdapter {

    CasinoGame game;

    Camera camera;
    Viewport viewport;

    Sprite fallBall;
    Texture img;

    World world;
    Body ballBody;
    Body dotBody;
    Body ground;

    Box2DDebugRenderer debugRenderer;

    public BallDrop(CasinoGame game) {
        this.game = game;
        game.playerMoney = game.playerMoney - 10;
        Box2D.init();
        debugRenderer = new Box2DDebugRenderer();

        //part1 implementation
        camera = new OrthographicCamera();
        viewport = new StretchViewport(game.screenWidth, game.screenHeight, camera);
        img = new Texture("fall_ball.png");
        fallBall = new Sprite(img);

        //pulling all of this as a sample to learn about physics in libgdx

        fallBall.setPosition(Gdx.graphics.getWidth() / 2 - fallBall.getWidth() / 2,
                Gdx.graphics.getHeight() / 2);

        world = new World(new Vector2(0, -98), true);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(fallBall.getX(), fallBall.getY());

        ballBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(fallBall.getWidth()/2, fallBall.getHeight());

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = ballBody.createFixture(fixtureDef);
        //end reference code for constructor
        shape.dispose();
    }
    //phase 2
    final float STEP_TIME = 1f / 60f;
    final int VELOCITY_ITERATIONS = 6;
    final int POSITION_ITERATIONS = 2;

    float accumulator = 0;

    private void stepWorld(){
        float delta = Gdx.graphics.getDeltaTime();

        accumulator += Math.min(delta, 0.25f);

        if(accumulator >= STEP_TIME){
            accumulator -= STEP_TIME;

            world.step(STEP_TIME, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }
    private void createDots(){

    }
    private void createGround(){
        if(ground != null) world.destroyBody(ground);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(camera.viewportWidth, 1);

        fixtureDef.shape = shape;

        ground = world.createBody(bodyDef);
        ground.createFixture(fixtureDef);
        ground.setTransform(0,0,0);

        shape.dispose();
    }
    @Override
    public void render(float d){
        Gdx.gl.glClearColor(0,0,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stepWorld();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //this is the base rectangle that will feature multipliers...
        game.shapeRenderer.rect(0,400, camera.viewportWidth, 20);
        //this is the button for redo
        game.shapeRenderer.rect(250, 200, 200, 50);
        game.shapeRenderer.end();

        game.batch.begin();
        fallBall.setPosition(ballBody.getPosition().x, ballBody.getPosition().y);
        drawSprite();
        backgroundSetup();
        game.batch.end();


        //debugRenderer.render(world, camera.combined);
    }
    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                 if(250 < screenX && screenX < 450){
                   if(1025 < screenY && screenY < 1075) {
                       game.setScreen(new BallDrop(game));
                       return true;
                   }
                 }
                return false;
            }
            });
    }
    private void backgroundSetup(){
        String moneyCt = String.valueOf(game.playerMoney);
        game.font.draw(game.batch,"BallDrop!",  250, 1200 );
        game.font.getData().setScale(3);
        game.font.draw(game.batch, "Available funds: $" + moneyCt, 140, 175);
    }
    //this will be used in onclick when button clicked, draw ball and make it fall - simple stuff
    private void drawSprite(){
        fallBall.setPosition(fallBall.getX(), fallBall.getY() + 400);
        fallBall.draw(game.batch);
    }
    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);
        createGround();
    }
    @Override
    public void dispose(){
        img.dispose();
        world.dispose();
        debugRenderer.dispose();
    }
}

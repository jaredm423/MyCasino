package com.mycasino.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.w3c.dom.css.Rect;

public class BallDrop extends ScreenAdapter {

    CasinoGame game;
    int playerMoney;

    Camera camera;
    Viewport viewport;

    Sprite fallBall;
    Texture img;

    World world;
    Body ballBody;
    Body dotBody;
    Body ground;
    boolean Colliding;

    Box2DDebugRenderer debugRenderer;

    public BallDrop(CasinoGame game, int playerMoney) {
        this.game = game;
        this.playerMoney = playerMoney - 10;
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
        contactSetup();
        setupBall();
        createDots();

    }
    private void contactSetup(){
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if(contact.getFixtureA().getBody().getUserData() == "ballBody" &&
                        contact.getFixtureB().getBody().getUserData() == "dotBody") {
                    Colliding = true;
                }

            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                //auto generated
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                //auto generated
            }
        });
    }
    private void setupBall(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(fallBall.getX(), fallBall.getY() + 400);

        ballBody = world.createBody(bodyDef);
        ballBody.setUserData("ballBody");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(fallBall.getWidth()/2, fallBall.getHeight() / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;

        Fixture fixture = ballBody.createFixture(fixtureDef);
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
        if(dotBody != null) world.destroyBody(dotBody);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10, 10);

        fixtureDef.shape = shape;

        dotBody = world.createBody(bodyDef);
        dotBody.createFixture(fixtureDef);
        dotBody.setTransform(350,500,0);
        dotBody.setUserData("dotBody");

        shape.dispose();
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
        ground.setTransform(0,410,0);

        shape.dispose();
    }
    @Override
    public void render(float d){
        Gdx.gl.glClearColor(0,0,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stepWorld();
        if(Colliding){
            ballBody.applyForce((float)Math.random() * 10,100, ballBody.getPosition().x, ballBody.getPosition().y, true);
        }
        game.font.setColor(Color.WHITE);

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
        //unccoment for debugMode
        debugRenderer.render(world, camera.combined);
    }
    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                 if(250 < screenX && screenX < 450){
                   if(1025 < screenY && screenY < 1075) {
                       game.setScreen(new BallDrop(game, playerMoney));
                       return true;
                   }
                 }
                return false;
            }
            });
    }
    private void backgroundSetup(){
        String moneyCt = String.valueOf(playerMoney);
        game.font.draw(game.batch,"BallDrop!",  250, 1200 );
        game.font.getData().setScale(3);
        game.font.draw(game.batch, "Available funds: $" + moneyCt, 140, 175);
    }
    //this will be used in onclick when button clicked, draw ball and make it fall - simple stuff
    private void drawSprite(){
        fallBall.setPosition(fallBall.getX() - 10, fallBall.getY() - 10);

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

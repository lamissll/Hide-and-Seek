package com.steve.flames.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.steve.flames.HaSGame;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Flames on 10/4/16.
 */
public class MenuScreen implements Screen, InputProcessor {

    private static float BUTTON_WIDTH = 220;
    private static float BUTTON_HEIGHT = 100;

    private float BTN12X;
    private float BTN34X;
    private float BTN13Y;
    private float BTN24Y;

    //public final static float resolutionFactorX = (Gdx.graphics.getWidth()/800.0f);
    //public final static float resolutionFactorY = (Gdx.graphics.getHeight()/480.0f);

    private HaSGame game;
    private Timer timer;

    private ShapeRenderer sr;

    private Stage stage;
    private Viewport viewport;

    private int menuChoice;
    private Texture bg;
    private Texture hostT;
    private Texture joinT;
    private Rectangle hostRect;
    private Rectangle joinRect;

    private Vector3 coords;


    public MenuScreen(HaSGame game) {
        this.game = game;
        initCoords();
        menuChoice = 0;

        sr = new ShapeRenderer();
        coords = new Vector3();

        bg = new Texture("welcome_screen.png");
        hostT = new Texture("hostBtn.png");
        joinT = new Texture("joinBtn.png");

        hostRect = new Rectangle((int)BTN12X, (int)BTN13Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        joinRect = new Rectangle((int)BTN34X, (int)BTN24Y, BUTTON_WIDTH, BUTTON_HEIGHT);

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
    }

    private void initCoords() {
        //BTN12X =  50;
        //BTN13Y = 50;
        BTN12X = game.cam.viewportWidth/2 - BUTTON_WIDTH/2 ;
        BTN34X = game.cam.viewportWidth/2 - BUTTON_WIDTH/2;
        BTN13Y = game.cam.viewportHeight/2 + 30;
        BTN24Y = game.cam.viewportHeight/2 - 120;
    }

    @Override
    public void show() {

    }

    public void update() {
        if(menuChoice == 1) {
            dispose();
            game.setScreen(new HostScreen(game));
        }
        else if (menuChoice == 2) {
            dispose();
            game.setScreen(new JoinScreen(game));
        }
    }

    @Override
    public void render(float delta) {//clear game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(game.cam.combined);

        //game.batch.setProjectionMatrix(stage.getCamera().combined);
        //stage.draw();
        //System.out.println(game.btm.isConnected());
        //game.batch.setProjectionMatrix(game.cam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 0, 800, 480);
        game.batch.draw(hostT, BTN12X, BTN13Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        game.batch.draw(joinT, BTN34X, BTN24Y, BUTTON_WIDTH, BUTTON_HEIGHT);
        game.batch.end();

        /*sr.begin(ShapeRenderer.ShapeType.Line);
        sr.rect(hostRect.x, hostRect.y, hostRect.width, hostRect.height);
        sr.rect(joinRect.x, joinRect.y, joinRect.width, joinRect.height);
        sr.end();*/

        update();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        joinT.dispose();
        hostT.dispose();
        bg.dispose();
        sr.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.ESCAPE || keycode == Input.Keys.BACK){
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        coords.set(screenX, screenY, 0);
        game.cam.unproject(coords);

        if (new Rectangle(coords.x, coords.y, 2, 2).overlaps(hostRect)) {
            game.wfm.resetData();
            game.wfm.disconnect();
            game.wfm.discoverDevices();
            timer = new Timer(true);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    game.wfm.createGroup();
                    timer.cancel();
                }
            }, 2000, 2000);
            menuChoice = 1;
        } else if (new Rectangle(coords.x, coords.y, 2, 2).overlaps(joinRect)) {
            game.wfm.resetData();
            game.wfm.disconnect();
            game.wfm.discoverDevices();
            menuChoice = 2;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

package com.seana02;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class Main extends ApplicationAdapter {
	String title = "Pong!";
	static final int WINDOW_WIDTH = 1280;
	static final int WINDOW_HEIGHT = 720;

	static final int VIRTUAL_WIDTH = 432;
	static final int VIRTUAL_HEIGHT = 243;

	SpriteBatch batch;
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter size8;
	FreeTypeFontParameter size16;
	FreeTypeFontParameter size32;
	FreeTypeFontParameter fpsParam;
	BitmapFont smallFont;
	BitmapFont largeFont;
	BitmapFont scoreFont;
	BitmapFont fpsFont;
	ShapeRenderer shapeRenderer;
	StretchViewport vp;
	OrthographicCamera cam;
	int player1Score;
	int player2Score;
	public enum GameState {
		START, PLAY, SERVE, DONE
	}
	GameState gameState;
	Ball ball;
	Paddle player1;
	Paddle player2;
	int servingPlayer;
	int winningPlayer;
	Sound paddleHit;
	Sound score;
	Sound wallHit;

	final int PADDLE_SPEED = 200;
	final float SPEEDUP_RATE = 1.03f;
	final int WIN_SCORE = 10;
	
	@Override
	public void create() {
		batch = new SpriteBatch();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));

		size8 = new FreeTypeFontParameter();
		size8.size = 8;
		smallFont = generator.generateFont(size8);

		size16 = new FreeTypeFontParameter();
		size16.size = 16;
		largeFont = generator.generateFont(size16);

		size32 = new FreeTypeFontParameter();
		size32.size = 32;
		scoreFont = generator.generateFont(size32);

		fpsParam = new FreeTypeFontParameter();
		fpsParam.size = 8;
		fpsParam.color = Color.GREEN;
		fpsFont = generator.generateFont(fpsParam);

		paddleHit = Gdx.audio.newSound(Gdx.files.internal("paddle_hit.wav"));
		score = Gdx.audio.newSound(Gdx.files.internal("score.wav"));
		wallHit = Gdx.audio.newSound(Gdx.files.internal("wall_hit.wav"));
		
		shapeRenderer = new ShapeRenderer();

		cam = new OrthographicCamera(WINDOW_WIDTH, WINDOW_HEIGHT);
		cam.setToOrtho(false);
		vp = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, cam);
		vp.apply();
		cam.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2, 0);
		cam.update();

		player1Score = 0;
		player2Score = 0;
		
		/*
		player1Y = 30;
		player2Y = VIRTUAL_HEIGHT - 50;
		*/
		player1 = new Paddle(10, 30, 5, 20);
		player2 = new Paddle(VIRTUAL_WIDTH - 10, VIRTUAL_HEIGHT - 30, 5, 20);

		/*
		ballX = VIRTUAL_WIDTH/2 - 2;
		ballY = VIRTUAL_HEIGHT/2 -2;

		ballDX = ((int) Math.random()*2) == 1 ? 100 : -100;
		ballDY = (int) Math.random() * 50;
		*/
		ball = new Ball(VIRTUAL_WIDTH/2 - 2, VIRTUAL_HEIGHT/2 - 2, 4, 4);

		gameState = GameState.START;
		servingPlayer = 1;
		winningPlayer = 0;

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keyCode) {
				if (keyCode == Input.Keys.ESCAPE) {
					Gdx.app.exit();
					System.exit(0);
				} else if (keyCode == Input.Keys.ENTER) {
					if (gameState == GameState.START) {
						gameState = GameState.SERVE;
					} else if (gameState == GameState.SERVE) {
						gameState = GameState.PLAY;

						ball.reset();
					} else if (gameState == GameState.DONE) {
						gameState = GameState.SERVE;
						ball.reset();
						player1Score = 0;
						player2Score = 0;

						if (winningPlayer == 1) {
							servingPlayer = 2;
						} else {
							servingPlayer = 1;
						}
					}
				}
				return true;
			}
		});
	}

	@Override
	public void render() {

		
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			player1.dy = PADDLE_SPEED;
		} else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			player1.dy = -PADDLE_SPEED;
		} else {
			player1.dy = 0;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
			player2.dy = PADDLE_SPEED;
		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			player2.dy = -PADDLE_SPEED;
		} else {
			player2.dy = 0;
		}
		

		if (gameState == GameState.SERVE) {
			ball.setDY((float)Math.random()*100 - 50);
			if (servingPlayer == 1) {
				ball.setDX((float)Math.random()*60 + 140);
			} else {
				ball.setDX((float)Math.random()*60 - 200);
			}
		} else if (gameState == GameState.PLAY) {
			/*
			ballX += ballDX * Gdx.graphics.getDeltaTime();
			ballY += ballDY * Gdx.graphics.getDeltaTime();
			*/
			if (ball.collides(player1)) {
				ball.setDX(-ball.getDX() * SPEEDUP_RATE);
				ball.setX(player1.getX() + 5);
				if (ball.getDY() < 0) {
					ball.setDY((float)(Math.random()*140 - 150));
				} else {
					ball.setDY((float)(Math.random()*140 + 10));
				}
				paddleHit.play();
			}
			if (ball.collides(player2)) {
				ball.setDX(-ball.getDX() * SPEEDUP_RATE);
				ball.setX(player2.getX() - 4);
				if (ball.getDY() < 0) {
					ball.setDY((float)(Math.random()*140 - 150));
				} else {
					ball.setDY((float)(Math.random()*140 + 10));
				}
				paddleHit.play();
			}

			if (ball.getY() <= 0) {
				ball.setY(0);
				ball.setDY(-ball.getDY());
				wallHit.play();
			}
			if (ball.getY() >= VIRTUAL_HEIGHT - 4) {
				ball.setY(VIRTUAL_HEIGHT - 4);
				ball.setDY(-ball.getDY());
				wallHit.play();
			}
			ball.update(Gdx.graphics.getDeltaTime());
		}

		if (ball.getX() < 0) {
			servingPlayer = 1;
			player2Score++;
			score.play();
			ball.reset();

			if (player2Score == WIN_SCORE) {
				winningPlayer = 2;
				gameState = GameState.DONE;
			} else {
				gameState = GameState.SERVE;
			}
		}
		if (ball.getX() > VIRTUAL_WIDTH) {
			servingPlayer = 2;
			player1Score++;
			ball.reset();
			
			if (player1Score == WIN_SCORE) {
				winningPlayer = 1;
				gameState = GameState.DONE;
			} else {
				gameState = GameState.SERVE;
			}
		}
		
		player1.update(Gdx.graphics.getDeltaTime());
		player2.update(Gdx.graphics.getDeltaTime());

		ScreenUtils.clear(40f/255, 45f/255, 52f/255, 1);
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		if (gameState == GameState.START) {
			smallFont.draw(batch, "Welcome to Pong!", VIRTUAL_WIDTH/2 - 40, VIRTUAL_HEIGHT - 10);
			smallFont.draw(batch, "Press Enter to begin!", VIRTUAL_WIDTH/2 - 40, VIRTUAL_HEIGHT - 20);
		} else if (gameState == GameState.SERVE) {
			smallFont.draw(batch, "Player " + servingPlayer + "'s serve!", VIRTUAL_WIDTH/2 - 40, VIRTUAL_HEIGHT - 10);
			smallFont.draw(batch, "Press Enter to serve!", VIRTUAL_WIDTH/2 - 40, VIRTUAL_HEIGHT - 20);
		} else if (gameState == GameState.DONE) {
			largeFont.draw(batch, "Player " + winningPlayer + " wins!", VIRTUAL_WIDTH/2 - 40, VIRTUAL_HEIGHT - 10);
			smallFont.draw(batch, "Press Enter to restart!", VIRTUAL_WIDTH/2 - 40, VIRTUAL_HEIGHT - 30);
		}

		scoreFont.draw(batch, Integer.toString(player1Score), VIRTUAL_WIDTH/2-50, VIRTUAL_HEIGHT/3);
		scoreFont.draw(batch, Integer.toString(player2Score), VIRTUAL_WIDTH/2+50, VIRTUAL_HEIGHT/3);
		fpsFont.draw(batch,Float.toString(Gdx.graphics.getFramesPerSecond()), 10, VIRTUAL_HEIGHT - 10);
		//fpsFont.draw(batch, "X:" + ball.getX() + " Y:" + ball.getY(), 10, VIRTUAL_HEIGHT - 30);
		batch.end();

		shapeRenderer.setProjectionMatrix(cam.combined);
		shapeRenderer.begin(ShapeType.Filled);
		//shapeRenderer.rect(10, player1Y, 5, 20);
		player1.render(shapeRenderer);
		//shapeRenderer.rect(VIRTUAL_WIDTH-10, player2Y, 5, 20);
		player2.render(shapeRenderer);
		//shapeRenderer.rect(ballX, ballY, 4, 4);
		ball.render(shapeRenderer);
		shapeRenderer.end();

	}
	
	@Override
	public void dispose() {
		batch.dispose();
		generator.dispose();
		shapeRenderer.dispose();
		
		smallFont.dispose();
		scoreFont.dispose();
		fpsFont.dispose();
		
		paddleHit.dispose();
		wallHit.dispose();
		score.dispose();
	}


	public String getTitle() { return title; }
	public int getWidth() { return WINDOW_WIDTH; }
	public int getHeight() { return WINDOW_HEIGHT; }
}

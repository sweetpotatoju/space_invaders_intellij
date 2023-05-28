package spaceinvaders.entity;

import spaceinvaders.Game;
import spaceinvaders.Sprite;
import spaceinvaders.SpriteStore;
import spaceinvaders.SystemTimer;

import java.util.List;
import java.util.TimerTask;

/**
 * An entity which represents one of our space invader aliens.
 *
 * @author Kevin Glass
 */
public class AlienEntity extends Entity {
	/** The speed at which the alient moves horizontally */
	/** The game in which the entity exists */
	private static Game game;
	/** The animation frames */
	private Sprite[] frames = new Sprite[4];
	/** The time since the last frame change took place */
	private long lastFrameChange;
	/** The frame duration in milliseconds, i.e. how long any given frame of animation lasts */
	private long frameDuration = 250;
	/** The current frame of animation being displayed */
	private int frameNumber;
	private static double initHSpeed=75;
	private static double initVSpeed=10;
	private static TimerTask taskInvasion;

	/**
	 * Create a new alien entity
	 *
	 * @param game The game in which this entity is being created
	 * @param x The intial x location of this alien
	 * @param y The intial y location of this alient
	 */
	public AlienEntity(Game game, int x, int y) {
		super("sprites/ufoo1.png", x, y);

		// setup the animatin frames
		frames[0] = sprite;
		frames[1] = SpriteStore.get().getSprite("sprites/ufoo2.png");
		frames[2] = sprite;
		frames[3] = SpriteStore.get().getSprite("sprites/ufoo3.png");
		this.game = game;
		int coinToss=(int)Math.round(Math.random());
		if(coinToss ==0){
			dx = -initHSpeed;
		}
		else{
			dx = initHSpeed;
		}
		dy = initVSpeed;
	}

	public void createLevel2Alien(Game game, int x, int y) {
		// Create a new alien entity
		AlienEntity level2Alien = new AlienEntity(game, x, y);

		frames[0] = SpriteStore.get().getSprite("sprites/ufoo1.png");
		frames[1] = SpriteStore.get().getSprite("sprites/ufoo2.png");
		frames[2] = SpriteStore.get().getSprite("sprites/ufoo1.png");
		frames[3] = SpriteStore.get().getSprite("sprites/ufoo3.png");
		this.game = game;
		dx = -getHorizontalMovement();
		dy = getVerticalMovement();
		// Customize the attributes of the level 2 alien

		// Add the level 2 alien to the gam
	}


	/**
	 * Request that this alien moved based on time elapsed
	 *
	 * @param delta The time that has elapsed since last move
	 */
	public void move(long delta) {
		// since the move tells us how much time has passed
		// by we can use it to drive the animation, however
		// its the not the prettiest solution
		lastFrameChange += delta;

		// if we need to change the frame, update the frame number
		// and flip over the sprite in use
		if (lastFrameChange > frameDuration) {
			// reset our frame change time counter
			lastFrameChange = 0;

			// update the frame
			frameNumber++;
			if (frameNumber >= frames.length) {
				frameNumber = 0;
			}

			sprite = frames[frameNumber];
		}

		// if we have reached the left hand side of the screen and
		// are moving left then request a logic update
		if ((dx < 0) && (x < 10)) {
			game.updateLogic();
		}
		// and vice vesa, if we have reached the right hand side of
		// the screen and are moving right, request a logic update
		if ((dx > 0) && (x > 750)) {
			game.updateLogic();
		}

		// proceed with normal move
		super.move(delta);
	}

	/**
	 * Update the game logic related to aliens
	 */
	public void doLogic() {
		// swap over horizontal movement and move down the
		// screen a bit
		dx = -dx;
		y += 10;

		// if we've reached the bottom of the screen then the player
		// dies
		if (y > 570) {
			game.notifyRetire();
		}
	}

	/**
	 * Notification that this alien has collided with another entity
	 *
	 * @param other The other entity
	 */
	public void collidedWith(Entity other) {
		// collisions with aliens are handled elsewhere
	}

	public static void setAlienHMovement(double hSpeed){
		initHSpeed=hSpeed;
	}public static void setAlienVMovement(double vSpeed){
		initVSpeed=vSpeed;
	}
	public static double getAlienHMovement(){
		return initHSpeed;
	}

	public static double getAlienVMovement(){
		return initVSpeed;
	}

	//need to fix logic
	public static void alienInvasion() {
		if(game.isTaskExist(taskInvasion)!=null)return;
		double originHoriSpeed = getAlienHMovement();
		double originVertSpeed = getAlienVMovement();
		long startTimer = SystemTimer.getTime();
		double targetHoriSpeed = originHoriSpeed * 2;
		double targetVertSpeed = originVertSpeed * 2;
		taskInvasion = new TimerTask() {

			long durationTimer = SystemTimer.getTime();

			@Override
			public void run() {
				if (durationTimer - startTimer < 5000) {
					setAlienHMovement(targetHoriSpeed);
					setAlienVMovement(targetVertSpeed);
					durationTimer = SystemTimer.getTime();
				}
				else{
					setAlienHMovement(originHoriSpeed);
					setAlienVMovement(originVertSpeed);
					game.removeTask(taskInvasion);
				}
			}
		};
		game.addTask(taskInvasion,0,4);
	}
}
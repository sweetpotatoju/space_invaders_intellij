package spaceinvaders.entity;

import spaceinvaders.Game;
import spaceinvaders.Sprite;
import spaceinvaders.SpriteStore;
import java.util.ArrayList;
import java.util.List;

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

	private List<String[]> imgArray = new ArrayList<>();
	private String[][] arrAlienImg = new String[2][3];
	/**
	 * Create a new alien entity
	 *
	 * @param game The game in which this entity is being created
	 * @param x The intial x location of this alien
	 * @param y The intial y location of this alient
	 */
	public AlienEntity(Game game, int x, int y) {
		super("sprites/xIcon.png",x,y);
		this.game = game;
		arrAlienImg[0][0]="sprites/ufoo1.png";
		arrAlienImg[0][1]="sprites/ufoo2.png";
		arrAlienImg[0][2]="sprites/ufoo3.png";
		arrAlienImg[1][0]="sprites/ufoo1.png";
		arrAlienImg[1][1]="sprites/ufoo1.png";
		arrAlienImg[1][2]="sprites/ufoo1.png";
		// setup the animatin frames
		frames[0] = SpriteStore.get().getSprite(arrAlienImg[game.getLevel()-1][0]);
		frames[1] = SpriteStore.get().getSprite(arrAlienImg[0][1]);
		frames[2] = SpriteStore.get().getSprite(arrAlienImg[game.getLevel()-1][0]);
		frames[3] = SpriteStore.get().getSprite(arrAlienImg[0][2]);
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

		frames[0] = SpriteStore.get().getSprite("sprites/ufoo1.png");
		frames[1] = SpriteStore.get().getSprite("sprites/ufoo2.png");
		frames[2] = SpriteStore.get().getSprite("sprites/ufoo1.png");
		frames[3] = SpriteStore.get().getSprite("sprites/ufoo3.png");
		this.game = game;
		dx = -getAlienHMovement();
		dy = getAlienVMovement();
		// Customize the attributes of the level 2 alien

		// Add the level 2 alien to the gam
	}

	public void createLevel1Alien(Game game, int x, int y) {
		// Create a new alien entity

		frames[0] = SpriteStore.get().getSprite("sprites/ufoo1.png");
		frames[1] = SpriteStore.get().getSprite("sprites/ufoo2.png");
		frames[2] = SpriteStore.get().getSprite("sprites/ufoo1.png");
		frames[3] = SpriteStore.get().getSprite("sprites/ufoo3.png");
		this.game = game;
		dx = -getAlienHMovement();
		dy = getAlienVMovement();
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
}
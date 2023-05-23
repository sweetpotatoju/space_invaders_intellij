package spaceinvaders.entity;

import spaceinvaders.BackgroundMusic;
import spaceinvaders.Game;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An entity representing a shot fired by the player's ship
 *
 * @author Kevin Glass
 */
public class ShotEntity extends Entity {
	/** The vertical speed at which the players shot moves */
	/** The game in which this entity exists */
	private Game game;
	/** True if this shot has been "used", i.e. its hit something */
	private boolean used = false;

	/**
	 * Create a new shot from the player
	 *
	 * @param game The game in which the shot has been created
	 * @param sprite The sprite representing this shot
	 * @param x The initial x location of the shot
	 * @param y The initial y location of the shot
	 */
	public ShotEntity(Game game,String sprite,int x,int y) {
		super(sprite,x,y);

		this.game = game;

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		BackgroundMusic bgm = new BackgroundMusic("src/main/resources/audio/shot.wav", executorService);
		executorService.execute(bgm);

		dy = -300;
	}

	/**
	 * Request that this shot moved based on time elapsed
	 *
	 * @param delta The time that has elapsed since last move
	 */
	public void move(long delta) {
		// proceed with normal move
		super.move(delta);

		// if we shot off the screen, remove ourselfs
		if (y < -100) {
			game.removeEntity(this);
		}
	}

	/**
	 * Notification that this shot has collided with another
	 * entity
	 *
	 * @parma other The other entity with which we've collided
	 */
	public void collidedWith(Entity other) {
		// prevents double kills, if we've already hit something,
		// don't collide
		// if we've hit an alien, kill it!
		if (other instanceof AlienEntity) {
			// remove the affected entities
			game.removeEntity(this);
			game.removeEntity(other);
			// notify the game that the alien has been killed
			game.notifyAlienKilled(other,5);
		}
		else if (other instanceof level2alienEntity){
			game.removeEntity(this);
			game.removeEntity(other);
			game.notifyAlienKilled(other,10);
		}
		else if (other instanceof bosseEntity){
			game.removeEntity(this);
		}
	}
}
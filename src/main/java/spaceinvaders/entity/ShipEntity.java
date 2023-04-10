package spaceinvaders.entity;

import spaceinvaders.Game;

/**
 * The entity that represents the players ship
 * 
 * @author Kevin Glass
 */
public class ShipEntity extends Entity {
	/** The game in which the ship exists */
	private Game game;
	private boolean multiPlay;
	private int playerLife;

	/**
	 * Create a new entity to represent the players ship
	 *
	 * @param game The game in which the ship is being created
	 * @param ref The reference to the sprite to show for the ship
	 * @param x The initial x location of the player's ship
	 * @param y The initial y location of the player's ship
	 */
	//boolean can make decision to multi play
	public ShipEntity(Game game,String ref,int x,int y,boolean player) {
		super(ref,x,y);
		this.multiPlay = player;
		this.game = game;
		this.playerLife = 3;
	}

	/**
	 * Request that the ship move itself based on an elapsed ammount of
	 * time
	 *
	 * @param delta The time that has elapsed since last move (ms)
	 */
	public void move(long delta) {
		// if we're moving left and have reached the left hand side
		// of the screen, don't move
		if ((dx < 0) && (x < 10)) {
			return;
		}
		// if we're moving right and have reached the right hand side
		// of the screen, don't move
		if ((dx > 0) && (x > 750)) {
			return;
		}
		//same as horizontal.
		if ((dy < 0) && (y < 10)) {
			return;
		}
		if ((dy > 0) && (y > 550)) {
			return;
		}

		super.move(delta);
	}

	/**
	 * Notification that the player's ship has collided with something
	 *
	 * @param other The entity with which the ship has collided
	 */
	public void collidedWith(Entity other) {
		// if its an alien, notify the game that the player is dead
		if (this.multiPlay) {
			if (other instanceof AlienEntity) {
				if (playerLife == 1) {
					game.notifyHit((LifeEntity) game.LifeCounter[2+playerLife]);
					game.removeEntity(this);
					--playerLife;
					game.notifyDeath();
				} else {
					game.removeEntity(other);
					game.notifyHit((LifeEntity) game.LifeCounter[2+playerLife]);
					--playerLife;
				}
			} else if (other instanceof level2shotEntity) {
				if (playerLife == 1) {
					game.notifyHit((LifeEntity) game.LifeCounter[2+playerLife]);
					game.removeEntity(this);
					--playerLife;
					game.notifyDeath();
				} else {
					game.removeEntity(other);
					game.notifyHit((LifeEntity) game.LifeCounter[2+playerLife]);
					--playerLife;}
			}
		} else {
			if (other instanceof AlienEntity) {
				if (playerLife == 1) {
					game.notifyHit((LifeEntity) game.LifeCounter[playerLife-1]);
					game.removeEntity(this);
					--playerLife;
					game.notifyDeath();
				} else {
					game.removeEntity(other);
					game.notifyHit((LifeEntity) game.LifeCounter[playerLife-1]);
					--playerLife;
				}
			} else if (other instanceof level2shotEntity) {
				game.notifyHit((LifeEntity) game.LifeCounter[2+playerLife]);
				game.removeEntity(this);
				--playerLife;
				game.notifyDeath();
			} else {
				game.removeEntity(other);
				game.notifyHit((LifeEntity) game.LifeCounter[2+playerLife]);
				--playerLife;
			}

			}
		}
	}





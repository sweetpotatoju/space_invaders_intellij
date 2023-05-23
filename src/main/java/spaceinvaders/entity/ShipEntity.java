package spaceinvaders.entity;

import spaceinvaders.Game;
import spaceinvaders.ItemSlotMachine;

import java.util.Timer;
import java.util.TimerTask;

/**
 * The entity that represents the players ship
 * 
 * @author Kevin Glass
 */
public class ShipEntity extends Entity {
	/**
	 * The game in which the ship exists
	 */
	private Game game;
	private boolean player2, deadSign;
	private long fireTime;
	private LifeCounter playerLifes;
	private int moveSpeed, fireRatio;

	/**
	 * Create a new entity to represent the players ship
	 *
	 * @param game The game in which the ship is being created
	 * @param ref  The reference to the sprite to show for the ship
	 * @param x    The initial x location of the player's ship
	 * @param y    The initial y location of the player's ship
	 */
	//boolean can make decision to multi play
	public ShipEntity(Game game, String ref, int x, int y, boolean player) {
		super(ref, x, y);
		this.player2 = player;
		this.game = game;
		deadSign = false;
		fireRatio = 500;
		playerLifes = new LifeCounter(game, null, this, 3);
		this.moveSpeed=300;
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
	public void movingLogic(int direction){
		if(direction == 9)this.setHorizontalMovement(-getMoveSpeed());
		else if(direction == 11){this.setHorizontalMovement(-getMoveSpeed());this.setVerticalMovement(-getMoveSpeed());}
		else if(direction == 12)this.setVerticalMovement(-getMoveSpeed());
		else if(direction == 1){this.setVerticalMovement(-getMoveSpeed());this.setHorizontalMovement(getMoveSpeed());}
		else if(direction == 3)this.setHorizontalMovement(getMoveSpeed());
		else if(direction == 5){this.setHorizontalMovement(getMoveSpeed()); this.setVerticalMovement(getMoveSpeed());}
		else if(direction == 6)this.setVerticalMovement(getMoveSpeed());
		else if(direction == 7){this.setVerticalMovement(getMoveSpeed());this.setHorizontalMovement(-getMoveSpeed());}
		else{this.setHorizontalMovement(0);this.setVerticalMovement(0);}
	}
	public void collidedWith(Entity other) {
		if (other instanceof AlienEntity) {
			if (getLife() == 0) return;
			game.killCount+=1;
			if (getLife() == 1) {
				game.removeEntity(this);
				LifeDecrease();
				if (is2P()) game.notifyDeath(2);
				else game.notifyDeath(1);
			} else {
				game.removeEntity(other);
				LifeDecrease();
			}
		} else if (other instanceof ItemEntity) {
			ItemSlotMachine itemGet = new ItemSlotMachine(game, this);
			itemGet.spinItem();
			game.removeEntity(other);
		} else if (other instanceof BossEntity) {
			LifeDecrease();
			LifeDecrease();
			LifeDecrease();
			if (is2P()) game.notifyDeath(2);
			else game.notifyDeath(1);
			game.removeEntity(this);

		} else if (other instanceof AttackEntity) {
			if (getLife() > 1) {
				LifeDecrease();
				game.removeEntity(other);
			} else if (getLife() == 1) {
				LifeDecrease();
				if (is2P()) game.notifyDeath(2);
				else game.notifyDeath(1);
				game.removeEntity(this);
			}
		}
	}
	public void LifeIncrease () {
		playerLifes.LifeIncrease();
	}
	public void LifeDecrease () {
		playerLifes.LifeDecrease();
	}
	public boolean is2P () {
		return player2;
	}
	public int getLife () {
		return playerLifes.getEntityLife();
	}
	public long getFireTime (){
		return fireTime;
	}
	public void setFireTime (long fireTimeStmp){
		this.fireTime = fireTimeStmp;
	}
	public int getFireRatio (){
		return fireRatio;
	}
	public void setFireRatio(int fireRatioTgt){
		this.fireRatio = fireRatioTgt;
	}
	public boolean isDead(){
		return deadSign;
	}
	public void playerDead(){
		this.deadSign = true;
	}
	public int getMoveSpeed(){ return moveSpeed; }
	public void setMoveSpeed(int tgt){ moveSpeed=tgt; }
	public void accelation(){
		int originMoveSpeed = getMoveSpeed();
		int originFireRatio = getFireRatio();
		long startTime = System.currentTimeMillis();
		System.out.println("accel");
		Timer timer = new Timer();
		int accelMove = getMoveSpeed()*2;
		int accelShot = getFireRatio()/2;
		TimerTask task = new TimerTask() {
			long durationtime = System.currentTimeMillis();
			@Override
			public void run() {
				while(durationtime - startTime<5000){
					durationtime = System.currentTimeMillis();
					setMoveSpeed(accelMove);
					setFireRatio(accelShot);
				}
			}
		};
		timer.schedule(task, 0, 500);
		setMoveSpeed(originMoveSpeed);
		setFireRatio(originFireRatio);
		System.out.println("done");
	}
}
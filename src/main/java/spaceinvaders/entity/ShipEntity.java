package spaceinvaders.entity;

import spaceinvaders.Game;
import spaceinvaders.ItemSlotMachine;
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
	private boolean player2, deadSign, keyReversed;
	private long fireTime, accelTime, reverseTime;
	private LifeCounter playerLifes;
	private int moveSpeed, fireRatio;
	private int directionVal;
	private TimerTask taskAccel, taskReverse;

	/**
	 * Create a new entity to represent the players ship
	 *
	 * @param game The game in which the ship is being created
	 * @param ref  The reference to the sprite to show for the ship
	 * @param x    The initial x location of the player's ship
	 * @param y    The initial y location of the player's ship
	 */
	//boolean can make decision to multi play
	public ShipEntity(Game game, String ref, int x, int y, int lifeNumber,boolean player) {
		super(ref, x, y);
		this.player2 = player;
		this.game = game;
		this.deadSign = false;
		this.fireRatio = 500;
		this.playerLifes = new LifeCounter(game, this,lifeNumber);
		this.moveSpeed=300;
		this.fireTime=0;
		this.accelTime=0;
		this.reverseTime=0;
		this.directionVal=0;
		this.keyReversed=false;
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

	 */
	public void movingLogic(int direction){
		if(isDead())return;
		directionVal=direction;
		if(direction>15) {
			tryToFire();
			directionVal = direction-16;
			if(keyReversed) directionVal=15-directionVal;
		}
		switch (directionVal){
			case 1://up
			case 13://up&left&right = up
				setHorizontalMovement(0);
				setVerticalMovement(-getMoveSpeed());
				break;
			case 2://down
			case 14://down&left&right = down
				setHorizontalMovement(0);
				setVerticalMovement(getMoveSpeed());
				break;
			case 4://left
			case 7://up&down&left = left
				setHorizontalMovement(-getMoveSpeed());
				setVerticalMovement(0);
				break;
			case 5://up&left
				setHorizontalMovement(-getMoveSpeed());
				setVerticalMovement(-getMoveSpeed());
				break;
			case 6://down&left
				setHorizontalMovement(-getMoveSpeed());
				setVerticalMovement(getMoveSpeed());
				break;
			case 8://right
			case 11://up&down&right = right
				setHorizontalMovement(getMoveSpeed());
				setVerticalMovement(0);
				break;
			case 9://up&right
				setHorizontalMovement(getMoveSpeed());
				setVerticalMovement(-getMoveSpeed());
				break;
			case 10://down&right
				setHorizontalMovement(getMoveSpeed());
				setVerticalMovement(getMoveSpeed());
				break;
			default:
				setHorizontalMovement(0);
				setVerticalMovement(0);
		}
	}

	public void collidedWith(Entity other) {
		if (other instanceof AlienEntity) {
			if (getLife() == 0) return;
			game.addKillCount();
			if (getLife() == 1) {
				if (is2P()) game.notifyDeath(2);
				else game.notifyDeath(1);
				LifeDecrease();
				game.removeEntity(this);
			} else {
				game.notifyAlienKilled(other,0);
				game.removeEntity(other);
				LifeDecrease();
			}
		} else if (other instanceof ItemEntity) {
			ItemSlotMachine itemGet = new ItemSlotMachine(game, this);
			itemGet.spinItem();
			game.removeEntity(other);
		} else if (other instanceof BossEntity) {
			int currLife=playerLifes.getEntityLife();
			for (int i = 0;i<currLife;i++){
				LifeDecrease();
			}
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

	public void tryToFire() {
		if (isDead()) return;
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - getFireTime() < getFireRatio()) {
			return;
		}
		// if we waited long enough, create the shot entity, and record the time.
		setFireTime(System.currentTimeMillis());
		ShotEntity shot = new ShotEntity(game, "sprites/shot.png",getX()+10,getY()-30);
		game.addEntity(shot);
	}

	public void accelation(){
		if(game.isTaskExist(taskAccel)!=null){return;}
		accelTime = System.currentTimeMillis();
		int originMoveSpeed = getMoveSpeed();
		int originFireRatio = getFireRatio();
		System.out.println("accel");
		int accelMove = getMoveSpeed()*2;
		int accelShot = getFireRatio()/2;
		taskAccel = new TimerTask() {
			@Override
			public void run() {
				long durationtime = System.currentTimeMillis();
				if(durationtime - accelTime<5000){
					setMoveSpeed(accelMove);
					setFireRatio(accelShot);
				} else{
					setMoveSpeed(originMoveSpeed);
					setFireRatio(originFireRatio);
					game.removeTask(taskAccel);
					System.out.println("Accelate done");
				}
			}
		};
		game.addTask(taskAccel,0,50);
	}

	public void keyReverse() {
		if(game.isTaskExist(taskReverse)!=null){return;}
		reverseTime = System.currentTimeMillis();
		System.out.println("key reverse");
		taskReverse = new TimerTask() {
			@Override
			public void run() {
				long durationtime = System.currentTimeMillis();
				if(durationtime - reverseTime<5000){
					keyReversed=true;
				} else{
					keyReversed = false;
					game.removeTask(taskReverse);
					System.out.println("Reverse done.");
				}
			}
		};
		game.addTask(taskReverse,0,100);
	}
}
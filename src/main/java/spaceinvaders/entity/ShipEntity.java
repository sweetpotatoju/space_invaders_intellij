package spaceinvaders.entity;

import spaceinvaders.Game;

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
	private long fireTime, fireRatio;
	private LifeCounter playerLifes;
	private int moveSpeed;

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
		playerLifes = new LifeCounter(game, null, this);
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
		// if its an alien, notify the game that the player is dead

//		if (player2) {
//			if (other instanceof AlienEntity) {
//				if (playerLife == 1) {
//					game.notifyHit(game.LifeCounter[2 + playerLife]);
//					game.removeEntity(this);
//					--playerLife;
//					game.notifyDeath(2);
//				} else {
//					game.notifyDeath(2);
//				}
//				else{
//					game.removeEntity(other);
//					game.notifyHit(game.LifeCounter[2 + playerLife]);
//					--playerLife;
//				}
//			} else if (other instanceof level2alienEntity) {
////				if (playerLife == 1) {
////					game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
////					game.removeEntity(this);
////					--playerLife;
////					game.notifyDeath(2);
////				} else {
////					game.removeEntity(other);
////					game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
////					--playerLife;
////				}
////			} else if (other instanceof bosseEntity) {
////				if (playerLife > 1) {
////					game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
////					--playerLife;
////				} else if (playerLife == 1) {
////					game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
////					--playerLife;
////					game.notifyDeath(2);
////					game.removeEntity(this);
////					{
////
//					}
//				}
//			} else {
//				if (other instanceof AlienEntity) {
//					if (playerLife == 1) {
//						game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
//					}
//				} else {
//					if (other instanceof AlienEntity) {
//						if (playerLife == 1) {
//							game.notifyHit(game.LifeCounter[playerLife - 1]);
//							game.removeEntity(this);
//							--playerLife;
//							game.notifyDeath(2);
//						} else {
//							game.notifyDeath(1);
//						}
//					} else {
//						game.removeEntity(other);
//						game.notifyHit(game.LifeCounter[playerLife - 1]);
//						--playerLife;
//					}
//				}
//			} else if (other instanceof level2alienEntity) {
//				if (playerLife == 1) {
//					game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
//					game.removeEntity(this);
//					--playerLife;
//					game.notifyDeath(2);
//				} else {
//					game.removeEntity(other);
//					game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
//					--playerLife;
//				}
//
//
//			} else if (other instanceof bosseEntity) {
//				if (playerLife > 1) {
//					game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
//					--playerLife;
//				} else if (playerLife == 1) {
//					game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
//					--playerLife;
//					game.notifyDeath(2);
//					game.removeEntity(this);
//					{
//
//					}
//
//
//				}
//			}
//		}
//	}
//}
		if (other instanceof AlienEntity) {
			if (getLife() == 0)return;
			if (getLife() == 1) {
				game.removeEntity(this);
				LifeDecrease();
				if (is2P()) game.notifyDeath(2);
				else game.notifyDeath(1);
			} else {
				game.removeEntity(other);
				LifeDecrease();
			}
		}
		else if (other instanceof ItemEntity){
			if (getLife() == 0)return;
			if (getLife() == 1) {
				game.removeEntity(this);
				LifeDecrease();
				if (is2P()) game.notifyDeath(2);
				else game.notifyDeath(1);
			} else {
				game.removeEntity(other);
				LifeDecrease();
		}}
		else if (other instanceof bosseEntity) {
				if (getLife() == 0)return;
				if (getLife() == 1) {
					game.removeEntity(this);
					LifeDecrease();
					if (is2P()) game.notifyDeath(2);
					else game.notifyDeath(1);
				} else {
					game.removeEntity(other);
					LifeDecrease();
			}
		}

		else if (other instanceof level2alienEntity) {
			if (getLife() == 0) return;
			if (getLife() == 1) {
				game.removeEntity(this);
				LifeDecrease();
				if (is2P()) game.notifyDeath(2);
				else game.notifyDeath(1);
			} else {
				game.removeEntity(other);
				LifeDecrease();
			}
		}
		else if (other instanceof bossacttackentity) {
			if (getLife() == 0)return;
			if (getLife() == 1) {
				game.removeEntity(this);
				LifeDecrease();
				if (is2P()) game.notifyDeath(2);
				else game.notifyDeath(1);
			} else {
				game.removeEntity(other);
				LifeDecrease();;
			}
		}

		else if (other instanceof level2shotEntity) {
			if (getLife() == 0)return;
			if (getLife() == 1) {
				game.removeEntity(this);
				LifeDecrease();
				if (is2P()) game.notifyDeath(2);
				else game.notifyDeath(1);
			} else {
				game.removeEntity(other);
				LifeDecrease();
			}
		}



		/** 					game.notifyHit(game.LifeCounter[2+playerLife]);
         --playerLife;
         }
         }else if (other instanceof level2alienEntity) {
         if (playerLife == 1) {
         game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
         game.removeEntity(this);
         --playerLife;
         game.notifyDeath(2);
         } else {
         game.removeEntity(other);
         game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
         --playerLife;
         }
         } else if (other instanceof bosseEntity) {
         if (playerLife > 1) {
         game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
         --playerLife;
         } else if (playerLife == 1) {
         game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
         --playerLife;
         game.notifyDeath(2);
         game.removeEntity(this);
         }}



         }

         else {
         if (other instanceof AlienEntity) {
         if (playerLife == 1) {
         game.notifyHit(game.LifeCounter[playerLife - 1]);
         game.removeEntity(this);
         --playerLife;
         game.notifyDeath(1);
         }
         else{
         game.removeEntity(other);
         game.notifyHit(game.LifeCounter[playerLife - 1]);
         --playerLife;
         }
         }
         else if (other instanceof level2alienEntity) {
         if (playerLife == 1) {
         game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
         game.removeEntity(this);
         --playerLife;
         game.notifyDeath(1);
         } else {
         game.removeEntity(other);
         game.notifyHit((LifeEntity) game.LifeCounter[playerLife - 1]);
         --playerLife;
         } */
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
	public long getFireRatio (){
		return fireRatio;
	}
	public void setFireRatio(long fireRatioTgt){
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
}
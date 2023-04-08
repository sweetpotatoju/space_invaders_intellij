package spaceinvaders;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.*;

import spaceinvaders.entity.*;

/**
 * The main hook of our game. This class with both act as a manager
 * for the display and central mediator for the game logic. 
 * 
 * Display management will consist of a loop that cycles round all
 * entities in the game asking them to move and then drawing them
 * in the appropriate place. With the help of an inner class it
 * will also allow the player to control the main ship.
 * 
 * As a mediator it will be informed when entities within our game
 * detect events (e.g. alient killed, played died) and will take
 * appropriate game actions.
 * 
 * @author Kevin Glass
 */
public class Game extends Canvas 
{
	/** The stragey that allows us to use accelerate page flipping */
	private BufferStrategy strategy;
	/** True if the game is currently "running", i.e. the game loop is looping */
	private boolean gameRunning = true;
	/** The list of all the entities that exist in our game */
	private ArrayList entities = new ArrayList();
	/** The list of entities that need to be removed from the game this loop */
	private ArrayList removeList = new ArrayList();
	/** The entity representing the player */
	/** The speed at which the player's ship should move (pixels/sec) */
	private static double moveSpeed = 300;
	/** The number of aliens left on the screen */
	private int alienCount;
	
	/** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if the left cursor key is currently pressed */
	//1P key set
	private ShipEntity ship;
	private Entity p1Life1, p1Life2, p1Life3;
	/** True if the left cursor key is currently pressed */
	private static boolean leftPressed = false;
	/** True if the right cursor key is currently pressed */
	private static boolean rightPressed = false;
	/** down movement key detection */
	private static boolean downPressed = false;
	/** up movement key detection */
	private static boolean upPressed = false;
	/** True if we are firing */
	private static boolean firePressed = false;
	/** The time at which last fired a shot */
	private long lastFire = 0;
	/** The interval between our players shot (ms) */
	private long firingInterval = 500;

	//2P key set
	private ShipEntity ship2;
	private Entity p2Life1, p2Life2, p2Life3;
	/** True if the 2P left cursor key is currently pressed */
	private static boolean left2Pressed = false;
	/** True if the 2P right cursor key is currently pressed */
	private static boolean right2Pressed = false;
	/** 2P down movement key detection */
	private static boolean down2Pressed = false;
	/** 2P up movement key detection */
	private static boolean up2Pressed = false;
	/** 2P True if we are firing */
	private static boolean fire2Pressed = false;
	/** The time at which last fired a shot */
	private long last2Fire = 0;
	/** The interval between our players shot (ms) */
	private long firing2Interval = 500;

	/** True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean logicRequiredThisLoop = false;
	private boolean isGameStart = false;
	/** The last time at which we recorded the frame rate */
	private long lastFpsTime;
	/** The current number of frames recorded */
	private int fps;
	/** The normal title of the game window */
	private String windowTitle = "Space Invaders 102";
	/** The game window that we'll update with the frame count */
	private JFrame container;

	public Entity[] LifeCounter = {p1Life1, p1Life2, p1Life3, p2Life1, p2Life2, p2Life3};
	
	/**
	 * Construct our game and set it running.
	 */
	public Game(String option) {
		// create a frame to contain our game
		container = new JFrame("Space Invaders 102");

		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);

		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,800,600);
		panel.add(this);

		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);

		// finally make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(new KeyInputHandler());

		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2 );
		strategy = getBufferStrategy();

		// initialise the entities in our game so there's something
		// to see at startup

		if (option.equals("2p")) {
			System.out.println("2p");
		}

		initEntities();
	}


	
	/**
	 * Start a fresh game, this should clear out any old data and
	 * create a new set.
	 */
	private void startGame() {
		// clear out any existing entities and intialise a new set
		entities.clear();
		initEntities();
		
		// blank out any keyboard settings we might currently have
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		downPressed = false;
		firePressed = false;

		//2P key init
		left2Pressed = false;
		right2Pressed = false;
		up2Pressed = false;
		down2Pressed = false;
		fire2Pressed = false;
	}
	
	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entitiy will be added to the overall list of entities in the game.
	 */

//	private void initEntities() {
//		// create the player ship and place it roughly in the center of the screen
//		ship = new ShipEntity(this, "sprites/ship.gif",370,550);
//		entities.add(ship);
//
//
//		int alienCount = 50; // number of aliens
//		int alienWidth = 50; // width of each alien
//		int alienHeight = 30; // height of each alien
//		int minY = 10; // minimum y-coordinate
//		int maxY = 200; // maximum y-coordinate
//
//		Set<Point> points = new HashSet<>(); // set to keep track of the generated points
//		Random random = new Random();
//
//		while (points.size() < alienCount) {
//			int x = random.nextInt(getWidth() - alienWidth);
//			int y = random.nextInt(maxY - minY) + minY;
//
//			// check if the new point overlaps with any existing points
//			boolean overlapping = false;
//			for (Point point : points) {
//				if (Math.abs(point.x - x) < alienWidth && Math.abs(point.y - y) < alienHeight) {
//					overlapping = true;
//					break;
//				}
//			}
//
//			// if not overlapping, add the new point to the set
//			if (!overlapping) {
//				points.add(new Point(x, y));
//			}
//		}
//
//// create aliens for each generated point
//		for (Point point : points) {
//			Entity alien = new AlienEntity(this, point.x, point.y);
//			entities.add(alien);
//		}}
	private void initEntities() {
		// create the player ship and place it roughly in the center of the screen
		//1P ship
		ship = new ShipEntity(this, "sprites/ship.gif", 350, 550, false);
		//2P ship
		ship2 = new ShipEntity(this, "sprites/ship2.gif", 390, 550, true);
		entities.add(ship);
		entities.add(ship2);
		int idx = 20;
		for (Entity Life : LifeCounter){
			if (idx > 60) {
				Life = new LifeEntity(this, 655+idx, 580);
				LifeCounter[idx/20 - 1] = Life;
			}
			else {
				Life = new LifeEntity(this, idx-15, 580);
				LifeCounter[idx/20 - 1] = Life;
			}
			entities.add(Life);
			idx+=20;
		}

		final int alienCount = 50; // number of aliens
		int alienWidth = 50; // width of each alien
		int alienHeight = 30; // height of each alien
		int minY = 10; // minimum y-coordinate
		int maxY = 200; // maximum y-coordinate
		int delay = 5000; // time delay between each batch of aliens (in milliseconds)

		final Set<Point> points = new HashSet<>(); // set to keep track of the generated points
		Random random = new Random();

		while (points.size() < alienCount) {
			int x = random.nextInt(getWidth() - alienWidth);
			int y = random.nextInt(maxY - minY) + minY;

			// check if the new point overlaps with any existing points
			boolean overlapping = false;
			for (Point point : points) {
				if (Math.abs(point.x - x) < alienWidth && Math.abs(point.y - y) < alienHeight) {
					overlapping = true;
					break;
				}
			}

			// if not overlapping, add the new point to the set
			if (!overlapping) {
				points.add(new Point(x, y));
			}
		}

		// create a timer to add aliens every delay milliseconds
		Timer timer = new Timer(delay, new ActionListener() {
			int count = 0;
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isGameStart) {
					if (count < alienCount) {
						Point point = points.toArray(new Point[0])[count];
						Entity alien = new AlienEntity(Game.this, point.x, point.y);
						entities.add(alien);
						count++;
					}
				}
			}
		});
		timer.start();
	}
	/**
             * Notification from a game entity that the logic of the game
             * should be run at the next opportunity (normally as a result of some
             * game event)
             */

	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
	
	/**
	 * Remove an entity from the game. The entity removed will
	 * no longer move or be drawn.
	 * 
	 * @param entity The entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}
	/**
	 * HeartBox Entity Decreasion
	 */
	public void notifyHit(LifeEntity Life){
		Life.LifeDecrease();
	}
	/**
	 * Notification that the player has died.
	 */
	public void notifyDeath() {
		message = "Oh no! They got you, try again?";
		waitingForKeyPress = true;
		isGameStart = false;
	}
	
	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		message = "Well done! You Win!";
		waitingForKeyPress = true;
		isGameStart = false;
	}
	
	/**
	 * Notification that an alien has been killed
	 */
	public void notifyAlienKilled() {
		// reduce the alient count, if there are none left, the player has won!
		alienCount--;
		
		if (alienCount == 0) {
			notifyWin();
		}
		
		// if there are still some aliens left then they all need to get faster, so
		// speed up all the existing aliens
		for (int i=0;i<entities.size();i++) {
			Entity entity = (Entity) entities.get(i);
			
			if (entity instanceof AlienEntity) {
				// speed up by 2%
				entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.02);
			}
		}
	}
	
	/**
	 * Attempt to fire a shot from the player. Its called "try"
	 * since we must first check that the player can fire at this 
	 * point, i.e. has he/she waited long enough between shots
	 */
	public void tryToFire() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		// if we waited long enough, create the shot entity, and record the time.
		lastFire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this, "sprites/shot.gif",ship.getX()+10,ship.getY()-30);
		entities.add(shot);
	}
	public void tryToFire2() {
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - last2Fire < firing2Interval) {
			return;
		}

		// if we waited long enough, create the shot entity, and record the time.
		last2Fire = System.currentTimeMillis();
		ShotEntity shot = new ShotEntity(this, "sprites/shot.gif",ship2.getX()+10,ship2.getY()-30);
		entities.add(shot);
	}
	
	/**
	 * The main game loop. This loop is running during all game
	 * play as is responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves
	 * - Moving the game entities
	 * - Drawing the screen contents (entities, text)
	 * - Updating game events
	 * - Checking Input
	 * <p>
	 */
	public void gameLoop() {
		long lastLoopTime = SystemTimer.getTime();
		
		// keep looping round til the game ends
		while (gameRunning) {
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long delta = SystemTimer.getTime() - lastLoopTime;
			lastLoopTime = SystemTimer.getTime();

			// update the frame counter
			lastFpsTime += delta;
			fps++;
			
			// update our FPS counter if a second has passed since
			// we last recorded
			if (lastFpsTime >= 1000) {
				container.setTitle(windowTitle+" (FPS: "+fps+")");
				lastFpsTime = 0;
				fps = 0;
			}
			
			// Get hold of a graphics context for the accelerated 
			// surface and blank it out
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0,0,800,600);
			
			// cycle round asking each entity to move itself
			if (!waitingForKeyPress) {
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);
					
					entity.move(delta);
				}
			}
			
			// cycle round drawing all the entities we have in the game
			for (int i=0;i<entities.size();i++) {
				Entity entity = (Entity) entities.get(i);
				
				entity.draw(g);
			}
			
			// brute force collisions, compare every entity against
			// every other entity. If any of them collide notify 
			// both entities that the collision has occured
			for (int p=0;p<entities.size();p++) {
				for (int s=p+1;s<entities.size();s++) {
					Entity me = (Entity) entities.get(p);
					Entity him = (Entity) entities.get(s);
					
					if (me.collidesWith(him)) {
						me.collidedWith(him);
						him.collidedWith(me);
					}
				}
			}
			
			// remove any entity that has been marked for clear up
			entities.removeAll(removeList);
			removeList.clear();

			// if a game event has indicated that game logic should
			// be resolved, cycle round every entity requesting that
			// their personal logic should be considered.
			if (logicRequiredThisLoop) {
				for (int i=0;i<entities.size();i++) {
					Entity entity = (Entity) entities.get(i);
					entity.doLogic();
				}
				
				logicRequiredThisLoop = false;
			}
			
			// if we're waiting for an "any key" press then draw the 
			// current message 
			if (waitingForKeyPress) {
				g.setColor(Color.white);
				g.drawString(message,(800-g.getFontMetrics().stringWidth(message))/2,250);
				g.drawString("Press any key",(800-g.getFontMetrics().stringWidth("Press any key"))/2,300);
			} else {
				isGameStart = true;
			}
			
			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			g.dispose();
			strategy.show();
			
			// resolve the movement of the ship. First assume the ship 
			// isn't moving. If either cursor key is pressed then
			// update the movement appropraitely

			//1P Control
			shipControl(ship);
			//2P control
			shipControl2(ship2);
			// if we're pressing fire, attempt to fire
			if (firePressed) {
				tryToFire();
			}
			if (fire2Pressed){
				tryToFire2();
			}
			// we want each frame to take 10 milliseconds, to do this
			// we've recorded when we started the frame. We add 10 milliseconds
			// to this and then factor in the current time to give 
			// us our final value to wait for
			SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
		}
	}
	
	/**
	 * A class to handle keyboard input from the user. The class
	 * handles both dynamic input during game play, i.e. left/right 
	 * and shoot, and more static type input (i.e. press any key to
	 * continue)
	 * 
	 * This has been implemented as an inner class more through 
	 * habbit then anything else. Its perfectly normal to implement
	 * this as seperate class if slight less convienient.
	 * 
	 * @author Kevin Glass
	 */
	private class KeyInputHandler extends KeyAdapter {
		/** The number of key presses we've had while waiting for an "any key" press */
		private int pressCount = 1;
		
		/**
		 * Notification from AWT that a key has been pressed. Note that
		 * a key being pressed is equal to being pushed down but *NOT*
		 * released. Thats where keyTyped() comes in.
		 *
		 * @param e The details of the key that was pressed 
		 */
		public void keyPressed(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't 
			// want to do anything with just a "press"
			if (waitingForKeyPress) {
				return;
			}


			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP){
				upPressed = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				downPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				left2Pressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				right2Pressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_W){
				up2Pressed = true;
			}
			if(e.getKeyCode() == KeyEvent.VK_S){
				down2Pressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_1) {
				fire2Pressed = true;
			}
		}
		/**
		 * Notification from AWT that a key has been released.
		 *
		 * @param e The details of the key that was released
		 */
		public void keyReleased(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't
			// want to do anything with just a "released"
			if (waitingForKeyPress) {
				return;
			}

			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP){
				upPressed = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_DOWN){
				downPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				firePressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_A) {
				left2Pressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				right2Pressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_W){
				up2Pressed = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_S){
				down2Pressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_1) {
				fire2Pressed = false;
			}
		}
		/**
		 * Notification from AWT that a key has been typed. Note that
		 * typing a key means to both press and then release it.
		 *
		 * @param e The details of the key that was typed. 
		 */
		public void keyTyped(KeyEvent e) {
			// if we're waiting for a "any key" type then
			// check if we've recieved any recently. We may
			// have had a keyType() event from the user releasing
			// the shoot or move keys, hence the use of the "pressCount"
			// counter.
			if (waitingForKeyPress) {
				if (pressCount == 1) {
					// since we've now recieved our key typed
					// event we can mark it as such and start 
					// our new game
					waitingForKeyPress = false;
					startGame();
					pressCount = 0;
				} else {
					pressCount++;
				}
			}
			
			// if we hit escape, then quit the game
			if (e.getKeyChar() == 27) {
				System.exit(0);
			}
		}
	}
	public static void shipControl(ShipEntity ship){
		ship.setHorizontalMovement(0);
		ship.setVerticalMovement(0);
		if ((leftPressed)&&(!rightPressed)&&(!upPressed)&&(!downPressed)){
			ship.setHorizontalMovement(-moveSpeed);
		}
		//right unique move
		else if ((rightPressed)&&(!leftPressed)&&(!upPressed)&&(!downPressed)){
			ship.setHorizontalMovement(moveSpeed);
		}
		//up unique move
		else if ((upPressed)&&(!downPressed)&&(!rightPressed)&&(!leftPressed)){
			ship.setVerticalMovement(-moveSpeed);
		}
		//down unique move
		else if ((downPressed)&&(!upPressed)&&(!rightPressed)&&(!leftPressed)){
			ship.setVerticalMovement(moveSpeed);
		}
		//left&up degree 45
		else if((leftPressed)&&(upPressed)&&(!rightPressed)&&(!downPressed)){
			ship.setVerticalMovement(-moveSpeed);
			ship.setHorizontalMovement(-moveSpeed);
		}
		else if((leftPressed)&&(downPressed)&&(!rightPressed)&&(!upPressed)){
			ship.setVerticalMovement(moveSpeed);
			ship.setHorizontalMovement(-moveSpeed);
		}
		else if((rightPressed)&&(upPressed)&&(!downPressed)&&(!leftPressed)){
			ship.setVerticalMovement(-moveSpeed);
			ship.setHorizontalMovement(moveSpeed);
		}
		else if((rightPressed)&&(downPressed)&&(!upPressed)&&(!leftPressed)){
			ship.setVerticalMovement(moveSpeed);
			ship.setHorizontalMovement(moveSpeed);
		}
	}
	public static void shipControl2(ShipEntity ship){
		ship.setHorizontalMovement(0);
		ship.setVerticalMovement(0);
		if ((left2Pressed)&&(!right2Pressed)&&(!up2Pressed)&&(!down2Pressed)){
			ship.setHorizontalMovement(-moveSpeed);
		}
		//right unique move
		else if ((right2Pressed)&&(!left2Pressed)&&(!up2Pressed)&&(!down2Pressed)){
			ship.setHorizontalMovement(moveSpeed);
		}
		//up unique move
		else if ((up2Pressed)&&(!down2Pressed)&&(!right2Pressed)&&(!left2Pressed)){
			ship.setVerticalMovement(-moveSpeed);
		}
		//down unique move
		else if ((down2Pressed)&&(!up2Pressed)&&(!right2Pressed)&&(!left2Pressed)){
			ship.setVerticalMovement(moveSpeed);
		}
		//left&up degree 45
		else if((left2Pressed)&&(up2Pressed)&&(!right2Pressed)&&(!down2Pressed)){
			ship.setVerticalMovement(-moveSpeed);
			ship.setHorizontalMovement(-moveSpeed);
		}
		else if((left2Pressed)&&(down2Pressed)&&(!right2Pressed)&&(!up2Pressed)){
			ship.setVerticalMovement(moveSpeed);
			ship.setHorizontalMovement(-moveSpeed);
		}
		else if((right2Pressed)&&(up2Pressed)&&(!down2Pressed)&&(!left2Pressed)){
			ship.setVerticalMovement(-moveSpeed);
			ship.setHorizontalMovement(moveSpeed);
		}
		else if((right2Pressed)&&(down2Pressed)&&(!up2Pressed)&&(!left2Pressed)){
			ship.setVerticalMovement(moveSpeed);
			ship.setHorizontalMovement(moveSpeed);
		}
	}
	public void LifeCounter(){

	}
	/**
	 * The entry point into the game. We'll simply create an
	 * instance of class which will start the display and game
	 * loop.
	 * 
	 * @param argv The arguments that are passed into our game
	 */
	public static void main(String argv[]) {
		Game g = new Game("");

		// Start the main game loop, note: this method will not
		// return until the game has finished running. Hence we are
		// using the actual main thread to run the game.
		g.gameLoop();

	}


}

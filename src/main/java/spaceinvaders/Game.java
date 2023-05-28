package spaceinvaders;

import spaceinvaders.entity.*;
import javax.swing.*;
import java.util.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

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
public class Game extends Canvas {
	/**
	 * The stragey that allows us to use accelerate page flipping
	 */
	private final BufferStrategy strategy;
	/**
	 * True if the game is currently "running", i.e. the game loop is looping
	 */
	private final boolean gameRunning = true; private boolean stageRunning = false;
	/**
	 * The list of all the entities that exist in our game
	 */
	private final ArrayList<Entity> entities = new ArrayList<>();
	/**
	 * The list of entities that need to be removed from the game this loop
	 */
	private final ArrayList<Entity> removeList = new ArrayList<Entity>();
	/** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if the left cursor key is currently pressed */
	private Entity[] ShipCounter = new ShipEntity[2]; private boolean multiPlay;
	private boolean leftPressed,left2Pressed, rightPressed, right2Pressed;
	private boolean upPressed, up2Pressed, downPressed, down2Pressed;
	private int Lonly, Ronly, Uonly, Donly, LnD, RnD, LnU, RnU;
	private boolean firePressed, fire2Pressed, player1Dead, player2Dead;
	private boolean goGo, keyP1Reverse, keyP2Reverse;
	/** True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean logicRequiredThisLoop = false;
	private boolean isGameStart = false;
	/**
	 * The last time at which we recorded the frame rate
	 */
	private long lastFpsTime;
	/**
	 * The current number of frames recorded
	 */
	private int fps, cycle;
	/**
	 * The normal title of the game window
	 */
	private final String windowTitle = "Space Invaders 102";
	/**
	 * The game window that we'll update with the frame count
	 */
	private final JFrame container;
	private int level = 1;
	private Timer timer;
	private static int genCount, alienCount;
	public static int killCount, liveCount;
	private static String bestScore = "";
	private FirebaseTool firebaseTool;

	private GlobalStorage globalStorage;

	//Record Variables
	private int tenToHundMillis, aSecond, aMinute;
	/** timedisplay */
	private String timeStamp="";
	private long lastLoopTime; private long initTime;
	private RecordRecorder playBoard = new RecordRecorder(this);

	private static int alienVertSpeed=10, alienHoriSpeed=75;
	private JLabel backLabel;
	private Graphics2D userHUD;
	private Image image;
	private Game game = this;

	/**
	 * Construct our game and set it running.
	 */
	public Game(String option) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		BackgroundMusic bgm = new BackgroundMusic("src/main/resources/audio/backgroundmusic.wav", executorService);
		executorService.execute(bgm);

		cycle = 0;
		if (option.equals("2p")) {multiPlay = true; System.out.println("2p");}
		else {multiPlay = false; System.out.println("1p");}
		// create a frame to contain our game
		container = new JFrame("Space Invaders 102");

		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);



		JButton home = new JButton("HOME");
		home.setBounds(0,0,80,30);
		home.setBackground(Color.BLACK);
		home.setOpaque(false);
		home.setForeground(Color.BLACK);
		home.setContentAreaFilled(false);
		home.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				home.setIcon(changeIconGoHome);
				bgm.stop();
				container.dispose();
			}



		});


		panel.add(home);





		/*TimeCounter timeCounter = new TimeCounter((int) 0);*/
		//panel.add(timeCounter);
		// setup our canvas size and put it into the content of the frame
		setBounds(0,0,800,600);
		panel.add(this);
		// TimeCounter 객체를 실행하는 Thread를 생성하고 시작합니다.
		/*timeCounterThread = new Thread(timeCounter);
		timeCounterThread.start();*/
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
//				System.exit(0);
				bgm.stop();
			}
		});
		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(new KeyInputHandler());
		// request the focus so key events come to us
		requestFocus();
		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		// initialise the entities in our game so there's something
		// to see at startup
		firebaseTool = FirebaseTool.getInstance();
		globalStorage = GlobalStorage.getInstance();
		genCount = 0;
		killCount = 0;
		alienCount = 0;
		liveCount = 0;
		playBoard.scoreInit();
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
		player1Dead = false;

		//2P key init
		left2Pressed = false;
		right2Pressed = false;
		up2Pressed = false;
		down2Pressed = false;
		fire2Pressed = false;
		player2Dead = false;
		tenToHundMillis=0; aSecond=0; aMinute=0;
		//윈도우랑 게임창 노래 겹쳐들림
//		new BackgroundMusic();

		JButton home = new JButton("HOME");
		home.setBounds(0,0,80,30);
		home.setBackground(Color.WHITE);

	}
	private void initEntities() {
		if (multiPlay){
			ShipCounter[0] = new ShipEntity(this, "sprites/ship1p.png",350, 550, false);
			entities.add(ShipCounter[0]);
			ShipCounter[1] = new ShipEntity(this, "sprites/ship2p.png",390, 550, true);
			addEntity(ShipCounter[1]);
		}
		else{
			ShipCounter[0] = new ShipEntity(this, "sprites/ship1p.png",370, 550, false);
			entities.add(ShipCounter[0]);
		}
		killCount = 0;
		// create the aliens
		message ="When you're ready, please press the button!";
	}
	public void repeatGame(){
		if(level==4){
			setAlienVertSpeed((int)(getAlienVertSpeed()*1.05));
			setAlienHoriSpeed((int)(getAlienHoriSpeed()*1.05));
			stageRunning=false;
			level=1;
		}
		else{
			setAlienHoriSpeed(75);
			setAlienVertSpeed(10);
			stageRunning=false;
			level=1;
		}
		genCount=0;
	}
	private void createAliens() {
		if(waitingForKeyPress) return;
		stageRunning=true;
		// determine the parameters for the aliens based on the current level// increase the number of aliens by 2 for each level
		alienCount = 10  + (level - 1) * 2;// increase the number of aliens by 2 for each level11
		if (level == 3) liveCount = 1;
		else liveCount = alienCount;
		int alienWidth = 50; // width of each alien
		int alienHeight = 30; // height of each alien
		int minY = 10; // minimum y-coordinate
		int maxY = 200; // maximum y-coordinate
		final Set<Point> points = new HashSet<>(); // set to keep track of the generated points
		Random random = new Random();
		while (points.size() < alienCount) {
			int x = random.nextInt(getWidth() - alienWidth);
			int y = random.nextInt(maxY - minY) + minY;
			// check if the new point overlaps with any existing points
			boolean overlapping = false;
			if(level == 3){break;}
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
		if (level == 1) {
			System.out.println("level 1 intro");
			if (isGameStart) {
				/**
				 * this section is added by jgs
				 * */
				//initTime = lastLoopTime;
				//aMinute = 0;
				//aSecond = 0;
				//tenToHundMillis = 0;
				//goGo = true;
			}
			stageRunning = true;
			Timer timerLv1 = new Timer();
			TimerTask taskLv1 = new TimerTask() {
				@Override
				public void run() {
					if (waitingForKeyPress) return;
					if (genCount < alienCount) {
						Point[] pointArray = points.toArray(new Point[0]);
						Point randomPoint = pointArray[genCount];
						int x = randomPoint.x;
						int y = randomPoint.y;
						AlienEntity alien = new AlienEntity(Game.this, x, y);
						alien.createLevel1Alien(Game.this, x, y);
						addEntity(alien);
						System.out.println("level 1 spawned: " + (genCount + 1));
						++genCount;
					} else {
						System.out.println("Lv1 All Spawned");
						timerLv1.cancel();
						genCount = 0;
					}
				}
			};
			timerLv1.schedule(taskLv1, 0, 1000);
		}
		else if (level == 2){
			System.out.println("level 2 intro");
			stageRunning = true;
			genCount = 0;

			Timer timerLv2 = new Timer();
			TimerTask taskLv2 = new TimerTask() {
				@Override
				public void run() {
					if (waitingForKeyPress) return;
					if (genCount < alienCount) {
						Point[] pointArray = points.toArray(new Point[0]);
						Point randomPoint = pointArray[genCount];
						int x = randomPoint.x;
						int y = randomPoint.y;
						AlienEntity alien = new AlienEntity(Game.this, x, y);
						alien.createLevel2Alien(Game.this, x, y);
						addEntity(alien);
						System.out.println("level 1 spawned: " + (genCount + 1));
						++genCount;
					} else {
						System.out.println("Lv1 All Spawned");
						timerLv2.cancel();
						genCount = 0;
					}
				}
			};
			timerLv2.schedule(taskLv2, 0, 1000);
		}
		else if (level == 3) {
			System.out.println("level 3 intro");
			if(waitingForKeyPress)return;
			stageRunning = true;
			BossEntity boss = new BossEntity(this, "sprites/boss2.png", getWidth() / 2, 50);
			entities.add(boss);
		}
	}
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
	/** This can help you to access entities.add() in other class */
	public void addEntity(Entity entity){ entities.add(entity); }

	/**
	 * Notification that the player has died.
	 */
	public void notifyDeath(int status) {
		if(status == 1) {((ShipEntity)ShipCounter[0]).playerDead(); }
		if(status == 2) {((ShipEntity)ShipCounter[1]).playerDead(); }
		if(multiPlay){
			if (((ShipEntity)ShipCounter[0]).isDead() && ((ShipEntity)ShipCounter[1]).isDead()) notifyRetire();
		} else notifyRetire();
	}

	public void notifyRetire(){
		if (playBoard.getScore()> Integer.parseInt(globalStorage.getUserBestScore())) {
			message = "Oh no!, but  New best score!";
			resultSender(Integer.toString(playBoard.getScore()));
			killCount = 0;
			playBoard.scoreInit();
		}
		else{
			message = "Oh no! They got you, try again?";
			waitingForKeyPress = true;
			isGameStart = false;
			repeatGame();
			killCount=0;
			playBoard.scoreInit();
		}
	}
	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		message = "Well done! You Win!";
		level++;
		message = "level" + level;
		waitingForKeyPress = true;
		isGameStart = false;
		stageRunning = false;
		if (level == 4) level = 1;
	}
	public void resultSender(String result){
		firebaseTool.setUserBestScore(globalStorage.getUserID(), result);
		globalStorage.setUserBestScore(result); // 베스트 스코어 업데이트
	}
	public void itemDrop(int x, int y){
		if (killCount%3 == 0 && killCount/3 >= 1){
			addEntity(new ItemEntity(this,"sprites/itemBox.png",x,y));
		}

	}

	public void notifyAlienKilled(Entity other, int score) {
		// reduce the alient count, if there are none left, the player has won!
		++killCount;
		--liveCount;
		playBoard.scoreModeAdd(score);
		itemDrop(other.getX(), other.getY());
		System.out.println(killCount);
		if (level == 1) {
			if (liveCount == 0) {
				notifyWin();
			}
		} else if (level == 2) {
			if(alienCount%2 == 0){
				level2shot();
			}
			if (liveCount == 0) {
				notifyWin();
			}
		} else if (level == 3) {
			if(alienCount%5 ==0){
				bossAttack();
			}
			if (liveCount == 0) {
				notifyWin();
			}
		}
	}
	/**
	 * Attempt to fire a shot from the player. Its called "try"
	 * since we must first check that the player can fire at this
	 * point, i.e. has he/she waited long enough between shots
	 */
	public void tryToFire() {
		ShipEntity ship = (ShipEntity) ShipCounter[0];
		if (ship.isDead()) return;
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - ship.getFireTime() < ship.getFireRatio()) {
			return;
		}
		// if we waited long enough, create the shot entity, and record the time.
		ship.setFireTime(System.currentTimeMillis());
		ShotEntity shot = new ShotEntity(this, "sprites/shot.png",ShipCounter[0].getX()+10,ShipCounter[0].getY()-30);
		entities.add(shot);
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		BackgroundMusic ss = new BackgroundMusic("src/main/resources/audio/shot.wav", executorService);
		executorService.execute(ss);

	}
	public void level2shot(){

		int randomX = new Random().nextInt(600); // 0부터 599까지의 랜덤한 x좌표 생성
		AttackEntity level2Shot = new AttackEntity(this, "sprites/shot.gif", randomX, 100);
		entities.add(level2Shot);


	}
	public void bossAttack() {

		int randomX = new Random().nextInt(600); // 0부터 599까지의 랜덤한 x좌표 생성
		AttackEntity bossAttack = new AttackEntity(this, "sprites/bossAttack.png", randomX, 100);
		entities.add(bossAttack);
	}
	public void tryToFire2() {
		ShipEntity ship = (ShipEntity) ShipCounter[1];
		if (ship.isDead()) return;
		// check that we have waiting long enough to fire
		if (System.currentTimeMillis() - ship.getFireTime() < ship.getFireRatio()) {
			return;
		}
		// if we waited long enough, create the shot entity, and record the time.
		ship.setFireTime(System.currentTimeMillis());
		ShotEntity shot = new ShotEntity(this, "sprites/shot.gif",ShipCounter[1].getX()+10,ShipCounter[1].getY()-30);
		entities.add(shot);

		ExecutorService executorService = Executors.newSingleThreadExecutor();
		BackgroundMusic ss = new BackgroundMusic("src/main/resources/audio/shot.wav", executorService);
		executorService.execute(ss);
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
		lastLoopTime = SystemTimer.getTime();
		// keep looping round til the game ends
		while (gameRunning) {
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long delta = SystemTimer.getTime() - lastLoopTime;
			//lastLoopTime = SystemTimer.getTime();
			lastLoopTime = SystemTimer.getTime();
		   /*System.out.format("%02d", aMinute); System.out.print(':');
		System.out.format("%02d", aSecond); System.out.print('.');
		System.out.format("%02d%n", tenToHundMillis);*/
			/**
			 * delta could be a second, lastLooptime is 1ms. And its count up about 10ms.
			 * so we will use this class to get a live time ticks up from down 4 numbers of digits
			 * */
			//System.out.println(delta+", "+lastLoopTime);
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
			//Status HUD
			Graphics2D userHUD = (Graphics2D) strategy.getDrawGraphics();
			userHUD.setColor(Color.white);
			userHUD.drawString("Score : "+playBoard.getScore(),(800-g.getFontMetrics().stringWidth("Score : "+killCount))/2,20);
			/*userHUD.drawString(timeStamp,5,580);*/
			// cycle round asking each entity to move itself
			if (!stageRunning)createAliens();
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
					Entity entity = entities.get(i);
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
			}
			else {
				isGameStart = true;
			}
			//timeCalc();
			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			//g.dispose();
			strategy.show();
			// resolve the movement of the ship. First assume the ship
			// isn't moving. If either cursor key is pressed then
			// update the movement appropraitely
			//1P Controlb
			shipControl1();
			//2P control
			shipControl2();
			// if we're pressing fire, attempt to fire
			if (firePressed) {
				tryToFire();
			}
			if (fire2Pressed){
				if(!multiPlay)return;
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

	/*public String giveSurvivalTime() {
		return String.format("%02d", aMinute) + ":" + String.format("%02d", aSecond) + "." + String.format("%02d", tenToHundMillis);
	}*/
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
		/**
		 * The number of key presses we've had while waiting for an "any key" press
		 */
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
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
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
			if (e.getKeyCode() == KeyEvent.VK_W) {
				up2Pressed = true;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
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
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
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
			if (e.getKeyCode() == KeyEvent.VK_W) {
				up2Pressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_S) {
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
	public void keyReverse(boolean whois) {
		Timer timerKeyReverse = new Timer();
		TimerTask task = new TimerTask() {
			long startTime = System.currentTimeMillis();
			long durationtime = System.currentTimeMillis();
			@Override
			public void run() {
				while(durationtime - startTime<5000){
					if(whois)keyP2Reverse=true;
					else keyP1Reverse=true;
					durationtime = System.currentTimeMillis();
				}
				timerKeyReverse.cancel();
				if (whois) keyP2Reverse = false;
				else keyP1Reverse =false;
			}
		};
		timerKeyReverse.schedule(task, 0,10);
	}
	public void shipControl1() {
		ShipEntity ship = (ShipEntity) ShipCounter[0];
		if(keyP1Reverse){
			Lonly = 3; Ronly = 9; Uonly = 6; Donly = 12; LnD = 1; RnD = 11; LnU = 5; RnU = 7;
		}
		else {
			Lonly = 9; Ronly = 3; Uonly = 12; Donly = 6; LnU = 11; LnD = 7; RnU = 1; RnD = 5;
		}
		if (leftPressed && !rightPressed && !upPressed && !downPressed) ship.movingLogic(Lonly);
			//right unique move
		else if (rightPressed && !leftPressed && !upPressed && !downPressed) ship.movingLogic(Ronly);
			//up unique move
		else if (upPressed && !downPressed && !rightPressed && !leftPressed) ship.movingLogic(Uonly);
			//down unique move
		else if (downPressed && !upPressed && !rightPressed && !leftPressed) ship.movingLogic(Donly);
			//left&up degree 45
		else if (leftPressed && upPressed && !rightPressed && !downPressed) ship.movingLogic(LnU);
		else if (leftPressed && downPressed && !rightPressed && !upPressed) ship.movingLogic(LnD);
		else if (rightPressed && upPressed && !downPressed && !leftPressed) ship.movingLogic(RnU);
		else if (rightPressed && downPressed && !upPressed && !leftPressed) ship.movingLogic(RnD);
		else ship.movingLogic(0);
	}
	public int getAlienHoriSpeed(){
		return alienHoriSpeed;
	}
	public void setAlienHoriSpeed(int Tgt){
		alienHoriSpeed=Tgt;
	}
	public int getAlienVertSpeed(){
		return alienVertSpeed;
	}
	public void setAlienVertSpeed(int Tgt){
		alienVertSpeed=Tgt;
	}
	public void alienInvasion() {
		int originHoriSpeed = getAlienHoriSpeed();
		int originVertSpeed = getAlienVertSpeed();
		long startTimer = SystemTimer.getTime();
		Timer timerInvasion = new Timer();
		int targetHoriSpeed = originHoriSpeed * 2;
		int targetVertSpeed = originVertSpeed * 2;
		TimerTask taskInvasion = new TimerTask() {

			long durationTimer = SystemTimer.getTime();
			@Override
			public void run() {
				while (durationTimer - startTimer < 5000) {
					setAlienVertSpeed(targetHoriSpeed);
					setAlienVertSpeed(targetVertSpeed);
					durationTimer = SystemTimer.getTime();
				}
			}
		};
		timerInvasion.schedule(taskInvasion, 0,500);
		setAlienVertSpeed(originHoriSpeed);
		setAlienVertSpeed(originVertSpeed);
	}
	public void shipControl2() {
		if(!multiPlay)return;
		if(keyP2Reverse){
			Lonly = 3; Ronly = 9; Uonly = 6; Donly = 12; LnD = 1; RnD = 11; LnU = 5; RnU = 7;
		}
		else {
			Lonly = 9; Ronly = 3; Uonly = 12; Donly = 6; LnU = 11; LnD = 7; RnU = 1; RnD = 5;
		}
		ShipEntity ship = (ShipEntity) ShipCounter[1];
		if (left2Pressed && !right2Pressed && !up2Pressed && !down2Pressed) ship.movingLogic(Lonly);
			//right unique move
		else if (right2Pressed && !left2Pressed && !up2Pressed && !down2Pressed) ship.movingLogic(Ronly);
			//up unique move
		else if (up2Pressed && !down2Pressed && !right2Pressed && !left2Pressed) ship.movingLogic(Uonly);
			//down unique move
		else if (down2Pressed && !up2Pressed && !right2Pressed && !left2Pressed) ship.movingLogic(Donly);
			//left&up degree 45
		else if (left2Pressed && up2Pressed && !right2Pressed && !down2Pressed) ship.movingLogic(LnU);
		else if (left2Pressed && down2Pressed && !right2Pressed && !up2Pressed) ship.movingLogic(LnD);
		else if (right2Pressed && up2Pressed && !down2Pressed && !left2Pressed) ship.movingLogic(RnU);
		else if (right2Pressed && down2Pressed && !up2Pressed && !left2Pressed) ship.movingLogic(RnD);
		else ship.movingLogic(0);
	}
	/*public static void main(String[] args) {
		Game g = new Game("1p");
		g.gameLoop();
	}*/
	//추후 사용할 경우 재정비 필요. 현재 화면 HUD불가.
	/**public void timeCalc(){//time is spent even not started
	 if(goGo==false)return;
	 else if(player1Dead && player2Dead)return;
	 tenToHundMillis = (int) ((lastLoopTime - initTime) / 10 % 100);//default time duration
	 aSecond = (int) lastLoopTime / 1000 % 60;
	 aMinute = (int) lastLoopTime / 60000 % 60;

	 timeStamp = String.format("%02d", aMinute) + ":" + String.format("%02d", aSecond) + "." + String.format("%02d", tenToHundMillis);*/

}

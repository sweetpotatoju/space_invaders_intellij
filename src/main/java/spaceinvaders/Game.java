package spaceinvaders;

import spaceinvaders.entity.*;

import javax.swing.*;
import java.util.List;
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
	 * This variable will be used in exit command.(For expiring game loop)
	 * */
	private final boolean gameExit=false;
	/**
	 * The list of all the entities that exist in our game
	 */
	private final List<Entity> entities = new ArrayList<>();
	/**
	 * The list of entities that need to be removed from the game this loop
	 */
	private final List<Entity> removeList = new ArrayList<Entity>();
	private GameTimer gameTimer;
	private TimerTask shipCtrl, genTask;
	/** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	private boolean stageRunning = false;
	/** True if the left cursor key is currently pressed */
	private Entity[] ShipCounter = new ShipEntity[2]; private boolean multiPlay;
	private final int uOnly = 1;
	private final int dOnly = 2;
	private final int lOnly = 4;
	private final int rOnly = 8;
	private final int fOnly = 16;
	private int keyVal;
	private boolean leftPressed,left2Pressed, rightPressed, right2Pressed;
	private boolean upPressed, up2Pressed, downPressed, down2Pressed;
	private final int defaultPlayerLife=3, defaultBossLife=50;
	private boolean firePressed, fire2Pressed;
	/** True if game logic needs to be applied this loop, normally as a result of a game event */
	private boolean logicRequiredThisLoop = false;
	/**
	 * The last time at which we recorded the frame rate
	 */
	private long lastFpsTime;
	/**
	 * The current number of frames recorded
	 */
	private int fps;
	/**
	 * The normal title of the game window
	 */
	private final String windowTitle = "Space Invaders 102";
	/**
	 * The game window that we'll update with the frame count
	 */
	private final JFrame container;
	private int level;
	private int killCount, liveCount, alienCount, ptIdx;
	private static String bestScore = "";
	private FirebaseTool firebaseTool;

	private GlobalStorage globalStorage;
	private long lastLoopTime;
	private RecordRecorder playBoard = new RecordRecorder();
	private JLabel backLabel;
	private Graphics2D userHUD;
	private Image image;

	/**
	 * Construct our game and set it running.
	 */
	public Game(String option) {
		ExecutorService executorService = Executors.newSingleThreadExecutor();
		BackgroundMusic bgm = new BackgroundMusic("src/main/resources/audio/backgroundmusic.wav", executorService);
		executorService.execute(bgm);
		if (option.equals("2p")) {multiPlay = true; System.out.println("2p");}
		else {multiPlay = false; System.out.println("1p");}
		gameTimer = new GameTimer();
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
		playBoard.scoreInit();

		this.keyVal = 0;
		this.level = 1;
		this.alienCount = 0;
		this.killCount = 0;
		this.liveCount = 0;
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
		//윈도우랑 게임창 노래 겹쳐들림
//		new BackgroundMusic();

		JButton home = new JButton("HOME");
		home.setBounds(0,0,80,30);
		home.setBackground(Color.WHITE);

	}
	private void initEntities() {
		if (multiPlay){
			ShipCounter[0] = new ShipEntity(this, "sprites/ship1p.png",350, 550, defaultPlayerLife,false);
			addEntity(ShipCounter[0]);
			ShipCounter[1] = new ShipEntity(this, "sprites/ship2p.png",390, 550, defaultPlayerLife,true);
			addEntity(ShipCounter[1]);
		} else{
			ShipCounter[0] = new ShipEntity(this, "sprites/ship1p.png",370, 550, defaultPlayerLife,false);
			addEntity(ShipCounter[0]);
		}
		shipControl();
		message ="When you're ready, please press the button!";
	}

	public void repeatGame(){
		//need to fix with cycle increasement. it just loop with only e
		if(level==4){
			AlienEntity.setAlienHMovement(AlienEntity.getAlienHMovement()*1.1);
			AlienEntity.setAlienVMovement((AlienEntity.getAlienVMovement()*1.1));
			level=1;
		} else{
			AlienEntity.setAlienHMovement(75);
			AlienEntity.setAlienVMovement(10);
			setKillCount(0);
			level=1;
		}
		setLiveCount(0);
	}

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
			waitingForKeyPress = true;
			stageRunning=false;
			resultSender(Integer.toString(playBoard.getScore()));
			playBoard.scoreInit();
			repeatGame();
		} else{
			message = "Oh no! They got you, try again?";
			waitingForKeyPress = true;
			stageRunning=false;
			playBoard.scoreInit();
			repeatGame();
		}
	}

	/**
	 * Notification that the player has won since all the aliens
	 * are dead.
	 */
	public void notifyWin() {
		level++;
		setLiveCount(0);
		message = "level" + level;
		waitingForKeyPress = true;
		stageRunning = false;
		if (level == 4) repeatGame();
	}

	public void notifyAlienKilled(Entity other, int score) {
		// reduce the alient count, if there are none left, the player has won!
		addKillCount();
		subLiveCount();
		System.out.println("kill :"+getKillCount()+" live : "+getLiveCount()+" alien : "+alienCount);
		playBoard.scoreModeAdd(score);
		itemDrop(other.getX(), other.getY());
		System.out.println(getKillCount());
		if (getLevel() == 2&& getKillCount()%2 == 0 && getKillCount() != 0) {
			level2Shot();
		}
		if(getLiveCount()==0&&gameTimer.getTask(genTask)==null)notifyWin();
	}

	public void level2Shot(){

		int randomX = new Random().nextInt(600); // 0부터 599까지의 랜덤한 x좌표 생성
		AttackEntity level2Shot = new AttackEntity(this, "sprites/shot.gif", randomX, 100);
		addEntity(level2Shot);
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
		while (!gameExit) {
			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long delta = SystemTimer.getTime() - lastLoopTime;
			lastLoopTime = SystemTimer.getTime();
			/**
			 * delta could be a second, lastLooptime is 1ms. And its count up about 10ms.
			 * so we will use this class to get a live time ticks up from down 4 numbers of digits
			 * */
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
			userHUD.drawString("Score: " + playBoard.getScore(), 720, 20);
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
			} else {
				//waitingForKeyPress가 거짓이라 이건 루프문 동안 계속 접근될거임.
				createAliens();
				stageRunning=true;
			}
			SystemTimer.sleep(lastLoopTime+10-SystemTimer.getTime());
			strategy.show();
			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			//g.dispose();
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

	public void shipControl() {
		if(gameTimer.getTask(shipCtrl)!=null);
		shipCtrl = new TimerTask() {
			@Override
			public void run() {
				if(waitingForKeyPress)return;
				if(multiPlay){
					keyVal = (left2Pressed?lOnly:0)+(right2Pressed?rOnly:0)+(up2Pressed?uOnly:0)+(down2Pressed?dOnly:0)+(fire2Pressed?fOnly:0);
					((ShipEntity)ShipCounter[1]).movingLogic(keyVal);
				}
				keyVal = (leftPressed?lOnly:0)+(rightPressed?rOnly:0)+(upPressed?uOnly:0)+(downPressed?dOnly:0)+(firePressed?fOnly:0);
				((ShipEntity)ShipCounter[0]).movingLogic(keyVal);
			}
		};
		gameTimer.addTask(shipCtrl,0,5);
	}

	public void resultSender(String result){
		firebaseTool.setUserBestScore(globalStorage.getUserID(), result);
		globalStorage.setUserBestScore(result); // 베스트 스코어 업데이트
	}

	public void itemDrop(int x, int y){
		if (getKillCount()%3 == 0 && getKillCount()/3 >= 1){
			addEntity(new ItemEntity("sprites/itemBox.png",this,x,y));
		}
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
	public int getLevel(){return level;}

	public void addTask(TimerTask task,long delay,long period){
		gameTimer.addTask(task,delay,period);
	}

	public void removeTask(TimerTask task){
		gameTimer.removeTask(task);
	}

	public TimerTask isTaskExist(TimerTask task){return gameTimer.getTask(task);}//if its not, it will return correct task.

	private void createAliens() {
		if (stageRunning) return;
		if (gameTimer.getTask(genTask) != null) return;
		//int ptIdx = 0;
		System.out.println("level " + level + " intro");
		if (level == 3) {
			alienCount = 1;
			BossEntity boss = new BossEntity(this, 370, 50,defaultBossLife);
			addEntity(boss);
			addLiveCount();
		}
		else {
			alienCount = 10 + (level - 1) * 2;
			Entity[] alien = new Entity[alienCount];
			ptIdx = 0;
			int alienWidth = 50; // width of each alien
			int alienHeight = 30; // height of each alien
			int minY = 10; // minimum y-coordinate
			int maxY = 200; // maximum y-coordinate
			final Set<Point> points = new HashSet<>(); // set to keep track of the generated points
			Random random = new Random();
			Point[] pointArray = new Point[0];
			while (points.size() < alienCount) {
				int x = random.nextInt(getWidth() - alienWidth);
				int y = random.nextInt(maxY - minY) + minY;
				// check if the new point overlaps with any existing points
				boolean overlapping = false;
				if (level == 3) {
					break;
				}
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
				pointArray = points.toArray(new Point[0]);
			}
			for(Point point : points){
				alien[ptIdx] = new AlienEntity(this, (int) pointArray[ptIdx].getX(), (int) pointArray[ptIdx++].getY());
			}
			genTask = new TimerTask() {
				int tmpIdx = 0;
				@Override
				public void run() {
					System.out.println("level " + level + "sponed : " + ((tmpIdx) + 1));
					addEntity(alien[tmpIdx]);
					addLiveCount();
					if (++tmpIdx == alienCount) {
						System.out.println("Lv" + level + " All Sponed");
						gameTimer.removeTask(genTask);
					}
				}
			};
			gameTimer.addTask(genTask, 0, 1000);
		}
	}
	public void addLiveCount(){liveCount++;}
	public void subLiveCount(){liveCount--;}
	public void setLiveCount(int set){liveCount = set;}
	public int getLiveCount(){return liveCount;}
	public void addKillCount(){killCount++;}
	public void setKillCount(int set){killCount = set;}
	public int getKillCount(){return killCount;}
	public void updateLogic() {
		logicRequiredThisLoop = true;
	}
}

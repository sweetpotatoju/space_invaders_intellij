package spaceinvaders;

import com.google.firebase.database.*;
import com.google.firebase.internal.NonNull;
import spaceinvaders.entity.*;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.*;


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
	private final boolean gameRunning = true;
	/**
	 * The list of all the entities that exist in our game
	 */
	private final ArrayList<Entity> entities = new ArrayList<>();
	/**
	 * The list of entities that need to be removed from the game this loop
	 */
	private final ArrayList<Entity> removeList = new ArrayList<Entity>();
	/** The number of aliens left on the screen */
	private int alienCount;
	/** The message to display which waiting for a key press */
	private String message = "";
	/** True if we're holding up game play until a key has been pressed */
	private boolean waitingForKeyPress = true;
	/** True if the left cursor key is currently pressed */
	private Entity[] ShipCounter = new ShipEntity[2]; private boolean multiPlay, loopMode;
	private boolean leftPressed; private boolean left2Pressed;
	private boolean rightPressed; private boolean right2Pressed;
	private boolean upPressed; private boolean up2Pressed;
	private boolean downPressed; private boolean down2Pressed;
	private boolean firePressed; private boolean fire2Pressed;
	private boolean player1Dead, player2Dead;			private boolean goGo = false;
	private long lastFire = 0; private long last2Fire = 0;
	private long level2lastFire = 0; private long level2ShotInterval = 0;
	private long bossfire= 0; private long bossShotInterval= 0;
	private long firingInterval = 500; private long firing2Interval = 500;
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
	private Thread timeCounterThread;
	private int level = 1;
	private Timer timer;
	private int killCount;
	private static User User;
	private static String bestScore = "";
	private FirebaseTool firebaseTool;

	private GlobalStorage globalStorage;

	private JLabel backgroundMap;

	private Clip clip;

	//Record Variables
	private int tenToHundMillis, aSecond, aMinute;
	/** timedisplay */
	private String timeStamp="";
	private long lastLoopTime; private long initTime;
	private RecordRecorder playBoard = new RecordRecorder(this);

	private int levelSpeedControl=75;
	private JLabel backLabel;

	/**
	 * Construct our game and set it running.
	 */
	public Game(String option) {
		cycle = 0;
		if (option.equals("2p")) multiPlay = true;
		else if (option.equals("1p")) multiPlay = false;
		else if (option.equals("loopMode")) {multiPlay =false; loopMode = true;}
		// create a frame to contain our game
		container = new JFrame("Space Invaders 102");
		// get hold the content of the frame and set up the resolution of the game
		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(800,600));
		panel.setLayout(null);

		// Add background image
		ImageIcon backgroundImage = new ImageIcon("sprites/rankingPage.png");
		JLabel background = new JLabel(backgroundImage);
		container.add(background,BorderLayout.CENTER);



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
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// initialise the entities in our game so there's something
		// to see at startup
		if (option.equals("2p")) {
			System.out.println("2p");
		}



		firebaseTool = FirebaseTool.getInstance();
		globalStorage = GlobalStorage.getInstance();

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

	}


	/**
	 * Initialise the starting state of the entities (ship and aliens). Each
	 * entitiy will be added to the overall list of entities in the game.
	 */
//	private void initEntities() {
//		// create the player ship and place it roughly in the center of the screen
//		ship = new ShipEntity(this, "sprites/ship.gif",370,550);
//		entities.add(ship);
//		entities.add(ship2);
//		int idx = 20;
//		for (Entity Life : LifeCounter){
//			if (idx > 60) {
//				Life = new LifeEntity(this, 655+idx, 580);
//				LifeCounter[idx/20 - 1] = Life;
//			}
//			else {
//				Life = new LifeEntity(this, idx-15, 580);
//				LifeCounter[idx/20 - 1] = Life;
//			}
//			entities.add(Life);
//			idx+=20;
//		}
//
//		/** 아래 함수에 int 중복선언하고 나서, 값 할당이 initGame로컬변수 취급받다보니 중괄호 범위 넘어간 이후로 값이 틀어지는것 같습니다.
//		 *
//		 *
//		 *
//		 *
//		 * */
//		alienCount = 3;
//
//		int alienCount = 50; // number of aliens
//		int alienWidth = 50; // width of each alien
//		int alienHeight = 30; // height of each alien
//		int minY = 10; // minimum y-coordinate
//		int maxY = 200; // maximum y-coordinate
//		int delay = 1000; // time delay between each batch of aliens (in milliseconds)
//
//		final Set<Point> points = new HashSet<>(); // set to keep track of the generated points
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
		if (multiPlay){
			ShipCounter[0] = new ShipEntity(this, "sprites/ship1p.png",350, 550, false);
			entities.add(ShipCounter[0]);
			ShipCounter[1] = new ShipEntity(this, "sprites/ship2p.png",390, 550, true);
			addEntity(ShipCounter[1]);
		}
		else{
			ShipCounter[0] = new ShipEntity(this, "sprites/ship2p.png",370, 550, false);
			entities.add(ShipCounter[0]);
		}
		killCount = 0;

		// create the aliens

		message ="When you're ready, please press the button!";
		createAliens();

	}

	private void createAliens() {
		// determine the parameters for the aliens based on the current level// increase the number of aliens by 2 for each level
		alienCount = 10  + (level - 1) * 2;// increase the number of aliens by 2 for each level11
		int killCount = 0;
		int alienWidth = 50; // width of each alien
		int alienHeight = 30; // height of each alien
		int minY = 10; // minimum y-coordinate
		int maxY = 200; // maximum y-coordinate
		int delay = 1000; // time delay between each batch of aliens (in milliseconds)

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
		if (level == 1) {
			timer = new Timer(delay, new ActionListener() {
				int count = 0;

					@Override
					public void actionPerformed(ActionEvent e) {

					if (isGameStart) {
						/**
						 * this section is added by jgs
						 * */
						initTime=lastLoopTime;
						aMinute=0; aSecond=0; tenToHundMillis=0;
						goGo=true;


						if (count < alienCount) {
							Point[] pointArray = points.toArray(new Point[0]); // convert set to array
							Entity alien = new AlienEntity(Game.this, pointArray[count].x, pointArray[count].y,getAlienSpeed());
							entities.add(alien);
							count +=2 ; // increase count by 2 to prevent two aliens being added at once
						}
					} else {
						timer.stop(); // stop the timer when the game is over
					}
				}
			});
		} else if (level == 2) {

			timer = new Timer(delay, new ActionListener() {
				int count = 0;

					@Override
					public void actionPerformed(ActionEvent e) {

					if (isGameStart) {

						if (count < alienCount) {
							timer.setInitialDelay(1000);
							timer.setDelay(1000);
							timer.start();
							Point[] pointArray = points.toArray(new Point[0]); // convert set to array
							Entity alien = new level2alienEntity(Game.this, pointArray[count].x, pointArray[count].y);
							entities.add(alien);
							count++; // increase count by 2 to prevent two aliens being added at once
						}
					} else {
						timer.stop(); // stop the timer when the game is over
					}
				}
			});
		} else if (level == 3) {
			alienCount = 100;
			bosseEntity boss = new bosseEntity(this, "sprites/boss2.png", getWidth() / 2, 50);
			entities.add(boss);
		}



		timer.setInitialDelay(1000);
		timer.setDelay(1000);
		timer.start();
	}


	// start timer }
//             * Notification from a game entity that the logic of the game
//             * should be run at the next opportunity (normally as a result of some
//             * game event)
//             */

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
		message = "Oh no! They got you, try again?";
		waitingForKeyPress = true;
		isGameStart = false;
		//for checking println
		playBoard.printRecord();
		if (killCount > Integer.parseInt(globalStorage.getUserBestScore())) {
			message = "Oh no!, but  New best score!";
			resultSender(Integer.toString(killCount));
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
		if (level == 4 && loopMode){ level = 1; ++cycle; setAlienSpeed(); createAliens();}
		if (level == 4 && killCount > Integer.parseInt(globalStorage.getUserBestScore())) {
			message = "mission complete! New best score!";
			waitingForKeyPress = true;
			isGameStart = false;
			resultSender(Integer.toString(killCount));
		} else if (level == 4) {
			message = "mission complete";
			waitingForKeyPress = true;
			isGameStart = false;
		}


	}
	public void resultSender(String result){
		firebaseTool.setUserBestScore(globalStorage.getUserID(), result);
		globalStorage.setUserBestScore(result); // 베스트 스코어 업데이트
	}


		/**
		 * Notification that an alien has been killed
		 */
//	public void notifyAlienKilled() {
//		// reduce the alient count, if there are none left, the player has won!
//		alienCount--;
//		System.out.println("notifyAlienKilled() called, alienCount: " + alienCount);
//
//		if (alienCount == 0) {
//			notifyWin();
//		}
	public void itemDrop(int x, int y){
		if (killCount%3 == 0 && killCount/3 >= 1){
			addEntity(new ItemEntity(this,x,y));
		}

	}

	public void notifyAlienKilled(Entity other) {
		// reduce the alient count, if there are none left, the player has won!
		alienCount--;
		killCount++;
		itemDrop(other.getX(), other.getY());
		System.out.println(killCount);


			if (level == 1) {
				if (alienCount == 2) {
					notifyWin();
				}
			} else if (level == 2) {
				if(alienCount%2 == 0){
					level2shot();
				}
				if (alienCount == 2) {
					notifyWin();
				}
			} else if (level == 3) {
				if(alienCount%5 ==0){
					bossAttack();
				}
				if (alienCount == 0) {
					notifyWin();
				}
			}


//
			// if there are still some aliens left then they all need to get faster, so
			// speed up all the existing aliens
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);// 게임의 상태 확인 엔티티
				if (level == 1) {
					if (entity instanceof AlienEntity) {
						// speed up by 2%
						entity.setHorizontalMovement(entity.getHorizontalMovement() * 1.00);
					}
				} else if (level == 2) {
					if (entity instanceof AlienEntity) {
						// speed up by 2%
						entity.setHorizontalMovement(entity.getHorizontalMovement());


					}
				}
			}
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = (Entity) entities.get(i);// 게임의 상태 확인 엔티티
				if (level == 1) {
					if (entity instanceof level2alienEntity) {
						// speed up by 2%
						entity.setHorizontalMovement(entity.getHorizontalMovement());
					}
				} else if (level == 2) {
					if (entity instanceof level2alienEntity) {
						// speed up by 2%
						entity.setHorizontalMovement(entity.getHorizontalMovement());
					}
				}
			}

//
			// if there are still some aliens left then they all need to get faster, so
			// speed up all the existing aliens
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);// 게임의 상태 확인 엔티티
				if (entity instanceof AlienEntity) {
					// speed up by 2%
					entity.setHorizontalMovement(entity.getHorizontalMovement());
					if (level == 1) {
						if (entity instanceof AlienEntity) {
							// speed up by 2%
							entity.setHorizontalMovement(entity.getHorizontalMovement());
						}
					} else if (level == 2) {
						if (entity instanceof AlienEntity) {
							// speed up by 2%
							entity.setHorizontalMovement(entity.getHorizontalMovement());


						}
					}
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
			/*try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/audio/shot.wav"));
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.setFramePosition(0);
				//볼륨조정
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-20.0f);
				clip.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}*/
		}

		public void level2shot(){

			int randomX = new Random().nextInt(600); // 0부터 599까지의 랜덤한 x좌표 생성
			bossacttackentity bossshot = new bossacttackentity(this, "sprites/shot.gif", randomX, 100);
			entities.add(bossshot);

			try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/audio/shot.wav"));
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.setFramePosition(0);
				//볼륨조정
				FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(-20.0f);
				clip.start();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	public void bossAttack() {

		int randomX = new Random().nextInt(600); // 0부터 599까지의 랜덤한 x좌표 생성
		bossacttackentity bossshot = new bossacttackentity(this, "sprites/bossShot.png", randomX, 100);
		entities.add(bossshot);
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
			userHUD.drawString("Score : "+killCount,(800-g.getFontMetrics().stringWidth("Score : "+killCount))/2,20);
			userHUD.drawString(timeStamp,5,580);
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
				isGameStart = true;
			}
			timeCalc();
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

	public String giveSurvivalTime() {
		return String.format("%02d", aMinute) + ":" + String.format("%02d", aSecond) + "." + String.format("%02d", tenToHundMillis);
	}
	public int giveKillScore(){
		return killCount;
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
	public void shipControl1() {
		ShipEntity ship = (ShipEntity) ShipCounter[0];
		if (leftPressed && !rightPressed && !upPressed && !downPressed) ship.movingLogic(9);
			//right unique move
		else if (rightPressed && !leftPressed && !upPressed && !downPressed) ship.movingLogic(3);
			//up unique move
		else if (upPressed && !downPressed && !rightPressed && !leftPressed) ship.movingLogic(12);
			//down unique move
		else if (downPressed && !upPressed && !rightPressed && !leftPressed) ship.movingLogic(6);
			//left&up degree 45
		else if (leftPressed && upPressed && !rightPressed && !downPressed) ship.movingLogic(11);
		else if (leftPressed && downPressed && !rightPressed && !upPressed) ship.movingLogic(7);
		else if (rightPressed && upPressed && !downPressed && !leftPressed) ship.movingLogic(1);
		else if (rightPressed && downPressed && !upPressed && !leftPressed) ship.movingLogic(5);
		else ship.movingLogic(0);
	}
	public int getAlienSpeed(){
		return levelSpeedControl;
	}
	public void setAlienSpeed(){
		levelSpeedControl*=1.02;
	}

	public void shipControl2() {
		if(!multiPlay)return;
		ShipEntity ship = (ShipEntity) ShipCounter[1];
		if (left2Pressed && !right2Pressed && !up2Pressed && !down2Pressed) ship.movingLogic(9);
		//right unique move
		else if (right2Pressed && !left2Pressed && !up2Pressed && !down2Pressed) ship.movingLogic(3);
		//up unique move
		else if (up2Pressed && !down2Pressed && !right2Pressed && !left2Pressed) ship.movingLogic(12);
		//down unique move
		else if (down2Pressed && !up2Pressed && !right2Pressed && !left2Pressed) ship.movingLogic(6);
		//left&up degree 45
		else if (left2Pressed && up2Pressed && !right2Pressed && !down2Pressed) ship.movingLogic(11);
		else if (left2Pressed && down2Pressed && !right2Pressed && !up2Pressed) ship.movingLogic(7);
		else if (right2Pressed && up2Pressed && !down2Pressed && !left2Pressed) ship.movingLogic(1);
		else if (right2Pressed && down2Pressed && !up2Pressed && !left2Pressed) ship.movingLogic(5);
		else ship.movingLogic(0);
	}
	/*public void shipSpeedControl(Entity entity, long SpeedTgt){
		entity.setMoveSpeed(SpeedTgt);
	}*/
	public void shipFireRatioControl(ShipEntity ship, long RatioTgt){
		ship.setFireRatio(RatioTgt);
	}

	public void timeCalc(){//time is spent even not started
		if(goGo==false)return;
		else if(player1Dead && player2Dead)return;
		tenToHundMillis = (int) ((lastLoopTime - initTime) / 10 % 100);//default time duration
		aSecond = (int) lastLoopTime / 1000 % 60;
		aMinute = (int) lastLoopTime / 60000 % 60;
		timeStamp = String.format("%02d", aMinute) + ":" + String.format("%02d", aSecond) + "." + String.format("%02d", tenToHundMillis);
	}

	/**
	 * The entry point into the game. We'll simply create an
	 * instance of class which will start the display and game
	 * loop.
	 *
	 * @param argv The arguments that are passed into our game
	 */
//	public static void main(String[] argv) {
//		Game g = new Game("");
//		// Start the main game loop, note: this method will not
//		// return until the game has finished running. Hence we are
//		// using the actual main thread to run the game.
//		g.gameLoop();
//	}
}

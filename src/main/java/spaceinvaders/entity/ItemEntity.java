package spaceinvaders.entity;

import spaceinvaders.BackgroundMusic;
import spaceinvaders.Game;
import spaceinvaders.ItemSlotMachine;
import spaceinvaders.Sprite;
import spaceinvaders.SpriteStore;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ItemEntity extends Entity {
    private int moveSpeed = 200;
    private Game game;

    private long lastFrameChange;
    /**
     * The frame duration in milliseconds, i.e. how long any given frame of animation lasts
     */
    private long frameDuration = 100;
    private int frameNumber;
    private Sprite[] frames = new Sprite[5];
    private String[] imgArray = new String[5];

    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param ref The reference to the image to be displayed for this entity
     * @param x   The initial x location of this entity
     * @param y   The initial y location of this entity
     */
    public ItemEntity(Game game, int x, int y) {
        super("sprites/itemBox.png", x, y);
        this.game = game;
        int coinToss = (int) Math.round(Math.random());
        if (coinToss == 0) {
            setHorizontalMovement(moveSpeed);
        } else {
            setHorizontalMovement(-moveSpeed);
        }
        this.setVerticalMovement(moveSpeed);
        this.imgArray[0] = "sprites/itemBox.png";
        this.imgArray[1] = "sprites/heart.GIF";
        this.imgArray[2] = "sprites/accelator.gif";
        this.imgArray[3] = "sprites/keyreverse.png";
        this.imgArray[4] = "sprites/brokenBox.png";
        frames[0] = SpriteStore.get().getSprite(imgArray[0]);
        frames[1] = SpriteStore.get().getSprite(imgArray[1]);
        frames[2] = SpriteStore.get().getSprite(imgArray[2]);
        frames[3] = SpriteStore.get().getSprite(imgArray[3]);
        frames[4] = SpriteStore.get().getSprite(imgArray[4]);
    }

    public void slot(long delta) {
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
    }
    public void move ( long delta){
        if ((dx < 0) && (x < 10)) {
            dx = -dx;
            return;
        }
        if ((dx > 0) && (x > 750)) {
            dx = -dx;
        }
        //y = 10
        if ((dy > 0) && (y > 550)) {
            game.removeEntity(this);
            return;
        }
        slot(delta);
        super.move(delta);
    }
    @Override
    public void collidedWith (Entity other){

        if (other instanceof ShipEntity) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            BackgroundMusic gain = new BackgroundMusic("src/main/resources/audio/itemGain.wav", executorService);
            executorService.execute(gain);
        }
    }
}

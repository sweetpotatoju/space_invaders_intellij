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
    private int moveSpeed=200;
    private Game game;
    private Sprite[] imgArray = new Sprite[6];
    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param ref The reference to the image to be displayed for this entity
     * @param x   The initial x location of this entity
     * @param y   The initial y location of this entity
     */
    public ItemEntity(String ref,Game game, int x, int y) {
        super(ref, x, y);
        this.sprite = SpriteStore.get().getSprite(ref);
        this.game = game;
        this.setHorizontalMovement(moveSpeed);
        this.setVerticalMovement(moveSpeed);
        this.imgArray[0] = SpriteStore.get().getSprite("sprites/itemBox.png");
        this.imgArray[1] = SpriteStore.get().getSprite("sprites/brokenBox.png");
        this.imgArray[2] = SpriteStore.get().getSprite("sprites/heart.GIF");
        this.imgArray[3] = SpriteStore.get().getSprite("sprites/accelator.gif");
        this.imgArray[4] = SpriteStore.get().getSprite("sprites/keyreverse.png");
        this.imgArray[5] = SpriteStore.get().getSprite("sprites/invasion.png");
    }
    public void move(long delta) {
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

        super.move(delta);
    }
    public void showIt(int i){
        this.sprite = imgArray[i];
    }
    @Override
    public void collidedWith(Entity other) {

        if (other instanceof ShipEntity){

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            BackgroundMusic gain = new BackgroundMusic("src/main/resources/audio/itemGain.wav", executorService);
            executorService.execute(gain);
        }

        // 아이템 먹을때 효과음
        /*try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/audio/loseHeart.wav"));
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
}

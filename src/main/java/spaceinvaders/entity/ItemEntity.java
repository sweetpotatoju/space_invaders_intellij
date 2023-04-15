package spaceinvaders.entity;

import spaceinvaders.Game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class ItemEntity extends Entity {
    private int moveSpeed=100;
    private int players;
    private Game game;

    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param ref The reference to the image to be displayed for this entity
     * @param x   The initial x location of this entity
     * @param y   The initial y location of this entity
     */
    public ItemEntity(Game game, int x, int y) {
        super("sprites/heart.gif", x, y);
        this.game = game;
        this.setHorizontalMovement(moveSpeed);
        this.setVerticalMovement(moveSpeed);
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
    @Override
    public void collidedWith(Entity other) {

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

package spaceinvaders.entity;

import spaceinvaders.BackgroundMusic;
import spaceinvaders.Game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LifeCounter {
    private Game game;
    /** This means Entitiy's heart point ex) ship default = 3 */
    private int entityLife=3;//Originally wants to make this active var. but error occurs when init.
    /** This will recognize the player differences */
    /** not use now
    private Entity thatObj; */
    private LifeEntity[] entityLifeArray = new LifeEntity[3];
    /** Should be fixed to access in only entity. And need to remove  ship Entity Var */
    public LifeCounter(Game game, Entity entity, ShipEntity ship){
        //setLife(3); error occured; ArrayIndexOutOfBoundsException, breakPoint 0
        this.game = game;
        int posIdx = 20;
        for (int i = 0; i < entityLife; i++){
            if (ship.is2P()){
                entityLifeArray[i] = new LifeEntity(game, posIdx*(i+1)+715, 580);
            }
            else {
                entityLifeArray[i] = new LifeEntity(game, posIdx*(i+1)-15, 580);
            }
            game.addEntity(entityLifeArray[i]);
        }
    }
    public void setLife(int max){
        entityLife = max;
    }
    public void LifeIncrease(){
        if (getEntityLife()==3) return;
        entityLifeArray[getEntityLife()].onIt();
        entityLife++;
    }
    public void LifeDecrease(){
        if(getEntityLife()==0) return;
        entityLifeArray[getEntityLife()-1].offIt();
        entityLife--;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        BackgroundMusic sound = new BackgroundMusic("src/main/resources/audio/loseHeart.wav", executorService);
        executorService.execute(sound);
//        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/audio/loseHeart.wav"));
//            Clip clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//            clip.setFramePosition(0);
//            //볼륨조정
//            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//            gainControl.setValue(-20.0f);
//            clip.start();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
    }
    public int getEntityLife(){
        return entityLife;
    }
}

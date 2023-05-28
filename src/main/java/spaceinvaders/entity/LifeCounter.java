package spaceinvaders.entity;

import spaceinvaders.BackgroundMusic;
import spaceinvaders.Game;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LifeCounter {
    private Game game;
    /** This means Entitiy's heart point ex) ship default = 3 */
    private int entityLife;//Originally wants to make this active var. but error occurs when init.
    /** This will recognize the player differences */
    /** not use now
    private Entity thatObj; */
    private LifeEntity[] entityLifeArray;
    /** Should be fixed to access in only entity. And need to remove  ship Entity Var */
    LifeCounter(Game game, Entity entity, int lifeNumber){
        //setLife(3); error occured; ArrayIndexOutOfBoundsException, breakPoint 0
        this.game = game;
        int posIdx = 20;
        this.entityLife = lifeNumber;
        this.entityLifeArray = new LifeEntity[lifeNumber];
        for (int i = 0; i < entityLife; i++) {
            if (entity instanceof BossEntity) {
                entityLifeArray[i] = new LifeEntity(game,"sprites/bossHp.png",399-(lifeNumber/2)+i,15,true);
            }
            else {
                if (((ShipEntity)entity).is2P()) {
                    entityLifeArray[i] = new LifeEntity(game,"sprites/heart.gif", posIdx * (i + 1) + 715, 580,false);
                } else {
                    entityLifeArray[i] = new LifeEntity(game,"sprites/heart.gif", posIdx * (i + 1) - 15, 580,false);
                }
            }
            game.addEntity(entityLifeArray[i]);
        }
    }
    void LifeIncrease(){
        if (getEntityLife()==3) return;
        entityLifeArray[getEntityLife()].onIt();
        entityLife++;
    }
    void LifeDecrease(){
        if(getEntityLife()==0) return;
        entityLifeArray[getEntityLife()-1].offIt();
        entityLife--;
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
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        BackgroundMusic sound = new BackgroundMusic("src/main/resources/audio/loseHeart.wav", executorService);
        executorService.execute(sound);
    }
    int getEntityLife(){
        return entityLife;
    }
}

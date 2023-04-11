package spaceinvaders;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Music extends Thread{
        public Music(){
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/music/backgroundmusic.wav"));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                //소리설정
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

                //볼륨조정
                gainControl.setValue(-30.0f);

                clip.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        }
}

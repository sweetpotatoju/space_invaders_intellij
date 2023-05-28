package spaceinvaders;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundMusic implements Runnable {
    private String filepath;
    private Executor executor;
    private Clip clip;
    private  long stopTime = 0;
    public boolean isPlaying = false;


    public BackgroundMusic(String filepath, Executor executor) {
        this.filepath = filepath;
        this.executor = executor;
    }

    @Override
    public void run() {
        executor.execute(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                //소리설정
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                //볼륨조정
                gainControl.setValue(-15.0f);
                clip.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void stop() {
        clip.stop();
        clip.close();
    }

}
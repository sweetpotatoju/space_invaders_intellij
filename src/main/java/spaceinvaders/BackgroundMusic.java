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

    public BackgroundMusic(String filepath, Executor executor) {
        this.filepath = filepath;
        this.executor = executor;
    }

    @Override
    public void run() {
        executor.execute(() -> {
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filepath));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
                System.out.println("음악 재생 시작");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
    }
}













//package spaceinvaders;
//
//import javax.sound.sampled.*;
//import java.io.File;
//import java.io.IOException;
//
//public class BackgroundMusic {
//    public BackgroundMusic(){
//        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/audio/backgroundmusic.wav"));
//            Clip clip = AudioSystem.getClip();
//            clip.open(audioInputStream);
//
//            //소리설정
//            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
//
//            //볼륨조정
//            gainControl.setValue(-20.0f);
//
//            clip.start();
//
//            System.out.println("노래 시작");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        } catch (UnsupportedAudioFileException e) {
//            throw new RuntimeException(e);
//        } catch (LineUnavailableException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static void pause() {
//        Clip clip = null;
//        try {
//            clip = AudioSystem.getClip();
//        } catch (LineUnavailableException e) {
//            throw new RuntimeException(e);
//        }
//        clip.stop();
//    }
//}

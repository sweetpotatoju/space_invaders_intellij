package spaceinvaders;

import javax.swing.*;
import java.awt.*;

public class TimerBar extends JLabel implements Runnable {
    int width = 450;
    int height = 50;
    int x = 300;
    int y = 500;
    Color color = new Color(255,0,0);

    int second;
    public TimerBar(){
        setBackground(color);
        setOpaque(true);
        setBounds(x,y,width,height);

        this.second = second;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000/(width/second));
            } catch (Exception e){
                e.printStackTrace();
            }

            if (getWidth() > 0){
                width -= 1;
                setBounds(x,y,width,height);
            }
            else {
                break;
            }
        }
    }
}
